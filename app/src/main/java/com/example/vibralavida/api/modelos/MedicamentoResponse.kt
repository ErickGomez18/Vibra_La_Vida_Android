package com.example.vibralavida.api.modelos

import com.example.vibralavida.agenda.medicamentos.Medicamento


// ============================================================================
// RESPUESTA DE CREAR MEDICAMENTO
// ============================================================================
//
// POST /api/medicamentos
//
// ============================================================================

data class MedicamentoResponse(

    val success: Boolean = false,

    val message: String? = null,

    val medicamento: Medicamento? = null,

    val error: String? = null
)


// ============================================================================
// RESPUESTA DE OPERACIONES
// ============================================================================
//
// PUT /api/medicamentos/:id
// DELETE /api/medicamentos/:id
//
// ============================================================================

data class MedicamentoOperacionResponse(

    val success: Boolean = false,

    val message: String? = null,

    val error: String? = null
)
