package com.example.vibralavida.pantallas_principales

import com.example.vibralavida.backgroundGradient

// ============================================================================
// IMPORTACIONES
// ============================================================================

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable

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
import androidx.compose.foundation.layout.widthIn

import androidx.compose.foundation.rememberScrollState

import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.foundation.text.KeyboardOptions

import androidx.compose.foundation.verticalScroll


// ============================================================================
// ICONOS
// ============================================================================

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessibilityNew
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Height
import androidx.compose.material.icons.filled.MonitorWeight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.HealthAndSafety


// ============================================================================
// MATERIAL 3
// ============================================================================

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton


// ============================================================================
// ESTADOS DE COMPOSE
// ============================================================================

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue


// ============================================================================
// INTERFAZ
// ============================================================================

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


// ============================================================================
// PANTALLA DE PERFIL INICIAL
// ============================================================================

@Composable
fun InitialProfileScreen(

    // ========================================================================
    // DATOS QUE SE DEVUELVEN AL TERMINAR
    // ========================================================================
    //
    // edad
    // género
    // peso
    // estatura
    // actividad física
    // enfermedades crónicas
    // otra enfermedad
    //

    onFinish: (
        String,
        String,
        String,
        String,
        String,
        List<String>,
        String
    ) -> Unit,

    // Función para regresar.
    onBack: () -> Unit

) {


    // ========================================================================
    // DATOS PERSONALES
    // ========================================================================

    var age by remember {

        mutableStateOf("")
    }


    var gender by remember {

        mutableStateOf("")
    }


    var weight by remember {

        mutableStateOf("")
    }


    var height by remember {

        mutableStateOf("")
    }


    var activityLevel by remember {

        mutableStateOf("")
    }


    // ========================================================================
    // ENFERMEDADES CRÓNICAS
    // ========================================================================
    //
    // null:
    // todavía no responde.
    //
    // true:
    // sí padece una enfermedad crónica.
    //
    // false:
    // no padece enfermedades crónicas declaradas.
    //

    var tieneEnfermedadCronica by remember {

        mutableStateOf<Boolean?>(
            null
        )
    }


    // Lista de enfermedades seleccionadas.

    val enfermedadesSeleccionadas =
        remember {

            mutableStateListOf<String>()
        }


    // Campo para la opción "Otra".

    var otraEnfermedad by remember {

        mutableStateOf("")
    }


    // ========================================================================
    // ERROR
    // ========================================================================

    var errorMessage by remember {

        mutableStateOf("")
    }


    // ========================================================================
    // PANTALLA
    // ========================================================================

    Box(

        modifier = Modifier
            .fillMaxSize()
            .background(
                backgroundGradient()
            )
            .imePadding()
            .navigationBarsPadding(),

        contentAlignment =
            Alignment.Center

    ) {


        // ====================================================================
        // CÍRCULO DECORATIVO
        // ====================================================================

        BackgroundBlurCircle(

            modifier = Modifier
                .align(
                    Alignment.TopCenter
                )
                .padding(
                    top = 70.dp
                )
        )


        // ====================================================================
        // CONTENIDO
        // ====================================================================

        Column(

            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(
                    horizontal = 26.dp,
                    vertical = 36.dp
                ),

            horizontalAlignment =
                Alignment.CenterHorizontally

        ) {


            Spacer(
                modifier =
                    Modifier.height(
                        20.dp
                    )
            )


            // =================================================================
            // ICONO SUPERIOR
            // =================================================================

            Icon(

                imageVector =
                    Icons.Default.AccessibilityNew,

                contentDescription =
                    null,

                tint =
                    Color(0xFF0F766E)
            )


            Spacer(
                modifier =
                    Modifier.height(
                        10.dp
                    )
            )


            // =================================================================
            // TÍTULO
            // =================================================================

            Text(

                text =
                    "Queremos conocerte",

                fontSize =
                    29.sp,

                fontWeight =
                    FontWeight.Bold,

                color =
                    Color(0xFF0F766E),

                textAlign =
                    TextAlign.Center
            )


            Spacer(
                modifier =
                    Modifier.height(
                        8.dp
                    )
            )


            Text(

                text =
                    "Completa estos datos para personalizar tu experiencia en Vibra la vida.",

                fontSize =
                    14.sp,

                color =
                    Color(0xFF64748B),

                textAlign =
                    TextAlign.Center,

                lineHeight =
                    20.sp,

                modifier =
                    Modifier.padding(
                        horizontal = 8.dp
                    )
            )


            Spacer(
                modifier =
                    Modifier.height(
                        28.dp
                    )
            )


            // =================================================================
            // TARJETA DEL FORMULARIO
            // =================================================================

            Card(

                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(
                        max = 420.dp
                    ),

                shape =
                    RoundedCornerShape(
                        30.dp
                    ),

                colors =
                    CardDefaults.cardColors(

                        containerColor =
                            Color(0xFFF8FFF1)
                    ),

                elevation =
                    CardDefaults.cardElevation(

                        defaultElevation =
                            10.dp
                    )

            ) {


                Column(

                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 22.dp,
                            vertical = 28.dp
                        ),

                    verticalArrangement =
                        Arrangement.spacedBy(
                            16.dp
                        )

                ) {


                    // =========================================================
                    // EDAD
                    // =========================================================

                    ProfileTextField(

                        value =
                            age,

                        onValueChange = {
                                newValue ->


                            age =
                                newValue
                                    .filter {
                                        it.isDigit()
                                    }
                                    .take(
                                        3
                                    )


                            errorMessage =
                                ""
                        },

                        label =
                            "Edad",

                        placeholder =
                            "Ej. 21",

                        leadingIcon = {

                            Icon(

                                imageVector =
                                    Icons.Default.Cake,

                                contentDescription =
                                    null
                            )
                        },

                        keyboardType =
                            KeyboardType.Number
                    )


                    // =========================================================
                    // GÉNERO
                    // =========================================================

                    GenderDropdown(

                        selectedValue =
                            gender,

                        onValueSelected = {

                            gender =
                                it


                            errorMessage =
                                ""
                        }
                    )


                    // =========================================================
                    // PESO
                    // =========================================================

                    ProfileTextField(

                        value =
                            weight,

                        onValueChange = {
                                newValue ->


                            weight =
                                newValue
                                    .filter {

                                        it.isDigit() ||
                                                it == '.'
                                    }
                                    .take(
                                        5
                                    )


                            errorMessage =
                                ""
                        },

                        label =
                            "Peso",

                        placeholder =
                            "Ej. 70.5 kg",

                        leadingIcon = {

                            Icon(

                                imageVector =
                                    Icons.Default.MonitorWeight,

                                contentDescription =
                                    null
                            )
                        },

                        keyboardType =
                            KeyboardType.Decimal
                    )


                    // =========================================================
                    // ESTATURA
                    // =========================================================

                    ProfileTextField(

                        value =
                            height,

                        onValueChange = {
                                newValue ->


                            height =
                                newValue
                                    .filter {

                                        it.isDigit() ||
                                                it == '.'
                                    }
                                    .take(
                                        4
                                    )


                            errorMessage =
                                ""
                        },

                        label =
                            "Estatura",

                        placeholder =
                            "Ej. 1.70 m",

                        leadingIcon = {

                            Icon(

                                imageVector =
                                    Icons.Default.Height,

                                contentDescription =
                                    null
                            )
                        },

                        keyboardType =
                            KeyboardType.Decimal
                    )


                    // =========================================================
                    // ACTIVIDAD FÍSICA
                    // =========================================================

                    ActivityLevelDropdown(

                        selectedValue =
                            activityLevel,

                        onValueSelected = {

                            activityLevel =
                                it


                            errorMessage =
                                ""
                        }
                    )


                    // =========================================================
                    // SEPARADOR DE ANTECEDENTES
                    // =========================================================

                    Spacer(
                        modifier =
                            Modifier.height(
                                4.dp
                            )
                    )


                    Text(

                        text =
                            "Antecedentes de salud",

                        fontSize =
                            18.sp,

                        fontWeight =
                            FontWeight.Bold,

                        color =
                            Color(0xFF0F766E)
                    )


                    Text(

                        text =
                            "Esta información nos ayuda a brindarte una experiencia más contextualizada.",

                        fontSize =
                            13.sp,

                        lineHeight =
                            18.sp,

                        color =
                            Color(0xFF64748B)
                    )


                    // =========================================================
                    // PREGUNTA DE ENFERMEDAD CRÓNICA
                    // =========================================================

                    Card(

                        modifier =
                            Modifier.fillMaxWidth(),

                        shape =
                            RoundedCornerShape(
                                22.dp
                            ),

                        colors =
                            CardDefaults.cardColors(

                                containerColor =
                                    Color(0xFFEAFBF5)
                            )
                    ) {


                        Column(

                            modifier =
                                Modifier.padding(
                                    18.dp
                                )
                        ) {


                            Row(

                                verticalAlignment =
                                    Alignment.CenterVertically
                            ) {


                                Icon(

                                    imageVector =
                                        Icons.Default.HealthAndSafety,

                                    contentDescription =
                                        null,

                                    tint =
                                        Color(0xFF0F766E)
                                )


                                Spacer(
                                    modifier =
                                        Modifier.padding(
                                            horizontal = 5.dp
                                        )
                                )


                                Text(

                                    text =
                                        "¿Padeces alguna enfermedad crónica?",

                                    fontSize =
                                        15.sp,

                                    fontWeight =
                                        FontWeight.Bold,

                                    color =
                                        Color(0xFF111827),

                                    modifier =
                                        Modifier.weight(
                                            1f
                                        )
                                )
                            }


                            Spacer(
                                modifier =
                                    Modifier.height(
                                        8.dp
                                    )
                            )


                            Text(

                                text =
                                    "Considera únicamente enfermedades que hayan sido diagnosticadas previamente por un profesional de la salud.",

                                fontSize =
                                    12.sp,

                                lineHeight =
                                    17.sp,

                                color =
                                    Color(0xFF64748B)
                            )


                            Spacer(
                                modifier =
                                    Modifier.height(
                                        12.dp
                                    )
                            )


                            // -------------------------------------------------
                            // RESPUESTA NO
                            // -------------------------------------------------

                            Row(

                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {

                                        tieneEnfermedadCronica =
                                            false


                                        enfermedadesSeleccionadas
                                            .clear()


                                        otraEnfermedad =
                                            ""


                                        errorMessage =
                                            ""
                                    },

                                verticalAlignment =
                                    Alignment.CenterVertically
                            ) {


                                RadioButton(

                                    selected =
                                        tieneEnfermedadCronica ==
                                                false,

                                    onClick = {

                                        tieneEnfermedadCronica =
                                            false


                                        enfermedadesSeleccionadas
                                            .clear()


                                        otraEnfermedad =
                                            ""


                                        errorMessage =
                                            ""
                                    },

                                    colors =
                                        RadioButtonDefaults.colors(

                                            selectedColor =
                                                Color(0xFF0F766E)
                                        )
                                )


                                Text(

                                    text =
                                        "No",

                                    fontSize =
                                        14.sp
                                )
                            }


                            // -------------------------------------------------
                            // RESPUESTA SÍ
                            // -------------------------------------------------

                            Row(

                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {

                                        tieneEnfermedadCronica =
                                            true


                                        errorMessage =
                                            ""
                                    },

                                verticalAlignment =
                                    Alignment.CenterVertically
                            ) {


                                RadioButton(

                                    selected =
                                        tieneEnfermedadCronica ==
                                                true,

                                    onClick = {

                                        tieneEnfermedadCronica =
                                            true


                                        errorMessage =
                                            ""
                                    },

                                    colors =
                                        RadioButtonDefaults.colors(

                                            selectedColor =
                                                Color(0xFF0F766E)
                                        )
                                )


                                Text(

                                    text =
                                        "Sí",

                                    fontSize =
                                        14.sp
                                )
                            }
                        }
                    }


                    // =========================================================
                    // ENFERMEDADES DISPONIBLES
                    // =========================================================

                    if (
                        tieneEnfermedadCronica ==
                        true
                    ) {


                        Text(

                            text =
                                "Selecciona una o más opciones:",

                            fontSize =
                                13.sp,

                            fontWeight =
                                FontWeight.SemiBold,

                            color =
                                Color(0xFF475569)
                        )


                        val enfermedadesDisponibles =
                            listOf(

                                "Diabetes mellitus",

                                "Hipertensión arterial",

                                "Asma",

                                "Enfermedad renal crónica",

                                "Enfermedad cardiovascular",

                                "Trastorno tiroideo",

                                "Otra"
                            )


                        enfermedadesDisponibles.forEach {
                                enfermedad ->


                            EnfermedadCheckbox(

                                texto =
                                    enfermedad,

                                seleccionada =
                                    enfermedadesSeleccionadas
                                        .contains(
                                            enfermedad
                                        ),

                                onCheckedChange = {
                                        seleccionada ->


                                    if (
                                        seleccionada
                                    ) {

                                        if (
                                            !enfermedadesSeleccionadas
                                                .contains(
                                                    enfermedad
                                                )
                                        ) {

                                            enfermedadesSeleccionadas
                                                .add(
                                                    enfermedad
                                                )
                                        }

                                    } else {

                                        enfermedadesSeleccionadas
                                            .remove(
                                                enfermedad
                                            )


                                        if (
                                            enfermedad ==
                                            "Otra"
                                        ) {

                                            otraEnfermedad =
                                                ""
                                        }
                                    }


                                    errorMessage =
                                        ""
                                }
                            )
                        }


                        // =====================================================
                        // OTRA ENFERMEDAD
                        // =====================================================

                        if (
                            enfermedadesSeleccionadas
                                .contains(
                                    "Otra"
                                )
                        ) {

                            ProfileTextField(

                                value =
                                    otraEnfermedad,

                                onValueChange = {

                                    otraEnfermedad =
                                        it


                                    errorMessage =
                                        ""
                                },

                                label =
                                    "Especifica cuál",

                                placeholder =
                                    "Ej. Lupus",

                                leadingIcon = {

                                    Icon(

                                        imageVector =
                                            Icons.Default.HealthAndSafety,

                                        contentDescription =
                                            null
                                    )
                                }
                            )
                        }
                    }


                    // =========================================================
                    // MENSAJE DE ERROR
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
                    }


                    Spacer(
                        modifier =
                            Modifier.height(
                                6.dp
                            )
                    )


                    // =========================================================
                    // INGRESAR
                    // =========================================================

                    Button(

                        onClick = {


                            // -------------------------------------------------
                            // VALIDACIÓN GENERAL
                            // -------------------------------------------------

                            errorMessage =
                                validateInitialProfile(

                                    age =
                                        age,

                                    gender =
                                        gender,

                                    weight =
                                        weight,

                                    height =
                                        height,

                                    activityLevel =
                                        activityLevel,

                                    tieneEnfermedadCronica =
                                        tieneEnfermedadCronica,

                                    enfermedadesCronicas =
                                        enfermedadesSeleccionadas,

                                    otraEnfermedad =
                                        otraEnfermedad
                                )


                            // -------------------------------------------------
                            // DATOS CORRECTOS
                            // -------------------------------------------------

                            if (
                                errorMessage.isBlank()
                            ) {


                                // Si respondió NO,
                                // se manda una lista vacía.

                                val enfermedadesFinales =
                                    if (
                                        tieneEnfermedadCronica ==
                                        true
                                    ) {

                                        enfermedadesSeleccionadas
                                            .toList()

                                    } else {

                                        emptyList()
                                    }


                                onFinish(

                                    age,

                                    gender,

                                    weight,

                                    height,

                                    activityLevel,

                                    enfermedadesFinales,

                                    otraEnfermedad.trim()
                                )
                            }
                        },

                        modifier = Modifier
                            .fillMaxWidth()
                            .height(
                                54.dp
                            ),

                        shape =
                            RoundedCornerShape(
                                18.dp
                            ),

                        colors =
                            ButtonDefaults.buttonColors(

                                containerColor =
                                    Color(0xFF86A327),

                                contentColor =
                                    Color.White
                            ),

                        contentPadding =
                            PaddingValues(
                                0.dp
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
                                "Ingresar",

                            fontSize =
                                16.sp,

                            fontWeight =
                                FontWeight.Bold
                        )
                    }


                    // =========================================================
                    // VOLVER
                    // =========================================================

                    TextButton(

                        onClick =
                            onBack,

                        modifier =
                            Modifier.fillMaxWidth()
                    ) {


                        Text(

                            text =
                                "Volver",

                            color =
                                Color(0xFF64748B),

                            fontSize =
                                14.sp
                        )
                    }
                }
            }


            Spacer(
                modifier =
                    Modifier.height(
                        24.dp
                    )
            )
        }
    }
}


// ============================================================================
// CHECKBOX DE ENFERMEDAD
// ============================================================================

@Composable
private fun EnfermedadCheckbox(

    texto: String,

    seleccionada: Boolean,

    onCheckedChange: (Boolean) -> Unit

) {

    Row(

        modifier = Modifier
            .fillMaxWidth()
            .clickable {

                onCheckedChange(
                    !seleccionada
                )
            },

        verticalAlignment =
            Alignment.CenterVertically

    ) {


        Checkbox(

            checked =
                seleccionada,

            onCheckedChange =
                onCheckedChange,

            colors =
                CheckboxDefaults.colors(

                    checkedColor =
                        Color(0xFF0F766E),

                    checkmarkColor =
                        Color.White
                )
        )


        Text(

            text =
                texto,

            fontSize =
                14.sp,

            color =
                Color(0xFF111827)
        )
    }
}


// ============================================================================
// CAMPO DE TEXTO REUTILIZABLE
// ============================================================================

@Composable
fun ProfileTextField(

    value: String,

    onValueChange: (String) -> Unit,

    label: String,

    placeholder: String,

    leadingIcon: @Composable (() -> Unit)? = null,

    keyboardType: KeyboardType =
        KeyboardType.Text

) {


    OutlinedTextField(

        value =
            value,

        onValueChange =
            onValueChange,

        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(
                minHeight = 62.dp
            ),

        label = {

            Text(

                text =
                    label,

                fontSize =
                    12.sp
            )
        },

        placeholder = {

            Text(

                text =
                    placeholder,

                fontSize =
                    13.sp,

                color =
                    Color(0xFF64748B)
            )
        },

        singleLine =
            true,

        leadingIcon =
            leadingIcon,

        keyboardOptions =
            KeyboardOptions(

                keyboardType =
                    keyboardType
            ),

        shape =
            RoundedCornerShape(
                28.dp
            ),

        textStyle =
            TextStyle(

                fontSize =
                    15.sp,

                color =
                    Color(0xFF111827)
            ),

        colors =
            OutlinedTextFieldDefaults.colors(

                focusedContainerColor =
                    Color(0xFFB8F7E8),

                unfocusedContainerColor =
                    Color(0xFFB8F7E8),

                focusedBorderColor =
                    Color(0xFF0F766E),

                unfocusedBorderColor =
                    Color(0xFF111827),

                focusedLabelColor =
                    Color(0xFF0F766E),

                unfocusedLabelColor =
                    Color(0xFF111827),

                focusedTextColor =
                    Color(0xFF111827),

                unfocusedTextColor =
                    Color(0xFF111827),

                cursorColor =
                    Color(0xFF0F766E),

                focusedLeadingIconColor =
                    Color(0xFF4B5563),

                unfocusedLeadingIconColor =
                    Color(0xFF4B5563)
            )
    )
}


// ============================================================================
// MENÚ DE GÉNERO
// ============================================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenderDropdown(

    selectedValue: String,

    onValueSelected: (String) -> Unit

) {


    var expanded by remember {

        mutableStateOf(
            false
        )
    }


    val options =
        listOf(

            "Mujer",

            "Hombre",

            "Otro",

            "Prefiero no decirlo"
        )


    ExposedDropdownMenuBox(

        expanded =
            expanded,

        onExpandedChange = {

            expanded =
                !expanded
        }

    ) {


        OutlinedTextField(

            value =
                selectedValue,

            onValueChange = {},

            readOnly =
                true,

            modifier = Modifier
                .menuAnchor(
                    type =
                        MenuAnchorType.PrimaryNotEditable
                )
                .fillMaxWidth()
                .defaultMinSize(
                    minHeight = 62.dp
                ),

            label = {

                Text(

                    text =
                        "Género",

                    fontSize =
                        12.sp
                )
            },

            placeholder = {

                Text(

                    text =
                        "Selecciona una opción",

                    fontSize =
                        13.sp,

                    color =
                        Color(0xFF64748B)
                )
            },

            leadingIcon = {

                Icon(

                    imageVector =
                        Icons.Default.Person,

                    contentDescription =
                        null
                )
            },

            trailingIcon = {

                ExposedDropdownMenuDefaults
                    .TrailingIcon(

                        expanded =
                            expanded
                    )
            },

            singleLine =
                true,

            shape =
                RoundedCornerShape(
                    28.dp
                ),

            colors =
                OutlinedTextFieldDefaults.colors(

                    focusedContainerColor =
                        Color(0xFFB8F7E8),

                    unfocusedContainerColor =
                        Color(0xFFB8F7E8),

                    focusedBorderColor =
                        Color(0xFF0F766E),

                    unfocusedBorderColor =
                        Color(0xFF111827),

                    focusedLabelColor =
                        Color(0xFF0F766E),

                    unfocusedLabelColor =
                        Color(0xFF111827),

                    focusedTextColor =
                        Color(0xFF111827),

                    unfocusedTextColor =
                        Color(0xFF111827),

                    focusedLeadingIconColor =
                        Color(0xFF4B5563),

                    unfocusedLeadingIconColor =
                        Color(0xFF4B5563),

                    focusedTrailingIconColor =
                        Color(0xFF4B5563),

                    unfocusedTrailingIconColor =
                        Color(0xFF4B5563)
                )
        )


        ExposedDropdownMenu(

            expanded =
                expanded,

            onDismissRequest = {

                expanded =
                    false
            }

        ) {


            options.forEach {
                    option ->


                DropdownMenuItem(

                    text = {

                        Text(
                            text =
                                option
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


// ============================================================================
// NIVEL DE ACTIVIDAD FÍSICA
// ============================================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityLevelDropdown(

    selectedValue: String,

    onValueSelected: (String) -> Unit

) {


    var expanded by remember {

        mutableStateOf(
            false
        )
    }


    val options =
        listOf(

            "Bajo",

            "Moderado",

            "Alto",

            "Muy alto"
        )


    ExposedDropdownMenuBox(

        expanded =
            expanded,

        onExpandedChange = {

            expanded =
                !expanded
        }

    ) {


        OutlinedTextField(

            value =
                selectedValue,

            onValueChange = {},

            readOnly =
                true,

            modifier = Modifier
                .menuAnchor(
                    type =
                        MenuAnchorType.PrimaryNotEditable
                )
                .fillMaxWidth()
                .defaultMinSize(
                    minHeight = 62.dp
                ),

            label = {

                Text(

                    text =
                        "Nivel de actividad física",

                    fontSize =
                        12.sp
                )
            },

            placeholder = {

                Text(

                    text =
                        "Selecciona una opción",

                    fontSize =
                        13.sp,

                    color =
                        Color(0xFF64748B)
                )
            },

            leadingIcon = {

                Icon(

                    imageVector =
                        Icons.Default.FitnessCenter,

                    contentDescription =
                        null
                )
            },

            trailingIcon = {

                ExposedDropdownMenuDefaults
                    .TrailingIcon(

                        expanded =
                            expanded
                    )
            },

            singleLine =
                true,

            shape =
                RoundedCornerShape(
                    28.dp
                ),

            textStyle =
                TextStyle(

                    fontSize =
                        15.sp,

                    color =
                        Color(0xFF111827)
                ),

            colors =
                OutlinedTextFieldDefaults.colors(

                    focusedContainerColor =
                        Color(0xFFB8F7E8),

                    unfocusedContainerColor =
                        Color(0xFFB8F7E8),

                    focusedBorderColor =
                        Color(0xFF0F766E),

                    unfocusedBorderColor =
                        Color(0xFF111827),

                    focusedLabelColor =
                        Color(0xFF0F766E),

                    unfocusedLabelColor =
                        Color(0xFF111827),

                    focusedTextColor =
                        Color(0xFF111827),

                    unfocusedTextColor =
                        Color(0xFF111827),

                    cursorColor =
                        Color(0xFF0F766E),

                    focusedLeadingIconColor =
                        Color(0xFF4B5563),

                    unfocusedLeadingIconColor =
                        Color(0xFF4B5563),

                    focusedTrailingIconColor =
                        Color(0xFF4B5563),

                    unfocusedTrailingIconColor =
                        Color(0xFF4B5563)
                )
        )


        ExposedDropdownMenu(

            expanded =
                expanded,

            onDismissRequest = {

                expanded =
                    false
            }

        ) {


            options.forEach {
                    option ->


                DropdownMenuItem(

                    text = {

                        Text(

                            text =
                                option,

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


// ============================================================================
// VALIDACIÓN DEL PERFIL INICIAL
// ============================================================================

fun validateInitialProfile(

    age: String,

    gender: String,

    weight: String,

    height: String,

    activityLevel: String,

    tieneEnfermedadCronica: Boolean?,

    enfermedadesCronicas: List<String>,

    otraEnfermedad: String

): String {


    // ========================================================================
    // CONVERSIONES
    // ========================================================================

    val ageNumber =
        age.toIntOrNull()


    val weightNumber =
        weight.toDoubleOrNull()


    val heightNumber =
        height.toDoubleOrNull()


    // ========================================================================
    // VALIDACIONES
    // ========================================================================

    return when {


        age.isBlank() ->

            "Ingresa tu edad."


        ageNumber == null ->

            "Ingresa una edad válida."


        ageNumber < 10 ->

            "La edad debe ser mayor o igual a 10 años."


        ageNumber > 100 ->

            "Ingresa una edad realista."


        // --------------------------------------------------------------------
        // GÉNERO
        // --------------------------------------------------------------------

        gender.isBlank() ->

            "Selecciona una opción de género."


        // --------------------------------------------------------------------
        // PESO
        // --------------------------------------------------------------------

        weight.isBlank() ->

            "Ingresa tu peso."


        weightNumber == null ->

            "Ingresa un peso válido. Ejemplo: 70.5"


        weightNumber < 25.0 ->

            "Ingresa un peso realista."


        weightNumber > 250.0 ->

            "Ingresa un peso realista."


        // --------------------------------------------------------------------
        // ESTATURA
        // --------------------------------------------------------------------

        height.isBlank() ->

            "Ingresa tu estatura."


        heightNumber == null ->

            "Ingresa una estatura válida. Ejemplo: 1.70"


        heightNumber < 1.0 ->

            "La estatura debe ingresarse en metros. Ejemplo: 1.70"


        heightNumber > 2.5 ->

            "Ingresa una estatura realista."


        // --------------------------------------------------------------------
        // ACTIVIDAD
        // --------------------------------------------------------------------

        activityLevel.isBlank() ->

            "Selecciona tu nivel de actividad física."


        // --------------------------------------------------------------------
        // ENFERMEDAD CRÓNICA
        // --------------------------------------------------------------------

        tieneEnfermedadCronica == null ->

            "Indica si padeces alguna enfermedad crónica."


        tieneEnfermedadCronica &&
                enfermedadesCronicas.isEmpty() ->

            "Selecciona al menos una enfermedad crónica."


        tieneEnfermedadCronica &&
                enfermedadesCronicas.contains(
                    "Otra"
                ) &&
                otraEnfermedad.isBlank() ->

            "Especifica cuál es la otra enfermedad crónica."


        // --------------------------------------------------------------------
        // TODO CORRECTO
        // --------------------------------------------------------------------

        else ->

            ""
    }
}