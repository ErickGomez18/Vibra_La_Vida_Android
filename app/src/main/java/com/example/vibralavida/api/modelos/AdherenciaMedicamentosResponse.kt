package com.example.vibralavida.api.modelos

import com.example.vibralavida.agenda.medicamentos.RegistroAdherenciaMedicamento


// ============================================================================
// RESPUESTA DE HISTORIAL DE ADHERENCIA
// ============================================================================

data class AdherenciaMedicamentosResponse(

    val success: Boolean = false,

    val registros: List<RegistroAdherenciaMedicamento> = emptyList(),

    val message: String? = null,

    val error: String? = null
)
