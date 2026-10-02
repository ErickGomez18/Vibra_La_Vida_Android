package com.example.vibralavida.api.modelos


// ============================================================================
// REGISTRO SALUD REQUEST
// ============================================================================
//
// POST /api/bitacora/registros
// PUT  /api/bitacora/registros/:id
//
// ============================================================================

data class RegistroSaludRequest(

    val tipo: String,

    val fecha: String,

    val hora: String,

    val valorPrincipal: String,

    val valorSecundario: String,

    val unidad: String,

    val condicion: String,

    val observaciones: String
)
