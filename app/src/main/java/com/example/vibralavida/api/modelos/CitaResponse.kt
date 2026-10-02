package com.example.vibralavida.api.modelos

import com.example.vibralavida.agenda.citas.Cita


// ============================================================================
// RESPUESTA DE UNA CITA
// ============================================================================
//
// Puede utilizarse para respuestas de operaciones como creación o edición.
//
// ============================================================================

data class CitaResponse(

    val success: Boolean = false,

    val cita: Cita? = null,

    val message: String? = null,

    val error: String? = null
)
