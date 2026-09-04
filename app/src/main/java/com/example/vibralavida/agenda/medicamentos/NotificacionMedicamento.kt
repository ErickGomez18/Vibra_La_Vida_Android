package com.example.vibralavida.agenda.medicamentos
import com.example.vibralavida.R

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log

import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat


// ============================================================================
// NOTIFICACIÓN DE MEDICAMENTOS
// ============================================================================
//
// Esta clase se encarga de:
//
// 1. Crear el canal de notificaciones.
// 2. Mostrar la notificación cuando llega una alarma.
//
// ============================================================================

object NotificacionMedicamento {

    private const val TAG = "VibraAlarmas"

    const val CANAL_ID =
        "recordatorios_medicamentos"

    private const val CANAL_NOMBRE =
        "Recordatorios de medicamentos"

    private const val CANAL_DESCRIPCION =
        "Avisos para recordar la toma de medicamentos"


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
                        CANAL_DESCRIPCION

                    enableVibration(
                        true
                    )
                }


            val notificationManager =
                context.getSystemService(
                    Context.NOTIFICATION_SERVICE
                ) as NotificationManager


            notificationManager
                .createNotificationChannel(
                    canal
                )


            Log.d(
                TAG,
                "Canal de notificaciones creado"
            )
        }
    }


    // ========================================================================
    // MOSTRAR NOTIFICACIÓN
    // ========================================================================

    fun mostrarNotificacion(

        context: Context,

        notificationId: Int,

        nombreMedicamento: String,

        dosis: String,

        indicaciones: String

    ) {

        Log.d(
            TAG,
            "Intentando mostrar notificación: $nombreMedicamento"
        )


        // ====================================================================
        // COMPROBAR PERMISO ANDROID 13+
        // ====================================================================

        if (
            Build.VERSION.SDK_INT >=
            Build.VERSION_CODES.TIRAMISU
        ) {

            if (
                ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                Log.e(
                    TAG,
                    "No se puede mostrar notificación: falta permiso POST_NOTIFICATIONS"
                )

                return
            }
        }


        // ====================================================================
        // TEXTO
        // ====================================================================

        val textoPrincipal =
            if (
                dosis.isNotBlank()
            ) {

                "$nombreMedicamento · $dosis"

            } else {

                nombreMedicamento
            }


        val textoCompleto =
            if (
                indicaciones.isNotBlank()
            ) {

                "$textoPrincipal\n$indicaciones"

            } else {

                textoPrincipal
            }


        // ====================================================================
        // CONSTRUIR NOTIFICACIÓN
        // ====================================================================

        val notificacion =
            NotificationCompat.Builder(
                context,
                CANAL_ID
            )

                /*
                 * Usamos temporalmente un icono del sistema
                 * para evitar problemas con iconos adaptativos
                 * en las notificaciones.
                 *
                 * Después podemos hacer nuestro icono de
                 * notificación personalizado de Vibra la vida.
                 */
                .setSmallIcon(
                    android.R.drawable.ic_dialog_info
                )

                .setContentTitle(
                    "Es hora de tu medicamento"
                )

                .setContentText(
                    textoPrincipal
                )

                .setStyle(

                    NotificationCompat
                        .BigTextStyle()
                        .bigText(
                            textoCompleto
                        )
                )

                .setPriority(
                    NotificationCompat.PRIORITY_HIGH
                )

                .setCategory(
                    NotificationCompat.CATEGORY_REMINDER
                )

                .setAutoCancel(
                    true
                )

                .build()


        // ====================================================================
        // MOSTRAR
        // ====================================================================

        NotificationManagerCompat
            .from(context)
            .notify(
                notificationId,
                notificacion
            )


        Log.d(
            TAG,
            "NOTIFICACIÓN MOSTRADA: $nombreMedicamento"
        )
    }
}