package com.example.vibralavida.api

import com.example.vibralavida.api.modelos.EstudioLaboratorioOperacionResponse
import com.example.vibralavida.api.modelos.EstudioLaboratorioRequest
import com.example.vibralavida.api.modelos.EstudioLaboratorioResponse
import com.example.vibralavida.api.modelos.EstudiosLaboratorioResponse
import com.example.vibralavida.api.modelos.RegistroSaludOperacionResponse
import com.example.vibralavida.api.modelos.RegistroSaludRequest
import com.example.vibralavida.api.modelos.RegistroSaludResponse
import com.example.vibralavida.api.modelos.RegistrosSaludResponse

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path


// ============================================================================
// BITÁCORA API
// ============================================================================
//
// Endpoints protegidos por Firebase Authentication.
//
// Authorization: Bearer TOKEN
//
// ============================================================================

interface BitacoraApi {


    // ========================================================================
    // REGISTROS DE SALUD
    // ========================================================================

    @GET("api/bitacora/registros")
    fun obtenerRegistros(

        @Header("Authorization")
        authorization: String

    ): Call<RegistrosSaludResponse>


    @POST("api/bitacora/registros")
    fun crearRegistro(

        @Header("Authorization")
        authorization: String,

        @Body
        registro: RegistroSaludRequest

    ): Call<RegistroSaludResponse>


    @PUT("api/bitacora/registros/{id}")
    fun actualizarRegistro(

        @Header("Authorization")
        authorization: String,

        @Path("id")
        registroId: String,

        @Body
        registro: RegistroSaludRequest

    ): Call<RegistroSaludOperacionResponse>


    @DELETE("api/bitacora/registros/{id}")
    fun eliminarRegistro(

        @Header("Authorization")
        authorization: String,

        @Path("id")
        registroId: String

    ): Call<RegistroSaludOperacionResponse>


    // ========================================================================
    // LABORATORIOS
    // ========================================================================

    @GET("api/bitacora/laboratorios")
    fun obtenerLaboratorios(

        @Header("Authorization")
        authorization: String

    ): Call<EstudiosLaboratorioResponse>


    @POST("api/bitacora/laboratorios")
    fun crearLaboratorio(

        @Header("Authorization")
        authorization: String,

        @Body
        estudio: EstudioLaboratorioRequest

    ): Call<EstudioLaboratorioResponse>


    @PUT("api/bitacora/laboratorios/{id}")
    fun actualizarLaboratorio(

        @Header("Authorization")
        authorization: String,

        @Path("id")
        estudioId: String,

        @Body
        estudio: EstudioLaboratorioRequest

    ): Call<EstudioLaboratorioOperacionResponse>


    @DELETE("api/bitacora/laboratorios/{id}")
    fun eliminarLaboratorio(

        @Header("Authorization")
        authorization: String,

        @Path("id")
        estudioId: String

    ): Call<EstudioLaboratorioOperacionResponse>
}
