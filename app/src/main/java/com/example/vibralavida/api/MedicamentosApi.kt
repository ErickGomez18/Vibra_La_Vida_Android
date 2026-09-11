package com.example.vibralavida.api

import com.example.vibralavida.api.modelos.MedicamentoOperacionResponse
import com.example.vibralavida.api.modelos.MedicamentoRequest
import com.example.vibralavida.api.modelos.MedicamentoResponse
import com.example.vibralavida.api.modelos.MedicamentosResponse

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path


// ============================================================================
// MEDICAMENTOS API
// ============================================================================
//
// Endpoints protegidos por Firebase Authentication.
//
// La API espera:
//
// Authorization: Bearer TOKEN
//
// ============================================================================

interface MedicamentosApi {


    // ========================================================================
    // OBTENER MEDICAMENTOS
    // ========================================================================
    //
    // GET /api/medicamentos
    //
    // ========================================================================

    @GET("api/medicamentos")
    fun obtenerMedicamentos(

        @Header("Authorization")
        authorization: String

    ): Call<MedicamentosResponse>


    // ========================================================================
    // CREAR MEDICAMENTO
    // ========================================================================
    //
    // POST /api/medicamentos
    //
    // ========================================================================

    @POST("api/medicamentos")
    fun crearMedicamento(

        @Header("Authorization")
        authorization: String,

        @Body
        medicamento: MedicamentoRequest

    ): Call<MedicamentoResponse>


    // ========================================================================
    // ACTUALIZAR MEDICAMENTO
    // ========================================================================
    //
    // PUT /api/medicamentos/:id
    //
    // ========================================================================

    @PUT("api/medicamentos/{id}")
    fun actualizarMedicamento(

        @Header("Authorization")
        authorization: String,

        @Path("id")
        medicamentoId: String,

        @Body
        medicamento: MedicamentoRequest

    ): Call<MedicamentoOperacionResponse>


    // ========================================================================
    // ELIMINAR MEDICAMENTO
    // ========================================================================
    //
    // DELETE /api/medicamentos/:id
    //
    // ========================================================================

    @DELETE("api/medicamentos/{id}")
    fun eliminarMedicamento(

        @Header("Authorization")
        authorization: String,

        @Path("id")
        medicamentoId: String

    ): Call<MedicamentoOperacionResponse>
}
