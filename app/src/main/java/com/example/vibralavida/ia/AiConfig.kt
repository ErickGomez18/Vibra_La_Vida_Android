package com.example.vibralavida.ia

import android.content.Context
import java.io.File


// ============================================================================
// AI CONFIG - NUBY
// ============================================================================
//
// FASE 8
//
// Cambiamos de:
//
// Gemma 3 1B
//
// a:
//
// Gemma 3 270M Instruct
//
// Objetivo:
// reducir de forma importante la latencia y el consumo de memoria en teléfonos
// de gama media.
//
// Modelo recomendado para esta prueba:
//
// gemma3-270m-it-q8.litertlm
//
// ============================================================================

object AiConfig {


    // ========================================================================
    // NOMBRE DEL MODELO
    // ========================================================================

    const val MODEL_FILE_NAME =
        "gemma3-270m-it-q8.litertlm"


    // ========================================================================
    // CARPETA DE MODELOS
    // ========================================================================

    private const val MODELS_FOLDER =
        "models"


    // ========================================================================
    // OBTENER CARPETA
    // ========================================================================

    fun getModelsDirectory(
        context: Context
    ): File {


        val externalFilesDirectory =
            context.getExternalFilesDir(
                null
            )
                ?: context.filesDir


        return File(
            externalFilesDirectory,
            MODELS_FOLDER
        )
    }


    // ========================================================================
    // OBTENER ARCHIVO DEL MODELO
    // ========================================================================

    fun getModelFile(
        context: Context
    ): File {


        return File(

            getModelsDirectory(
                context
            ),

            MODEL_FILE_NAME
        )
    }


    // ========================================================================
    // COMPROBAR INSTALACIÓN
    // ========================================================================

    fun isModelInstalled(
        context: Context
    ): Boolean {


        val modelFile =
            getModelFile(
                context
            )


        return modelFile.exists() &&
                modelFile.isFile &&
                modelFile.length() > 0L
    }


    // ========================================================================
    // RUTA ESPERADA
    // ========================================================================

    fun getExpectedModelPath(
        context: Context
    ): String {


        return getModelFile(
            context
        ).absolutePath
    }
}
