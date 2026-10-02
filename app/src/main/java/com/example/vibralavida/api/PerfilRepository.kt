package com.example.vibralavida.api


// ============================================================================
// MODELOS
// ============================================================================

import com.example.vibralavida.api.modelos.ActualizarPerfilResponse
import com.example.vibralavida.api.modelos.PerfilRequest
import com.example.vibralavida.api.modelos.PerfilResponse


// ============================================================================
// FIREBASE
// ============================================================================

import com.google.firebase.auth.FirebaseAuth


// ============================================================================
// RETROFIT
// ============================================================================

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


// ============================================================================
// JSON
// ============================================================================

import org.json.JSONObject


// ============================================================================
// PERFIL REPOSITORY
// ============================================================================
//
// Se encarga de:
//
// Firebase Auth
//       ↓
// obtener ID Token
//       ↓
// Retrofit
//       ↓
// API Express
//
// ============================================================================

object PerfilRepository {


    // ========================================================================
    // GUARDAR PERFIL
    // ========================================================================
    //
    // PUT /api/users/me
    //
    // ========================================================================

    fun guardarPerfil(

        perfil: PerfilRequest,

        onSuccess: () -> Unit,

        onError: (String) -> Unit

    ) {


        // ====================================================================
        // USUARIO ACTUAL
        // ====================================================================

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


        // ====================================================================
        // TOKEN
        // ====================================================================

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


                val authorization =
                    "Bearer $token"


                // ============================================================
                // PUT
                // ============================================================

                ApiClient
                    .perfilApi
                    .actualizarPerfil(

                        authorization =
                            authorization,

                        perfil =
                            perfil
                    )
                    .enqueue(

                        object :
                            Callback<ActualizarPerfilResponse> {


                            override fun onResponse(

                                call:
                                Call<ActualizarPerfilResponse>,

                                response:
                                Response<ActualizarPerfilResponse>

                            ) {


                                if (response.isSuccessful) {

                                    val body =
                                        response.body()


                                    if (body?.success == true) {

                                        onSuccess()

                                    } else {

                                        onError(

                                            body?.message
                                                ?: "No fue posible guardar el perfil."
                                        )
                                    }

                                } else {

                                    onError(

                                        "Error del servidor: ${response.code()}"
                                    )
                                }
                            }


                            override fun onFailure(

                                call:
                                Call<ActualizarPerfilResponse>,

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
                    exception ->


                onError(

                    exception.message
                        ?: "No fue posible obtener la sesión."
                )
            }
    }


    // ========================================================================
    // ACTUALIZAR FOTO DE PERFIL
    // ========================================================================
    //
    // No vuelve a enviar edad, peso, etc.
    //
    // El backend usa actualización parcial con merge y conserva
    // todos los demás datos del perfil.
    //
    // ========================================================================

    fun actualizarFotoPerfil(

        fotoPerfilUrl: String,

        onSuccess: () -> Unit,

        onError: (String) -> Unit

    ) {


        if (
            fotoPerfilUrl.isBlank()
        ) {

            onError(
                "La URL de la foto está vacía."
            )

            return
        }


        guardarPerfil(

            perfil =
                PerfilRequest(

                    fotoPerfilUrl =
                        fotoPerfilUrl
                ),

            onSuccess =
                onSuccess,

            onError =
                onError
        )
    }


    // ========================================================================
    // OBTENER PERFIL
    // ========================================================================
    //
    // GET /api/users/me
    //
    // onSuccess:
    // existe documento usuarios/{uid}
    //
    // onProfileNotFound:
    // Firebase Auth existe, pero Firestore todavía no tiene perfil
    //
    // onUnauthorized:
    // token inválido / sesión no válida
    //
    // ========================================================================

    fun obtenerPerfil(

        onSuccess:
            (PerfilResponse) -> Unit,

        onProfileNotFound:
            () -> Unit,

        onUnauthorized:
            () -> Unit,

        onError:
            (String) -> Unit

    ) {


        // ====================================================================
        // USUARIO AUTENTICADO
        // ====================================================================

        val usuarioActual =
            FirebaseAuth
                .getInstance()
                .currentUser


        if (usuarioActual == null) {

            onUnauthorized()

            return
        }


        // ====================================================================
        // TOKEN ACTUAL
        // ====================================================================

        usuarioActual
            .getIdToken(false)
            .addOnSuccessListener {
                    tokenResult ->


                val token =
                    tokenResult.token


                if (token.isNullOrBlank()) {

                    onUnauthorized()

                    return@addOnSuccessListener
                }


                val authorization =
                    "Bearer $token"


                // ============================================================
                // GET /api/users/me
                // ============================================================

                ApiClient
                    .perfilApi
                    .obtenerPerfil(

                        authorization =
                            authorization
                    )
                    .enqueue(

                        object :
                            Callback<PerfilResponse> {


                            override fun onResponse(

                                call:
                                Call<PerfilResponse>,

                                response:
                                Response<PerfilResponse>

                            ) {


                                // =============================================
                                // 200
                                // =============================================

                                if (response.isSuccessful) {

                                    val body =
                                        response.body()


                                    if (
                                        body?.success == true &&
                                        body.user != null
                                    ) {

                                        onSuccess(
                                            body
                                        )

                                    } else {

                                        onError(
                                            body?.message
                                                ?: "No fue posible obtener el perfil."
                                        )
                                    }


                                    return
                                }


                                // =============================================
                                // 404
                                // =============================================
                                //
                                // Existe Auth, pero aún no existe documento
                                // del perfil en Firestore.
                                //
                                // =============================================

                                if (response.code() == 404) {

                                    onProfileNotFound()

                                    return
                                }


                                // =============================================
                                // 401
                                // =============================================

                                if (response.code() == 401) {

                                    onUnauthorized()

                                    return
                                }


                                // =============================================
                                // OTROS
                                // =============================================

                                onError(

                                    "Error del servidor: ${response.code()}"
                                )
                            }


                            // ================================================
                            // NO SE PUDO CONECTAR
                            // ================================================

                            override fun onFailure(

                                call:
                                Call<PerfilResponse>,

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


                // Si Firebase ya no puede obtener
                // un token válido, tratamos la sesión
                // como no autorizada.

                onUnauthorized()
            }
    }

    // ============================================================================
// ELIMINAR CUENTA
// ============================================================================
//
// DELETE /api/users/me
//
// La API elimina:
//
// usuarios/{uid}
// subcolecciones del usuario
// Firebase Authentication
//
// ============================================================================

    fun eliminarCuenta(

        onSuccess: () -> Unit,

        // Debe declararse antes de los callbacks por defecto
        // que lo utilizan.
        onError: (String) -> Unit,

        onMultiRoleAccount: (String) -> Unit = { mensaje ->

            onError(
                mensaje
            )
        },

        onUnauthorized: () -> Unit = {

            onError(
                "La sesión no es válida."
            )
        }

    ) {


        // ====================================================================
        // USUARIO ACTUAL
        // ====================================================================

        val usuarioActual =
            FirebaseAuth
                .getInstance()
                .currentUser


        if (
            usuarioActual == null
        ) {

            onUnauthorized()

            return
        }


        // ====================================================================
        // OBTENER TOKEN
        // ====================================================================

        usuarioActual
            .getIdToken(false)
            .addOnSuccessListener {
                    tokenResult ->


                val token =
                    tokenResult.token


                if (
                    token.isNullOrBlank()
                ) {

                    onUnauthorized()

                    return@addOnSuccessListener
                }


                val authorization =
                    "Bearer $token"


                // ============================================================
                // DELETE /api/users/me
                // ============================================================

                ApiClient
                    .perfilApi
                    .eliminarCuenta(

                        authorization =
                            authorization
                    )
                    .enqueue(

                        object :
                            Callback<ActualizarPerfilResponse> {


                            override fun onResponse(

                                call:
                                Call<ActualizarPerfilResponse>,

                                response:
                                Response<ActualizarPerfilResponse>

                            ) {


                                // =============================================
                                // 200 - CUENTA ELIMINADA
                                // =============================================

                                if (
                                    response.isSuccessful
                                ) {

                                    val body =
                                        response.body()


                                    if (
                                        body?.success == true
                                    ) {

                                        onSuccess()

                                    } else {

                                        onError(

                                            body?.message
                                                ?: "No fue posible eliminar la cuenta."
                                        )
                                    }


                                    return
                                }


                                // =============================================
                                // LEER RESPUESTA DE ERROR DEL BACKEND
                                // =============================================
                                //
                                // Ejemplo:
                                //
                                // {
                                //   "success": false,
                                //   "code": "MULTI_ROLE_ACCOUNT",
                                //   "message": "Esta cuenta también tiene..."
                                // }
                                //
                                // =============================================

                                val errorTexto =
                                    try {

                                        response
                                            .errorBody()
                                            ?.string()
                                            .orEmpty()

                                    } catch (
                                        e: Exception
                                    ) {

                                        ""
                                    }


                                var codigoBackend =
                                    ""


                                var mensajeBackend =
                                    ""


                                if (
                                    errorTexto.isNotBlank()
                                ) {

                                    try {

                                        val json =
                                            JSONObject(
                                                errorTexto
                                            )


                                        codigoBackend =
                                            json.optString(
                                                "code"
                                            )


                                        mensajeBackend =
                                            json.optString(
                                                "message"
                                            )

                                    } catch (
                                        e: Exception
                                    ) {

                                        // Si el backend devolviera algo que no
                                        // fuera JSON, seguimos usando el código
                                        // HTTP como respaldo.
                                    }
                                }


                                // =============================================
                                // 409 - CUENTA MULTIRROL
                                // =============================================
                                //
                                // IMPORTANTE:
                                //
                                // NO cerramos sesión.
                                // NO limpiamos datos locales.
                                // NO borramos Firebase Auth.
                                //
                                // El backend ya detuvo la eliminación.
                                //
                                // =============================================

                                if (
                                    response.code() == 409 &&
                                    codigoBackend == "MULTI_ROLE_ACCOUNT"
                                ) {

                                    onMultiRoleAccount(

                                        mensajeBackend.ifBlank {

                                            "Esta cuenta también está vinculada a un perfil profesional. Para evitar eliminar tu acceso como especialista, no puede eliminarse completamente desde la app."
                                        }
                                    )


                                    return
                                }


                                // =============================================
                                // 401 - SESIÓN INVÁLIDA
                                // =============================================

                                if (
                                    response.code() == 401
                                ) {

                                    onUnauthorized()

                                    return
                                }


                                // =============================================
                                // OTROS ERRORES
                                // =============================================

                                onError(

                                    mensajeBackend.ifBlank {

                                        "Error del servidor: ${response.code()}"
                                    }
                                )
                            }


                            // ================================================
                            // ERROR DE CONEXIÓN
                            // ================================================

                            override fun onFailure(

                                call:
                                Call<ActualizarPerfilResponse>,

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