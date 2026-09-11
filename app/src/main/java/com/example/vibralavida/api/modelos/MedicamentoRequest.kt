package com.example.vibralavida.api.modelos


// ============================================================================
// MEDICAMENTO REQUEST
// ============================================================================
//
// Representa los datos que Android envía a:
//
// POST /api/medicamentos
//
// y:
//
// PUT /api/medicamentos/:id
//
// El ID NO se envía en el body.
//
// Cuando creamos un medicamento, Firestore genera el ID.
// Cuando editamos, el ID se manda dentro de la URL.
//
// ============================================================================

data class MedicamentoRequest(

    val nombre: String,

    val dosis: String,

    val presentacion: String,

    val horarios: List<String>,

    val fechaInicio: String,

    val fechaFin: String,

    val indicaciones: String,

    val recordatorioActivo: Boolean,

    val fotoUri: String? = null
)