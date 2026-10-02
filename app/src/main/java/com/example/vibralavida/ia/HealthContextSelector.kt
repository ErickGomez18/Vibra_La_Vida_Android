package com.example.vibralavida.ia

import java.text.Normalizer
import java.util.Locale


// ============================================================================
// SELECTOR DE DATOS DE HEALTH CONNECT
// ============================================================================
//
// Nuby NO consulta siempre todo.
//
// Ejemplos:
//
// "¿Cuántos pasos llevo?"
// → STEPS
//
// "Dormí poco, ¿qué actividad hago hoy?"
// → SLEEP + STEPS
//
// "¿Cómo estuvo mi frecuencia cardiaca?"
// → HEART_RATE
//
// "¿Qué actividad me recomiendas hoy?"
// → STEPS + SLEEP + HEART_RATE
//
// ============================================================================

object HealthContextSelector {


    fun select(
        question: String
    ): Set<HealthContextType> {


        val text =
            normalize(
                question
            )


        val result =
            mutableSetOf<HealthContextType>()


        // ====================================================================
        // PASOS
        // ====================================================================

        if (
            containsAny(

                text,

                "paso",
                "pasos",
                "caminar",
                "caminata",
                "actividad",
                "ejercicio",
                "ejercicios",
                "moverme",
                "movimiento",
                "activo",
                "activa",
                "sedentario",
                "sedentaria"
            )
        ) {

            result.add(
                HealthContextType.STEPS
            )
        }


        // ====================================================================
        // SUEÑO
        // ====================================================================

        if (
            containsAny(

                text,

                "sueno",
                "dormi",
                "dormir",
                "descanso",
                "descansar",
                "cansado",
                "cansada",
                "fatiga",
                "insomnio"
            )
        ) {

            result.add(
                HealthContextType.SLEEP
            )
        }


        // ====================================================================
        // FRECUENCIA CARDÍACA
        // ====================================================================

        if (
            containsAny(

                text,

                "frecuencia cardiaca",
                "frecuencia cardíaca",
                "ritmo cardiaco",
                "ritmo cardíaco",
                "latidos",
                "pulso",
                "bpm",
                "corazon",
                "corazón"
            )
        ) {

            result.add(
                HealthContextType.HEART_RATE
            )
        }


        // ====================================================================
        // RECOMENDACIÓN DE ACTIVIDAD "HOY"
        // ====================================================================
        //
        // Si pregunta qué puede hacer HOY, sí tiene sentido considerar
        // los tres datos disponibles como contexto.
        //
        // ====================================================================

        val asksTodayRecommendation =
            containsAny(
                text,
                "que actividad me recomiendas hoy",
                "que ejercicio me recomiendas hoy",
                "que puedo hacer hoy",
                "que me recomiendas hacer hoy",
                "actividad para hoy"
            )


        if (
            asksTodayRecommendation
        ) {

            result.add(
                HealthContextType.STEPS
            )

            result.add(
                HealthContextType.SLEEP
            )

            result.add(
                HealthContextType.HEART_RATE
            )
        }


        return result
    }


    private fun containsAny(
        text: String,
        vararg values: String
    ): Boolean {


        return values.any {
            text.contains(
                normalize(
                    it
                )
            )
        }
    }


    private fun normalize(
        value: String
    ): String {


        return Normalizer
            .normalize(

                value
                    .lowercase(
                        Locale.ROOT
                    )
                    .trim(),

                Normalizer.Form.NFD
            )
            .replace(
                "\\p{Mn}+".toRegex(),
                ""
            )
            .replace(
                "[¿?¡!.,;:]".toRegex(),
                " "
            )
            .replace(
                "\\s+".toRegex(),
                " "
            )
            .trim()
    }
}
