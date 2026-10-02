package com.example.vibralavida.api

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns

import com.example.vibralavida.api.modelos.CloudinaryUploadResponse
import com.example.vibralavida.api.modelos.UploadSignatureResponse

import com.google.firebase.auth.FirebaseAuth

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import java.io.File


// ============================================================================
// CLOUDINARY REPOSITORY
// ============================================================================
//
// Flujo:
//
// URI local
//   ↓
// pedir firma a Express
//   ↓
// copiar a archivo temporal
//   ↓
// subir a Cloudinary
//   ↓
// obtener secure_url
//   ↓
// devolver URL a la app
//
// ============================================================================

object CloudinaryRepository {


    // ========================================================================
    // SUBIR UN ARCHIVO
    // ========================================================================

    fun subirArchivo(

        context: Context,

        uri: Uri,

        onSuccess: (String) -> Unit,

        onError: (String) -> Unit,

        // "laboratorio" conserva el comportamiento existente.
        tipo: String = "laboratorio"

    ) {

        val usuario =
            FirebaseAuth
                .getInstance()
                .currentUser


        if (usuario == null) {

            onError(
                "No existe una sesión activa."
            )

            return
        }


        usuario
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


                // ============================================================
                // PEDIR FIRMA AL BACKEND
                // ============================================================

                ApiClient
                    .uploadsApi
                    .obtenerFirma(

                        authorization =
                            "Bearer $token",

                        tipo =
                            tipo
                    )
                    .enqueue(

                        object :
                            Callback<UploadSignatureResponse> {


                            override fun onResponse(

                                call:
                                Call<UploadSignatureResponse>,

                                response:
                                Response<UploadSignatureResponse>

                            ) {

                                if (!response.isSuccessful) {

                                    onError(
                                        "Error al obtener firma: ${response.code()}"
                                    )

                                    return
                                }


                                val firma =
                                    response.body()


                                if (
                                    firma == null ||
                                    !firma.success ||
                                    firma.signature.isBlank() ||
                                    firma.apiKey.isBlank() ||
                                    firma.cloudName.isBlank() ||
                                    firma.folder.isBlank()
                                ) {

                                    onError(
                                        firma?.message
                                            ?: "La firma de Cloudinary no es válida."
                                    )

                                    return
                                }


                                subirConFirma(

                                    context =
                                        context,

                                    uri =
                                        uri,

                                    firma =
                                        firma,

                                    onSuccess =
                                        onSuccess,

                                    onError =
                                        onError
                                )
                            }


                            override fun onFailure(

                                call:
                                Call<UploadSignatureResponse>,

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
    // SUBIR FOTO DE PERFIL
    // ========================================================================
    //
    // Utiliza una firma destinada a:
    //
    // perfiles/{uid}
    //
    // ========================================================================

    fun subirFotoPerfil(

        context: Context,

        uri: Uri,

        onSuccess: (String) -> Unit,

        onError: (String) -> Unit

    ) {


        subirArchivo(

            context =
                context,

            uri =
                uri,

            onSuccess =
                onSuccess,

            onError =
                onError,

            tipo =
                "perfil"
        )
    }


    // ========================================================================
    // SUBIR VARIOS ARCHIVOS
    // ========================================================================

    fun subirArchivos(

        context: Context,

        uris: List<String>,

        onSuccess: (List<String>) -> Unit,

        onError: (String) -> Unit

    ) {

        if (uris.isEmpty()) {

            onSuccess(
                emptyList()
            )

            return
        }


        val urlsSubidas =
            mutableListOf<String>()


        fun subirSiguiente(
            indice: Int
        ) {

            if (
                indice >= uris.size
            ) {

                onSuccess(
                    urlsSubidas
                )

                return
            }


            val uri =
                Uri.parse(
                    uris[indice]
                )


            subirArchivo(

                context =
                    context,

                uri =
                    uri,

                onSuccess = {
                        url ->


                    urlsSubidas.add(
                        url
                    )


                    subirSiguiente(
                        indice + 1
                    )
                },

                onError = {
                        mensaje ->


                    onError(

                        "Archivo ${indice + 1} de ${uris.size}: $mensaje"
                    )
                }
            )
        }


        subirSiguiente(
            0
        )
    }


    // ========================================================================
    // SUBIR CON FIRMA
    // ========================================================================

    private fun subirConFirma(

        context: Context,

        uri: Uri,

        firma: UploadSignatureResponse,

        onSuccess: (String) -> Unit,

        onError: (String) -> Unit

    ) {

        var archivoTemporal: File? =
            null


        try {

            val nombre =
                obtenerNombreArchivo(

                    context =
                        context,

                    uri =
                        uri
                )


            val mimeType =
                context
                    .contentResolver
                    .getType(
                        uri
                    )
                    ?: "application/octet-stream"


            val extension =
                nombre
                    .substringAfterLast(
                        ".",
                        ""
                    )
                    .takeIf {
                        it.isNotBlank()
                    }


            archivoTemporal =
                File.createTempFile(

                    "vibra_upload_",

                    if (extension != null) {
                        ".$extension"
                    } else {
                        ".tmp"
                    },

                    context.cacheDir
                )


            val inputStream =
                context
                    .contentResolver
                    .openInputStream(
                        uri
                    )


            if (inputStream == null) {

                archivoTemporal.delete()

                onError(
                    "No fue posible leer el archivo seleccionado."
                )

                return
            }


            inputStream.use {
                    input ->


                archivoTemporal
                    .outputStream()
                    .use {
                            output ->


                        input.copyTo(
                            output
                        )
                    }
            }


            val fileBody =
                archivoTemporal
                    .asRequestBody(
                        mimeType.toMediaTypeOrNull()
                    )


            val filePart =
                MultipartBody.Part
                    .createFormData(

                        "file",

                        nombre,

                        fileBody
                    )


            val apiKeyBody =
                firma.apiKey
                    .toRequestBody(
                        "text/plain".toMediaTypeOrNull()
                    )


            val timestampBody =
                firma.timestamp
                    .toString()
                    .toRequestBody(
                        "text/plain".toMediaTypeOrNull()
                    )


            val signatureBody =
                firma.signature
                    .toRequestBody(
                        "text/plain".toMediaTypeOrNull()
                    )


            val folderBody =
                firma.folder
                    .toRequestBody(
                        "text/plain".toMediaTypeOrNull()
                    )


            val uploadUrl =
                "https://api.cloudinary.com/v1_1/${firma.cloudName}/auto/upload"


            ApiClient
                .uploadsApi
                .subirArchivoCloudinary(

                    url =
                        uploadUrl,

                    file =
                        filePart,

                    apiKey =
                        apiKeyBody,

                    timestamp =
                        timestampBody,

                    signature =
                        signatureBody,

                    folder =
                        folderBody
                )
                .enqueue(

                    object :
                        Callback<CloudinaryUploadResponse> {


                        override fun onResponse(

                            call:
                            Call<CloudinaryUploadResponse>,

                            response:
                            Response<CloudinaryUploadResponse>

                        ) {

                            archivoTemporal
                                ?.delete()


                            if (!response.isSuccessful) {

                                onError(
                                    "Cloudinary respondió con error ${response.code()}."
                                )

                                return
                            }


                            val secureUrl =
                                response
                                    .body()
                                    ?.secure_url


                            if (secureUrl.isNullOrBlank()) {

                                onError(
                                    response
                                        .body()
                                        ?.error
                                        ?.message
                                        ?: "Cloudinary no devolvió la URL del archivo."
                                )

                                return
                            }


                            onSuccess(
                                secureUrl
                            )
                        }


                        override fun onFailure(

                            call:
                            Call<CloudinaryUploadResponse>,

                            throwable:
                            Throwable

                        ) {

                            archivoTemporal
                                ?.delete()


                            onError(

                                throwable.message
                                    ?: "No fue posible subir el archivo a Cloudinary."
                            )
                        }
                    }
                )


        } catch (
            e: Exception
        ) {

            archivoTemporal
                ?.delete()


            onError(

                e.message
                    ?: "No fue posible preparar el archivo para subirlo."
            )
        }
    }


    // ========================================================================
    // NOMBRE DEL ARCHIVO
    // ========================================================================

    private fun obtenerNombreArchivo(

        context: Context,

        uri: Uri

    ): String {

        var nombre =
            "archivo"


        try {

            context
                .contentResolver
                .query(

                    uri,

                    null,

                    null,

                    null,

                    null

                )
                ?.use {
                        cursor ->


                    val indice =
                        cursor.getColumnIndex(
                            OpenableColumns.DISPLAY_NAME
                        )


                    if (
                        indice >= 0 &&
                        cursor.moveToFirst()
                    ) {

                        nombre =
                            cursor.getString(
                                indice
                            )
                                ?: nombre
                    }
                }

        } catch (
            e: Exception
        ) {

            e.printStackTrace()
        }


        return nombre
    }
}
