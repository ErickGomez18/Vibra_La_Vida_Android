package com.example.vibralavida.api

import com.example.vibralavida.agenda.citas.Cita
import com.example.vibralavida.api.modelos.RespuestaPacienteCitaResponse

import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import org.json.JSONObject


// ============================================================================
// CITAS PACIENTE REPOSITORY
// ============================================================================
//
// Este repositorio usa los mismos endpoints que la web.
//
// De esta manera:
// - Web
// - Android
//
// producen exactamente los mismos estados y campos en Firestore.
// ============================================================================

object CitasPacienteRepository {


    // ========================================================================
    // RESPUESTA GENÉRICA
    // ========================================================================

    private fun ejecutarLlamada(

        llamada:
            Call<RespuestaPacienteCitaResponse>,

        onSuccess:
            (
                Cita,
                String
            ) -> Unit,

        onUnauthorized:
            (() -> Unit)? = null,

        onError:
            (String) -> Unit

    ) {

        llamada.enqueue(

            object :
                Callback<RespuestaPacienteCitaResponse> {


                override fun onResponse(

                    call:
                        Call<RespuestaPacienteCitaResponse>,

                    response:
                        Response<RespuestaPacienteCitaResponse>

                ) {

                    val body =
                        response.body()


                    // ------------------------------------------------------------
                    // ÉXITO
                    // ------------------------------------------------------------

                    if (
                        response.isSuccessful &&
                        body?.success == true &&
                        body.cita != null
                    ) {

                        onSuccess(

                            body.cita,

                            body.message
                                ?: "Cita actualizada correctamente."
                        )

                        return
                    }


                    // ------------------------------------------------------------
                    // ERROR HTTP
                    // ------------------------------------------------------------
                    //
                    // Retrofit NO coloca el JSON de un 4xx/5xx en body().
                    // Ese contenido viene en errorBody().
                    //
                    // Antes se perdía el mensaje real del backend y Android
                    // solamente mostraba:
                    //
                    // "No fue posible actualizar la cita."
                    //
                    // ------------------------------------------------------------

                    val mensajeError =
                        try {

                            val textoError =
                                response
                                    .errorBody()
                                    ?.string()
                                    .orEmpty()


                            if (
                                textoError.isNotBlank()
                            ) {

                                val json =
                                    JSONObject(
                                        textoError
                                    )


                                json.optString(
                                    "message"
                                )
                                    .takeIf {
                                        it.isNotBlank()
                                    }
                                    ?: json.optString(
                                        "mensaje"
                                    )
                                        .takeIf {
                                            it.isNotBlank()
                                        }
                                    ?: "Error ${response.code()} al actualizar la cita."

                            } else {

                                body?.message
                                    ?: "Error ${response.code()} al actualizar la cita."
                            }

                        } catch (
                            error: Exception
                        ) {

                            "Error ${response.code()} al actualizar la cita."
                        }


                    // Un 401 por autenticación reciente NO significa
                    // necesariamente que la sesión de Firebase haya caducado.
                    // Por eso mostramos primero el mensaje real del backend.

                    if (
                        response.code() == 401
                    ) {

                        if (
                            mensajeError.contains(
                                "sesión",
                                ignoreCase = true
                            ) ||
                            mensajeError.contains(
                                "token",
                                ignoreCase = true
                            )
                        ) {

                            onUnauthorized?.invoke()

                        } else {

                            onError(
                                mensajeError
                            )
                        }

                        return
                    }


                    onError(
                        mensajeError
                    )
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


    // ========================================================================
    // OBTENER TOKEN FIREBASE
    // ========================================================================

    private fun conToken(

        forzarNuevo:
            Boolean = false,

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
                forzarNuevo
            )
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
    // CONFIRMAR CITA CON CONTRASEÑA
    // ========================================================================
    //
    // La contraseña:
    // - se valida directamente con Firebase;
    // - no se envía a Express;
    // - no se guarda.
    //
    // Después pedimos un token nuevo para que Express pueda comprobar
    // que la autenticación fue reciente.
    // ========================================================================

    fun confirmarCita(

        citaId:
            String,

        contrasena:
            String,

        onSuccess:
            (
                Cita,
                String
            ) -> Unit,

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


        val correo =
            usuario.email


        if (
            correo.isNullOrBlank()
        ) {

            onError(
                "Tu cuenta no tiene un correo disponible para confirmar tu identidad."
            )

            return
        }


        if (
            contrasena.length < 6
        ) {

            onError(
                "Escribe tu contraseña actual."
            )

            return
        }


        val credencial =
            EmailAuthProvider
                .getCredential(
                    correo,
                    contrasena
                )


        usuario
            .reauthenticate(
                credencial
            )
            .addOnSuccessListener {

                conToken(

                    forzarNuevo =
                        true,

                    onToken = {
                            token ->

                        ejecutarLlamada(

                            llamada =
                                ApiClient
                                    .citasPacienteApi
                                    .confirmarCita(

                                        authorization =
                                            "Bearer $token",

                                        citaId =
                                            citaId
                                    ),

                            onSuccess =
                                onSuccess,

                            onUnauthorized =
                                onUnauthorized,

                            onError =
                                onError
                        )
                    },

                    onUnauthorized =
                        onUnauthorized,

                    onError =
                        onError
                )
            }
            .addOnFailureListener {
                    error ->

                val mensaje =
                    when (
                        error.message
                            ?.lowercase()
                    ) {

                        else ->
                            if (
                                error.javaClass
                                    .simpleName
                                    .contains(
                                        "InvalidCredentials",
                                        ignoreCase = true
                                    )
                            ) {
                                "La contraseña es incorrecta."
                            } else {
                                "No fue posible confirmar tu contraseña."
                            }
                    }


                onError(
                    mensaje
                )
            }
    }


    // ========================================================================
    // SOLICITAR REAGENDA
    // ========================================================================

    fun solicitarReagenda(

        citaId:
            String,

        motivo:
            String,

        fechaSolicitada:
            String = "",

        horaSolicitada:
            String = "",

        onSuccess:
            (
                Cita,
                String
            ) -> Unit,

        onUnauthorized:
            (() -> Unit)? = null,

        onError:
            (String) -> Unit

    ) {

        if (
            motivo.trim().length < 5
        ) {

            onError(
                "Escribe brevemente el motivo de la reagenda."
            )

            return
        }


        conToken(

            onToken = {
                    token ->

                ejecutarLlamada(

                    llamada =
                        ApiClient
                            .citasPacienteApi
                            .solicitarReagenda(

                                authorization =
                                    "Bearer $token",

                                citaId =
                                    citaId,

                                body =
                                    mapOf(

                                        "motivoReagenda" to
                                            motivo.trim(),

                                        "fechaSolicitada" to
                                            fechaSolicitada.trim(),

                                        "horaSolicitada" to
                                            horaSolicitada.trim()
                                    )
                            ),

                    onSuccess =
                        onSuccess,

                    onUnauthorized =
                        onUnauthorized,

                    onError =
                        onError
                )
            },

            onUnauthorized =
                onUnauthorized,

            onError =
                onError
        )
    }


    // ========================================================================
    // CANCELAR CITA
    // ========================================================================

    fun cancelarCita(

        citaId:
            String,

        motivo:
            String,

        onSuccess:
            (
                Cita,
                String
            ) -> Unit,

        onUnauthorized:
            (() -> Unit)? = null,

        onError:
            (String) -> Unit

    ) {

        conToken(

            onToken = {
                    token ->

                ejecutarLlamada(

                    llamada =
                        ApiClient
                            .citasPacienteApi
                            .cancelarCita(

                                authorization =
                                    "Bearer $token",

                                citaId =
                                    citaId,

                                body =
                                    mapOf(
                                        "motivoCancelacion" to
                                            motivo.trim()
                                    )
                            ),

                    onSuccess =
                        onSuccess,

                    onUnauthorized =
                        onUnauthorized,

                    onError =
                        onError
                )
            },

            onUnauthorized =
                onUnauthorized,

            onError =
                onError
        )
    }
}
