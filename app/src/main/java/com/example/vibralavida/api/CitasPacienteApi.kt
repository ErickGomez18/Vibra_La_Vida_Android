package com.example.vibralavida.api

import com.example.vibralavida.api.modelos.RespuestaPacienteCitaRequest
import com.example.vibralavida.api.modelos.RespuestaPacienteCitaResponse

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.Path


// ============================================================================
// RESPUESTA DEL PACIENTE A CITAS API
// ============================================================================

interface CitasPacienteApi {


    @PATCH(
        "api/citas/{id}/respuesta-paciente"
    )
    fun actualizarRespuestaPaciente(

        @Header("Authorization")
        authorization: String,

        @Path("id")
        citaId: String,

        @Body
        request: RespuestaPacienteCitaRequest

    ): Call<RespuestaPacienteCitaResponse>
}
