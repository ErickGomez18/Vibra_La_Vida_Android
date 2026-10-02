package com.example.vibralavida.ia.cartilla

// ============================================================================
// CONTEXTO QUE SE ENTREGA A NUBY
// ============================================================================

data class CartillaContext(
    val ageGroup: CartillaAgeGroup,
    val topic: CartillaTopic,
    val sourceName: String,
    val sourceYear: Int,
    val sourcePages: List<Int>,
    val content: String
)
