package com.example.vibralavida.agenda.medicamentos


// ============================================================================
// MODELO DE MEDICAMENTO
// ============================================================================
//
// Representa un medicamento registrado por el paciente.
//
// Más adelante podremos guardar esta misma estructura
// en Firebase / Firestore.
//

data class Medicamento(

    // Identificador único.
    val id: String = "",

    // Nombre del medicamento.
    // Ejemplo: Metformina
    val nombre: String = "",

    // Dosis.
    // Ejemplo: 500 mg
    val dosis: String = "",

    // Ejemplo:
    // Tableta, cápsula, jarabe, etc.
    val presentacion: String = "",

    // Horarios en los que debe tomarse.
    // Ejemplo:
    // 08:00
    // 20:00
    val horarios: List<String> = emptyList(),

    // Fecha de inicio del tratamiento.
    val fechaInicio: String = "",

    // Fecha final.
    // Puede quedar vacía.
    val fechaFin: String = "",

    // Indicaciones adicionales.
    val indicaciones: String = "",

    // Indica si los recordatorios estarán activos.
    val recordatorioActivo: Boolean = true,

    // URI local de la fotografía.
    // Más adelante podrá convertirse en una URL de Firebase.
    val fotoUri: String? = null
)