package com.example.vibralavida.api.modelos


// ============================================================================
// RESPUESTA DIRECTA DE CLOUDINARY
// ============================================================================

data class CloudinaryUploadResponse(

    val public_id: String? = null,

    val secure_url: String? = null,

    val resource_type: String? = null,

    val format: String? = null,

    val bytes: Long? = null,

    val original_filename: String? = null,

    val error: CloudinaryError? = null
)


data class CloudinaryError(

    val message: String? = null
)
