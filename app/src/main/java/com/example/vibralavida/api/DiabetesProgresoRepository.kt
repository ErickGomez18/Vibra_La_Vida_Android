package com.example.vibralavida.api

import com.example.vibralavida.api.modelos.DiabetesIntentoRequest
import com.example.vibralavida.api.modelos.DiabetesIntentoResponse
import com.example.vibralavida.api.modelos.DiabetesProgresoRemoto
import com.example.vibralavida.api.modelos.DiabetesProgresoRequest
import com.example.vibralavida.api.modelos.DiabetesProgresoResponse

import com.google.firebase.auth.FirebaseAuth

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


// ============================================================================
// REPOSITORY DEL PROGRESO DE DIABETES
// ============================================================================
//
// Firebase Auth
//      ↓
// ID Token
//      ↓
// API Express
//      ↓
// Firestore
//
// La app NO escribe directamente en Firestore.
// ============================================================================

object DiabetesProgresoRepository {


    // ========================================================================
    // OBTENER TOKEN
    // ========================================================================

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

            onUnauthorized
                ?.invoke()

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
                    resultado ->

                val token =
                    resultado.token


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


    // ========================================================================
    // OBTENER PROGRESO
    // ========================================================================

    fun obtenerProgreso(

        onSuccess:
            (DiabetesProgresoRemoto?) -> Unit,

        onUnauthorized:
            (() -> Unit)? = null,

        onError:
            (String) -> Unit

    ) {

        conToken(

            onToken = {
                    token ->

                DiabetesApiClient
                    .api
                    .obtenerProgreso(
                        token
                    )
                    .enqueue(

                        object :
                            Callback<DiabetesProgresoResponse> {


                            override fun onResponse(

                                call:
                                    Call<DiabetesProgresoResponse>,

                                response:
                                    Response<DiabetesProgresoResponse>

                            ) {

                                if (
                                    response.code() ==
                                    401
                                ) {

                                    onUnauthorized
                                        ?.invoke()

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
                                        body
                                            ?.message
                                            ?: "No fue posible obtener el progreso."
                                    )

                                    return
                                }


                                if (
                                    !body.existe
                                ) {

                                    onSuccess(
                                        null
                                    )

                                    return
                                }


                                onSuccess(
                                    body.progreso
                                )
                            }


                            override fun onFailure(

                                call:
                                    Call<DiabetesProgresoResponse>,

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

            onUnauthorized =
                onUnauthorized,

            onError =
                onError
        )
    }


    // ========================================================================
    // GUARDAR RESUMEN DE PROGRESO
    // ========================================================================

    fun guardarProgreso(

        progreso:
            DiabetesProgresoRequest,

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

                DiabetesApiClient
                    .api
                    .guardarProgreso(
                        authorization =
                            token,
                        progreso =
                            progreso
                    )
                    .enqueue(

                        object :
                            Callback<DiabetesProgresoResponse> {


                            override fun onResponse(

                                call:
                                    Call<DiabetesProgresoResponse>,

                                response:
                                    Response<DiabetesProgresoResponse>

                            ) {

                                if (
                                    response.code() ==
                                    401
                                ) {

                                    onUnauthorized
                                        ?.invoke()

                                    return
                                }


                                if (
                                    !response.isSuccessful
                                ) {

                                    onError(
                                        "Error ${response.code()} al guardar el progreso."
                                    )

                                    return
                                }


                                val body =
                                    response.body()


                                if (
                                    body?.success ==
                                    true
                                ) {

                                    onSuccess()

                                } else {

                                    onError(
                                        body
                                            ?.message
                                            ?: "No fue posible guardar el progreso."
                                    )
                                }
                            }


                            override fun onFailure(

                                call:
                                    Call<DiabetesProgresoResponse>,

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

            onUnauthorized =
                onUnauthorized,

            onError =
                onError
        )
    }


    // ========================================================================
    // GUARDAR UN INTENTO
    // ========================================================================

    fun guardarIntento(

        intento:
            DiabetesIntentoRequest,

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

                DiabetesApiClient
                    .api
                    .guardarIntento(
                        authorization =
                            token,
                        intento =
                            intento
                    )
                    .enqueue(

                        object :
                            Callback<DiabetesIntentoResponse> {


                            override fun onResponse(

                                call:
                                    Call<DiabetesIntentoResponse>,

                                response:
                                    Response<DiabetesIntentoResponse>

                            ) {

                                if (
                                    response.code() ==
                                    401
                                ) {

                                    onUnauthorized
                                        ?.invoke()

                                    return
                                }


                                if (
                                    !response.isSuccessful
                                ) {

                                    onError(
                                        "Error ${response.code()} al guardar el intento."
                                    )

                                    return
                                }


                                val body =
                                    response.body()


                                if (
                                    body?.success ==
                                    true
                                ) {

                                    onSuccess()

                                } else {

                                    onError(
                                        body
                                            ?.message
                                            ?: "No fue posible guardar el intento."
                                    )
                                }
                            }


                            override fun onFailure(

                                call:
                                    Call<DiabetesIntentoResponse>,

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

            onUnauthorized =
                onUnauthorized,

            onError =
                onError
        )
    }
}
