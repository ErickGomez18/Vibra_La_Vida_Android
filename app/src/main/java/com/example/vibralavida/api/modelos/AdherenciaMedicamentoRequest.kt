package com.example.vibralavida.api.modelos


// ============================================================================
// REQUEST DE ADHERENCIA
// ============================================================================

data class AdherenciaMedicamentoRequest(

    val medicamentoId: String,

    val nombreMedicamento: String,

    val dosis: String,

    val fecha: String,

    val horarioProgramado: String,

    val fechaHoraProgramadaMs: Long,

    val estado: String
)
