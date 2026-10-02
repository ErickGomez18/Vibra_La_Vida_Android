package com.example.vibralavida.api.modelos

import com.example.vibralavida.agenda.citas.Cita


// ============================================================================
// RESPUESTA DE LISTADO DE CITAS
// ============================================================================
//
// GET /api/citas
//
// ============================================================================

data class CitasResponse(

    val success: Boolean = false,

    val citas: List<Cita> = emptyList(),

    val message: String? = null,

    val error: String? = null
)
