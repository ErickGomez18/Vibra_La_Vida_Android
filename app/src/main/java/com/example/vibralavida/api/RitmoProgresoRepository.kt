package com.example.vibralavida.api

import com.example.vibralavida.api.modelos.RitmoIntentoRequest
import com.example.vibralavida.api.modelos.RitmoIntentoResponse
import com.example.vibralavida.api.modelos.RitmoProgresoRemoto
import com.example.vibralavida.api.modelos.RitmoProgresoRequest
import com.example.vibralavida.api.modelos.RitmoProgresoResponse
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object RitmoProgresoRepository {

    private fun conToken(
        onToken: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        val usuario = FirebaseAuth.getInstance().currentUser

        if (usuario == null) {
            onError("No existe una sesión activa.")
            return
        }

        usuario.getIdToken(false)
            .addOnSuccessListener { result ->
                val token = result.token

                if (token.isNullOrBlank()) {
                    onError("No fue posible obtener el token.")
                } else {
                    onToken("Bearer $token")
                }
            }
            .addOnFailureListener { error ->
                onError(
                    error.message
                        ?: "No fue posible obtener la sesión."
                )
            }
    }

    fun obtenerProgreso(
        onSuccess: (RitmoProgresoRemoto?) -> Unit,
        onError: (String) -> Unit
    ) {
        conToken(
            onToken = { token ->
                RitmoApiClient.api
                    .obtenerProgreso(token)
                    .enqueue(
                        object : Callback<RitmoProgresoResponse> {
                            override fun onResponse(
                                call: Call<RitmoProgresoResponse>,
                                response: Response<RitmoProgresoResponse>
                            ) {
                                val body = response.body()

                                if (
                                    response.isSuccessful &&
                                    body?.success == true
                                ) {
                                    onSuccess(
                                        if (body.existe) body.progreso
                                        else null
                                    )
                                } else {
                                    onError(
                                        "Error ${response.code()} al obtener el progreso."
                                    )
                                }
                            }

                            override fun onFailure(
                                call: Call<RitmoProgresoResponse>,
                                t: Throwable
                            ) {
                                onError(
                                    t.message
                                        ?: "No fue posible conectarse con la API."
                                )
                            }
                        }
                    )
            },
            onError = onError
        )
    }

    fun guardarProgreso(
        progreso: RitmoProgresoRequest,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        conToken(
            onToken = { token ->
                RitmoApiClient.api
                    .guardarProgreso(token, progreso)
                    .enqueue(
                        object : Callback<RitmoProgresoResponse> {
                            override fun onResponse(
                                call: Call<RitmoProgresoResponse>,
                                response: Response<RitmoProgresoResponse>
                            ) {
                                if (
                                    response.isSuccessful &&
                                    response.body()?.success == true
                                ) {
                                    onSuccess()
                                } else {
                                    onError(
                                        "Error ${response.code()} al guardar el progreso."
                                    )
                                }
                            }

                            override fun onFailure(
                                call: Call<RitmoProgresoResponse>,
                                t: Throwable
                            ) {
                                onError(
                                    t.message
                                        ?: "No fue posible guardar el progreso."
                                )
                            }
                        }
                    )
            },
            onError = onError
        )
    }

    fun guardarIntento(
        intento: RitmoIntentoRequest,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        conToken(
            onToken = { token ->
                RitmoApiClient.api
                    .guardarIntento(token, intento)
                    .enqueue(
                        object : Callback<RitmoIntentoResponse> {
                            override fun onResponse(
                                call: Call<RitmoIntentoResponse>,
                                response: Response<RitmoIntentoResponse>
                            ) {
                                if (
                                    response.isSuccessful &&
                                    response.body()?.success == true
                                ) {
                                    onSuccess()
                                } else {
                                    onError(
                                        "Error ${response.code()} al guardar el intento."
                                    )
                                }
                            }

                            override fun onFailure(
                                call: Call<RitmoIntentoResponse>,
                                t: Throwable
                            ) {
                                onError(
                                    t.message
                                        ?: "No fue posible guardar el intento."
                                )
                            }
                        }
                    )
            },
            onError = onError
        )
    }
}
