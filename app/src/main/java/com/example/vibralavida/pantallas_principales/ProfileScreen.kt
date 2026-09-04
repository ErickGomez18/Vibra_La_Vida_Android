package com.example.vibralavida.pantallas_principales

import com.example.vibralavida.backgroundGradient

import android.net.Uri

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Height
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.MonitorWeight
import androidx.compose.material.icons.filled.Person

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.layout.ContentScale

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import coil.compose.rememberAsyncImagePainter


// ============================================================================
// PROFILE SCREEN
// ============================================================================

@Composable
fun ProfileScreen(

    userName: String,

    age: String,

    weight: String,

    height: String,

    activityLevel: String,

    profileImageUri: Uri?,

    onBack: () -> Unit,

    onEditProfile: () -> Unit,

    onImageSelected: (Uri) -> Unit,

    // ========================================================================
    // CERRAR SESIÓN
    // ========================================================================

    onLogout: () -> Unit

) {


    // ========================================================================
    // SELECTOR DE IMAGEN
    // ========================================================================

    val imagePickerLauncher =
        rememberLauncherForActivityResult(

            contract =
                ActivityResultContracts.GetContent()

        ) { uri ->

            if (uri != null) {

                onImageSelected(
                    uri
                )
            }
        }


    // ========================================================================
    // CONTENEDOR PRINCIPAL
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


        Column(

            modifier =
                Modifier
                    .fillMaxSize()
                    .verticalScroll(
                        rememberScrollState()
                    )
                    .padding(
                        horizontal = 22.dp,
                        vertical = 18.dp
                    ),

            horizontalAlignment =
                Alignment.CenterHorizontally

        ) {


            // =================================================================
            // BARRA SUPERIOR
            // =================================================================

            ProfileTopBar(

                onBack =
                    onBack
            )


            Spacer(

                modifier =
                    Modifier.height(
                        18.dp
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
                            max = 430.dp
                        ),

                shape =
                    RoundedCornerShape(
                        32.dp
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
                                horizontal = 24.dp,
                                vertical = 28.dp
                            ),

                    horizontalAlignment =
                        Alignment.CenterHorizontally

                ) {


                    // =========================================================
                    // TÍTULO
                    // =========================================================

                    Text(

                        text =
                            "Cuenta",

                        fontSize =
                            26.sp,

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
                            "Información personal del usuario",

                        fontSize =
                            14.sp,

                        color =
                            Color(0xFF64748B),

                        textAlign =
                            TextAlign.Center
                    )


                    Spacer(

                        modifier =
                            Modifier.height(
                                28.dp
                            )
                    )


                    // =========================================================
                    // FOTO DE PERFIL
                    // =========================================================

                    ProfileAvatar(

                        imageUri =
                            profileImageUri,

                        onClick = {

                            imagePickerLauncher
                                .launch(
                                    "image/*"
                                )
                        }
                    )


                    Spacer(

                        modifier =
                            Modifier.height(
                                18.dp
                            )
                    )


                    // =========================================================
                    // NOMBRE
                    // =========================================================

                    Text(

                        text =
                            userName.ifBlank {
                                "Usuario"
                            },

                        fontSize =
                            19.sp,

                        fontWeight =
                            FontWeight.Bold,

                        color =
                            Color(0xFF111827),

                        textAlign =
                            TextAlign.Center
                    )


                    Spacer(

                        modifier =
                            Modifier.height(
                                6.dp
                            )
                    )


                    Text(

                        text =
                            "Perfil de bienestar",

                        fontSize =
                            13.sp,

                        color =
                            Color(0xFF64748B),

                        textAlign =
                            TextAlign.Center
                    )


                    Spacer(

                        modifier =
                            Modifier.height(
                                26.dp
                            )
                    )


                    // =========================================================
                    // PESO Y ESTATURA
                    // =========================================================

                    Row(

                        modifier =
                            Modifier.fillMaxWidth(),

                        horizontalArrangement =
                            Arrangement.spacedBy(
                                12.dp
                            )

                    ) {


                        ProfileInfoCard(

                            modifier =
                                Modifier.weight(
                                    1f
                                ),

                            icon = {

                                Icon(

                                    imageVector =
                                        Icons.Default.MonitorWeight,

                                    contentDescription =
                                        null,

                                    tint =
                                        Color(0xFF86A327)
                                )
                            },

                            value =
                                if (
                                    weight.isBlank()
                                ) {

                                    "--"

                                } else {

                                    weight
                                },

                            label =
                                "Peso",

                            unit =
                                "kg"
                        )


                        ProfileInfoCard(

                            modifier =
                                Modifier.weight(
                                    1f
                                ),

                            icon = {

                                Icon(

                                    imageVector =
                                        Icons.Default.Height,

                                    contentDescription =
                                        null,

                                    tint =
                                        Color(0xFF0F766E)
                                )
                            },

                            value =
                                if (
                                    height.isBlank()
                                ) {

                                    "--"

                                } else {

                                    height
                                },

                            label =
                                "Estatura",

                            unit =
                                "m"
                        )
                    }


                    Spacer(

                        modifier =
                            Modifier.height(
                                14.dp
                            )
                    )


                    // =========================================================
                    // EDAD Y ACTIVIDAD
                    // =========================================================

                    Row(

                        modifier =
                            Modifier.fillMaxWidth(),

                        horizontalArrangement =
                            Arrangement.spacedBy(
                                12.dp
                            )

                    ) {


                        ProfileInfoCard(

                            modifier =
                                Modifier.weight(
                                    1f
                                ),

                            icon = {

                                Icon(

                                    imageVector =
                                        Icons.Default.Cake,

                                    contentDescription =
                                        null,

                                    tint =
                                        Color(0xFF0284C7)
                                )
                            },

                            value =
                                if (
                                    age.isBlank()
                                ) {

                                    "--"

                                } else {

                                    age
                                },

                            label =
                                "Edad",

                            unit =
                                "años"
                        )


                        ProfileInfoCard(

                            modifier =
                                Modifier.weight(
                                    1f
                                ),

                            icon = {

                                Icon(

                                    imageVector =
                                        Icons.Default.FitnessCenter,

                                    contentDescription =
                                        null,

                                    tint =
                                        Color(0xFF9333EA)
                                )
                            },

                            value =
                                if (
                                    activityLevel.isBlank()
                                ) {

                                    "--"

                                } else {

                                    activityLevel
                                },

                            label =
                                "Actividad",

                            unit =
                                ""
                        )
                    }


                    Spacer(

                        modifier =
                            Modifier.height(
                                26.dp
                            )
                    )


                    // =========================================================
                    // DIVISOR
                    // =========================================================

                    HorizontalDivider(

                        color =
                            Color(0xFFE2E8F0),

                        thickness =
                            1.dp
                    )


                    Spacer(

                        modifier =
                            Modifier.height(
                                24.dp
                            )
                    )


                    // =========================================================
                    // EDITAR PERFIL
                    // =========================================================

                    Button(

                        onClick =
                            onEditProfile,

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
                                    Color.White
                            )

                    ) {


                        Icon(

                            imageVector =
                                Icons.Default.Edit,

                            contentDescription =
                                null,

                            modifier =
                                Modifier.size(
                                    20.dp
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
                                "Editar perfil",

                            fontSize =
                                16.sp,

                            fontWeight =
                                FontWeight.Bold
                        )
                    }


                    Spacer(

                        modifier =
                            Modifier.height(
                                12.dp
                            )
                    )


                    // =========================================================
                    // CAMBIAR FOTO
                    // =========================================================

                    OutlinedButton(

                        onClick = {

                            imagePickerLauncher
                                .launch(
                                    "image/*"
                                )
                        },

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


                        Icon(

                            imageVector =
                                Icons.Default.CameraAlt,

                            contentDescription =
                                null,

                            modifier =
                                Modifier.size(
                                    20.dp
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
                                "Cambiar foto",

                            fontSize =
                                15.sp,

                            fontWeight =
                                FontWeight.SemiBold
                        )
                    }


                    // =========================================================
                    // ESPACIO ANTES DE CERRAR SESIÓN
                    // =========================================================

                    Spacer(

                        modifier =
                            Modifier.height(
                                20.dp
                            )
                    )


                    // =========================================================
                    // DIVISOR
                    // =========================================================

                    HorizontalDivider(

                        color =
                            Color(0xFFFECACA),

                        thickness =
                            1.dp
                    )


                    Spacer(

                        modifier =
                            Modifier.height(
                                20.dp
                            )
                    )


                    // =========================================================
                    // CERRAR SESIÓN
                    // =========================================================
                    //
                    // Este botón NO cierra Firebase directamente.
                    //
                    // Solamente llama al callback onLogout.
                    //
                    // MainActivity será quien se encargue de:
                    //
                    // FirebaseAuth.signOut()
                    // limpiar estados
                    // regresar al inicio
                    //
                    // =========================================================

                    Button(

                        onClick =
                            onLogout,

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

                        colors =
                            ButtonDefaults.buttonColors(

                                containerColor =
                                    Color(0xFFDC2626),

                                contentColor =
                                    Color.White
                            )

                    ) {


                        Icon(

                            imageVector =
                                Icons.Default.Logout,

                            contentDescription =
                                null,

                            modifier =
                                Modifier.size(
                                    20.dp
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
                                "Cerrar sesión",

                            fontSize =
                                15.sp,

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
// TOP BAR
// ============================================================================

@Composable
fun ProfileTopBar(

    onBack: () -> Unit

) {

    Row(

        modifier =
            Modifier
                .fillMaxWidth()
                .widthIn(
                    max = 430.dp
                )
                .defaultMinSize(
                    minHeight = 54.dp
                ),

        verticalAlignment =
            Alignment.CenterVertically

    ) {


        IconButton(

            onClick =
                onBack

        ) {

            Icon(

                imageVector =
                    Icons.Default.ArrowBack,

                contentDescription =
                    "Volver",

                tint =
                    Color(0xFF0F766E),

                modifier =
                    Modifier.size(
                        28.dp
                    )
            )
        }


        Text(

            text =
                "Mi perfil",

            color =
                Color(0xFF0F172A),

            fontSize =
                18.sp,

            fontWeight =
                FontWeight.Bold,

            modifier =
                Modifier.weight(
                    1f
                ),

            textAlign =
                TextAlign.Center
        )


        Spacer(

            modifier =
                Modifier.size(
                    48.dp
                )
        )
    }
}


// ============================================================================
// AVATAR
// ============================================================================

@Composable
fun ProfileAvatar(

    imageUri: Uri?,

    onClick: () -> Unit

) {

    Box(

        modifier =
            Modifier
                .size(
                    132.dp
                )
                .clip(
                    CircleShape
                )
                .background(

                    brush =
                        Brush.radialGradient(

                            colors =
                                listOf(

                                    Color(0xFFFFE7C7),

                                    Color(0xFFD9F99D)
                                )
                        )
                )
                .clickable {

                    onClick()
                },

        contentAlignment =
            Alignment.Center

    ) {


        // ====================================================================
        // FOTO
        // ====================================================================

        if (
            imageUri != null
        ) {

            Image(

                painter =
                    rememberAsyncImagePainter(
                        imageUri
                    ),

                contentDescription =
                    "Foto de perfil",

                modifier =
                    Modifier
                        .fillMaxSize()
                        .clip(
                            CircleShape
                        ),

                contentScale =
                    ContentScale.Crop
            )

        } else {

            Icon(

                imageVector =
                    Icons.Default.Person,

                contentDescription =
                    "Foto de perfil",

                tint =
                    Color(0xFF7C4A2D),

                modifier =
                    Modifier.size(
                        72.dp
                    )
            )
        }


        // ====================================================================
        // BOTÓN CÁMARA
        // ====================================================================

        Box(

            modifier =
                Modifier
                    .align(
                        Alignment.BottomEnd
                    )
                    .size(
                        38.dp
                    )
                    .clip(
                        CircleShape
                    )
                    .background(
                        Color(0xFF0F766E)
                    ),

            contentAlignment =
                Alignment.Center

        ) {

            Icon(

                imageVector =
                    Icons.Default.CameraAlt,

                contentDescription =
                    "Cambiar foto",

                tint =
                    Color.White,

                modifier =
                    Modifier.size(
                        20.dp
                    )
            )
        }
    }
}


// ============================================================================
// TARJETA DE INFORMACIÓN
// ============================================================================

@Composable
fun ProfileInfoCard(

    modifier: Modifier =
        Modifier,

    icon:
    @Composable () -> Unit,

    value: String,

    label: String,

    unit: String

) {

    Card(

        modifier =
            modifier
                .defaultMinSize(
                    minHeight = 120.dp
                ),

        shape =
            RoundedCornerShape(
                24.dp
            ),

        colors =
            CardDefaults.cardColors(

                containerColor =
                    Color(0xFFF7FCEB)
            ),

        elevation =
            CardDefaults.cardElevation(

                defaultElevation =
                    4.dp
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


            icon()


            Spacer(

                modifier =
                    Modifier.height(
                        8.dp
                    )
            )


            Text(

                text =
                    value,

                color =
                    Color(0xFF111827),

                fontSize =
                    if (
                        value.length > 8
                    ) {

                        16.sp

                    } else {

                        22.sp
                    },

                fontWeight =
                    FontWeight.Bold,

                textAlign =
                    TextAlign.Center,

                maxLines =
                    1
            )


            if (
                unit.isNotBlank()
            ) {

                Text(

                    text =
                        unit,

                    color =
                        Color(0xFF64748B),

                    fontSize =
                        12.sp,

                    textAlign =
                        TextAlign.Center
                )
            }


            Spacer(

                modifier =
                    Modifier.height(
                        4.dp
                    )
            )


            Text(

                text =
                    label,

                color =
                    Color(0xFF0F766E),

                fontSize =
                    13.sp,

                fontWeight =
                    FontWeight.SemiBold,

                textAlign =
                    TextAlign.Center
            )
        }
    }
}