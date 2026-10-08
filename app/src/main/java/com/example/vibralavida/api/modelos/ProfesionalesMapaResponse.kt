package com.example.vibralavida.api.modelos

// ============================================================================
// RESPUESTA DEL MAPA DE PROFESIONALES
// ============================================================================
//
// Representa:
//
// GET /api/profesionales/mapa
//
// ============================================================================

data class ProfesionalesMapaResponse(

    val success: Boolean = false,

    val total: Int = 0,

    val profesionales: List<ProfesionalMapaResponse> = emptyList()
)


// ============================================================================
// PROFESIONAL
// ============================================================================

data class ProfesionalMapaResponse(

    val id: String = "",

    val nombre: String = "",

    val profesion: String = "",

    val especialidad: String = "",

    val cedulaProfesional: String = "",

    val cedulaVerificada: Boolean = false,

    val consultorio: ConsultorioMapaResponse =
        ConsultorioMapaResponse()
)


// ============================================================================
// CONSULTORIO
// ============================================================================

data class ConsultorioMapaResponse(

    val nombre: String = "",

    val direccion: String? = null,

    val telefono: String? = null,

    val horario: String? = null,

    val latitud: Double = 0.0,

    val longitud: Double = 0.0
)
