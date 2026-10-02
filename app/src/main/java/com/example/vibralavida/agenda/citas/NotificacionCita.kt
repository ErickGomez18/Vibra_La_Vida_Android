package com.example.vibralavida.agenda.citas

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build


// ============================================================================
// NOTIFICACIÓN DE CITAS
// ============================================================================
//
// Crea el canal utilizado por los recordatorios locales de citas.
//
// ============================================================================

object NotificacionCita {


    // ========================================================================
    // DATOS DEL CANAL
    // ========================================================================

    const val CANAL_ID =
        "recordatorios_citas"


    const val CANAL_NOMBRE =
        "Recordatorios de citas"


    // ========================================================================
    // CREAR CANAL
    // ========================================================================

    fun crearCanal(
        context: Context
    ) {

        if (
            Build.VERSION.SDK_INT >=
            Build.VERSION_CODES.O
        ) {

            val canal =
                NotificationChannel(

                    CANAL_ID,

                    CANAL_NOMBRE,

                    NotificationManager.IMPORTANCE_HIGH
                ).apply {

                    description =
                        "Recordatorios de próximas citas médicas."
                }


            val notificationManager =
                context.getSystemService(
                    Context.NOTIFICATION_SERVICE
                ) as NotificationManager


            notificationManager
                .createNotificationChannel(
                    canal
                )
        }
    }
}
