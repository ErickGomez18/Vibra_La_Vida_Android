package com.example.vibralavida.habitos_saludables

import kotlin.math.abs
import kotlin.math.log10
import kotlin.math.sqrt


// ============================================================================
// RESULTADO DE UN BLOQUE DE AUDIO
// ============================================================================

/**
 * Resultado del análisis de una pequeña ventana de audio.
 *
 * IMPORTANTE:
 *
 * Esto NO diagnostica ronquidos ni apnea.
 * Únicamente identifica señales acústicas que,
 * por intensidad y patrón, podrían ser compatibles
 * con un episodio de ronquido.
 */
data class AudioAnalysisResult(

    // Intensidad aproximada del bloque.
    val rmsDb: Double,

    // Número aproximado de cruces por cero.
    val zeroCrossingRate: Double,

    // ¿El bloque parece compatible con ronquido?
    val possibleSnore: Boolean
)


// ============================================================================
// ANALIZADOR
// ============================================================================

class SnoreAnalyzer {

    /**
     * Umbral experimental.
     *
     * NO representa decibeles ambientales reales calibrados.
     *
     * Es una medida relativa calculada con las muestras PCM
     * capturadas por el teléfono.
     */
    private val minimumRelativeDb =
        -35.0


    /**
     * El ronquido normalmente contiene componentes
     * de frecuencia relativamente bajas.
     *
     * Usamos Zero Crossing Rate como un filtro MUY básico
     * para evitar clasificar cualquier sonido fuerte.
     *
     * Más adelante podremos sustituir esto por FFT
     * o un modelo de clasificación.
     */
    private val maximumZeroCrossingRate =
        0.18


    // ========================================================================
    // ANALIZAR
    // ========================================================================

    fun analyze(
        samples: ShortArray,
        validSamples: Int
    ): AudioAnalysisResult {


        if (
            validSamples <= 0
        ) {

            return AudioAnalysisResult(

                rmsDb =
                    -100.0,

                zeroCrossingRate =
                    0.0,

                possibleSnore =
                    false
            )
        }


        // ====================================================================
        // RMS
        // ====================================================================

        var sumSquares =
            0.0


        for (
        index in 0 until validSamples
        ) {

            val normalized =
                samples[index] /
                        Short.MAX_VALUE.toDouble()


            sumSquares +=
                normalized *
                        normalized
        }


        val rms =
            sqrt(
                sumSquares /
                        validSamples
            )


        // Evitamos log10(0).
        val safeRms =
            if (
                rms <= 0.000001
            ) {

                0.000001

            } else {

                rms
            }


        val rmsDb =
            20.0 *
                    log10(
                        safeRms
                    )


        // ====================================================================
        // ZERO CROSSING RATE
        // ====================================================================

        var zeroCrossings =
            0


        for (
        index in 1 until validSamples
        ) {

            val previous =
                samples[index - 1]


            val current =
                samples[index]


            if (
                (
                        previous < 0 &&
                                current >= 0
                        ) ||
                (
                        previous >= 0 &&
                                current < 0
                        )
            ) {

                zeroCrossings++
            }
        }


        val zeroCrossingRate =
            zeroCrossings.toDouble() /
                    validSamples.toDouble()


        // ====================================================================
        // CLASIFICACIÓN EXPERIMENTAL
        // ====================================================================

        val possibleSnore =
            rmsDb >=
                    minimumRelativeDb &&
                    zeroCrossingRate <=
                    maximumZeroCrossingRate


        return AudioAnalysisResult(

            rmsDb =
                rmsDb,

            zeroCrossingRate =
                zeroCrossingRate,

            possibleSnore =
                possibleSnore
        )
    }
}