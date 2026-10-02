package com.example.vibralavida.agenda.citas

import android.Manifest
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager

import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

import com.example.vibralavida.MainActivity
import com.example.vibralavida.R


// ============================================================================
// RECEIVER DE RECORDATORIOS DE CITAS
// ============================================================================
//
// Recibe la alarma programada por AlarmManager y muestra la notificación.
//
// ============================================================================

class CitaReminderReceiver :
    BroadcastReceiver() {


    override fun onReceive(
        context: Context,
        intent: Intent
    ) {

        // ====================================================================
        // DATOS DE LA CITA
        // ====================================================================

        val citaId =
            intent.getStringExtra(
                EXTRA_CITA_ID
            ).orEmpty()


        val especialista =
            intent.getStringExtra(
                EXTRA_ESPECIALISTA
            )
                ?.takeIf {
                    it.isNotBlank()
                }
                ?: "tu profesional de salud"


        val fecha =
            intent.getStringExtra(
                EXTRA_FECHA
            ).orEmpty()


        val hora =
            intent.getStringExtra(
                EXTRA_HORA
            ).orEmpty()


        val tipoRecordatorio =
            intent.getStringExtra(
                EXTRA_TIPO_RECORDATORIO
            ).orEmpty()


        // ====================================================================
        // TEXTO DE LA NOTIFICACIÓN
        // ====================================================================

        val titulo =
            when (
                tipoRecordatorio
            ) {

                TIPO_24_HORAS ->
                    "Tu cita es mañana"

                TIPO_2_HORAS ->
                    "Tu cita es en aproximadamente 2 horas"

                else ->
                    "Recordatorio de cita"
            }


        val mensaje =
            buildString {

                append(
                    "Tienes una cita con $especialista"
                )


                if (
                    fecha.isNotBlank()
                ) {

                    append(
                        " el $fecha"
                    )
                }


                if (
                    hora.isNotBlank()
                ) {

                    append(
                        " a las $hora"
                    )
                }


                append(".")
            }


        // ====================================================================
        // AL TOCAR LA NOTIFICACIÓN
        // ====================================================================

        val abrirAppIntent =
            Intent(
                context,
                MainActivity::class.java
            ).apply {

                flags =
                    Intent.FLAG_ACTIVITY_NEW_TASK or
                            Intent.FLAG_ACTIVITY_CLEAR_TOP
            }


        val abrirAppPendingIntent =
            PendingIntent.getActivity(

                context,

                citaId.hashCode(),

                abrirAppIntent,

                PendingIntent.FLAG_UPDATE_CURRENT or
                        PendingIntent.FLAG_IMMUTABLE
            )


        // ====================================================================
        // CONSTRUIR NOTIFICACIÓN
        // ====================================================================

        val notification =
            NotificationCompat
                .Builder(
                    context,
                    NotificacionCita.CANAL_ID
                )
                .setSmallIcon(
                    R.drawable.logo
                )
                .setContentTitle(
                    titulo
                )
                .setContentText(
                    mensaje
                )
                .setStyle(
                    NotificationCompat
                        .BigTextStyle()
                        .bigText(
                            mensaje
                        )
                )
                .setPriority(
                    NotificationCompat.PRIORITY_HIGH
                )
                .setAutoCancel(
                    true
                )
                .setContentIntent(
                    abrirAppPendingIntent
                )
                .build()


        // ====================================================================
        // PERMISO DE NOTIFICACIONES
        // ====================================================================

        if (
            android.os.Build.VERSION.SDK_INT >=
            android.os.Build.VERSION_CODES.TIRAMISU
        ) {

            val permiso =
                ContextCompat.checkSelfPermission(

                    context,

                    Manifest.permission.POST_NOTIFICATIONS
                )


            if (
                permiso !=
                PackageManager.PERMISSION_GRANTED
            ) {

                return
            }
        }


        // ====================================================================
        // MOSTRAR
        // ====================================================================

        val notificationId =
            (
                citaId +
                        tipoRecordatorio
                )
                .hashCode()


        NotificationManagerCompat
            .from(
                context
            )
            .notify(
                notificationId,
                notification
            )
    }


    companion object {

        const val EXTRA_CITA_ID =
            "extra_cita_id"

        const val EXTRA_ESPECIALISTA =
            "extra_especialista"

        const val EXTRA_FECHA =
            "extra_fecha"

        const val EXTRA_HORA =
            "extra_hora"

        const val EXTRA_TIPO_RECORDATORIO =
            "extra_tipo_recordatorio"


        const val TIPO_24_HORAS =
            "24_horas"

        const val TIPO_2_HORAS =
            "2_horas"
    }
}
