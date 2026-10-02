package com.example.vibralavida.api

import com.example.vibralavida.agenda.citas.Cita
import com.example.vibralavida.api.modelos.RespuestaPacienteCitaRequest
import com.example.vibralavida.api.modelos.RespuestaPacienteCitaResponse

import com.google.firebase.auth.FirebaseAuth

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


// ============================================================================
// CITAS PACIENTE REPOSITORY
// ============================================================================

object CitasPacienteRepository {


    // ========================================================================
    // ACTUALIZAR RESPUESTA
    // ========================================================================

    fun actualizarRespuesta(

        citaId: String,

        request: RespuestaPacienteCitaRequest,

        onSuccess: (
            Cita,
            String
        ) -> Unit,

        onUnauthorized: (() -> Unit)? = null,

        onError: (String) -> Unit

    ) {

        val usuario =
            FirebaseAuth
                .getInstance()
                .currentUser


        if (
            usuario == null
        ) {

            onUnauthorized?.invoke()

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

                    return@addOnSuccessListener
                }


                ApiClient
                    .citasPacienteApi
                    .actualizarRespuestaPaciente(

                        authorization =
                            "Bearer $token",

                        citaId =
                            citaId,

                        request =
                            request
                    )
                    .enqueue(

                        object :
                            Callback<RespuestaPacienteCitaResponse> {


                            override fun onResponse(

                                call:
                                Call<RespuestaPacienteCitaResponse>,

                                response:
                                Response<RespuestaPacienteCitaResponse>

                            ) {

                                if (
                                    response.code() == 401
                                ) {

                                    onUnauthorized?.invoke()

                                    return
                                }


                                val body =
                                    response.body()


                                if (
                                    response.isSuccessful &&
                                    body?.success == true &&
                                    body.cita != null
                                ) {

                                    onSuccess(

                                        body.cita,

                                        body.message
                                            ?: "Respuesta actualizada."
                                    )

                                } else {

                                    onError(

                                        body?.message
                                            ?: "No fue posible actualizar la cita."
                                    )
                                }
                            }


                            override fun onFailure(

                                call:
                                Call<RespuestaPacienteCitaResponse>,

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
