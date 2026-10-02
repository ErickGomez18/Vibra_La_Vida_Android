package com.example.vibralavida.api

import com.example.vibralavida.agenda.medicamentos.RegistroAdherenciaMedicamento
import com.example.vibralavida.api.modelos.AdherenciaMedicamentoOperacionResponse
import com.example.vibralavida.api.modelos.AdherenciaMedicamentoRequest
import com.example.vibralavida.api.modelos.AdherenciaMedicamentosResponse

import com.google.firebase.auth.FirebaseAuth

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


// ============================================================================
// ADHERENCIA DE MEDICAMENTOS REPOSITORY
// ============================================================================

object AdherenciaMedicamentosRepository {


    // ========================================================================
    // REGISTRAR ESTADO
    // ========================================================================

    fun registrarEstado(

        request: AdherenciaMedicamentoRequest,

        onSuccess: (RegistroAdherenciaMedicamento) -> Unit,

        onError: (String) -> Unit

    ) {

        obtenerToken(

            onSuccess = {
                    token ->


                ApiClient
                    .adherenciaMedicamentosApi
                    .registrarEstado(

                        authorization =
                            "Bearer $token",

                        request =
                            request
                    )
                    .enqueue(

                        object :
                            Callback<AdherenciaMedicamentoOperacionResponse> {


                            override fun onResponse(

                                call:
                                Call<AdherenciaMedicamentoOperacionResponse>,

                                response:
                                Response<AdherenciaMedicamentoOperacionResponse>

                            ) {

                                val body =
                                    response.body()


                                if (
                                    response.isSuccessful &&
                                    body?.success == true &&
                                    body.registro != null
                                ) {

                                    onSuccess(
                                        body.registro
                                    )

                                } else {

                                    onError(

                                        body?.message
                                            ?: "No fue posible registrar el medicamento."
                                    )
                                }
                            }


                            override fun onFailure(

                                call:
                                Call<AdherenciaMedicamentoOperacionResponse>,

                                t:
                                Throwable

                            ) {

                                onError(

                                    t.message
                                        ?: "No fue posible conectarse con la API."
                                )
                            }
                        }
                    )
            },

            onError =
                onError
        )
    }


    // ========================================================================
    // OBTENER HISTORIAL
    // ========================================================================

    fun obtenerHistorial(

        onSuccess: (List<RegistroAdherenciaMedicamento>) -> Unit,

        onError: (String) -> Unit

    ) {

        obtenerToken(

            onSuccess = {
                    token ->


                ApiClient
                    .adherenciaMedicamentosApi
                    .obtenerHistorial(

                        authorization =
                            "Bearer $token"
                    )
                    .enqueue(

                        object :
                            Callback<AdherenciaMedicamentosResponse> {


                            override fun onResponse(

                                call:
                                Call<AdherenciaMedicamentosResponse>,

                                response:
                                Response<AdherenciaMedicamentosResponse>

                            ) {

                                val body =
                                    response.body()


                                if (
                                    response.isSuccessful &&
                                    body?.success == true
                                ) {

                                    onSuccess(
                                        body.registros
                                    )

                                } else {

                                    onError(

                                        body?.message
                                            ?: "No fue posible obtener el historial."
                                    )
                                }
                            }


                            override fun onFailure(

                                call:
                                Call<AdherenciaMedicamentosResponse>,

                                t:
                                Throwable

                            ) {

                                onError(

                                    t.message
                                        ?: "No fue posible conectarse con la API."
                                )
                            }
                        }
                    )
            },

            onError =
                onError
        )
    }


    // ========================================================================
    // TOKEN FIREBASE
    // ========================================================================

    private fun obtenerToken(

        onSuccess: (String) -> Unit,

        onError: (String) -> Unit

    ) {

        val usuario =
            FirebaseAuth
                .getInstance()
                .currentUser


        if (
            usuario == null
        ) {

            onError(
                "No existe una sesión activa."
            )

            return
        }


        usuario
            .getIdToken(false)
            .addOnSuccessListener {
                    tokenResult ->


                val token =
                    tokenResult.token


                if (
                    token.isNullOrBlank()
                ) {

                    onError(
                        "No fue posible obtener el token de sesión."
                    )

                } else {

                    onSuccess(
                        token
                    )
                }
            }
            .addOnFailureListener {
                    error ->


                onError(

                    error.message
                        ?: "No fue posible obtener la sesión."
                )
            }
    }
}
