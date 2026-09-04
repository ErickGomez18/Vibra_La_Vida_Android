package com.example.vibralavida.agenda.bitacora


// ============================================================================
// TIPO DE ENTRADA DE LA BITÁCORA
// ============================================================================
//
// La Bitácora de Salud puede guardar:
//
// 1. Mediciones individuales.
//    Ejemplo:
//    - Glucosa
//    - Presión arterial
//    - Peso
//
// 2. Estudios de laboratorio completos.
//    Ejemplo:
//    - Química sanguínea
//    - Biometría hemática
//
// ============================================================================

enum class TipoEntradaBitacora {

    MEDICION,

    LABORATORIO
}


// ============================================================================
// REGISTRO DE SALUD
// ============================================================================
//
// Representa una medición individual.
//
// Ejemplos:
//
// Glucosa:
// valorPrincipal = "96"
// unidad = "mg/dL"
//
// Presión arterial:
// valorPrincipal = "120"
// valorSecundario = "80"
// unidad = "mmHg"
//
// ============================================================================

data class RegistroSalud(

    // Identificador único.
    val id: String = "",

    // Tipo de medición.
    //
    // Ejemplo:
    // "Glucosa"
    // "Presión arterial"
    // "Peso"
    val tipo: String = "",

    // Fecha del registro.
    //
    // dd/MM/yyyy
    val fecha: String = "",

    // Hora del registro.
    //
    // HH:mm
    val hora: String = "",

    // Valor principal.
    //
    // Ejemplo:
    // glucosa = 96
    // presión sistólica = 120
    val valorPrincipal: String = "",

    // Segundo valor cuando la medición lo necesita.
    //
    // Ejemplo:
    // presión diastólica = 80
    val valorSecundario: String = "",

    // Unidad.
    //
    // mg/dL
    // mmHg
    // kg
    // %
    val unidad: String = "",

    // Contexto opcional.
    //
    // Ejemplo:
    // "En ayuno"
    // "Después de comer"
    // "En reposo"
    val condicion: String = "",

    // Notas adicionales.
    val observaciones: String = ""
)