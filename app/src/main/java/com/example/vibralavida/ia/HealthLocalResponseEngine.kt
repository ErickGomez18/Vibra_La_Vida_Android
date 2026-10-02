package com.example.vibralavida.ia


// ============================================================================
// RESPUESTAS LOCALES DE HEALTH CONNECT
// ============================================================================
//
// Responde preguntas directas sin pedirle a Gemma que interprete las métricas.
//
// Ejemplos:
// - pasos;
// - sueño;
// - frecuencia cardiaca.
//
// ============================================================================

object HealthLocalResponseEngine {


    fun answerDirectMetricQuestion(

        question: String,

        requestedTypes: Set<HealthContextType>,

        snapshot: HealthContextSnapshot

    ): String? {


        if (
            requestedTypes.isEmpty()
        ) {

            return null
        }


        if (
            !snapshot.healthConnectAvailable
        ) {

            return "No puedo consultar Health Connect en este dispositivo en este momento."
        }


        if (
            !snapshot.permissionsGranted
        ) {

            return "Health Connect está disponible, pero Nuby no tiene los permisos de lectura necesarios para consultar tus datos."
        }


        // --------------------------------------------------------------------
        // PASOS SOLAMENTE
        // --------------------------------------------------------------------

        if (
            requestedTypes ==
            setOf(
                HealthContextType.STEPS
            )
        ) {

            val steps =
                snapshot.stepsToday


            if (
                steps != null
            ) {

                return "Hoy Health Connect registra $steps pasos. Lo uso únicamente como referencia de actividad, no como una valoración médica."
            }


            return "No encontré pasos recientes disponibles en Health Connect."
        }


        // --------------------------------------------------------------------
        // FRECUENCIA CARDÍACA SOLAMENTE
        // --------------------------------------------------------------------

        if (
            requestedTypes ==
            setOf(
                HealthContextType.HEART_RATE
            )
        ) {

            val bpm =
                snapshot.latestHeartRateBpm


            if (
                bpm != null
            ) {

                return "La medición más reciente disponible en Health Connect es de $bpm latidos por minuto. Esta lectura es solo un dato de referencia y Nuby no la utiliza por sí sola para diagnosticar o decir si es normal o anormal."
            }


            return "No encontré una medición reciente de frecuencia cardiaca disponible en Health Connect."
        }


        // --------------------------------------------------------------------
        // SUEÑO SOLAMENTE
        // --------------------------------------------------------------------

        if (
            requestedTypes ==
            setOf(
                HealthContextType.SLEEP
            )
        ) {

            val totalMinutes =
                snapshot.sleepTotalMinutes


            if (
                totalMinutes != null
            ) {

                val hours =
                    totalMinutes / 60

                val minutes =
                    totalMinutes % 60


                return "La última sesión de sueño disponible en Health Connect registra aproximadamente ${hours} h ${minutes} min. Nuby usa este dato como contexto de bienestar y no como diagnóstico de calidad del sueño."
            }


            return "No encontré una sesión reciente de sueño disponible en Health Connect."
        }


        return null
    }
}
