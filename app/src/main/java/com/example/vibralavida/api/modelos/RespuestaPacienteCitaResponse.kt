package com.example.vibralavida.api.modelos

import com.example.vibralavida.agenda.citas.Cita


// ============================================================================
// RESPONSE - RESPUESTA DEL PACIENTE
// ============================================================================

data class RespuestaPacienteCitaResponse(

    val success: Boolean = false,

    val message: String? = null,

    val cita: Cita? = null,

    val error: String? = null
)
