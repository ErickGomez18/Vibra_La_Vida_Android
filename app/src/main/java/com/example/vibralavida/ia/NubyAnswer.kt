package com.example.vibralavida.ia

import com.example.vibralavida.api.nuby.EspecialistaNuby

// ============================================================================
// RESPUESTA DE NUBY
// ============================================================================

data class NubyAnswer(

    val text: String,

    val sourceName: String? = null,

    val sourcePages: List<Int> = emptyList(),

    // Especialistas sugeridos cuando la conversación rebasa
    // la orientación educativa de Nuby.
    val specialists: List<EspecialistaNuby> = emptyList()
)
