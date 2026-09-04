package com.example.vibralavida.agenda.bitacora


// ============================================================================
// ESTUDIO DE LABORATORIO
// ============================================================================
//
// Este modelo representa un estudio completo.
//
// No intentamos guardar manualmente todos sus valores.
//
// Ejemplo:
//
// Química sanguínea de 32 elementos
//
// El usuario puede adjuntar:
//
// - PDF
// - Una fotografía
// - Varias fotografías
//
// ============================================================================

data class EstudioLaboratorio(

    // Identificador único.
    val id: String = "",

    // Tipo general del estudio.
    //
    // Ejemplo:
    // "Química sanguínea"
    // "Biometría hemática"
    val tipoEstudio: String = "",

    // Se utiliza cuando el usuario selecciona "Otro".
    val nombrePersonalizado: String = "",

    // Fecha del estudio.
    val fecha: String = "",

    // Nombre del laboratorio donde se realizó.
    //
    // Campo opcional.
    val laboratorio: String = "",

    // Lista de archivos adjuntos.
    //
    // Puede contener URI de:
    // - PDF
    // - Fotografías
    val archivosUri: List<String> = emptyList(),

    // Observaciones opcionales.
    val observaciones: String = ""
)