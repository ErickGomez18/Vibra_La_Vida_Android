package com.example.vibralavida.api

import com.example.vibralavida.api.modelos.AdherenciaMedicamentoOperacionResponse
import com.example.vibralavida.api.modelos.AdherenciaMedicamentoRequest
import com.example.vibralavida.api.modelos.AdherenciaMedicamentosResponse

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST


// ============================================================================
// ADHERENCIA DE MEDICAMENTOS API
// ============================================================================

interface AdherenciaMedicamentosApi {


    @GET("api/adherencia-medicamentos")
    fun obtenerHistorial(

        @Header("Authorization")
        authorization: String

    ): Call<AdherenciaMedicamentosResponse>


    @POST("api/adherencia-medicamentos")
    fun registrarEstado(

        @Header("Authorization")
        authorization: String,

        @Body
        request: AdherenciaMedicamentoRequest

    ): Call<AdherenciaMedicamentoOperacionResponse>
}
