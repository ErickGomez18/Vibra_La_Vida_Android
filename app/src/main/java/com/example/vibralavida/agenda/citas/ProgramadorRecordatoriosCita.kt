package com.example.vibralavida.agenda.citas

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


// ============================================================================
// PROGRAMADOR DE RECORDATORIOS DE CITAS
// ============================================================================
//
// Programa dos recordatorios locales:
//
// 1. 24 horas antes.
// 2. 2 horas antes.
//
// Si la cita está:
// - cancelada
// - completada
// - recordatorioActivo = false
//
// se cancelan los recordatorios.
//
// ============================================================================

object ProgramadorRecordatoriosCita {


    // ========================================================================
    // MILISEGUNDOS
    // ========================================================================

    private const val HORAS_24_MS =
        24L * 60L * 60L * 1000L


    private const val HORAS_2_MS =
        2L * 60L * 60L * 1000L


    // ========================================================================
    // PROGRAMAR CITA
    // ========================================================================

    fun programarCita(
        context: Context,
        cita: Cita
    ) {

        // --------------------------------------------------------------------
        // CANCELAMOS SIEMPRE LO ANTERIOR
        // --------------------------------------------------------------------
        //
        // Esto evita duplicados cuando una cita cambia de fecha u hora.
        //
        // --------------------------------------------------------------------

        cancelarCita(
            context =
                context,

            cita =
                cita
        )


        // --------------------------------------------------------------------
        // ¿DEBE TENER RECORDATORIOS?
        // --------------------------------------------------------------------

        if (
            !cita.recordatorioActivo
        ) {

            return
        }


        if (
            cita.estado.equals(
                "cancelada",
                ignoreCase = true
            ) ||
            cita.estado.equals(
                "completada",
                ignoreCase = true
            )
        ) {

            return
        }


        // --------------------------------------------------------------------
        // FECHA/HORA REAL DE LA CITA
        // --------------------------------------------------------------------

        val citaMillis =
            obtenerFechaHoraMillis(
                cita
            )


        if (
            citaMillis == null
        ) {

            return
        }


        val ahora =
            System.currentTimeMillis()


        // --------------------------------------------------------------------
        // 24 HORAS ANTES
        // --------------------------------------------------------------------

        val recordatorio24 =
            citaMillis -
                    HORAS_24_MS


        if (
            recordatorio24 >
            ahora
        ) {

            programarAlarma(

                context =
                    context,

                cita =
                    cita,

                momentoMillis =
                    recordatorio24,

                tipo =
                    CitaReminderReceiver
                        .TIPO_24_HORAS,

                requestCode =
                    crearRequestCode(
                        cita.id,
                        "24"
                    )
            )
        }


        // --------------------------------------------------------------------
        // 2 HORAS ANTES
        // --------------------------------------------------------------------

        val recordatorio2 =
            citaMillis -
                    HORAS_2_MS


        if (
            recordatorio2 >
            ahora
        ) {

            programarAlarma(

                context =
                    context,

                cita =
                    cita,

                momentoMillis =
                    recordatorio2,

                tipo =
                    CitaReminderReceiver
                        .TIPO_2_HORAS,

                requestCode =
                    crearRequestCode(
                        cita.id,
                        "2"
                    )
            )
        }
    }


    // ========================================================================
    // PROGRAMAR TODAS
    // ========================================================================

    fun programarCitas(
        context: Context,
        citas: List<Cita>
    ) {

        citas.forEach {
                cita ->

            programarCita(

                context =
                    context,

                cita =
                    cita
            )
        }
    }


    // ========================================================================
    // CANCELAR CITA
    // ========================================================================

    fun cancelarCita(
        context: Context,
        cita: Cita
    ) {

        cancelarAlarma(

            context =
                context,

            cita =
                cita,

            tipo =
                CitaReminderReceiver
                    .TIPO_24_HORAS,

            requestCode =
                crearRequestCode(
                    cita.id,
                    "24"
                )
        )


        cancelarAlarma(

            context =
                context,

            cita =
                cita,

            tipo =
                CitaReminderReceiver
                    .TIPO_2_HORAS,

            requestCode =
                crearRequestCode(
                    cita.id,
                    "2"
                )
        )
    }


    // ========================================================================
    // PROGRAMAR ALARMA
    // ========================================================================

    private fun programarAlarma(
        context: Context,
        cita: Cita,
        momentoMillis: Long,
        tipo: String,
        requestCode: Int
    ) {

        val alarmManager =
            context.getSystemService(
                Context.ALARM_SERVICE
            ) as AlarmManager


        val pendingIntent =
            crearPendingIntent(

                context =
                    context,

                cita =
                    cita,

                tipo =
                    tipo,

                requestCode =
                    requestCode
            )


        // --------------------------------------------------------------------
        // ANDROID 12+
        // --------------------------------------------------------------------

        if (
            Build.VERSION.SDK_INT >=
            Build.VERSION_CODES.S
        ) {

            if (
                alarmManager
                    .canScheduleExactAlarms()
            ) {

                alarmManager
                    .setExactAndAllowWhileIdle(

                        AlarmManager.RTC_WAKEUP,

                        momentoMillis,

                        pendingIntent
                    )

            } else {

                // Si el usuario todavía no autorizó alarmas exactas,
                // dejamos un recordatorio no exacto como respaldo.

                alarmManager
                    .setAndAllowWhileIdle(

                        AlarmManager.RTC_WAKEUP,

                        momentoMillis,

                        pendingIntent
                    )
            }

            return
        }


        // --------------------------------------------------------------------
        // ANDROID 6 A 11
        // --------------------------------------------------------------------

        if (
            Build.VERSION.SDK_INT >=
            Build.VERSION_CODES.M
        ) {

            alarmManager
                .setExactAndAllowWhileIdle(

                    AlarmManager.RTC_WAKEUP,

                    momentoMillis,

                    pendingIntent
                )

            return
        }


        // --------------------------------------------------------------------
        // VERSIONES ANTERIORES
        // --------------------------------------------------------------------

        alarmManager
            .setExact(

                AlarmManager.RTC_WAKEUP,

                momentoMillis,

                pendingIntent
            )
    }


    // ========================================================================
    // CANCELAR ALARMA
    // ========================================================================

    private fun cancelarAlarma(
        context: Context,
        cita: Cita,
        tipo: String,
        requestCode: Int
    ) {

        val alarmManager =
            context.getSystemService(
                Context.ALARM_SERVICE
            ) as AlarmManager


        val pendingIntent =
            crearPendingIntent(

                context =
                    context,

                cita =
                    cita,

                tipo =
                    tipo,

                requestCode =
                    requestCode
            )


        alarmManager.cancel(
            pendingIntent
        )


        pendingIntent.cancel()
    }


    // ========================================================================
    // PENDING INTENT
    // ========================================================================

    private fun crearPendingIntent(
        context: Context,
        cita: Cita,
        tipo: String,
        requestCode: Int
    ): PendingIntent {

        val intent =
            Intent(
                context,
                CitaReminderReceiver::class.java
            ).apply {

                putExtra(
                    CitaReminderReceiver.EXTRA_CITA_ID,
                    cita.id
                )


                putExtra(
                    CitaReminderReceiver.EXTRA_ESPECIALISTA,
                    cita.nombreEspecialista
                )


                putExtra(
                    CitaReminderReceiver.EXTRA_FECHA,
                    cita.fecha
                )


                putExtra(
                    CitaReminderReceiver.EXTRA_HORA,
                    cita.hora
                )


                putExtra(
                    CitaReminderReceiver.EXTRA_TIPO_RECORDATORIO,
                    tipo
                )
            }


        return PendingIntent.getBroadcast(

            context,

            requestCode,

            intent,

            PendingIntent.FLAG_UPDATE_CURRENT or
                    PendingIntent.FLAG_IMMUTABLE
        )
    }


    // ========================================================================
    // REQUEST CODE ESTABLE
    // ========================================================================

    private fun crearRequestCode(
        citaId: String,
        sufijo: String
    ): Int {

        return (
            "$citaId-$sufijo"
            )
            .hashCode()
    }


    // ========================================================================
    // FECHA + HORA DE LA CITA
    // ========================================================================
    //
    // Acepta:
    //
    // dd/MM/yyyy
    // yyyy-MM-dd
    //
    // y utiliza la zona horaria que viene en la cita.
    //
    // ========================================================================

    private fun obtenerFechaHoraMillis(
        cita: Cita
    ): Long? {

        return try {

            val formatosFecha =
                listOf(

                    DateTimeFormatter.ofPattern(
                        "dd/MM/yyyy"
                    ),

                    DateTimeFormatter.ofPattern(
                        "yyyy-MM-dd"
                    )
                )


            var fechaLocal:
                LocalDate? =
                null


            for (
                formato in formatosFecha
            ) {

                try {

                    fechaLocal =
                        LocalDate.parse(

                            cita.fecha,

                            formato
                        )


                    break

                } catch (
                    ignored: Exception
                ) {

                    // Probamos el siguiente formato.
                }
            }


            if (
                fechaLocal == null
            ) {

                return null
            }


            val horaLocal =
                LocalTime.parse(

                    cita.hora,

                    DateTimeFormatter.ofPattern(
                        "HH:mm"
                    )
                )


            val zona =
                try {

                    ZoneId.of(
                        cita.zonaHoraria
                    )

                } catch (
                    e: Exception
                ) {

                    ZoneId.systemDefault()
                }


            LocalDateTime
                .of(
                    fechaLocal,
                    horaLocal
                )
                .atZone(
                    zona
                )
                .toInstant()
                .toEpochMilli()

        } catch (
            e: Exception
        ) {

            null
        }
    }
}
