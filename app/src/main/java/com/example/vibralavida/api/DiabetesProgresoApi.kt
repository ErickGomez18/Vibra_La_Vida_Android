package com.example.vibralavida.api

import com.example.vibralavida.api.modelos.DiabetesIntentoRequest
import com.example.vibralavida.api.modelos.DiabetesIntentoResponse
import com.example.vibralavida.api.modelos.DiabetesProgresoRequest
import com.example.vibralavida.api.modelos.DiabetesProgresoResponse

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT


// ============================================================================
// API DEL MÓDULO DE DIABETES
// ============================================================================

interface DiabetesProgresoApi {


    @GET("api/diabetes/progreso")
    fun obtenerProgreso(

        @Header("Authorization")
        authorization:
            String

    ): Call<DiabetesProgresoResponse>


    @PUT("api/diabetes/progreso")
    fun guardarProgreso(

        @Header("Authorization")
        authorization:
            String,

        @Body
        progreso:
            DiabetesProgresoRequest

    ): Call<DiabetesProgresoResponse>


    @POST("api/diabetes/intentos")
    fun guardarIntento(

        @Header("Authorization")
        authorization:
            String,

        @Body
        intento:
            DiabetesIntentoRequest

    ): Call<DiabetesIntentoResponse>
}


// ============================================================================
// CLIENTE AISLADO
// ============================================================================
//
// Lo dejamos independiente de ApiClient para no arriesgar las APIs que ya
// funcionan en citas, medicamentos, seguimiento, etc.
// ============================================================================

internal object DiabetesApiClient {

    private const val BASE_URL =
        "https://api-vibra-la-vida.onrender.com/"


    private val retrofit:
        Retrofit by lazy {

        Retrofit
            .Builder()
            .baseUrl(
                BASE_URL
            )
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()
    }


    val api:
        DiabetesProgresoApi by lazy {

        retrofit.create(
            DiabetesProgresoApi::class.java
        )
    }
}
