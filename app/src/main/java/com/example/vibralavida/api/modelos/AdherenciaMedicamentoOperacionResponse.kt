package com.example.vibralavida.api.modelos

import com.example.vibralavida.agenda.medicamentos.RegistroAdherenciaMedicamento


// ============================================================================
// RESPUESTA AL REGISTRAR ADHERENCIA
// ============================================================================

data class AdherenciaMedicamentoOperacionResponse(

    val success: Boolean = false,

    val registro: RegistroAdherenciaMedicamento? = null,

    val message: String? = null,

    val error: String? = null
)
