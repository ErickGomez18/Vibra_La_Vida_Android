package com.example.vibralavida.api.modelos


// ============================================================================
// DATOS DEL PERFIL DEL USUARIO
// ============================================================================
//
// Representa el documento:
//
// usuarios/{uid}
//
// que devuelve:
//
// GET /api/users/me
//
// Todos los valores tienen defaults para evitar errores
// si algún usuario antiguo todavía no tiene todos los campos.
//
// ============================================================================

data class UsuarioPerfil(

    val uid: String? = null,

    val nombre: String? = null,

    val nombreCompleto: String? = null,

    val correo: String? = null,

    val edad: String? = null,

    val genero: String? = null,

    val peso: String? = null,

    val estatura: String? = null,

    val nivelActividad: String? = null,

    val enfermedadesCronicas: List<String> = emptyList(),

    val otraEnfermedadCronica: String? = null,

    val fotoPerfilUrl: String? = null
)


// ============================================================================
// GET /api/users/me
// ============================================================================

data class PerfilResponse(

    val success: Boolean = false,

    val user: UsuarioPerfil? = null,

    val message: String? = null,

    val error: String? = null
)


// ============================================================================
// PUT /api/users/me
// ============================================================================

data class ActualizarPerfilResponse(

    val success: Boolean = false,

    val message: String? = null,

    val error: String? = null
)