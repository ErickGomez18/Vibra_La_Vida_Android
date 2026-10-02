package com.example.vibralavida.api.nuby

data class EspecialistasNubyResponse(
    val success: Boolean = false,
    val especialidadSolicitada: String? = null,
    val especialistas: List<EspecialistaNuby> = emptyList(),
    val message: String? = null
)
