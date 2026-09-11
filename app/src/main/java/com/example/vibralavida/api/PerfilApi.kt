package com.example.vibralavida.api

import com.example.vibralavida.api.modelos.ActualizarPerfilResponse
import com.example.vibralavida.api.modelos.PerfilRequest
import com.example.vibralavida.api.modelos.PerfilResponse

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.DELETE


// ============================================================================
// API DEL PERFIL
// ============================================================================
//
// Aquí describimos para Retrofit los endpoints
// que ya probamos correctamente en Postman.
//
// ============================================================================

interface PerfilApi {


    // ========================================================================
    // OBTENER PERFIL
    // ========================================================================
    //
    // GET /api/users/me
    //
    // Ejemplo del header:
    //
    // Authorization: Bearer eyJhbGci...
    //
    // ========================================================================

    @GET("api/users/me")
    fun obtenerPerfil(

        @Header("Authorization")
        authorization: String

    ): Call<PerfilResponse>


    // ========================================================================
    // ACTUALIZAR PERFIL
    // ========================================================================
    //
    // PUT /api/users/me
    //
    // ========================================================================

    @PUT("api/users/me")
    fun actualizarPerfil(

        @Header("Authorization")
        authorization: String,

        @Body
        perfil: PerfilRequest

    ): Call<ActualizarPerfilResponse>

    // ========================================================================
    // ELIMINAR CUENTA
    // ========================================================================
    //
    // DELETE /api/users/me
    //
    // ========================================================================

    @DELETE("api/users/me")
    fun eliminarCuenta(

        @Header("Authorization")
        authorization: String

    ): Call<ActualizarPerfilResponse>
}