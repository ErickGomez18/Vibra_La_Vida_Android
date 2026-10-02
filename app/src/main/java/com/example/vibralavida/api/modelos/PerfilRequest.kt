package com.example.vibralavida.api.modelos


// ============================================================================
// PERFIL REQUEST
// ============================================================================
//
// Se utiliza en:
//
// PUT /api/users/me
//
// Todos los campos son opcionales para permitir actualizaciones parciales.
//
// Ejemplos:
//
// - completar/editar el perfil;
// - actualizar solamente la foto de perfil.
//
// ============================================================================

data class PerfilRequest(

    val nombreCompleto: String? = null,

    val edad: String? = null,

    val genero: String? = null,

    val peso: String? = null,

    val estatura: String? = null,

    val nivelActividad: String? = null,

    val enfermedadesCronicas: List<String>? = null,

    val otraEnfermedadCronica: String? = null,

    // URL HTTPS devuelta por Cloudinary.
    val fotoPerfilUrl: String? = null
)
