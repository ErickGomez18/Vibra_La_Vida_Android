package com.example.vibralavida.api.nuby

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface NubyReferralApi {

    @GET("api/nuby/especialistas")
    fun obtenerEspecialistas(
        @Header("Authorization")
        authorization: String,
        @Query("especialidad")
        especialidad: String,
        @Query("limit")
        limit: Int = 3
    ): Call<EspecialistasNubyResponse>
}
