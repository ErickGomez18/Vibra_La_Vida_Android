package com.example.vibralavida.ia

import com.example.vibralavida.api.nuby.EspecialistaNuby

// ============================================================================
// MENSAJE DEL CHAT
// ============================================================================

data class NubyMessage(

    val text: String,

    val fromUser: Boolean,

    val sourceName: String? = null,

    val sourcePages: List<Int> = emptyList(),

    val specialists: List<EspecialistaNuby> = emptyList()
)
