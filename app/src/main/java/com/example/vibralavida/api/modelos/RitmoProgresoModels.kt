package com.example.vibralavida.api.modelos

data class RitmoProgresoRequest(
    val xp: Int,
    val nivel: String,
    val temasCompletados: List<String>,
    val intentos: Int,
    val mejorPuntaje: Int,
    val temasAReforzar: List<String>
)

data class RitmoProgresoRemoto(
    val xp: Int = 0,
    val nivel: String = "Primer latido",
    val temasCompletados: List<String> = emptyList(),
    val intentos: Int = 0,
    val mejorPuntaje: Int = 0,
    val temasAReforzar: List<String> = emptyList()
)

data class RitmoProgresoResponse(
    val success: Boolean = false,
    val existe: Boolean = false,
    val progreso: RitmoProgresoRemoto? = null,
    val message: String? = null
)

data class RitmoIntentoRequest(
    val puntaje: Int,
    val total: Int,
    val dificultad: String,
    val xpGanado: Int,
    val temasFallados: List<String>
)

data class RitmoIntentoResponse(
    val success: Boolean = false,
    val id: String? = null,
    val message: String? = null
)
