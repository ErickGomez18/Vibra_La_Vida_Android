package com.example.vibralavida.api.modelos

import com.example.vibralavida.agenda.bitacora.EstudioLaboratorio


// ============================================================================
// RESPUESTA AL CREAR UN ESTUDIO
// ============================================================================

data class EstudioLaboratorioResponse(

    val success: Boolean = false,

    val message: String? = null,

    val estudio: EstudioLaboratorio? = null,

    val error: String? = null
)


// ============================================================================
// RESPUESTA DE ACTUALIZAR / ELIMINAR
// ============================================================================

data class EstudioLaboratorioOperacionResponse(

    val success: Boolean = false,

    val message: String? = null,

    val error: String? = null
)
