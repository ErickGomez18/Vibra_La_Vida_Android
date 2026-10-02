package com.example.vibralavida.ia

import java.text.Normalizer
import java.util.Locale


// ============================================================================
// SELECTOR DE OBJETIVO ALIMENTARIO
// ============================================================================
//
// Detecta si la pregunta está relacionada con:
//
// - mantener peso;
// - bajar peso;
// - subir peso;
// - alimentación general.
//
// ============================================================================

object AiNutritionGoalSelector {


    fun select(
        question: String
    ): AiNutritionGoal {


        val text =
            normalize(
                question
            )


        return when {


            containsAny(
                text,
                "bajar de peso",
                "perder peso",
                "adelgazar",
                "reducir peso",
                "bajar peso"
            ) -> {

                AiNutritionGoal.LOSE
            }


            containsAny(
                text,
                "subir de peso",
                "aumentar de peso",
                "ganar peso",
                "subir peso",
                "aumentar peso"
            ) -> {

                AiNutritionGoal.GAIN
            }


            containsAny(
                text,
                "mantener mi peso",
                "mantener peso",
                "mantenerme",
                "mantenimiento"
            ) -> {

                AiNutritionGoal.MAINTAIN
            }


            else -> {

                AiNutritionGoal.GENERAL
            }
        }
    }


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


        return Normalizer
            .normalize(

                value.lowercase(
                    Locale.ROOT
                ),

                Normalizer.Form.NFD
            )
            .replace(
                "\\p{Mn}+".toRegex(),
                ""
            )
            .trim()
    }
}
