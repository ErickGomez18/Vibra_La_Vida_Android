package com.example.vibralavida.ia

import java.text.Normalizer
import java.util.Locale

object RecommendationProfileInterpreter {

    fun interpret(userContext: AiUserContext): RecommendationProfileSummary {
        val age = userContext.age
        val activity = normalize(userContext.activityLevel.orEmpty())

        val activityCategory = when {
            activity.contains("sedent") -> ActivityCategory.SEDENTARY
            activity.contains("liger") -> ActivityCategory.LIGHT
            activity.contains("moder") -> ActivityCategory.MODERATE
            activity.contains("muy activo") || activity.contains("activo") -> ActivityCategory.ACTIVE
            else -> ActivityCategory.UNKNOWN
        }

        val bmi = userContext.bmi

        return RecommendationProfileSummary(
            age = age,
            isMinor = age != null && age < 18,
            isOlderAdult = age != null && age >= 60,
            activityCategory = activityCategory,
            bmi = bmi,
            // Se usa solo como protección para no reforzar pérdida de peso.
            lowBmiFlag = bmi != null && bmi < 18.5
        )
    }

    private fun normalize(value: String): String {
        return Normalizer
            .normalize(value.lowercase(Locale.ROOT).trim(), Normalizer.Form.NFD)
            .replace("\\p{Mn}+".toRegex(), "")
            .trim()
    }
}
