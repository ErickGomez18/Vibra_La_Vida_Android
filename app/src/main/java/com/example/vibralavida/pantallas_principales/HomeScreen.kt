package com.example.vibralavida.pantallas_principales

// ============================================================================
// VIBRA LA VIDA
// ============================================================================

import com.example.vibralavida.R
import com.example.vibralavida.backgroundGradient
import com.example.vibralavida.ia.NubyTtsManager

// ============================================================================
// COMPOSE FOUNDATION
// ============================================================================

import androidx.compose.foundation.Image
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

// ============================================================================
// ICONOS
// ============================================================================

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.VolumeUp

// ============================================================================
// MATERIAL 3
// ============================================================================

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState

// ============================================================================
// ESTADOS
// ============================================================================

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope

// ============================================================================
// UI
// ============================================================================

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ============================================================================
// CORRUTINAS
// ============================================================================

import kotlinx.coroutines.launch


// ============================================================================
// HOME SCREEN
// ============================================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(

    userName: String = "Usuario",

    onProfileClick: () -> Unit,

    onRhythmClick: () -> Unit,

    onHealthyLifeClick: () -> Unit,

    onAgendaClick: () -> Unit,

    onDiabetesClick: () -> Unit,

    // NUEVO:
    // abre la pantalla interactiva de Nuby.
    onNubyClick: () -> Unit,

    // NUEVO:
    // abre el mapa de profesionales y consultorios.
    //
    // Tiene un valor por defecto para que MainActivity
    // siga compilando mientras conectamos la nueva pantalla.
    onProfessionalsMapClick: () -> Unit = {},

    // Abre Mi equipo de salud.
    onHealthTeamClick: () -> Unit = {}
) {

    // ========================================================================
    // DRAWER
    // ========================================================================

    val drawerState =
        rememberDrawerState(
            initialValue =
                DrawerValue.Closed
        )


    val scope =
        rememberCoroutineScope()


    // ========================================================================
    // CONTENEDOR CON DRAWER
    // ========================================================================

    ModalNavigationDrawer(

        drawerState =
            drawerState,

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

            // =================================================================
            // DECORACIÓN
            // =================================================================

            BackgroundBlurCircle(

                modifier =
                    Modifier
                        .align(
                            Alignment.TopCenter
                        )
                        .padding(
                            top = 40.dp
                        )
            )


            // =================================================================
            // CONTENIDO
            // =================================================================

            Column(

                modifier =
                    Modifier
                        .fillMaxSize()
                        .verticalScroll(
                            rememberScrollState()
                        )
                        .padding(
                            horizontal = 18.dp,
                            vertical = 18.dp
                        ),

                horizontalAlignment =
                    Alignment.CenterHorizontally
            ) {

                // =============================================================
                // TOP BAR
                // =============================================================

                HomeTopBar(

                    userName =
                        userName,

                    onMenuClick = {

                        scope.launch {

                            drawerState.open()
                        }
                    },

                    onProfileClick =
                        onProfileClick
                )


                Spacer(

                    modifier =
                        Modifier.height(
                            18.dp
                        )
                )


                // =============================================================
                // NUBY
                // =============================================================

                MascotPresentationCard(

                    onNubyClick =
                        onNubyClick
                )


                Spacer(

                    modifier =
                        Modifier.height(
                            18.dp
                        )
                )


                // =============================================================
                // PROFESIONALES CERCA DE TI
                // =============================================================

                ProfessionalsMapCard(

                    onClick =
                        onProfessionalsMapClick
                )


                Spacer(

                    modifier =
                        Modifier.height(
                            14.dp
                        )
                )


                // =============================================================
                // MI EQUIPO DE SALUD
                // =============================================================

                Card(

                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .widthIn(
                                max = 430.dp
                            )
                            .clickable {
                                onHealthTeamClick()
                            },

                    shape =
                        RoundedCornerShape(
                            22.dp
                        ),

                    colors =
                        CardDefaults.cardColors(
                            containerColor =
                                Color(0xFFE9F8F0)
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
                                Icons.Default.HealthAndSafety,

                            contentDescription =
                                null,

                            tint =
                                Color(0xFF0F766E),

                            modifier =
                                Modifier.size(
                                    40.dp
                                )
                        )


                        Spacer(

                            modifier =
                                Modifier.width(
                                    14.dp
                                )
                        )


                        Column {

                            Text(

                                text =
                                    "Mi equipo de salud",

                                color =
                                    Color(0xFF123B5D),

                                fontWeight =
                                    FontWeight.Bold,

                                fontSize =
                                    17.sp
                            )


                            Text(

                                text =
                                    "Solicitudes y profesionales autorizados",

                                color =
                                    Color(0xFF64748B),

                                fontSize =
                                    12.sp
                            )
                        }
                    }
                }


                Spacer(

                    modifier =
                        Modifier.height(
                            22.dp
                        )
                )


                // =============================================================
                // TÍTULO
                // =============================================================

                Text(

                    text =
                        "Selecciona el campo que deseas visitar",

                    color =
                        Color(0xFF0F172A),

                    fontSize =
                        16.sp,

                    fontWeight =
                        FontWeight.SemiBold,

                    textAlign =
                        TextAlign.Center
                )


                Spacer(

                    modifier =
                        Modifier.height(
                            18.dp
                        )
                )


                // =============================================================
                // FILA 1
                // =============================================================

                Row(

                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .widthIn(
                                max = 430.dp
                            ),

                    horizontalArrangement =
                        Arrangement.spacedBy(
                            14.dp
                        )
                ) {

                    // ---------------------------------------------------------
                    // TRASTORNOS DEL RITMO
                    // ---------------------------------------------------------

                    HomeCategoryCard(

                        modifier =
                            Modifier.weight(
                                1f
                            ),

                        title =
                            "Trastornos del ritmo",

                        subtitle =
                            "Seguimiento y orientación",

                        backgroundColor =
                            Color(0xFFF7FCEB),

                        icon = {

                            Icon(

                                imageVector =
                                    Icons.Default.Favorite,

                                contentDescription =
                                    null,

                                tint =
                                    Color(0xFFEF4444),

                                modifier =
                                    Modifier.size(
                                        42.dp
                                    )
                            )
                        },

                        onClick =
                            onRhythmClick
                    )


                    // ---------------------------------------------------------
                    // VIDA SALUDABLE
                    // ---------------------------------------------------------

                    HomeCategoryCard(

                        modifier =
                            Modifier.weight(
                                1f
                            ),

                        title =
                            "Vida saludable",

                        subtitle =
                            "Hábitos y bienestar",

                        backgroundColor =
                            Color(0xFFEAF8FF),

                        icon = {

                            Icon(

                                imageVector =
                                    Icons.Default.Spa,

                                contentDescription =
                                    null,

                                tint =
                                    Color(0xFF0284C7),

                                modifier =
                                    Modifier.size(
                                        42.dp
                                    )
                            )
                        },

                        onClick =
                            onHealthyLifeClick
                    )
                }


                Spacer(

                    modifier =
                        Modifier.height(
                            14.dp
                        )
                )


                // =============================================================
                // FILA 2
                // =============================================================

                Row(

                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .widthIn(
                                max = 430.dp
                            ),

                    horizontalArrangement =
                        Arrangement.spacedBy(
                            14.dp
                        )
                ) {

                    // ---------------------------------------------------------
                    // MI AGENDA
                    // ---------------------------------------------------------

                    HomeCategoryCard(

                        modifier =
                            Modifier.weight(
                                1f
                            ),

                        title =
                            "Mi Agenda",

                        subtitle =
                            "Medicamentos, citas y estudios",

                        backgroundColor =
                            Color(0xFFE9F8F0),

                        icon = {

                            Icon(

                                imageVector =
                                    Icons.Default.CalendarMonth,

                                contentDescription =
                                    null,

                                tint =
                                    Color(0xFF0F766E),

                                modifier =
                                    Modifier.size(
                                        42.dp
                                    )
                            )
                        },

                        onClick =
                            onAgendaClick
                    )


                    // ---------------------------------------------------------
                    // DIABETES
                    // ---------------------------------------------------------

                    HomeCategoryCard(

                        modifier =
                            Modifier.weight(
                                1f
                            ),

                        title =
                            "Diabetes Mellitus",

                        subtitle =
                            "Control y prevención",

                        backgroundColor =
                            Color(0xFFFBFFCC),

                        icon = {

                            Icon(

                                imageVector =
                                    Icons.Default.HealthAndSafety,

                                contentDescription =
                                    null,

                                tint =
                                    Color(0xFF86A327),

                                modifier =
                                    Modifier.size(
                                        44.dp
                                    )
                            )
                        },

                        onClick =
                            onDiabetesClick
                    )
                }


                Spacer(

                    modifier =
                        Modifier.height(
                            40.dp
                        )
                )


                // =============================================================
                // BARRA DECORATIVA
                // =============================================================

                Box(

                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .widthIn(
                                max = 430.dp
                            )
                            .height(
                                58.dp
                            )
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


// ============================================================================
// TOP BAR
// ============================================================================

@Composable
fun HomeTopBar(

    userName: String,

    onMenuClick: () -> Unit,

    onProfileClick: () -> Unit
) {

    Row(

        modifier =
            Modifier
                .fillMaxWidth()
                .widthIn(
                    max = 430.dp
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
                    )
                    .clickable {

                        onProfileClick()
                    },

            contentAlignment =
                Alignment.Center
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
                        36.dp
                    )
            )
        }
    }
}


// ============================================================================
// TARJETA DE NUBY
// ============================================================================

@Composable
fun MascotPresentationCard(

    onNubyClick: () -> Unit
) {

    val presentationText =
        """
        Hola, soy Nuby, tu guía interactiva de Vibra la vida.

        Puedo orientarte sobre alimentación, actividad física, sueño, hábitos saludables y ayudarte a resolver tus dudas dentro de la aplicación.
        """.trimIndent()


    Card(

        modifier =
            Modifier
                .fillMaxWidth()
                .widthIn(
                    max = 430.dp
                ),

        shape =
            RoundedCornerShape(
                28.dp
            ),

        colors =
            CardDefaults.cardColors(

                containerColor =
                    Color(0xFFFEFFF6)
            ),

        elevation =
            CardDefaults.cardElevation(

                defaultElevation =
                    8.dp
            )
    ) {

        Column(

            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(
                        18.dp
                    )
        ) {

            Row(

                modifier =
                    Modifier.fillMaxWidth(),

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                // =============================================================
                // IMAGEN REAL DE NUBY
                // =============================================================

                MascotImage(

                    modifier =
                        Modifier.size(
                            118.dp
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
                            "Conoce a “Nuby”",

                        color =
                            Color(0xFF0F766E),

                        fontSize =
                            17.sp,

                        fontWeight =
                            FontWeight.Bold
                    )


                    Spacer(

                        modifier =
                            Modifier.height(
                                6.dp
                            )
                    )


                    Text(

                        text =
                            "Tu guía interactiva dentro de Vibra la vida.",

                        color =
                            Color(0xFF334155),

                        fontSize =
                            13.sp,

                        lineHeight =
                            18.sp,

                        fontWeight =
                            FontWeight.Medium
                    )
                }
            }


            Spacer(

                modifier =
                    Modifier.height(
                        14.dp
                    )
            )


            Text(

                text =
                    "Nuby puede orientarte, resolver dudas y acompañarte en el cuidado de tus hábitos y bienestar.",

                color =
                    Color(0xFF334155),

                fontSize =
                    13.sp,

                lineHeight =
                    19.sp,

                textAlign =
                    TextAlign.Justify
            )


            Spacer(

                modifier =
                    Modifier.height(
                        14.dp
                    )
            )


            Row(

                modifier =
                    Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.spacedBy(
                        10.dp
                    ),

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                // =============================================================
                // ESCUCHAR
                // =============================================================

                TextToSpeechButton(

                    textToRead =
                        presentationText,

                    modifier =
                        Modifier.weight(
                            1f
                        )
                )


                // =============================================================
                // ENTRAR A NUBY
                // =============================================================

                Button(

                    onClick =
                        onNubyClick,

                    modifier =
                        Modifier.height(
                            44.dp
                        ),

                    shape =
                        RoundedCornerShape(
                            18.dp
                        ),

                    colors =
                        ButtonDefaults.buttonColors(

                            containerColor =
                                Color(0xFFBFEA7C),

                            contentColor =
                                Color(0xFF0F766E)
                        ),

                    contentPadding =
                        PaddingValues(
                            horizontal = 12.dp
                        )
                ) {

                    Icon(

                        imageVector =
                            Icons.Default.PlayArrow,

                        contentDescription =
                            "Abrir Nuby",

                        modifier =
                            Modifier.size(
                                22.dp
                            )
                    )
                }
            }
        }
    }
}


// ============================================================================
// IMAGEN DE NUBY
// ============================================================================

@Composable
fun MascotImage(
    modifier: Modifier = Modifier
) {

    Box(

        modifier =
            modifier
                .clip(
                    RoundedCornerShape(
                        24.dp
                    )
                )
                .background(

                    brush =
                        Brush.radialGradient(

                            colors =
                                listOf(

                                    Color(0xFFB8F7E8),

                                    Color(0xFFF7FCEB)
                                )
                        )
                )
                .padding(
                    4.dp
                ),

        contentAlignment =
            Alignment.Center
    ) {

        Image(

            painter =
                painterResource(
                    id =
                        R.drawable.nuby_saludo
                ),

            contentDescription =
                "Nuby, mascota virtual de Vibra la vida",

            modifier =
                Modifier.fillMaxSize(),

            contentScale =
                ContentScale.Fit
        )
    }
}


// ============================================================================
// TTS DE NUBY
// ============================================================================
//
// Ya NO creamos un TextToSpeech independiente aquí.
//
// Reutilizamos NubyTtsManager para:
// - es-MX
// - mejor selección de voz disponible
// - velocidad 0.92
// - tono 1.05
//
// ============================================================================

@Composable
fun TextToSpeechButton(

    textToRead: String,

    modifier: Modifier = Modifier
) {

    val context =
        LocalContext.current


    val nubyTtsManager =
        remember {

            NubyTtsManager(
                context
            )
        }


    DisposableEffect(
        Unit
    ) {

        onDispose {

            nubyTtsManager.shutdown()
        }
    }


    Button(

        onClick = {

            nubyTtsManager.speak(
                textToRead
            )
        },

        modifier =
            modifier.height(
                44.dp
            ),

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
            ),

        contentPadding =
            PaddingValues(
                horizontal = 14.dp
            )
    ) {

        Icon(

            imageVector =
                Icons.Default.VolumeUp,

            contentDescription =
                null,

            modifier =
                Modifier.size(
                    19.dp
                )
        )


        Spacer(

            modifier =
                Modifier.width(
                    8.dp
                )
        )


        Text(

            text =
                "Escuchar",

            fontSize =
                14.sp,

            fontWeight =
                FontWeight.Bold
        )
    }
}


// ============================================================================
// TARJETA DE PROFESIONALES
// ============================================================================

/**
 * Acceso desde la pantalla principal al futuro mapa de
 * profesionales y consultorios registrados en Vibra la vida.
 *
 * Por ahora esta tarjeta solamente dispara el callback.
 * La pantalla del mapa se conectará en la siguiente etapa.
 */
@Composable
fun ProfessionalsMapCard(

    onClick: () -> Unit
) {


    Card(

        modifier =
            Modifier
                .fillMaxWidth()
                .widthIn(
                    max = 430.dp
                ),

        shape =
            RoundedCornerShape(
                24.dp
            ),

        colors =
            CardDefaults.cardColors(

                containerColor =
                    Color(0xFFEAF8F4)
            ),

        elevation =
            CardDefaults.cardElevation(

                defaultElevation =
                    6.dp
            )
    ) {


        Row(

            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(
                        16.dp
                    ),

            verticalAlignment =
                Alignment.CenterVertically
        ) {


            // ----------------------------------------------------------------
            // ICONO
            // ----------------------------------------------------------------

            Box(

                modifier =
                    Modifier
                        .size(
                            58.dp
                        )
                        .clip(
                            RoundedCornerShape(
                                18.dp
                            )
                        )
                        .background(
                            Color.White.copy(
                                alpha = 0.78f
                            )
                        ),

                contentAlignment =
                    Alignment.Center
            ) {


                Icon(

                    imageVector =
                        Icons.Default.LocationOn,

                    contentDescription =
                        null,

                    tint =
                        Color(0xFF0F766E),

                    modifier =
                        Modifier.size(
                            34.dp
                        )
                )
            }


            Spacer(

                modifier =
                    Modifier.width(
                        14.dp
                    )
            )


            // ----------------------------------------------------------------
            // TEXTO
            // ----------------------------------------------------------------

            Column(

                modifier =
                    Modifier.weight(
                        1f
                    )
            ) {


                Text(

                    text =
                        "Profesionales cerca de ti",

                    color =
                        Color(0xFF0F172A),

                    fontSize =
                        15.sp,

                    fontWeight =
                        FontWeight.Bold
                )


                Spacer(

                    modifier =
                        Modifier.height(
                            4.dp
                        )
                )


                Text(

                    text =
                        "Encuentra especialistas y consultorios registrados en Vibra la vida.",

                    color =
                        Color(0xFF64748B),

                    fontSize =
                        11.sp,

                    lineHeight =
                        15.sp
                )


                Spacer(

                    modifier =
                        Modifier.height(
                            10.dp
                        )
                )


                Button(

                    onClick =
                        onClick,

                    modifier =
                        Modifier.height(
                            34.dp
                        ),

                    shape =
                        RoundedCornerShape(
                            16.dp
                        ),

                    colors =
                        ButtonDefaults.buttonColors(

                            containerColor =
                                Color(0xFF0F766E),

                            contentColor =
                                Color.White
                        ),

                    contentPadding =
                        PaddingValues(
                            horizontal = 16.dp,
                            vertical = 0.dp
                        )
                ) {


                    Text(

                        text =
                            "Ver mapa",

                        fontSize =
                            12.sp,

                        fontWeight =
                            FontWeight.Bold
                    )
                }
            }
        }
    }
}


// ============================================================================
// TARJETA DE CATEGORÍA
// ============================================================================

@Composable
fun HomeCategoryCard(

    modifier: Modifier = Modifier,

    title: String,

    subtitle: String,

    backgroundColor: Color,

    icon: @Composable () -> Unit,

    onClick: () -> Unit
) {

    Card(

        modifier =
            modifier.defaultMinSize(
                minHeight = 155.dp
            ),

        shape =
            RoundedCornerShape(
                24.dp
            ),

        colors =
            CardDefaults.cardColors(

                containerColor =
                    backgroundColor
            ),

        elevation =
            CardDefaults.cardElevation(

                defaultElevation =
                    6.dp
            )
    ) {

        Column(

            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(
                        14.dp
                    ),

            horizontalAlignment =
                Alignment.CenterHorizontally,

            verticalArrangement =
                Arrangement.Center
        ) {

            Box(

                modifier =
                    Modifier
                        .size(
                            58.dp
                        )
                        .clip(
                            RoundedCornerShape(
                                18.dp
                            )
                        )
                        .background(

                            Color.White.copy(
                                alpha = 0.72f
                            )
                        ),

                contentAlignment =
                    Alignment.Center
            ) {

                icon()
            }


            Spacer(

                modifier =
                    Modifier.height(
                        10.dp
                    )
            )


            Text(

                text =
                    title,

                color =
                    Color(0xFF0F172A),

                fontSize =
                    13.sp,

                fontWeight =
                    FontWeight.Bold,

                textAlign =
                    TextAlign.Center,

                lineHeight =
                    16.sp
            )


            Spacer(

                modifier =
                    Modifier.height(
                        4.dp
                    )
            )


            Text(

                text =
                    subtitle,

                color =
                    Color(0xFF64748B),

                fontSize =
                    11.sp,

                textAlign =
                    TextAlign.Center,

                lineHeight =
                    14.sp
            )


            Spacer(

                modifier =
                    Modifier.height(
                        10.dp
                    )
            )


            Button(

                onClick =
                    onClick,

                modifier =
                    Modifier.height(
                        32.dp
                    ),

                shape =
                    RoundedCornerShape(
                        16.dp
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
                        horizontal = 16.dp,
                        vertical = 0.dp
                    )
            ) {

                Text(

                    text =
                        "Vamos",

                    fontSize =
                        12.sp,

                    fontWeight =
                        FontWeight.Bold
                )
            }
        }
    }
}


// ============================================================================
// DRAWER
// ============================================================================

@Composable
fun HomeDrawerContent(
    onClose: () -> Unit
) {

    Column(

        modifier =
            Modifier
                .fillMaxHeight()
                .width(
                    280.dp
                )
                .background(
                    Color(0xFFFEFFF6)
                )
                .padding(
                    18.dp
                )
    ) {

        Row(

            modifier =
                Modifier.fillMaxWidth(),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Text(

                text =
                    "Vibra la vida",

                color =
                    Color(0xFF0F766E),

                fontSize =
                    22.sp,

                fontWeight =
                    FontWeight.Bold,

                modifier =
                    Modifier.weight(
                        1f
                    )
            )


            IconButton(

                onClick =
                    onClose
            ) {

                Icon(

                    imageVector =
                        Icons.Default.Close,

                    contentDescription =
                        "Cerrar menú",

                    tint =
                        Color(0xFF64748B)
                )
            }
        }


        Spacer(

            modifier =
                Modifier.height(
                    18.dp
                )
        )


        Text(

            text =
                "Menú",

            color =
                Color(0xFF64748B),

            fontSize =
                14.sp,

            fontWeight =
                FontWeight.SemiBold
        )


        Spacer(

            modifier =
                Modifier.height(
                    12.dp
                )
        )


        NavigationDrawerItem(

            label = {

                Text(
                    text =
                        "Inicio"
                )
            },

            selected =
                true,

            onClick =
                onClose,

            icon = {

                Icon(

                    imageVector =
                        Icons.Default.Spa,

                    contentDescription =
                        null
                )
            },

            colors =
                NavigationDrawerItemDefaults.colors(

                    selectedContainerColor =
                        Color(0xFFD9F99D),

                    selectedIconColor =
                        Color(0xFF0F766E),

                    selectedTextColor =
                        Color(0xFF0F766E)
                )
        )


        NavigationDrawerItem(

            label = {

                Text(
                    text =
                        "Recordatorios"
                )
            },

            selected =
                false,

            onClick =
                onClose,

            icon = {

                Icon(

                    imageVector =
                        Icons.Default.Favorite,

                    contentDescription =
                        null
                )
            }
        )


        NavigationDrawerItem(

            label = {

                Text(
                    text =
                        "Configuración"
                )
            },

            selected =
                false,

            onClick =
                onClose,

            icon = {

                Icon(

                    imageVector =
                        Icons.Default.AccountCircle,

                    contentDescription =
                        null
                )
            }
        )


        Spacer(

            modifier =
                Modifier.weight(
                    1f
                )
        )


        Text(

            text =
                "Este menú queda como base para futuras opciones.",

            color =
                Color(0xFF94A3B8),

            fontSize =
                12.sp,

            lineHeight =
                16.sp
        )
    }
}
