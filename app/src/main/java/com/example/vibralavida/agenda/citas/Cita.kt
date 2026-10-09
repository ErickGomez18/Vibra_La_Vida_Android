package com.example.vibralavida.agenda.citas

import com.example.vibralavida.api.modelos.SolicitudCambioCita


// ============================================================================
// MODELO DE CITA
// ============================================================================
//
// Representa una cita médica que llega desde la API.
//
// La cita es creada principalmente por el profesional de salud desde
// el panel web.
//
// La app móvil del paciente:
//
// - consulta la cita,
// - confirma su asistencia,
// - solicita reagendación,
// - solicita cancelación.
//
// IMPORTANTE:
// Reagendar y cancelar son solicitudes.
// El paciente no modifica directamente la cita.
//
// ============================================================================

data class Cita(

    val id: String = "",

    val pacienteUid: String = "",

    val especialistaUid: String = "",

    val nombreEspecialista: String = "",

    val especialidad: String = "",

    val fecha: String = "",

    val hora: String = "",

    val zonaHoraria: String = "America/Cancun",

    val motivo: String = "",

    val lugar: String = "",

    val modalidad: String = "",

    val notas: String = "",


    // ========================================================================
    // ESTADO GENERAL DE LA CITA
    // ========================================================================
    //
    // Ejemplos:
    //
    // pendiente
    // confirmada
    // completada
    // cancelada
    //
    // Este estado es administrado principalmente por el profesional.
    //
    // ========================================================================

    val estado: String = "pendiente",


    // ========================================================================
    // RESPUESTA DEL PACIENTE
    // ========================================================================
    //
    // Ejemplos:
    //
    // pendiente
    // confirmada
    // solicitud_reagendar
    // solicitud_cancelar
    //
    // ========================================================================

    val estadoPaciente: String = "pendiente",


    // ========================================================================
    // SOLICITUD DE CAMBIO
    // ========================================================================
    //
    // Aquí se guardan los datos cuando el paciente solicita:
    //
    // - reagendar
    // - cancelar
    //
    // Si no existe ninguna solicitud, permanece en null.
    //
    // ========================================================================

    val solicitudCambio: SolicitudCambioCita? = null,


    // ========================================================================
    // NUEVO FLUJO UNIFICADO WEB / ANDROID
    // ========================================================================
    //
    // Estos campos llegan desde los endpoints actuales de Express.
    //
    // ========================================================================

    val motivoReagenda: String = "",

    val motivoCancelacion: String = "",

    val fechaSolicitadaPaciente: String = "",

    val horaSolicitadaPaciente: String = "",


    // ========================================================================
    // RECORDATORIO
    // ========================================================================

    val recordatorioActivo: Boolean = true
)