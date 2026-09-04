package com.example.vibralavida.trastornos_ritmo

// ============================================================================
// ANDROID
// ============================================================================

import android.content.Context


// ============================================================================
// HEALTH CONNECT
// ============================================================================

import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.permission.HealthPermission

import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.records.SleepSessionRecord
import androidx.health.connect.client.records.StepsRecord

import androidx.health.connect.client.records.metadata.DataOrigin

import androidx.health.connect.client.request.AggregateRequest
import androidx.health.connect.client.request.ReadRecordsRequest

import androidx.health.connect.client.time.TimeRangeFilter


// ============================================================================
// FECHAS
// ============================================================================

import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId


// ============================================================================
// FRECUENCIA CARDÍACA
// ============================================================================

/**
 * Representa una medición individual de frecuencia cardíaca.
 */
data class HeartRatePoint(

    val time: Instant,

    val bpm: Long
)


// ============================================================================
// ETAPA DEL SUEÑO
// ============================================================================

/**
 * Representa una etapa registrada durante una sesión de sueño.
 */
data class SleepStagePoint(

    val startTime: Instant,

    val endTime: Instant,

    val stage: Int
) {

    /**
     * Duración de la etapa en minutos.
     */
    val durationMinutes: Long
        get() =
            Duration.between(
                startTime,
                endTime
            ).toMinutes()
}


// ============================================================================
// RESUMEN DE SUEÑO
// ============================================================================

/**
 * Contiene la información completa de una sesión de sueño.
 */
data class SleepSummary(

    val startTime: Instant,

    val endTime: Instant,

    val totalMinutes: Long,

    val stages: List<SleepStagePoint>
) {


    // ========================================================================
    // SUEÑO LIGERO
    // ========================================================================

    val lightSleepMinutes: Long
        get() =
            stages

                .filter {

                    it.stage ==
                            SleepSessionRecord.STAGE_TYPE_LIGHT
                }

                .sumOf {

                    it.durationMinutes
                }


    // ========================================================================
    // SUEÑO PROFUNDO
    // ========================================================================

    val deepSleepMinutes: Long
        get() =
            stages

                .filter {

                    it.stage ==
                            SleepSessionRecord.STAGE_TYPE_DEEP
                }

                .sumOf {

                    it.durationMinutes
                }


    // ========================================================================
    // REM
    // ========================================================================

    val remSleepMinutes: Long
        get() =
            stages

                .filter {

                    it.stage ==
                            SleepSessionRecord.STAGE_TYPE_REM
                }

                .sumOf {

                    it.durationMinutes
                }


    // ========================================================================
    // DESPIERTO
    // ========================================================================

    val awakeMinutes: Long
        get() =
            stages

                .filter {

                    it.stage ==
                            SleepSessionRecord.STAGE_TYPE_AWAKE ||

                            it.stage ==
                            SleepSessionRecord.STAGE_TYPE_AWAKE_IN_BED ||

                            it.stage ==
                            SleepSessionRecord.STAGE_TYPE_OUT_OF_BED
                }

                .sumOf {

                    it.durationMinutes
                }


    // ========================================================================
    // ¿EXISTEN ETAPAS?
    // ========================================================================

    val hasSleepStages: Boolean
        get() =
            stages.isNotEmpty()


    // ========================================================================
    // ¿MI FITNESS PROPORCIONÓ SUEÑO LIGERO?
    // ========================================================================

    val hasLightSleep: Boolean
        get() =
            stages.any {

                it.stage ==
                        SleepSessionRecord.STAGE_TYPE_LIGHT
            }


    // ========================================================================
    // ¿MI FITNESS PROPORCIONÓ SUEÑO PROFUNDO?
    // ========================================================================

    val hasDeepSleep: Boolean
        get() =
            stages.any {

                it.stage ==
                        SleepSessionRecord.STAGE_TYPE_DEEP
            }


    // ========================================================================
    // ¿MI FITNESS PROPORCIONÓ REM?
    // ========================================================================

    val hasRemSleep: Boolean
        get() =
            stages.any {

                it.stage ==
                        SleepSessionRecord.STAGE_TYPE_REM
            }


    // ========================================================================
    // ¿MI FITNESS PROPORCIONÓ DESPIERTO?
    // ========================================================================

    val hasAwakeData: Boolean
        get() =
            stages.any {

                it.stage ==
                        SleepSessionRecord.STAGE_TYPE_AWAKE ||

                        it.stage ==
                        SleepSessionRecord.STAGE_TYPE_AWAKE_IN_BED ||

                        it.stage ==
                        SleepSessionRecord.STAGE_TYPE_OUT_OF_BED
            }
}


// ============================================================================
// HEALTH CONNECT MANAGER
// ============================================================================

class HealthConnectManager(
    private val context: Context
) {


    // ========================================================================
    // FUENTE DEL RELOJ
    // ========================================================================

    /**
     * Mi Fitness.
     *
     * Esta app recibe los datos del reloj Xiaomi/Redmi
     * y posteriormente los comparte con Health Connect.
     */
    private val watchPackageName =
        "com.xiaomi.wearable"


    // ========================================================================
    // HEALTH CONNECT
    // ========================================================================

    private val healthConnectClient by lazy {

        HealthConnectClient.getOrCreate(
            context
        )
    }


    // ========================================================================
    // PERMISOS
    // ========================================================================

    val permissions =
        setOf(

            HealthPermission.getReadPermission(
                StepsRecord::class
            ),

            HealthPermission.getReadPermission(
                HeartRateRecord::class
            ),

            HealthPermission.getReadPermission(
                SleepSessionRecord::class
            )
        )


    // ========================================================================
    // DISPONIBILIDAD
    // ========================================================================

    fun getAvailabilityStatus(): Int {

        return HealthConnectClient.getSdkStatus(
            context
        )
    }


    // ========================================================================
    // COMPROBAR PERMISOS
    // ========================================================================

    suspend fun hasAllPermissions(): Boolean {

        val grantedPermissions =
            healthConnectClient
                .permissionController
                .getGrantedPermissions()


        return grantedPermissions.containsAll(
            permissions
        )
    }


    // =========================================================================
    // PASOS
    // =========================================================================


    // ========================================================================
    // PASOS DE HOY
    // ========================================================================

    suspend fun readTodaySteps(): Long {

        val zoneId =
            ZoneId.systemDefault()


        val startOfDay =
            LocalDate
                .now(zoneId)
                .atStartOfDay(zoneId)
                .toInstant()


        val now =
            Instant.now()


        return readStepsBetween(

            startTime =
                startOfDay,

            endTime =
                now
        )
    }


    // ========================================================================
    // PASOS DE LOS ÚLTIMOS DÍAS
    // ========================================================================

    suspend fun readStepsFromLastDays(
        days: Long = 30
    ): Long {

        val now =
            Instant.now()


        val startTime =
            now.minus(
                Duration.ofDays(
                    days
                )
            )


        return readStepsBetween(

            startTime =
                startTime,

            endTime =
                now
        )
    }


    // ========================================================================
    // LEER PASOS
    // ========================================================================

    /**
     * Solo cuenta pasos provenientes de Mi Fitness.
     */
    private suspend fun readStepsBetween(

        startTime: Instant,

        endTime: Instant

    ): Long {

        val response =
            healthConnectClient.aggregate(

                AggregateRequest(

                    metrics =
                        setOf(
                            StepsRecord.COUNT_TOTAL
                        ),

                    timeRangeFilter =
                        TimeRangeFilter.between(

                            startTime,

                            endTime
                        ),

                    dataOriginFilter =
                        setOf(

                            DataOrigin(
                                watchPackageName
                            )
                        )
                )
            )


        return response[
            StepsRecord.COUNT_TOTAL
        ] ?: 0L
    }


    // =========================================================================
    // FRECUENCIA CARDÍACA
    // =========================================================================


    // ========================================================================
    // ÚLTIMA FRECUENCIA
    // ========================================================================

    suspend fun readLatestHeartRateFromLastDays(
        days: Long = 30
    ): Long? {

        val now =
            Instant.now()


        val startTime =
            now.minus(
                Duration.ofDays(
                    days
                )
            )


        val response =
            healthConnectClient.readRecords(

                ReadRecordsRequest(

                    recordType =
                        HeartRateRecord::class,

                    timeRangeFilter =
                        TimeRangeFilter.between(

                            startTime,

                            now
                        )
                )
            )


        val latestSample =
            response.records

                // Solo Mi Fitness.
                .filter {

                    it.metadata
                        .dataOrigin
                        .packageName ==
                            watchPackageName
                }

                // Obtener muestras.
                .flatMap {

                    it.samples
                }

                // Buscar la última.
                .maxByOrNull {

                    it.time
                }


        return latestSample
            ?.beatsPerMinute
    }


    // ========================================================================
    // FRECUENCIA CARDÍACA DE HOY
    // ========================================================================

    suspend fun readTodayHeartRatePoints():
            List<HeartRatePoint> {

        val zoneId =
            ZoneId.systemDefault()


        val startOfDay =
            LocalDate
                .now(zoneId)
                .atStartOfDay(zoneId)
                .toInstant()


        val now =
            Instant.now()


        val response =
            healthConnectClient.readRecords(

                ReadRecordsRequest(

                    recordType =
                        HeartRateRecord::class,

                    timeRangeFilter =
                        TimeRangeFilter.between(

                            startOfDay,

                            now
                        )
                )
            )


        return response.records

            // Solo datos provenientes de Mi Fitness.
            .filter {

                it.metadata
                    .dataOrigin
                    .packageName ==
                        watchPackageName
            }

            // Convertimos las muestras.
            .flatMap { record ->

                record.samples.map { sample ->

                    HeartRatePoint(

                        time =
                            sample.time,

                        bpm =
                            sample.beatsPerMinute
                    )
                }
            }

            // Orden cronológico.
            .sortedBy {

                it.time
            }
    }


    // ========================================================================
    // MÍNIMA
    // ========================================================================

    suspend fun readTodayMinimumHeartRate():
            HeartRatePoint? {

        return readTodayHeartRatePoints()
            .minByOrNull {

                it.bpm
            }
    }


    // ========================================================================
    // MÁXIMA
    // ========================================================================

    suspend fun readTodayMaximumHeartRate():
            HeartRatePoint? {

        return readTodayHeartRatePoints()
            .maxByOrNull {

                it.bpm
            }
    }


    // =========================================================================
    // SUEÑO
    // =========================================================================


    // ========================================================================
    // ÚLTIMA SESIÓN COMPLETA
    // ========================================================================

    suspend fun readLastSleepSummaryFromLastDays(
        days: Long = 30
    ): SleepSummary? {

        val now =
            Instant.now()


        val startTime =
            now.minus(
                Duration.ofDays(
                    days
                )
            )


        val response =
            healthConnectClient.readRecords(

                ReadRecordsRequest(

                    recordType =
                        SleepSessionRecord::class,

                    timeRangeFilter =
                        TimeRangeFilter.between(

                            startTime,

                            now
                        )
                )
            )


        // ====================================================================
        // ÚLTIMA SESIÓN PROVENIENTE DE MI FITNESS
        // ====================================================================

        val latestSession =
            response.records

                .filter {

                    it.metadata
                        .dataOrigin
                        .packageName ==
                            watchPackageName
                }

                .maxByOrNull {

                    it.endTime
                }

                ?: return null


        // ====================================================================
        // DURACIÓN TOTAL
        // ====================================================================

        val totalMinutes =
            Duration.between(

                latestSession.startTime,

                latestSession.endTime

            ).toMinutes()


        // ====================================================================
        // ETAPAS
        // ====================================================================

        val stages =
            latestSession.stages

                .map { stage ->

                    SleepStagePoint(

                        startTime =
                            stage.startTime,

                        endTime =
                            stage.endTime,

                        stage =
                            stage.stage
                    )
                }

                .sortedBy {

                    it.startTime
                }


        // ====================================================================
        // RESULTADO
        // ====================================================================

        return SleepSummary(

            startTime =
                latestSession.startTime,

            endTime =
                latestSession.endTime,

            totalMinutes =
                totalMinutes,

            stages =
                stages
        )
    }


    // ========================================================================
    // SOLO DURACIÓN
    // ========================================================================

    /**
     * Se conserva porque otras partes de la app
     * podrían seguir utilizándola.
     */
    suspend fun readLastSleepMinutesFromLastDays(
        days: Long = 30
    ): Long? {

        return readLastSleepSummaryFromLastDays(
            days = days
        )?.totalMinutes
    }
}