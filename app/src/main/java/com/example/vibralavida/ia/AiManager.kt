package com.example.vibralavida.ia

import android.content.Context
import android.os.SystemClock
import android.util.Log

import com.google.ai.edge.litertlm.Backend
import com.google.ai.edge.litertlm.Contents
import com.google.ai.edge.litertlm.Conversation
import com.google.ai.edge.litertlm.ConversationConfig
import com.google.ai.edge.litertlm.Engine
import com.google.ai.edge.litertlm.EngineConfig
import com.google.ai.edge.litertlm.LogSeverity
import com.google.ai.edge.litertlm.SamplerConfig

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout


// ============================================================================
// AI MANAGER - GEMMA 3 270M
// ============================================================================
//
// FASE 9
//
// Gemma deja de ser el motor de decisión alimentaria.
//
// Ahora se usa principalmente como redactor.
//
// Por eso:
// - conversación nueva por pregunta;
// - contexto máximo 1024;
// - timeout reducido a 15 segundos;
// - si falla, Repository puede usar una respuesta local.
//
// ============================================================================

class AiManager(
    context: Context
) {


    companion object {

        private const val TAG =
            "NubyPerf"


        private const val RESPONSE_TIMEOUT_MS =
            15_000L
    }


    private val appContext =
        context.applicationContext


    private var engine:
            Engine? =
        null


    private var conversation:
            Conversation? =
        null


    @Volatile
    private var initialized =
        false


    // ========================================================================
    // INICIALIZAR
    // ========================================================================

    suspend fun initialize() {


        if (
            initialized
        ) {

            return
        }


        withContext(
            Dispatchers.Default
        ) {


            val modelFile =
                AiConfig.getModelFile(
                    appContext
                )


            if (
                !modelFile.exists()
            ) {

                throw IllegalStateException(
                    "No se encontró el modelo de Nuby en:\n${modelFile.absolutePath}"
                )
            }


            Engine.setNativeMinLogSeverity(
                LogSeverity.ERROR
            )


            val start =
                SystemClock.elapsedRealtime()


            val config =
                EngineConfig(

                    modelPath =
                        modelFile.absolutePath,

                    backend =
                        Backend.CPU(),

                    maxNumTokens =
                        1024,

                    cacheDir =
                        appContext.cacheDir.absolutePath
                )


            val newEngine =
                Engine(
                    config
                )


            newEngine.initialize()


            engine =
                newEngine


            initialized =
                true


            Log.i(
                TAG,
                "Gemma 270M listo en ${SystemClock.elapsedRealtime() - start} ms"
            )
        }
    }


    // ========================================================================
    // CONVERSACIÓN NUEVA
    // ========================================================================

    private fun createFreshConversation(): Conversation {


        try {

            conversation?.close()

        } catch (
            _: Throwable
        ) {
        }


        val currentEngine =
            engine
                ?: throw IllegalStateException(
                    "El motor de Nuby no está disponible."
                )


        val config =
            ConversationConfig(

                systemInstruction =
                    Contents.of(
                        AiPromptBuilder
                            .buildSystemInstruction()
                    ),

                samplerConfig =
                    SamplerConfig(

                        topK =
                            8,

                        topP =
                            0.85,

                        temperature =
                            0.40
                    )
            )


        val newConversation =
            currentEngine.createConversation(
                config
            )


        conversation =
            newConversation


        return newConversation
    }


    // ========================================================================
    // ENVIAR
    // ========================================================================

    suspend fun sendMessage(
        userMessage: String
    ): String {


        val cleanMessage =
            userMessage.trim()


        if (
            cleanMessage.isBlank()
        ) {

            throw IllegalArgumentException(
                "La pregunta está vacía."
            )
        }


        if (
            !initialized
        ) {

            initialize()
        }


        return withContext(
            Dispatchers.Default
        ) {


            val currentConversation =
                createFreshConversation()


            val start =
                SystemClock.elapsedRealtime()


            var firstChunk =
                false


            val response =
                StringBuilder()


            Log.i(
                TAG,
                "Gemma redactando. Prompt=${cleanMessage.length} caracteres"
            )


            try {


                withTimeout(
                    RESPONSE_TIMEOUT_MS
                ) {


                    currentConversation
                        .sendMessageAsync(
                            cleanMessage
                        )
                        .collect {
                                chunk ->


                            if (
                                !firstChunk
                            ) {


                                firstChunk =
                                    true


                                Log.i(
                                    TAG,
                                    "Primer fragmento=${SystemClock.elapsedRealtime() - start} ms"
                                )
                            }


                            response.append(
                                chunk.toString()
                            )
                        }
                }


            } catch (
                timeout: TimeoutCancellationException
            ) {


                try {

                    currentConversation.cancelProcess()

                } catch (
                    _: Throwable
                ) {
                }


                throw IllegalStateException(
                    "Gemma superó 15 segundos."
                )
            }


            Log.i(
                TAG,
                "Redacción total=${SystemClock.elapsedRealtime() - start} ms"
            )


            response
                .toString()
                .trim()
                .ifBlank {

                    throw IllegalStateException(
                        "Gemma devolvió una respuesta vacía."
                    )
                }
        }
    }


    fun isInitialized(): Boolean {

        return initialized
    }


    fun getBackendName(): String {

        return "CPU"
    }


    fun close() {


        try {

            conversation?.close()

        } catch (
            _: Throwable
        ) {
        }


        conversation =
            null


        try {

            engine?.close()

        } catch (
            _: Throwable
        ) {
        }


        engine =
            null


        initialized =
            false
    }
}
