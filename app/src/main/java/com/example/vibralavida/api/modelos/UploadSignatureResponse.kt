package com.example.vibralavida.api.modelos


// ============================================================================
// RESPUESTA DE FIRMA DE CLOUDINARY
// ============================================================================

// Viene de POST /api/uploads/signature

data class UploadSignatureResponse(

    val success: Boolean = false,

    val timestamp: Long = 0L,

    val signature: String = "",

    val apiKey: String = "",

    val cloudName: String = "",

    val folder: String = "",

    val message: String? = null,

    val error: String? = null
)
