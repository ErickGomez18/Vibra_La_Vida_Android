package com.example.vibralavida.ia

// ============================================================================
// TIPOS DE CONTEXTO QUE NUBY PUEDE UTILIZAR
// ============================================================================
//
// No mandamos todo el perfil a Gemma.
//
// Primero analizamos la pregunta y seleccionamos únicamente los datos
// necesarios.
//
// ============================================================================

enum class AiContextType {

    AGE,

    GENDER,

    WEIGHT,

    HEIGHT,

    ACTIVITY_LEVEL,

    BMI,

    CALORIES
}
