package com.example.vibralavida.api.modelos


// ============================================================================
// PROGRESO REMOTO DE DIABETES
// ============================================================================

data class DiabetesProgresoRequest(

    val xp:
        Int,

    val nivel:
        String,

    val temasCompletados:
        List<String>,

    val intentos:
        Int,

    val mejorPuntaje:
        Int,

    val temasAReforzar:
        List<String>
)


data class DiabetesProgresoRemoto(

    val xp:
        Int = 0,

    val nivel:
        String = "Aprendiz de salud",

    val temasCompletados:
        List<String> = emptyList(),

    val intentos:
        Int = 0,

    val mejorPuntaje:
        Int = 0,

    val temasAReforzar:
        List<String> = emptyList()
)


data class DiabetesProgresoResponse(

    val success:
        Boolean = false,

    val existe:
        Boolean = false,

    val progreso:
        DiabetesProgresoRemoto? = null,

    val message:
        String? = null
)


// ============================================================================
// INTENTO DE EVALUACIÓN
// ============================================================================

data class DiabetesIntentoRequest(

    val puntaje:
        Int,

    val total:
        Int,

    val dificultad:
        String,

    val xpGanado:
        Int,

    val temasFallados:
        List<String>
)


data class DiabetesIntentoResponse(

    val success:
        Boolean = false,

    val id:
        String? = null,

    val message:
        String? = null
)
