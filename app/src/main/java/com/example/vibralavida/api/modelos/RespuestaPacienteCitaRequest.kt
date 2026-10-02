package com.example.vibralavida.api.modelos


// ============================================================================
// REQUEST - RESPUESTA DEL PACIENTE
// ============================================================================
//
// accion:
//
// "confirmar"
// "reagendar"
// "cancelar"
//
// Para confirmar:
// motivo, fechaSolicitada y horaSolicitada pueden quedar vacíos.
//
// Para reagendar:
// fechaSolicitada y horaSolicitada son obligatorios.
//
// Para cancelar:
// se recomienda enviar motivo.
//
// ============================================================================

data class RespuestaPacienteCitaRequest(

    val accion: String,

    val motivo: String = "",

    val fechaSolicitada: String = "",

    val horaSolicitada: String = ""
)
