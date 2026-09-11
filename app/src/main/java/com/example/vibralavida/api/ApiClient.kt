package com.example.vibralavida.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


// ============================================================================
// CLIENTE DE LA API
// ============================================================================

object ApiClient {


    // ========================================================================
    // URL PÚBLICA DE RENDER
    // ========================================================================
    //
    // Ya no utilizamos la IP local de la laptop.
    //
    // Esta URL funciona desde:
    //
    // - Wi-Fi de la escuela
    // - Wi-Fi de casa
    // - Datos móviles
    // - Cualquier otra red
    //
    // ========================================================================

    private const val BASE_URL =
        "https://api-vibra-la-vida.onrender.com/"


    // ========================================================================
    // CLIENTE HTTP
    // ========================================================================
    //
    // Render Free puede "dormir" nuestra API después de un periodo
    // sin actividad.
    //
    // Cuando vuelve a recibir una petición puede tardar varios segundos
    // en despertar.
    //
    // Por eso aumentamos los tiempos de espera.
    //
    // ========================================================================

    private val okHttpClient: OkHttpClient by lazy {

        OkHttpClient
            .Builder()

            // Tiempo máximo para establecer conexión.
            .connectTimeout(
                60,
                TimeUnit.SECONDS
            )

            // Tiempo máximo esperando una respuesta del servidor.
            .readTimeout(
                90,
                TimeUnit.SECONDS
            )

            // Tiempo máximo para enviar información.
            .writeTimeout(
                60,
                TimeUnit.SECONDS
            )

            // Tiempo máximo de toda la llamada.
            .callTimeout(
                90,
                TimeUnit.SECONDS
            )

            .build()
    }


    // ========================================================================
    // RETROFIT
    // ========================================================================

    private val retrofit: Retrofit by lazy {

        Retrofit
            .Builder()

            .baseUrl(
                BASE_URL
            )

            // Usamos nuestro cliente con tiempos mayores.
            .client(
                okHttpClient
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


    // ========================================================================
    // MEDICAMENTOS API
    // ========================================================================

    val medicamentosApi: MedicamentosApi by lazy {

        retrofit.create(
            MedicamentosApi::class.java
        )
    }
}