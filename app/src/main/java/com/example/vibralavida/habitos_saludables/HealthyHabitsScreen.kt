package com.example.vibralavida.habitos_saludables
import com.example.vibralavida.trastornos_ritmo.SleepSummary
import com.example.vibralavida.pantallas_principales.BackgroundBlurCircle
import com.example.vibralavida.trastornos_ritmo.HealthConnectManager
import com.example.vibralavida.backgroundGradient

// ============================================================================
// ANDROID
// ============================================================================

import android.speech.tts.TextToSpeech


// ============================================================================
// COMPOSE FOUNDATION
// ============================================================================

import androidx.compose.foundation.background

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll


// ============================================================================
// ICONOS
// ============================================================================

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MonitorHeart
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.VolumeUp


// ============================================================================
// MATERIAL 3
// ============================================================================

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text


// ============================================================================
// ESTADOS
// ============================================================================

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue


// ============================================================================
// UI
// ============================================================================

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


// ============================================================================
// FECHA Y HORA
// ============================================================================

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale


// ============================================================================
// ESTADO DE DURACIÓN DEL SUEÑO
// ============================================================================

/**
 * Representa el semáforo correspondiente
 * únicamente a la duración del sueño.
 */
enum class SleepTrafficLight {

    GOOD,

    REGULAR,

    LOW
}


// ============================================================================
// RESULTADO COMBINADO DE SUEÑO
// ============================================================================

/**
 * Representa el resultado después de combinar:
 *
 * - Duración del sueño.
 * - Resultado AIS.
 *
 * Más adelante agregaremos:
 *
 * - Ronquidos.
 * - Calidad de las etapas.
 * - Movimientos.
 * - Sleep Score.
 */
enum class CombinedSleepStatus {

    GOOD,

    REGULAR,

    LOW
}


// ============================================================================
// PANTALLA DE HÁBITOS SALUDABLES
// ============================================================================

@Composable
fun HealthyHabitsScreen(

    userName: String,

    userAge: String,

    sleepSurveyScore: Int?,

    onBackToMenu: () -> Unit,

    onProfileClick: () -> Unit,

    onMoodSurveyClick: () -> Unit,

    onSleepSurveyClick: () -> Unit,

    // Abre el nuevo Modo Sueño.
    onSleepModeClick: () -> Unit,

    onImcClick: () -> Unit,

    onCaloriesClick: () -> Unit,

    onCardioRiskClick: () -> Unit
) {

    // ========================================================================
    // NOMBRE DEL USUARIO
    // ========================================================================

    val firstName =
        userName
            .trim()
            .split(" ")
            .firstOrNull()
            .orEmpty()
            .ifBlank {

                "Usuario"
            }


    // ========================================================================
    // EDAD
    // ========================================================================

    val age =
        userAge.toIntOrNull()


    // ========================================================================
    // CONTEXTO
    // ========================================================================

    val context =
        LocalContext.current


    // ========================================================================
    // HEALTH CONNECT
    // ========================================================================

    val healthConnectManager =
        remember {

            HealthConnectManager(
                context
            )
        }


    // ========================================================================
    // RESUMEN DEL SUEÑO
    // ========================================================================

    var sleepSummary by remember {

        mutableStateOf<SleepSummary?>(
            null
        )
    }


    // ========================================================================
    // ESTADO DE CARGA
    // ========================================================================

    var isSleepLoading by remember {

        mutableStateOf(
            true
        )
    }


    // ========================================================================
    // ERROR
    // ========================================================================

    var sleepError by remember {

        mutableStateOf<String?>(
            null
        )
    }


    // ========================================================================
    // LEER DATOS DE HEALTH CONNECT
    // ========================================================================

    LaunchedEffect(Unit) {

        try {

            isSleepLoading =
                true


            sleepError =
                null


            val hasPermissions =
                healthConnectManager
                    .hasAllPermissions()


            if (hasPermissions) {

                sleepSummary =
                    healthConnectManager
                        .readLastSleepSummaryFromLastDays(
                            days = 30
                        )

            } else {

                sleepError =
                    "No se concedieron permisos para consultar los datos de sueño."
            }

        } catch (_: Exception) {

            sleepError =
                "No fue posible obtener los datos de sueño."

        } finally {

            isSleepLoading =
                false
        }
    }


    // ========================================================================
    // CONTENEDOR PRINCIPAL
    // ========================================================================

    Box(

        modifier =
            Modifier
                .fillMaxSize()

                // ============================================================
                // CORREGIDO
                // ============================================================
                //
                // Esta pantalla ahora tiene su propio fondo
                // y ya NO depende de MainActivity.
                // ============================================================

                .background(
                    healthyHabitsBackgroundGradient()
                )

                .statusBarsPadding()

                .navigationBarsPadding()

                .imePadding()
    ) {


        // ====================================================================
        // DECORACIÓN SUPERIOR
        // ====================================================================

        BackgroundBlurCircle(

            modifier =
                Modifier
                    .align(
                        Alignment.TopCenter
                    )
                    .padding(
                        top = 36.dp
                    )
        )


        // ====================================================================
        // CONTENIDO CON SCROLL
        // ====================================================================

        Column(

            modifier =
                Modifier
                    .fillMaxSize()
                    .verticalScroll(
                        rememberScrollState()
                    )
                    .padding(
                        horizontal = 18.dp,
                        vertical = 16.dp
                    ),

            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {


            // =================================================================
            // TOP BAR
            // =================================================================

            HealthyTopBar(

                userName =
                    firstName,

                onMenuClick = {

                    println(
                        "Abrir menú lateral"
                    )
                },

                onProfileClick =
                    onProfileClick
            )


            Spacer(

                modifier =
                    Modifier.height(
                        16.dp
                    )
            )


            // =================================================================
            // TARJETA GENERAL
            // =================================================================

            Card(

                modifier =
                    Modifier
                        .fillMaxWidth()
                        .widthIn(
                            max = 450.dp
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
                        Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 18.dp,
                                vertical = 22.dp
                            ),

                    horizontalAlignment =
                        Alignment.CenterHorizontally
                ) {


                    // =========================================================
                    // TÍTULO
                    // =========================================================

                    Text(

                        text =
                            "Hábitos saludables",

                        fontSize =
                            24.sp,

                        fontWeight =
                            FontWeight.Bold,

                        color =
                            Color(0xFF0F172A),

                        textAlign =
                            TextAlign.Center
                    )


                    Spacer(

                        modifier =
                            Modifier.height(
                                18.dp
                            )
                    )


                    // =========================================================
                    // INTRODUCCIÓN
                    // =========================================================

                    HealthyIntroCard()


                    Spacer(

                        modifier =
                            Modifier.height(
                                22.dp
                            )
                    )


                    // =========================================================
                    // SUEÑO DE ANOCHE
                    // =========================================================

                    SleepSummaryCard(

                        sleepSummary =
                            sleepSummary,

                        age =
                            age,

                        aisScore =
                            sleepSurveyScore,

                        isLoading =
                            isSleepLoading,

                        error =
                            sleepError,

                        onSleepSurveyClick =
                            onSleepSurveyClick
                    )


                    // =========================================================
                    // MODO SUEÑO
                    // =========================================================

                    Spacer(

                        modifier =
                            Modifier.height(
                                16.dp
                            )
                    )


                    SleepModeEntryCard(

                        onClick =
                            onSleepModeClick
                    )


                    Spacer(

                        modifier =
                            Modifier.height(
                                26.dp
                            )
                    )


                    HorizontalDivider(

                        color =
                            Color(0xFFE2E8F0)
                    )


                    Spacer(

                        modifier =
                            Modifier.height(
                                24.dp
                            )
                    )


                    // =========================================================
                    // BIENESTAR EMOCIONAL
                    // =========================================================
                    //
                    // Atenas ya NO está duplicada aquí.
                    //
                    // Se encuentra dentro del módulo de sueño.
                    // =========================================================

                    Text(

                        text =
                            "Evaluación de bienestar",

                        fontSize =
                            18.sp,

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
                                14.dp
                            )
                    )


                    HealthyMiniCard(

                        modifier =
                            Modifier.fillMaxWidth(),

                        title =
                            "¿Cómo te sientes?",

                        subtitle =
                            "Evalúa tu bienestar emocional",

                        backgroundColor =
                            Color(0xFFF7FCEB),

                        icon = {

                            Icon(

                                imageVector =
                                    Icons.Default.Favorite,

                                contentDescription =
                                    null,

                                tint =
                                    Color(0xFFEF4444),

                                modifier =
                                    Modifier.size(
                                        42.dp
                                    )
                            )
                        },

                        onClick =
                            onMoodSurveyClick
                    )


                    Spacer(

                        modifier =
                            Modifier.height(
                                26.dp
                            )
                    )


                    HorizontalDivider(

                        color =
                            Color(0xFFE2E8F0)
                    )


                    Spacer(

                        modifier =
                            Modifier.height(
                                24.dp
                            )
                    )


                    // =========================================================
                    // IMAGEN CENTRAL
                    // =========================================================

                    HealthyCentralImagePlaceholder()


                    Spacer(

                        modifier =
                            Modifier.height(
                                26.dp
                            )
                    )


                    // =========================================================
                    // HERRAMIENTAS
                    // =========================================================

                    Text(

                        text =
                            "Herramientas para cuidar tu salud",

                        fontSize =
                            18.sp,

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
                                14.dp
                            )
                    )


                    // =========================================================
                    // IMC
                    // =========================================================

                    HealthToolCard(

                        title =
                            "Calculadora de IMC",

                        subtitle =
                            "Conoce tu índice de masa corporal",

                        backgroundColor =
                            Color(0xFFF7FCEB),

                        icon = {

                            Icon(

                                imageVector =
                                    Icons.Default.Calculate,

                                contentDescription =
                                    null,

                                tint =
                                    Color(0xFF86A327),

                                modifier =
                                    Modifier.size(
                                        38.dp
                                    )
                            )
                        },

                        onClick =
                            onImcClick
                    )


                    Spacer(

                        modifier =
                            Modifier.height(
                                14.dp
                            )
                    )


                    // =========================================================
                    // CALORÍAS
                    // =========================================================

                    HealthToolCard(

                        title =
                            "Calculadora de calorías",

                        subtitle =
                            "Estima tus necesidades energéticas",

                        backgroundColor =
                            Color(0xFFEAF8FF),

                        icon = {

                            Icon(

                                imageVector =
                                    Icons.Default.LocalFireDepartment,

                                contentDescription =
                                    null,

                                tint =
                                    Color(0xFF0284C7),

                                modifier =
                                    Modifier.size(
                                        38.dp
                                    )
                            )
                        },

                        onClick =
                            onCaloriesClick
                    )


                    Spacer(

                        modifier =
                            Modifier.height(
                                14.dp
                            )
                    )


                    // =========================================================
                    // RIESGO CARDIOVASCULAR
                    // =========================================================

                    HealthToolCard(

                        title =
                            "Calculadora de riesgo cardiovascular",

                        subtitle =
                            "Identifica factores de riesgo",

                        backgroundColor =
                            Color(0xFFFFEAEA),

                        icon = {

                            Icon(

                                imageVector =
                                    Icons.Default.MonitorHeart,

                                contentDescription =
                                    null,

                                tint =
                                    Color(0xFFEF4444),

                                modifier =
                                    Modifier.size(
                                        38.dp
                                    )
                            )
                        },

                        onClick =
                            onCardioRiskClick
                    )


                    Spacer(

                        modifier =
                            Modifier.height(
                                26.dp
                            )
                    )


                    // =========================================================
                    // VOLVER
                    // =========================================================

                    Button(

                        onClick =
                            onBackToMenu,

                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(
                                    54.dp
                                ),

                        shape =
                            RoundedCornerShape(
                                20.dp
                            ),

                        colors =
                            ButtonDefaults.buttonColors(

                                containerColor =
                                    Color(0xFF22C55E),

                                contentColor =
                                    Color(0xFF052E16)
                            )
                    ) {


                        Text(

                            text =
                                "Volver al menú",

                            fontSize =
                                16.sp,

                            fontWeight =
                                FontWeight.Bold
                        )
                    }
                }
            }


            Spacer(

                modifier =
                    Modifier.height(
                        28.dp
                    )
            )
        }
    }
}


// ============================================================================
// TARJETA PARA ENTRAR AL MODO SUEÑO
// ============================================================================

@Composable
fun SleepModeEntryCard(
    onClick: () -> Unit
) {


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
                    Color(0xFF0B2340)
            ),

        elevation =
            CardDefaults.cardElevation(

                defaultElevation =
                    5.dp
            )
    ) {


        Row(

            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(
                        18.dp
                    ),

            verticalAlignment =
                Alignment.CenterVertically
        ) {


            // =================================================================
            // ICONO
            // =================================================================

            Box(

                modifier =
                    Modifier
                        .size(
                            58.dp
                        )
                        .clip(
                            CircleShape
                        )
                        .background(

                            Color.White.copy(
                                alpha = 0.10f
                            )
                        ),

                contentAlignment =
                    Alignment.Center
            ) {


                Icon(

                    imageVector =
                        Icons.Default.Bedtime,

                    contentDescription =
                        null,

                    tint =
                        Color(0xFF7DD3FC),

                    modifier =
                        Modifier.size(
                            34.dp
                        )
                )
            }


            Spacer(

                modifier =
                    Modifier.width(
                        14.dp
                    )
            )


            // =================================================================
            // TEXTO
            // =================================================================

            Column(

                modifier =
                    Modifier.weight(
                        1f
                    )
            ) {


                Text(

                    text =
                        "Modo sueño",

                    color =
                        Color.White,

                    fontSize =
                        17.sp,

                    fontWeight =
                        FontWeight.Bold
                )


                Spacer(

                    modifier =
                        Modifier.height(
                            4.dp
                        )
                )


                Text(

                    text =
                        "Prepara el monitoreo de tu descanso para esta noche.",

                    color =
                        Color(0xFFCBD5E1),

                    fontSize =
                        11.sp,

                    lineHeight =
                        15.sp
                )
            }


            Spacer(

                modifier =
                    Modifier.width(
                        10.dp
                    )
            )


            // =================================================================
            // ABRIR
            // =================================================================

            Button(

                onClick =
                    onClick,

                shape =
                    RoundedCornerShape(
                        16.dp
                    ),

                colors =
                    ButtonDefaults.buttonColors(

                        containerColor =
                            Color(0xFF38BDF8),

                        contentColor =
                            Color(0xFF082F49)
                    )
            ) {


                Text(

                    text =
                        "Abrir",

                    fontWeight =
                        FontWeight.Bold
                )
            }
        }
    }
}


// ============================================================================
// RESUMEN DEL SUEÑO
// ============================================================================

@Composable
fun SleepSummaryCard(

    sleepSummary: SleepSummary?,

    age: Int?,

    aisScore: Int?,

    isLoading: Boolean,

    error: String?,

    onSleepSurveyClick: () -> Unit
) {


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
                    Color(0xFFEAF8FF)
            ),

        elevation =
            CardDefaults.cardElevation(

                defaultElevation =
                    5.dp
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
                                52.dp
                            )
                            .clip(
                                CircleShape
                            )
                            .background(
                                Color.White
                            ),

                    contentAlignment =
                        Alignment.Center
                ) {


                    Icon(

                        imageVector =
                            Icons.Default.Bedtime,

                        contentDescription =
                            null,

                        tint =
                            Color(0xFF0284C7),

                        modifier =
                            Modifier.size(
                                30.dp
                            )
                    )
                }


                Spacer(

                    modifier =
                        Modifier.width(
                            12.dp
                        )
                )


                Column {


                    Text(

                        text =
                            "Sueño de anoche",

                        fontSize =
                            18.sp,

                        fontWeight =
                            FontWeight.Bold,

                        color =
                            Color(0xFF0F172A)
                    )


                    Text(

                        text =
                            "Datos obtenidos de tu wearable",

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
            // CARGANDO
            // =================================================================

            if (isLoading) {


                Row(

                    verticalAlignment =
                        Alignment.CenterVertically
                ) {


                    CircularProgressIndicator(

                        modifier =
                            Modifier.size(
                                22.dp
                            ),

                        strokeWidth =
                            3.dp,

                        color =
                            Color(0xFF0284C7)
                    )


                    Spacer(

                        modifier =
                            Modifier.width(
                                10.dp
                            )
                    )


                    Text(

                        text =
                            "Consultando tu sueño...",

                        fontSize =
                            13.sp,

                        color =
                            Color(0xFF64748B)
                    )
                }


                return@Column
            }


            // =================================================================
            // ERROR
            // =================================================================

            if (error != null) {


                Text(

                    text =
                        error,

                    fontSize =
                        13.sp,

                    color =
                        Color(0xFF64748B)
                )


                return@Column
            }


            // =================================================================
            // SIN DATOS
            // =================================================================

            if (sleepSummary == null) {


                Text(

                    text =
                        "No se encontró una sesión de sueño del wearable.",

                    fontSize =
                        13.sp,

                    color =
                        Color(0xFF64748B)
                )


                Spacer(

                    modifier =
                        Modifier.height(
                            14.dp
                        )
                )


                Button(

                    onClick =
                        onSleepSurveyClick,

                    modifier =
                        Modifier.fillMaxWidth(),

                    colors =
                        ButtonDefaults.buttonColors(

                            containerColor =
                                Color(0xFF0284C7),

                            contentColor =
                                Color.White
                        )
                ) {


                    Text(

                        text =
                            "Responder Escala de Atenas"
                    )
                }


                return@Column
            }


            // =================================================================
            // DURACIÓN
            // =================================================================

            val hours =
                sleepSummary.totalMinutes / 60


            val minutes =
                sleepSummary.totalMinutes % 60


            val durationStatus =
                classifySleepDuration(

                    totalMinutes =
                        sleepSummary.totalMinutes,

                    age =
                        age
                )


            val durationColor =
                sleepTrafficLightColor(
                    durationStatus
                )


            Text(

                text =
                    "$hours h $minutes min",

                fontSize =
                    30.sp,

                fontWeight =
                    FontWeight.Bold,

                color =
                    Color(0xFF0F766E)
            )


            Spacer(

                modifier =
                    Modifier.height(
                        5.dp
                    )
            )


            // =================================================================
            // HORARIO
            // =================================================================

            Text(

                text =
                    "${formatSleepTime(sleepSummary.startTime)} - " +
                            formatSleepTime(
                                sleepSummary.endTime
                            ),

                fontSize =
                    12.sp,

                color =
                    Color(0xFF64748B)
            )


            Spacer(

                modifier =
                    Modifier.height(
                        12.dp
                    )
            )


            // =================================================================
            // SEMÁFORO
            // =================================================================

            Row(

                verticalAlignment =
                    Alignment.CenterVertically
            ) {


                Box(

                    modifier =
                        Modifier
                            .size(
                                13.dp
                            )
                            .background(

                                color =
                                    durationColor,

                                shape =
                                    CircleShape
                            )
                )


                Spacer(

                    modifier =
                        Modifier.width(
                            8.dp
                        )
                )


                Text(

                    text =
                        when (durationStatus) {


                            SleepTrafficLight.GOOD ->

                                "Duración adecuada"


                            SleepTrafficLight.REGULAR ->

                                "Duración por revisar"


                            SleepTrafficLight.LOW ->

                                "Duración fuera del rango esperado"
                        },

                    fontSize =
                        14.sp,

                    fontWeight =
                        FontWeight.Bold,

                    color =
                        durationColor
                )
            }


            Spacer(

                modifier =
                    Modifier.height(
                        8.dp
                    )
            )


            Text(

                text =
                    sleepRecommendationText(
                        age
                    ),

                fontSize =
                    12.sp,

                color =
                    Color(0xFF64748B),

                lineHeight =
                    17.sp
            )


            // =================================================================
            // ETAPAS
            // =================================================================

            Spacer(

                modifier =
                    Modifier.height(
                        18.dp
                    )
            )


            HorizontalDivider(

                color =
                    Color.White
            )


            Spacer(

                modifier =
                    Modifier.height(
                        16.dp
                    )
            )


            Text(

                text =
                    "Etapas del sueño",

                fontSize =
                    15.sp,

                fontWeight =
                    FontWeight.Bold,

                color =
                    Color(0xFF0F172A)
            )


            Spacer(

                modifier =
                    Modifier.height(
                        12.dp
                    )
            )


            if (
                sleepSummary.hasSleepStages
            ) {


                // =============================================================
                // LIGERO
                // =============================================================

                if (
                    sleepSummary.hasLightSleep
                ) {


                    SleepStageRow(

                        label =
                            "Sueño ligero",

                        minutes =
                            sleepSummary.lightSleepMinutes
                    )
                }


                // =============================================================
                // PROFUNDO
                // =============================================================

                if (
                    sleepSummary.hasDeepSleep
                ) {


                    SleepStageRow(

                        label =
                            "Sueño profundo",

                        minutes =
                            sleepSummary.deepSleepMinutes
                    )
                }


                // =============================================================
                // REM
                // =============================================================
                //
                // Solo aparece si el wearable realmente
                // proporciona esta etapa.
                // =============================================================

                if (
                    sleepSummary.hasRemSleep
                ) {


                    SleepStageRow(

                        label =
                            "Sueño REM",

                        minutes =
                            sleepSummary.remSleepMinutes
                    )
                }


                // =============================================================
                // DESPIERTO
                // =============================================================

                if (
                    sleepSummary.hasAwakeData
                ) {


                    SleepStageRow(

                        label =
                            "Despierto",

                        minutes =
                            sleepSummary.awakeMinutes
                    )
                }


                Spacer(

                    modifier =
                        Modifier.height(
                            8.dp
                        )
                )


                Text(

                    text =
                        "${sleepSummary.stages.size} cambios de etapa registrados durante la sesión.",

                    fontSize =
                        11.sp,

                    color =
                        Color(0xFF94A3B8)
                )


                Spacer(

                    modifier =
                        Modifier.height(
                            6.dp
                        )
                )


                // =============================================================
                // ACLARACIÓN
                // =============================================================

                if (
                    !sleepSummary.hasRemSleep ||
                    !sleepSummary.hasAwakeData
                ) {


                    Text(

                        text =
                            "Se muestran únicamente las etapas que tu wearable comparte con Health Connect.",

                        fontSize =
                            11.sp,

                        color =
                            Color(0xFF64748B),

                        lineHeight =
                            15.sp
                    )
                }

            } else {


                Text(

                    text =
                        "El wearable registró el sueño, pero no proporcionó información sobre sus etapas.",

                    fontSize =
                        12.sp,

                    color =
                        Color(0xFF64748B),

                    lineHeight =
                        17.sp
                )
            }


            // =================================================================
            // ATENAS
            // =================================================================

            Spacer(

                modifier =
                    Modifier.height(
                        18.dp
                    )
            )


            HorizontalDivider(

                color =
                    Color.White
            )


            Spacer(

                modifier =
                    Modifier.height(
                        16.dp
                    )
            )


            Text(

                text =
                    "Percepción del sueño",

                fontSize =
                    15.sp,

                fontWeight =
                    FontWeight.Bold,

                color =
                    Color(0xFF0F172A)
            )


            Spacer(

                modifier =
                    Modifier.height(
                        8.dp
                    )
            )


            // =================================================================
            // SIN ATENAS
            // =================================================================

            if (aisScore == null) {


                Text(

                    text =
                        "Todavía no has respondido la Escala de Insomnio de Atenas.",

                    fontSize =
                        12.sp,

                    color =
                        Color(0xFF64748B),

                    lineHeight =
                        17.sp
                )


                Spacer(

                    modifier =
                        Modifier.height(
                            12.dp
                        )
                )


                Button(

                    onClick =
                        onSleepSurveyClick,

                    modifier =
                        Modifier.fillMaxWidth(),

                    shape =
                        RoundedCornerShape(
                            16.dp
                        ),

                    colors =
                        ButtonDefaults.buttonColors(

                            containerColor =
                                Color(0xFF0284C7),

                            contentColor =
                                Color.White
                        )
                ) {


                    Text(

                        text =
                            "Responder Escala de Atenas",

                        fontWeight =
                            FontWeight.Bold
                    )
                }

            } else {


                // =============================================================
                // RESULTADO AIS
                // =============================================================

                val aisFavorable =
                    aisScore < 6


                Text(

                    text =
                        "AIS: $aisScore de 24",

                    fontSize =
                        14.sp,

                    fontWeight =
                        FontWeight.Bold,

                    color =
                        if (aisFavorable) {

                            Color(0xFF22C55E)

                        } else {

                            Color(0xFFF59E0B)
                        }
                )


                Spacer(

                    modifier =
                        Modifier.height(
                            5.dp
                        )
                )


                Text(

                    text =
                        if (aisFavorable) {

                            "Tu percepción del sueño fue favorable."

                        } else {

                            "Reportaste dificultades relacionadas con el sueño."
                        },

                    fontSize =
                        12.sp,

                    color =
                        Color(0xFF64748B)
                )


                Spacer(

                    modifier =
                        Modifier.height(
                            16.dp
                        )
                )


                // =============================================================
                // RESULTADO COMBINADO
                // =============================================================

                val combinedStatus =
                    combineSleepResults(

                        durationStatus =
                            durationStatus,

                        aisScore =
                            aisScore
                    )


                CombinedSleepResultCard(

                    status =
                        combinedStatus,

                    message =
                        combinedSleepMessage(

                            durationStatus =
                                durationStatus,

                            aisScore =
                                aisScore
                        )
                )


                Spacer(

                    modifier =
                        Modifier.height(
                            12.dp
                        )
                )


                Button(

                    onClick =
                        onSleepSurveyClick,

                    modifier =
                        Modifier.fillMaxWidth(),

                    shape =
                        RoundedCornerShape(
                            16.dp
                        ),

                    colors =
                        ButtonDefaults.buttonColors(

                            containerColor =
                                Color(0xFF0284C7),

                            contentColor =
                                Color.White
                        )
                ) {


                    Text(

                        text =
                            "Volver a evaluar mi sueño",

                        fontWeight =
                            FontWeight.Bold
                    )
                }
            }


            Spacer(

                modifier =
                    Modifier.height(
                        12.dp
                    )
            )


            // =================================================================
            // AVISO
            // =================================================================

            Text(

                text =
                    "Los datos del wearable y el resultado de Atenas son orientativos y no constituyen un diagnóstico médico.",

                fontSize =
                    10.sp,

                color =
                    Color(0xFF94A3B8),

                textAlign =
                    TextAlign.Center,

                lineHeight =
                    14.sp,

                modifier =
                    Modifier.fillMaxWidth()
            )
        }
    }
}


// ============================================================================
// FILA DE ETAPA
// ============================================================================

@Composable
fun SleepStageRow(

    label: String,

    minutes: Long
) {


    val hours =
        minutes / 60


    val remainingMinutes =
        minutes % 60


    Row(

        modifier =
            Modifier
                .fillMaxWidth()
                .padding(
                    vertical = 6.dp
                ),

        verticalAlignment =
            Alignment.CenterVertically
    ) {


        Text(

            text =
                label,

            fontSize =
                13.sp,

            color =
                Color(0xFF334155),

            modifier =
                Modifier.weight(
                    1f
                )
        )


        Text(

            text =
                if (hours > 0) {

                    "$hours h $remainingMinutes min"

                } else {

                    "$remainingMinutes min"
                },

            fontSize =
                13.sp,

            fontWeight =
                FontWeight.Bold,

            color =
                Color(0xFF0F172A)
        )
    }
}


// ============================================================================
// RESULTADO COMBINADO
// ============================================================================

@Composable
fun CombinedSleepResultCard(

    status: CombinedSleepStatus,

    message: String
) {


    val statusColor =
        combinedSleepStatusColor(
            status
        )


    val title =
        when (status) {


            CombinedSleepStatus.GOOD ->

                "Buen descanso"


            CombinedSleepStatus.REGULAR ->

                "Descanso regular"


            CombinedSleepStatus.LOW ->

                "Descanso por mejorar"
        }


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
                    Color.White
            )
    ) {


        Column(

            modifier =
                Modifier.padding(
                    14.dp
                )
        ) {


            Text(

                text =
                    "Resultado combinado",

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
                        8.dp
                    )
            )


            Row(

                verticalAlignment =
                    Alignment.CenterVertically
            ) {


                Box(

                    modifier =
                        Modifier
                            .size(
                                14.dp
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
                        Modifier.width(
                            8.dp
                        )
                )


                Text(

                    text =
                        title,

                    fontSize =
                        17.sp,

                    fontWeight =
                        FontWeight.Bold,

                    color =
                        statusColor
                )
            }


            Spacer(

                modifier =
                    Modifier.height(
                        8.dp
                    )
            )


            Text(

                text =
                    message,

                fontSize =
                    12.sp,

                color =
                    Color(0xFF334155),

                lineHeight =
                    17.sp
            )
        }
    }
}


// ============================================================================
// CLASIFICACIÓN POR DURACIÓN
// ============================================================================

fun classifySleepDuration(

    totalMinutes: Long,

    age: Int?

): SleepTrafficLight {


    val hours =
        totalMinutes /
                60.0


    return when {


        // ====================================================================
        // MENORES DE 18
        // ====================================================================

        age != null &&
                age <= 17 -> {


            when {


                hours in 8.0..10.0 ->

                    SleepTrafficLight.GOOD


                hours >= 7.0 &&
                        hours < 8.0 ->

                    SleepTrafficLight.REGULAR


                hours > 10.0 &&
                        hours <= 11.0 ->

                    SleepTrafficLight.REGULAR


                else ->

                    SleepTrafficLight.LOW
            }
        }


        // ====================================================================
        // 18 AÑOS O MÁS
        // ====================================================================

        else -> {


            when {


                hours in 7.0..9.0 ->

                    SleepTrafficLight.GOOD


                hours >= 6.0 &&
                        hours < 7.0 ->

                    SleepTrafficLight.REGULAR


                hours > 9.0 &&
                        hours <= 10.0 ->

                    SleepTrafficLight.REGULAR


                else ->

                    SleepTrafficLight.LOW
            }
        }
    }
}


// ============================================================================
// COMBINAR DURACIÓN + ATENAS
// ============================================================================

fun combineSleepResults(

    durationStatus: SleepTrafficLight,

    aisScore: Int

): CombinedSleepStatus {


    val aisFavorable =
        aisScore < 6


    return when {


        // ====================================================================
        // DURACIÓN ADECUADA + AIS FAVORABLE
        // ====================================================================

        durationStatus ==
                SleepTrafficLight.GOOD &&
                aisFavorable ->

            CombinedSleepStatus.GOOD


        // ====================================================================
        // DURACIÓN ADECUADA + AIS DESFAVORABLE
        // ====================================================================

        durationStatus ==
                SleepTrafficLight.GOOD &&
                !aisFavorable ->

            CombinedSleepStatus.REGULAR


        // ====================================================================
        // DURACIÓN REGULAR
        // ====================================================================

        durationStatus ==
                SleepTrafficLight.REGULAR ->

            CombinedSleepStatus.REGULAR


        // ====================================================================
        // DURACIÓN FUERA DEL RANGO + AIS FAVORABLE
        // ====================================================================

        durationStatus ==
                SleepTrafficLight.LOW &&
                aisFavorable ->

            CombinedSleepStatus.REGULAR


        // ====================================================================
        // DURACIÓN FUERA DEL RANGO + AIS DESFAVORABLE
        // ====================================================================

        else ->

            CombinedSleepStatus.LOW
    }
}


// ============================================================================
// MENSAJE COMBINADO
// ============================================================================

fun combinedSleepMessage(

    durationStatus: SleepTrafficLight,

    aisScore: Int

): String {


    val favorable =
        aisScore < 6


    return when {


        durationStatus ==
                SleepTrafficLight.GOOD &&
                favorable ->

            "La duración registrada y tu percepción del descanso fueron favorables."


        durationStatus ==
                SleepTrafficLight.GOOD &&
                !favorable ->

            "Dormiste una cantidad adecuada de horas, pero reportaste dificultades relacionadas con tu descanso."


        durationStatus ==
                SleepTrafficLight.LOW &&
                favorable ->

            "La duración registrada estuvo fuera del rango esperado, aunque tu percepción del descanso fue favorable."


        durationStatus ==
                SleepTrafficLight.LOW &&
                !favorable ->

            "La duración estuvo fuera del rango esperado y también reportaste dificultades relacionadas con el sueño."


        durationStatus ==
                SleepTrafficLight.REGULAR &&
                favorable ->

            "La duración estuvo cerca del rango esperado y tu percepción del descanso fue favorable."


        else ->

            "La duración del sueño requiere revisión y la Escala de Atenas también identificó dificultades relacionadas con el descanso."
    }
}


// ============================================================================
// COLORES
// ============================================================================

fun sleepTrafficLightColor(
    status: SleepTrafficLight
): Color {


    return when (status) {


        SleepTrafficLight.GOOD ->

            Color(0xFF22C55E)


        SleepTrafficLight.REGULAR ->

            Color(0xFFF59E0B)


        SleepTrafficLight.LOW ->

            Color(0xFFDC2626)
    }
}


fun combinedSleepStatusColor(
    status: CombinedSleepStatus
): Color {


    return when (status) {


        CombinedSleepStatus.GOOD ->

            Color(0xFF22C55E)


        CombinedSleepStatus.REGULAR ->

            Color(0xFFF59E0B)


        CombinedSleepStatus.LOW ->

            Color(0xFFDC2626)
    }
}


// ============================================================================
// RECOMENDACIÓN DE HORAS
// ============================================================================

fun sleepRecommendationText(
    age: Int?
): String {


    return when {


        age != null &&
                age <= 17 ->

            "Para este grupo de edad se utiliza como referencia una duración de 8 a 10 horas."


        age != null ->

            "Para este grupo de edad se utiliza como referencia una duración habitual de 7 a 9 horas."


        else ->

            "La duración recomendada del sueño depende de la edad."
    }
}


// ============================================================================
// FORMATEAR HORA
// ============================================================================

fun formatSleepTime(
    instant: Instant
): String {


    val formatter =
        DateTimeFormatter.ofPattern(
            "HH:mm"
        )


    return instant
        .atZone(
            ZoneId.systemDefault()
        )
        .format(
            formatter
        )
}


// ============================================================================
// BARRA SUPERIOR
// ============================================================================

@Composable
fun HealthyTopBar(

    userName: String,

    onMenuClick: () -> Unit,

    onProfileClick: () -> Unit
) {


    Row(

        modifier =
            Modifier
                .fillMaxWidth()
                .widthIn(
                    max = 450.dp
                )
                .defaultMinSize(
                    minHeight = 56.dp
                ),

        verticalAlignment =
            Alignment.CenterVertically
    ) {


        IconButton(

            onClick =
                onMenuClick
        ) {


            Icon(

                imageVector =
                    Icons.Default.Menu,

                contentDescription =
                    "Abrir menú",

                tint =
                    Color(0xFF6B8E23),

                modifier =
                    Modifier.size(
                        30.dp
                    )
            )
        }


        Text(

            text =
                "Hola, $userName",

            color =
                Color(0xFF0F172A),

            fontSize =
                17.sp,

            fontWeight =
                FontWeight.Bold,

            modifier =
                Modifier.weight(
                    1f
                )
        )


        Box(

            modifier =
                Modifier
                    .size(
                        46.dp
                    )
                    .clip(
                        CircleShape
                    )
                    .background(
                        Color(0xFFFFE7C7)
                    ),

            contentAlignment =
                Alignment.Center
        ) {


            IconButton(

                onClick =
                    onProfileClick
            ) {


                Icon(

                    imageVector =
                        Icons.Default.AccountCircle,

                    contentDescription =
                        "Perfil",

                    tint =
                        Color(0xFF7C4A2D),

                    modifier =
                        Modifier.size(
                            34.dp
                        )
                )
            }
        }
    }
}


// ============================================================================
// INTRODUCCIÓN
// ============================================================================

@Composable
fun HealthyIntroCard() {


    val introText =
        """
        Los hábitos de vida saludable se construyen con acciones pequeñas que se repiten todos los días.

        Dormir bien, comer de forma equilibrada, hidratarse, moverse y reducir el estrés ayudan a mejorar el bienestar.
        """.trimIndent()


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
                    14.dp
                )
        ) {


            Row(

                verticalAlignment =
                    Alignment.CenterVertically
            ) {


                HealthyMascotPlaceholder(

                    modifier =
                        Modifier.size(
                            112.dp
                        )
                )


                Spacer(

                    modifier =
                        Modifier.width(
                            14.dp
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
                            "¿Sabías que...?",

                        fontSize =
                            15.sp,

                        fontWeight =
                            FontWeight.Bold,

                        color =
                            Color(0xFF0F766E)
                    )


                    Spacer(

                        modifier =
                            Modifier.height(
                                6.dp
                            )
                    )


                    Text(

                        text =
                            "Los hábitos saludables se relacionan con dormir bien, comer equilibradamente y manejar el estrés.",

                        fontSize =
                            13.sp,

                        color =
                            Color(0xFF334155),

                        lineHeight =
                            18.sp
                    )
                }
            }


            Spacer(

                modifier =
                    Modifier.height(
                        14.dp
                    )
            )


            TextToSpeechSmallButton(

                textToRead =
                    introText
            )
        }
    }
}


// ============================================================================
// MASCOTA
// ============================================================================

@Composable
fun HealthyMascotPlaceholder(
    modifier: Modifier = Modifier
) {


    Box(

        modifier =
            modifier
                .clip(
                    RoundedCornerShape(
                        24.dp
                    )
                )
                .background(

                    Brush.radialGradient(

                        colors =
                            listOf(

                                Color(0xFFB8F7E8),

                                Color(0xFFF7FCEB)
                            )
                    )
                ),

        contentAlignment =
            Alignment.Center
    ) {


        Icon(

            imageVector =
                Icons.Default.Spa,

            contentDescription =
                null,

            tint =
                Color(0xFF0F766E),

            modifier =
                Modifier.size(
                    54.dp
                )
        )
    }
}


// ============================================================================
// TEXT TO SPEECH
// ============================================================================

@Composable
fun TextToSpeechSmallButton(
    textToRead: String
) {


    val context =
        LocalContext.current


    var textToSpeech by remember {

        mutableStateOf<TextToSpeech?>(
            null
        )
    }


    var ready by remember {

        mutableStateOf(
            false
        )
    }


    DisposableEffect(Unit) {


        val tts =
            TextToSpeech(
                context
            ) { status ->


                if (
                    status ==
                    TextToSpeech.SUCCESS
                ) {


                    ready =
                        true
                }
            }


        textToSpeech =
            tts


        onDispose {


            tts.stop()


            tts.shutdown()
        }
    }


    Button(

        onClick = {


            if (ready) {


                textToSpeech?.language =
                    Locale.forLanguageTag(
                        "es-MX"
                    )


                textToSpeech?.speak(

                    textToRead,

                    TextToSpeech.QUEUE_FLUSH,

                    null,

                    "habitos_saludables_intro"
                )
            }
        },

        modifier =
            Modifier.height(
                40.dp
            ),

        shape =
            RoundedCornerShape(
                16.dp
            ),

        colors =
            ButtonDefaults.buttonColors(

                containerColor =
                    Color(0xFF0F766E),

                contentColor =
                    Color.White
            ),

        contentPadding =
            PaddingValues(
                horizontal = 14.dp
            )
    ) {


        Icon(

            imageVector =
                Icons.Default.VolumeUp,

            contentDescription =
                null,

            modifier =
                Modifier.size(
                    18.dp
                )
        )


        Spacer(

            modifier =
                Modifier.width(
                    8.dp
                )
        )


        Text(

            text =
                "Escuchar",

            fontSize =
                13.sp,

            fontWeight =
                FontWeight.Bold
        )
    }
}


// ============================================================================
// TARJETA DE BIENESTAR
// ============================================================================

@Composable
fun HealthyMiniCard(

    modifier: Modifier = Modifier,

    title: String,

    subtitle: String,

    backgroundColor: Color,

    icon:
    @Composable () -> Unit,

    onClick: () -> Unit
) {


    Card(

        modifier =
            modifier.defaultMinSize(
                minHeight = 150.dp
            ),

        shape =
            RoundedCornerShape(
                24.dp
            ),

        colors =
            CardDefaults.cardColors(

                containerColor =
                    backgroundColor
            ),

        elevation =
            CardDefaults.cardElevation(

                defaultElevation =
                    5.dp
            )
    ) {


        Column(

            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(
                        14.dp
                    ),

            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {


            Box(

                modifier =
                    Modifier
                        .size(
                            58.dp
                        )
                        .clip(
                            RoundedCornerShape(
                                18.dp
                            )
                        )
                        .background(

                            Color.White.copy(
                                alpha = 0.75f
                            )
                        ),

                contentAlignment =
                    Alignment.Center
            ) {


                icon()
            }


            Spacer(

                modifier =
                    Modifier.height(
                        10.dp
                    )
            )


            Text(

                text =
                    title,

                fontSize =
                    14.sp,

                fontWeight =
                    FontWeight.Bold,

                textAlign =
                    TextAlign.Center
            )


            Spacer(

                modifier =
                    Modifier.height(
                        5.dp
                    )
            )


            Text(

                text =
                    subtitle,

                fontSize =
                    12.sp,

                color =
                    Color(0xFF64748B),

                textAlign =
                    TextAlign.Center
            )


            Spacer(

                modifier =
                    Modifier.height(
                        12.dp
                    )
            )


            Button(

                onClick =
                    onClick,

                shape =
                    RoundedCornerShape(
                        16.dp
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
                        "Vamos"
                )
            }
        }
    }
}


// ============================================================================
// IMAGEN CENTRAL
// ============================================================================

@Composable
fun HealthyCentralImagePlaceholder() {


    Box(

        modifier =
            Modifier
                .size(
                    120.dp
                )
                .clip(
                    RoundedCornerShape(
                        28.dp
                    )
                )
                .background(

                    Brush.radialGradient(

                        colors =
                            listOf(

                                Color(0xFFEAF8FF),

                                Color(0xFFF7FCEB)
                            )
                    )
                ),

        contentAlignment =
            Alignment.Center
    ) {


        Icon(

            imageVector =
                Icons.Default.Fastfood,

            contentDescription =
                null,

            tint =
                Color(0xFF86A327),

            modifier =
                Modifier.size(
                    58.dp
                )
        )
    }
}


// ============================================================================
// TARJETAS DE HERRAMIENTAS
// ============================================================================

@Composable
fun HealthToolCard(

    title: String,

    subtitle: String,

    backgroundColor: Color,

    icon:
    @Composable () -> Unit,

    onClick: () -> Unit
) {


    Card(

        modifier =
            Modifier
                .fillMaxWidth()
                .defaultMinSize(
                    minHeight = 120.dp
                ),

        shape =
            RoundedCornerShape(
                24.dp
            ),

        colors =
            CardDefaults.cardColors(

                containerColor =
                    backgroundColor
            ),

        elevation =
            CardDefaults.cardElevation(

                defaultElevation =
                    5.dp
            )
    ) {


        Row(

            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(
                        18.dp
                    ),

            verticalAlignment =
                Alignment.CenterVertically
        ) {


            Box(

                modifier =
                    Modifier
                        .size(
                            60.dp
                        )
                        .clip(
                            RoundedCornerShape(
                                18.dp
                            )
                        )
                        .background(
                            Color.White
                        ),

                contentAlignment =
                    Alignment.Center
            ) {


                icon()
            }


            Spacer(

                modifier =
                    Modifier.width(
                        14.dp
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
                        title,

                    fontSize =
                        15.sp,

                    fontWeight =
                        FontWeight.Bold
                )


                Spacer(

                    modifier =
                        Modifier.height(
                            4.dp
                        )
                )


                Text(

                    text =
                        subtitle,

                    fontSize =
                        12.sp,

                    color =
                        Color(0xFF64748B)
                )
            }


            Button(

                onClick =
                    onClick,

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
                        "Vamos"
                )
            }
        }
    }
}


// ============================================================================
// FONDO PROPIO DE HÁBITOS SALUDABLES
// ============================================================================

/**
 * Fondo utilizado por HealthyHabitsScreen.
 *
 * Esta función se encuentra dentro de este mismo archivo
 * para que HealthyHabitsScreen no dependa de la función
 * backgroundGradient() declarada en MainActivity.
 *
 * Esto corrige el error:
 *
 * Unresolved reference 'backgroundGradient'
 */
fun healthyHabitsBackgroundGradient(): Brush {


    return Brush.radialGradient(

        colors =
            listOf(

                // Azul claro.
                Color(0xFFE0F7FA),

                // Verde agua.
                Color(0xFFD1F5E8),

                // Verde lima muy claro.
                Color(0xFFF0F4C3)
            )
    )
}