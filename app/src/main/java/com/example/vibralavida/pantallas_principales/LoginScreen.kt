package com.example.vibralavida.pantallas_principales
import com.example.vibralavida.backgroundGradient

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

// Pantalla de inicio de sesión
@Composable
fun LoginScreen(
    // Función para volver a la pantalla anterior
    onBack: () -> Unit,
    // Función que se ejecuta cuando el login es correcto
    onLoginSuccess: () -> Unit,
    // Función para ir a la pantalla de registro
    onGoToRegister: () -> Unit
) {
    // Variable para guardar el correo
    var email by remember { mutableStateOf("") }

    // Variable para guardar la contraseña
    var password by remember { mutableStateOf("") }

    // Variable para saber si la contraseña se muestra o se oculta
    var showPassword by remember { mutableStateOf(false) }

    // Variable para mostrar mensajes de error
    var errorMessage by remember { mutableStateOf("") }

    // Contenedor principal de la pantalla
    Box(
        modifier = Modifier
            .fillMaxSize() // ocupa toda la pantalla
            .background(backgroundGradient()) // aplica fondo degradado
            .imePadding() // ajusta la pantalla cuando aparece el teclado
            .navigationBarsPadding(), // evita que el contenido choque con la barra inferior
        contentAlignment = Alignment.Center
    ) {
        // Círculo decorativo en la parte superior
        BackgroundBlurCircle(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 70.dp)
        )

        // Columna principal con scroll
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 26.dp, vertical = 36.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Espacio superior
            Spacer(modifier = Modifier.height(28.dp))

            // Título principal
            Text(
                text = "Iniciar sesión",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F766E),
                textAlign = TextAlign.Center
            )

            // Espacio entre textos
            Spacer(modifier = Modifier.height(8.dp))

            // Subtítulo de bienvenida
            Text(
                text = "Bienvenido de nuevo a Vibra la vida",
                fontSize = 14.sp,
                color = Color(0xFF64748B),
                textAlign = TextAlign.Center
            )

            // Espacio antes de la tarjeta
            Spacer(modifier = Modifier.height(28.dp))

            // Tarjeta principal que contiene el formulario
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
                // Columna interna del formulario
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 22.dp, vertical = 28.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Campo para escribir el correo electrónico
                    LoginTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = "Correo electrónico",
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = null
                            )
                        }
                    )

                    // Campo para escribir la contraseña
                    LoginTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = "Contraseña",
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null
                            )
                        },
                        visualTransformation = if (showPassword) {
                            // Si showPassword es true, se muestra la contraseña
                            VisualTransformation.None
                        } else {
                            // Si es false, se oculta
                            PasswordVisualTransformation()
                        },
                        trailingIcon = {
                            // Botón para mostrar u ocultar la contraseña
                            IconButton(
                                onClick = { showPassword = !showPassword }
                            ) {
                                Icon(
                                    imageVector = if (showPassword) {
                                        Icons.Default.VisibilityOff
                                    } else {
                                        Icons.Default.Visibility
                                    },
                                    contentDescription = null
                                )
                            }
                        }
                    )

                    // Botón de recuperar contraseña
                    TextButton(
                        onClick = {
                            println("Recuperar contraseña")
                        },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text(
                            text = "¿Olvidaste tu contraseña?",
                            color = Color(0xFF0D9488),
                            fontSize = 13.sp
                        )
                    }

                    // Si existe un error, se muestra aquí
                    if (errorMessage.isNotBlank()) {
                        Text(
                            text = errorMessage,
                            color = Color(0xFFDC2626),
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    // Botón principal para iniciar sesión
                    Button(
                        onClick = {
                            // Valida el formulario y guarda el resultado en errorMessage
                            errorMessage = validateLoginForm(
                                email = email,
                                password = password
                            )

                            // Si no hay errores, ejecuta la función de éxito
                            if (errorMessage.isBlank()) {
                                onLoginSuccess()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF86A327),
                            contentColor = Color.White
                        ),
                        contentPadding = PaddingValues(0.dp),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 6.dp,
                            pressedElevation = 3.dp
                        )
                    ) {
                        Text(
                            text = "Entrar",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Botón secundario para volver
                    OutlinedButton(
                        onClick = onBack,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(18.dp),
                        border = BorderStroke(
                            width = 1.5.dp,
                            color = Color(0xFFBFEA7C)
                        ),
                        colors = ButtonDefaults.outlinedButtonColors(
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

            // Espacio debajo de la tarjeta
            Spacer(modifier = Modifier.height(16.dp))

            // Botón para ir al registro
            TextButton(
                onClick = onGoToRegister
            ) {
                Text(
                    text = "¿No tienes cuenta? Crear cuenta",
                    color = Color(0xFF0D9488),
                    fontSize = 14.sp
                )
            }

            // Espacio inferior
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// Campo de texto reutilizable para esta pantalla de login
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
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    // Campo de texto con borde
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 62.dp),
        label = {
            Text(
                text = label,
                fontSize = 12.sp
            )
        },
        singleLine = true, // permite solo una línea
        shape = RoundedCornerShape(28.dp),
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        visualTransformation = visualTransformation,
        textStyle = TextStyle(
            fontSize = 15.sp,
            color = Color(0xFF111827)
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color(0xFFB8F7E8),
            unfocusedContainerColor = Color(0xFFB8F7E8),
            focusedBorderColor = Color(0xFF0F766E),
            unfocusedBorderColor = Color(0xFF111827),
            focusedLabelColor = Color(0xFF0F766E),
            unfocusedLabelColor = Color(0xFF111827),
            focusedTextColor = Color(0xFF111827),
            unfocusedTextColor = Color(0xFF111827),
            cursorColor = Color(0xFF0F766E),
            focusedLeadingIconColor = Color(0xFF4B5563),
            unfocusedLeadingIconColor = Color(0xFF4B5563),
            focusedTrailingIconColor = Color(0xFF4B5563),
            unfocusedTrailingIconColor = Color(0xFF4B5563)
        )
    )
}

// Función para validar los datos del formulario de login
fun validateLoginForm(
    email: String,
    password: String
): String {
    // Evalúa los campos y devuelve el mensaje de error correspondiente
    return when {
        email.isBlank() -> "Ingresa tu correo electrónico."
        !email.contains("@") -> "Ingresa un correo electrónico válido."
        password.isBlank() -> "Ingresa tu contraseña."
        password.length < 6 -> "La contraseña debe tener al menos 6 caracteres."
        else -> ""
    }
}