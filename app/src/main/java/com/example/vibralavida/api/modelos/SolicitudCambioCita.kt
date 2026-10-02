package com.example.vibralavida.api.modelos


// ============================================================================
// SOLICITUD DE CAMBIO DE CITA
// ============================================================================

data class SolicitudCambioCita(

    val tipo: String = "",

    val motivo: String = "",

    val fechaSolicitada: String = "",

    val horaSolicitada: String = "",

    val estado: String = "pendiente"
)
