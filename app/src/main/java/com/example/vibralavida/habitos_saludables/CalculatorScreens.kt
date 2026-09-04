package com.example.vibralavida.habitos_saludables

import com.example.vibralavida.backgroundGradient

// Imports para construir la interfaz.
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll

// Iconos usados en las calculadoras.
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.WarningAmber

// Componentes de Material 3.
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text

// Estados de Compose.
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

// Utilidades visuales.
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Modelo para representar un resultado de calculadora.
 *
 * title: título del resultado.
 * value: valor principal.
 * description: explicación breve.
 * color: color usado para semaforización.
 */
data class CalculatorResult(
    val title: String,
    val value: String,
    val description: String,
    val color: Color
)

/**
 * Pantalla de Calculadora de IMC.
 *
 * Fórmula:
 * IMC = peso / estatura²
 *
 * El peso se ingresa en kg.
 * La estatura se ingresa en cm y luego se convierte a metros.
 */
@Composable
fun ImcCalculatorScreen(
    userName: String,
    onBackToMenu: () -> Unit,
    onProfileClick: () -> Unit
) {
    // Campos escritos por el usuario.
    var age by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }

    // Género seleccionado. En IMC no cambia el cálculo, pero se deja por diseño.
    var gender by remember { mutableStateOf("Hombre") }

    // Mensaje de error si falta algún dato o hay valores no válidos.
    var errorMessage by remember { mutableStateOf("") }

    // Resultado calculado del IMC.
    var result by remember { mutableStateOf<CalculatorResult?>(null) }

    CalculatorScaffold(
        userName = userName,
        title = "Calculadora de IMC",
        subtitle = "Calcula tu índice de masa corporal.",
        headerColor = Color(0xFF2563EB),
        headerIcon = {
            Icon(
                imageVector = Icons.Default.Calculate,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(42.dp)
            )
        },
        onBackToMenu = onBackToMenu,
        onProfileClick = onProfileClick
    ) {
        Text(
            text = "El IMC es una referencia general que relaciona el peso y la estatura. No sustituye una valoración médica.",
            fontSize = 13.sp,
            color = Color(0xFF475569),
            lineHeight = 18.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(18.dp))

        GenderSelector(
            selectedGender = gender,
            activeColor = Color(0xFF2563EB),
            onGenderSelected = { selected ->
                gender = selected
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        CalculatorTextField(
            value = age,
            onValueChange = { newValue ->
                age = newValue.filter { it.isDigit() }.take(3)
            },
            label = "Edad",
            placeholder = "Años",
            keyboardType = KeyboardType.Number
        )

        Spacer(modifier = Modifier.height(12.dp))

        CalculatorTextField(
            value = height,
            onValueChange = { newValue ->
                height = newValue.filter { it.isDigit() || it == '.' }.take(5)
            },
            label = "Altura",
            placeholder = "cm",
            keyboardType = KeyboardType.Decimal
        )

        Spacer(modifier = Modifier.height(12.dp))

        CalculatorTextField(
            value = weight,
            onValueChange = { newValue ->
                weight = newValue.filter { it.isDigit() || it == '.' }.take(5)
            },
            label = "Peso",
            placeholder = "kg",
            keyboardType = KeyboardType.Decimal
        )

        Spacer(modifier = Modifier.height(18.dp))

        if (errorMessage.isNotBlank()) {
            ErrorText(message = errorMessage)
            Spacer(modifier = Modifier.height(12.dp))
        }

        Button(
            onClick = {
                val ageNumber = age.toIntOrNull()
                val weightNumber = weight.toDoubleOrNull()
                val heightCm = height.toDoubleOrNull()

                // Validación de datos.
                errorMessage = when {
                    ageNumber == null -> "Ingresa una edad válida."
                    ageNumber < 10 || ageNumber > 100 -> "Ingresa una edad realista."
                    weightNumber == null -> "Ingresa un peso válido."
                    weightNumber < 25.0 || weightNumber > 250.0 -> "Ingresa un peso realista."
                    heightCm == null -> "Ingresa una altura válida en centímetros."
                    heightCm < 100.0 || heightCm > 250.0 -> "Ingresa una altura realista."
                    else -> ""
                }

                if (errorMessage.isBlank()) {
                    // Conversión de centímetros a metros.
                    val heightMeters = heightCm!! / 100.0

                    // Cálculo del IMC.
                    val imc = weightNumber!! / (heightMeters * heightMeters)

                    // Genera el resultado con semaforización.
                    result = buildImcResult(imc)
                } else {
                    result = null
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2563EB),
                contentColor = Color.White
            )
        ) {
            Text(
                text = "Calcular IMC",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        result?.let { calculatedResult ->
            CalculatorResultCard(result = calculatedResult)
        }
    }
}

/**
 * Pantalla de Calculadora de Calorías.
 *
 * Calcula calorías aproximadas para:
 * - Bajar de peso
 * - Mantener peso
 * - Subir de peso
 */
@Composable
fun CaloriesCalculatorScreen(
    userName: String,
    onBackToMenu: () -> Unit,
    onProfileClick: () -> Unit
) {
    var gender by remember { mutableStateOf("Hombre") }
    var age by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var activityLevel by remember { mutableStateOf("Sedentario") }

    var errorMessage by remember { mutableStateOf("") }

    // Resultados de calorías.
    var loseCalories by remember { mutableStateOf<Int?>(null) }
    var maintainCalories by remember { mutableStateOf<Int?>(null) }
    var gainCalories by remember { mutableStateOf<Int?>(null) }

    CalculatorScaffold(
        userName = userName,
        title = "Calculadora de Calorías",
        subtitle = "Estima tus calorías para bajar, mantener o subir de peso.",
        headerColor = Color(0xFF10B981),
        headerIcon = {
            Icon(
                imageVector = Icons.Default.LocalFireDepartment,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(42.dp)
            )
        },
        onBackToMenu = onBackToMenu,
        onProfileClick = onProfileClick
    ) {
        GenderSelector(
            selectedGender = gender,
            activeColor = Color(0xFF10B981),
            onGenderSelected = { selected ->
                gender = selected
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CalculatorTextField(
                modifier = Modifier.weight(1f),
                value = age,
                onValueChange = { newValue ->
                    age = newValue.filter { it.isDigit() }.take(3)
                },
                label = "Edad",
                placeholder = "Años",
                keyboardType = KeyboardType.Number
            )

            CalculatorTextField(
                modifier = Modifier.weight(1f),
                value = weight,
                onValueChange = { newValue ->
                    weight = newValue.filter { it.isDigit() || it == '.' }.take(5)
                },
                label = "Peso",
                placeholder = "kg",
                keyboardType = KeyboardType.Decimal
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        CalculatorTextField(
            value = height,
            onValueChange = { newValue ->
                height = newValue.filter { it.isDigit() || it == '.' }.take(5)
            },
            label = "Altura",
            placeholder = "cm",
            keyboardType = KeyboardType.Decimal
        )

        Spacer(modifier = Modifier.height(12.dp))

        ActivityDropdown(
            selectedActivity = activityLevel,
            activeColor = Color(0xFF10B981),
            onActivitySelected = { selected ->
                activityLevel = selected
            }
        )

        Spacer(modifier = Modifier.height(18.dp))

        if (errorMessage.isNotBlank()) {
            ErrorText(message = errorMessage)
            Spacer(modifier = Modifier.height(12.dp))
        }

        Button(
            onClick = {
                val ageNumber = age.toIntOrNull()
                val weightNumber = weight.toDoubleOrNull()
                val heightNumber = height.toDoubleOrNull()

                errorMessage = when {
                    ageNumber == null -> "Ingresa una edad válida."
                    ageNumber < 10 || ageNumber > 100 -> "Ingresa una edad realista."
                    weightNumber == null -> "Ingresa un peso válido."
                    weightNumber < 25.0 || weightNumber > 250.0 -> "Ingresa un peso realista."
                    heightNumber == null -> "Ingresa una altura válida en centímetros."
                    heightNumber < 100.0 || heightNumber > 250.0 -> "Ingresa una altura realista."
                    else -> ""
                }

                if (errorMessage.isBlank()) {
                    // Fórmula educativa tipo Mifflin-St Jeor.
                    val bmr = if (gender == "Hombre") {
                        (10 * weightNumber!!) + (6.25 * heightNumber!!) - (5 * ageNumber!!) + 5
                    } else {
                        (10 * weightNumber!!) + (6.25 * heightNumber!!) - (5 * ageNumber!!) - 161
                    }

                    // Factor según actividad física.
                    val activityFactor = getActivityFactor(activityLevel)

                    // Gasto energético diario total aproximado.
                    val maintenance = (bmr * activityFactor).toInt()

                    // Tres resultados solicitados.
                    maintainCalories = maintenance
                    loseCalories = maintenance - 500
                    gainCalories = maintenance + 500
                } else {
                    maintainCalories = null
                    loseCalories = null
                    gainCalories = null
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF10B981),
                contentColor = Color.White
            )
        ) {
            Text(
                text = "Calcular Calorías",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        if (maintainCalories != null && loseCalories != null && gainCalories != null) {
            CalorieResultCard(
                title = "Para bajar de peso",
                value = "${loseCalories ?: 0} kcal/día",
                description = "Déficit aproximado de 500 kcal.",
                color = Color(0xFFF59E0B)
            )

            Spacer(modifier = Modifier.height(12.dp))

            CalorieResultCard(
                title = "Para mantener peso",
                value = "${maintainCalories ?: 0} kcal/día",
                description = "Estimación de tu gasto energético diario total.",
                color = Color(0xFF22C55E)
            )

            Spacer(modifier = Modifier.height(12.dp))

            CalorieResultCard(
                title = "Para subir de peso",
                value = "${gainCalories ?: 0} kcal/día",
                description = "Superávit aproximado de 500 kcal.",
                color = Color(0xFF3B82F6)
            )
        }
    }
}

/**
 * Pantalla de Riesgo Cardiovascular.
 *
 * Esta calculadora es educativa y simulada.
 * No es una escala clínica oficial.
 */
@Composable
fun CardiovascularRiskCalculatorScreen(
    userName: String,
    onBackToMenu: () -> Unit,
    onProfileClick: () -> Unit
) {
    var gender by remember { mutableStateOf("Hombre") }
    var age by remember { mutableStateOf("") }
    var systolicPressure by remember { mutableStateOf("") }
    var cholesterol by remember { mutableStateOf("") }

    var smoker by remember { mutableStateOf(false) }
    var diabetes by remember { mutableStateOf(false) }

    var errorMessage by remember { mutableStateOf("") }
    var result by remember { mutableStateOf<CalculatorResult?>(null) }

    CalculatorScaffold(
        userName = userName,
        title = "Riesgo Cardiovascular",
        subtitle = "Estimación educativa del riesgo cardiovascular.",
        headerColor = Color(0xFFE11D48),
        headerIcon = {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(42.dp)
            )
        },
        onBackToMenu = onBackToMenu,
        onProfileClick = onProfileClick
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFFFF7ED)
            ),
            border = BorderStroke(
                width = 1.dp,
                color = Color(0xFFF59E0B)
            )
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.WarningAmber,
                    contentDescription = null,
                    tint = Color(0xFFF59E0B),
                    modifier = Modifier.size(30.dp)
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = "Herramienta educativa. No sustituye una valoración médica ni una escala clínica profesional.",
                    fontSize = 12.sp,
                    color = Color(0xFF7C2D12),
                    lineHeight = 17.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        GenderSelector(
            selectedGender = gender,
            activeColor = Color(0xFFE11D48),
            onGenderSelected = { selected ->
                gender = selected
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        CalculatorTextField(
            value = age,
            onValueChange = { newValue ->
                age = newValue.filter { it.isDigit() }.take(3)
            },
            label = "Edad",
            placeholder = "Años",
            keyboardType = KeyboardType.Number
        )

        Spacer(modifier = Modifier.height(12.dp))

        CalculatorTextField(
            value = systolicPressure,
            onValueChange = { newValue ->
                systolicPressure = newValue.filter { it.isDigit() }.take(3)
            },
            label = "Presión sistólica",
            placeholder = "mmHg",
            keyboardType = KeyboardType.Number
        )

        Spacer(modifier = Modifier.height(12.dp))

        CalculatorTextField(
            value = cholesterol,
            onValueChange = { newValue ->
                cholesterol = newValue.filter { it.isDigit() }.take(3)
            },
            label = "Colesterol total",
            placeholder = "mg/dL",
            keyboardType = KeyboardType.Number
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CalculatorCheckCard(
                modifier = Modifier.weight(1f),
                checked = smoker,
                label = "Fumador activo",
                activeColor = Color(0xFFE11D48),
                onCheckedChange = { checked ->
                    smoker = checked
                }
            )

            CalculatorCheckCard(
                modifier = Modifier.weight(1f),
                checked = diabetes,
                label = "Diagnóstico de diabetes",
                activeColor = Color(0xFFE11D48),
                onCheckedChange = { checked ->
                    diabetes = checked
                }
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        if (errorMessage.isNotBlank()) {
            ErrorText(message = errorMessage)
            Spacer(modifier = Modifier.height(12.dp))
        }

        Button(
            onClick = {
                val ageNumber = age.toIntOrNull()
                val pressureNumber = systolicPressure.toIntOrNull()
                val cholesterolNumber = cholesterol.toIntOrNull()

                errorMessage = when {
                    ageNumber == null -> "Ingresa una edad válida."
                    ageNumber < 10 || ageNumber > 100 -> "Ingresa una edad realista."
                    pressureNumber == null -> "Ingresa una presión sistólica válida."
                    pressureNumber < 80 || pressureNumber > 220 -> "Ingresa una presión sistólica realista."
                    cholesterolNumber == null -> "Ingresa un colesterol total válido."
                    cholesterolNumber < 100 || cholesterolNumber > 400 -> "Ingresa un colesterol realista."
                    else -> ""
                }

                if (errorMessage.isBlank()) {
                    val risk = estimateEducationalCardioRisk(
                        gender = gender,
                        age = ageNumber!!,
                        systolicPressure = pressureNumber!!,
                        cholesterol = cholesterolNumber!!,
                        smoker = smoker,
                        diabetes = diabetes
                    )

                    result = buildCardioRiskResult(risk)
                } else {
                    result = null
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFE11D48),
                contentColor = Color.White
            )
        ) {
            Text(
                text = "Estimar Riesgo Simulado",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        result?.let { calculatedResult ->
            CalculatorResultCard(result = calculatedResult)
        }
    }
}

/**
 * Estructura base para las calculadoras.
 *
 * Esta función evita repetir el mismo diseño en:
 * - IMC
 * - Calorías
 * - Riesgo cardiovascular
 *
 * IMPORTANTE:
 * El parámetro content está corregido como:
 * content: @Composable () -> Unit
 */
@Composable
fun CalculatorScaffold(
    userName: String,
    title: String,
    subtitle: String,
    headerColor: Color,
    headerIcon: @Composable () -> Unit,
    onBackToMenu: () -> Unit,
    onProfileClick: () -> Unit,
    content: @Composable () -> Unit
) {
    val firstName = userName
        .trim()
        .split(" ")
        .firstOrNull()
        .orEmpty()
        .ifBlank { "Usuario" }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient())
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CalculatorTopBar(
                userName = firstName,
                onMenuClick = {
                    println("Abrir menú lateral")
                },
                onProfileClick = onProfileClick
            )

            Spacer(modifier = Modifier.height(14.dp))

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
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(headerColor)
                            .padding(horizontal = 22.dp, vertical = 30.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(62.dp)
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(Color.White.copy(alpha = 0.18f)),
                                contentAlignment = Alignment.Center
                            ) {
                                headerIcon()
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = title,
                                fontSize = 26.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                textAlign = TextAlign.Center,
                                lineHeight = 31.sp
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = subtitle,
                                fontSize = 13.sp,
                                color = Color.White.copy(alpha = 0.95f),
                                textAlign = TextAlign.Center,
                                lineHeight = 18.sp
                            )
                        }
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 22.dp, vertical = 24.dp)
                    ) {
                        // Aquí se dibuja el contenido específico de cada calculadora.
                        content()

                        Spacer(modifier = Modifier.height(24.dp))

                        HorizontalDivider(
                            color = Color(0xFFE2E8F0),
                            thickness = 1.dp
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        Button(
                            onClick = onBackToMenu,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            shape = RoundedCornerShape(18.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF86A327),
                                contentColor = Color.White
                            )
                        ) {
                            Text(
                                text = "Volver al menú",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}

/**
 * Barra superior compartida por las calculadoras.
 */
@Composable
fun CalculatorTopBar(
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
 * Campo de texto reutilizable para las calculadoras.
 */
@Composable
fun CalculatorTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    keyboardType: KeyboardType
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0F172A)
        )

        Spacer(modifier = Modifier.height(6.dp))

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 58.dp),
            placeholder = {
                Text(
                    text = placeholder,
                    color = Color(0xFF94A3B8),
                    fontWeight = FontWeight.SemiBold
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType
            ),
            textStyle = TextStyle(
                fontSize = 15.sp,
                color = Color(0xFF111827),
                fontWeight = FontWeight.SemiBold
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF10B981),
                unfocusedBorderColor = Color(0xFFCBD5E1),
                focusedContainerColor = Color(0xFFF8FAFC),
                unfocusedContainerColor = Color(0xFFF8FAFC),
                cursorColor = Color(0xFF10B981)
            )
        )
    }
}

/**
 * Selector de género.
 */
@Composable
fun GenderSelector(
    selectedGender: String,
    activeColor: Color,
    onGenderSelected: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        GenderButton(
            modifier = Modifier.weight(1f),
            text = "Hombre",
            selected = selectedGender == "Hombre",
            activeColor = activeColor,
            onClick = {
                onGenderSelected("Hombre")
            }
        )

        GenderButton(
            modifier = Modifier.weight(1f),
            text = "Mujer",
            selected = selectedGender == "Mujer",
            activeColor = activeColor,
            onClick = {
                onGenderSelected("Mujer")
            }
        )
    }
}

/**
 * Botón individual del selector de género.
 */
@Composable
fun GenderButton(
    modifier: Modifier = Modifier,
    text: String,
    selected: Boolean,
    activeColor: Color,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(52.dp),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(
            width = 1.5.dp,
            color = if (selected) activeColor else Color(0xFFCBD5E1)
        ),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (selected) activeColor.copy(alpha = 0.10f) else Color(0xFFF8FAFC),
            contentColor = if (selected) activeColor else Color(0xFF64748B)
        )
    ) {
        Text(
            text = text,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

/**
 * Dropdown para elegir el nivel de actividad física.
 */
@Composable
fun ActivityDropdown(
    selectedActivity: String,
    activeColor: Color,
    onActivitySelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    val activities = listOf(
        "Sedentario",
        "Ligero",
        "Moderado",
        "Activo",
        "Muy activo"
    )

    Column {
        Text(
            text = "Nivel de Actividad Física",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0F172A)
        )

        Spacer(modifier = Modifier.height(6.dp))

        Box {
            Button(
                onClick = {
                    expanded = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF8FAFC),
                    contentColor = Color(0xFF111827)
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = activeColor
                ),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                Text(
                    text = selectedActivity,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Start,
                    fontWeight = FontWeight.SemiBold
                )

                Text(text = "▼")
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                }
            ) {
                activities.forEach { item ->
                    DropdownMenuItem(
                        text = {
                            Text(text = item)
                        },
                        onClick = {
                            onActivitySelected(item)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

/**
 * Checkbox dentro de una tarjeta para riesgo cardiovascular.
 */
@Composable
fun CalculatorCheckCard(
    modifier: Modifier = Modifier,
    checked: Boolean,
    label: String,
    activeColor: Color,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        modifier = modifier.defaultMinSize(minHeight = 76.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF8FAFC)
        ),
        border = BorderStroke(
            width = 1.dp,
            color = if (checked) activeColor else Color(0xFFCBD5E1)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = CheckboxDefaults.colors(
                    checkedColor = activeColor
                )
            )

            Text(
                text = label,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A)
            )
        }
    }
}

/**
 * Tarjeta general para mostrar un resultado.
 */
@Composable
fun CalculatorResultCard(
    result: CalculatorResult
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = result.color.copy(alpha = 0.14f)
        ),
        border = BorderStroke(
            width = 1.5.dp,
            color = result.color
        )
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = result.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = result.color,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = result.value,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = result.description,
                fontSize = 13.sp,
                color = Color(0xFF334155),
                lineHeight = 18.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * Tarjeta específica para resultados de calorías.
 */
@Composable
fun CalorieResultCard(
    title: String,
    value: String,
    description: String,
    color: Color
) {
    CalculatorResultCard(
        result = CalculatorResult(
            title = title,
            value = value,
            description = description,
            color = color
        )
    )
}

/**
 * Muestra mensajes de error.
 */
@Composable
fun ErrorText(
    message: String
) {
    Text(
        text = message,
        color = Color(0xFFDC2626),
        fontSize = 13.sp,
        fontWeight = FontWeight.SemiBold,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )
}

/**
 * Construye el resultado del IMC con semaforización.
 */
fun buildImcResult(imc: Double): CalculatorResult {
    val rounded = String.format("%.1f", imc)

    return when {
        imc < 18.5 -> CalculatorResult(
            title = "Bajo peso",
            value = rounded,
            description = "Tu IMC se encuentra por debajo del rango considerado normal.",
            color = Color(0xFF3B82F6)
        )

        imc < 25.0 -> CalculatorResult(
            title = "Peso normal",
            value = rounded,
            description = "Tu IMC se encuentra dentro del rango considerado saludable.",
            color = Color(0xFF22C55E)
        )

        imc < 30.0 -> CalculatorResult(
            title = "Sobrepeso",
            value = rounded,
            description = "Tu IMC se encuentra por encima del rango normal.",
            color = Color(0xFFF59E0B)
        )

        else -> CalculatorResult(
            title = "Obesidad",
            value = rounded,
            description = "Tu IMC se encuentra en un rango alto. Considera orientación profesional.",
            color = Color(0xFFEF4444)
        )
    }
}

/**
 * Factor de actividad física usado en la calculadora de calorías.
 */
fun getActivityFactor(activityLevel: String): Double {
    return when (activityLevel) {
        "Sedentario" -> 1.2
        "Ligero" -> 1.375
        "Moderado" -> 1.55
        "Activo" -> 1.725
        "Muy activo" -> 1.9
        else -> 1.2
    }
}

/**
 * Estimación educativa de riesgo cardiovascular.
 *
 * No es una escala clínica real.
 * Solo sirve como simulación para la app.
 */
fun estimateEducationalCardioRisk(
    gender: String,
    age: Int,
    systolicPressure: Int,
    cholesterol: Int,
    smoker: Boolean,
    diabetes: Boolean
): Int {
    var risk = 1

    risk += when {
        age < 30 -> 0
        age < 40 -> 2
        age < 50 -> 5
        age < 60 -> 8
        else -> 12
    }

    risk += when {
        systolicPressure < 120 -> 0
        systolicPressure < 140 -> 3
        systolicPressure < 160 -> 6
        else -> 9
    }

    risk += when {
        cholesterol < 180 -> 0
        cholesterol < 220 -> 2
        cholesterol < 260 -> 4
        else -> 6
    }

    if (smoker) risk += 5
    if (diabetes) risk += 6
    if (gender == "Hombre") risk += 2

    return risk.coerceIn(1, 35)
}

/**
 * Construye el resultado de riesgo cardiovascular con semaforización.
 */
fun buildCardioRiskResult(risk: Int): CalculatorResult {
    return when {
        risk < 5 -> CalculatorResult(
            title = "Riesgo bajo",
            value = "$risk%",
            description = "El resultado simulado muestra un nivel bajo de riesgo.",
            color = Color(0xFF22C55E)
        )

        risk < 10 -> CalculatorResult(
            title = "Riesgo moderado",
            value = "$risk%",
            description = "El resultado simulado indica un riesgo moderado.",
            color = Color(0xFFF59E0B)
        )

        risk < 20 -> CalculatorResult(
            title = "Riesgo alto",
            value = "$risk%",
            description = "El resultado simulado indica un riesgo alto. Conviene revisar hábitos y acudir a orientación profesional.",
            color = Color(0xFFF97316)
        )

        else -> CalculatorResult(
            title = "Riesgo muy alto",
            value = "$risk%",
            description = "El resultado simulado indica un riesgo muy alto. Se recomienda valoración profesional.",
            color = Color(0xFFEF4444)
        )
    }
}