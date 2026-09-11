package com.example.vibralavida.api.modelos

import com.example.vibralavida.agenda.medicamentos.Medicamento


// ============================================================================
// RESPUESTA DE LISTA DE MEDICAMENTOS
// ============================================================================
//
// GET /api/medicamentos
//
// ============================================================================

data class MedicamentosResponse(

    val success: Boolean = false,

    val medicamentos: List<Medicamento> = emptyList(),

    val message: String? = null,

    val error: String? = null
)
