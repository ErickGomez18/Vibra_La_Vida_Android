package com.example.vibralavida.api

import com.example.vibralavida.api.modelos.HabitosIntentoRequest
import com.example.vibralavida.api.modelos.HabitosIntentoResponse
import com.example.vibralavida.api.modelos.HabitosProgresoRemoto
import com.example.vibralavida.api.modelos.HabitosProgresoRequest
import com.example.vibralavida.api.modelos.HabitosProgresoResponse

import com.google.firebase.auth.FirebaseAuth

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


object HabitosProgresoRepository {


    private fun conToken(

        onToken:
            (String) -> Unit,

        onUnauthorized:
            (() -> Unit)? = null,

        onError:
            (String) -> Unit
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
            .getIdToken(
                false
            )
            .addOnSuccessListener {
                    result ->

                val token =
                    result.token


                if (
                    token.isNullOrBlank()
                ) {

                    onError(
                        "No fue posible obtener el token de sesión."
                    )

                    return@addOnSuccessListener
                }


                onToken(
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


    fun obtenerProgreso(

        onSuccess:
            (HabitosProgresoRemoto?) -> Unit,

        onUnauthorized:
            (() -> Unit)? = null,

        onError:
            (String) -> Unit
    ) {

        conToken(

            onToken = {
                    token ->

                HabitosApiClient
                    .api
                    .obtenerProgreso(
                        token
                    )
                    .enqueue(

                        object :
                            Callback<HabitosProgresoResponse> {

                            override fun onResponse(
                                call:
                                    Call<HabitosProgresoResponse>,
                                response:
                                    Response<HabitosProgresoResponse>
                            ) {

                                if (
                                    response.code() ==
                                        401
                                ) {

                                    onUnauthorized?.invoke()

                                    return
                                }


                                if (
                                    !response.isSuccessful
                                ) {

                                    onError(
                                        "Error ${response.code()} al obtener el progreso."
                                    )

                                    return
                                }


                                val body =
                                    response.body()


                                if (
                                    body == null ||
                                    !body.success
                                ) {

                                    onError(
                                        body?.message
                                            ?: "No fue posible obtener el progreso."
                                    )

                                    return
                                }


                                onSuccess(
                                    if (
                                        body.existe
                                    ) {
                                        body.progreso
                                    } else {
                                        null
                                    }
                                )
                            }


                            override fun onFailure(
                                call:
                                    Call<HabitosProgresoResponse>,
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

            onUnauthorized =
                onUnauthorized,

            onError =
                onError
        )
    }


    fun guardarProgreso(

        progreso:
            HabitosProgresoRequest,

        onSuccess:
            () -> Unit,

        onUnauthorized:
            (() -> Unit)? = null,

        onError:
            (String) -> Unit
    ) {

        conToken(

            onToken = {
                    token ->

                HabitosApiClient
                    .api
                    .guardarProgreso(
                        token,
                        progreso
                    )
                    .enqueue(

                        object :
                            Callback<HabitosProgresoResponse> {

                            override fun onResponse(
                                call:
                                    Call<HabitosProgresoResponse>,
                                response:
                                    Response<HabitosProgresoResponse>
                            ) {

                                if (
                                    response.code() ==
                                        401
                                ) {

                                    onUnauthorized?.invoke()

                                    return
                                }


                                if (
                                    response.isSuccessful &&
                                    response.body()?.success ==
                                        true
                                ) {

                                    onSuccess()

                                } else {

                                    onError(
                                        "Error ${response.code()} al guardar el progreso."
                                    )
                                }
                            }


                            override fun onFailure(
                                call:
                                    Call<HabitosProgresoResponse>,
                                t:
                                    Throwable
                            ) {

                                onError(
                                    t.message
                                        ?: "No fue posible guardar el progreso."
                                )
                            }
                        }
                    )
            },

            onUnauthorized =
                onUnauthorized,

            onError =
                onError
        )
    }


    fun guardarIntento(

        intento:
            HabitosIntentoRequest,

        onSuccess:
            () -> Unit,

        onUnauthorized:
            (() -> Unit)? = null,

        onError:
            (String) -> Unit
    ) {

        conToken(

            onToken = {
                    token ->

                HabitosApiClient
                    .api
                    .guardarIntento(
                        token,
                        intento
                    )
                    .enqueue(

                        object :
                            Callback<HabitosIntentoResponse> {

                            override fun onResponse(
                                call:
                                    Call<HabitosIntentoResponse>,
                                response:
                                    Response<HabitosIntentoResponse>
                            ) {

                                if (
                                    response.code() ==
                                        401
                                ) {

                                    onUnauthorized?.invoke()

                                    return
                                }


                                if (
                                    response.isSuccessful &&
                                    response.body()?.success ==
                                        true
                                ) {

                                    onSuccess()

                                } else {

                                    onError(
                                        "Error ${response.code()} al guardar el intento."
                                    )
                                }
                            }


                            override fun onFailure(
                                call:
                                    Call<HabitosIntentoResponse>,
                                t:
                                    Throwable
                            ) {

                                onError(
                                    t.message
                                        ?: "No fue posible guardar el intento."
                                )
                            }
                        }
                    )
            },

            onUnauthorized =
                onUnauthorized,

            onError =
                onError
        )
    }
}
