package com.example.vibralavida.api

import com.example.vibralavida.api.modelos.RitmoIntentoRequest
import com.example.vibralavida.api.modelos.RitmoIntentoResponse
import com.example.vibralavida.api.modelos.RitmoProgresoRequest
import com.example.vibralavida.api.modelos.RitmoProgresoResponse

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT

interface RitmoProgresoApi {

    @GET("api/ritmo/progreso")
    fun obtenerProgreso(
        @Header("Authorization")
        authorization: String
    ): Call<RitmoProgresoResponse>

    @PUT("api/ritmo/progreso")
    fun guardarProgreso(
        @Header("Authorization")
        authorization: String,
        @Body
        progreso: RitmoProgresoRequest
    ): Call<RitmoProgresoResponse>

    @POST("api/ritmo/intentos")
    fun guardarIntento(
        @Header("Authorization")
        authorization: String,
        @Body
        intento: RitmoIntentoRequest
    ): Call<RitmoIntentoResponse>
}

internal object RitmoApiClient {

    private const val BASE_URL =
        "https://api-vibra-la-vida.onrender.com/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: RitmoProgresoApi by lazy {
        retrofit.create(RitmoProgresoApi::class.java)
    }
}
