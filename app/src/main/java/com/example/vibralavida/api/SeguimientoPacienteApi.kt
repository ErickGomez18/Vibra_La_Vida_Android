package com.example.vibralavida.api

import com.example.vibralavida.api.modelos.AutorizarSeguimientoRequest
import com.example.vibralavida.api.modelos.CodigoSeguimientoResponse
import com.example.vibralavida.api.modelos.MisVinculosSeguimientoResponse
import com.example.vibralavida.api.modelos.OperacionSeguimientoResponse

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path


// ============================================================================
// SEGUIMIENTO PACIENTE API
// ============================================================================
//
// Android utiliza los MISMOS endpoints que la web.
// El token de Firebase identifica al paciente.
//
// No usamos SMS.
// No usamos reCAPTCHA.
// ============================================================================

interface SeguimientoPacienteApi {


    // ------------------------------------------------------------------------
    // CONSULTAR SOLICITUDES Y EQUIPO DE SALUD
    // ------------------------------------------------------------------------

    @GET(
        "api/seguimiento/mis-vinculos"
    )
    fun obtenerMisVinculos(

        @Header("Authorization")
        authorization: String

    ): Call<MisVinculosSeguimientoResponse>


    // ------------------------------------------------------------------------
    // OBTENER OTP DENTRO DE VIBRA LA VIDA
    // ------------------------------------------------------------------------

    @POST(
        "api/seguimiento/{id}/codigo"
    )
    fun obtenerCodigo(

        @Header("Authorization")
        authorization: String,

        @Path("id")
        seguimientoId: String

    ): Call<CodigoSeguimientoResponse>


    // ------------------------------------------------------------------------
    // AUTORIZAR CON OTP
    // ------------------------------------------------------------------------

    @POST(
        "api/seguimiento/{id}/autorizar"
    )
    fun autorizar(

        @Header("Authorization")
        authorization: String,

        @Path("id")
        seguimientoId: String,

        @Body
        request: AutorizarSeguimientoRequest

    ): Call<OperacionSeguimientoResponse>


    // ------------------------------------------------------------------------
    // RECHAZAR SOLICITUD
    // ------------------------------------------------------------------------

    @POST(
        "api/seguimiento/{id}/rechazar"
    )
    fun rechazar(

        @Header("Authorization")
        authorization: String,

        @Path("id")
        seguimientoId: String

    ): Call<OperacionSeguimientoResponse>
}
