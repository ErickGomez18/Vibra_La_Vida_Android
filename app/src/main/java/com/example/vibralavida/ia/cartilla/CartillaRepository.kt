package com.example.vibralavida.ia.cartilla

import android.content.Context
import org.json.JSONObject

// ============================================================================
// REPOSITORIO LOCAL DE CARTILLAS NACIONALES DE SALUD 2026
// ============================================================================
//
// Lee ÚNICAMENTE contenido extraído de los PDF oficiales que fueron cargados
// al proyecto y guardados como JSON dentro de:
//
// app/src/main/assets/cartillas/
//
// No necesita internet.
//
// ============================================================================

object CartillaRepository {

    private const val ASSETS_FOLDER =
        "cartillas"

    private fun assetFor(
        ageGroup: CartillaAgeGroup
    ): String? {

        return when (ageGroup) {

            CartillaAgeGroup.NINAS_NINOS_0_9 ->
                "cartilla_ninas_ninos_0_9_2026.json"

            CartillaAgeGroup.ADOLESCENTES_10_19 ->
                "cartilla_adolescentes_10_19_2026.json"

            CartillaAgeGroup.MUJERES_20_59 ->
                "cartilla_mujeres_20_59_2026.json"

            CartillaAgeGroup.HOMBRES_20_59 ->
                "cartilla_hombres_20_59_2026.json"

            CartillaAgeGroup.PERSONAS_MAYORES_60_MAS ->
                "cartilla_personas_mayores_60_mas_2026.json"

            CartillaAgeGroup.DESCONOCIDO ->
                null
        }
    }

    fun getContext(
        context: Context,
        ageGroup: CartillaAgeGroup,
        topic: CartillaTopic,
        maxChunks: Int = 2
    ): CartillaContext? {

        val assetName =
            assetFor(ageGroup)
                ?: return null

        val jsonText =
            context.assets
                .open("$ASSETS_FOLDER/$assetName")
                .bufferedReader()
                .use { it.readText() }

        val root =
            JSONObject(jsonText)

        val title =
            root.optString(
                "title",
                "Cartilla Nacional de Salud 2026"
            )

        val year =
            root.optInt(
                "year",
                2026
            )

        val array =
            root.getJSONArray(
                "chunks"
            )

        val exact =
            mutableListOf<CartillaChunk>()

        val fallback =
            mutableListOf<CartillaChunk>()

        for (
        index in
        0 until array.length()
        ) {

            val item =
                array.getJSONObject(index)

            val topicsJson =
                item.getJSONArray("topics")

            val topics =
                buildList {

                    for (
                    topicIndex in
                    0 until topicsJson.length()
                    ) {

                        add(
                            topicsJson.getString(
                                topicIndex
                            )
                        )
                    }
                }

            val chunk =
                CartillaChunk(
                    id = item.getString("id"),
                    pdfPage = item.getInt("pdfPage"),
                    topics = topics,
                    content = item.getString("content")
                )

            if (
                topics.contains(
                    topic.id
                )
            ) {

                exact.add(chunk)

            } else if (
                topics.contains(
                    CartillaTopic.GENERAL.id
                )
            ) {

                fallback.add(chunk)
            }
        }

        val selected =
            (
                if (exact.isNotEmpty()) {
                    exact
                } else {
                    fallback
                }
            )
                .take(
                    maxChunks.coerceIn(
                        1,
                        4
                    )
                )

        if (
            selected.isEmpty()
        ) {

            return null
        }

        val joinedContent =
            selected.joinToString(
                separator = "\n\n"
            ) { chunk ->

                "[PDF página ${chunk.pdfPage}]\n${chunk.content}"
            }

        return CartillaContext(
            ageGroup = ageGroup,
            topic = topic,
            sourceName = title,
            sourceYear = year,
            sourcePages = selected
                .map { it.pdfPage }
                .distinct(),
            content = joinedContent
        )
    }
}
