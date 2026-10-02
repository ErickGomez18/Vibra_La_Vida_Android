package com.example.vibralavida.ia

// ============================================================================
// RECOMENDACIÓN ALIMENTARIA LOCAL
// ============================================================================
//
// Esta estructura NO representa una dieta clínica.
//
// Guarda:
// - objetivo detectado;
// - referencia energética, si existe;
// - recomendaciones generales;
// - una respuesta de respaldo lista para mostrar.
//
// ============================================================================

data class NutritionRecommendation(

    val goal: AiNutritionGoal,

    val calorieReference: Int?,

    val suggestions: List<String>,

    val fallbackResponse: String
)
