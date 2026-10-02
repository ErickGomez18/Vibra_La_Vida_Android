package com.example.vibralavida.api.modelos

import com.example.vibralavida.agenda.bitacora.RegistroSalud


// ============================================================================
// RESPUESTA AL CREAR UN REGISTRO
// ============================================================================

data class RegistroSaludResponse(

    val success: Boolean = false,

    val message: String? = null,

    val registro: RegistroSalud? = null,

    val error: String? = null
)


// ============================================================================
// RESPUESTA DE ACTUALIZAR / ELIMINAR
// ============================================================================

data class RegistroSaludOperacionResponse(

    val success: Boolean = false,

    val message: String? = null,

    val error: String? = null
)
