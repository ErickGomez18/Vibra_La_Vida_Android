package com.example.vibralavida.habitos_saludables
import com.example.vibralavida.pantallas_principales.BackgroundBlurCircle
import com.example.vibralavida.R
import com.example.vibralavida.backgroundGradient

// Imports para estructuras visuales básicas de Compose.
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
import androidx.compose.foundation.layout.widthIn

// Imports para scroll y formas.
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll

// Iconos temporales. Después puedes reemplazarlos por imágenes.
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Mood
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.SentimentDissatisfied
import androidx.compose.material.icons.filled.Speed

// Componentes de Material 3.
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text

// Anotación para crear funciones composables.
import androidx.compose.runtime.Composable

// Imports para alineación, modificadores, colores y texto.
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Pantalla principal del apartado "Conoce tu sentir".
 *
 * Esta pantalla funciona como un menú de cuestionarios emocionales.
 * Desde aquí el usuario podrá entrar a:
 * - ¿Cómo te sientes hoy?
 * - ¿Te sientes inquieto/a o alerta?
 * - ¿Sientes mucha presión encima?
 */
@Composable
fun MoodSurveyMenuScreen(
    userName: String,
    onBackToMenu: () -> Unit,
    onProfileClick: () -> Unit,
    onDailyMoodClick: () -> Unit,
    onAnxietyClick: () -> Unit,
    onStressClick: () -> Unit
) {
    // Se toma solo el primer nombre para el saludo.
    val firstName = userName
        .trim()
        .split(" ")
        .firstOrNull()
        .orEmpty()
        .ifBlank { "Usuario" }

    // Contenedor principal de toda la pantalla.
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient())
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
    ) {
        // Fondo decorativo difuminado en la parte superior.
        BackgroundBlurCircle(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 36.dp)
        )

        // Contenido con scroll para que funcione bien en pantallas pequeñas.
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Barra superior con menú, saludo y perfil.
            MoodTopBar(
                userName = firstName,
                onMenuClick = {
                    // Por ahora queda como adorno.
                    println("Abrir menú lateral")
                },
                onProfileClick = onProfileClick
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Tarjeta principal blanca que contiene todo el contenido.
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 450.dp),
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
                    // Título de la pantalla.
                    Text(
                        text = "“Conoce tu sentir”",
                        fontSize = 23.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Texto breve explicativo.
                    Text(
                        text = "Elige un cuestionario para identificar cómo te encuentras emocionalmente.",
                        fontSize = 13.sp,
                        color = Color(0xFF64748B),
                        textAlign = TextAlign.Center,
                        lineHeight = 18.sp,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(22.dp))

                    // Imagen central decorativa del apartado.
                    MoodHeaderImagePlaceholder()

                    Spacer(modifier = Modifier.height(28.dp))

                    // Primera fila con dos tarjetas.
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        MoodOptionCard(
                            modifier = Modifier.weight(1f),
                            title = "¿Cómo te sientes hoy?",
                            subtitle = "Estado de ánimo",
                            backgroundColor = Color(0xFFF7FCEB),
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.SelfImprovement,
                                    contentDescription = null,
                                    tint = Color(0xFF86A327),
                                    modifier = Modifier.size(44.dp)
                                )
                            },
                            onClick = onDailyMoodClick
                        )

                        MoodOptionCard(
                            modifier = Modifier.weight(1f),
                            title = "¿Te sientes inquieto/a o alerta?",
                            subtitle = "Ansiedad",
                            backgroundColor = Color(0xFFEAF8FF),
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.Psychology,
                                    contentDescription = null,
                                    tint = Color(0xFF0284C7),
                                    modifier = Modifier.size(44.dp)
                                )
                            },
                            onClick = onAnxietyClick
                        )
                    }

                    Spacer(modifier = Modifier.height(22.dp))

                    // Tarjeta centrada para estrés o presión.
                    MoodOptionCard(
                        modifier = Modifier
                            .fillMaxWidth(0.68f)
                            .widthIn(max = 260.dp),
                        title = "¿Sientes mucha presión encima?",
                        subtitle = "Estrés",
                        backgroundColor = Color(0xFFEAF8FF),
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Speed,
                                contentDescription = null,
                                tint = Color(0xFF7C3AED),
                                modifier = Modifier.size(44.dp)
                            )
                        },
                        onClick = onStressClick
                    )

                    Spacer(modifier = Modifier.height(34.dp))

                    // Botón para regresar a la pantalla anterior.
                    Button(
                        onClick = onBackToMenu,
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
 * Barra superior de la pantalla.
 *
 * Contiene:
 * - Ícono de menú a la izquierda.
 * - Saludo al usuario.
 * - Botón de perfil a la derecha.
 */
@Composable
fun MoodTopBar(
    userName: String,
    onMenuClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .widthIn(max = 450.dp)
            .defaultMinSize(minHeight = 56.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Botón de menú.
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

        // Saludo personalizado.
        Text(
            text = "Hola, $userName",
            color = Color(0xFF0F172A),
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )

        // Botón de perfil.
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
 * Imagen principal del apartado "Conoce tu sentir".
 *
 * Por ahora usa un diseño con ícono.
 * Después puedes reemplazarlo por una imagen colocada en drawable.
 */
@Composable
fun MoodHeaderImagePlaceholder() {
    Box(
        modifier = Modifier
            .size(130.dp)
            .clip(RoundedCornerShape(32.dp))
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFEAF8FF),
                        Color(0xFFF7FCEB),
                        Color(0xFFFFF7ED)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        /*
            Para poner una imagen real, guarda tu imagen en:

            app/src/main/res/drawable/conoce_tu_sentir.png

            Y reemplaza el Icon por:

            Image(
                painter = painterResource(id = R.drawable.conoce_tu_sentir),
                contentDescription = "Conoce tu sentir",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        */

        Icon(
            imageVector = Icons.Default.Mood,
            contentDescription = null,
            tint = Color(0xFF0F766E),
            modifier = Modifier.size(76.dp)
        )
    }
}

/**
 * Tarjeta para cada cuestionario emocional.
 *
 * Recibe:
 * - title: título principal.
 * - subtitle: texto pequeño.
 * - backgroundColor: color de fondo.
 * - icon: ícono o imagen que se muestra arriba.
 * - onClick: acción al presionar el botón Vamos.
 */
@Composable
fun MoodOptionCard(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    backgroundColor: Color,
    icon: @Composable () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.defaultMinSize(minHeight = 160.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Contenedor del ícono o imagen.
            Box(
                modifier = Modifier
                    .size(62.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color.White.copy(alpha = 0.75f)),
                contentAlignment = Alignment.Center
            ) {
                /*
                    Si quieres usar imagen, reemplaza icon() por:

                    Image(
                        painter = painterResource(id = R.drawable.nombre_de_tu_imagen),
                        contentDescription = title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                */

                icon()
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = title,
                color = Color(0xFF0F172A),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = subtitle,
                color = Color(0xFF64748B),
                fontSize = 11.sp,
                textAlign = TextAlign.Center,
                lineHeight = 14.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Botón para entrar al cuestionario.
            Button(
                onClick = onClick,
                modifier = Modifier.height(32.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF86A327),
                    contentColor = Color.White
                ),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                Text(
                    text = "Vamos",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}