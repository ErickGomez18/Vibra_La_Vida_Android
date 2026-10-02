package com.example.vibralavida.api.modelos

import com.example.vibralavida.agenda.bitacora.RegistroSalud


// ============================================================================
// RESPUESTA DE LISTA DE REGISTROS
// ============================================================================

data class RegistrosSaludResponse(

    val success: Boolean = false,

    val registros: List<RegistroSalud> = emptyList(),

    val message: String? = null,

    val error: String? = null
)
