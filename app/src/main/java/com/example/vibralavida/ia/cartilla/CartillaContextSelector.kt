package com.example.vibralavida.ia.cartilla

import java.text.Normalizer
import java.util.Locale

// ============================================================================
// SELECTOR TRANSPARENTE DE TEMA
// ============================================================================
//
// No usa el LLM para decidir la fuente.
// Clasifica la pregunta con reglas simples y auditables.
//
// ============================================================================

object CartillaContextSelector {

    fun selectTopic(
        question: String
    ): CartillaTopic {

        val text = normalize(question)

        fun any(vararg words: String): Boolean =
            words.any { text.contains(it) }

        return when {

            any("bebe", "lactancia", "amamantar", "alimentacion complementaria", "crianza") ->
                CartillaTopic.LACTANCIA_CRIANZA

            any("sueno seguro", "muerte de cuna", "boca arriba") ->
                CartillaTopic.SUENO_SEGURO

            any("desarrollo infantil", "desarrollo", "lenguaje", "estimulacion", "camina", "habla") ->
                CartillaTopic.DESARROLLO_INFANTIL

            any("signo de alarma", "alarma", "urgencia", "emergencia", "fiebre", "dificultad para respirar") ->
                CartillaTopic.SIGNOS_ALARMA

            any("comer", "comida", "alimentacion", "alimento", "dieta", "desayuno",
                "cena", "calorias", "peso", "nutricion", "imc") ->
                CartillaTopic.ALIMENTACION

            any("ejercicio", "actividad fisica", "caminar", "pasos", "correr",
                "deporte", "sedentario", "sedentarismo") ->
                CartillaTopic.ACTIVIDAD_FISICA

            any("dormir", "sueno", "descanso", "insomnio") ->
                CartillaTopic.SUENO

            any("ansiedad", "estres", "depresion", "triste", "emocion",
                "salud mental", "animo", "panico") ->
                CartillaTopic.SALUD_MENTAL

            any("alcohol", "tabaco", "cigarro", "vape", "nicotina",
                "droga", "adiccion") ->
                CartillaTopic.PREVENCION_ADICCIONES

            any("menstruacion", "periodo", "regla", "ciclo menstrual",
                "menstrual", "menopausia", "climaterio") ->
                CartillaTopic.SALUD_MENSTRUAL

            any("sexual", "sexo", "condon", "embarazo", "anticonceptivo",
                "its", "vih", "consentimiento") ->
                CartillaTopic.SALUD_SEXUAL_REPRODUCTIVA

            any("dientes", "dental", "cepillado", "caries", "encia",
                "salud bucal", "hilo dental") ->
                CartillaTopic.SALUD_BUCAL

            any("vista", "vision", "ojo", "visual") ->
                CartillaTopic.SALUD_VISUAL

            any("oir", "audicion", "oido", "auditiva") ->
                CartillaTopic.SALUD_AUDITIVA

            any("vacuna", "vacunacion", "vph", "tetanos", "hepatitis") ->
                CartillaTopic.VACUNACION

            any("diabetes", "hipertension", "colesterol", "glucosa",
                "dislipidemia", "enfermedad cronica") ->
                CartillaTopic.ENFERMEDADES_CRONICAS

            any("cancer", "mastografia", "papanicolaou", "prostata") ->
                CartillaTopic.CANCER

            any("violencia", "acoso", "maltrato", "agresion", "ciberacoso") ->
                CartillaTopic.PREVENCION_VIOLENCIAS

            any("caida", "equilibrio") ->
                CartillaTopic.PREVENCION_CAIDAS

            any("accidente", "lesion", "quemadura", "ahogamiento",
                "envenenamiento", "asfixia") ->
                CartillaTopic.PREVENCION_ACCIDENTES

            any("envejecimiento", "funcionalidad", "deterioro cognitivo") ->
                CartillaTopic.ENVEJECIMIENTO_FUNCIONALIDAD

            any("higiene", "aseo") ->
                CartillaTopic.HIGIENE_PERSONAL

            any("vivienda", "entorno", "escuela", "comunidad", "red de apoyo") ->
                CartillaTopic.ENTORNOS_SALUDABLES

            any("deteccion", "tamiz", "revision", "medicion") ->
                CartillaTopic.DETECCION_RIESGOS

            else ->
                CartillaTopic.GENERAL
        }
    }

    private fun normalize(
        value: String
    ): String {

        val normalized =
            Normalizer.normalize(
                value.lowercase(Locale.ROOT),
                Normalizer.Form.NFD
            )

        return normalized
            .replace("\\p{Mn}+".toRegex(), "")
            .trim()
    }
}
