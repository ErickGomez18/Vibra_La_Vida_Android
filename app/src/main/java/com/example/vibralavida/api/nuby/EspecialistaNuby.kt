package com.example.vibralavida.api.nuby

data class EspecialistaNuby(
    val id: String = "",
    val nombre: String = "",
    val profesion: String = "",
    val especialidad: String = "",
    val cedulaProfesional: String = "",
    val modalidad: List<String> = emptyList(),
    val telefono: String? = null,
    val ubicacion: String? = null,
    val horarioAtencion: String? = null
)
