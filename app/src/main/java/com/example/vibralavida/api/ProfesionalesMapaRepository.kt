package com.example.vibralavida.api

// ============================================================================
// MODELOS
// ============================================================================

import com.example.vibralavida.api.modelos.ProfesionalMapaResponse
import com.example.vibralavida.api.modelos.ProfesionalesMapaResponse

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
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


// ============================================================================
// PROFESIONALES MAPA REPOSITORY
// ============================================================================
//
// Flujo:
//
// Firebase Auth
//      ↓
// ID Token
//      ↓
// GET /api/profesionales/mapa
//      ↓
// Lista de consultorios
//
// ============================================================================

object ProfesionalesMapaRepository {


    // ========================================================================
    // URL DE RENDER
    // ========================================================================

    private const val BASE_URL =
        "https://api-vibra-la-vida.onrender.com/"


    // ========================================================================
    // RETROFIT
    // ========================================================================
    //
    // Lo dejamos independiente para NO modificar tu ApiClient actual.
    //
    // Después, si quieres, podemos centralizar todas las APIs.
    //
    // ========================================================================

    private val api: ProfesionalesMapaApi by lazy {

        Retrofit
            .Builder()
            .baseUrl(
                BASE_URL
            )
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()
            .create(
                ProfesionalesMapaApi::class.java
            )
    }


    // ========================================================================
    // OBTENER PROFESIONALES
    // ========================================================================

    fun obtenerProfesionales(

        onSuccess:
        (List<ProfesionalMapaResponse>) -> Unit,

        onUnauthorized:
        () -> Unit,

        onError:
        (String) -> Unit
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
        // TOKEN DE FIREBASE
        // ====================================================================

        usuarioActual
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

                    onUnauthorized()

                    return@addOnSuccessListener
                }


                val authorization =
                    "Bearer $token"


                // ============================================================
                // CONSULTAR API
                // ============================================================

                api
                    .obtenerProfesionales(
                        authorization =
                            authorization
                    )
                    .enqueue(

                        object :
                            Callback<ProfesionalesMapaResponse> {


                            override fun onResponse(

                                call:
                                Call<ProfesionalesMapaResponse>,

                                response:
                                Response<ProfesionalesMapaResponse>
                            ) {


                                // --------------------------------------------
                                // TOKEN INVÁLIDO
                                // --------------------------------------------

                                if (
                                    response.code() == 401
                                ) {

                                    onUnauthorized()

                                    return
                                }


                                // --------------------------------------------
                                // ERROR HTTP
                                // --------------------------------------------

                                if (
                                    !response.isSuccessful
                                ) {

                                    onError(
                                        "Error del servidor: ${response.code()}"
                                    )

                                    return
                                }


                                // --------------------------------------------
                                // RESPUESTA
                                // --------------------------------------------

                                val body =
                                    response.body()


                                if (
                                    body?.success == true
                                ) {

                                    onSuccess(
                                        body.profesionales
                                    )

                                } else {

                                    onError(
                                        "No fue posible obtener los profesionales."
                                    )
                                }
                            }


                            override fun onFailure(

                                call:
                                Call<ProfesionalesMapaResponse>,

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
}
