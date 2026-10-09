package com.example.vibralavida.api

import com.example.vibralavida.api.modelos.RespuestaPacienteCitaResponse

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path


// ============================================================================
// RESPUESTA DEL PACIENTE A CITAS API
// ============================================================================
//
// Android usa exactamente los mismos endpoints que la web.
//
// Ya NO usamos:
// PATCH /api/citas/:id/respuesta-paciente
//
// Ahora usamos:
// POST /api/citas/:id/confirmar
// POST /api/citas/:id/solicitar-reagenda
// POST /api/citas/:id/cancelar
//
// ============================================================================

interface CitasPacienteApi {


    // ========================================================================
    // CONFIRMAR CITA
    // ========================================================================

    @POST(
        "api/citas/{id}/confirmar"
    )
    fun confirmarCita(

        @Header("Authorization")
        authorization: String,

        @Path("id")
        citaId: String

    ): Call<RespuestaPacienteCitaResponse>


    // ========================================================================
    // SOLICITAR REAGENDA
    // ========================================================================

    @POST(
        "api/citas/{id}/solicitar-reagenda"
    )
    fun solicitarReagenda(

        @Header("Authorization")
        authorization: String,

        @Path("id")
        citaId: String,

        @Body
        body: Map<String, String>

    ): Call<RespuestaPacienteCitaResponse>


    // ========================================================================
    // CANCELAR CITA
    // ========================================================================

    @POST(
        "api/citas/{id}/cancelar"
    )
    fun cancelarCita(

        @Header("Authorization")
        authorization: String,

        @Path("id")
        citaId: String,

        @Body
        body: Map<String, String>

    ): Call<RespuestaPacienteCitaResponse>
}
