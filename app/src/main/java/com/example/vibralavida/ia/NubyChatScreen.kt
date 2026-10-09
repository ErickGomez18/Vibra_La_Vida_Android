package com.example.vibralavida.ia

import com.example.vibralavida.R
import com.example.vibralavida.backgroundGradient

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedAssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


// ============================================================================
// NUBY CHAT SCREEN
// ============================================================================
//
// FASE 5
//
// Nuby ya puede recibir selectivamente:
//
// - edad;
// - sexo/género;
// - peso;
// - estatura;
// - nivel de actividad;
// - IMC calculado localmente.
//
// El selector decide qué datos necesita cada pregunta.
//
// Los tres resultados reales de la calculadora de calorías pueden llegar
// desde MainActivity.
//
// Health Connect todavía NO se usa en esta fase.
//
// ============================================================================

@OptIn(
    ExperimentalLayoutApi::class
)
@Composable
fun NubyChatScreen(

    onBack: () -> Unit,

    onMenuClick: () -> Unit,

    userAge: String,

    userGender: String,

    userWeight: String,

    userHeight: String,

    userActivityLevel: String,

    userLoseCalories: Int? = null,

    userMaintainCalories: Int? = null,

    userGainCalories: Int? = null
) {


    // ========================================================================
    // CONTEXTO
    // ========================================================================

    val context =
        LocalContext.current


    // ========================================================================
    // COROUTINES
    // ========================================================================

    val scope =
        rememberCoroutineScope()


    // ========================================================================
    // LISTA
    // ========================================================================

    val listState =
        rememberLazyListState()


    // ========================================================================
    // REPOSITORY IA
    // ========================================================================

    val repository =
        remember {

            NubyChatRepository(
                context
            )
        }


    // ========================================================================
    // TTS
    // ========================================================================

    val ttsManager =
        remember {

            NubyTtsManager(
                context
            )
        }


    // ========================================================================
    // MENSAJES
    // ========================================================================

    val messages =
        remember {

            mutableStateListOf(

                NubyMessage(

                    text =
                        "¡Hola! Soy Nuby, tu guía interactiva de Vibra la vida. " +
                                "Puedes preguntarme sobre alimentación, actividad física, " +
                                "sueño y hábitos saludables.",

                    fromUser =
                        false
                )
            )
        }


    // ========================================================================
    // ESTADO
    // ========================================================================

    var userInput by
    remember {

        mutableStateOf(
            ""
        )
    }


    var isLoading by
    remember {

        mutableStateOf(
            false
        )
    }


    var modelReady by
    remember {

        mutableStateOf(
            false
        )
    }


    var statusMessage by
    remember {

        mutableStateOf(
            "Preparando a Nuby..."
        )
    }


    // ========================================================================
    // INICIALIZAR GEMMA
    // ========================================================================

    LaunchedEffect(
        Unit
    ) {


        if (
            !AiConfig.isModelInstalled(
                context
            )
        ) {

            modelReady =
                false


            statusMessage =
                "No encontré el modelo local."


            messages.add(

                NubyMessage(

                    text =
                        "Todavía no puedo iniciar mi inteligencia local. " +
                                "Falta el archivo del modelo en:\n\n" +
                                AiConfig.getExpectedModelPath(
                                    context
                                ),

                    fromUser =
                        false
                )
            )


            return@LaunchedEffect
        }


        try {


            statusMessage =
                "Cargando IA local..."


            repository.initialize()


            modelReady =
                true


            statusMessage =
                "Nuby está listo · IA local"


        } catch (
            e: Exception
        ) {


            modelReady =
                false


            statusMessage =
                "No fue posible iniciar la IA"


            messages.add(

                NubyMessage(

                    text =
                        "No pude iniciar el modelo local.\n\n" +
                                (
                                        e.message
                                            ?: "Error desconocido."
                                        ),

                    fromUser =
                        false
                )
            )
        }
    }


    // ========================================================================
    // SALUDO POR VOZ
    // ========================================================================

    LaunchedEffect(
        Unit
    ) {


        delay(
            1600
        )


        ttsManager.speak(
            "Hola, soy Nuby. Tu guía interactiva de Vibra la vida. ¿En qué puedo ayudarte hoy?"
        )
    }


    // ========================================================================
    // AUTO SCROLL
    // ========================================================================

    LaunchedEffect(
        messages.size,
        isLoading
    ) {


        if (
            messages.isNotEmpty()
        ) {

            listState.animateScrollToItem(
                messages.lastIndex
            )
        }
    }


    // ========================================================================
    // LIBERAR RECURSOS
    // ========================================================================

    DisposableEffect(
        Unit
    ) {


        onDispose {

            repository.close()

            ttsManager.shutdown()
        }
    }


    // ========================================================================
    // ENVIAR PREGUNTA
    // ========================================================================

    fun sendQuestion(
        question: String
    ) {


        val cleanQuestion =
            question.trim()


        if (
            cleanQuestion.isBlank() ||
            isLoading
        ) {

            return
        }


        if (
            !modelReady
        ) {


            messages.add(

                NubyMessage(

                    text =
                        "Mi modelo local todavía no está listo.",

                    fromUser =
                        false
                )
            )


            return
        }


        // --------------------------------------------------------------------
        // MENSAJE DEL USUARIO
        // --------------------------------------------------------------------

        messages.add(

            NubyMessage(

                text =
                    cleanQuestion,

                fromUser =
                    true
            )
        )


        userInput =
            ""


        isLoading =
            true


        // --------------------------------------------------------------------
        // CONSULTAR NUBY
        // --------------------------------------------------------------------

        scope.launch {


            try {


                val answer =
                    repository.ask(

                        question =
                            cleanQuestion,

                        age =
                            userAge,

                        gender =
                            userGender,

                        weight =
                            userWeight,

                        height =
                            userHeight,

                        activityLevel =
                            userActivityLevel,

                        caloriesLose =
                            userLoseCalories,

                        caloriesMaintain =
                            userMaintainCalories,

                        caloriesGain =
                            userGainCalories
                    )


                messages.add(

                    NubyMessage(

                        text =
                            answer.text,

                        fromUser =
                            false,

                        sourceName =
                            answer.sourceName,

                        sourcePages =
                            answer.sourcePages,

                        specialists =
                            answer.specialists
                    )
                )


            } catch (
                e: Exception
            ) {


                messages.add(

                    NubyMessage(

                        text =
                            "No pude responder en este momento. " +
                                    (
                                            e.message
                                                ?: "Intenta nuevamente."
                                            ),

                        fromUser =
                            false
                    )
                )


            } finally {


                isLoading =
                    false
            }
        }
    }


    // ========================================================================
    // UI
    // ========================================================================

    Box(

        modifier =
            Modifier
                .fillMaxSize()
                .background(
                    backgroundGradient()
                )
                .statusBarsPadding()
                .navigationBarsPadding()
                .imePadding()
    ) {


        Column(

            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(
                        horizontal = 14.dp,
                        vertical = 10.dp
                    )
        ) {


            // =================================================================
            // TOP BAR
            // =================================================================

            Row(

                modifier =
                    Modifier.fillMaxWidth(),

                verticalAlignment =
                    Alignment.CenterVertically
            ) {


                IconButton(
                    onClick =
                        onMenuClick
                ) {


                    Icon(

                        imageVector =
                            Icons.Default.Menu,

                        contentDescription =
                            "Abrir menú",

                        tint =
                            Color(0xFF0F766E)
                    )
                }


                Column(

                    modifier =
                        Modifier.weight(
                            1f
                        )
                ) {


                    Text(

                        text =
                            "Nuby",

                        fontSize =
                            21.sp,

                        fontWeight =
                            FontWeight.Bold,

                        color =
                            Color(0xFF0F766E)
                    )


                    Text(

                        text =
                            statusMessage,

                        fontSize =
                            10.sp,

                        color =
                            if (
                                modelReady
                            ) {

                                Color(0xFF4D7C0F)

                            } else {

                                Color(0xFF64748B)
                            }
                    )
                }


                // -------------------------------------------------------------
                // ESCUCHAR ÚLTIMA RESPUESTA
                // -------------------------------------------------------------

                IconButton(

                    onClick = {


                        val lastNubyMessage =
                            messages
                                .lastOrNull {
                                    !it.fromUser
                                }


                        if (
                            lastNubyMessage != null
                        ) {

                            ttsManager.speak(
                                lastNubyMessage.text
                            )
                        }
                    }
                ) {


                    Icon(

                        imageVector =
                            Icons.Default.VolumeUp,

                        contentDescription =
                            "Escuchar respuesta",

                        tint =
                            Color(0xFF0F766E)
                    )
                }
            }


            // =================================================================
            // NUBY
            // =================================================================

            NubyMascot(

                poses =
                    listOf(

                        R.drawable.nuby_saludo,

                        R.drawable.nuby_feliz,

                        R.drawable.nuby_tranquilo
                    ),

                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(
                            150.dp
                        )
            )


            // =================================================================
            // AVISO
            // =================================================================

            Text(

                text =
                    "Orientación educativa. Nuby no sustituye una valoración profesional.",

                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(
                            bottom = 6.dp
                        ),

                color =
                    Color(0xFF64748B),

                fontSize =
                    10.sp,

                textAlign =
                    TextAlign.Center
            )


            // =================================================================
            // MENSAJES
            // =================================================================

            LazyColumn(

                state =
                    listState,

                modifier =
                    Modifier
                        .weight(
                            1f
                        )
                        .fillMaxWidth(),

                verticalArrangement =
                    Arrangement.spacedBy(
                        10.dp
                    )
            ) {


                items(
                    messages
                ) {
                        message ->


                    Row(

                        modifier =
                            Modifier.fillMaxWidth(),

                        horizontalArrangement =

                            if (
                                message.fromUser
                            ) {

                                Arrangement.End

                            } else {

                                Arrangement.Start
                            }
                    ) {


                        Card(

                            modifier =
                                Modifier.fillMaxWidth(
                                    0.86f
                                ),

                            shape =
                                RoundedCornerShape(
                                    20.dp
                                ),

                            colors =
                                CardDefaults.cardColors(

                                    containerColor =

                                        if (
                                            message.fromUser
                                        ) {

                                            Color(0xFFD9F99D)

                                        } else {

                                            Color.White
                                        }
                                ),

                            elevation =
                                CardDefaults.cardElevation(
                                    defaultElevation =
                                        3.dp
                                )
                        ) {


                            Column(

                                modifier =
                                    Modifier.padding(
                                        14.dp
                                    )
                            ) {


                                Text(

                                    text =
                                        message.text,

                                    color =
                                        Color(0xFF1E293B),

                                    fontSize =
                                        13.sp,

                                    lineHeight =
                                        19.sp
                                )


                                // ------------------------------------------------
                                // FUENTE
                                // ------------------------------------------------

                                if (
                                    !message.fromUser &&
                                    !message.sourceName.isNullOrBlank()
                                ) {


                                    Spacer(

                                        modifier =
                                            Modifier.height(
                                                8.dp
                                            )
                                    )


                                    val pagesText =
                                        if (
                                            message.sourcePages.isNotEmpty()
                                        ) {

                                            " · PDF pág. " +
                                                    message
                                                        .sourcePages
                                                        .joinToString(
                                                            ", "
                                                        )

                                        } else {

                                            ""
                                        }


                                    Text(

                                        text =
                                            "Referencia: ${message.sourceName}$pagesText",

                                        color =
                                            Color(0xFF64748B),

                                        fontSize =
                                            9.sp,

                                        lineHeight =
                                            12.sp
                                    )
                                }


                                // ------------------------------------------------
                                // PROFESIONALES SUGERIDOS
                                // ------------------------------------------------

                                if (
                                    !message.fromUser &&
                                    message.specialists.isNotEmpty()
                                ) {


                                    Spacer(

                                        modifier =
                                            Modifier.height(
                                                10.dp
                                            )
                                    )


                                    Text(

                                        text =
                                            "Profesionales disponibles en Vibra la vida",

                                        fontSize =
                                            11.sp,

                                        fontWeight =
                                            FontWeight.Bold,

                                        color =
                                            Color(0xFF0F766E)
                                    )


                                    Spacer(

                                        modifier =
                                            Modifier.height(
                                                6.dp
                                            )
                                    )


                                    message.specialists.forEach {
                                            specialist ->


                                        NubySpecialistCard(

                                            specialist =
                                                specialist
                                        )


                                        Spacer(

                                            modifier =
                                                Modifier.height(
                                                    8.dp
                                                )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }


                // -------------------------------------------------------------
                // CARGANDO
                // -------------------------------------------------------------

                if (
                    isLoading
                ) {


                    item {


                        Row(

                            modifier =
                                Modifier.fillMaxWidth(),

                            horizontalArrangement =
                                Arrangement.Start
                        ) {


                            Card(

                                shape =
                                    RoundedCornerShape(
                                        20.dp
                                    ),

                                colors =
                                    CardDefaults.cardColors(
                                        containerColor =
                                            Color.White
                                    )
                            ) {


                                Row(

                                    modifier =
                                        Modifier.padding(
                                            14.dp
                                        ),

                                    verticalAlignment =
                                        Alignment.CenterVertically,

                                    horizontalArrangement =
                                        Arrangement.spacedBy(
                                            10.dp
                                        )
                                ) {


                                    CircularProgressIndicator(

                                        modifier =
                                            Modifier.height(
                                                20.dp
                                            ),

                                        strokeWidth =
                                            2.dp
                                    )


                                    Text(

                                        text =
                                            "Nuby está pensando...",

                                        fontSize =
                                            12.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }


            Spacer(

                modifier =
                    Modifier.height(
                        8.dp
                    )
            )


            // =================================================================
            // PROMPTS RÁPIDOS
            // =================================================================

            FlowRow(

                modifier =
                    Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.spacedBy(
                        6.dp
                    ),

                verticalArrangement =
                    Arrangement.spacedBy(
                        6.dp
                    )
            ) {


                NubyQuickPrompts
                    .prompts
                    .take(
                        3
                    )
                    .forEach {
                            prompt ->


                        ElevatedAssistChip(

                            enabled =
                                !isLoading,

                            onClick = {

                                userInput =
                                    prompt
                            },

                            label = {

                                Text(

                                    text =
                                        prompt,

                                    fontSize =
                                        10.sp
                                )
                            }
                        )
                    }
            }


            // =================================================================
            // ENTRADA
            // =================================================================

            Row(

                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(
                            top = 6.dp
                        ),

                verticalAlignment =
                    Alignment.CenterVertically,

                horizontalArrangement =
                    Arrangement.spacedBy(
                        8.dp
                    )
            ) {


                OutlinedTextField(

                    value =
                        userInput,

                    onValueChange = {
                            value ->

                        userInput =
                            value
                    },

                    modifier =
                        Modifier.weight(
                            1f
                        ),

                    enabled =
                        !isLoading,

                    placeholder = {

                        Text(
                            text =
                                "Pregúntale algo a Nuby..."
                        )
                    },

                    maxLines =
                        4
                )


                Button(

                    enabled =
                        userInput.isNotBlank() &&
                                !isLoading &&
                                modelReady,

                    onClick = {

                        sendQuestion(
                            userInput
                        )
                    },

                    shape =
                        RoundedCornerShape(
                            18.dp
                        ),

                    colors =
                        ButtonDefaults.buttonColors(

                            containerColor =
                                Color(0xFF0F766E),

                            contentColor =
                                Color.White
                        )
                ) {


                    Icon(

                        imageVector =
                            Icons.Default.Send,

                        contentDescription =
                            "Enviar"
                    )
                }
            }
        }
    }
}