package com.example.vibralavida.api


// ============================================================================
// MODELOS
// ============================================================================

import com.example.vibralavida.api.modelos.HealthConnectSyncRequest
import com.example.vibralavida.api.modelos.HealthConnectSyncResponse


// ============================================================================
// RETROFIT
// ============================================================================

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST


// ============================================================================
// HEALTH CONNECT API
// ============================================================================

interface HealthConnectApi {


    // ========================================================================
    // SINCRONIZAR RESUMEN DIARIO
    // ========================================================================
    //
    // POST /api/health-connect/sync
    //
    // ========================================================================

    @POST("api/health-connect/sync")
    fun sincronizar(

        @Header("Authorization")
        authorization: String,

        @Body
        datos: HealthConnectSyncRequest

    ): Call<HealthConnectSyncResponse>
}
