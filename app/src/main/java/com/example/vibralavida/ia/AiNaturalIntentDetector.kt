package com.example.vibralavida.ia

import java.text.Normalizer
import java.util.Locale

object AiNaturalIntentDetector {

    fun detect(question: String): AiNaturalIntent {
        val text = normalize(question)

        return when {
            containsAny(
                text,
                "agua", "hidratar", "hidratacion", "tomar agua",
                "beber agua", "litros", "vasos de agua", "sed"
            ) -> AiNaturalIntent.HYDRATION

            containsAny(
                text,
                "ejercicio", "ejercicios", "actividad fisica", "entrenar",
                "entrenamiento", "rutina", "caminar", "caminata",
                "correr", "trotar", "sentadilla", "sentadillas",
                "desplante", "desplantes", "flexion", "flexiones",
                "lagartija", "lagartijas", "abdominal", "abdominales",
                "cuerda", "saltar", "bicicleta", "escaleras", "cardio"
            ) -> AiNaturalIntent.ACTIVITY

            containsAny(
                text,
                "comer", "comida", "alimentacion", "nutricion", "dieta",
                "desayuno", "almuerzo", "cena", "colacion", "caloria",
                "calorias", "carne", "harina", "harinas", "fruta",
                "frutas", "verdura", "verduras", "proteina", "proteinas",
                "subir de peso", "bajar de peso", "mantener mi peso"
            ) -> AiNaturalIntent.NUTRITION

            containsAny(
                text,
                "dormir", "sueno", "insomnio", "descansar", "descanso"
            ) -> AiNaturalIntent.SLEEP

            containsAny(
                text,
                "estres", "ansiedad", "animo", "emocional",
                "salud mental", "triste", "preocupado", "preocupada"
            ) -> AiNaturalIntent.MENTAL_WELLBEING

            else -> AiNaturalIntent.GENERAL
        }
    }

    private fun containsAny(text: String, vararg values: String): Boolean {
        return values.any { text.contains(it) }
    }

    private fun normalize(value: String): String {
        return Normalizer
            .normalize(
                value.lowercase(Locale.ROOT).trim(),
                Normalizer.Form.NFD
            )
            .replace("\\p{Mn}+".toRegex(), "")
            .replace("[¿?¡!.,;:]".toRegex(), " ")
            .replace("\\s+".toRegex(), " ")
            .trim()
    }
}
