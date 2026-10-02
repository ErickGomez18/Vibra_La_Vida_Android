package com.example.vibralavida.api.nuby

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NubyReferralApiClient {

    private const val BASE_URL =
        "https://api-vibra-la-vida.onrender.com/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(
                BASE_URL
            )
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()
    }

    val api: NubyReferralApi by lazy {
        retrofit.create(
            NubyReferralApi::class.java
        )
    }
}
