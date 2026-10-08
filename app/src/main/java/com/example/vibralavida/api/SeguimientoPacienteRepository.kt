package com.example.vibralavida.api

import com.example.vibralavida.api.modelos.AutorizarSeguimientoRequest
import com.example.vibralavida.api.modelos.CodigoSeguimientoResponse
import com.example.vibralavida.api.modelos.MisVinculosSeguimientoResponse
import com.example.vibralavida.api.modelos.OperacionSeguimientoResponse

import com.google.firebase.auth.FirebaseAuth

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


// ============================================================================
// SEGUIMIENTO PACIENTE REPOSITORY
// ============================================================================
//
// Todas las llamadas:
// 1. obtienen el token Firebase del usuario actual;
// 2. llaman a Express;
// 3. Express decide si la solicitud realmente pertenece al paciente.
//
// El doctor nunca recibe el OTP.
// ============================================================================

object SeguimientoPacienteRepository {


    // ========================================================================
    // TOKEN FIREBASE
    // ========================================================================

    private fun conToken(

        onToken: (String) -> Unit,

        onUnauthorized: (() -> Unit)? = null,

        onError: (String) -> Unit

    ) {

        val usuario =
            FirebaseAuth
                .getInstance()
                .currentUser


        if (usuario == null) {

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


                if (token.isNullOrBlank()) {

                    onError(
                        "No fue posible obtener el token de sesión."
                    )

                    return@addOnSuccessListener
                }


                onToken(
                    token
                )
            }
            .addOnFailureListener {
                    error ->

                onError(
                    error.message
                        ?: "No fue posible obtener tu sesión."
                )
            }
    }


    // ========================================================================
    // OBTENER SOLICITUDES Y EQUIPO DE SALUD
    // ========================================================================

    fun obtenerMisVinculos(

        onSuccess: (
            MisVinculosSeguimientoResponse
        ) -> Unit,

        onUnauthorized: (() -> Unit)? = null,

        onError: (String) -> Unit

    ) {

        conToken(

            onToken = {
                    token ->

                ApiClient
                    .seguimientoPacienteApi
                    .obtenerMisVinculos(
                        authorization =
                            "Bearer $token"
                    )
                    .enqueue(

                        object :
                            Callback<MisVinculosSeguimientoResponse> {


                            override fun onResponse(

                                call:
                                Call<MisVinculosSeguimientoResponse>,

                                response:
                                Response<MisVinculosSeguimientoResponse>

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
                                    body?.success == true
                                ) {

                                    onSuccess(
                                        body
                                    )

                                } else {

                                    onError(
                                        body?.message
                                            ?: "No fue posible cargar tu equipo de salud."
                                    )
                                }
                            }


                            override fun onFailure(

                                call:
                                Call<MisVinculosSeguimientoResponse>,

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


    // ========================================================================
    // OBTENER CÓDIGO
    // ========================================================================

    fun obtenerCodigo(

        seguimientoId: String,

        onSuccess: (
            CodigoSeguimientoResponse
        ) -> Unit,

        onUnauthorized: (() -> Unit)? = null,

        onError: (String) -> Unit

    ) {

        conToken(

            onToken = {
                    token ->

                ApiClient
                    .seguimientoPacienteApi
                    .obtenerCodigo(

                        authorization =
                            "Bearer $token",

                        seguimientoId =
                            seguimientoId
                    )
                    .enqueue(

                        object :
                            Callback<CodigoSeguimientoResponse> {


                            override fun onResponse(

                                call:
                                Call<CodigoSeguimientoResponse>,

                                response:
                                Response<CodigoSeguimientoResponse>

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
                                    !body.codigo.isNullOrBlank()
                                ) {

                                    onSuccess(
                                        body
                                    )

                                } else {

                                    onError(
                                        body?.message
                                            ?: "No fue posible obtener el código."
                                    )
                                }
                            }


                            override fun onFailure(

                                call:
                                Call<CodigoSeguimientoResponse>,

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


    // ========================================================================
    // AUTORIZAR SEGUIMIENTO
    // ========================================================================

    fun autorizar(

        seguimientoId: String,

        codigo: String,

        onSuccess: (
            OperacionSeguimientoResponse
        ) -> Unit,

        onUnauthorized: (() -> Unit)? = null,

        onError: (String) -> Unit

    ) {

        conToken(

            onToken = {
                    token ->

                ApiClient
                    .seguimientoPacienteApi
                    .autorizar(

                        authorization =
                            "Bearer $token",

                        seguimientoId =
                            seguimientoId,

                        request =
                            AutorizarSeguimientoRequest(
                                codigo =
                                    codigo
                            )
                    )
                    .enqueue(

                        object :
                            Callback<OperacionSeguimientoResponse> {


                            override fun onResponse(

                                call:
                                Call<OperacionSeguimientoResponse>,

                                response:
                                Response<OperacionSeguimientoResponse>

                            ) {

                                if (
                                    response.code() == 401 &&
                                    response.body() == null
                                ) {

                                    // Un 401 también puede significar OTP incorrecto.
                                    // En ese caso Express envía un mensaje JSON y
                                    // NO debemos cerrar la sesión Firebase.
                                    onError(
                                        "No fue posible validar la solicitud."
                                    )

                                    return
                                }


                                val body =
                                    response.body()


                                if (
                                    response.isSuccessful &&
                                    body?.success == true
                                ) {

                                    onSuccess(
                                        body
                                    )

                                } else {

                                    onError(
                                        body?.message
                                            ?: "No fue posible autorizar el seguimiento."
                                    )
                                }
                            }


                            override fun onFailure(

                                call:
                                Call<OperacionSeguimientoResponse>,

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


    // ========================================================================
    // RECHAZAR
    // ========================================================================

    fun rechazar(

        seguimientoId: String,

        onSuccess: (
            OperacionSeguimientoResponse
        ) -> Unit,

        onUnauthorized: (() -> Unit)? = null,

        onError: (String) -> Unit

    ) {

        conToken(

            onToken = {
                    token ->

                ApiClient
                    .seguimientoPacienteApi
                    .rechazar(

                        authorization =
                            "Bearer $token",

                        seguimientoId =
                            seguimientoId
                    )
                    .enqueue(

                        object :
                            Callback<OperacionSeguimientoResponse> {


                            override fun onResponse(

                                call:
                                Call<OperacionSeguimientoResponse>,

                                response:
                                Response<OperacionSeguimientoResponse>

                            ) {

                                val body =
                                    response.body()


                                if (
                                    response.isSuccessful &&
                                    body?.success == true
                                ) {

                                    onSuccess(
                                        body
                                    )

                                } else {

                                    onError(
                                        body?.message
                                            ?: "No fue posible rechazar la solicitud."
                                    )
                                }
                            }


                            override fun onFailure(

                                call:
                                Call<OperacionSeguimientoResponse>,

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
}
