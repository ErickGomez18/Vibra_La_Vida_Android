package com.example.vibralavida.api

import com.example.vibralavida.api.modelos.CloudinaryUploadResponse
import com.example.vibralavida.api.modelos.UploadSignatureResponse

import okhttp3.MultipartBody
import okhttp3.RequestBody

import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query
import retrofit2.http.Url


// ============================================================================
// UPLOADS API
// ============================================================================
//
// 1. Pide a Express una firma segura.
// 2. Sube el archivo directamente a Cloudinary.
//
// El API Secret nunca sale del backend.
//
// El parámetro "tipo" permite separar:
// - laboratorios/{uid}
// - perfiles/{uid}
//
// ============================================================================

interface UploadsApi {


    // ========================================================================
    // OBTENER FIRMA
    // ========================================================================

    @POST("api/uploads/signature")
    fun obtenerFirma(

        @Header("Authorization")
        authorization: String,

        @Query("tipo")
        tipo: String? = null

    ): Call<UploadSignatureResponse>


    // ========================================================================
    // SUBIR A CLOUDINARY
    // ========================================================================

    @Multipart
    @POST
    fun subirArchivoCloudinary(

        @Url
        url: String,

        @Part
        file: MultipartBody.Part,

        @Part("api_key")
        apiKey: RequestBody,

        @Part("timestamp")
        timestamp: RequestBody,

        @Part("signature")
        signature: RequestBody,

        @Part("folder")
        folder: RequestBody

    ): Call<CloudinaryUploadResponse>
}
