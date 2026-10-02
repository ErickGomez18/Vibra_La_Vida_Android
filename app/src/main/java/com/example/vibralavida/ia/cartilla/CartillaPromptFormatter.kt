package com.example.vibralavida.ia.cartilla

// ============================================================================
// CONTEXTO COMPACTO DE CARTILLA PARA GEMMA 270M
// ============================================================================

object CartillaPromptFormatter {

    private const val MAX_CONTENT_CHARS =
        650

    fun format(
        context: CartillaContext?
    ): String {

        if (
            context == null ||
            context.content.isBlank()
        ) {
            return ""
        }

        val compactContent =
            context.content
                .replace(
                    "\\s+".toRegex(),
                    " "
                )
                .trim()
                .take(
                    MAX_CONTENT_CHARS
                )

        val pages =
            context.sourcePages
                .joinToString(
                    ", "
                )

        return """
            CARTILLA 2026 (${context.topic.displayName}, pág. $pages):
            $compactContent

            Usa este fragmento como fuente principal. No inventes datos que no aparezcan aquí.
        """.trimIndent()
    }
}
