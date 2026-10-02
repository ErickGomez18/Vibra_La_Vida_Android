package com.example.vibralavida.ia

import java.text.Normalizer
import java.util.Locale


// ============================================================================
// SELECTOR DE CONTEXTO
// ============================================================================
//
// Determina qué datos personales necesita una pregunta.
//
// Ejemplos:
//
// "¿Cómo puedo dormir mejor?"
// → AGE
//
// "¿Qué puedo comer para mantener mi peso?"
// → AGE + GENDER + WEIGHT + HEIGHT + ACTIVITY_LEVEL + BMI + CALORIES
//
// ============================================================================

object AiContextSelector {


    fun select(
        question: String
    ): Set<AiContextType> {


        val text =
            normalize(
                question
            )


        val result =
            mutableSetOf<AiContextType>()


        // --------------------------------------------------------------------
        // EDAD
        // --------------------------------------------------------------------
        //
        // La edad es útil para casi todas las recomendaciones de estilos de
        // vida y además permite aplicar protecciones para menores de edad.
        //
        // --------------------------------------------------------------------

        result.add(
            AiContextType.AGE
        )


        // --------------------------------------------------------------------
        // ALIMENTACIÓN / PESO / CALORÍAS
        // --------------------------------------------------------------------

        if (
            containsAny(

                text,

                "comer",
                "comida",
                "alimentacion",
                "nutricion",
                "caloria",
                "calorias",
                "peso",
                "subir de peso",
                "bajar de peso",
                "mantener mi peso",
                "adelgazar",
                "engordar",
                "dieta",
                "desayuno",
                "cena"
            )
        ) {


            result.addAll(

                setOf(

                    AiContextType.GENDER,

                    AiContextType.WEIGHT,

                    AiContextType.HEIGHT,

                    AiContextType.ACTIVITY_LEVEL,

                    AiContextType.BMI,

                    AiContextType.CALORIES
                )
            )
        }


        // --------------------------------------------------------------------
        // IMC / ESTADO CORPORAL
        // --------------------------------------------------------------------

        if (
            containsAny(

                text,

                "imc",
                "indice de masa corporal",
                "masa corporal",
                "sobrepeso",
                "obesidad",
                "bajo peso"
            )
        ) {


            result.addAll(

                setOf(

                    AiContextType.GENDER,

                    AiContextType.WEIGHT,

                    AiContextType.HEIGHT,

                    AiContextType.BMI
                )
            )
        }


        // --------------------------------------------------------------------
        // ACTIVIDAD FÍSICA
        // --------------------------------------------------------------------

        if (
            containsAny(

                text,

                "ejercicio",
                "actividad fisica",
                "entrenar",
                "caminar",
                "correr",
                "pasos",
                "deporte",
                "sedentario"
            )
        ) {


            result.add(
                AiContextType.ACTIVITY_LEVEL
            )
        }


        // --------------------------------------------------------------------
        // SALUD SEXUAL / MENSTRUAL
        // --------------------------------------------------------------------

        if (
            containsAny(

                text,

                "sexual",
                "sexo",
                "menstruacion",
                "periodo",
                "regla",
                "anticonceptivo",
                "embarazo"
            )
        ) {


            result.add(
                AiContextType.GENDER
            )
        }


        return result
    }


    // ========================================================================
    // UTILIDADES
    // ========================================================================

    private fun containsAny(
        text: String,
        vararg values: String
    ): Boolean {

        return values.any {
            text.contains(it)
        }
    }


    private fun normalize(
        value: String
    ): String {


        val normalized =
            Normalizer.normalize(

                value.lowercase(
                    Locale.ROOT
                ),

                Normalizer.Form.NFD
            )


        return normalized
            .replace(
                "\\p{Mn}+".toRegex(),
                ""
            )
            .trim()
    }
}
