package com.example.vibralavida.agenda.medicamentos


// ============================================================================
// REGISTRO DE ADHERENCIA
// ============================================================================
//
// Representa lo que el paciente REPORTÓ acerca de una dosis programada.
//
// No debe interpretarse como confirmación clínica de ingestión.
//
// ============================================================================

data class RegistroAdherenciaMedicamento(

    val id: String = "",

    val medicamentoId: String = "",

    val nombreMedicamento: String = "",

    val dosis: String = "",

    val fecha: String = "",

    val horarioProgramado: String = "",

    val fechaHoraProgramadaMs: Long = 0L,

    val estado: String = "",

    val fuente: String = "autorreporte_paciente"
)
