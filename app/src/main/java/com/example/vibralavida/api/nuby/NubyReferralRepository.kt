package com.example.vibralavida.api.nuby

import com.google.firebase.auth.FirebaseAuth

import kotlinx.coroutines.suspendCancellableCoroutine

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import kotlin.coroutines.resume


// ============================================================================
// REPOSITORY DE ESPECIALISTAS PARA NUBY
// ============================================================================

object NubyReferralRepository {


    // ========================================================================
    // VERSIÓN CALLBACK
    // ========================================================================

    fun buscarEspecialistas(

        especialidad: String,

        limit: Int = 3,

        onSuccess: (List<EspecialistaNuby>) -> Unit,

        onError: (String) -> Unit

    ) {


        val user =
            FirebaseAuth
                .getInstance()
                .currentUser


        if (
            user == null
        ) {

            onError(
                "No existe una sesión activa."
            )

            return
        }


        user
            .getIdToken(
                false
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


                NubyReferralApiClient
                    .api
                    .obtenerEspecialistas(

                        authorization =
                            "Bearer $token",

                        especialidad =
                            especialidad,

                        limit =
                            limit
                    )
                    .enqueue(

                        object :
                            Callback<EspecialistasNubyResponse> {


                            override fun onResponse(

                                call:
                                Call<EspecialistasNubyResponse>,

                                response:
                                Response<EspecialistasNubyResponse>

                            ) {


                                val body =
                                    response.body()


                                if (
                                    response.isSuccessful &&
                                    body?.success == true
                                ) {

                                    onSuccess(
                                        body.especialistas
                                    )

                                } else {

                                    onError(
                                        body?.message
                                            ?: "No fue posible consultar especialistas."
                                    )
                                }
                            }


                            override fun onFailure(

                                call:
                                Call<EspecialistasNubyResponse>,

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


    // ========================================================================
    // VERSIÓN SUSPEND
    // ========================================================================
    //
    // NubyChatRepository trabaja con funciones suspend.
    // Esta función transforma el callback de Retrofit en un resultado simple.
    //
    // Si la API falla, devolvemos una lista vacía:
    // Nuby puede seguir orientando sin romper el chat.
    //
    // ========================================================================

    suspend fun buscarEspecialistasSuspend(

        especialidad: String,

        limit: Int = 3

    ): List<EspecialistaNuby> {


        return suspendCancellableCoroutine {
                continuation ->


            buscarEspecialistas(

                especialidad =
                    especialidad,

                limit =
                    limit,

                onSuccess = {
                        especialistas ->


                    if (
                        continuation.isActive
                    ) {

                        continuation.resume(
                            especialistas
                        )
                    }
                },

                onError = {


                    if (
                        continuation.isActive
                    ) {

                        continuation.resume(
                            emptyList()
                        )
                    }
                }
            )
        }
    }
}
