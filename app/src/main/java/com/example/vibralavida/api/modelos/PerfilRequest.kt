package com.example.vibralavida.api.modelos


// ============================================================================
// PERFIL REQUEST
// ============================================================================
//
// Representa la información que Android enviará a:
//
// PUT /api/users/me
//
// Los nombres deben coincidir con los campos que espera
// nuestra API de Express.
//
// ============================================================================

data class PerfilRequest(

    val edad: String,

    val genero: String,

    val peso: String,

    val estatura: String,

    val nivelActividad: String,

    val enfermedadesCronicas: List<String>,

    val otraEnfermedadCronica: String
)