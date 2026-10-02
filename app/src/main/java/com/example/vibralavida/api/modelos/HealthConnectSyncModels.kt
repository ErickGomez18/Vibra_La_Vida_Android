package com.example.vibralavida.api.modelos


// ============================================================================
// REQUEST DE SINCRONIZACIÓN DE HEALTH CONNECT
// ============================================================================
//
// Este modelo viaja desde Android hacia:
//
// POST /api/health-connect/sync
//
// ============================================================================

data class HealthConnectSyncRequest(

    val fecha: String,

    val pasos: Long? = null,

    val frecuenciaCardiaca: Long? = null,

    val frecuenciaCardiacaMinima: Long? = null,

    val frecuenciaCardiacaMaxima: Long? = null,

    val cantidadMedicionesFrecuenciaCardiaca: Int? = null,

    val suenoMinutos: Long? = null,

    val inicioSueno: String? = null,

    val finSueno: String? = null,

    val suenoLigeroMinutos: Long? = null,

    val suenoProfundoMinutos: Long? = null,

    val suenoRemMinutos: Long? = null,

    val despiertoMinutos: Long? = null,

    val fuente: String = "Mi Fitness / Health Connect",

    val fechaLectura: String? = null
)


// ============================================================================
// RESPONSE
// ============================================================================

data class HealthConnectSyncResponse(

    val success: Boolean = false,

    val message: String = "",

    val healthRecordId: String? = null,

    val fecha: String? = null
)
