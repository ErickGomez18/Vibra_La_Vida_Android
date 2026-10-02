package com.example.vibralavida.ia

// ============================================================================
// CONTEXTO PERSONAL DEL USUARIO
// ============================================================================
//
// Contiene datos que YA existen en Vibra la vida.
//
// Los tres objetivos calóricos provienen directamente de la calculadora de
// calorías de la app.
//
// ============================================================================

data class AiUserContext(

    val age: Int? = null,

    val gender: String? = null,

    val weightKg: Double? = null,

    val heightCm: Double? = null,

    val activityLevel: String? = null,

    val bmi: Double? = null,

    val caloriesLose: Int? = null,

    val caloriesMaintain: Int? = null,

    val caloriesGain: Int? = null
)
