package com.example.vibralavida.ia

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.Voice
import java.util.Locale

class NubyTtsManager(
    context: Context
) : TextToSpeech.OnInitListener {

    private val appContext =
        context.applicationContext

    private var textToSpeech: TextToSpeech? = null

    private var ready =
        false

    init {

        textToSpeech =
            TextToSpeech(
                appContext,
                this
            )
    }

    override fun onInit(
        status: Int
    ) {

        if (
            status != TextToSpeech.SUCCESS
        ) {

            ready =
                false

            return
        }

        val tts =
            textToSpeech
                ?: return

        val locale =
            Locale.forLanguageTag(
                "es-MX"
            )

        val result =
            tts.setLanguage(
                locale
            )

        ready =
            result != TextToSpeech.LANG_MISSING_DATA &&
            result != TextToSpeech.LANG_NOT_SUPPORTED

        tts.setSpeechRate(
            0.92f
        )

        tts.setPitch(
            1.05f
        )

        seleccionarMejorVoz(
            tts,
            locale
        )
    }

    private fun seleccionarMejorVoz(
        tts: TextToSpeech,
        locale: Locale
    ) {

        val voces =
            tts.voices
                ?: return

        val mejorVoz =
            voces
                .filter { voice ->

                    voice.locale.language ==
                            locale.language
                }
                .sortedWith(
                    compareByDescending<Voice> {
                        !it.isNetworkConnectionRequired
                    }.thenByDescending {
                        it.quality
                    }
                )
                .firstOrNull()

        if (
            mejorVoz != null
        ) {

            tts.voice =
                mejorVoz
        }
    }

    fun speak(
        text: String
    ) {

        if (
            !ready ||
            text.isBlank()
        ) {

            return
        }

        textToSpeech?.speak(
            text,
            TextToSpeech.QUEUE_FLUSH,
            null,
            "nuby_tts"
        )
    }

    fun shutdown() {

        textToSpeech?.stop()
        textToSpeech?.shutdown()

        textToSpeech =
            null

        ready =
            false
    }
}
