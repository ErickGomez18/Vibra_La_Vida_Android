package com.example.vibralavida.api


// ============================================================================
// MODELOS
// ============================================================================

import com.example.vibralavida.api.modelos.HealthConnectSyncRequest
import com.example.vibralavida.api.modelos.HealthConnectSyncResponse


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
// REPOSITORY DE HEALTH CONNECT
// ============================================================================
//
// Flujo:
//
// Health Connect
//      ↓
// HealthConnectScreen
//      ↓
// HealthConnectRepository
//      ↓
// Firebase ID Token
//      ↓
// Express API
//      ↓
// Firestore
//
// ============================================================================

object HealthConnectRepository {


    // ========================================================================
    // SINCRONIZAR
    // ========================================================================

    fun sincronizar(

        datos: HealthConnectSyncRequest,

        onSuccess: (String) -> Unit,

        onUnauthorized: () -> Unit = {},

        onError: (String) -> Unit

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
        // OBTENER FIREBASE ID TOKEN
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
                // POST /api/health-connect/sync
                // ============================================================

                ApiClient
                    .healthConnectApi
                    .sincronizar(

                        authorization =
                            authorization,

                        datos =
                            datos
                    )
                    .enqueue(

                        object :
                            Callback<HealthConnectSyncResponse> {


                            override fun onResponse(

                                call:
                                Call<HealthConnectSyncResponse>,

                                response:
                                Response<HealthConnectSyncResponse>

                            ) {


                                // =============================================
                                // 200 / 201
                                // =============================================

                                if (
                                    response.isSuccessful
                                ) {

                                    val body =
                                        response.body()


                                    if (
                                        body?.success == true
                                    ) {

                                        onSuccess(

                                            body.message.ifBlank {

                                                "Datos sincronizados correctamente."
                                            }
                                        )

                                    } else {

                                        onError(

                                            body?.message
                                                ?: "No fue posible sincronizar los datos."
                                        )
                                    }


                                    return
                                }


                                // =============================================
                                // 401
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

                                    "Error del servidor: ${response.code()}"
                                )
                            }


                            override fun onFailure(

                                call:
                                Call<HealthConnectSyncResponse>,

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

                onUnauthorized()
            }
    }
}
