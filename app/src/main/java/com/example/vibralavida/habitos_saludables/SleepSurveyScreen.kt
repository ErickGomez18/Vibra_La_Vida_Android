package com.example.vibralavida.habitos_saludables
import com.example.vibralavida.pantallas_principales.BackgroundBlurCircle
import com.example.vibralavida.backgroundGradient

// ============================================================================
// IMPORTS DE COMPOSE
// ============================================================================

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll

// ============================================================================
// ICONOS
// ============================================================================

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.WarningAmber

// ============================================================================
// MATERIAL 3
// ============================================================================

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text

// ============================================================================
// ESTADOS
// ============================================================================

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

// ============================================================================
// UI
// ============================================================================

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


// ============================================================================
// PANTALLA ESCALA DE INSOMNIO DE ATENAS
// ============================================================================

/**
 * Pantalla de la Escala de Insomnio de Atenas (AIS).
 *
 * La escala tiene:
 *
 * - 8 preguntas.
 * - Cada respuesta vale de 0 a 3.
 * - Puntaje total de 0 a 24.
 *
 * NUEVO:
 *
 * onResultCalculated permite enviar el puntaje obtenido
 * hacia MainActivity.
 *
 * Gracias a eso, Hábitos saludables podrá combinar:
 *
 * - Horas registradas por el reloj.
 * - Resultado de la Escala de Atenas.
 */
@Composable
fun SleepSurveyScreen(

    userName: String,

    onBackToMenu: () -> Unit,

    onProfileClick: () -> Unit,

    onResultCalculated: (Int) -> Unit

) {

    // ========================================================================
    // NOMBRE
    // ========================================================================

    val firstName =
        userName
            .trim()
            .split(" ")
            .firstOrNull()
            .orEmpty()
            .ifBlank {
                "Usuario"
            }


    // ========================================================================
    // PREGUNTAS AIS
    // ========================================================================

    val questions =
        listOf(

            "1. ¿Cómo sientes el tiempo que tardas en conciliar el sueño después de apagar las luces?",

            "2. ¿Cuántas veces te levantas durante la noche?",

            "3. ¿Te despiertas antes de lo deseado?",

            "4. Considera el tiempo que duermes en total.",

            "5. Considera en general tu calidad de sueño.",

            "6. La sensación de bienestar durante el día la notas...",

            "7. Siento que mi rendimiento físico y mental durante el día es...",

            "8. ¿Durante el día se presenta somnolencia?"
        )


    // ========================================================================
    // RESPUESTAS
    // ========================================================================

    var answers by remember {

        mutableStateOf(
            List<Int?>(8) {
                null
            }
        )
    }


    // ========================================================================
    // ERROR
    // ========================================================================

    var errorMessage by remember {

        mutableStateOf("")
    }


    // ========================================================================
    // RESULTADO
    // ========================================================================

    var result by remember {

        mutableStateOf<Int?>(
            null
        )
    }


    // ========================================================================
    // PANTALLA
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

        BackgroundBlurCircle(

            modifier =
                Modifier
                    .align(
                        Alignment.TopCenter
                    )
                    .padding(
                        top = 36.dp
                    )
        )


        Column(

            modifier =
                Modifier
                    .fillMaxSize()
                    .verticalScroll(
                        rememberScrollState()
                    )
                    .padding(
                        horizontal = 18.dp,
                        vertical = 16.dp
                    ),

            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {


            // =================================================================
            // BARRA SUPERIOR
            // =================================================================

            SleepTopBar(

                userName =
                    firstName,

                onMenuClick = {

                    println(
                        "Abrir menú lateral"
                    )
                },

                onProfileClick =
                    onProfileClick
            )


            Spacer(
                modifier =
                    Modifier.height(
                        16.dp
                    )
            )


            // =================================================================
            // TARJETA PRINCIPAL
            // =================================================================

            Card(

                modifier =
                    Modifier
                        .fillMaxWidth()
                        .widthIn(
                            max = 460.dp
                        ),

                shape =
                    RoundedCornerShape(
                        30.dp
                    ),

                colors =
                    CardDefaults.cardColors(

                        containerColor =
                            Color(0xFFFEFFF6)
                    ),

                elevation =
                    CardDefaults.cardElevation(

                        defaultElevation =
                            10.dp
                    )
            ) {

                Column(

                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 18.dp,
                                vertical = 24.dp
                            ),

                    horizontalAlignment =
                        Alignment.CenterHorizontally
                ) {


                    // =========================================================
                    // TÍTULO
                    // =========================================================

                    Text(

                        text =
                            "¿Qué tal tu sueño?",

                        fontSize =
                            23.sp,

                        fontWeight =
                            FontWeight.Bold,

                        color =
                            Color(0xFF0F172A),

                        textAlign =
                            TextAlign.Center
                    )


                    Spacer(
                        modifier =
                            Modifier.height(
                                8.dp
                            )
                    )


                    SleepHeaderImagePlaceholder()


                    Spacer(
                        modifier =
                            Modifier.height(
                                18.dp
                            )
                    )


                    // =========================================================
                    // DESCRIPCIÓN
                    // =========================================================

                    Text(

                        text =
                            "Contesta esta encuesta. Toma menos de 5 minutos y ayuda a conocer tu percepción sobre la calidad del sueño.",

                        fontSize =
                            13.sp,

                        color =
                            Color(0xFF334155),

                        textAlign =
                            TextAlign.Center,

                        lineHeight =
                            18.sp,

                        modifier =
                            Modifier.padding(
                                horizontal = 6.dp
                            )
                    )


                    Spacer(
                        modifier =
                            Modifier.height(
                                8.dp
                            )
                    )


                    Text(

                        text =
                            "Cada pregunta se valora de 0 a 3 puntos. El resultado final va de 0 a 24.",

                        fontSize =
                            12.sp,

                        color =
                            Color(0xFF64748B),

                        textAlign =
                            TextAlign.Center,

                        lineHeight =
                            17.sp,

                        modifier =
                            Modifier.padding(
                                horizontal = 6.dp
                            )
                    )


                    Spacer(
                        modifier =
                            Modifier.height(
                                18.dp
                            )
                    )


                    HorizontalDivider(

                        color =
                            Color(0xFFE2E8F0),

                        thickness =
                            1.dp
                    )


                    Spacer(
                        modifier =
                            Modifier.height(
                                14.dp
                            )
                    )


                    // =========================================================
                    // PREGUNTAS
                    // =========================================================

                    questions.forEachIndexed {
                            index,
                            question ->


                        SleepQuestionItem(

                            question =
                                question,

                            selectedValue =
                                answers[index],

                            onValueSelected = {
                                    value ->


                                val updatedAnswers =
                                    answers.toMutableList()


                                updatedAnswers[index] =
                                    value


                                answers =
                                    updatedAnswers


                                errorMessage =
                                    ""


                                // Si modifica una respuesta,
                                // el resultado anterior ya no es válido.
                                result =
                                    null
                            }
                        )


                        Spacer(
                            modifier =
                                Modifier.height(
                                    12.dp
                                )
                        )
                    }


                    // =========================================================
                    // ERROR
                    // =========================================================

                    if (
                        errorMessage.isNotBlank()
                    ) {

                        Text(

                            text =
                                errorMessage,

                            color =
                                Color(0xFFDC2626),

                            fontSize =
                                13.sp,

                            textAlign =
                                TextAlign.Center,

                            modifier =
                                Modifier.fillMaxWidth()
                        )


                        Spacer(
                            modifier =
                                Modifier.height(
                                    12.dp
                                )
                        )
                    }


                    // =========================================================
                    // RESULTADO
                    // =========================================================

                    result?.let {
                            score ->


                        SleepResultCard(

                            score =
                                score
                        )


                        Spacer(
                            modifier =
                                Modifier.height(
                                    16.dp
                                )
                        )
                    }


                    // =========================================================
                    // TERMINAR
                    // =========================================================

                    Button(

                        onClick = {

                            val allAnswered =
                                answers.all {
                                    it != null
                                }


                            if (!allAnswered) {

                                errorMessage =
                                    "Responde las 8 preguntas para calcular el resultado."

                                result =
                                    null

                            } else {

                                // Sumamos las 8 respuestas.
                                val totalScore =
                                    answers.sumOf {
                                        it ?: 0
                                    }


                                // Guardamos localmente.
                                result =
                                    totalScore


                                // =================================================
                                // NUEVO:
                                // enviamos el resultado a MainActivity.
                                // =================================================

                                onResultCalculated(
                                    totalScore
                                )


                                errorMessage =
                                    ""
                            }
                        },

                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(
                                    54.dp
                                ),

                        shape =
                            RoundedCornerShape(
                                20.dp
                            ),

                        colors =
                            ButtonDefaults.buttonColors(

                                containerColor =
                                    Color(0xFF22C55E),

                                contentColor =
                                    Color(0xFF052E16)
                            ),

                        elevation =
                            ButtonDefaults.buttonElevation(

                                defaultElevation =
                                    6.dp,

                                pressedElevation =
                                    3.dp
                            )
                    ) {

                        Text(

                            text =
                                "Terminar cuestionario",

                            fontSize =
                                16.sp,

                            fontWeight =
                                FontWeight.Bold
                        )
                    }


                    Spacer(
                        modifier =
                            Modifier.height(
                                14.dp
                            )
                    )


                    // =========================================================
                    // VOLVER
                    // =========================================================

                    Button(

                        onClick =
                            onBackToMenu,

                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(
                                    54.dp
                                ),

                        shape =
                            RoundedCornerShape(
                                20.dp
                            ),

                        colors =
                            ButtonDefaults.buttonColors(

                                containerColor =
                                    Color(0xFF86A327),

                                contentColor =
                                    Color.White
                            )
                    ) {

                        Text(

                            text =
                                "Volver a hábitos saludables",

                            fontSize =
                                16.sp,

                            fontWeight =
                                FontWeight.Bold
                        )
                    }
                }
            }


            Spacer(
                modifier =
                    Modifier.height(
                        28.dp
                    )
            )
        }
    }
}


// ============================================================================
// BARRA SUPERIOR
// ============================================================================

@Composable
fun SleepTopBar(

    userName: String,

    onMenuClick: () -> Unit,

    onProfileClick: () -> Unit

) {

    Row(

        modifier =
            Modifier
                .fillMaxWidth()
                .widthIn(
                    max = 460.dp
                )
                .defaultMinSize(
                    minHeight = 56.dp
                ),

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
                    Color(0xFF6B8E23),

                modifier =
                    Modifier.size(
                        30.dp
                    )
            )
        }


        Text(

            text =
                "Hola, $userName",

            color =
                Color(0xFF0F172A),

            fontSize =
                17.sp,

            fontWeight =
                FontWeight.Bold,

            modifier =
                Modifier.weight(
                    1f
                )
        )


        Box(

            modifier =
                Modifier
                    .size(
                        46.dp
                    )
                    .clip(
                        CircleShape
                    )
                    .background(
                        Color(0xFFFFE7C7)
                    ),

            contentAlignment =
                Alignment.Center
        ) {

            IconButton(

                onClick =
                    onProfileClick
            ) {

                Icon(

                    imageVector =
                        Icons.Default.AccountCircle,

                    contentDescription =
                        "Perfil",

                    tint =
                        Color(0xFF7C4A2D),

                    modifier =
                        Modifier.size(
                            34.dp
                        )
                )
            }
        }
    }
}


// ============================================================================
// IMAGEN DE SUEÑO
// ============================================================================

@Composable
fun SleepHeaderImagePlaceholder() {

    Box(

        modifier =
            Modifier
                .size(
                    128.dp
                )
                .clip(
                    RoundedCornerShape(
                        32.dp
                    )
                )
                .background(

                    brush =
                        Brush.radialGradient(

                            colors =
                                listOf(

                                    Color(0xFFEAF8FF),

                                    Color(0xFFFFF7ED),

                                    Color(0xFFF7FCEB)
                                )
                        )
                ),

        contentAlignment =
            Alignment.Center
    ) {

        Icon(

            imageVector =
                Icons.Default.NightsStay,

            contentDescription =
                null,

            tint =
                Color(0xFF0284C7),

            modifier =
                Modifier.size(
                    76.dp
                )
        )
    }
}


// ============================================================================
// PREGUNTA
// ============================================================================

@Composable
fun SleepQuestionItem(

    question: String,

    selectedValue: Int?,

    onValueSelected: (Int) -> Unit

) {

    var expanded by remember {

        mutableStateOf(
            false
        )
    }


    Row(

        modifier =
            Modifier.fillMaxWidth(),

        verticalAlignment =
            Alignment.CenterVertically
    ) {


        Text(

            text =
                question,

            fontSize =
                13.sp,

            color =
                Color(0xFF0F172A),

            lineHeight =
                17.sp,

            modifier =
                Modifier
                    .weight(
                        1f
                    )
                    .padding(
                        end = 10.dp
                    )
        )


        Box {

            Button(

                onClick = {

                    expanded =
                        true
                },

                modifier =
                    Modifier
                        .width(
                            64.dp
                        )
                        .height(
                            40.dp
                        ),

                shape =
                    RoundedCornerShape(
                        12.dp
                    ),

                colors =
                    ButtonDefaults.buttonColors(

                        containerColor =
                            if (
                                selectedValue == null
                            ) {

                                Color.White

                            } else {

                                Color(0xFFD9F99D)
                            },

                        contentColor =
                            Color(0xFF111827)
                    ),

                contentPadding =
                    PaddingValues(
                        horizontal = 0.dp
                    )
            ) {

                Text(

                    text =
                        selectedValue?.toString()
                            ?: "▼",

                    fontSize =
                        14.sp,

                    fontWeight =
                        FontWeight.Bold
                )
            }


            DropdownMenu(

                expanded =
                    expanded,

                onDismissRequest = {

                    expanded =
                        false
                }
            ) {


                listOf(
                    0,
                    1,
                    2,
                    3
                ).forEach {
                        option ->


                    DropdownMenuItem(

                        text = {

                            Text(

                                text =
                                    "$option puntos",

                                fontSize =
                                    14.sp
                            )
                        },

                        onClick = {

                            onValueSelected(
                                option
                            )


                            expanded =
                                false
                        }
                    )
                }
            }
        }
    }
}


// ============================================================================
// RESULTADO
// ============================================================================

@Composable
fun SleepResultCard(
    score: Int
) {

    // AIS menor de 6:
    // resultado favorable.
    //
    // AIS 6 o superior:
    // resultado que amerita revisión orientativa.
    val favorable =
        score < 6


    val resultColor =
        if (favorable) {

            Color(0xFF22C55E)

        } else {

            Color(0xFFF59E0B)
        }


    val resultText =
        if (favorable) {

            "Percepción favorable del sueño"

        } else {

            "Se identificaron dificultades relacionadas con el sueño"
        }


    Card(

        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(
                24.dp
            ),

        colors =
            CardDefaults.cardColors(

                containerColor =
                    Color(0xFFEAF8FF)
            ),

        elevation =
            CardDefaults.cardElevation(

                defaultElevation =
                    4.dp
            )
    ) {

        Row(

            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(
                        18.dp
                    ),

            verticalAlignment =
                Alignment.CenterVertically
        ) {


            Icon(

                imageVector =
                    Icons.Default.WarningAmber,

                contentDescription =
                    null,

                tint =
                    resultColor,

                modifier =
                    Modifier.size(
                        42.dp
                    )
            )


            Spacer(
                modifier =
                    Modifier.width(
                        14.dp
                    )
            )


            Column(

                modifier =
                    Modifier.weight(
                        1f
                    )
            ) {


                Text(

                    text =
                        "Resultado AIS",

                    fontSize =
                        16.sp,

                    fontWeight =
                        FontWeight.Bold,

                    color =
                        Color(0xFF0F172A)
                )


                Spacer(
                    modifier =
                        Modifier.height(
                            4.dp
                        )
                )


                Text(

                    text =
                        "Puntaje obtenido: $score de 24 puntos.",

                    fontSize =
                        14.sp,

                    color =
                        Color(0xFF334155)
                )


                Spacer(
                    modifier =
                        Modifier.height(
                            5.dp
                        )
                )


                Text(

                    text =
                        resultText,

                    fontSize =
                        13.sp,

                    fontWeight =
                        FontWeight.Bold,

                    color =
                        resultColor
                )


                Spacer(
                    modifier =
                        Modifier.height(
                            5.dp
                        )
                )


                Text(

                    text =
                        "Este resultado es orientativo y no sustituye una valoración profesional.",

                    fontSize =
                        11.sp,

                    color =
                        Color(0xFF64748B),

                    lineHeight =
                        15.sp
                )
            }
        }
    }
}