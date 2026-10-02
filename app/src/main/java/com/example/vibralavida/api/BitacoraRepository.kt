package com.example.vibralavida.api

import com.example.vibralavida.agenda.bitacora.EstudioLaboratorio
import com.example.vibralavida.agenda.bitacora.RegistroSalud
import com.example.vibralavida.api.modelos.EstudioLaboratorioOperacionResponse
import com.example.vibralavida.api.modelos.EstudioLaboratorioRequest
import com.example.vibralavida.api.modelos.EstudioLaboratorioResponse
import com.example.vibralavida.api.modelos.EstudiosLaboratorioResponse
import com.example.vibralavida.api.modelos.RegistroSaludOperacionResponse
import com.example.vibralavida.api.modelos.RegistroSaludRequest
import com.example.vibralavida.api.modelos.RegistroSaludResponse
import com.example.vibralavida.api.modelos.RegistrosSaludResponse

import com.google.firebase.auth.FirebaseAuth

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


// ============================================================================
// BITÁCORA REPOSITORY
// ============================================================================
//
// Firebase Auth
//      ↓
// ID Token
//      ↓
// Retrofit
//      ↓
// API Express
//      ↓
// Firestore
//
// ============================================================================

object BitacoraRepository {


    // ========================================================================
    // TOKEN
    // ========================================================================

    private fun obtenerToken(

        onSuccess: (String) -> Unit,

        onError: (String) -> Unit

    ) {

        val usuario =
            FirebaseAuth
                .getInstance()
                .currentUser


        if (usuario == null) {

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
    // OBTENER REGISTROS
    // ========================================================================

    fun obtenerRegistros(

        onSuccess: (List<RegistroSalud>) -> Unit,

        onError: (String) -> Unit

    ) {

        obtenerToken(

            onSuccess = {
                    authorization ->


                ApiClient
                    .bitacoraApi
                    .obtenerRegistros(
                        authorization
                    )
                    .enqueue(

                        object :
                            Callback<RegistrosSaludResponse> {


                            override fun onResponse(

                                call:
                                Call<RegistrosSaludResponse>,

                                response:
                                Response<RegistrosSaludResponse>

                            ) {

                                if (response.isSuccessful) {

                                    val body =
                                        response.body()


                                    if (body?.success == true) {

                                        onSuccess(
                                            body.registros
                                        )

                                    } else {

                                        onError(

                                            body?.message
                                                ?: "No fue posible obtener los registros."
                                        )
                                    }

                                } else {

                                    onError(
                                        mensajeHttp(
                                            response.code()
                                        )
                                    )
                                }
                            }


                            override fun onFailure(

                                call:
                                Call<RegistrosSaludResponse>,

                                throwable:
                                Throwable

                            ) {

                                onError(
                                    mensajeConexion(
                                        throwable
                                    )
                                )
                            }
                        }
                    )
            },

            onError = onError
        )
    }


    // ========================================================================
    // CREAR REGISTRO
    // ========================================================================

    fun crearRegistro(

        registro: RegistroSaludRequest,

        onSuccess: (RegistroSalud) -> Unit,

        onError: (String) -> Unit

    ) {

        obtenerToken(

            onSuccess = {
                    authorization ->


                ApiClient
                    .bitacoraApi
                    .crearRegistro(

                        authorization,

                        registro
                    )
                    .enqueue(

                        object :
                            Callback<RegistroSaludResponse> {


                            override fun onResponse(

                                call:
                                Call<RegistroSaludResponse>,

                                response:
                                Response<RegistroSaludResponse>

                            ) {

                                if (response.isSuccessful) {

                                    val body =
                                        response.body()

                                    val creado =
                                        body?.registro


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
                                                ?: "No fue posible guardar el registro."
                                        )
                                    }

                                } else {

                                    onError(
                                        mensajeHttp(
                                            response.code()
                                        )
                                    )
                                }
                            }


                            override fun onFailure(

                                call:
                                Call<RegistroSaludResponse>,

                                throwable:
                                Throwable

                            ) {

                                onError(
                                    mensajeConexion(
                                        throwable
                                    )
                                )
                            }
                        }
                    )
            },

            onError = onError
        )
    }


    // ========================================================================
    // ACTUALIZAR REGISTRO
    // ========================================================================

    fun actualizarRegistro(

        registroId: String,

        registro: RegistroSaludRequest,

        onSuccess: () -> Unit,

        onError: (String) -> Unit

    ) {

        obtenerToken(

            onSuccess = {
                    authorization ->


                ApiClient
                    .bitacoraApi
                    .actualizarRegistro(

                        authorization,

                        registroId,

                        registro
                    )
                    .enqueue(

                        object :
                            Callback<RegistroSaludOperacionResponse> {


                            override fun onResponse(

                                call:
                                Call<RegistroSaludOperacionResponse>,

                                response:
                                Response<RegistroSaludOperacionResponse>

                            ) {

                                if (
                                    response.isSuccessful &&
                                    response.body()?.success == true
                                ) {

                                    onSuccess()

                                } else {

                                    onError(

                                        response.body()?.message
                                            ?: mensajeHttp(
                                                response.code()
                                            )
                                    )
                                }
                            }


                            override fun onFailure(

                                call:
                                Call<RegistroSaludOperacionResponse>,

                                throwable:
                                Throwable

                            ) {

                                onError(
                                    mensajeConexion(
                                        throwable
                                    )
                                )
                            }
                        }
                    )
            },

            onError = onError
        )
    }


    // ========================================================================
    // ELIMINAR REGISTRO
    // ========================================================================

    fun eliminarRegistro(

        registroId: String,

        onSuccess: () -> Unit,

        onError: (String) -> Unit

    ) {

        obtenerToken(

            onSuccess = {
                    authorization ->


                ApiClient
                    .bitacoraApi
                    .eliminarRegistro(

                        authorization,

                        registroId
                    )
                    .enqueue(

                        object :
                            Callback<RegistroSaludOperacionResponse> {


                            override fun onResponse(

                                call:
                                Call<RegistroSaludOperacionResponse>,

                                response:
                                Response<RegistroSaludOperacionResponse>

                            ) {

                                if (
                                    response.isSuccessful &&
                                    response.body()?.success == true
                                ) {

                                    onSuccess()

                                } else {

                                    onError(

                                        response.body()?.message
                                            ?: mensajeHttp(
                                                response.code()
                                            )
                                    )
                                }
                            }


                            override fun onFailure(

                                call:
                                Call<RegistroSaludOperacionResponse>,

                                throwable:
                                Throwable

                            ) {

                                onError(
                                    mensajeConexion(
                                        throwable
                                    )
                                )
                            }
                        }
                    )
            },

            onError = onError
        )
    }


    // ========================================================================
    // OBTENER LABORATORIOS
    // ========================================================================

    fun obtenerLaboratorios(

        onSuccess: (List<EstudioLaboratorio>) -> Unit,

        onError: (String) -> Unit

    ) {

        obtenerToken(

            onSuccess = {
                    authorization ->


                ApiClient
                    .bitacoraApi
                    .obtenerLaboratorios(
                        authorization
                    )
                    .enqueue(

                        object :
                            Callback<EstudiosLaboratorioResponse> {


                            override fun onResponse(

                                call:
                                Call<EstudiosLaboratorioResponse>,

                                response:
                                Response<EstudiosLaboratorioResponse>

                            ) {

                                if (response.isSuccessful) {

                                    val body =
                                        response.body()


                                    if (body?.success == true) {

                                        onSuccess(
                                            body.laboratorios
                                        )

                                    } else {

                                        onError(

                                            body?.message
                                                ?: "No fue posible obtener los laboratorios."
                                        )
                                    }

                                } else {

                                    onError(
                                        mensajeHttp(
                                            response.code()
                                        )
                                    )
                                }
                            }


                            override fun onFailure(

                                call:
                                Call<EstudiosLaboratorioResponse>,

                                throwable:
                                Throwable

                            ) {

                                onError(
                                    mensajeConexion(
                                        throwable
                                    )
                                )
                            }
                        }
                    )
            },

            onError = onError
        )
    }


    // ========================================================================
    // CREAR LABORATORIO
    // ========================================================================

    fun crearLaboratorio(

        estudio: EstudioLaboratorioRequest,

        onSuccess: (EstudioLaboratorio) -> Unit,

        onError: (String) -> Unit

    ) {

        obtenerToken(

            onSuccess = {
                    authorization ->


                ApiClient
                    .bitacoraApi
                    .crearLaboratorio(

                        authorization,

                        estudio
                    )
                    .enqueue(

                        object :
                            Callback<EstudioLaboratorioResponse> {


                            override fun onResponse(

                                call:
                                Call<EstudioLaboratorioResponse>,

                                response:
                                Response<EstudioLaboratorioResponse>

                            ) {

                                if (response.isSuccessful) {

                                    val body =
                                        response.body()

                                    val creado =
                                        body?.estudio


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
                                                ?: "No fue posible guardar el estudio."
                                        )
                                    }

                                } else {

                                    onError(
                                        mensajeHttp(
                                            response.code()
                                        )
                                    )
                                }
                            }


                            override fun onFailure(

                                call:
                                Call<EstudioLaboratorioResponse>,

                                throwable:
                                Throwable

                            ) {

                                onError(
                                    mensajeConexion(
                                        throwable
                                    )
                                )
                            }
                        }
                    )
            },

            onError = onError
        )
    }


    // ========================================================================
    // ACTUALIZAR LABORATORIO
    // ========================================================================

    fun actualizarLaboratorio(

        estudioId: String,

        estudio: EstudioLaboratorioRequest,

        onSuccess: () -> Unit,

        onError: (String) -> Unit

    ) {

        obtenerToken(

            onSuccess = {
                    authorization ->


                ApiClient
                    .bitacoraApi
                    .actualizarLaboratorio(

                        authorization,

                        estudioId,

                        estudio
                    )
                    .enqueue(

                        object :
                            Callback<EstudioLaboratorioOperacionResponse> {


                            override fun onResponse(

                                call:
                                Call<EstudioLaboratorioOperacionResponse>,

                                response:
                                Response<EstudioLaboratorioOperacionResponse>

                            ) {

                                if (
                                    response.isSuccessful &&
                                    response.body()?.success == true
                                ) {

                                    onSuccess()

                                } else {

                                    onError(

                                        response.body()?.message
                                            ?: mensajeHttp(
                                                response.code()
                                            )
                                    )
                                }
                            }


                            override fun onFailure(

                                call:
                                Call<EstudioLaboratorioOperacionResponse>,

                                throwable:
                                Throwable

                            ) {

                                onError(
                                    mensajeConexion(
                                        throwable
                                    )
                                )
                            }
                        }
                    )
            },

            onError = onError
        )
    }


    // ========================================================================
    // ELIMINAR LABORATORIO
    // ========================================================================

    fun eliminarLaboratorio(

        estudioId: String,

        onSuccess: () -> Unit,

        onError: (String) -> Unit

    ) {

        obtenerToken(

            onSuccess = {
                    authorization ->


                ApiClient
                    .bitacoraApi
                    .eliminarLaboratorio(

                        authorization,

                        estudioId
                    )
                    .enqueue(

                        object :
                            Callback<EstudioLaboratorioOperacionResponse> {


                            override fun onResponse(

                                call:
                                Call<EstudioLaboratorioOperacionResponse>,

                                response:
                                Response<EstudioLaboratorioOperacionResponse>

                            ) {

                                if (
                                    response.isSuccessful &&
                                    response.body()?.success == true
                                ) {

                                    onSuccess()

                                } else {

                                    onError(

                                        response.body()?.message
                                            ?: mensajeHttp(
                                                response.code()
                                            )
                                    )
                                }
                            }


                            override fun onFailure(

                                call:
                                Call<EstudioLaboratorioOperacionResponse>,

                                throwable:
                                Throwable

                            ) {

                                onError(
                                    mensajeConexion(
                                        throwable
                                    )
                                )
                            }
                        }
                    )
            },

            onError = onError
        )
    }


    // ========================================================================
    // MENSAJES AUXILIARES
    // ========================================================================

    private fun mensajeHttp(
        codigo: Int
    ): String {

        return when (codigo) {

            400 ->
                "Los datos enviados no son válidos."

            401 ->
                "La sesión no es válida."

            404 ->
                "El registro solicitado no existe."

            else ->
                "Error del servidor: $codigo"
        }
    }


    private fun mensajeConexion(
        throwable: Throwable
    ): String {

        return throwable.message
            ?: "No fue posible conectarse con la API."
    }
}
