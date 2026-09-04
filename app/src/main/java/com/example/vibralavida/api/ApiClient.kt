package com.example.vibralavida.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


// ============================================================================
// CLIENTE DE LA API
// ============================================================================

object ApiClient {


    // ========================================================================
    // URL DE LA API
    // ========================================================================
    //
    // Estamos probando desde un CELULAR FÍSICO.
    //
    // Por eso usamos la IPv4 de la laptop dentro de la red Wi-Fi.
    //
    // Laptop:
    // 192.168.174.2
    //
    // Puerto de la API:
    // 3001
    //
    // ========================================================================

    private const val BASE_URL =
        "http://192.168.174.16:3001/"


    // ========================================================================
    // RETROFIT
    // ========================================================================

    private val retrofit: Retrofit by lazy {

        Retrofit.Builder()

            .baseUrl(
                BASE_URL
            )

            .addConverterFactory(
                GsonConverterFactory.create()
            )

            .build()
    }


    // ========================================================================
    // PERFIL API
    // ========================================================================

    val perfilApi: PerfilApi by lazy {

        retrofit.create(
            PerfilApi::class.java
        )
    }
}