package com.example.vibralavida.ia


// ============================================================================
// ACTIVIDAD CON CONTEXTO DE HEALTH CONNECT
// ============================================================================
//
// Complementa ActivityRecommendationEngine.
//
// No usa la frecuencia cardiaca para aumentar o reducir intensidad
// automáticamente.
//
// El dato de FC solamente se menciona si está disponible.
//
// ============================================================================

object HealthAwareActivityEngine {


    fun build(

        userContext: AiUserContext,

        snapshot: HealthContextSnapshot

    ): String {


        val base =
            ActivityRecommendationEngine
                .build(
                    userContext
                )
                .fallbackResponse


        val extras =
            mutableListOf<String>()


        snapshot.stepsToday
            ?.let {
                    steps ->


                extras.add(
                    "Hoy Health Connect registra $steps pasos."
                )
            }


        snapshot.sleepTotalMinutes
            ?.let {
                    sleepMinutes ->


                val hours =
                    sleepMinutes / 60

                val minutes =
                    sleepMinutes % 60


                extras.add(
                    "La última sesión de sueño registrada fue de aproximadamente ${hours} h ${minutes} min."
                )
            }


        snapshot.latestHeartRateBpm
            ?.let {
                    bpm ->


                extras.add(
                    "La frecuencia cardiaca más reciente disponible fue de $bpm lpm; la uso solo como referencia y no para ajustar automáticamente la intensidad."
                )
            }


        return if (
            extras.isEmpty()
        ) {

            base

        } else {

            extras.joinToString(
                " "
            ) +
                    " " +
                    base
        }
    }
}
