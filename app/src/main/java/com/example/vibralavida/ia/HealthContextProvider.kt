package com.example.vibralavida.ia

import android.content.Context

import androidx.health.connect.client.HealthConnectClient

import com.example.vibralavida.trastornos_ritmo.HealthConnectManager


// ============================================================================
// PROVEEDOR SELECTIVO DE HEALTH CONNECT
// ============================================================================
//
// Usa el HealthConnectManager que YA existe en Vibra la vida.
//
// No solicita permisos desde Nuby.
// Si no hay permisos, simplemente devuelve el contexto vacío.
//
// ============================================================================

class HealthContextProvider(
    context: Context
) {


    private val manager =
        HealthConnectManager(
            context.applicationContext
        )


    suspend fun read(
        requestedTypes: Set<HealthContextType>
    ): HealthContextSnapshot {


        // --------------------------------------------------------------------
        // SI LA PREGUNTA NO NECESITA HEALTH CONNECT
        // --------------------------------------------------------------------

        if (
            requestedTypes.isEmpty()
        ) {

            return HealthContextSnapshot()
        }


        // --------------------------------------------------------------------
        // DISPONIBILIDAD
        // --------------------------------------------------------------------

        val available =
            try {

                manager.getAvailabilityStatus() ==
                        HealthConnectClient.SDK_AVAILABLE

            } catch (
                _: Throwable
            ) {

                false
            }


        if (
            !available
        ) {

            return HealthContextSnapshot(
                healthConnectAvailable =
                    false
            )
        }


        // --------------------------------------------------------------------
        // PERMISOS
        // --------------------------------------------------------------------

        val hasPermissions =
            try {

                manager.hasAllPermissions()

            } catch (
                _: Throwable
            ) {

                false
            }


        if (
            !hasPermissions
        ) {

            return HealthContextSnapshot(

                healthConnectAvailable =
                    true,

                permissionsGranted =
                    false
            )
        }


        // --------------------------------------------------------------------
        // PASOS
        // --------------------------------------------------------------------

        val steps =
            if (
                HealthContextType.STEPS in
                requestedTypes
            ) {

                try {

                    manager.readTodaySteps()

                } catch (
                    _: Throwable
                ) {

                    null
                }

            } else {

                null
            }


        // --------------------------------------------------------------------
        // FRECUENCIA CARDÍACA
        // --------------------------------------------------------------------

        val heartRate =
            if (
                HealthContextType.HEART_RATE in
                requestedTypes
            ) {

                try {

                    manager
                        .readLatestHeartRateFromLastDays(
                            30
                        )

                } catch (
                    _: Throwable
                ) {

                    null
                }

            } else {

                null
            }


        // --------------------------------------------------------------------
        // SUEÑO
        // --------------------------------------------------------------------

        val sleepSummary =
            if (
                HealthContextType.SLEEP in
                requestedTypes
            ) {

                try {

                    manager
                        .readLastSleepSummaryFromLastDays(
                            30
                        )

                } catch (
                    _: Throwable
                ) {

                    null
                }

            } else {

                null
            }


        return HealthContextSnapshot(

            healthConnectAvailable =
                true,

            permissionsGranted =
                true,

            stepsToday =
                steps,

            latestHeartRateBpm =
                heartRate,

            sleepTotalMinutes =
                sleepSummary
                    ?.totalMinutes,

            lightSleepMinutes =
                sleepSummary
                    ?.lightSleepMinutes,

            deepSleepMinutes =
                sleepSummary
                    ?.deepSleepMinutes,

            remSleepMinutes =
                sleepSummary
                    ?.remSleepMinutes
        )
    }
}
