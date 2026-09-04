package com.example.vibralavida.pantallas_principales

import com.example.vibralavida.backgroundGradient

// ============================================================================
// FIREBASE
// ============================================================================

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest


// ============================================================================
// COMPOSE - FOUNDATION
// ============================================================================

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll


// ============================================================================
// ICONOS
// ============================================================================

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff


// ============================================================================
// MATERIAL 3
// ============================================================================

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton


// ============================================================================
// COMPOSE STATE
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
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


// ============================================================================
// REGISTER SCREEN
// ============================================================================

@Composable
fun RegisterScreen(

    onBack: () -> Unit,

    onRegisterSuccess: () -> Unit

) {


    // ========================================================================
    // FIREBASE AUTH
    // ========================================================================

    val firebaseAuth =
        remember {

            FirebaseAuth.getInstance()
        }


    // ========================================================================
    // CAMPOS
    // ========================================================================

    var fullName by remember {

        mutableStateOf("")
    }


    var email by remember {

        mutableStateOf("")
    }


    var password by remember {

        mutableStateOf("")
    }


    var confirmPassword by remember {

        mutableStateOf("")
    }


    // ========================================================================
    // VISIBILIDAD DE CONTRASEÑAS
    // ========================================================================

    var showPassword by remember {

        mutableStateOf(false)
    }


    var showConfirmPassword by remember {

        mutableStateOf(false)
    }


    // ========================================================================
    // MENSAJE DE ERROR
    // ========================================================================

    var errorMessage by remember {

        mutableStateOf("")
    }


    // ========================================================================
    // INDICADOR DE CARGA
    // ========================================================================
    //
    // Evita que el usuario pueda tocar varias veces "Continuar"
    // mientras Firebase está creando la cuenta.
    //
    // ========================================================================

    var isLoading by remember {

        mutableStateOf(false)
    }


    // ========================================================================
    // INTERFAZ
    // ========================================================================

    Box(

        modifier =
            Modifier
                .fillMaxSize()
                .background(
                    backgroundGradient()
                )
                .imePadding()
                .navigationBarsPadding(),

        contentAlignment =
            Alignment.Center

    ) {


        BackgroundBlurCircle(

            modifier =
                Modifier
                    .align(
                        Alignment.TopCenter
                    )
                    .padding(
                        top = 60.dp
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
                        horizontal = 26.dp,
                        vertical = 34.dp
                    ),

            horizontalAlignment =
                Alignment.CenterHorizontally

        ) {


            // =================================================================
            // TÍTULO
            // =================================================================

            Text(

                text =
                    "Crear cuenta",

                fontSize =
                    30.sp,

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
                    "Regístrate para comenzar a cuidar tu bienestar",

                fontSize =
                    14.sp,

                color =
                    Color(0xFF64748B),

                textAlign =
                    TextAlign.Center,

                lineHeight =
                    20.sp
            )


            Spacer(

                modifier =
                    Modifier.height(
                        28.dp
                    )
            )


            // =================================================================
            // TARJETA
            // =================================================================

            Card(

                modifier =
                    Modifier
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

                    modifier =
                        Modifier
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
                    // NOMBRE
                    // =========================================================

                    RegisterTextField(

                        value =
                            fullName,

                        onValueChange = {

                            fullName =
                                it

                            errorMessage =
                                ""
                        },

                        label =
                            "Nombre completo",

                        leadingIcon = {

                            Icon(

                                imageVector =
                                    Icons.Default.Person,

                                contentDescription =
                                    null
                            )
                        }
                    )


                    // =========================================================
                    // CORREO
                    // =========================================================

                    RegisterTextField(

                        value =
                            email,

                        onValueChange = {

                            email =
                                it

                            errorMessage =
                                ""
                        },

                        label =
                            "Correo electrónico",

                        leadingIcon = {

                            Icon(

                                imageVector =
                                    Icons.Default.Email,

                                contentDescription =
                                    null
                            )
                        }
                    )


                    // =========================================================
                    // CONTRASEÑA
                    // =========================================================

                    RegisterTextField(

                        value =
                            password,

                        onValueChange = {

                            password =
                                it

                            errorMessage =
                                ""
                        },

                        label =
                            "Contraseña",

                        leadingIcon = {

                            Icon(

                                imageVector =
                                    Icons.Default.Lock,

                                contentDescription =
                                    null
                            )
                        },

                        visualTransformation =

                            if (
                                showPassword
                            ) {

                                VisualTransformation.None

                            } else {

                                PasswordVisualTransformation()
                            },

                        trailingIcon = {

                            IconButton(

                                onClick = {

                                    showPassword =
                                        !showPassword
                                }

                            ) {

                                Icon(

                                    imageVector =

                                        if (
                                            showPassword
                                        ) {

                                            Icons.Default.VisibilityOff

                                        } else {

                                            Icons.Default.Visibility
                                        },

                                    contentDescription =
                                        null
                                )
                            }
                        }
                    )


                    // =========================================================
                    // CONFIRMAR CONTRASEÑA
                    // =========================================================

                    RegisterTextField(

                        value =
                            confirmPassword,

                        onValueChange = {

                            confirmPassword =
                                it

                            errorMessage =
                                ""
                        },

                        label =
                            "Confirmar contraseña",

                        leadingIcon = {

                            Icon(

                                imageVector =
                                    Icons.Default.Lock,

                                contentDescription =
                                    null
                            )
                        },

                        visualTransformation =

                            if (
                                showConfirmPassword
                            ) {

                                VisualTransformation.None

                            } else {

                                PasswordVisualTransformation()
                            },

                        trailingIcon = {

                            IconButton(

                                onClick = {

                                    showConfirmPassword =
                                        !showConfirmPassword
                                }

                            ) {

                                Icon(

                                    imageVector =

                                        if (
                                            showConfirmPassword
                                        ) {

                                            Icons.Default.VisibilityOff

                                        } else {

                                            Icons.Default.Visibility
                                        },

                                    contentDescription =
                                        null
                                )
                            }
                        }
                    )


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
                    }


                    Spacer(

                        modifier =
                            Modifier.height(
                                4.dp
                            )
                    )


                    // =========================================================
                    // BOTÓN CONTINUAR
                    // =========================================================

                    Button(

                        // -----------------------------------------------------
                        // REGISTRAR USUARIO
                        // -----------------------------------------------------

                        onClick = {


                            // =================================================
                            // PRIMERO VALIDAMOS LOCALMENTE
                            // =================================================

                            errorMessage =
                                validateRegisterForm(

                                    fullName =
                                        fullName,

                                    email =
                                        email,

                                    password =
                                        password,

                                    confirmPassword =
                                        confirmPassword
                                )


                            // =================================================
                            // SI TODO ES VÁLIDO, REGISTRAMOS EN FIREBASE
                            // =================================================

                            if (
                                errorMessage.isBlank()
                            ) {


                                isLoading =
                                    true


                                // -------------------------------------------------
                                // LIMPIAR ESPACIOS
                                // -------------------------------------------------

                                val correoLimpio =
                                    email
                                        .trim()
                                        .lowercase()


                                val nombreLimpio =
                                    fullName
                                        .trim()


                                // -------------------------------------------------
                                // CREAR USUARIO EN FIREBASE AUTHENTICATION
                                // -------------------------------------------------
                                //
                                // IMPORTANTE:
                                //
                                // Firebase automáticamente deja iniciada
                                // la sesión del usuario cuando la cuenta
                                // se crea correctamente.
                                //
                                // Por eso después:
                                //
                                // FirebaseAuth.currentUser
                                //
                                // YA NO será null.
                                //
                                // -------------------------------------------------

                                firebaseAuth
                                    .createUserWithEmailAndPassword(

                                        correoLimpio,

                                        password
                                    )
                                    .addOnCompleteListener {
                                            registroTask ->


                                        // ==========================================
                                        // REGISTRO EXITOSO
                                        // ==========================================

                                        if (
                                            registroTask.isSuccessful
                                        ) {


                                            val usuario =
                                                firebaseAuth.currentUser


                                            if (
                                                usuario == null
                                            ) {

                                                isLoading =
                                                    false


                                                errorMessage =
                                                    "La cuenta fue creada, pero no se pudo iniciar la sesión."

                                                return@addOnCompleteListener
                                            }


                                            // ======================================
                                            // GUARDAR NOMBRE EN FIREBASE AUTH
                                            // ======================================
                                            //
                                            // Esto nos permitirá obtener después:
                                            //
                                            // currentUser.displayName
                                            //
                                            // ======================================

                                            val profileUpdates =
                                                UserProfileChangeRequest
                                                    .Builder()
                                                    .setDisplayName(
                                                        nombreLimpio
                                                    )
                                                    .build()


                                            usuario
                                                .updateProfile(
                                                    profileUpdates
                                                )
                                                .addOnCompleteListener {


                                                    // ==============================
                                                    // TERMINAMOS EL PROCESO
                                                    // ==============================
                                                    //
                                                    // Aunque fallara solamente la
                                                    // actualización de displayName,
                                                    // la cuenta YA está creada y
                                                    // autenticada.
                                                    //
                                                    // ==============================

                                                    isLoading =
                                                        false


                                                    // ==============================
                                                    // IR A "QUEREMOS CONOCERTE"
                                                    // ==============================

                                                    onRegisterSuccess()
                                                }


                                        } else {


                                            // ======================================
                                            // ERROR DE FIREBASE
                                            // ======================================

                                            isLoading =
                                                false


                                            val firebaseError =
                                                registroTask
                                                    .exception
                                                    ?.message


                                            // ======================================
                                            // MENSAJES MÁS AMIGABLES
                                            // ======================================

                                            errorMessage =
                                                when {


                                                    firebaseError
                                                        ?.contains(
                                                            "email address is already in use",
                                                            ignoreCase = true
                                                        ) == true ->

                                                        "Este correo electrónico ya está registrado."


                                                    firebaseError
                                                        ?.contains(
                                                            "badly formatted",
                                                            ignoreCase = true
                                                        ) == true ->

                                                        "El correo electrónico no tiene un formato válido."


                                                    firebaseError
                                                        ?.contains(
                                                            "network",
                                                            ignoreCase = true
                                                        ) == true ->

                                                        "No fue posible conectarse. Revisa tu conexión a internet."


                                                    firebaseError
                                                        ?.contains(
                                                            "password",
                                                            ignoreCase = true
                                                        ) == true ->

                                                        "La contraseña no cumple con los requisitos de seguridad."


                                                    else ->

                                                        firebaseError
                                                            ?: "No fue posible crear la cuenta."
                                                }
                                        }
                                    }
                            }
                        },


                        // -----------------------------------------------------
                        // DESACTIVAR DURANTE CARGA
                        // -----------------------------------------------------

                        enabled =
                            !isLoading,


                        modifier =
                            Modifier
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
                                    Color.White,

                                disabledContainerColor =
                                    Color(0xFFB0BE77),

                                disabledContentColor =
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


                        // =====================================================
                        // CONTENIDO DEL BOTÓN
                        // =====================================================

                        if (
                            isLoading
                        ) {

                            CircularProgressIndicator(

                                modifier =
                                    Modifier.height(
                                        24.dp
                                    ),

                                color =
                                    Color.White,

                                strokeWidth =
                                    2.dp
                            )

                        } else {

                            Text(

                                text =
                                    "Continuar",

                                fontSize =
                                    16.sp,

                                fontWeight =
                                    FontWeight.Bold
                            )
                        }
                    }


                    // =========================================================
                    // VOLVER
                    // =========================================================

                    OutlinedButton(

                        onClick =
                            onBack,

                        enabled =
                            !isLoading,

                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(
                                    52.dp
                                ),

                        shape =
                            RoundedCornerShape(
                                18.dp
                            ),

                        border =
                            BorderStroke(

                                width =
                                    1.5.dp,

                                color =
                                    Color(0xFFBFEA7C)
                            ),

                        colors =
                            ButtonDefaults.outlinedButtonColors(

                                containerColor =
                                    Color.White,

                                contentColor =
                                    Color(0xFF0F766E)
                            )

                    ) {

                        Text(

                            text =
                                "Volver",

                            fontSize =
                                15.sp,

                            fontWeight =
                                FontWeight.SemiBold
                        )
                    }
                }
            }


            Spacer(

                modifier =
                    Modifier.height(
                        16.dp
                    )
            )


            // =================================================================
            // YA TENGO CUENTA
            // =================================================================

            TextButton(

                onClick =
                    onBack,

                enabled =
                    !isLoading

            ) {

                Text(

                    text =
                        "¿Ya tienes cuenta? Inicia sesión",

                    color =
                        Color(0xFF0D9488),

                    fontSize =
                        14.sp
                )
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
// CAMPO DE TEXTO
// ============================================================================

@Composable
fun RegisterTextField(

    value: String,

    onValueChange: (String) -> Unit,

    label: String,

    leadingIcon:
    @Composable (() -> Unit)? = null,

    trailingIcon:
    @Composable (() -> Unit)? = null,

    visualTransformation:
    VisualTransformation =
        VisualTransformation.None

) {

    OutlinedTextField(

        value =
            value,

        onValueChange =
            onValueChange,

        modifier =
            Modifier
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

        singleLine =
            true,

        shape =
            RoundedCornerShape(
                28.dp
            ),

        leadingIcon =
            leadingIcon,

        trailingIcon =
            trailingIcon,

        visualTransformation =
            visualTransformation,

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
}


// ============================================================================
// CÍRCULO DECORATIVO DE FONDO
// ============================================================================

@Composable
fun BackgroundBlurCircle(

    modifier:
    Modifier =
        Modifier

) {

    Box(

        modifier =
            modifier
                .height(
                    180.dp
                )
                .fillMaxWidth()
                .blur(
                    70.dp
                )
                .background(

                    brush =
                        Brush.radialGradient(

                            colors =
                                listOf(

                                    Color(0x99CDDC39),

                                    Color(0x6606B6D4),

                                    Color.Transparent
                                )
                        ),

                    shape =
                        CircleShape
                )
    )
}


// ============================================================================
// VALIDACIÓN DEL FORMULARIO
// ============================================================================

fun validateRegisterForm(

    fullName: String,

    email: String,

    password: String,

    confirmPassword: String

): String {


    return when {


        fullName.isBlank() ->

            "Ingresa tu nombre completo."


        email.isBlank() ->

            "Ingresa tu correo electrónico."


        !email.contains("@") ->

            "Ingresa un correo electrónico válido."


        password.isBlank() ->

            "Ingresa una contraseña."


        password.length < 6 ->

            "La contraseña debe tener al menos 6 caracteres."


        confirmPassword.isBlank() ->

            "Confirma tu contraseña."


        password != confirmPassword ->

            "Las contraseñas no coinciden."


        else ->

            ""
    }
}