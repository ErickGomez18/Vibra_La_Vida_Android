package com.example.vibralavida.pantallas_principales

import com.example.vibralavida.R
import com.example.vibralavida.backgroundGradient

// Clase de Android que permite convertir texto en voz.
import android.speech.tts.TextToSpeech

// ============================================================
// IMPORTACIONES DE COMPOSE
// ============================================================

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
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


// ============================================================
// ICONOS
// ============================================================

import androidx.compose.material.icons.Icons

import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.VolumeUp


// ============================================================
// MATERIAL DESIGN 3
// ============================================================

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState


// ============================================================
// ESTADOS DE COMPOSE
// ============================================================

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue


// ============================================================
// INTERFAZ
// ============================================================

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.draw.clip

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


// ============================================================
// OTROS
// ============================================================

// Para establecer el idioma del TextToSpeech.
import java.util.Locale

// Para abrir y cerrar el menú lateral.
import kotlinx.coroutines.launch


// ============================================================
// PANTALLA PRINCIPAL
// ============================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(

    // Nombre mostrado en la parte superior.
    userName: String = "María",

    // Abre el perfil.
    onProfileClick: () -> Unit,

    // Abre Trastornos del ritmo.
    onRhythmClick: () -> Unit,

    // Abre Vida saludable.
    onHealthyLifeClick: () -> Unit,

    // Abre Diabetes Mellitus.
    onDiabetesClick: () -> Unit,

    // Abre Mi Agenda.
    onAgendaClick: () -> Unit

) {

    // ------------------------------------------------------------
    // ESTADO DEL MENÚ LATERAL
    // ------------------------------------------------------------

    val drawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed
    )


    // Permite ejecutar operaciones como abrir y cerrar el menú.
    val scope = rememberCoroutineScope()


    // ============================================================
    // MENÚ LATERAL
    // ============================================================

    ModalNavigationDrawer(

        drawerState = drawerState,

        drawerContent = {

            HomeDrawerContent(

                onClose = {

                    scope.launch {

                        drawerState.close()

                    }

                }

            )

        }

    ) {


        // ========================================================
        // CONTENEDOR PRINCIPAL
        // ========================================================

        Box(

            modifier = Modifier
                .fillMaxSize()

                // Utiliza el fondo degradado de Vibra la vida.
                .background(backgroundGradient())

                // Evita que el contenido se coloque debajo
                // de la barra superior del celular.
                .statusBarsPadding()

                // Evita la barra inferior.
                .navigationBarsPadding()

                // Ajusta la pantalla cuando aparece el teclado.
                .imePadding()

        ) {


            // ----------------------------------------------------
            // EFECTO DECORATIVO DEL FONDO
            // ----------------------------------------------------

            BackgroundBlurCircle(

                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 40.dp)

            )


            // ====================================================
            // CONTENIDO CON SCROLL
            // ====================================================

            Column(

                modifier = Modifier

                    .fillMaxSize()

                    // Permite desplazarse verticalmente.
                    .verticalScroll(
                        rememberScrollState()
                    )

                    .padding(
                        horizontal = 18.dp,
                        vertical = 18.dp
                    ),

                horizontalAlignment = Alignment.CenterHorizontally

            ) {


                // =================================================
                // BARRA SUPERIOR
                // =================================================

                HomeTopBar(

                    userName = userName,

                    onMenuClick = {

                        scope.launch {

                            drawerState.open()

                        }

                    },

                    onProfileClick = onProfileClick

                )


                Spacer(
                    modifier = Modifier.height(18.dp)
                )


                // =================================================
                // PRESENTACIÓN DE FITY
                // =================================================

                MascotPresentationCard()


                Spacer(
                    modifier = Modifier.height(22.dp)
                )


                // =================================================
                // TÍTULO DE LAS CATEGORÍAS
                // =================================================

                Text(

                    text = "Selecciona el campo que deseas visitar",

                    color = Color(0xFF0F172A),

                    fontSize = 16.sp,

                    fontWeight = FontWeight.SemiBold,

                    textAlign = TextAlign.Center

                )


                Spacer(
                    modifier = Modifier.height(18.dp)
                )


                // =================================================
                // PRIMERA FILA
                // =================================================
                //
                // Trastornos del ritmo | Vida saludable
                //

                Row(

                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 430.dp),

                    horizontalArrangement = Arrangement.spacedBy(14.dp)

                ) {


                    // ------------------------------------------------
                    // TRASTORNOS DEL RITMO
                    // ------------------------------------------------

                    HomeCategoryCard(

                        modifier = Modifier.weight(1f),

                        title = "Trastornos del ritmo",

                        subtitle = "Seguimiento y orientación",

                        backgroundColor = Color(0xFFF7FCEB),

                        icon = {

                            Icon(

                                imageVector = Icons.Default.Favorite,

                                contentDescription = "Trastornos del ritmo",

                                tint = Color(0xFFEF4444),

                                modifier = Modifier.size(42.dp)

                            )

                        },

                        onClick = onRhythmClick

                    )


                    // ------------------------------------------------
                    // VIDA SALUDABLE
                    // ------------------------------------------------

                    HomeCategoryCard(

                        modifier = Modifier.weight(1f),

                        title = "Vida saludable",

                        subtitle = "Hábitos y bienestar",

                        backgroundColor = Color(0xFFEAF8FF),

                        icon = {

                            Icon(

                                imageVector = Icons.Default.Spa,

                                contentDescription = "Vida saludable",

                                tint = Color(0xFF0284C7),

                                modifier = Modifier.size(42.dp)

                            )

                        },

                        onClick = onHealthyLifeClick

                    )

                }


                // Espacio entre la primera y segunda fila.
                Spacer(
                    modifier = Modifier.height(14.dp)
                )


                // =================================================
                // SEGUNDA FILA
                // =================================================
                //
                // Mi Agenda | Diabetes Mellitus
                //

                Row(

                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 430.dp),

                    horizontalArrangement = Arrangement.spacedBy(14.dp)

                ) {


                    // ------------------------------------------------
                    // MI AGENDA
                    // ------------------------------------------------

                    HomeCategoryCard(

                        modifier = Modifier.weight(1f),

                        title = "Mi Agenda",

                        subtitle = "Medicamentos, citas y estudios",

                        backgroundColor = Color(0xFFE8F7EE),

                        icon = {

                            Icon(

                                imageVector = Icons.Default.CalendarMonth,

                                contentDescription = "Mi Agenda",

                                tint = Color(0xFF0F766E),

                                modifier = Modifier.size(42.dp)

                            )

                        },

                        onClick = onAgendaClick

                    )


                    // ------------------------------------------------
                    // DIABETES MELLITUS
                    // ------------------------------------------------

                    HomeCategoryCard(

                        modifier = Modifier.weight(1f),

                        title = "Diabetes Mellitus",

                        subtitle = "Control y prevención",

                        backgroundColor = Color(0xFFFBFFCC),

                        icon = {

                            Icon(

                                imageVector = Icons.Default.HealthAndSafety,

                                contentDescription = "Diabetes Mellitus",

                                tint = Color(0xFF86A327),

                                modifier = Modifier.size(42.dp)

                            )

                        },

                        onClick = onDiabetesClick

                    )

                }


                Spacer(
                    modifier = Modifier.height(40.dp)
                )


                // =================================================
                // BARRA DECORATIVA INFERIOR
                // =================================================

                Box(

                    modifier = Modifier

                        .fillMaxWidth()

                        .widthIn(max = 430.dp)

                        .height(58.dp)

                        .clip(

                            RoundedCornerShape(

                                topStart = 28.dp,

                                topEnd = 28.dp

                            )

                        )

                        .background(
                            Color(0xFFCFE8B5)
                        )

                )

            }

        }

    }

}


// ============================================================
// BARRA SUPERIOR
// ============================================================

@Composable
fun HomeTopBar(

    userName: String,

    onMenuClick: () -> Unit,

    onProfileClick: () -> Unit

) {

    Row(

        modifier = Modifier

            .fillMaxWidth()

            .widthIn(max = 430.dp)

            .defaultMinSize(
                minHeight = 56.dp
            ),

        verticalAlignment = Alignment.CenterVertically

    ) {


        // --------------------------------------------------------
        // BOTÓN DEL MENÚ
        // --------------------------------------------------------

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


        // --------------------------------------------------------
        // SALUDO
        // --------------------------------------------------------

        Text(

            text = "Hola, $userName",

            color = Color(0xFF0F172A),

            fontSize = 17.sp,

            fontWeight = FontWeight.Bold,

            modifier = Modifier.weight(1f)

        )


        // --------------------------------------------------------
        // PERFIL
        // --------------------------------------------------------

        Box(

            modifier = Modifier

                .size(46.dp)

                .clip(CircleShape)

                .background(
                    Color(0xFFFFE7C7)
                )

                .clickable {

                    onProfileClick()

                },

            contentAlignment = Alignment.Center

        ) {

            Icon(

                imageVector = Icons.Default.AccountCircle,

                contentDescription = "Perfil",

                tint = Color(0xFF7C4A2D),

                modifier = Modifier.size(36.dp)

            )

        }

    }

}


// ============================================================
// TARJETA DE FITY
// ============================================================

@Composable
fun MascotPresentationCard() {


    // Texto que será leído por TextToSpeech.
    val presentationText = """

        Conoce a Fity, tu acompañante dentro de la aplicación.

        Te enviará recordatorios, mensajes de motivación y apoyo para ayudarte a cuidar tu bienestar día a día.

    """.trimIndent()


    Card(

        modifier = Modifier

            .fillMaxWidth()

            .widthIn(max = 430.dp),

        shape = RoundedCornerShape(28.dp),

        colors = CardDefaults.cardColors(

            containerColor = Color(0xFFFEFFF6)

        ),

        elevation = CardDefaults.cardElevation(

            defaultElevation = 8.dp

        )

    ) {


        Column(

            modifier = Modifier

                .fillMaxWidth()

                .padding(18.dp)

        ) {


            Row(

                modifier = Modifier.fillMaxWidth(),

                verticalAlignment = Alignment.CenterVertically

            ) {


                // ----------------------------------------------------
                // IMAGEN TEMPORAL DE FITY
                // ----------------------------------------------------

                MascotImagePlaceholder(

                    modifier = Modifier.size(118.dp)

                )


                Spacer(
                    modifier = Modifier.width(14.dp)
                )


                Column(

                    modifier = Modifier.weight(1f)

                ) {


                    Text(

                        text = "Conoce a “Fity”",

                        color = Color(0xFF0F766E),

                        fontSize = 17.sp,

                        fontWeight = FontWeight.Bold

                    )


                    Spacer(
                        modifier = Modifier.height(6.dp)
                    )


                    Text(

                        text = "Tu personaje especial te acompañará dentro de la aplicación.",

                        color = Color(0xFF334155),

                        fontSize = 13.sp,

                        lineHeight = 18.sp,

                        fontWeight = FontWeight.Medium

                    )

                }

            }


            Spacer(
                modifier = Modifier.height(14.dp)
            )


            Text(

                text = "Te enviará recordatorios y mensajes de motivación para ayudarte a cuidar tu bienestar.",

                color = Color(0xFF334155),

                fontSize = 13.sp,

                lineHeight = 19.sp,

                textAlign = TextAlign.Justify

            )


            Spacer(
                modifier = Modifier.height(14.dp)
            )


            // --------------------------------------------------------
            // BOTONES DE FITY
            // --------------------------------------------------------

            Row(

                modifier = Modifier.fillMaxWidth(),

                horizontalArrangement = Arrangement.spacedBy(10.dp),

                verticalAlignment = Alignment.CenterVertically

            ) {


                // Botón para escuchar a Fity.
                TextToSpeechButton(

                    textToRead = presentationText,

                    modifier = Modifier.weight(1f)

                )


                // Botón para futura presentación visual.
                Button(

                    onClick = {

                        println(
                            "Reproducir presentación visual"
                        )

                    },

                    modifier = Modifier.height(44.dp),

                    shape = RoundedCornerShape(18.dp),

                    colors = ButtonDefaults.buttonColors(

                        containerColor = Color(0xFFBFEA7C),

                        contentColor = Color(0xFF0F766E)

                    ),

                    contentPadding = PaddingValues(

                        horizontal = 12.dp

                    )

                ) {

                    Icon(

                        imageVector = Icons.Default.PlayArrow,

                        contentDescription = "Reproducir",

                        modifier = Modifier.size(22.dp)

                    )

                }

            }

        }

    }

}


// ============================================================
// IMAGEN TEMPORAL DE FITY
// ============================================================

@Composable
fun MascotImagePlaceholder(

    modifier: Modifier = Modifier

) {

    Box(

        modifier = modifier

            .clip(
                RoundedCornerShape(24.dp)
            )

            .background(

                brush = Brush.radialGradient(

                    colors = listOf(

                        Color(0xFFB8F7E8),

                        Color(0xFFF7FCEB)

                    )

                )

            ),

        contentAlignment = Alignment.Center

    ) {


        /*
         * Cuando tengamos la imagen real de Fity,
         * este icono se podrá reemplazar por:
         *
         * Image(
         *     painter = painterResource(
         *         id = R.drawable.fity
         *     ),
         *     contentDescription = "Fity",
         *     modifier = Modifier.fillMaxSize()
         * )
         */


        Icon(

            imageVector = Icons.Default.Spa,

            contentDescription = "Fity",

            tint = Color(0xFF0F766E),

            modifier = Modifier.size(52.dp)

        )

    }

}


// ============================================================
// BOTÓN PARA TEXTO A VOZ
// ============================================================

@Composable
fun TextToSpeechButton(

    textToRead: String,

    modifier: Modifier = Modifier

) {


    // Obtiene el contexto actual de Android.
    val context = LocalContext.current


    // Guarda el motor TextToSpeech.
    var textToSpeech by remember {

        mutableStateOf<TextToSpeech?>(null)

    }


    // Indica si el motor ya está listo.
    var isReady by remember {

        mutableStateOf(false)

    }


    // --------------------------------------------------------
    // INICIALIZAR TEXT TO SPEECH
    // --------------------------------------------------------

    DisposableEffect(Unit) {


        val tts = TextToSpeech(context) { status ->


            if (status == TextToSpeech.SUCCESS) {


                // Configura español de México.
                textToSpeech?.language = Locale(
                    "es",
                    "MX"
                )


                isReady = true

            }

        }


        // Guarda el motor creado.
        textToSpeech = tts


        // ----------------------------------------------------
        // LIBERAR RECURSOS
        // ----------------------------------------------------

        onDispose {

            tts.stop()

            tts.shutdown()

        }

    }


    // --------------------------------------------------------
    // BOTÓN ESCUCHAR
    // --------------------------------------------------------

    Button(

        onClick = {


            if (isReady) {


                textToSpeech?.speak(

                    textToRead,

                    TextToSpeech.QUEUE_FLUSH,

                    null,

                    "vibra_la_vida_presentacion"

                )

            }

        },

        modifier = modifier.height(44.dp),

        shape = RoundedCornerShape(18.dp),

        colors = ButtonDefaults.buttonColors(

            containerColor = Color(0xFF0F766E),

            contentColor = Color.White

        ),

        contentPadding = PaddingValues(

            horizontal = 14.dp

        )

    ) {


        Icon(

            imageVector = Icons.Default.VolumeUp,

            contentDescription = "Escuchar",

            modifier = Modifier.size(19.dp)

        )


        Spacer(
            modifier = Modifier.width(8.dp)
        )


        Text(

            text = "Escuchar",

            fontSize = 14.sp,

            fontWeight = FontWeight.Bold

        )

    }

}


// ============================================================
// TARJETA REUTILIZABLE DE CATEGORÍAS
// ============================================================

@Composable
fun HomeCategoryCard(

    modifier: Modifier = Modifier,

    // Nombre de la categoría.
    title: String,

    // Pequeña explicación.
    subtitle: String,

    // Fondo de la tarjeta.
    backgroundColor: Color,

    // Icono mostrado en la tarjeta.
    icon: @Composable () -> Unit,

    // Acción del botón.
    onClick: () -> Unit

) {


    Card(

        modifier = modifier

            // Todas las tarjetas tendrán como mínimo
            // la misma altura.
            .defaultMinSize(
                minHeight = 170.dp
            ),

        shape = RoundedCornerShape(24.dp),

        colors = CardDefaults.cardColors(

            containerColor = backgroundColor

        ),

        elevation = CardDefaults.cardElevation(

            defaultElevation = 6.dp

        )

    ) {


        Column(

            modifier = Modifier

                .fillMaxSize()

                .padding(14.dp),

            horizontalAlignment = Alignment.CenterHorizontally,

            verticalArrangement = Arrangement.Center

        ) {


            // --------------------------------------------------------
            // ICONO
            // --------------------------------------------------------

            Box(

                modifier = Modifier

                    .size(58.dp)

                    .clip(
                        RoundedCornerShape(18.dp)
                    )

                    .background(

                        Color.White.copy(
                            alpha = 0.72f
                        )

                    ),

                contentAlignment = Alignment.Center

            ) {


                icon()

            }


            Spacer(
                modifier = Modifier.height(10.dp)
            )


            // --------------------------------------------------------
            // TÍTULO
            // --------------------------------------------------------

            Text(

                text = title,

                color = Color(0xFF0F172A),

                fontSize = 13.sp,

                fontWeight = FontWeight.Bold,

                textAlign = TextAlign.Center,

                lineHeight = 16.sp

            )


            Spacer(
                modifier = Modifier.height(4.dp)
            )


            // --------------------------------------------------------
            // DESCRIPCIÓN
            // --------------------------------------------------------

            Text(

                text = subtitle,

                color = Color(0xFF64748B),

                fontSize = 11.sp,

                textAlign = TextAlign.Center,

                lineHeight = 14.sp

            )


            Spacer(
                modifier = Modifier.height(10.dp)
            )


            // --------------------------------------------------------
            // BOTÓN VAMOS
            // --------------------------------------------------------

            Button(

                onClick = onClick,

                modifier = Modifier.height(32.dp),

                shape = RoundedCornerShape(16.dp),

                colors = ButtonDefaults.buttonColors(

                    containerColor = Color(0xFF86A327),

                    contentColor = Color.White

                ),

                contentPadding = PaddingValues(

                    horizontal = 16.dp,

                    vertical = 0.dp

                )

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


// ============================================================
// MENÚ LATERAL
// ============================================================

@Composable
fun HomeDrawerContent(

    onClose: () -> Unit

) {


    Column(

        modifier = Modifier

            .fillMaxHeight()

            .width(280.dp)

            .background(
                Color(0xFFFEFFF6)
            )

            .padding(18.dp)

    ) {


        // --------------------------------------------------------
        // ENCABEZADO
        // --------------------------------------------------------

        Row(

            modifier = Modifier.fillMaxWidth(),

            verticalAlignment = Alignment.CenterVertically

        ) {


            Text(

                text = "Vibra la vida",

                color = Color(0xFF0F766E),

                fontSize = 22.sp,

                fontWeight = FontWeight.Bold,

                modifier = Modifier.weight(1f)

            )


            IconButton(

                onClick = onClose

            ) {


                Icon(

                    imageVector = Icons.Default.Close,

                    contentDescription = "Cerrar menú",

                    tint = Color(0xFF64748B)

                )

            }

        }


        Spacer(
            modifier = Modifier.height(18.dp)
        )


        Text(

            text = "Menú",

            color = Color(0xFF64748B),

            fontSize = 14.sp,

            fontWeight = FontWeight.SemiBold

        )


        Spacer(
            modifier = Modifier.height(12.dp)
        )


        // --------------------------------------------------------
        // INICIO
        // --------------------------------------------------------

        NavigationDrawerItem(

            label = {

                Text(
                    text = "Inicio"
                )

            },

            selected = true,

            onClick = onClose,

            icon = {

                Icon(

                    imageVector = Icons.Default.Spa,

                    contentDescription = null

                )

            },

            colors = NavigationDrawerItemDefaults.colors(

                selectedContainerColor = Color(0xFFD9F99D),

                selectedIconColor = Color(0xFF0F766E),

                selectedTextColor = Color(0xFF0F766E)

            )

        )


        // --------------------------------------------------------
        // RECORDATORIOS
        // --------------------------------------------------------

        NavigationDrawerItem(

            label = {

                Text(
                    text = "Recordatorios"
                )

            },

            selected = false,

            onClick = onClose,

            icon = {

                Icon(

                    imageVector = Icons.Default.Favorite,

                    contentDescription = null

                )

            }

        )


        // --------------------------------------------------------
        // CONFIGURACIÓN
        // --------------------------------------------------------

        NavigationDrawerItem(

            label = {

                Text(
                    text = "Configuración"
                )

            },

            selected = false,

            onClick = onClose,

            icon = {

                Icon(

                    imageVector = Icons.Default.AccountCircle,

                    contentDescription = null

                )

            }

        )


        // Empuja el texto inferior hasta abajo.
        Spacer(
            modifier = Modifier.weight(1f)
        )


        Text(

            text = "Este menú queda como base para futuras opciones.",

            color = Color(0xFF94A3B8),

            fontSize = 12.sp,

            lineHeight = 16.sp

        )

    }

}