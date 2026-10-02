package com.example.vibralavida.ia

// ============================================================================
// MOTOR LOCAL DE ACTIVIDAD FÍSICA - FASE 11
// ============================================================================
// Usa edad y nivel de actividad para dar sugerencias concretas.
// No prescribe ejercicio médico ni aumenta intensidad por peso corporal.
// ============================================================================

object ActivityRecommendationEngine {

    fun build(userContext: AiUserContext): ActivityRecommendation {
        val profile = RecommendationProfileInterpreter.interpret(userContext)

        if (profile.isMinor) {
            return ActivityRecommendation(
                suggestions = listOf(
                    "caminar, bailar o andar en bicicleta de forma recreativa entre 15 y 20 minutos",
                    "hacer pausas activas de 3 a 5 minutos después de periodos largos sentado",
                    "realizar ejercicios sencillos con el propio peso a un ritmo cómodo"
                ),
                fallbackResponse =
                    "De acuerdo con tu perfil, puedes empezar con 15 a 20 minutos de una actividad que disfrutes, como caminar, bailar o andar en bicicleta. También puedes incluir pausas activas y movimientos sencillos con tu propio peso, siempre de forma progresiva y sin enfocarte en bajar peso."
            )
        }

        if (profile.isOlderAdult) {
            return ActivityRecommendation(
                suggestions = listOf(
                    "caminar entre 10 y 20 minutos a un ritmo cómodo",
                    "hacer 1 o 2 series de 6 a 10 levantamientos desde una silla estable",
                    "practicar movimientos suaves de hombros, tobillos y cadera",
                    "hacer ejercicios sencillos de equilibrio cerca de un apoyo estable"
                ),
                fallbackResponse =
                    "De acuerdo con tu edad, podrías priorizar caminatas de 10 a 20 minutos, levantarte de una silla varias veces y hacer movilidad suave. Si haces ejercicios de equilibrio, ten cerca una superficie estable y detente si aparece dolor, mareo o falta de aire inusual."
            )
        }

        return when (profile.activityCategory) {
            ActivityCategory.SEDENTARY -> ActivityRecommendation(
                suggestions = listOf(
                    "caminar 10 a 15 minutos al aire libre o dentro de casa a un ritmo cómodo",
                    "hacer 1 o 2 series de 8 a 10 sentadillas a tu ritmo",
                    "hacer 1 o 2 series de 8 a 10 flexiones contra la pared",
                    "realizar 5 minutos de movilidad de hombros, cadera y tobillos"
                ),
                fallbackResponse =
                    "De acuerdo con tu nivel de actividad, podrías comenzar con una caminata de 10 a 15 minutos y después hacer 1 o 2 series de 8 a 10 sentadillas a tu ritmo. También puedes agregar flexiones contra la pared o 5 minutos de movilidad; aumenta poco a poco cuando se vuelva fácil."
            )

            ActivityCategory.LIGHT -> ActivityRecommendation(
                suggestions = listOf(
                    "caminar 20 minutos a paso cómodo o ligeramente rápido",
                    "hacer 2 series de 8 a 12 sentadillas",
                    "hacer 2 series de 8 a 12 flexiones contra la pared o una superficie elevada",
                    "subir escaleras durante 3 a 5 minutos si te resulta cómodo"
                ),
                fallbackResponse =
                    "Como tu perfil indica actividad ligera, puedes probar una caminata de unos 20 minutos y complementar con 2 series de sentadillas y flexiones contra la pared. Si te resulta cómodo, también puedes usar escaleras durante unos minutos y aumentar gradualmente."
            )

            ActivityCategory.MODERATE -> ActivityRecommendation(
                suggestions = listOf(
                    "caminar a paso rápido entre 20 y 30 minutos",
                    "hacer 2 o 3 series de 10 a 12 sentadillas",
                    "alternar sentadillas, desplantes cortos y flexiones contra pared",
                    "saltar la cuerda en intervalos de 30 a 60 segundos, hasta completar unos 5 minutos, solo si te resulta cómodo y no hay dolor"
                ),
                fallbackResponse =
                    "De acuerdo con tu nivel moderado de actividad, puedes combinar una caminata rápida de 20 a 30 minutos con 2 o 3 series de sentadillas o desplantes cortos. Si te resulta cómodo y no hay dolor, también puedes probar cuerda en intervalos cortos hasta acumular unos 5 minutos."
            )

            ActivityCategory.ACTIVE -> ActivityRecommendation(
                suggestions = listOf(
                    "combinar caminata rápida, bicicleta o trote suave según tu preferencia",
                    "hacer circuitos cortos con sentadillas, desplantes y empujes",
                    "alternar sesiones exigentes con días de actividad ligera y movilidad",
                    "evitar aumentar al mismo tiempo duración, intensidad y volumen"
                ),
                fallbackResponse =
                    "Como ya tienes un nivel activo, puedes alternar actividad aeróbica con ejercicios de fuerza usando tu propio peso. Procura variar la carga entre días y aumentar solo un aspecto a la vez, por ejemplo duración o repeticiones."
            )

            ActivityCategory.UNKNOWN -> ActivityRecommendation(
                suggestions = listOf(
                    "caminar entre 10 y 15 minutos",
                    "hacer 1 serie de 8 a 10 sentadillas a ritmo cómodo",
                    "realizar 5 minutos de movilidad suave"
                ),
                fallbackResponse =
                    "No tengo un nivel de actividad suficientemente claro en tu perfil, así que te recomiendo comenzar de forma conservadora: 10 a 15 minutos de caminata, una serie corta de sentadillas y unos minutos de movilidad. Si te resulta fácil, aumenta gradualmente."
            )
        }
    }

    fun buildRewritePrompt(recommendation: ActivityRecommendation): String {
        return """
            Reescribe como Nuby en máximo 3 oraciones.
            Di \"de acuerdo con tu perfil\" cuando sea natural.
            Incluye 2 o 3 acciones concretas.

            Opciones permitidas:
            ${recommendation.suggestions.joinToString("; ")}

            Reglas:
            - No inventes ejercicios adicionales.
            - No presentes esto como rutina médica.
            - No prometas resultados.
            - No uses el peso corporal para aumentar intensidad.
            - Mantén progresión gradual.
        """.trimIndent()
    }
}
