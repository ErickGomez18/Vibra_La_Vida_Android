package com.example.vibralavida.ia

import java.text.Normalizer
import java.util.Locale

object SafetyIntentDetector {

    fun detect(
        question: String
    ): SafetyIntent {

        val text =
            normalize(
                question
            )

        return when {

            containsAny(
                text,
                "que medicamento debo tomar",
                "que medicina debo tomar",
                "que pastilla debo tomar",
                "que dosis",
                "cambiar mi dosis",
                "suspender mi medicamento",
                "tratamiento exacto"
            ) -> SafetyIntent.MEDICATION_OR_TREATMENT

            containsAny(
                text,
                "diagnostico",
                "diagnosticarme",
                "dime que tengo",
                "que enfermedad tengo",
                "tengo diabetes",
                "tengo hipertension",
                "tengo depresion"
            ) -> SafetyIntent.DIAGNOSIS

            containsAny(
                text,
                "dieta exacta",
                "menu exacto",
                "plan alimenticio exacto",
                "dieta personalizada",
                "dame una dieta",
                "hazme una dieta"
            ) -> SafetyIntent.CLINICAL_DIET

            containsAny(
                text,
                "rutina exacta",
                "rutina personalizada",
                "rutina para mi enfermedad",
                "ejercicio para mi enfermedad",
                "hazme una rutina"
            ) -> SafetyIntent.CLINICAL_EXERCISE

            containsAny(
                text,
                "ansiedad",
                "depresion",
                "panico",
                "salud mental"
            ) -> SafetyIntent.MENTAL_HEALTH

            containsAny(
                text,
                "palpitaciones",
                "arritmia",
                "ritmo cardiaco",
                "frecuencia cardiaca",
                "dolor en el pecho"
            ) -> SafetyIntent.CARDIOVASCULAR

            containsAny(
                text,
                "sintoma",
                "me siento mal",
                "que puede ser"
            ) -> SafetyIntent.GENERAL_MEDICAL

            else -> SafetyIntent.NONE
        }
    }

    fun suggestedSpecialty(
        intent: SafetyIntent,
        question: String
    ): ProfessionalSpecialty? {

        val text =
            normalize(
                question
            )

        return when (
            intent
        ) {

            SafetyIntent.CLINICAL_DIET ->
                ProfessionalSpecialty.NUTRITION

            SafetyIntent.CLINICAL_EXERCISE,
            SafetyIntent.GENERAL_MEDICAL,
            SafetyIntent.DIAGNOSIS,
            SafetyIntent.MEDICATION_OR_TREATMENT -> {

                if (
                    containsAny(
                        text,
                        "glucosa",
                        "diabetes",
                        "metabol",
                        "insulina"
                    )
                ) {
                    ProfessionalSpecialty.ENDOCRINOLOGY
                } else {
                    ProfessionalSpecialty.GENERAL_MEDICINE
                }
            }

            SafetyIntent.MENTAL_HEALTH ->
                ProfessionalSpecialty.PSYCHOLOGY

            SafetyIntent.CARDIOVASCULAR ->
                ProfessionalSpecialty.CARDIOLOGY

            SafetyIntent.NONE ->
                null
        }
    }

    fun shouldReferImmediately(
        intent: SafetyIntent
    ): Boolean {

        return intent == SafetyIntent.DIAGNOSIS ||
                intent == SafetyIntent.MEDICATION_OR_TREATMENT ||
                intent == SafetyIntent.GENERAL_MEDICAL ||
                intent == SafetyIntent.CARDIOVASCULAR
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
                value.lowercase(Locale.ROOT),
                Normalizer.Form.NFD
            )
            .replace(
                "\\p{Mn}+".toRegex(),
                ""
            )
            .trim()
    }
}
