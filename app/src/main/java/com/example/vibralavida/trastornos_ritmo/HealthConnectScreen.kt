package com.example.vibralavida.trastornos_ritmo
import com.example.vibralavida.backgroundGradient

// ============================================================================
// ANDROID
// ============================================================================

import android.content.Context

// ============================================================================
// ACTIVITY RESULT / HEALTH CONNECT
// ============================================================================

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.PermissionController

// ============================================================================
// COMPOSE - FOUNDATION
// ============================================================================

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll

// ============================================================================
// MATERIAL 3
// ============================================================================

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text

// ============================================================================
// COMPOSE - ESTADOS
// ============================================================================

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue

// ============================================================================
// UI
// ============================================================================

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ============================================================================
// CORRUTINAS
// ============================================================================

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// ============================================================================
// FECHA Y HORA
// ============================================================================

import java.time.ZoneId
import java.time.format.DateTimeFormatter


// ============================================================================
// ESTADOS DE SEMAFORIZACIÓN
// ============================================================================

/**
 * Estados visuales que utilizaremos para la semaforización.
 */
enum class HealthTrafficLight {

    // Estado favorable.
    GOOD,

    // Estado intermedio.
    REGULAR,

    // Estado que requiere atención.
    LOW
}


// ============================================================================
// PANTALLA PRINCIPAL
// ============================================================================

/**
 * Pantalla de monitoreo de Trastornos del ritmo.
 *
 * IMPORTANTE:
 *
 * Conservamos el nombre HealthConnectScreen porque MainActivity
 * ya navega correctamente hacia esta función.
 *
 * Esta pantalla muestra:
 *
 * - Estado de conexión con Health Connect.
 * - Pasos realizados durante el día.
 * - Meta de 10,000 pasos.
 * - Semaforización de actividad física.
 * - Frecuencia cardíaca del día.
 * - Gráfica interactiva.
 * - Valor mínimo.
 * - Valor máximo.
 * - Selección de puntos tocando o arrastrando.
 *
 * El sueño ya NO se muestra aquí.
 * Posteriormente lo mostraremos en Hábitos saludables.
 */
@Composable
fun HealthConnectScreen(
    onBackToMenu: () -> Unit
) {

    // Contexto actual.
    val context =
        LocalContext.current


    // ------------------------------------------------------------------------
    // MANAGER DE HEALTH CONNECT
    // ------------------------------------------------------------------------

    val healthConnectManager =
        remember {

            HealthConnectManager(
                context
            )
        }


    // ------------------------------------------------------------------------
    // CORRUTINA
    // ------------------------------------------------------------------------

    val coroutineScope =
        rememberCoroutineScope()


    // ------------------------------------------------------------------------
    // SHARED PREFERENCES
    // ------------------------------------------------------------------------

    val preferences =
        remember {

            context.getSharedPreferences(
                "health_connect_preferences",
                Context.MODE_PRIVATE
            )
        }


    // ========================================================================
    // ESTADOS
    // ========================================================================

    // Indica si tenemos permisos.
    var hasPermissions by
    remember {

        mutableStateOf(false)
    }


    // Mensaje de estado.
    var message by
    remember {

        mutableStateOf(
            "Revisando conexión con Health Connect..."
        )
    }


    // Indica si estamos leyendo datos.
    var isLoading by
    remember {

        mutableStateOf(false)
    }


    // Pasos de hoy.
    var todaySteps by
    remember {

        mutableStateOf<Long?>(null)
    }


    // Todas las mediciones cardíacas del día.
    var heartRatePoints by
    remember {

        mutableStateOf<List<HeartRatePoint>>(
            emptyList()
        )
    }


    // ========================================================================
    // PERMISOS
    // ========================================================================

    val permissionLauncher =
        rememberLauncherForActivityResult(

            contract =
                PermissionController
                    .createRequestPermissionResultContract()

        ) {

            coroutineScope.launch {

                hasPermissions =
                    healthConnectManager
                        .hasAllPermissions()


                if (hasPermissions) {

                    message =
                        "Health Connect conectado correctamente."

                } else {

                    message =
                        "Faltan permisos para consultar tus datos."
                }
            }
        }


    // ========================================================================
    // LEER DATOS
    // ========================================================================

    /**
     * Consulta los datos necesarios para esta pantalla.
     *
     * Solamente necesitamos:
     *
     * - Pasos de hoy.
     * - Frecuencia cardíaca de hoy.
     */
    suspend fun readHealthData() {

        try {

            isLoading =
                true


            hasPermissions =
                healthConnectManager
                    .hasAllPermissions()


            if (!hasPermissions) {

                message =
                    "Primero concede los permisos de Health Connect."

                isLoading =
                    false

                return
            }


            // ---------------------------------------------------------------
            // PASOS DE HOY
            // ---------------------------------------------------------------

            todaySteps =
                healthConnectManager
                    .readTodaySteps()


            // ---------------------------------------------------------------
            // FRECUENCIA CARDÍACA DEL DÍA
            // ---------------------------------------------------------------

            heartRatePoints =
                healthConnectManager
                    .readTodayHeartRatePoints()


            message =
                "Datos actualizados correctamente."


        } catch (e: Exception) {

            message =
                "Error al leer datos: ${e.message}"

        } finally {

            isLoading =
                false
        }
    }


    // ========================================================================
    // PRIMERA APERTURA
    // ========================================================================

    LaunchedEffect(Unit) {

        val status =
            healthConnectManager
                .getAvailabilityStatus()


        if (
            status !=
            HealthConnectClient.SDK_AVAILABLE
        ) {

            message =
                "Health Connect no está disponible o necesita actualizarse."

            return@LaunchedEffect
        }


        hasPermissions =
            healthConnectManager
                .hasAllPermissions()


        val alreadyAskedAutomatically =
            preferences.getBoolean(

                "asked_health_permissions_automatically",

                false
            )


        if (
            !hasPermissions &&
            !alreadyAskedAutomatically
        ) {

            preferences
                .edit()
                .putBoolean(

                    "asked_health_permissions_automatically",

                    true
                )
                .apply()


            message =
                "Solicitando permisos de Health Connect..."


            delay(
                600
            )


            permissionLauncher.launch(
                healthConnectManager.permissions
            )

        } else if (hasPermissions) {

            readHealthData()

        } else {

            message =
                "Los permisos no están concedidos."
        }
    }


    // ========================================================================
    // ACTUALIZACIÓN AUTOMÁTICA
    // ========================================================================

    /**
     * Mientras esta pantalla esté abierta,
     * actualizamos los datos cada 60 segundos.
     */
    LaunchedEffect(hasPermissions) {

        if (hasPermissions) {

            while (true) {

                readHealthData()

                delay(
                    60_000
                )
            }
        }
    }


    // ========================================================================
    // INTERFAZ
    // ========================================================================

    Box(

        modifier =
            Modifier
                .fillMaxSize()
                .background(
                    backgroundGradient()
                )
                .statusBarsPadding()
                .navigationBarsPadding()
                .imePadding()

    ) {

        Column(

            modifier =
                Modifier
                    .fillMaxSize()
                    .verticalScroll(
                        rememberScrollState()
                    )
                    .padding(
                        horizontal = 18.dp,
                        vertical = 20.dp
                    ),

            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {


            // =================================================================
            // TARJETA PRINCIPAL
            // =================================================================

            Card(

                modifier =
                    Modifier
                        .fillMaxWidth()
                        .widthIn(
                            max = 470.dp
                        ),

                shape =
                    RoundedCornerShape(
                        30.dp
                    ),

                colors =
                    CardDefaults.cardColors(

                        containerColor =
                            Color(0xFFFEFFF6)
                    ),

                elevation =
                    CardDefaults.cardElevation(

                        defaultElevation =
                            10.dp
                    )
            ) {

                Column(

                    modifier =
                        Modifier.padding(
                            20.dp
                        ),

                    horizontalAlignment =
                        Alignment.CenterHorizontally
                ) {


                    // =========================================================
                    // TÍTULO
                    // =========================================================

                    Text(

                        text =
                            "Trastornos del ritmo",

                        fontSize =
                            27.sp,

                        fontWeight =
                            FontWeight.Bold,

                        color =
                            Color(0xFF0F766E),

                        textAlign =
                            TextAlign.Center
                    )


                    Spacer(
                        modifier =
                            Modifier.height(
                                6.dp
                            )
                    )


                    Text(

                        text =
                            "Monitorea tu actividad física y frecuencia cardíaca con los datos disponibles de tu dispositivo.",

                        fontSize =
                            13.sp,

                        color =
                            Color(0xFF64748B),

                        textAlign =
                            TextAlign.Center,

                        lineHeight =
                            18.sp
                    )


                    Spacer(
                        modifier =
                            Modifier.height(
                                14.dp
                            )
                    )


                    // =========================================================
                    // ESTADO DE HEALTH CONNECT
                    // =========================================================

                    Text(

                        text =
                            message,

                        fontSize =
                            12.sp,

                        color =
                            Color(0xFF475569),

                        textAlign =
                            TextAlign.Center
                    )


                    if (isLoading) {

                        Spacer(
                            modifier =
                                Modifier.height(
                                    12.dp
                                )
                        )


                        CircularProgressIndicator(

                            modifier =
                                Modifier.size(
                                    26.dp
                                ),

                            color =
                                Color(0xFF0F766E),

                            strokeWidth =
                                3.dp
                        )
                    }


                    Spacer(
                        modifier =
                            Modifier.height(
                                20.dp
                            )
                    )


                    // =========================================================
                    // ACTIVIDAD FÍSICA
                    // =========================================================

                    StepsMonitoringCard(

                        steps =
                            todaySteps
                                ?: 0L
                    )


                    Spacer(
                        modifier =
                            Modifier.height(
                                22.dp
                            )
                    )


                    HorizontalDivider(

                        color =
                            Color(0xFFE2E8F0)
                    )


                    Spacer(
                        modifier =
                            Modifier.height(
                                22.dp
                            )
                    )


                    // =========================================================
                    // FRECUENCIA CARDÍACA
                    // =========================================================

                    HeartRateMonitoringCard(

                        points =
                            heartRatePoints
                    )


                    Spacer(
                        modifier =
                            Modifier.height(
                                22.dp
                            )
                    )


                    // =========================================================
                    // ACTUALIZAR
                    // =========================================================

                    Button(

                        onClick = {

                            coroutineScope.launch {

                                readHealthData()
                            }
                        },

                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(
                                    52.dp
                                ),

                        shape =
                            RoundedCornerShape(
                                18.dp
                            ),

                        colors =
                            ButtonDefaults.buttonColors(

                                containerColor =
                                    Color(0xFF0F766E),

                                contentColor =
                                    Color.White
                            )
                    ) {

                        Icon(

                            imageVector =
                                Icons.Default.Refresh,

                            contentDescription =
                                null,

                            modifier =
                                Modifier.size(
                                    20.dp
                                )
                        )


                        Spacer(
                            modifier =
                                Modifier.size(
                                    8.dp
                                )
                        )


                        Text(

                            text =
                                "Actualizar datos",

                            fontWeight =
                                FontWeight.Bold
                        )
                    }


                    // =========================================================
                    // SOLICITAR PERMISOS
                    // =========================================================

                    if (!hasPermissions) {

                        Spacer(
                            modifier =
                                Modifier.height(
                                    12.dp
                                )
                        )


                        Button(

                            onClick = {

                                val status =
                                    healthConnectManager
                                        .getAvailabilityStatus()


                                if (
                                    status ==
                                    HealthConnectClient.SDK_AVAILABLE
                                ) {

                                    permissionLauncher.launch(
                                        healthConnectManager.permissions
                                    )
                                }
                            },

                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .height(
                                        50.dp
                                    ),

                            shape =
                                RoundedCornerShape(
                                    18.dp
                                )
                        ) {

                            Text(
                                text =
                                    "Conceder permisos"
                            )
                        }
                    }


                    Spacer(
                        modifier =
                            Modifier.height(
                                14.dp
                            )
                    )


                    // =========================================================
                    // REGRESAR
                    // =========================================================

                    Button(

                        onClick =
                            onBackToMenu,

                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(
                                    52.dp
                                ),

                        shape =
                            RoundedCornerShape(
                                18.dp
                            ),

                        colors =
                            ButtonDefaults.buttonColors(

                                containerColor =
                                    Color(0xFF86A327),

                                contentColor =
                                    Color.White
                            )
                    ) {

                        Text(

                            text =
                                "Volver al menú",

                            fontWeight =
                                FontWeight.Bold
                        )
                    }
                }
            }


            Spacer(
                modifier =
                    Modifier.height(
                        30.dp
                    )
            )
        }
    }
}


// ============================================================================
// TARJETA DE PASOS
// ============================================================================

/**
 * Muestra la actividad física diaria.
 *
 * La meta utilizada por la app es:
 *
 * 10,000 pasos.
 */
@Composable
fun StepsMonitoringCard(
    steps: Long
) {

    val target =
        10_000L


    // Porcentaje respecto a la meta.
    val progress =
        (steps.toFloat() / target.toFloat())
            .coerceIn(
                0f,
                1f
            )


    // Clasificamos los pasos.
    val status =
        classifySteps(
            steps
        )


    val statusColor =
        trafficLightColor(
            status
        )


    val statusText =
        when (status) {

            HealthTrafficLight.GOOD ->
                "Meta alcanzada"

            HealthTrafficLight.REGULAR ->
                "Progreso regular"

            HealthTrafficLight.LOW ->
                "Actividad baja"
        }


    val remainingSteps =
        (target - steps)
            .coerceAtLeast(
                0L
            )


    Card(

        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(
                24.dp
            ),

        colors =
            CardDefaults.cardColors(

                containerColor =
                    Color.White
            ),

        elevation =
            CardDefaults.cardElevation(

                defaultElevation =
                    4.dp
            )
    ) {

        Column(

            modifier =
                Modifier.padding(
                    18.dp
                )
        ) {


            Row(

                verticalAlignment =
                    Alignment.CenterVertically
            ) {


                Box(

                    modifier =
                        Modifier
                            .size(
                                50.dp
                            )
                            .background(

                                color =
                                    Color(0xFFF7FCEB),

                                shape =
                                    CircleShape
                            ),

                    contentAlignment =
                        Alignment.Center
                ) {

                    Icon(

                        imageVector =
                            Icons.Default.DirectionsWalk,

                        contentDescription =
                            null,

                        tint =
                            Color(0xFF86A327),

                        modifier =
                            Modifier.size(
                                30.dp
                            )
                    )
                }


                Spacer(
                    modifier =
                        Modifier.size(
                            12.dp
                        )
                )


                Column {

                    Text(

                        text =
                            "Actividad física",

                        fontSize =
                            18.sp,

                        fontWeight =
                            FontWeight.Bold,

                        color =
                            Color(0xFF0F172A)
                    )


                    Text(

                        text =
                            "Meta diaria: 10,000 pasos",

                        fontSize =
                            12.sp,

                        color =
                            Color(0xFF64748B)
                    )
                }
            }


            Spacer(
                modifier =
                    Modifier.height(
                        18.dp
                    )
            )


            Text(

                text =
                    "$steps pasos",

                fontSize =
                    28.sp,

                fontWeight =
                    FontWeight.Bold,

                color =
                    Color(0xFF0F766E)
            )


            Spacer(
                modifier =
                    Modifier.height(
                        10.dp
                    )
            )


            LinearProgressIndicator(

                progress = {
                    progress
                },

                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(
                            10.dp
                        ),

                color =
                    statusColor,

                trackColor =
                    Color(0xFFE2E8F0)
            )


            Spacer(
                modifier =
                    Modifier.height(
                        12.dp
                    )
            )


            Row(

                verticalAlignment =
                    Alignment.CenterVertically
            ) {


                // Punto de semáforo.
                Box(

                    modifier =
                        Modifier
                            .size(
                                12.dp
                            )
                            .background(

                                color =
                                    statusColor,

                                shape =
                                    CircleShape
                            )
                )


                Spacer(
                    modifier =
                        Modifier.size(
                            8.dp
                        )
                )


                Text(

                    text =
                        statusText,

                    fontSize =
                        14.sp,

                    fontWeight =
                        FontWeight.Bold,

                    color =
                        statusColor
                )
            }


            Spacer(
                modifier =
                    Modifier.height(
                        6.dp
                    )
            )


            Text(

                text =
                    if (
                        steps >= target
                    ) {

                        "¡Completaste tu meta de actividad del día!"

                    } else {

                        "Te faltan $remainingSteps pasos para alcanzar tu meta."
                    },

                fontSize =
                    12.sp,

                color =
                    Color(0xFF64748B)
            )
        }
    }
}


// ============================================================================
// CLASIFICAR PASOS
// ============================================================================

/**
 * Semaforización inicial de pasos.
 *
 * VERDE:
 * 10,000 o más.
 *
 * AMARILLO:
 * 7,000 a 9,999.
 *
 * ROJO:
 * Menos de 7,000.
 */
fun classifySteps(
    steps: Long
): HealthTrafficLight {

    return when {

        steps >= 10_000L ->
            HealthTrafficLight.GOOD

        steps >= 7_000L ->
            HealthTrafficLight.REGULAR

        else ->
            HealthTrafficLight.LOW
    }
}


// ============================================================================
// TARJETA DE FRECUENCIA CARDÍACA
// ============================================================================

@Composable
fun HeartRateMonitoringCard(
    points: List<HeartRatePoint>
) {

    // Punto seleccionado por el usuario.
    var selectedPoint by
    remember(points) {

        mutableStateOf<HeartRatePoint?>(
            null
        )
    }


    // Buscamos mínimo.
    val minimum =
        remember(points) {

            points.minByOrNull {
                it.bpm
            }
        }


    // Buscamos máximo.
    val maximum =
        remember(points) {

            points.maxByOrNull {
                it.bpm
            }
        }


    Card(

        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(
                24.dp
            ),

        colors =
            CardDefaults.cardColors(

                containerColor =
                    Color.White
            ),

        elevation =
            CardDefaults.cardElevation(

                defaultElevation =
                    4.dp
            )
    ) {

        Column(

            modifier =
                Modifier.padding(
                    18.dp
                )
        ) {


            // =================================================================
            // ENCABEZADO
            // =================================================================

            Row(

                verticalAlignment =
                    Alignment.CenterVertically
            ) {


                Box(

                    modifier =
                        Modifier
                            .size(
                                50.dp
                            )
                            .background(

                                color =
                                    Color(0xFFFFEAEA),

                                shape =
                                    CircleShape
                            ),

                    contentAlignment =
                        Alignment.Center
                ) {

                    Icon(

                        imageVector =
                            Icons.Default.Favorite,

                        contentDescription =
                            null,

                        tint =
                            Color(0xFFEF4444),

                        modifier =
                            Modifier.size(
                                28.dp
                            )
                    )
                }


                Spacer(
                    modifier =
                        Modifier.size(
                            12.dp
                        )
                )


                Column {

                    Text(

                        text =
                            "Frecuencia cardíaca",

                        fontSize =
                            18.sp,

                        fontWeight =
                            FontWeight.Bold,

                        color =
                            Color(0xFF0F172A)
                    )


                    Text(

                        text =
                            "Mediciones registradas hoy",

                        fontSize =
                            12.sp,

                        color =
                            Color(0xFF64748B)
                    )
                }
            }


            Spacer(
                modifier =
                    Modifier.height(
                        18.dp
                    )
            )


            // =================================================================
            // SIN DATOS
            // =================================================================

            if (points.isEmpty()) {

                Text(

                    text =
                        "No hay mediciones de frecuencia cardíaca disponibles para el día de hoy.",

                    fontSize =
                        13.sp,

                    color =
                        Color(0xFF64748B),

                    textAlign =
                        TextAlign.Center,

                    modifier =
                        Modifier.fillMaxWidth()
                )

                return@Column
            }


            // =================================================================
            // PUNTO SELECCIONADO
            // =================================================================

            selectedPoint?.let {
                    point ->


                SelectedHeartRateCard(
                    point = point
                )


                Spacer(
                    modifier =
                        Modifier.height(
                            12.dp
                        )
                )
            }


            // =================================================================
            // GRÁFICA
            // =================================================================

            HeartRateChart(

                points =
                    points,

                selectedPoint =
                    selectedPoint,

                onPointSelected = {
                        point ->

                    selectedPoint =
                        point
                }
            )


            Spacer(
                modifier =
                    Modifier.height(
                        8.dp
                    )
            )


            Text(

                text =
                    "Toca o arrastra el dedo sobre la gráfica para revisar cada medición.",

                fontSize =
                    11.sp,

                color =
                    Color(0xFF64748B),

                textAlign =
                    TextAlign.Center,

                modifier =
                    Modifier.fillMaxWidth()
            )


            Spacer(
                modifier =
                    Modifier.height(
                        18.dp
                    )
            )


            // =================================================================
            // MÍNIMO / MÁXIMO
            // =================================================================

            Row(

                modifier =
                    Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.spacedBy(
                        12.dp
                    )
            ) {


                HeartRateExtremeCard(

                    modifier =
                        Modifier.weight(
                            1f
                        ),

                    title =
                        "Mínimo",

                    point =
                        minimum,

                    color =
                        Color(0xFF0284C7)
                )


                HeartRateExtremeCard(

                    modifier =
                        Modifier.weight(
                            1f
                        ),

                    title =
                        "Máximo",

                    point =
                        maximum,

                    color =
                        Color(0xFFEF4444)
                )
            }


            Spacer(
                modifier =
                    Modifier.height(
                        14.dp
                    )
            )


            Text(

                text =
                    "${points.size} mediciones registradas hoy",

                fontSize =
                    11.sp,

                color =
                    Color(0xFF64748B),

                textAlign =
                    TextAlign.Center,

                modifier =
                    Modifier.fillMaxWidth()
            )


            Spacer(
                modifier =
                    Modifier.height(
                        8.dp
                    )
            )


            Text(

                text =
                    "La semaforización es orientativa y no representa un diagnóstico médico.",

                fontSize =
                    10.sp,

                color =
                    Color(0xFF94A3B8),

                textAlign =
                    TextAlign.Center,

                modifier =
                    Modifier.fillMaxWidth()
            )
        }
    }
}


// ============================================================================
// GRÁFICA INTERACTIVA
// ============================================================================

@Composable
fun HeartRateChart(

    points: List<HeartRatePoint>,

    selectedPoint: HeartRatePoint?,

    onPointSelected:
        (HeartRatePoint) -> Unit
) {

    // Ancho real de la gráfica.
    var chartWidthPx by
    remember {

        mutableIntStateOf(
            1
        )
    }


    /**
     * Convierte la posición horizontal del dedo
     * en el punto de frecuencia cardiaca más cercano.
     */
    fun selectPointFromX(
        x: Float
    ) {

        if (
            points.isEmpty() ||
            chartWidthPx <= 0
        ) {

            return
        }


        val clampedX =
            x.coerceIn(
                0f,
                chartWidthPx.toFloat()
            )


        val percentage =
            clampedX /
                    chartWidthPx.toFloat()


        val index =
            (
                    percentage *
                            (points.size - 1)
                    )
                .toInt()
                .coerceIn(
                    0,
                    points.lastIndex
                )


        onPointSelected(
            points[index]
        )
    }


    Canvas(

        modifier =
            Modifier
                .fillMaxWidth()
                .height(
                    230.dp
                )

                // Guardamos el ancho real.
                .onSizeChanged {

                    chartWidthPx =
                        it.width
                }

                // ------------------------------------------------------------
                // TOCAR
                // ------------------------------------------------------------

                .pointerInput(points) {

                    detectTapGestures {
                            offset ->

                        selectPointFromX(
                            offset.x
                        )
                    }
                }

                // ------------------------------------------------------------
                // ARRASTRAR
                // ------------------------------------------------------------

                .pointerInput(points) {

                    detectDragGestures(

                        onDragStart = {
                                offset ->

                            selectPointFromX(
                                offset.x
                            )
                        },

                        onDrag = {
                                change,
                                _ ->

                            selectPointFromX(
                                change.position.x
                            )
                        }
                    )
                }

    ) {


        if (
            points.size < 2
        ) {

            return@Canvas
        }


        // ====================================================================
        // MÁXIMO Y MÍNIMO DE ESCALA
        // ====================================================================

        val minBpm =
            points
                .minOf {
                    it.bpm
                }
                .toFloat()


        val maxBpm =
            points
                .maxOf {
                    it.bpm
                }
                .toFloat()


        // Agregamos espacio vertical.
        val graphMin =
            (minBpm - 10f)
                .coerceAtLeast(
                    30f
                )


        val graphMax =
            maxBpm + 10f


        val range =
            (graphMax - graphMin)
                .coerceAtLeast(
                    1f
                )


        // ====================================================================
        // LÍNEAS HORIZONTALES DE REFERENCIA
        // ====================================================================

        repeat(
            4
        ) {
                index ->


            val y =
                size.height *
                        index /
                        3f


            drawLine(

                color =
                    Color(0xFFE2E8F0),

                start =
                    Offset(
                        0f,
                        y
                    ),

                end =
                    Offset(
                        size.width,
                        y
                    ),

                strokeWidth =
                    1.5f
            )
        }


        // ====================================================================
        // FUNCIÓN PARA OBTENER X
        // ====================================================================

        fun pointX(
            index: Int
        ): Float {

            return if (
                points.size <= 1
            ) {

                0f

            } else {

                index.toFloat() /
                        points.lastIndex.toFloat() *
                        size.width
            }
        }


        // ====================================================================
        // FUNCIÓN PARA OBTENER Y
        // ====================================================================

        fun pointY(
            bpm: Long
        ): Float {

            val normalized =
                (
                        bpm.toFloat() -
                                graphMin
                        ) /
                        range


            return size.height -
                    (
                            normalized *
                                    size.height
                            )
        }


        // ====================================================================
        // DIBUJAMOS LA LÍNEA POR SEGMENTOS
        // ====================================================================

        for (
        index in
        0 until points.lastIndex
        ) {

            val current =
                points[index]


            val next =
                points[index + 1]


            val start =
                Offset(

                    x =
                        pointX(
                            index
                        ),

                    y =
                        pointY(
                            current.bpm
                        )
                )


            val end =
                Offset(

                    x =
                        pointX(
                            index + 1
                        ),

                    y =
                        pointY(
                            next.bpm
                        )
                )


            // Color según la clasificación del punto.
            val segmentColor =
                heartRateColor(
                    current.bpm
                )


            drawLine(

                color =
                    segmentColor,

                start =
                    start,

                end =
                    end,

                strokeWidth =
                    6f,

                cap =
                    StrokeCap.Round
            )
        }


        // ====================================================================
        // PUNTOS DE MEDICIÓN
        // ====================================================================

        points.forEachIndexed {
                index,
                point ->


            val position =
                Offset(

                    x =
                        pointX(
                            index
                        ),

                    y =
                        pointY(
                            point.bpm
                        )
                )


            drawCircle(

                color =
                    heartRateColor(
                        point.bpm
                    ),

                radius =
                    5f,

                center =
                    position
            )
        }


        // ====================================================================
        // PUNTO SELECCIONADO
        // ====================================================================

        selectedPoint?.let {
                selected ->


            val selectedIndex =
                points.indexOf(
                    selected
                )


            if (
                selectedIndex >= 0
            ) {

                val x =
                    pointX(
                        selectedIndex
                    )


                val y =
                    pointY(
                        selected.bpm
                    )


                // Línea vertical.
                drawLine(

                    color =
                        Color(0xFF334155),

                    start =
                        Offset(
                            x,
                            0f
                        ),

                    end =
                        Offset(
                            x,
                            size.height
                        ),

                    strokeWidth =
                        2f
                )


                // Círculo exterior.
                drawCircle(

                    color =
                        Color.White,

                    radius =
                        12f,

                    center =
                        Offset(
                            x,
                            y
                        )
                )


                // Círculo interior.
                drawCircle(

                    color =
                        heartRateColor(
                            selected.bpm
                        ),

                    radius =
                        8f,

                    center =
                        Offset(
                            x,
                            y
                        )
                )
            }
        }
    }
}


// ============================================================================
// TARJETA DEL PUNTO SELECCIONADO
// ============================================================================

@Composable
fun SelectedHeartRateCard(
    point: HeartRatePoint
) {

    val color =
        heartRateColor(
            point.bpm
        )


    val status =
        heartRateStatusText(
            point.bpm
        )


    Card(

        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(
                18.dp
            ),

        colors =
            CardDefaults.cardColors(

                containerColor =
                    Color(0xFFF8FAFC)
            )
    ) {

        Row(

            modifier =
                Modifier.padding(
                    14.dp
                ),

            verticalAlignment =
                Alignment.CenterVertically
        ) {


            Box(

                modifier =
                    Modifier
                        .size(
                            12.dp
                        )
                        .background(

                            color =
                                color,

                            shape =
                                CircleShape
                        )
            )


            Spacer(
                modifier =
                    Modifier.size(
                        10.dp
                    )
            )


            Column(

                modifier =
                    Modifier.weight(
                        1f
                    )
            ) {

                Text(

                    text =
                        "${point.bpm} bpm",

                    fontSize =
                        20.sp,

                    fontWeight =
                        FontWeight.Bold,

                    color =
                        Color(0xFF0F172A)
                )


                Text(

                    text =
                        formatHeartRateTime(
                            point
                        ),

                    fontSize =
                        12.sp,

                    color =
                        Color(0xFF64748B)
                )
            }


            Text(

                text =
                    status,

                fontSize =
                    12.sp,

                fontWeight =
                    FontWeight.Bold,

                color =
                    color
            )
        }
    }
}


// ============================================================================
// TARJETAS MÍNIMO / MÁXIMO
// ============================================================================

@Composable
fun HeartRateExtremeCard(

    modifier: Modifier = Modifier,

    title: String,

    point: HeartRatePoint?,

    color: Color
) {

    Card(

        modifier =
            modifier,

        shape =
            RoundedCornerShape(
                20.dp
            ),

        colors =
            CardDefaults.cardColors(

                containerColor =
                    Color(0xFFF8FAFC)
            )
    ) {

        Column(

            modifier =
                Modifier.padding(
                    14.dp
                ),

            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            Text(

                text =
                    title,

                fontSize =
                    12.sp,

                fontWeight =
                    FontWeight.Bold,

                color =
                    Color(0xFF64748B)
            )


            Spacer(
                modifier =
                    Modifier.height(
                        5.dp
                    )
            )


            Text(

                text =
                    point?.let {
                        "${it.bpm} bpm"
                    }
                        ?: "--",

                fontSize =
                    20.sp,

                fontWeight =
                    FontWeight.Bold,

                color =
                    color
            )


            Spacer(
                modifier =
                    Modifier.height(
                        3.dp
                    )
            )


            Text(

                text =
                    point?.let {

                        formatHeartRateTime(
                            it
                        )

                    } ?: "--",

                fontSize =
                    11.sp,

                color =
                    Color(0xFF64748B)
            )
        }
    }
}


// ============================================================================
// FORMATEAR HORA
// ============================================================================

/**
 * Convierte Instant en una hora fácil de leer.
 *
 * Ejemplo:
 *
 * 2026-08-31T17:42:00Z
 *
 * podría mostrarse localmente como:
 *
 * 12:42
 */
fun formatHeartRateTime(
    point: HeartRatePoint
): String {

    val formatter =
        DateTimeFormatter.ofPattern(
            "HH:mm"
        )


    return point.time
        .atZone(
            ZoneId.systemDefault()
        )
        .format(
            formatter
        )
}


// ============================================================================
// COLOR DE SEMAFORIZACIÓN DE FC
// ============================================================================

/**
 * CLASIFICACIÓN INICIAL.
 *
 * IMPORTANTE:
 *
 * Esta semaforización todavía es orientativa.
 *
 * Más adelante la mejoraremos utilizando:
 *
 * - Actividad física.
 * - Pasos.
 * - Duración del episodio.
 * - Frecuencia basal.
 * - Cambios bruscos.
 *
 * Por ahora sirve para comprobar visualmente
 * que la gráfica puede cambiar de color.
 */
fun heartRateColor(
    bpm: Long
): Color {

    return when {

        // Frecuencia muy baja.
        bpm < 40 -> {

            Color(0xFFDC2626)
        }


        // Frecuencia baja.
        bpm < 50 -> {

            Color(0xFFF59E0B)
        }


        // Rango inicialmente estable.
        bpm <= 100 -> {

            Color(0xFF22C55E)
        }


        // Frecuencia elevada.
        bpm <= 130 -> {

            Color(0xFFF59E0B)
        }


        // Frecuencia muy elevada.
        else -> {

            Color(0xFFDC2626)
        }
    }
}


// ============================================================================
// TEXTO DE ESTADO DE FC
// ============================================================================

fun heartRateStatusText(
    bpm: Long
): String {

    return when {

        bpm < 40 ->
            "Revisar"

        bpm < 50 ->
            "Baja"

        bpm <= 100 ->
            "Estable"

        bpm <= 130 ->
            "Elevada"

        else ->
            "Revisar"
    }
}


// ============================================================================
// COLOR GENERAL DE SEMÁFORO
// ============================================================================

fun trafficLightColor(
    status: HealthTrafficLight
): Color {

    return when (status) {

        HealthTrafficLight.GOOD ->

            Color(0xFF22C55E)


        HealthTrafficLight.REGULAR ->

            Color(0xFFF59E0B)


        HealthTrafficLight.LOW ->

            Color(0xFFDC2626)
    }
}