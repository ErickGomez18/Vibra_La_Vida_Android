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
    // Esta URL funciona desde:
    //
    // - Wi-Fi de la escuela
    // - Wi-Fi de casa
    // - Datos móviles
    // - Cualquier otra red
    //
    // IMPORTANTE:
    // La URL debe terminar con "/".
    //
    // ========================================================================

    private const val BASE_URL =
        "https://api-vibra-la-vida.onrender.com/"


    // ========================================================================
    // CLIENTE HTTP
    // ========================================================================
    //
    // Render Free puede tardar varios segundos en despertar
    // después de estar un tiempo sin actividad.
    //
    // Por eso usamos tiempos de espera un poco más largos.
    //
    // ========================================================================

    private val okHttpClient: OkHttpClient by lazy {

        OkHttpClient
            .Builder()

            .connectTimeout(
                60,
                TimeUnit.SECONDS
            )

            .readTimeout(
                90,
                TimeUnit.SECONDS
            )

            .writeTimeout(
                60,
                TimeUnit.SECONDS
            )

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


    // ========================================================================
    // BITÁCORA API
    // ========================================================================

    val bitacoraApi: BitacoraApi by lazy {

        retrofit.create(
            BitacoraApi::class.java
        )
    }


    // ========================================================================
    // SUBIDA DE ARCHIVOS / CLOUDINARY
    // ========================================================================

    val uploadsApi: UploadsApi by lazy {

        retrofit.create(
            UploadsApi::class.java
        )
    }


    // ========================================================================
    // CITAS API
    // ========================================================================

    val citasApi: CitasApi by lazy {

        retrofit.create(
            CitasApi::class.java
        )
    }


    // ========================================================================
    // HEALTH CONNECT API
    // ========================================================================

    val healthConnectApi: HealthConnectApi by lazy {

        retrofit.create(
            HealthConnectApi::class.java
        )
    }


    // ========================================================================
    // ADHERENCIA DE MEDICAMENTOS
    // ========================================================================

    val adherenciaMedicamentosApi: AdherenciaMedicamentosApi by lazy {

        retrofit.create(
            AdherenciaMedicamentosApi::class.java
        )
    }


    // ========================================================================
    // RESPUESTA DEL PACIENTE A CITAS
    // ========================================================================

    val citasPacienteApi: CitasPacienteApi by lazy {

        retrofit.create(
            CitasPacienteApi::class.java
        )
    }


    // ========================================================================
    // SEGUIMIENTO PROFESIONAL / EQUIPO DE SALUD
    // ========================================================================
    //
    // Utiliza:
    //
    // GET  /api/seguimiento/mis-vinculos
    // POST /api/seguimiento/:id/codigo
    // POST /api/seguimiento/:id/autorizar
    // POST /api/seguimiento/:id/rechazar
    //
    // ========================================================================

    val seguimientoPacienteApi: SeguimientoPacienteApi by lazy {

        retrofit.create(
            SeguimientoPacienteApi::class.java
        )
    }


    // ========================================================================
    // RESULTADOS / HISTORIAL DE EVALUACIONES
    // ========================================================================

    val resultsApi: ResultsApi by lazy {

        retrofit.create(
            ResultsApi::class.java
        )
    }
}
