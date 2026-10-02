package com.example.vibralavida.ia


// ============================================================================
// FORMATEADOR DE HEALTH CONNECT
// ============================================================================
//
// Mantiene el contexto corto para Gemma 270M.
//
// ============================================================================

object HealthContextFormatter {


    fun format(
        snapshot: HealthContextSnapshot,
        requestedTypes: Set<HealthContextType>
    ): String {


        if (
            requestedTypes.isEmpty()
        ) {

            return ""
        }


        if (
            !snapshot.healthConnectAvailable
        ) {

            return "HEALTH CONNECT: no disponible."
        }


        if (
            !snapshot.permissionsGranted
        ) {

            return "HEALTH CONNECT: sin permisos de lectura."
        }


        val parts =
            mutableListOf<String>()


        if (
            HealthContextType.STEPS in requestedTypes
        ) {

            snapshot.stepsToday
                ?.let {

                    parts.add(
                        "Pasos hoy: $it"
                    )
                }
        }


        if (
            HealthContextType.SLEEP in requestedTypes
        ) {

            snapshot.sleepTotalMinutes
                ?.let {

                    val hours =
                        it / 60

                    val minutes =
                        it % 60


                    parts.add(
                        "Último sueño: ${hours}h ${minutes}min"
                    )
                }
        }


        if (
            HealthContextType.HEART_RATE in requestedTypes
        ) {

            snapshot.latestHeartRateBpm
                ?.let {

                    parts.add(
                        "FC reciente: $it lpm"
                    )
                }
        }


        return if (
            parts.isEmpty()
        ) {

            "HEALTH CONNECT: no hay datos recientes disponibles."

        } else {

            "HEALTH CONNECT: " +
                    parts.joinToString(
                        " | "
                    )
        }
    }
}
