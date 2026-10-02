package com.example.vibralavida.ia

// ============================================================================
// FOTO DEL CONTEXTO DE HEALTH CONNECT
// ============================================================================
//
// Este objeto contiene únicamente datos de contexto.
//
// No representa:
// - diagnóstico;
// - interpretación clínica;
// - alerta médica automática.
//
// ============================================================================

data class HealthContextSnapshot(

    val healthConnectAvailable: Boolean = false,

    val permissionsGranted: Boolean = false,

    val stepsToday: Long? = null,

    val latestHeartRateBpm: Long? = null,

    val sleepTotalMinutes: Long? = null,

    val lightSleepMinutes: Long? = null,

    val deepSleepMinutes: Long? = null,

    val remSleepMinutes: Long? = null
)
