package com.example.vibralavida.api

import com.example.vibralavida.agenda.citas.Cita
import com.example.vibralavida.api.modelos.CitasResponse

import com.google.firebase.auth.FirebaseAuth

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


// ============================================================================
// CITAS REPOSITORY
// ============================================================================
//
// Se encarga de:
//
// 1. Obtener el token actual de Firebase.
// 2. Mandarlo como Bearer Token a la API Express.
// 3. Convertir la respuesta en una lista de Cita.
// 4. Entregar el resultado a MainActivity / Compose.
//
// ============================================================================

object CitasRepository {


    // ========================================================================
    // OBTENER CITAS
    // ========================================================================

    fun obtenerCitas(

        onSuccess: (List<Cita>) -> Unit,

        onUnauthorized: (() -> Unit)? = null,

        onError: (String) -> Unit

    ) {


        // ====================================================================
        // USUARIO ACTUAL
        // ====================================================================

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


        // ====================================================================
        // TOKEN FIREBASE
        // ====================================================================

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


                // ============================================================
                // LLAMAR A LA API
                // ============================================================

                ApiClient
                    .citasApi
                    .obtenerCitas(
                        "Bearer $token"
                    )
                    .enqueue(

                        object :
                            Callback<CitasResponse> {


                            override fun onResponse(

                                call:
                                Call<CitasResponse>,

                                response:
                                Response<CitasResponse>

                            ) {


                                // ------------------------------------------------
                                // TOKEN INVÁLIDO / SESIÓN VENCIDA
                                // ------------------------------------------------

                                if (
                                    response.code() == 401
                                ) {

                                    onUnauthorized?.invoke()

                                    return
                                }


                                // ------------------------------------------------
                                // ERROR HTTP
                                // ------------------------------------------------

                                if (
                                    !response.isSuccessful
                                ) {

                                    onError(
                                        "Error ${response.code()} al obtener las citas."
                                    )

                                    return
                                }


                                // ------------------------------------------------
                                // RESPUESTA
                                // ------------------------------------------------

                                val body =
                                    response.body()


                                if (
                                    body == null
                                ) {

                                    onError(
                                        "La API no devolvió información."
                                    )

                                    return
                                }


                                if (
                                    !body.success
                                ) {

                                    onError(
                                        body.message
                                            ?: "No fue posible obtener las citas."
                                    )

                                    return
                                }


                                // ------------------------------------------------
                                // ÉXITO
                                // ------------------------------------------------

                                onSuccess(
                                    body.citas
                                )
                            }


                            override fun onFailure(

                                call:
                                Call<CitasResponse>,

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
