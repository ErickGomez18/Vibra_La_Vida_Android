package com.example.vibralavida.ia

import android.content.Context
import android.util.Log

import com.example.vibralavida.api.nuby.NubyReferralRepository
import com.example.vibralavida.ia.cartilla.CartillaAgeSelector
import com.example.vibralavida.ia.cartilla.CartillaContextSelector
import com.example.vibralavida.ia.cartilla.CartillaPromptFormatter
import com.example.vibralavida.ia.cartilla.CartillaRepository

import java.text.Normalizer
import java.util.Locale

class NubyChatRepository(
    context: Context
) {

    companion object {
        private const val TAG = "NubyPerf"
    }

    private val appContext =
        context.applicationContext

    private val aiManager =
        AiManager(appContext)

    // ========================================================================
    // HEALTH CONNECT SELECTIVO
    // ========================================================================

    private val healthContextProvider =
        HealthContextProvider(
            appContext
        )

    private val insistenceCounts =
        mutableMapOf<SafetyIntent, Int>()

    suspend fun initialize() {
        aiManager.initialize()
    }

    suspend fun ask(
        question: String,
        age: String,
        gender: String,
        weight: String,
        height: String,
        activityLevel: String,
        caloriesLose: Int? = null,
        caloriesMaintain: Int? = null,
        caloriesGain: Int? = null
    ): NubyAnswer {

        val cleanQuestion =
            question.trim()

        if (cleanQuestion.isBlank()) {
            throw IllegalArgumentException("La pregunta está vacía.")
        }

        val simpleResponse =
            getSimpleConversationResponse(cleanQuestion)

        if (simpleResponse != null) {
            return NubyAnswer(text = simpleResponse)
        }

        val userContext =
            AiContextProvider.create(
                age = age,
                gender = gender,
                weight = weight,
                height = height,
                activityLevel = activityLevel,
                caloriesLose = caloriesLose,
                caloriesMaintain = caloriesMaintain,
                caloriesGain = caloriesGain
            )

        // ====================================================================
        // HEALTH CONNECT - SELECCIONAR SOLO LO NECESARIO
        // ====================================================================

        val requestedHealthTypes =
            HealthContextSelector.select(
                cleanQuestion
            )

        val healthSnapshot =
            healthContextProvider.read(
                requestedHealthTypes
            )

        val safetyIntent =
            SafetyIntentDetector.detect(cleanQuestion)

        if (safetyIntent != SafetyIntent.NONE) {
            val count =
                (insistenceCounts[safetyIntent] ?: 0) + 1

            insistenceCounts[safetyIntent] =
                count

            val shouldRefer =
                SafetyIntentDetector.shouldReferImmediately(safetyIntent) ||
                        (
                                (
                                        safetyIntent == SafetyIntent.CLINICAL_DIET ||
                                                safetyIntent == SafetyIntent.CLINICAL_EXERCISE ||
                                                safetyIntent == SafetyIntent.MENTAL_HEALTH
                                        ) &&
                                        count >= 2
                                )

            if (shouldRefer) {
                val specialty =
                    SafetyIntentDetector.suggestedSpecialty(
                        intent = safetyIntent,
                        question = cleanQuestion
                    )

                if (specialty != null) {
                    val specialists =
                        NubyReferralRepository.buscarEspecialistasSuspend(
                            especialidad = specialty.apiValue,
                            limit = 3
                        )

                    return NubyAnswer(
                        text =
                            buildReferralResponse(
                                intent = safetyIntent,
                                specialistsFound = specialists.isNotEmpty()
                            ),
                        specialists = specialists
                    )
                }
            }
        }

        val naturalIntent =
            AiNaturalIntentDetector.detect(cleanQuestion)

        Log.i(
            TAG,
            "Intención natural=$naturalIntent"
        )

        // ====================================================================
        // PREGUNTAS DIRECTAS DE HEALTH CONNECT
        // ====================================================================
        //
        // Ejemplos:
        // - ¿Cuántos pasos llevo hoy?
        // - ¿Cuánto dormí?
        // - ¿Cuál fue mi frecuencia cardiaca?
        //
        // Se responden localmente, sin pedirle a Gemma que interprete
        // clínicamente una lectura aislada.
        //
        // ====================================================================

        val directHealthAnswer =
            HealthLocalResponseEngine
                .answerDirectMetricQuestion(
                    question =
                        cleanQuestion,
                    requestedTypes =
                        requestedHealthTypes,
                    snapshot =
                        healthSnapshot
                )

        if (
            directHealthAnswer != null &&
            requestedHealthTypes.size == 1
        ) {

            return NubyAnswer(
                text =
                    directHealthAnswer
            )
        }

        if (naturalIntent == AiNaturalIntent.HYDRATION) {
            return NubyAnswer(
                text =
                    HydrationRecommendationEngine.build(userContext)
            )
        }

        if (naturalIntent == AiNaturalIntent.ACTIVITY) {

            // ================================================================
            // SI HAY DATOS DE HEALTH CONNECT, LOS USAMOS COMO CONTEXTO
            // ================================================================

            val useHealthContext =
                requestedHealthTypes.isNotEmpty() &&
                        healthSnapshot.permissionsGranted

            val response =
                if (
                    useHealthContext
                ) {

                    HealthAwareActivityEngine.build(
                        userContext =
                            userContext,
                        snapshot =
                            healthSnapshot
                    )

                } else {

                    val recommendation =
                        ActivityRecommendationEngine.build(
                            userContext
                        )

                    try {

                        aiManager.sendMessage(
                            ActivityRecommendationEngine.buildRewritePrompt(
                                recommendation
                            )
                        )

                    } catch (
                        error: Throwable
                    ) {

                        Log.e(
                            TAG,
                            "Gemma no pudo redactar actividad. Fallback local.",
                            error
                        )

                        recommendation.fallbackResponse
                    }
                }

            val topic =
                CartillaContextSelector.selectTopic(
                    cleanQuestion
                )

            val cartillaContext =
                if (
                    topic.id == "actividad_fisica"
                ) {

                    getCartillaContext(
                        userContext =
                            userContext,
                        topic =
                            topic
                    )

                } else {

                    null
                }

            return NubyAnswer(
                text =
                    response,
                sourceName =
                    cartillaContext?.sourceName,
                sourcePages =
                    cartillaContext?.sourcePages ?: emptyList()
            )
        }

        val topic =
            CartillaContextSelector.selectTopic(cleanQuestion)

        val selectedTypes =
            AiContextSelector.select(cleanQuestion)

        val isNutritionQuestion =
            naturalIntent == AiNaturalIntent.NUTRITION ||
                    topic.id == "alimentacion" ||
                    AiContextType.CALORIES in selectedTypes

        if (isNutritionQuestion) {
            val recommendation =
                NutritionRecommendationEngine.build(
                    question = cleanQuestion,
                    userContext = userContext
                )

            val response =
                try {
                    aiManager.sendMessage(
                        NutritionRecommendationEngine.buildRewritePrompt(
                            recommendation
                        )
                    )
                } catch (error: Throwable) {
                    recommendation.fallbackResponse
                }

            val cartillaContext =
                if (topic.id == "alimentacion") {
                    getCartillaContext(
                        userContext = userContext,
                        topic = topic
                    )
                } else {
                    null
                }

            return NubyAnswer(
                text = response,
                sourceName = cartillaContext?.sourceName,
                sourcePages = cartillaContext?.sourcePages ?: emptyList()
            )
        }

        val personalContext =
            AiContextProvider.formatSelectedContext(
                userContext = userContext,
                selectedTypes = selectedTypes
            )

        val cartillaContext =
            getCartillaContext(
                userContext = userContext,
                topic = topic
            )

        val officialContext =
            CartillaPromptFormatter.format(cartillaContext)

        // ====================================================================
        // CONTEXTO BREVE DE HEALTH CONNECT PARA PREGUNTAS GENERALES
        // ====================================================================

        val healthContextText =
            HealthContextFormatter.format(
                snapshot =
                    healthSnapshot,
                requestedTypes =
                    requestedHealthTypes
            )

        val prompt =
            buildString {
                if (personalContext.isNotBlank()) {
                    append(personalContext)
                    append("\n")
                }

                if (officialContext.isNotBlank()) {
                    append(officialContext)
                    append("\n")
                }

                if (healthContextText.isNotBlank()) {
                    append(healthContextText)
                    append("\n")
                }

                append("PREGUNTA: ")
                append(cleanQuestion)
                append("\nResponde en máximo 2 oraciones, de forma educativa y clara.")
            }

        val response =
            try {
                aiManager.sendMessage(prompt)
            } catch (error: Throwable) {
                buildGeneralFallback(topic.id)
            }

        return NubyAnswer(
            text = response,
            sourceName = cartillaContext?.sourceName,
            sourcePages = cartillaContext?.sourcePages ?: emptyList()
        )
    }

    private fun getCartillaContext(
        userContext: AiUserContext,
        topic: com.example.vibralavida.ia.cartilla.CartillaTopic
    ): com.example.vibralavida.ia.cartilla.CartillaContext? {

        val ageGroup =
            CartillaAgeSelector.select(
                age = userContext.age,
                gender = userContext.gender
            )

        return CartillaRepository.getContext(
            context = appContext,
            ageGroup = ageGroup,
            topic = topic,
            maxChunks = 1
        )
    }

    private fun buildReferralResponse(
        intent: SafetyIntent,
        specialistsFound: Boolean
    ): String {

        val reason =
            when (intent) {
                SafetyIntent.DIAGNOSIS ->
                    "Para saber con certeza qué tienes se necesita una valoración profesional."

                SafetyIntent.MEDICATION_OR_TREATMENT ->
                    "No es seguro que yo indique medicamentos, dosis o cambios de tratamiento."

                SafetyIntent.CLINICAL_DIET ->
                    "Para una dieta personalizada conviene que un profesional valore tus necesidades."

                SafetyIntent.CLINICAL_EXERCISE ->
                    "Para una rutina personalizada con condiciones específicas conviene una valoración profesional."

                SafetyIntent.MENTAL_HEALTH ->
                    "Si necesitas un apoyo más personalizado en salud emocional, un profesional puede orientarte mejor."

                SafetyIntent.CARDIOVASCULAR ->
                    "Los síntomas o dudas cardiovasculares necesitan una valoración profesional para interpretarse correctamente."

                SafetyIntent.GENERAL_MEDICAL ->
                    "Los síntomas necesitan una valoración profesional para determinar su causa."

                SafetyIntent.NONE ->
                    "Un profesional puede ayudarte con una valoración más completa."
            }

        return if (specialistsFound) {
            "$reason Encontré profesionales registrados en Vibra la vida que puedes consultar:"
        } else {
            "$reason En este momento no encontré profesionales registrados de esa especialidad disponibles en la app."
        }
    }

    private fun getSimpleConversationResponse(
        text: String
    ): String? {

        return when (normalize(text)) {
            "hola", "holaa", "holaaa" ->
                "¡Hola! Soy Nuby. ¿En qué tema de bienestar puedo ayudarte hoy?"

            "buenos dias" ->
                "¡Buenos días! ¿En qué puedo ayudarte hoy?"

            "buenas tardes" ->
                "¡Buenas tardes! ¿Qué te gustaría consultar?"

            "buenas noches" ->
                "¡Buenas noches! ¿En qué puedo orientarte?"

            "como estas", "que tal" ->
                "¡Muy bien! Estoy listo para ayudarte con tus dudas sobre hábitos y bienestar."

            "gracias", "muchas gracias" ->
                "¡Con gusto! Si tienes otra duda, aquí estoy."

            "ok", "oki", "va" ->
                "Perfecto. ¿Qué más te gustaría saber?"

            else ->
                null
        }
    }

    private fun buildGeneralFallback(
        topicId: String
    ): String {

        return when (topicId) {
            "sueno" ->
                "Dormir bien favorece la recuperación, la concentración y el bienestar diario. Procura horarios regulares y un ambiente tranquilo para dormir."

            "salud_mental" ->
                "Cuidar tu bienestar emocional incluye descansar, hablar con personas de confianza y pedir apoyo cuando lo necesites."

            else ->
                "Puedo darte orientación educativa general. Intenta hacerme una pregunta un poco más específica para ayudarte mejor."
        }
    }

    private fun normalize(
        value: String
    ): String {

        return Normalizer
            .normalize(
                value.lowercase(Locale.ROOT).trim(),
                Normalizer.Form.NFD
            )
            .replace("\\p{Mn}+".toRegex(), "")
            .replace("[¿?¡!.,]".toRegex(), "")
            .trim()
    }

    fun close() {
        aiManager.close()
    }
}
