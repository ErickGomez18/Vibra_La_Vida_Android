package com.example.vibralavida.pantallas_principales

import com.example.vibralavida.backgroundGradient

// ============================================================================
// FIREBASE
// ============================================================================

import com.google.firebase.auth.FirebaseAuth

// Importaciones para bordes, fondos y diseño general
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll

// Importaciones de iconos usados en el formulario
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff

// Componentes de Material 3
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

// Herramientas de Compose para crear componentes y manejar estados
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

// Herramientas de interfaz
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


// ============================================================================
// PANTALLA DE INICIO DE SESIÓN
// ============================================================================

@Composable
fun LoginScreen(

    // Función para volver a la pantalla anterior
    onBack: () -> Unit,

    // Función que se ejecuta cuando Firebase confirmó el login
    onLoginSuccess: () -> Unit,

    // Función para ir a la pantalla de registro
    onGoToRegister: () -> Unit

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

    // Variable para guardar el correo
    var email by remember {
        mutableStateOf("")
    }


    // Variable para guardar la contraseña
    var password by remember {
        mutableStateOf("")
    }


    // Variable para saber si la contraseña se muestra o se oculta
    var showPassword by remember {
        mutableStateOf(false)
    }


    // ========================================================================
    // MENSAJES
    // ========================================================================

    // Variable para mostrar mensajes de error
    var errorMessage by remember {
        mutableStateOf("")
    }


    // Variable para mostrar mensajes correctos
    var successMessage by remember {
        mutableStateOf("")
    }


    // ========================================================================
    // CARGANDO
    // ========================================================================

    var isLoading by remember {
        mutableStateOf(false)
    }


    // ========================================================================
    // CONTENEDOR PRINCIPAL
    // ========================================================================

    Box(

        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient())
            .imePadding()
            .navigationBarsPadding(),

        contentAlignment = Alignment.Center

    ) {


        // Círculo decorativo en la parte superior
        BackgroundBlurCircle(

            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 70.dp)
        )


        // ====================================================================
        // COLUMNA PRINCIPAL
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

            horizontalAlignment = Alignment.CenterHorizontally

        ) {


            // Espacio superior
            Spacer(
                modifier = Modifier.height(28.dp)
            )


            // =================================================================
            // TÍTULO
            // =================================================================

            Text(
                text = "Iniciar sesión",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F766E),
                textAlign = TextAlign.Center
            )


            Spacer(
                modifier = Modifier.height(8.dp)
            )


            // Subtítulo de bienvenida
            Text(
                text = "Bienvenido de nuevo a Vibra la vida",
                fontSize = 14.sp,
                color = Color(0xFF64748B),
                textAlign = TextAlign.Center
            )


            Spacer(
                modifier = Modifier.height(28.dp)
            )


            // =================================================================
            // TARJETA DEL FORMULARIO
            // =================================================================

            Card(

                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 420.dp),

                shape = RoundedCornerShape(30.dp),

                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFF8FFF1)
                ),

                elevation = CardDefaults.cardElevation(
                    defaultElevation = 10.dp
                )

            ) {


                Column(

                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 22.dp,
                            vertical = 28.dp
                        ),

                    verticalArrangement = Arrangement.spacedBy(16.dp)

                ) {


                    // =========================================================
                    // CORREO ELECTRÓNICO
                    // =========================================================

                    LoginTextField(

                        value = email,

                        onValueChange = {

                            email = it

                            // Limpiamos mensajes al volver a escribir
                            errorMessage = ""
                            successMessage = ""
                        },

                        label = "Correo electrónico",

                        leadingIcon = {

                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = null
                            )
                        }
                    )


                    // =========================================================
                    // CONTRASEÑA
                    // =========================================================

                    LoginTextField(

                        value = password,

                        onValueChange = {

                            password = it

                            errorMessage = ""
                            successMessage = ""
                        },

                        label = "Contraseña",

                        leadingIcon = {

                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null
                            )
                        },

                        visualTransformation =
                            if (showPassword) {

                                VisualTransformation.None

                            } else {

                                PasswordVisualTransformation()
                            },

                        trailingIcon = {

                            IconButton(

                                onClick = {
                                    showPassword = !showPassword
                                }

                            ) {

                                Icon(

                                    imageVector =
                                        if (showPassword) {
                                            Icons.Default.VisibilityOff
                                        } else {
                                            Icons.Default.Visibility
                                        },

                                    contentDescription = null
                                )
                            }
                        }
                    )


                    // =========================================================
                    // OLVIDASTE TU CONTRASEÑA
                    // =========================================================

                    TextButton(

                        onClick = {


                            // Limpiamos mensajes anteriores
                            errorMessage = ""
                            successMessage = ""


                            // -------------------------------------------------
                            // VALIDAR QUE HAYA CORREO
                            // -------------------------------------------------

                            if (email.isBlank()) {

                                errorMessage =
                                    "Escribe tu correo electrónico para recuperar tu contraseña."

                                return@TextButton
                            }


                            if (!email.contains("@")) {

                                errorMessage =
                                    "Ingresa un correo electrónico válido."

                                return@TextButton
                            }


                            // -------------------------------------------------
                            // ENVIAR CORREO DE RECUPERACIÓN
                            // -------------------------------------------------

                            isLoading = true


                            firebaseAuth
                                .sendPasswordResetEmail(
                                    email.trim().lowercase()
                                )
                                .addOnCompleteListener {
                                        task ->


                                    isLoading = false


                                    if (task.isSuccessful) {

                                        successMessage =
                                            "Te enviamos un correo para restablecer tu contraseña."

                                    } else {

                                        val mensajeFirebase =
                                            task.exception?.message


                                        errorMessage =
                                            when {


                                                mensajeFirebase
                                                    ?.contains(
                                                        "network",
                                                        ignoreCase = true
                                                    ) == true ->

                                                    "No fue posible conectarse. Revisa tu conexión a internet."


                                                mensajeFirebase
                                                    ?.contains(
                                                        "badly formatted",
                                                        ignoreCase = true
                                                    ) == true ->

                                                    "El correo electrónico no tiene un formato válido."


                                                else ->

                                                    "No fue posible enviar el correo de recuperación."
                                            }
                                    }
                                }
                        },

                        enabled = !isLoading,

                        modifier =
                            Modifier.align(
                                Alignment.End
                            )

                    ) {

                        Text(
                            text = "¿Olvidaste tu contraseña?",
                            color = Color(0xFF0D9488),
                            fontSize = 13.sp
                        )
                    }


                    // =========================================================
                    // ERROR
                    // =========================================================

                    if (errorMessage.isNotBlank()) {

                        Text(
                            text = errorMessage,
                            color = Color(0xFFDC2626),
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }


                    // =========================================================
                    // MENSAJE CORRECTO
                    // =========================================================

                    if (successMessage.isNotBlank()) {

                        Text(
                            text = successMessage,
                            color = Color(0xFF15803D),
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }


                    // =========================================================
                    // BOTÓN ENTRAR
                    // =========================================================

                    Button(

                        onClick = {


                            // -------------------------------------------------
                            // LIMPIAR MENSAJES
                            // -------------------------------------------------

                            errorMessage = ""
                            successMessage = ""


                            // -------------------------------------------------
                            // VALIDAR FORMULARIO
                            // -------------------------------------------------

                            errorMessage =
                                validateLoginForm(
                                    email = email,
                                    password = password
                                )


                            if (errorMessage.isNotBlank()) {

                                return@Button
                            }


                            // -------------------------------------------------
                            // INICIAR CARGA
                            // -------------------------------------------------

                            isLoading = true


                            // -------------------------------------------------
                            // LIMPIAR CORREO
                            // -------------------------------------------------

                            val correoLimpio =
                                email
                                    .trim()
                                    .lowercase()


                            // =================================================
                            // LOGIN REAL CON FIREBASE AUTHENTICATION
                            // =================================================
                            //
                            // Antes aquí solo hacíamos:
                            //
                            // onLoginSuccess()
                            //
                            // Ahora primero Firebase debe confirmar
                            // que el correo y contraseña son correctos.
                            //
                            // =================================================

                            firebaseAuth
                                .signInWithEmailAndPassword(
                                    correoLimpio,
                                    password
                                )
                                .addOnCompleteListener {
                                        task ->


                                    isLoading = false


                                    // =========================================
                                    // LOGIN EXITOSO
                                    // =========================================

                                    if (task.isSuccessful) {


                                        // Firebase ya creó nuevamente
                                        // una sesión activa.

                                        val usuario =
                                            firebaseAuth.currentUser


                                        if (usuario == null) {

                                            errorMessage =
                                                "No fue posible recuperar la sesión."

                                            return@addOnCompleteListener
                                        }


                                        // MainActivity hará:
                                        //
                                        // GET /api/users/me
                                        //
                                        // y decidirá si entra a Home
                                        // o a Queremos conocerte.

                                        onLoginSuccess()


                                    } else {


                                        // =====================================
                                        // LOGIN INCORRECTO
                                        // =====================================

                                        val mensajeFirebase =
                                            task.exception?.message


                                        errorMessage =
                                            when {


                                                mensajeFirebase
                                                    ?.contains(
                                                        "invalid credential",
                                                        ignoreCase = true
                                                    ) == true ->

                                                    "El correo o la contraseña son incorrectos."


                                                mensajeFirebase
                                                    ?.contains(
                                                        "credential is incorrect",
                                                        ignoreCase = true
                                                    ) == true ->

                                                    "El correo o la contraseña son incorrectos."


                                                mensajeFirebase
                                                    ?.contains(
                                                        "password is invalid",
                                                        ignoreCase = true
                                                    ) == true ->

                                                    "El correo o la contraseña son incorrectos."


                                                mensajeFirebase
                                                    ?.contains(
                                                        "no user record",
                                                        ignoreCase = true
                                                    ) == true ->

                                                    "El correo o la contraseña son incorrectos."


                                                mensajeFirebase
                                                    ?.contains(
                                                        "badly formatted",
                                                        ignoreCase = true
                                                    ) == true ->

                                                    "El correo electrónico no tiene un formato válido."


                                                mensajeFirebase
                                                    ?.contains(
                                                        "network",
                                                        ignoreCase = true
                                                    ) == true ->

                                                    "No fue posible conectarse. Revisa tu conexión a internet."


                                                mensajeFirebase
                                                    ?.contains(
                                                        "too many",
                                                        ignoreCase = true
                                                    ) == true ->

                                                    "Se realizaron demasiados intentos. Inténtalo nuevamente más tarde."


                                                else ->

                                                    "No fue posible iniciar sesión. Revisa tu correo y contraseña."
                                            }
                                    }
                                }
                        },


                        // Mientras Firebase trabaja,
                        // evitamos múltiples clics.
                        enabled = !isLoading,


                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),

                        shape = RoundedCornerShape(18.dp),

                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF86A327),
                            contentColor = Color.White,
                            disabledContainerColor = Color(0xFFB0BE77),
                            disabledContentColor = Color.White
                        ),

                        contentPadding = PaddingValues(0.dp),

                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 6.dp,
                            pressedElevation = 3.dp
                        )

                    ) {


                        // =====================================================
                        // CARGANDO / TEXTO
                        // =====================================================

                        if (isLoading) {

                            CircularProgressIndicator(
                                modifier = Modifier.height(24.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )

                        } else {

                            Text(
                                text = "Entrar",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }


                    // =========================================================
                    // VOLVER
                    // =========================================================

                    OutlinedButton(

                        onClick = onBack,

                        enabled = !isLoading,

                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),

                        shape = RoundedCornerShape(18.dp),

                        border = BorderStroke(
                            width = 1.5.dp,
                            color = Color(0xFFBFEA7C)
                        ),

                        colors =
                            ButtonDefaults.outlinedButtonColors(
                                containerColor = Color.White,
                                contentColor = Color(0xFF0F766E)
                            )

                    ) {

                        Text(
                            text = "Volver",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }


            // =================================================================
            // CREAR CUENTA
            // =================================================================

            Spacer(
                modifier = Modifier.height(16.dp)
            )


            TextButton(

                onClick = onGoToRegister,

                enabled = !isLoading

            ) {

                Text(
                    text = "¿No tienes cuenta? Crear cuenta",
                    color = Color(0xFF0D9488),
                    fontSize = 14.sp
                )
            }


            Spacer(
                modifier = Modifier.height(24.dp)
            )
        }
    }
}


// ============================================================================
// CAMPO DE TEXTO REUTILIZABLE
// ============================================================================

@Composable
fun LoginTextField(

    // Valor actual del campo
    value: String,

    // Función que se ejecuta cuando cambia el texto
    onValueChange: (String) -> Unit,

    // Etiqueta del campo
    label: String,

    // Icono opcional al inicio
    leadingIcon: @Composable (() -> Unit)? = null,

    // Icono opcional al final
    trailingIcon: @Composable (() -> Unit)? = null,

    // Define si el texto se ve normal o se oculta
    visualTransformation:
    VisualTransformation =
        VisualTransformation.None

) {


    OutlinedTextField(

        value = value,

        onValueChange = onValueChange,

        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(
                minHeight = 62.dp
            ),

        label = {

            Text(
                text = label,
                fontSize = 12.sp
            )
        },

        singleLine = true,

        shape = RoundedCornerShape(28.dp),

        leadingIcon = leadingIcon,

        trailingIcon = trailingIcon,

        visualTransformation = visualTransformation,

        textStyle = TextStyle(
            fontSize = 15.sp,
            color = Color(0xFF111827)
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
// VALIDAR LOS DATOS DEL FORMULARIO
// ============================================================================

fun validateLoginForm(

    email: String,

    password: String

): String {


    return when {

        email.isBlank() ->

            "Ingresa tu correo electrónico."


        !email.contains("@") ->

            "Ingresa un correo electrónico válido."


        password.isBlank() ->

            "Ingresa tu contraseña."


        password.length < 6 ->

            "La contraseña debe tener al menos 6 caracteres."


        else ->

            ""
    }
}