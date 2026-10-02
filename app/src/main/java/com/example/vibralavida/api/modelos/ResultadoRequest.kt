package com.example.vibralavida.api.modelos


// ============================================================================
// REQUEST PARA GUARDAR RESULTADOS
// ============================================================================
//
// Este modelo coincide con el body que recibe:
//
// POST /api/results
//
// Sirve para IMC y posteriormente también puede reutilizarse para:
// - calorías
// - AIS
// - DASS-21
// - riesgo cardiovascular
// - otras evaluaciones
//
// ============================================================================

data class ResultadoRequest(

    val tipo: String,

    val categoria: String? = null,

    val puntaje: Double? = null,

    val clasificacion: String? = null,

    val descripcion: String? = null,

    val respuestas: Any? = null,

    val datosExtra: Map<String, Any?>? = null
)


// ============================================================================
// RESPUESTA DEL BACKEND AL GUARDAR
// ============================================================================

// Ejemplo:
// {
//   "success": true,
//   "message": "Resultado guardado correctamente.",
//   "resultId": "..."
// }

data class ResultadoResponse(

    val success: Boolean = false,

    val message: String? = null,

    val resultId: String? = null
)
