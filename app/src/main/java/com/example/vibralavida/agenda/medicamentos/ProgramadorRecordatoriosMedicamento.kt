package com.example.vibralavida.agenda.medicamentos

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


// ============================================================================
// PROGRAMADOR DE RECORDATORIOS DE MEDICAMENTOS
// ============================================================================
//
// Esta clase se encarga de:
//
// 1. Programar la primera alarma de cada horario.
// 2. Programar automáticamente la alarma del día siguiente.
// 3. Respetar fecha de inicio.
// 4. Respetar fecha de finalización.
// 5. Cancelar alarmas cuando se edita o elimina un medicamento.
//
// ============================================================================

object ProgramadorRecordatoriosMedicamento {


    // ========================================================================
    // TAG PARA LOGCAT
    // ========================================================================

    private const val TAG =
        "VibraAlarmas"


    // ========================================================================
    // FORMATOS
    // ========================================================================

    // Formato utilizado por el formulario:
    //
    // 01/09/2026

    private val formatoFecha =
        DateTimeFormatter.ofPattern(
            "dd/MM/yyyy"
        )


    // Horarios:
    //
    // 08:00
    // 14:30
    // 20:00

    private val formatoHora =
        DateTimeFormatter.ofPattern(
            "HH:mm"
        )


    // Formato interno utilizado para pasar
    // la fecha programada al BroadcastReceiver.

    private val formatoFechaInterna =
        DateTimeFormatter.ofPattern(
            "yyyy-MM-dd"
        )


    // ========================================================================
    // PROGRAMAR MEDICAMENTO COMPLETO
    // ========================================================================

    fun programarMedicamento(

        context: Context,

        medicamento: Medicamento

    ) {


        Log.d(
            TAG,
            "Intentando programar: ${medicamento.nombre}"
        )


        // ====================================================================
        // RECORDATORIOS DESACTIVADOS
        // ====================================================================

        if (
            !medicamento.recordatorioActivo
        ) {

            Log.d(
                TAG,
                "Recordatorios desactivados para ${medicamento.nombre}"
            )

            return
        }


        // ====================================================================
        // SIN HORARIOS
        // ====================================================================

        if (
            medicamento.horarios.isEmpty()
        ) {

            Log.d(
                TAG,
                "No hay horarios para ${medicamento.nombre}"
            )

            return
        }


        // ====================================================================
        // PROGRAMAR CADA HORARIO
        // ====================================================================

        medicamento.horarios
            .forEachIndexed {
                    indice,
                    horario ->


                programarPrimerHorario(

                    context =
                        context,

                    medicamento =
                        medicamento,

                    horario =
                        horario,

                    indiceHorario =
                        indice
                )
            }
    }


    // =========================================================================
    // PROGRAMAR PRIMERA ALARMA
    // =========================================================================

    private fun programarPrimerHorario(

        context: Context,

        medicamento: Medicamento,

        horario: String,

        indiceHorario: Int

    ) {


        try {


            // =================================================================
            // FECHA DE INICIO
            // =================================================================

            val fechaInicio =
                LocalDate.parse(

                    medicamento.fechaInicio,

                    formatoFecha
                )


            // =================================================================
            // HORA
            // =================================================================

            val hora =
                LocalTime.parse(

                    horario,

                    formatoHora
                )


            // =================================================================
            // MOMENTO ACTUAL
            // =================================================================

            val ahora =
                LocalDateTime.now()


            // =================================================================
            // CALCULAR PRIMER DÍA
            // =================================================================

            var fechaObjetivo =
                LocalDate.now()


            // Si el tratamiento comienza después de hoy,
            // usamos la fecha de inicio.

            if (
                fechaObjetivo.isBefore(
                    fechaInicio
                )
            ) {

                fechaObjetivo =
                    fechaInicio
            }


            // Creamos fecha + hora.

            var fechaHoraObjetivo =
                LocalDateTime.of(

                    fechaObjetivo,

                    hora
                )


            // =================================================================
            // SI LA HORA DE HOY YA PASÓ
            // =================================================================

            if (
                !fechaHoraObjetivo.isAfter(
                    ahora
                )
            ) {

                fechaObjetivo =
                    fechaObjetivo.plusDays(
                        1
                    )


                fechaHoraObjetivo =
                    LocalDateTime.of(

                        fechaObjetivo,

                        hora
                    )
            }


            // =================================================================
            // VALIDAR FECHA FINAL
            // =================================================================

            if (
                !fechaPermitida(

                    fecha =
                        fechaObjetivo,

                    fechaFinTexto =
                        medicamento.fechaFin
                )
            ) {

                Log.d(
                    TAG,
                    "No se programa ${medicamento.nombre}: tratamiento finalizado"
                )

                return
            }


            // =================================================================
            // PROGRAMAR ALARMA
            // =================================================================

            programarAlarma(

                context =
                    context,

                medicamentoId =
                    medicamento.id,

                nombreMedicamento =
                    medicamento.nombre,

                dosis =
                    medicamento.dosis,

                indicaciones =
                    medicamento.indicaciones,

                horario =
                    horario,

                fechaFin =
                    medicamento.fechaFin,

                indiceHorario =
                    indiceHorario,

                fechaObjetivo =
                    fechaObjetivo
            )


        } catch (
            e: Exception
        ) {


            Log.e(
                TAG,
                "ERROR programando la primera alarma",
                e
            )
        }
    }


    // =========================================================================
    // PROGRAMAR EL DÍA SIGUIENTE
    // =========================================================================
    //
    // Esta función será llamada por el BroadcastReceiver
    // después de mostrar una notificación.
    //
    // Ejemplo:
    //
    // acaba de sonar:
    // 01/09/2026 08:00
    //
    // programa:
    // 02/09/2026 08:00
    //
    // =========================================================================

    fun programarSiguienteDia(

        context: Context,

        medicamentoId: String,

        nombreMedicamento: String,

        dosis: String,

        indicaciones: String,

        horario: String,

        fechaFin: String,

        indiceHorario: Int,

        fechaAnterior: String

    ) {


        try {


            // =================================================================
            // RECUPERAR LA FECHA QUE ACABA DE SONAR
            // =================================================================

            val fechaUltimaAlarma =
                LocalDate.parse(

                    fechaAnterior,

                    formatoFechaInterna
                )


            // =================================================================
            // DÍA SIGUIENTE
            // =================================================================

            val siguienteFecha =
                fechaUltimaAlarma.plusDays(
                    1
                )


            // =================================================================
            // COMPROBAR FECHA FINAL
            // =================================================================

            if (
                !fechaPermitida(

                    fecha =
                        siguienteFecha,

                    fechaFinTexto =
                        fechaFin
                )
            ) {

                Log.d(

                    TAG,

                    "Tratamiento terminado. " +
                            "No se programa otra alarma para $nombreMedicamento"
                )

                return
            }


            // =================================================================
            // PROGRAMAR
            // =================================================================

            programarAlarma(

                context =
                    context,

                medicamentoId =
                    medicamentoId,

                nombreMedicamento =
                    nombreMedicamento,

                dosis =
                    dosis,

                indicaciones =
                    indicaciones,

                horario =
                    horario,

                fechaFin =
                    fechaFin,

                indiceHorario =
                    indiceHorario,

                fechaObjetivo =
                    siguienteFecha
            )


            Log.d(

                TAG,

                "Siguiente alarma programada para " +
                        "$nombreMedicamento: $siguienteFecha $horario"
            )


        } catch (
            e: Exception
        ) {


            Log.e(
                TAG,
                "ERROR programando el siguiente día",
                e
            )
        }
    }


    // =========================================================================
    // PROGRAMAR UNA ALARMA
    // =========================================================================

    private fun programarAlarma(

        context: Context,

        medicamentoId: String,

        nombreMedicamento: String,

        dosis: String,

        indicaciones: String,

        horario: String,

        fechaFin: String,

        indiceHorario: Int,

        fechaObjetivo: LocalDate

    ) {


        try {


            // =================================================================
            // HORA
            // =================================================================

            val hora =
                LocalTime.parse(

                    horario,

                    formatoHora
                )


            // =================================================================
            // FECHA + HORA
            // =================================================================

            val fechaHora =
                LocalDateTime.of(

                    fechaObjetivo,

                    hora
                )


            // =================================================================
            // MILISEGUNDOS
            // =================================================================

            val tiempoMillis =
                fechaHora
                    .atZone(
                        ZoneId.systemDefault()
                    )
                    .toInstant()
                    .toEpochMilli()


            // =================================================================
            // ID ÚNICO
            // =================================================================

            val notificationId =
                crearIdUnico(

                    medicamentoId =
                        medicamentoId,

                    indiceHorario =
                        indiceHorario
                )


            // =================================================================
            // INTENT
            // =================================================================

            val intent =
                Intent(

                    context,

                    RecordatorioMedicamentoReceiver::class.java

                ).apply {


                    // ---------------------------------------------------------
                    // DATOS DE LA NOTIFICACIÓN
                    // ---------------------------------------------------------

                    putExtra(
                        "medicamentoId",
                        medicamentoId
                    )


                    putExtra(
                        "nombreMedicamento",
                        nombreMedicamento
                    )


                    putExtra(
                        "dosis",
                        dosis
                    )


                    putExtra(
                        "indicaciones",
                        indicaciones
                    )


                    // ---------------------------------------------------------
                    // DATOS PARA REPETIR LA ALARMA
                    // ---------------------------------------------------------

                    putExtra(
                        "horario",
                        horario
                    )


                    putExtra(
                        "fechaFin",
                        fechaFin
                    )


                    putExtra(
                        "indiceHorario",
                        indiceHorario
                    )


                    putExtra(
                        "fechaProgramada",

                        fechaObjetivo.format(
                            formatoFechaInterna
                        )
                    )


                    putExtra(
                        "notificationId",
                        notificationId
                    )
                }


            // =================================================================
            // PENDING INTENT
            // =================================================================

            val pendingIntent =
                PendingIntent.getBroadcast(

                    context,

                    notificationId,

                    intent,

                    PendingIntent.FLAG_UPDATE_CURRENT or
                            PendingIntent.FLAG_IMMUTABLE
                )


            // =================================================================
            // ALARM MANAGER
            // =================================================================

            val alarmManager =
                context.getSystemService(
                    Context.ALARM_SERVICE
                ) as AlarmManager


            // =================================================================
            // ANDROID 12+
            // =================================================================

            if (
                Build.VERSION.SDK_INT >=
                Build.VERSION_CODES.S
            ) {


                // =============================================================
                // ALARMAS EXACTAS PERMITIDAS
                // =============================================================

                if (
                    alarmManager.canScheduleExactAlarms()
                ) {


                    alarmManager.setExactAndAllowWhileIdle(

                        AlarmManager.RTC_WAKEUP,

                        tiempoMillis,

                        pendingIntent
                    )


                    Log.d(

                        TAG,

                        "ALARMA EXACTA PROGRAMADA: " +
                                "$nombreMedicamento - " +
                                "$fechaObjetivo $horario"
                    )


                } else {


                    // =========================================================
                    // SIN PERMISO
                    // =========================================================

                    Log.e(

                        TAG,

                        "No se programó la alarma de $nombreMedicamento. " +
                                "Falta permiso de alarmas exactas."
                    )
                }


            } else {


                // =================================================================
                // ANDROID 11 O ANTERIOR
                // =================================================================

                alarmManager.setExactAndAllowWhileIdle(

                    AlarmManager.RTC_WAKEUP,

                    tiempoMillis,

                    pendingIntent
                )


                Log.d(

                    TAG,

                    "ALARMA PROGRAMADA: " +
                            "$nombreMedicamento - $fechaObjetivo $horario"
                )
            }


        } catch (
            e: Exception
        ) {


            Log.e(
                TAG,
                "ERROR creando alarma",
                e
            )
        }
    }


    // =========================================================================
    // COMPROBAR FECHA FINAL
    // =========================================================================

    private fun fechaPermitida(

        fecha: LocalDate,

        fechaFinTexto: String

    ): Boolean {


        // Si no existe fecha final,
        // el tratamiento es indefinido.

        if (
            fechaFinTexto.isBlank()
        ) {

            return true
        }


        return try {


            val fechaFin =
                LocalDate.parse(

                    fechaFinTexto,

                    formatoFecha
                )


            // Permitimos si:
            //
            // fecha <= fechaFin

            !fecha.isAfter(
                fechaFin
            )


        } catch (
            e: Exception
        ) {


            Log.e(
                TAG,
                "Error leyendo fecha final",
                e
            )


            false
        }
    }


    // =========================================================================
    // CANCELAR RECORDATORIOS
    // =========================================================================

    fun cancelarMedicamento(

        context: Context,

        medicamento: Medicamento

    ) {


        val alarmManager =
            context.getSystemService(
                Context.ALARM_SERVICE
            ) as AlarmManager


        medicamento.horarios
            .forEachIndexed {
                    indice,
                    _ ->


                val id =
                    crearIdUnico(

                        medicamentoId =
                            medicamento.id,

                        indiceHorario =
                            indice
                    )


                val intent =
                    Intent(

                        context,

                        RecordatorioMedicamentoReceiver::class.java
                    )


                val pendingIntent =
                    PendingIntent.getBroadcast(

                        context,

                        id,

                        intent,

                        PendingIntent.FLAG_NO_CREATE or
                                PendingIntent.FLAG_IMMUTABLE
                    )


                if (
                    pendingIntent != null
                ) {


                    alarmManager.cancel(
                        pendingIntent
                    )


                    pendingIntent.cancel()


                    Log.d(

                        TAG,

                        "ALARMA CANCELADA: ${medicamento.nombre}"
                    )
                }
            }
    }


    // =========================================================================
    // ID ÚNICO
    // =========================================================================

    private fun crearIdUnico(

        medicamentoId: String,

        indiceHorario: Int

    ): Int {


        return medicamentoId.hashCode() * 31 +
                indiceHorario
    }
}