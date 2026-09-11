package com.example.vibralavida.api

import com.example.vibralavida.agenda.medicamentos.Medicamento
import com.example.vibralavida.api.modelos.MedicamentoOperacionResponse
import com.example.vibralavida.api.modelos.MedicamentoRequest
import com.example.vibralavida.api.modelos.MedicamentoResponse
import com.example.vibralavida.api.modelos.MedicamentosResponse

import com.google.firebase.auth.FirebaseAuth

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


// ============================================================================
// MEDICAMENTOS REPOSITORY
// ============================================================================
//
// Flujo:
//
// Firebase Auth
//      ↓
// ID Token
//      ↓
// Retrofit
//      ↓
// API Express / Render
//      ↓
// Firestore
//
// ============================================================================

object MedicamentosRepository {


    // ========================================================================
    // OBTENER TOKEN
    // ========================================================================

    private fun obtenerToken(

        onSuccess: (String) -> Unit,

        onError: (String) -> Unit

    ) {

        val usuarioActual =
            FirebaseAuth
                .getInstance()
                .currentUser


        if (usuarioActual == null) {

            onError(
                "No existe una sesión activa."
            )

            return
        }


        usuarioActual
            .getIdToken(false)
            .addOnSuccessListener {
                    tokenResult ->


                val token =
                    tokenResult.token


                if (token.isNullOrBlank()) {

                    onError(
                        "No fue posible obtener el token de sesión."
                    )

                    return@addOnSuccessListener
                }


                onSuccess(
                    "Bearer $token"
                )
            }
            .addOnFailureListener {
                    error ->


                onError(

                    error.message
                        ?: "No fue posible obtener la sesión."
                )
            }
    }


    // ========================================================================
    // OBTENER TODOS
    // ========================================================================

    fun obtenerMedicamentos(

        onSuccess: (List<Medicamento>) -> Unit,

        onError: (String) -> Unit

    ) {

        obtenerToken(

            onSuccess = {
                    authorization ->


                ApiClient
                    .medicamentosApi
                    .obtenerMedicamentos(
                        authorization = authorization
                    )
                    .enqueue(

                        object :
                            Callback<MedicamentosResponse> {


                            override fun onResponse(

                                call:
                                Call<MedicamentosResponse>,

                                response:
                                Response<MedicamentosResponse>

                            ) {

                                if (response.isSuccessful) {

                                    val body =
                                        response.body()


                                    if (body?.success == true) {

                                        onSuccess(
                                            body.medicamentos
                                        )

                                    } else {

                                        onError(

                                            body?.message
                                                ?: "No fue posible obtener los medicamentos."
                                        )
                                    }

                                } else {

                                    onError(

                                        when (response.code()) {

                                            401 ->
                                                "La sesión no es válida."

                                            else ->
                                                "Error del servidor: ${response.code()}"
                                        }
                                    )
                                }
                            }


                            override fun onFailure(

                                call:
                                Call<MedicamentosResponse>,

                                throwable:
                                Throwable

                            ) {

                                onError(

                                    throwable.message
                                        ?: "No fue posible conectarse con la API."
                                )
                            }
                        }
                    )
            },

            onError = onError
        )
    }


    // ========================================================================
    // CREAR
    // ========================================================================

    fun crearMedicamento(

        medicamento: MedicamentoRequest,

        onSuccess: (Medicamento) -> Unit,

        onError: (String) -> Unit

    ) {

        obtenerToken(

            onSuccess = {
                    authorization ->


                ApiClient
                    .medicamentosApi
                    .crearMedicamento(

                        authorization = authorization,

                        medicamento = medicamento
                    )
                    .enqueue(

                        object :
                            Callback<MedicamentoResponse> {


                            override fun onResponse(

                                call:
                                Call<MedicamentoResponse>,

                                response:
                                Response<MedicamentoResponse>

                            ) {

                                if (response.isSuccessful) {

                                    val body =
                                        response.body()

                                    val creado =
                                        body?.medicamento


                                    if (
                                        body?.success == true &&
                                        creado != null
                                    ) {

                                        onSuccess(
                                            creado
                                        )

                                    } else {

                                        onError(

                                            body?.message
                                                ?: "No fue posible crear el medicamento."
                                        )
                                    }

                                } else {

                                    onError(

                                        when (response.code()) {

                                            400 ->
                                                "Los datos del medicamento no son válidos."

                                            401 ->
                                                "La sesión no es válida."

                                            else ->
                                                "Error del servidor: ${response.code()}"
                                        }
                                    )
                                }
                            }


                            override fun onFailure(

                                call:
                                Call<MedicamentoResponse>,

                                throwable:
                                Throwable

                            ) {

                                onError(

                                    throwable.message
                                        ?: "No fue posible conectarse con la API."
                                )
                            }
                        }
                    )
            },

            onError = onError
        )
    }


    // ========================================================================
    // ACTUALIZAR
    // ========================================================================

    fun actualizarMedicamento(

        medicamentoId: String,

        medicamento: MedicamentoRequest,

        onSuccess: () -> Unit,

        onError: (String) -> Unit

    ) {

        obtenerToken(

            onSuccess = {
                    authorization ->


                ApiClient
                    .medicamentosApi
                    .actualizarMedicamento(

                        authorization = authorization,

                        medicamentoId = medicamentoId,

                        medicamento = medicamento
                    )
                    .enqueue(

                        object :
                            Callback<MedicamentoOperacionResponse> {


                            override fun onResponse(

                                call:
                                Call<MedicamentoOperacionResponse>,

                                response:
                                Response<MedicamentoOperacionResponse>

                            ) {

                                if (response.isSuccessful) {

                                    val body =
                                        response.body()


                                    if (body?.success == true) {

                                        onSuccess()

                                    } else {

                                        onError(

                                            body?.message
                                                ?: "No fue posible actualizar el medicamento."
                                        )
                                    }

                                } else {

                                    onError(

                                        when (response.code()) {

                                            400 ->
                                                "Los datos del medicamento no son válidos."

                                            401 ->
                                                "La sesión no es válida."

                                            404 ->
                                                "El medicamento no existe."

                                            else ->
                                                "Error del servidor: ${response.code()}"
                                        }
                                    )
                                }
                            }


                            override fun onFailure(

                                call:
                                Call<MedicamentoOperacionResponse>,

                                throwable:
                                Throwable

                            ) {

                                onError(

                                    throwable.message
                                        ?: "No fue posible conectarse con la API."
                                )
                            }
                        }
                    )
            },

            onError = onError
        )
    }


    // ========================================================================
    // ELIMINAR
    // ========================================================================

    fun eliminarMedicamento(

        medicamentoId: String,

        onSuccess: () -> Unit,

        onError: (String) -> Unit

    ) {

        obtenerToken(

            onSuccess = {
                    authorization ->


                ApiClient
                    .medicamentosApi
                    .eliminarMedicamento(

                        authorization = authorization,

                        medicamentoId = medicamentoId
                    )
                    .enqueue(

                        object :
                            Callback<MedicamentoOperacionResponse> {


                            override fun onResponse(

                                call:
                                Call<MedicamentoOperacionResponse>,

                                response:
                                Response<MedicamentoOperacionResponse>

                            ) {

                                if (response.isSuccessful) {

                                    val body =
                                        response.body()


                                    if (body?.success == true) {

                                        onSuccess()

                                    } else {

                                        onError(

                                            body?.message
                                                ?: "No fue posible eliminar el medicamento."
                                        )
                                    }

                                } else {

                                    onError(

                                        when (response.code()) {

                                            401 ->
                                                "La sesión no es válida."

                                            404 ->
                                                "El medicamento no existe."

                                            else ->
                                                "Error del servidor: ${response.code()}"
                                        }
                                    )
                                }
                            }


                            override fun onFailure(

                                call:
                                Call<MedicamentoOperacionResponse>,

                                throwable:
                                Throwable

                            ) {

                                onError(

                                    throwable.message
                                        ?: "No fue posible conectarse con la API."
                                )
                            }
                        }
                    )
            },

            onError = onError
        )
    }
}
