package com.example.vibralavida.agenda.medicamentos

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log


// ============================================================================
// RECEIVER DE RECORDATORIOS DE MEDICAMENTOS
// ============================================================================
//
// Este receiver tiene ahora DOS responsabilidades:
//
// 1. Mostrar la notificación del medicamento.
// 2. Programar automáticamente el mismo horario
//    para el día siguiente.
//
// ============================================================================

class RecordatorioMedicamentoReceiver :
    BroadcastReceiver() {


    override fun onReceive(

        context: Context,

        intent: Intent

    ) {


        // ====================================================================
        // LOG
        // ====================================================================

        Log.d(
            "VibraAlarmas",
            "RECEIVER: alarma recibida"
        )


        // ====================================================================
        // RECUPERAR ID DEL MEDICAMENTO
        // ====================================================================

        val medicamentoId =
            intent.getStringExtra(
                "medicamentoId"
            ) ?: ""


        // ====================================================================
        // NOMBRE
        // ====================================================================

        val nombre =
            intent.getStringExtra(
                "nombreMedicamento"
            ) ?: "Medicamento"


        // ====================================================================
        // DOSIS
        // ====================================================================

        val dosis =
            intent.getStringExtra(
                "dosis"
            ) ?: ""


        // ====================================================================
        // INDICACIONES
        // ====================================================================

        val indicaciones =
            intent.getStringExtra(
                "indicaciones"
            ) ?: ""


        // ====================================================================
        // HORARIO
        // ====================================================================

        val horario =
            intent.getStringExtra(
                "horario"
            ) ?: ""


        // ====================================================================
        // FECHA FINAL
        // ====================================================================

        val fechaFin =
            intent.getStringExtra(
                "fechaFin"
            ) ?: ""


        // ====================================================================
        // FECHA QUE ACABA DE SONAR
        // ====================================================================

        val fechaProgramada =
            intent.getStringExtra(
                "fechaProgramada"
            ) ?: ""


        // ====================================================================
        // ÍNDICE DEL HORARIO
        // ====================================================================

        val indiceHorario =
            intent.getIntExtra(
                "indiceHorario",
                0
            )


        // ====================================================================
        // ID DE NOTIFICACIÓN
        // ====================================================================

        val notificationId =
            intent.getIntExtra(
                "notificationId",
                1
            )


        Log.d(

            "VibraAlarmas",

            "RECEIVER: $nombre - $fechaProgramada $horario"
        )


        // ====================================================================
        // ASEGURAR CANAL DE NOTIFICACIONES
        // ====================================================================

        NotificacionMedicamento
            .crearCanal(
                context
            )


        // ====================================================================
        // MOSTRAR NOTIFICACIÓN
        // ====================================================================

        NotificacionMedicamento
            .mostrarNotificacion(

                context =
                    context,

                notificationId =
                    notificationId,

                nombreMedicamento =
                    nombre,

                dosis =
                    dosis,

                indicaciones =
                    indicaciones
            )


        // ====================================================================
        // PROGRAMAR EL DÍA SIGUIENTE
        // ====================================================================
        //
        // El receiver no necesita que MainActivity esté abierta.
        //
        // Toda la información que necesita está guardada
        // dentro del Intent de la alarma.
        //

        if (
            medicamentoId.isNotBlank() &&
            horario.isNotBlank() &&
            fechaProgramada.isNotBlank()
        ) {


            ProgramadorRecordatoriosMedicamento
                .programarSiguienteDia(

                    context =
                        context,

                    medicamentoId =
                        medicamentoId,

                    nombreMedicamento =
                        nombre,

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

                    fechaAnterior =
                        fechaProgramada
                )
        }


        Log.d(
            "VibraAlarmas",
            "RECEIVER: procesamiento terminado"
        )
    }
}