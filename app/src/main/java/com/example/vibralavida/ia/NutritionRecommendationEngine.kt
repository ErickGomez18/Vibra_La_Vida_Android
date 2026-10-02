package com.example.vibralavida.ia

// ============================================================================
// MOTOR LOCAL DE RECOMENDACIONES ALIMENTARIAS - FASE 11
// ============================================================================
// Considera edad, objetivo, actividad, calorías de la app e IMC como señal
// de seguridad cuando es bajo. No crea dietas clínicas ni menús rígidos.
// ============================================================================

object NutritionRecommendationEngine {

    fun build(question: String, userContext: AiUserContext): NutritionRecommendation {
        val goal = AiNutritionGoalSelector.select(question)
        val profile = RecommendationProfileInterpreter.interpret(userContext)

        if (profile.isMinor) {
            return NutritionRecommendation(
                goal = AiNutritionGoal.GENERAL,
                calorieReference = null,
                suggestions = listOf(
                    "incluye frutas y verduras de distintos tipos durante el día",
                    "combina alimentos como frijoles, lentejas, huevo, pollo, pescado o lácteos según tus preferencias",
                    "prefiere agua simple como bebida principal",
                    "evita dietas restrictivas, ayunos o metas numéricas para bajar o subir de peso"
                ),
                fallbackResponse =
                    "De acuerdo con tu perfil, conviene enfocarte en una alimentación variada y suficiente en lugar de usar metas de calorías para modificar tu peso. Puedes combinar frutas o verduras, una fuente de proteína y alimentos como tortilla, arroz, avena o leguminosas, además de preferir agua simple."
            )
        }

        if (profile.lowBmiFlag && goal == AiNutritionGoal.LOSE) {
            return NutritionRecommendation(
                goal = AiNutritionGoal.GENERAL,
                calorieReference = userContext.caloriesMaintain,
                suggestions = listOf(
                    "mantén comidas regulares y variadas",
                    "incluye fuentes de proteína, leguminosas, cereales, frutas y verduras",
                    "evita reducir aún más la comida sin orientación profesional"
                ),
                fallbackResponse =
                    "De acuerdo con los datos de tu perfil, no sería prudente orientar una reducción adicional de peso solo con una recomendación automática. Te sugiero mantener comidas variadas y regulares y, si deseas cambiar tu peso, consultarlo con un profesional."
            )
        }

        return when (goal) {
            AiNutritionGoal.MAINTAIN -> {
                val calories = userContext.caloriesMaintain
                NutritionRecommendation(
                    goal = goal,
                    calorieReference = calories,
                    suggestions = listOf(
                        "en tus comidas principales combina verduras o fruta, una fuente de proteína y una porción de cereal o leguminosa",
                        "alterna carne roja con pollo, pescado, huevo o leguminosas",
                        "modera refrescos, bebidas azucaradas, galletas, pan dulce y otras harinas refinadas",
                        "prefiere agua simple durante el día",
                        "si tienes hambre entre comidas, prueba fruta, yogurt natural o un puñado pequeño de nueces"
                    ),
                    fallbackResponse = buildMaintainFallback(calories, profile.activityCategory)
                )
            }

            AiNutritionGoal.LOSE -> {
                val calories = userContext.caloriesLose
                NutritionRecommendation(
                    goal = goal,
                    calorieReference = calories,
                    suggestions = listOf(
                        "aumenta verduras, frutas enteras y leguminosas para mejorar saciedad",
                        "incluye una fuente de proteína en las comidas principales, como huevo, pollo, pescado, frijoles o lentejas",
                        "modera pan dulce, galletas, frituras, bebidas azucaradas y harinas refinadas",
                        "si consumes carne roja con frecuencia, alterna algunos días con pescado, pollo, huevo o leguminosas",
                        "prefiere agua simple y evita dietas extremas o ayunos"
                    ),
                    fallbackResponse = buildLoseFallback(calories, profile.activityCategory)
                )
            }

            AiNutritionGoal.GAIN -> {
                val calories = userContext.caloriesGain
                NutritionRecommendation(
                    goal = goal,
                    calorieReference = calories,
                    suggestions = listOf(
                        "agrega una colación como yogurt con fruta, avena con leche o pan integral con crema de cacahuate",
                        "puedes aumentar poco a poco porciones de arroz, tortilla, avena, papa o leguminosas",
                        "incluye alimentos con energía y nutrientes como aguacate, nueces, semillas o crema de cacahuate",
                        "mantén fuentes de proteína como huevo, pollo, pescado, lácteos o leguminosas"
                    ),
                    fallbackResponse = buildGainFallback(calories, profile.activityCategory)
                )
            }

            AiNutritionGoal.GENERAL -> {
                val calories = userContext.caloriesMaintain
                NutritionRecommendation(
                    goal = goal,
                    calorieReference = calories,
                    suggestions = listOf(
                        "incluye verduras o fruta en la mayoría de tus comidas",
                        "combina proteína con tortilla, arroz, avena, papa o leguminosas",
                        "modera refrescos, bebidas azucaradas y harinas refinadas",
                        "alterna carnes con huevo, pescado y leguminosas",
                        "prefiere agua simple durante el día"
                    ),
                    fallbackResponse = buildGeneralFallback(calories)
                )
            }
        }
    }

    fun buildRewritePrompt(recommendation: NutritionRecommendation): String {
        val calorieText = recommendation.calorieReference
            ?.let { "Referencia aproximada de la app: $it kcal/día." }
            ?: "Sin referencia numérica."

        return """
            Reescribe como Nuby en máximo 3 oraciones.
            Di \"de acuerdo con tu perfil\" cuando sea natural.
            Sé concreto y menciona 2 o 3 recomendaciones.

            $calorieText
            Opciones permitidas:
            ${recommendation.suggestions.joinToString("; ")}

            Reglas:
            - No inventes cantidades de agua.
            - No inventes porciones exactas.
            - No diseñes un menú clínico.
            - No repartas calorías por comida.
            - No prohíbas alimentos.
        """.trimIndent()
    }

    private fun buildMaintainFallback(calories: Int?, activityCategory: ActivityCategory): String {
        val reference = calories?.let { "La app estima alrededor de $it kcal al día para mantenimiento. " } ?: ""
        val activityText = if (activityCategory == ActivityCategory.SEDENTARY) {
            "Como tu nivel de actividad es bajo, conviene priorizar alimentos que den saciedad sin depender tanto de bebidas azucaradas, pan dulce o frituras. "
        } else ""

        return reference + activityText +
                "De acuerdo con tu perfil, procura combinar verduras o fruta, una fuente de proteína y alimentos como tortilla, arroz, avena o leguminosas. Puedes moderar harinas refinadas y alternar la carne roja con pollo, pescado, huevo o leguminosas."
    }

    private fun buildLoseFallback(calories: Int?, activityCategory: ActivityCategory): String {
        val reference = calories?.let { "Tu calculadora mostró una referencia aproximada de $it kcal al día. " } ?: ""
        val activityText = if (activityCategory == ActivityCategory.SEDENTARY) {
            "Como también tienes poca actividad, puede ayudarte combinar estos cambios con caminatas cortas en lugar de depender solo de reducir comida. "
        } else ""

        return reference +
                "De acuerdo con tu perfil, prioriza verduras, frutas enteras, leguminosas y una fuente de proteína en tus comidas. Modera refrescos, pan dulce, galletas, frituras y otras harinas refinadas; " +
                activityText +
                "evita ayunos o dietas extremas."
    }

    private fun buildGainFallback(calories: Int?, activityCategory: ActivityCategory): String {
        val reference = calories?.let { "Tu calculadora mostró una referencia aproximada de $it kcal al día. " } ?: ""
        val activityText = if (
            activityCategory == ActivityCategory.ACTIVE ||
            activityCategory == ActivityCategory.MODERATE
        ) {
            "Como realizas más actividad, una colación adicional puede ser especialmente práctica. "
        } else ""

        return reference +
                "De acuerdo con tu perfil, puedes aumentar poco a poco alimentos como avena, arroz, tortilla, aguacate, nueces, lácteos o leguminosas. " +
                activityText +
                "Por ejemplo, prueba yogurt con fruta o pan integral con crema de cacahuate entre comidas."
    }

    private fun buildGeneralFallback(calories: Int?): String {
        val reference = calories?.let { "Tu referencia de mantenimiento en la app es de aproximadamente $it kcal al día. " } ?: ""
        return reference +
                "De acuerdo con tu perfil, procura combinar verduras o fruta, una fuente de proteína y alimentos como tortilla, arroz, avena o leguminosas. Modera bebidas azucaradas y harinas refinadas, y alterna la carne roja con otras fuentes de proteína."
    }
}
