package com.example.vibralavida.habitos_saludables
import com.example.vibralavida.MainActivity
import com.example.vibralavida.R

// ============================================================================
// ANDROID
// ============================================================================

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build

// ============================================================================
// ACTIVITY RESULTS
// ============================================================================

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts

// ============================================================================
// COMPOSE
// ============================================================================

import androidx.compose.foundation.background

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll

// ============================================================================
// ICONOS
// ============================================================================

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Watch

// ============================================================================
// MATERIAL
// ============================================================================

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text

// ============================================================================
// ESTADOS
// ============================================================================

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

// ============================================================================
// UI
// ============================================================================

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ============================================================================
// CORE
// ============================================================================

import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

// ============================================================================
// FECHA
// ============================================================================

import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

// ============================================================================
// COROUTINES
// ============================================================================

import kotlinx.coroutines.delay


// ============================================================================
// SESIÓN MANUAL
// ============================================================================

data class ManualSleepSession(

    val startTimeMillis: Long,

    val endTimeMillis: Long,

    val durationMinutes: Long
)


// ============================================================================
// PREFERENCIAS DEL MODO SUEÑO
// ============================================================================

object SleepModePreferences {


    private const val PREFS_NAME =
        "sleep_mode_preferences"


    private const val KEY_ACTIVE =
        "sleep_mode_active"


    private const val KEY_START_TIME =
        "sleep_mode_start_time"


    private const val KEY_LAST_START =
        "last_sleep_start"


    private const val KEY_LAST_END =
        "last_sleep_end"


    private const val KEY_LAST_DURATION =
        "last_sleep_duration"


    fun startSleepMode(
        context: Context
    ) {


        val now =
            System.currentTimeMillis()


        context
            .getSharedPreferences(

                PREFS_NAME,

                Context.MODE_PRIVATE
            )

            .edit()

            .putBoolean(
                KEY_ACTIVE,
                true
            )

            .putLong(
                KEY_START_TIME,
                now
            )

            .apply()
    }


    fun finishSleepMode(
        context: Context
    ): ManualSleepSession? {


        val prefs =
            context.getSharedPreferences(

                PREFS_NAME,

                Context.MODE_PRIVATE
            )


        val start =
            prefs.getLong(

                KEY_START_TIME,

                -1L
            )


        if (
            start == -1L
        ) {

            return null
        }


        val end =
            System.currentTimeMillis()


        val duration =
            Duration.between(

                Instant.ofEpochMilli(
                    start
                ),

                Instant.ofEpochMilli(
                    end
                )

            ).toMinutes()


        val session =
            ManualSleepSession(

                startTimeMillis =
                    start,

                endTimeMillis =
                    end,

                durationMinutes =
                    duration
            )


        prefs.edit()

            .putBoolean(
                KEY_ACTIVE,
                false
            )

            .remove(
                KEY_START_TIME
            )

            .putLong(
                KEY_LAST_START,
                start
            )

            .putLong(
                KEY_LAST_END,
                end
            )

            .putLong(
                KEY_LAST_DURATION,
                duration
            )

            .apply()


        return session
    }


    fun isSleepModeActive(
        context: Context
    ): Boolean {


        return context
            .getSharedPreferences(

                PREFS_NAME,

                Context.MODE_PRIVATE
            )

            .getBoolean(

                KEY_ACTIVE,

                false
            )
    }


    fun getSleepStartTime(
        context: Context
    ): Long? {


        val value =
            context
                .getSharedPreferences(

                    PREFS_NAME,

                    Context.MODE_PRIVATE
                )

                .getLong(

                    KEY_START_TIME,

                    -1L
                )


        return if (
            value == -1L
        ) {

            null

        } else {

            value
        }
    }


    fun getLastSession(
        context: Context
    ): ManualSleepSession? {


        val prefs =
            context.getSharedPreferences(

                PREFS_NAME,

                Context.MODE_PRIVATE
            )


        val start =
            prefs.getLong(
                KEY_LAST_START,
                -1L
            )


        val end =
            prefs.getLong(
                KEY_LAST_END,
                -1L
            )


        val duration =
            prefs.getLong(
                KEY_LAST_DURATION,
                -1L
            )


        if (
            start == -1L ||
            end == -1L ||
            duration == -1L
        ) {

            return null
        }


        return ManualSleepSession(

            startTimeMillis =
                start,

            endTimeMillis =
                end,

            durationMinutes =
                duration
        )
    }
}


// ============================================================================
// NOTIFICACIÓN DE BUENOS DÍAS
// ============================================================================

object SleepNotificationManager {


    private const val CHANNEL_ID =
        "sleep_summary_channel"


    private const val NOTIFICATION_ID =
        2001


    fun createChannel(
        context: Context
    ) {


        if (
            Build.VERSION.SDK_INT >=
            Build.VERSION_CODES.O
        ) {


            val channel =
                NotificationChannel(

                    CHANNEL_ID,

                    "Resumen del sueño",

                    NotificationManager.IMPORTANCE_DEFAULT
                )


            val manager =
                context.getSystemService(

                    Context.NOTIFICATION_SERVICE

                ) as NotificationManager


            manager.createNotificationChannel(
                channel
            )
        }
    }


    fun showGoodMorningNotification(

        context: Context,

        session: ManualSleepSession,

        snoreSummary: SnoreSummary
    ) {


        createChannel(
            context
        )


        val openIntent =
            Intent(

                context,

                MainActivity::class.java
            )


        val pendingIntent =
            PendingIntent.getActivity(

                context,

                0,

                openIntent,

                PendingIntent.FLAG_UPDATE_CURRENT or
                        PendingIntent.FLAG_IMMUTABLE
            )


        val duration =
            formatManualSleepDuration(

                session.durationMinutes
            )


        val text =
            if (
                snoreSummary.possibleEvents >
                0
            ) {

                "Modo Sueño: $duration. " +
                        "${snoreSummary.possibleEvents} posibles eventos acústicos para revisar."

            } else {

                "Tu Modo Sueño estuvo activo durante $duration."
            }


        val notification =
            NotificationCompat.Builder(

                context,

                CHANNEL_ID
            )

                .setSmallIcon(
                    R.drawable.logo
                )

                .setContentTitle(
                    "Buenos días"
                )

                .setContentText(
                    text
                )

                .setStyle(

                    NotificationCompat
                        .BigTextStyle()
                        .bigText(
                            text
                        )
                )

                .setAutoCancel(
                    true
                )

                .setContentIntent(
                    pendingIntent
                )

                .build()


        if (
            Build.VERSION.SDK_INT <
            Build.VERSION_CODES.TIRAMISU ||

            ContextCompat.checkSelfPermission(

                context,

                Manifest.permission.POST_NOTIFICATIONS

            ) ==
            PackageManager.PERMISSION_GRANTED
        ) {


            NotificationManagerCompat
                .from(
                    context
                )

                .notify(

                    NOTIFICATION_ID,

                    notification
                )
        }
    }
}


// ============================================================================
// MODO SUEÑO
// ============================================================================

@Composable
fun SleepModeScreen(

    onBack: () -> Unit,

    onSleepFinished: () -> Unit
) {


    val context =
        LocalContext.current


    // ========================================================================
    // PERMISO MICRÓFONO
    // ========================================================================

    var microphonePermissionGranted by remember {

        mutableStateOf(

            ContextCompat.checkSelfPermission(

                context,

                Manifest.permission.RECORD_AUDIO

            ) ==
                    PackageManager.PERMISSION_GRANTED
        )
    }


    // ========================================================================
    // PERMISO NOTIFICACIONES
    // ========================================================================

    var notificationPermissionGranted by remember {

        mutableStateOf(

            Build.VERSION.SDK_INT <
                    Build.VERSION_CODES.TIRAMISU ||

                    ContextCompat.checkSelfPermission(

                        context,

                        Manifest.permission.POST_NOTIFICATIONS

                    ) ==
                    PackageManager.PERMISSION_GRANTED
        )
    }


    // ========================================================================
    // ESTADOS
    // ========================================================================

    var sleepModeActive by remember {

        mutableStateOf(

            SleepModePreferences
                .isSleepModeActive(
                    context
                )
        )
    }


    var startTimeMillis by remember {

        mutableLongStateOf(

            SleepModePreferences
                .getSleepStartTime(
                    context
                )
                ?: 0L
        )
    }


    var elapsedMinutes by remember {

        mutableLongStateOf(
            0L
        )
    }


    var lastSession by remember {

        mutableStateOf(

            SleepModePreferences
                .getLastSession(
                    context
                )
        )
    }


    var lastSnoreSummary by remember {

        mutableStateOf(

            SnorePreferences
                .getSummary(
                    context
                )
        )
    }


    // ========================================================================
    // INICIAR REALMENTE
    // ========================================================================

    fun startSleepModeAndService() {


        SleepModePreferences
            .startSleepMode(
                context
            )


        startTimeMillis =
            SleepModePreferences
                .getSleepStartTime(
                    context
                )
                ?: 0L


        elapsedMinutes =
            0L


        sleepModeActive =
            true


        // ================================================================
        // INICIAR FOREGROUND SERVICE
        // ================================================================

        val serviceIntent =
            Intent(

                context,

                SleepMonitoringService::class.java
            ).apply {


                action =
                    SleepMonitoringService.ACTION_START
            }


        ContextCompat
            .startForegroundService(

                context,

                serviceIntent
            )
    }


    // ========================================================================
    // PERMISO MICRO
    // ========================================================================

    val microphonePermissionLauncher =
        rememberLauncherForActivityResult(

            contract =
                ActivityResultContracts.RequestPermission()

        ) {
                granted ->


            microphonePermissionGranted =
                granted


            // IMPORTANTE:
            //
            // El servicio se inicia inmediatamente después
            // de que el usuario conceda el permiso,
            // mientras la Activity todavía está visible.
            if (granted) {


                startSleepModeAndService()
            }
        }


    // ========================================================================
    // PERMISO NOTIFICACIONES
    // ========================================================================

    val notificationPermissionLauncher =
        rememberLauncherForActivityResult(

            contract =
                ActivityResultContracts.RequestPermission()

        ) {
                granted ->


            notificationPermissionGranted =
                granted
        }


    // ========================================================================
    // CANAL
    // ========================================================================

    LaunchedEffect(Unit) {


        SleepNotificationManager
            .createChannel(
                context
            )
    }


    // ========================================================================
    // CONTADOR
    // ========================================================================

    LaunchedEffect(

        sleepModeActive,

        startTimeMillis

    ) {


        while (
            sleepModeActive &&
            startTimeMillis >
            0L
        ) {


            elapsedMinutes =
                Duration.between(

                    Instant.ofEpochMilli(
                        startTimeMillis
                    ),

                    Instant.now()

                ).toMinutes()


            delay(
                60_000L
            )
        }
    }


    // ========================================================================
    // UI
    // ========================================================================

    Box(

        modifier =
            Modifier
                .fillMaxSize()

                .background(

                    Brush.verticalGradient(

                        listOf(

                            Color(0xFF071426),

                            Color(0xFF0B2340),

                            Color(0xFF12304F)
                        )
                    )
                )

                .statusBarsPadding()

                .navigationBarsPadding()
    ) {


        Column(

            modifier =
                Modifier
                    .fillMaxSize()

                    .verticalScroll(
                        rememberScrollState()
                    )

                    .padding(
                        22.dp
                    ),

            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {


            Spacer(

                modifier =
                    Modifier.height(
                        20.dp
                    )
            )


            Box(

                modifier =
                    Modifier
                        .size(
                            100.dp
                        )

                        .background(

                            Color.White.copy(
                                alpha =
                                    0.10f
                            ),

                            CircleShape
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
                            58.dp
                        )
                )
            }


            Spacer(

                modifier =
                    Modifier.height(
                        20.dp
                    )
            )


            // =================================================================
            // NO ACTIVO
            // =================================================================

            if (
                !sleepModeActive
            ) {


                Text(

                    text =
                        "Modo sueño",

                    color =
                        Color.White,

                    fontSize =
                        30.sp,

                    fontWeight =
                        FontWeight.Bold
                )


                Spacer(

                    modifier =
                        Modifier.height(
                            6.dp
                        )
                )


                Text(

                    text =
                        "Prepara Vibra la vida para acompañar tu descanso.",

                    color =
                        Color(0xFFCBD5E1),

                    fontSize =
                        14.sp,

                    textAlign =
                        TextAlign.Center
                )


                Spacer(

                    modifier =
                        Modifier.height(
                            22.dp
                        )
                )


                // =============================================================
                // PREPARACIÓN
                // =============================================================

                SleepPreparationCard(

                    microphoneReady =
                        microphonePermissionGranted,

                    notificationsReady =
                        notificationPermissionGranted
                )


                // =============================================================
                // ÚLTIMA SESIÓN
                // =============================================================

                lastSession?.let {
                        session ->


                    Spacer(

                        modifier =
                            Modifier.height(
                                16.dp
                            )
                    )


                    LastSleepSessionCard(

                        session =
                            session,

                        snoreSummary =
                            lastSnoreSummary
                    )
                }


                Spacer(

                    modifier =
                        Modifier.height(
                            24.dp
                        )
                )


                // =============================================================
                // INICIAR
                // =============================================================

                Button(

                    onClick = {


                        // =====================================================
                        // NOTIFICACIONES
                        // =====================================================

                        if (
                            Build.VERSION.SDK_INT >=
                            Build.VERSION_CODES.TIRAMISU &&
                            !notificationPermissionGranted
                        ) {


                            notificationPermissionLauncher
                                .launch(

                                    Manifest.permission
                                        .POST_NOTIFICATIONS
                                )
                        }


                        // =====================================================
                        // MICRÓFONO
                        // =====================================================

                        if (
                            microphonePermissionGranted
                        ) {


                            startSleepModeAndService()

                        } else {


                            microphonePermissionLauncher
                                .launch(

                                    Manifest.permission
                                        .RECORD_AUDIO
                                )
                        }
                    },

                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(
                                58.dp
                            ),

                    shape =
                        RoundedCornerShape(
                            20.dp
                        ),

                    colors =
                        ButtonDefaults.buttonColors(

                            containerColor =
                                Color(0xFF0EA5E9),

                            contentColor =
                                Color.White
                        )
                ) {


                    Text(

                        text =
                            "Iniciar modo sueño",

                        fontWeight =
                            FontWeight.Bold,

                        fontSize =
                            17.sp
                    )
                }


                Spacer(

                    modifier =
                        Modifier.height(
                            12.dp
                        )
                )


                Button(

                    onClick =
                        onBack,

                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(
                                52.dp
                            ),

                    colors =
                        ButtonDefaults.buttonColors(

                            containerColor =
                                Color.White.copy(
                                    alpha =
                                        0.10f
                                ),

                            contentColor =
                                Color.White
                        ),

                    shape =
                        RoundedCornerShape(
                            18.dp
                        )
                ) {


                    Text(
                        text =
                            "Volver"
                    )
                }

            } else {


                // =================================================================
                // ACTIVO
                // =================================================================

                Text(

                    text =
                        "Modo sueño activo",

                    color =
                        Color(0xFF7DD3FC),

                    fontSize =
                        27.sp,

                    fontWeight =
                        FontWeight.Bold
                )


                Spacer(

                    modifier =
                        Modifier.height(
                            5.dp
                        )
                )


                Text(

                    text =
                        "Que descanses",

                    color =
                        Color.White,

                    fontSize =
                        18.sp
                )


                Spacer(

                    modifier =
                        Modifier.height(
                            22.dp
                        )
                )


                // =============================================================
                // DURACIÓN
                // =============================================================

                Card(

                    modifier =
                        Modifier.fillMaxWidth(),

                    colors =
                        CardDefaults.cardColors(

                            containerColor =
                                Color.White.copy(
                                    alpha =
                                        0.10f
                                )
                        ),

                    shape =
                        RoundedCornerShape(
                            24.dp
                        )
                ) {


                    Column(

                        modifier =
                            Modifier
                                .fillMaxWidth()

                                .padding(
                                    20.dp
                                ),

                        horizontalAlignment =
                            Alignment.CenterHorizontally
                    ) {


                        Text(

                            text =
                                "Comenzaste a descansar",

                            color =
                                Color(0xFFCBD5E1),

                            fontSize =
                                13.sp
                        )


                        Spacer(

                            modifier =
                                Modifier.height(
                                    8.dp
                                )
                        )


                        Text(

                            text =
                                formatSleepModeTime(
                                    startTimeMillis
                                ),

                            color =
                                Color.White,

                            fontSize =
                                34.sp,

                            fontWeight =
                                FontWeight.Bold
                        )


                        Spacer(

                            modifier =
                                Modifier.height(
                                    12.dp
                                )
                        )


                        Text(

                            text =
                                formatManualSleepDuration(
                                    elapsedMinutes
                                ),

                            color =
                                Color(0xFF7DD3FC),

                            fontSize =
                                16.sp,

                            fontWeight =
                                FontWeight.Bold
                        )
                    }
                }


                Spacer(

                    modifier =
                        Modifier.height(
                            16.dp
                        )
                )


                // =============================================================
                // MICRÓFONO ACTIVO
                // =============================================================

                ActiveMonitoringCard()


                Spacer(

                    modifier =
                        Modifier.height(
                            28.dp
                        )
                )


                // =============================================================
                // YA DESPERTÉ
                // =============================================================

                Button(

                    onClick = {


                        // =====================================================
                        // DETENER MICRÓFONO
                        // =====================================================

                        val stopIntent =
                            Intent(

                                context,

                                SleepMonitoringService::class.java
                            ).apply {


                                action =
                                    SleepMonitoringService.ACTION_STOP
                            }


                        context.startService(
                            stopIntent
                        )


                        // Esperamos muy poco no es necesario.
                        //
                        // Leemos lo acumulado hasta este momento.
                        val summary =
                            SnorePreferences
                                .getSummary(
                                    context
                                )


                        // =====================================================
                        // TERMINAR SESIÓN
                        // =====================================================

                        val session =
                            SleepModePreferences
                                .finishSleepMode(
                                    context
                                )


                        if (
                            session != null
                        ) {


                            lastSession =
                                session


                            lastSnoreSummary =
                                summary


                            SleepNotificationManager
                                .showGoodMorningNotification(

                                    context =
                                        context,

                                    session =
                                        session,

                                    snoreSummary =
                                        summary
                                )
                        }


                        sleepModeActive =
                            false


                        startTimeMillis =
                            0L


                        elapsedMinutes =
                            0L


                        onSleepFinished()
                    },

                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(
                                60.dp
                            ),

                    shape =
                        RoundedCornerShape(
                            20.dp
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
                            "Ya desperté",

                        fontSize =
                            18.sp,

                        fontWeight =
                            FontWeight.Bold
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
                        "El análisis acústico es experimental y no permite diagnosticar apnea del sueño.",

                    color =
                        Color(0xFF94A3B8),

                    fontSize =
                        10.sp,

                    textAlign =
                        TextAlign.Center,

                    lineHeight =
                        14.sp
                )
            }


            Spacer(

                modifier =
                    Modifier.height(
                        24.dp
                    )
            )
        }
    }
}


// ============================================================================
// PREPARACIÓN
// ============================================================================

@Composable
private fun SleepPreparationCard(

    microphoneReady: Boolean,

    notificationsReady: Boolean
) {


    Card(

        modifier =
            Modifier.fillMaxWidth(),

        colors =
            CardDefaults.cardColors(

                containerColor =
                    Color.White.copy(
                        alpha =
                            0.10f
                    )
            ),

        shape =
            RoundedCornerShape(
                24.dp
            )
    ) {


        Column(

            modifier =
                Modifier.padding(
                    18.dp
                )
        ) {


            SleepStatusRow(

                imageVector =
                    Icons.Default.Watch,

                title =
                    "Wearable",

                subtitle =
                    "Health Connect preparado",

                tint =
                    Color(0xFF7DD3FC)
            )


            Spacer(
                Modifier.height(
                    16.dp
                )
            )


            SleepStatusRow(

                imageVector =
                    Icons.Default.Mic,

                title =
                    "Micrófono",

                subtitle =
                    if (
                        microphoneReady
                    ) {

                        "Preparado para análisis nocturno"

                    } else {

                        "Se solicitará permiso al iniciar"
                    },

                tint =
                    Color(0xFFC084FC)
            )


            Spacer(
                Modifier.height(
                    16.dp
                )
            )


            SleepStatusRow(

                imageVector =
                    Icons.Default.Notifications,

                title =
                    "Notificación al despertar",

                subtitle =
                    if (
                        notificationsReady
                    ) {

                        "Preparada"

                    } else {

                        "Permiso pendiente"
                    },

                tint =
                    Color(0xFFFACC15)
            )
        }
    }
}


// ============================================================================
// MONITOREO ACTIVO
// ============================================================================

@Composable
private fun ActiveMonitoringCard() {


    Card(

        modifier =
            Modifier.fillMaxWidth(),

        colors =
            CardDefaults.cardColors(

                containerColor =
                    Color.White.copy(
                        alpha =
                            0.08f
                    )
            ),

        shape =
            RoundedCornerShape(
                22.dp
            )
    ) {


        Column(

            modifier =
                Modifier.padding(
                    18.dp
                )
        ) {


            SleepStatusRow(

                imageVector =
                    Icons.Default.CheckCircle,

                title =
                    "Registro iniciado",

                subtitle =
                    "La hora de inicio quedó guardada.",

                tint =
                    Color(0xFF4ADE80)
            )


            Spacer(
                Modifier.height(
                    16.dp
                )
            )


            SleepStatusRow(

                imageVector =
                    Icons.Default.GraphicEq,

                title =
                    "Análisis acústico activo",

                subtitle =
                    "Buscando posibles eventos compatibles con ronquido.",

                tint =
                    Color(0xFFC084FC)
            )
        }
    }
}


// ============================================================================
// ÚLTIMA SESIÓN
// ============================================================================

@Composable
private fun LastSleepSessionCard(

    session: ManualSleepSession,

    snoreSummary: SnoreSummary
) {


    Card(

        modifier =
            Modifier.fillMaxWidth(),

        colors =
            CardDefaults.cardColors(

                containerColor =
                    Color.White.copy(
                        alpha =
                            0.08f
                    )
            ),

        shape =
            RoundedCornerShape(
                22.dp
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


                Icon(

                    imageVector =
                        Icons.Default.History,

                    contentDescription =
                        null,

                    tint =
                        Color(0xFF7DD3FC)
                )


                Spacer(
                    Modifier.width(
                        10.dp
                    )
                )


                Text(

                    text =
                        "Último Modo Sueño",

                    color =
                        Color.White,

                    fontWeight =
                        FontWeight.Bold
                )
            }


            Spacer(
                Modifier.height(
                    12.dp
                )
            )


            Text(

                text =
                    "${formatSleepModeTime(session.startTimeMillis)} - " +
                            formatSleepModeTime(
                                session.endTimeMillis
                            ),

                color =
                    Color(0xFFCBD5E1)
            )


            Text(

                text =
                    formatManualSleepDuration(
                        session.durationMinutes
                    ),

                color =
                    Color(0xFF7DD3FC),

                fontWeight =
                    FontWeight.Bold
            )


            Spacer(
                Modifier.height(
                    14.dp
                )
            )


            Text(

                text =
                    "Análisis acústico",

                color =
                    Color.White,

                fontWeight =
                    FontWeight.Bold,

                fontSize =
                    13.sp
            )


            Spacer(
                Modifier.height(
                    5.dp
                )
            )


            Text(

                text =
                    "Posibles eventos: ${snoreSummary.possibleEvents}",

                color =
                    Color(0xFFCBD5E1),

                fontSize =
                    12.sp
            )


            Text(

                text =
                    "Tiempo compatible: " +
                            formatSnoreDuration(
                                snoreSummary.possibleSnoreSeconds
                            ),

                color =
                    Color(0xFFCBD5E1),

                fontSize =
                    12.sp
            )


            Spacer(
                Modifier.height(
                    8.dp
                )
            )


            Text(

                text =
                    "Estos eventos son orientativos y no representan un diagnóstico.",

                color =
                    Color(0xFF94A3B8),

                fontSize =
                    10.sp,

                lineHeight =
                    14.sp
            )
        }
    }
}


// ============================================================================
// FILA
// ============================================================================

@Composable
private fun SleepStatusRow(

    imageVector:
    androidx.compose.ui.graphics.vector.ImageVector,

    title: String,

    subtitle: String,

    tint: Color
) {


    Row(

        verticalAlignment =
            Alignment.CenterVertically
    ) {


        Box(

            modifier =
                Modifier
                    .size(
                        46.dp
                    )

                    .background(

                        Color.White.copy(
                            alpha =
                                0.08f
                        ),

                        RoundedCornerShape(
                            15.dp
                        )
                    ),

            contentAlignment =
                Alignment.Center
        ) {


            Icon(

                imageVector =
                    imageVector,

                contentDescription =
                    null,

                tint =
                    tint
            )
        }


        Spacer(
            Modifier.width(
                12.dp
            )
        )


        Column {


            Text(

                text =
                    title,

                color =
                    Color.White,

                fontWeight =
                    FontWeight.Bold,

                fontSize =
                    14.sp
            )


            Text(

                text =
                    subtitle,

                color =
                    Color(0xFF94A3B8),

                fontSize =
                    11.sp
            )
        }
    }
}


// ============================================================================
// HORA
// ============================================================================

private fun formatSleepModeTime(
    millis: Long
): String {


    if (
        millis <=
        0L
    ) {

        return "--:--"
    }


    return Instant
        .ofEpochMilli(
            millis
        )

        .atZone(
            ZoneId.systemDefault()
        )

        .format(

            DateTimeFormatter
                .ofPattern(
                    "HH:mm"
                )
        )
}


// ============================================================================
// DURACIÓN MODO SUEÑO
// ============================================================================

fun formatManualSleepDuration(
    totalMinutes: Long
): String {


    val hours =
        totalMinutes /
                60


    val minutes =
        totalMinutes %
                60


    return if (
        hours >
        0
    ) {

        "$hours h $minutes min"

    } else {

        "$minutes min"
    }
}


// ============================================================================
// DURACIÓN RONQUIDOS
// ============================================================================

private fun formatSnoreDuration(
    seconds: Long
): String {


    val minutes =
        seconds /
                60


    val remainingSeconds =
        seconds %
                60


    return if (
        minutes >
        0
    ) {

        "$minutes min $remainingSeconds s"

    } else {

        "$remainingSeconds s"
    }
}