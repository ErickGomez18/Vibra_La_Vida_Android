package com.example.vibralavida.api

import com.example.vibralavida.api.modelos.ResultadoRequest
import com.example.vibralavida.api.modelos.ResultadoResponse
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


// ============================================================================
// REPOSITORIO DE RESULTADOS
// ============================================================================
//
// Responsabilidades:
//
// 1. Comprobar que existe una sesión de Firebase.
// 2. Obtener el ID Token del usuario autenticado.
// 3. Enviar el resultado a POST /api/results.
// 4. Informar a MainActivity si se guardó o si ocurrió un error.
//
// El UID NO se manda manualmente.
// El backend lo obtiene del token de Firebase.
//
// ============================================================================

object ResultsRepository {


    // ========================================================================
    // GUARDAR RESULTADO
    // ========================================================================

    fun guardarResultado(

        resultado: ResultadoRequest,

        onSuccess: (resultId: String?) -> Unit,

        onUnauthorized: () -> Unit = {},

        onError: (String) -> Unit

    ) {

        // --------------------------------------------------------------------
        // USUARIO ACTUAL
        // --------------------------------------------------------------------

        val usuario =
            FirebaseAuth
                .getInstance()
                .currentUser


        if (usuario == null) {

            onUnauthorized()
            return
        }


        // --------------------------------------------------------------------
        // TOKEN DE FIREBASE
        // --------------------------------------------------------------------

        usuario
            .getIdToken(false)
            .addOnSuccessListener {
                    tokenResult ->


                val token =
                    tokenResult.token


                if (token.isNullOrBlank()) {

                    onUnauthorized()
                    return@addOnSuccessListener
                }


                // ------------------------------------------------------------
                // LLAMADA A LA API
                // ------------------------------------------------------------

                ApiClient
                    .resultsApi
                    .guardarResultado(

                        authorization =
                            "Bearer $token",

                        resultado =
                            resultado
                    )
                    .enqueue(

                        object : Callback<ResultadoResponse> {


                            // ==================================================
                            // RESPUESTA DEL SERVIDOR
                            // ==================================================

                            override fun onResponse(

                                call: Call<ResultadoResponse>,

                                response: Response<ResultadoResponse>

                            ) {

                                // Sesión inválida o vencida.
                                if (
                                    response.code() == 401 ||
                                    response.code() == 403
                                ) {

                                    onUnauthorized()
                                    return
                                }


                                val body =
                                    response.body()


                                if (
                                    response.isSuccessful &&
                                    body?.success == true
                                ) {

                                    onSuccess(
                                        body.resultId
                                    )

                                    return
                                }


                                val mensaje =
                                    body?.message
                                        ?.takeIf {
                                            it.isNotBlank()
                                        }
                                        ?: "No fue posible guardar el resultado."


                                onError(
                                    mensaje
                                )
                            }


                            // ==================================================
                            // ERROR DE RED
                            // ==================================================

                            override fun onFailure(

                                call: Call<ResultadoResponse>,

                                throwable: Throwable

                            ) {

                                onError(
                                    throwable.message
                                        ?: "No fue posible conectar con el servidor."
                                )
                            }
                        }
                    )
            }
            .addOnFailureListener {
                    error ->


                onError(
                    error.message
                        ?: "No fue posible obtener la sesión de Firebase."
                )
            }
    }
}
