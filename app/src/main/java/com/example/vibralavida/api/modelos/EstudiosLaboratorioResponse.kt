package com.example.vibralavida.api.modelos

import com.example.vibralavida.agenda.bitacora.EstudioLaboratorio


// ============================================================================
// RESPUESTA DE LISTA DE LABORATORIOS
// ============================================================================

data class EstudiosLaboratorioResponse(

    val success: Boolean = false,

    val laboratorios: List<EstudioLaboratorio> = emptyList(),

    val message: String? = null,

    val error: String? = null
)
