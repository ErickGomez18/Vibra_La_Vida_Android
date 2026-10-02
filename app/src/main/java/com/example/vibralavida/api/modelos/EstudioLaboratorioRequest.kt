package com.example.vibralavida.api.modelos


// ============================================================================
// ESTUDIO LABORATORIO REQUEST
// ============================================================================
//
// POST /api/bitacora/laboratorios
// PUT  /api/bitacora/laboratorios/:id
//
// Por ahora archivosUri contiene las URI locales del teléfono.
// Más adelante las cambiaremos por URLs de Firebase Storage.
//
// ============================================================================

data class EstudioLaboratorioRequest(

    val tipoEstudio: String,

    val nombrePersonalizado: String,

    val fecha: String,

    val laboratorio: String,

    val archivosUri: List<String>,

    val observaciones: String
)
