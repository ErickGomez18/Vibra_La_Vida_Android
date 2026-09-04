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
}