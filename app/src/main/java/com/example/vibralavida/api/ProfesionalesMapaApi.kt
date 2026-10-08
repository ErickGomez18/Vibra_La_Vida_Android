package com.example.vibralavida.api

// ============================================================================
// MODELOS
// ============================================================================

import com.example.vibralavida.api.modelos.ProfesionalesMapaResponse

// ============================================================================
// RETROFIT
// ============================================================================

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header


// ============================================================================
// PROFESIONALES MAPA API
// ============================================================================

interface ProfesionalesMapaApi {


    // ========================================================================
    // OBTENER PROFESIONALES DEL MAPA
    // ========================================================================
    //
    // GET /api/profesionales/mapa
    //
    // Requiere:
    //
    // Authorization: Bearer TOKEN_FIREBASE
    //
    // ========================================================================

    @GET("api/profesionales/mapa")
    fun obtenerProfesionales(

        @Header("Authorization")
        authorization: String

    ): Call<ProfesionalesMapaResponse>
}
