package com.example.vibralavida.api

import com.example.vibralavida.api.modelos.HabitosIntentoRequest
import com.example.vibralavida.api.modelos.HabitosIntentoResponse
import com.example.vibralavida.api.modelos.HabitosProgresoRequest
import com.example.vibralavida.api.modelos.HabitosProgresoResponse

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT


// ============================================================================
// API DEL MÓDULO DE HÁBITOS SALUDABLES
// ============================================================================

interface HabitosProgresoApi {


    @GET("api/habitos/progreso")
    fun obtenerProgreso(

        @Header("Authorization")
        authorization:
            String

    ): Call<HabitosProgresoResponse>


    @PUT("api/habitos/progreso")
    fun guardarProgreso(

        @Header("Authorization")
        authorization:
            String,

        @Body
        progreso:
            HabitosProgresoRequest

    ): Call<HabitosProgresoResponse>


    @POST("api/habitos/intentos")
    fun guardarIntento(

        @Header("Authorization")
        authorization:
            String,

        @Body
        intento:
            HabitosIntentoRequest

    ): Call<HabitosIntentoResponse>
}


// ============================================================================
// CLIENTE AISLADO
// ============================================================================

internal object HabitosApiClient {

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
        HabitosProgresoApi by lazy {

        retrofit.create(
            HabitosProgresoApi::class.java
        )
    }
}
