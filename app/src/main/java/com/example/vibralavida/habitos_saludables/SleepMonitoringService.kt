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
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Build
import android.os.IBinder

// ============================================================================
// ANDROIDX
// ============================================================================

import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import androidx.core.content.ContextCompat

// ============================================================================
// JAVA / KOTLIN
// ============================================================================

import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.math.roundToLong


// ============================================================================
// SERVICIO DE MONITOREO NOCTURNO
// ============================================================================

/**
 * Mantiene el micrófono activo durante el Modo Sueño.
 *
 * FUNCIONAMIENTO:
 *
 * Micrófono
 *      ↓
 * pequeños bloques PCM
 *      ↓
 * SnoreAnalyzer
 *      ↓
 * posibles episodios acústicos
 *      ↓
 * SharedPreferences
 *
 *
 * IMPORTANTE:
 *
 * - NO guarda una grabación completa.
 * - NO guarda conversaciones.
 * - NO diagnostica apnea.
 * - NO diagnostica ronquidos.
 *
 * Solamente conserva métricas resumidas.
 */
class SleepMonitoringService : Service() {


    // ========================================================================
    // CONFIGURACIÓN
    // ========================================================================

    companion object {


        const val ACTION_START =
            "com.example.vibralavida.START_SLEEP_MONITORING"


        const val ACTION_STOP =
            "com.example.vibralavida.STOP_SLEEP_MONITORING"


        private const val CHANNEL_ID =
            "sleep_monitoring_channel"


        private const val NOTIFICATION_ID =
            3101


        // 16 kHz es suficiente para este
        // análisis acústico básico.
        private const val SAMPLE_RATE =
            16_000
    }


    // ========================================================================
    // VARIABLES
    // ========================================================================

    private val running =
        AtomicBoolean(
            false
        )


    private val executor =
        Executors.newSingleThreadExecutor()


    private var audioRecord:
            AudioRecord? =
        null


    private val snoreAnalyzer =
        SnoreAnalyzer()


    // ========================================================================
    // CONTROL DE EVENTOS
    // ========================================================================

    /**
     * Cantidad de bloques consecutivos
     * compatibles con ronquido.
     */
    private var currentPossibleSnoreBlocks =
        0


    /**
     * ¿Estamos dentro de un posible episodio?
     */
    private var possibleSnoreEventActive =
        false


    /**
     * Bloques silenciosos desde el último
     * bloque compatible.
     */
    private var quietBlocks =
        0


    // ========================================================================
    // ON CREATE
    // ========================================================================

    override fun onCreate() {

        super.onCreate()


        createNotificationChannel()
    }


    // ========================================================================
    // START COMMAND
    // ========================================================================

    override fun onStartCommand(

        intent: Intent?,

        flags: Int,

        startId: Int

    ): Int {


        when (
            intent?.action
        ) {


            ACTION_STOP -> {

                stopMonitoring()
            }


            else -> {

                startMonitoring()
            }
        }


        return START_STICKY
    }


    // ========================================================================
    // BINDER
    // ========================================================================

    override fun onBind(
        intent: Intent?
    ): IBinder? {

        return null
    }


    // ========================================================================
    // INICIAR MONITOREO
    // ========================================================================

    private fun startMonitoring() {


        // Evita iniciar dos veces.
        if (
            running.get()
        ) {

            return
        }


        // ====================================================================
        // PERMISO
        // ====================================================================

        if (
            ContextCompat.checkSelfPermission(

                this,

                Manifest.permission.RECORD_AUDIO

            ) !=
            PackageManager.PERMISSION_GRANTED
        ) {

            stopSelf()

            return
        }


        // ====================================================================
        // FOREGROUND SERVICE
        // ====================================================================

        startAsForegroundService()


        // ====================================================================
        // RESET DE RESULTADOS
        // ====================================================================

        SnorePreferences.startNewAnalysis(
            this
        )


        running.set(
            true
        )


        // ====================================================================
        // HILO DE AUDIO
        // ====================================================================

        executor.execute {

            captureAudio()
        }
    }


    // ========================================================================
    // FOREGROUND
    // ========================================================================

    private fun startAsForegroundService() {


        val openAppIntent =
            Intent(
                this,
                MainActivity::class.java
            )


        val pendingIntent =
            PendingIntent.getActivity(

                this,

                0,

                openAppIntent,

                PendingIntent.FLAG_UPDATE_CURRENT or
                        PendingIntent.FLAG_IMMUTABLE
            )


        val notification =
            NotificationCompat.Builder(

                this,

                CHANNEL_ID
            )

                .setSmallIcon(
                    R.drawable.logo
                )

                .setContentTitle(
                    "Modo sueño activo"
                )

                .setContentText(
                    "Vibra la vida está analizando sonidos durante tu descanso."
                )

                .setOngoing(
                    true
                )

                .setPriority(
                    NotificationCompat.PRIORITY_LOW
                )

                .setContentIntent(
                    pendingIntent
                )

                .build()


        // ====================================================================
        // ANDROID 11+
        // ====================================================================

        if (
            Build.VERSION.SDK_INT >=
            Build.VERSION_CODES.R
        ) {


            ServiceCompat.startForeground(

                this,

                NOTIFICATION_ID,

                notification,

                ServiceInfo
                    .FOREGROUND_SERVICE_TYPE_MICROPHONE
            )

        } else {


            ServiceCompat.startForeground(

                this,

                NOTIFICATION_ID,

                notification,

                0
            )
        }
    }


    // ========================================================================
    // CAPTURAR AUDIO
    // ========================================================================

    private fun captureAudio() {


        val minimumBufferSize =
            AudioRecord.getMinBufferSize(

                SAMPLE_RATE,

                AudioFormat.CHANNEL_IN_MONO,

                AudioFormat.ENCODING_PCM_16BIT
            )


        if (
            minimumBufferSize <= 0
        ) {

            stopMonitoring()

            return
        }


        // Utilizamos al menos un segundo aproximado
        // de audio por bloque.
        val bufferSize =
            maxOf(

                minimumBufferSize,

                SAMPLE_RATE
            )


        val recorder =
            try {

                AudioRecord(

                    MediaRecorder.AudioSource.VOICE_RECOGNITION,

                    SAMPLE_RATE,

                    AudioFormat.CHANNEL_IN_MONO,

                    AudioFormat.ENCODING_PCM_16BIT,

                    bufferSize * 2
                )

            } catch (_: Exception) {

                stopMonitoring()

                return
            }


        if (
            recorder.state !=
            AudioRecord.STATE_INITIALIZED
        ) {

            recorder.release()

            stopMonitoring()

            return
        }


        audioRecord =
            recorder


        val buffer =
            ShortArray(
                bufferSize
            )


        try {


            recorder.startRecording()


            while (
                running.get()
            ) {


                val read =
                    recorder.read(

                        buffer,

                        0,

                        buffer.size
                    )


                if (
                    read >
                    0
                ) {


                    val analysis =
                        snoreAnalyzer.analyze(

                            samples =
                                buffer,

                            validSamples =
                                read
                        )


                    processAnalysis(

                        analysis =
                            analysis,

                        samplesRead =
                            read
                    )
                }
            }

        } catch (_: Exception) {


            // Terminamos de forma segura.

        } finally {


            try {

                recorder.stop()

            } catch (_: Exception) {
            }


            recorder.release()


            audioRecord =
                null
        }
    }


    // ========================================================================
    // PROCESAR BLOQUE
    // ========================================================================

    private fun processAnalysis(

        analysis: AudioAnalysisResult,

        samplesRead: Int

    ) {


        // Duración aproximada del bloque.
        val blockSeconds =
            samplesRead.toDouble() /
                    SAMPLE_RATE.toDouble()


        // ====================================================================
        // BLOQUE COMPATIBLE
        // ====================================================================

        if (
            analysis.possibleSnore
        ) {


            currentPossibleSnoreBlocks++


            quietBlocks =
                0


            SnorePreferences
                .addPossibleSnoreTime(

                    context =
                        this,

                    seconds =
                        blockSeconds
                )


            // Después de varios bloques consecutivos
            // consideramos que inició un episodio.
            if (
                currentPossibleSnoreBlocks >= 2 &&
                !possibleSnoreEventActive
            ) {


                possibleSnoreEventActive =
                    true


                SnorePreferences
                    .registerPossibleSnoreEvent(
                        this
                    )
            }

        } else {


            currentPossibleSnoreBlocks =
                0


            if (
                possibleSnoreEventActive
            ) {


                quietBlocks++


                // Dos bloques sin coincidencia:
                // cerramos el episodio.
                if (
                    quietBlocks >= 2
                ) {


                    possibleSnoreEventActive =
                        false


                    quietBlocks =
                        0
                }
            }
        }
    }


    // ========================================================================
    // DETENER
    // ========================================================================

    private fun stopMonitoring() {


        running.set(
            false
        )


        try {

            audioRecord?.stop()

        } catch (_: Exception) {
        }


        SnorePreferences.finishAnalysis(
            this
        )


        stopForeground(
            STOP_FOREGROUND_REMOVE
        )


        stopSelf()
    }


    // ========================================================================
    // CANAL
    // ========================================================================

    private fun createNotificationChannel() {


        if (
            Build.VERSION.SDK_INT >=
            Build.VERSION_CODES.O
        ) {


            val channel =
                NotificationChannel(

                    CHANNEL_ID,

                    "Monitoreo del sueño",

                    NotificationManager.IMPORTANCE_LOW
                ).apply {


                    description =
                        "Mantiene activo el análisis nocturno durante el Modo Sueño."
                }


            val manager =
                getSystemService(
                    Context.NOTIFICATION_SERVICE
                ) as NotificationManager


            manager.createNotificationChannel(
                channel
            )
        }
    }


    // ========================================================================
    // DESTRUIR
    // ========================================================================

    override fun onDestroy() {


        running.set(
            false
        )


        try {

            audioRecord?.stop()

        } catch (_: Exception) {
        }


        audioRecord?.release()


        audioRecord =
            null


        super.onDestroy()
    }
}


// ============================================================================
// RESULTADO DE RONQUIDOS
// ============================================================================

data class SnoreSummary(

    val possibleEvents: Int,

    val possibleSnoreSeconds: Long,

    val analysisStartMillis: Long,

    val analysisEndMillis: Long
) {


    val possibleSnoreMinutes: Long
        get() {

            return possibleSnoreSeconds /
                    60L
        }
}


// ============================================================================
// STORAGE DE RESULTADOS
// ============================================================================

object SnorePreferences {


    private const val PREFS =
        "snore_analysis_preferences"


    private const val KEY_EVENTS =
        "possible_snore_events"


    private const val KEY_SECONDS =
        "possible_snore_seconds"


    private const val KEY_START =
        "analysis_start"


    private const val KEY_END =
        "analysis_end"


    // Acumulamos fracciones de segundo
    // durante la ejecución.
    private var fractionalSeconds =
        0.0


    // ========================================================================
    // NUEVO ANÁLISIS
    // ========================================================================

    fun startNewAnalysis(
        context: Context
    ) {


        fractionalSeconds =
            0.0


        context
            .getSharedPreferences(

                PREFS,

                Context.MODE_PRIVATE
            )

            .edit()

            .putInt(
                KEY_EVENTS,
                0
            )

            .putLong(
                KEY_SECONDS,
                0L
            )

            .putLong(
                KEY_START,
                System.currentTimeMillis()
            )

            .putLong(
                KEY_END,
                0L
            )

            .apply()
    }


    // ========================================================================
    // REGISTRAR EVENTO
    // ========================================================================

    fun registerPossibleSnoreEvent(
        context: Context
    ) {


        val prefs =
            context.getSharedPreferences(

                PREFS,

                Context.MODE_PRIVATE
            )


        val current =
            prefs.getInt(
                KEY_EVENTS,
                0
            )


        prefs.edit()

            .putInt(
                KEY_EVENTS,
                current + 1
            )

            .apply()
    }


    // ========================================================================
    // ACUMULAR TIEMPO
    // ========================================================================

    @Synchronized
    fun addPossibleSnoreTime(

        context: Context,

        seconds: Double

    ) {


        fractionalSeconds +=
            seconds


        if (
            fractionalSeconds <
            1.0
        ) {

            return
        }


        val wholeSeconds =
            fractionalSeconds
                .toLong()


        fractionalSeconds -=
            wholeSeconds


        val prefs =
            context.getSharedPreferences(

                PREFS,

                Context.MODE_PRIVATE
            )


        val current =
            prefs.getLong(
                KEY_SECONDS,
                0L
            )


        prefs.edit()

            .putLong(

                KEY_SECONDS,

                current +
                        wholeSeconds
            )

            .apply()
    }


    // ========================================================================
    // FINALIZAR
    // ========================================================================

    fun finishAnalysis(
        context: Context
    ) {


        context
            .getSharedPreferences(

                PREFS,

                Context.MODE_PRIVATE
            )

            .edit()

            .putLong(

                KEY_END,

                System.currentTimeMillis()
            )

            .apply()
    }


    // ========================================================================
    // LEER RESUMEN
    // ========================================================================

    fun getSummary(
        context: Context
    ): SnoreSummary {


        val prefs =
            context.getSharedPreferences(

                PREFS,

                Context.MODE_PRIVATE
            )


        return SnoreSummary(

            possibleEvents =
                prefs.getInt(
                    KEY_EVENTS,
                    0
                ),

            possibleSnoreSeconds =
                prefs.getLong(
                    KEY_SECONDS,
                    0L
                ),

            analysisStartMillis =
                prefs.getLong(
                    KEY_START,
                    0L
                ),

            analysisEndMillis =
                prefs.getLong(
                    KEY_END,
                    0L
                )
        )
    }
}