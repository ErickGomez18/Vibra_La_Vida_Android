package com.example.vibralavida.api

import com.example.vibralavida.api.modelos.ResultadoRequest
import com.example.vibralavida.api.modelos.ResultadoResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST


// ============================================================================
// RESULTS API
// ============================================================================
//
// Conecta Android con:
//
// POST /api/results
//
// El backend obtiene el UID directamente del token de Firebase y guarda en:
// usuarios/{uid}/resultados/{id}
//
// ============================================================================

interface ResultsApi {

    @POST("api/results")
    fun guardarResultado(

        @Header("Authorization")
        authorization: String,

        @Body
        resultado: ResultadoRequest

    ): Call<ResultadoResponse>
}
