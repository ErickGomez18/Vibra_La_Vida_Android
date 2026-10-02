package com.example.vibralavida.ia

import java.util.Locale
import kotlin.math.round

// ============================================================================
// CONTEXTO PERSONAL COMPACTO
// ============================================================================

object AiContextProvider {

    fun create(
        age: String,
        gender: String,
        weight: String,
        height: String,
        activityLevel: String,
        caloriesLose: Int? = null,
        caloriesMaintain: Int? = null,
        caloriesGain: Int? = null
    ): AiUserContext {

        val ageValue =
            age.trim().toIntOrNull()

        val weightValue =
            parseDecimal(
                weight
            )

        val heightValue =
            parseDecimal(
                height
            )

        return AiUserContext(
            age =
                ageValue,

            gender =
                gender
                    .trim()
                    .takeIf {
                        it.isNotBlank()
                    },

            weightKg =
                weightValue,

            heightCm =
                heightValue,

            activityLevel =
                activityLevel
                    .trim()
                    .takeIf {
                        it.isNotBlank()
                    },

            bmi =
                calculateBmi(
                    weightValue,
                    heightValue
                ),

            caloriesLose =
                caloriesLose,

            caloriesMaintain =
                caloriesMaintain,

            caloriesGain =
                caloriesGain
        )
    }

    fun formatSelectedContext(
        userContext: AiUserContext,
        selectedTypes: Set<AiContextType>
    ): String {

        val values =
            mutableListOf<String>()

        if (
            AiContextType.AGE in selectedTypes &&
            userContext.age != null
        ) {
            values.add(
                "edad=${userContext.age}"
            )
        }

        if (
            AiContextType.GENDER in selectedTypes &&
            !userContext.gender.isNullOrBlank()
        ) {
            values.add(
                "sexo/género=${userContext.gender}"
            )
        }

        if (
            AiContextType.WEIGHT in selectedTypes &&
            userContext.weightKg != null
        ) {
            values.add(
                "peso=${formatNumber(userContext.weightKg)} kg"
            )
        }

        if (
            AiContextType.HEIGHT in selectedTypes &&
            userContext.heightCm != null
        ) {
            values.add(
                "estatura=${formatNumber(userContext.heightCm)} cm"
            )
        }

        if (
            AiContextType.ACTIVITY_LEVEL in selectedTypes &&
            !userContext.activityLevel.isNullOrBlank()
        ) {
            values.add(
                "actividad=${userContext.activityLevel}"
            )
        }

        if (
            AiContextType.BMI in selectedTypes &&
            userContext.bmi != null
        ) {
            values.add(
                "IMC=${formatNumber(userContext.bmi)}"
            )
        }

        if (
            AiContextType.CALORIES in selectedTypes &&
            userContext.age != null &&
            userContext.age >= 18
        ) {

            userContext.caloriesMaintain
                ?.let {
                    values.add(
                        "mantenimiento=${it} kcal/día"
                    )
                }

            userContext.caloriesLose
                ?.let {
                    values.add(
                        "bajar=${it} kcal/día"
                    )
                }

            userContext.caloriesGain
                ?.let {
                    values.add(
                        "subir=${it} kcal/día"
                    )
                }
        }

        if (
            values.isEmpty()
        ) {
            return ""
        }

        return "DATOS ÚTILES: " +
                values.joinToString(
                    "; "
                )
    }

    fun isMinor(
        userContext: AiUserContext
    ): Boolean {

        val age =
            userContext.age
                ?: return false

        return age < 18
    }

    private fun calculateBmi(
        weightKg: Double?,
        heightCm: Double?
    ): Double? {

        if (
            weightKg == null ||
            heightCm == null ||
            weightKg <= 0.0 ||
            heightCm <= 0.0
        ) {
            return null
        }

        val heightMeters =
            heightCm / 100.0

        return round(
            (
                weightKg /
                        (
                            heightMeters *
                            heightMeters
                        )
            ) * 10.0
        ) / 10.0
    }

    private fun parseDecimal(
        value: String
    ): Double? {

        return value
            .trim()
            .replace(
                ",",
                "."
            )
            .toDoubleOrNull()
    }

    private fun formatNumber(
        value: Double
    ): String {

        return String.format(
            Locale.US,
            "%.1f",
            value
        )
    }
}
