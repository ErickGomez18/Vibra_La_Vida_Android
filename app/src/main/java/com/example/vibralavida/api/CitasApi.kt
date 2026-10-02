package com.example.vibralavida.api

import com.example.vibralavida.api.modelos.CitasResponse

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header


// ============================================================================
// CITAS API
// ============================================================================
//
// La app móvil del paciente solamente necesita CONSULTAR sus citas.
//
// Las citas son creadas y administradas desde el panel web del profesional.
//
// ============================================================================

interface CitasApi {


    // ========================================================================
    // OBTENER MIS CITAS
    // ========================================================================
    //
    // GET /api/citas
    //
    // El backend obtiene el UID desde el token de Firebase y devuelve
    // solamente las citas correspondientes al usuario autenticado.
    //
    // ========================================================================

    @GET("api/citas")
    fun obtenerCitas(

        @Header("Authorization")
        authorization: String

    ): Call<CitasResponse>
}
