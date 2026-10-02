package com.example.vibralavida.ia.cartilla

// ============================================================================
// FRAGMENTO VERIFICADO DE UNA CARTILLA
// ============================================================================

data class CartillaChunk(
    val id: String,
    val pdfPage: Int,
    val topics: List<String>,
    val content: String
)
