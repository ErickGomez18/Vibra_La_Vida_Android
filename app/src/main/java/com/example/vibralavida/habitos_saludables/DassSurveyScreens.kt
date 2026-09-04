package com.example.vibralavida.habitos_saludables
import com.example.vibralavida.pantallas_principales.BackgroundBlurCircle
import com.example.vibralavida.backgroundGradient

// Imports para construir la interfaz visual con Jetpack Compose.
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

// Imports para iconos.
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.WarningAmber

// Imports de Material 3.
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

// Imports para manejar estados.
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

// Imports visuales.
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Enum que representa el tipo de escala que se va a evaluar.
 *
 * Esto permite reutilizar la misma pantalla base para:
 * - Depresión
 * - Ansiedad
 * - Estrés
 */
enum class DassScaleType {
    Depression,
    Anxiety,
    Stress
}

/**
 * Pantalla para la escala de Depresión del DASS-21.
 *
 * Contiene los 7 ítems correspondientes:
 * 3, 5, 10, 13, 16, 17 y 21.
 */
@Composable
fun DepressionSurveyScreen(
    userName: String,
    onBackToMenu: () -> Unit,
    onProfileClick: () -> Unit
) {
    DassSurveyBaseScreen(
        userName = userName,
        title = "“Conoce tu sentir”",
        subtitle = "Depresión",
        scaleType = DassScaleType.Depression,
        headerIcon = {
            Icon(
                imageVector = Icons.Default.Cloud,
                contentDescription = null,
                tint = Color(0xFF86A327),
                modifier = Modifier.size(76.dp)
            )
        },
        questions = listOf(
            "1. No pude sentir nada positivo.",
            "2. Me resultó difícil tomar la iniciativa para hacer cosas.",
            "3. Sentí que no tenía nada que esperar del futuro.",
            "4. Me sentí triste y deprimido/a.",
            "5. No pude entusiasmarme por nada.",
            "6. Sentí que no valía mucho como persona.",
            "7. Sentí que la vida no tenía sentido."
        ),
        onBackToMenu = onBackToMenu,
        onProfileClick = onProfileClick
    )
}

/**
 * Pantalla para la escala de Ansiedad del DASS-21.
 *
 * Contiene los 7 ítems correspondientes:
 * 2, 4, 7, 9, 15, 19 y 20.
 */
@Composable
fun AnxietySurveyScreen(
    userName: String,
    onBackToMenu: () -> Unit,
    onProfileClick: () -> Unit
) {
    DassSurveyBaseScreen(
        userName = userName,
        title = "“¿Te sientes inquieto/a o alerta?”",
        subtitle = "Ansiedad",
        scaleType = DassScaleType.Anxiety,
        headerIcon = {
            Icon(
                imageVector = Icons.Default.Psychology,
                contentDescription = null,
                tint = Color(0xFF0284C7),
                modifier = Modifier.size(76.dp)
            )
        },
        questions = listOf(
            "1. Noté sequedad en mi boca.",
            "2. Tuve dificultad para respirar.",
            "3. Experimenté temblores.",
            "4. Me preocupé por situaciones en las que podía sentir pánico o hacer el ridículo.",
            "5. Sentí que estaba a punto de entrar en pánico.",
            "6. Sentí que mi corazón latía muy rápido sin esfuerzo físico.",
            "7. Sentí miedo sin razón aparente."
        ),
        onBackToMenu = onBackToMenu,
        onProfileClick = onProfileClick
    )
}

/**
 * Pantalla para la escala de Estrés del DASS-21.
 *
 * Contiene los 7 ítems correspondientes:
 * 1, 6, 8, 11, 12, 14 y 18.
 */
@Composable
fun StressSurveyScreen(
    userName: String,
    onBackToMenu: () -> Unit,
    onProfileClick: () -> Unit
) {
    DassSurveyBaseScreen(
        userName = userName,
        title = "“¿Sientes mucha presión encima?”",
        subtitle = "Estrés",
        scaleType = DassScaleType.Stress,
        headerIcon = {
            Icon(
                imageVector = Icons.Default.Speed,
                contentDescription = null,
                tint = Color(0xFF7C3AED),
                modifier = Modifier.size(76.dp)
            )
        },
        questions = listOf(
            "1. Me costó relajarme.",
            "2. Reaccioné exageradamente en algunas situaciones.",
            "3. Sentí que estaba usando mucha energía nerviosa.",
            "4. Me irrité con facilidad.",
            "5. Me costó tolerar interrupciones en lo que estaba haciendo.",
            "6. Me resultó difícil relajarme.",
            "7. Me sentí irritable."
        ),
        onBackToMenu = onBackToMenu,
        onProfileClick = onProfileClick
    )
}

/**
 * Pantalla base reutilizable para las tres escalas del DASS-21.
 *
 * Esta pantalla:
 * - Muestra 7 preguntas.
 * - Permite responder cada pregunta con valores de 0 a 3.
 * - Suma los valores.
 * - Multiplica el resultado por 2.
 * - Interpreta el nivel según la escala correspondiente.
 */
@Composable
fun DassSurveyBaseScreen(
    userName: String,
    title: String,
    subtitle: String,
    scaleType: DassScaleType,
    headerIcon: @Composable () -> Unit,
    questions: List<String>,
    onBackToMenu: () -> Unit,
    onProfileClick: () -> Unit
) {
    // Se obtiene solo el primer nombre del usuario.
    val firstName = userName
        .trim()
        .split(" ")
        .firstOrNull()
        .orEmpty()
        .ifBlank { "Usuario" }

    /*
        Lista de respuestas.

        Cada pregunta inicia en null porque aún no ha sido respondida.
        Cuando el usuario selecciona una opción, se guarda un valor de 0 a 3.
    */
    var answers by remember {
        mutableStateOf(List<Int?>(7) { null })
    }

    // Mensaje de error si el usuario intenta terminar sin contestar todo.
    var errorMessage by remember {
        mutableStateOf("")
    }

    // Guarda la suma directa antes de multiplicar por 2.
    var rawScore by remember {
        mutableStateOf<Int?>(null)
    }

    // Guarda el puntaje final multiplicado por 2.
    var finalScore by remember {
        mutableStateOf<Int?>(null)
    }

    // Guarda el nivel interpretado: normal, leve, moderado, severo, etc.
    var resultLevel by remember {
        mutableStateOf("")
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient())
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
    ) {
        // Fondo decorativo superior.
        BackgroundBlurCircle(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 36.dp)
        )

        // Columna principal con scroll.
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Barra superior con saludo y perfil.
            DassTopBar(
                userName = firstName,
                onMenuClick = {
                    println("Abrir menú lateral")
                },
                onProfileClick = onProfileClick
            )

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 460.dp),
                shape = RoundedCornerShape(30.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFEFFF6)
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 10.dp
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp, vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Título principal de la pantalla.
                    Text(
                        text = title,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A),
                        textAlign = TextAlign.Center,
                        lineHeight = 26.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Nombre de la escala actual.
                    Text(
                        text = subtitle,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF0F766E),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Imagen o icono superior.
                    DassHeaderImagePlaceholder(
                        icon = headerIcon
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // Instrucciones para responder.
                    Text(
                        text = "Indica cuánto te ha ocurrido cada situación durante la última semana.",
                        fontSize = 13.sp,
                        color = Color(0xFF334155),
                        textAlign = TextAlign.Center,
                        lineHeight = 18.sp,
                        modifier = Modifier.padding(horizontal = 6.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Explicación de los valores de respuesta.
                    Text(
                        text = "0 = No me ocurrió | 1 = Me ocurrió un poco | 2 = Me ocurrió bastante | 3 = Me ocurrió mucho",
                        fontSize = 11.sp,
                        color = Color(0xFF64748B),
                        textAlign = TextAlign.Center,
                        lineHeight = 16.sp,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    HorizontalDivider(
                        color = Color(0xFFE2E8F0),
                        thickness = 1.dp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Se muestran las 7 preguntas de la escala.
                    questions.forEachIndexed { index, question ->
                        DassQuestionItem(
                            question = question,
                            selectedValue = answers[index],
                            onValueSelected = { selected ->
                                val updatedAnswers = answers.toMutableList()
                                updatedAnswers[index] = selected
                                answers = updatedAnswers

                                // Si el usuario cambia algo, se borra el resultado anterior.
                                rawScore = null
                                finalScore = null
                                resultLevel = ""
                                errorMessage = ""
                            }
                        )

                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    // Muestra mensaje de error.
                    if (errorMessage.isNotBlank()) {
                        Text(
                            text = errorMessage,
                            color = Color(0xFFDC2626),
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    // Muestra el resultado cuando ya fue calculado.
                    if (finalScore != null) {
                        DassResultCard(
                            scaleName = subtitle,
                            rawScore = rawScore ?: 0,
                            finalScore = finalScore ?: 0,
                            resultLevel = resultLevel
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // Botón para calcular el resultado.
                    Button(
                        onClick = {
                            val allAnswered = answers.all { it != null }

                            if (!allAnswered) {
                                errorMessage = "Responde las 7 preguntas para calcular el resultado."
                                rawScore = null
                                finalScore = null
                                resultLevel = ""
                            } else {
                                // Suma directa de los 7 ítems.
                                val directSum = answers.sumOf { it ?: 0 }

                                // DASS-21 requiere multiplicar por 2.
                                val multipliedScore = directSum * 2

                                // Interpretación según la escala correspondiente.
                                val level = interpretDassScore(
                                    scaleType = scaleType,
                                    score = multipliedScore
                                )

                                rawScore = directSum
                                finalScore = multipliedScore
                                resultLevel = level
                                errorMessage = ""
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF22C55E),
                            contentColor = Color(0xFF052E16)
                        ),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 6.dp,
                            pressedElevation = 3.dp
                        )
                    ) {
                        Text(
                            text = "Terminar cuestionario",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Botón para volver a la pantalla anterior.
                    Button(
                        onClick = onBackToMenu,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF86A327),
                            contentColor = Color.White
                        ),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 6.dp,
                            pressedElevation = 3.dp
                        )
                    ) {
                        Text(
                            text = "Volver al menú",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}

/**
 * Barra superior compartida para las pantallas DASS.
 */
@Composable
fun DassTopBar(
    userName: String,
    onMenuClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .widthIn(max = 460.dp)
            .defaultMinSize(minHeight = 56.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onMenuClick
        ) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Abrir menú",
                tint = Color(0xFF6B8E23),
                modifier = Modifier.size(30.dp)
            )
        }

        Text(
            text = "Hola, $userName",
            color = Color(0xFF0F172A),
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )

        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .background(Color(0xFFFFE7C7)),
            contentAlignment = Alignment.Center
        ) {
            IconButton(
                onClick = onProfileClick
            ) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Perfil",
                    tint = Color(0xFF7C4A2D),
                    modifier = Modifier.size(34.dp)
                )
            }
        }
    }
}

/**
 * Placeholder para imagen principal.
 *
 * Por ahora recibe un ícono, pero después puedes cambiarlo por una imagen.
 */
@Composable
fun DassHeaderImagePlaceholder(
    icon: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .size(128.dp)
            .clip(RoundedCornerShape(32.dp))
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFEAF8FF),
                        Color(0xFFFFF7ED),
                        Color(0xFFF7FCEB)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        icon()
    }
}

/**
 * Componente para una pregunta del cuestionario.
 *
 * Muestra:
 * - Texto de la pregunta.
 * - Botón desplegable con opciones 0, 1, 2 y 3.
 */
@Composable
fun DassQuestionItem(
    question: String,
    selectedValue: Int?,
    onValueSelected: (Int) -> Unit
) {
    var expanded by remember {
        mutableStateOf(false)
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = question,
            fontSize = 13.sp,
            color = Color(0xFF0F172A),
            lineHeight = 17.sp,
            modifier = Modifier
                .weight(1f)
                .padding(end = 10.dp)
        )

        Box {
            Button(
                onClick = {
                    expanded = true
                },
                modifier = Modifier
                    .width(64.dp)
                    .height(40.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedValue == null) {
                        Color.White
                    } else {
                        Color(0xFFD9F99D)
                    },
                    contentColor = Color(0xFF111827)
                ),
                contentPadding = PaddingValues(horizontal = 0.dp)
            ) {
                Text(
                    text = selectedValue?.toString() ?: "▼",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                }
            ) {
                DropdownMenuItem(
                    text = { Text("0 - No me aplicó nada") },
                    onClick = {
                        onValueSelected(0)
                        expanded = false
                    }
                )

                DropdownMenuItem(
                    text = { Text("1 - Me aplicó un poco") },
                    onClick = {
                        onValueSelected(1)
                        expanded = false
                    }
                )

                DropdownMenuItem(
                    text = { Text("2 - Me aplicó bastante") },
                    onClick = {
                        onValueSelected(2)
                        expanded = false
                    }
                )

                DropdownMenuItem(
                    text = { Text("3 - Me aplicó mucho") },
                    onClick = {
                        onValueSelected(3)
                        expanded = false
                    }
                )
            }
        }
    }
}

/**
 * Tarjeta que muestra el resultado calculado.
 */
@Composable
fun DassResultCard(
    scaleName: String,
    rawScore: Int,
    finalScore: Int,
    resultLevel: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFEAF8FF)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.WarningAmber,
                contentDescription = null,
                tint = Color(0xFF0284C7),
                modifier = Modifier.size(42.dp)
            )

            Spacer(modifier = Modifier.width(14.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Resultado de $scaleName",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Suma directa: $rawScore",
                    fontSize = 13.sp,
                    color = Color(0xFF334155)
                )

                Text(
                    text = "Puntaje final: $finalScore",
                    fontSize = 13.sp,
                    color = Color(0xFF334155)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Nivel: $resultLevel",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F766E)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Este resultado es orientativo y no sustituye una valoración profesional.",
                    fontSize = 12.sp,
                    color = Color(0xFF64748B),
                    lineHeight = 16.sp
                )
            }
        }
    }
}

/**
 * Función que interpreta el puntaje DASS-21 según la escala.
 *
 * Importante:
 * El puntaje recibido aquí ya debe venir multiplicado por 2.
 */
fun interpretDassScore(
    scaleType: DassScaleType,
    score: Int
): String {
    return when (scaleType) {
        DassScaleType.Depression -> {
            when {
                score <= 9 -> "Normal"
                score <= 13 -> "Leve"
                score <= 20 -> "Moderado"
                score <= 27 -> "Severo"
                else -> "Extremadamente severo"
            }
        }

        DassScaleType.Anxiety -> {
            when {
                score <= 7 -> "Normal"
                score <= 9 -> "Leve"
                score <= 14 -> "Moderado"
                score <= 19 -> "Severo"
                else -> "Extremadamente severo"
            }
        }

        DassScaleType.Stress -> {
            when {
                score <= 14 -> "Normal"
                score <= 18 -> "Leve"
                score <= 25 -> "Moderado"
                score <= 33 -> "Severo"
                else -> "Extremadamente severo"
            }
        }
    }
}