package com.example.vibralavida.agenda.medicamentos

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.NotificationsActive

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.compose.ui.window.Dialog

import coil.compose.rememberAsyncImagePainter


// ============================================================================
// PANTALLA DE MEDICAMENTOS
// ============================================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicamentosScreen(

    medicamentos: List<Medicamento>,

    onBack: () -> Unit,

    onAgregarMedicamentoClick: () -> Unit,

    onEditarMedicamento: (Medicamento) -> Unit,

    onEliminarMedicamento: (Medicamento) -> Unit
) {

    val verdePrincipal =
        Color(0xFF0F766E)

    val verdeBoton =
        Color(0xFF86A327)

    val fondo =
        Color(0xFFF4F8CE)

    val textoOscuro =
        Color(0xFF0F172A)


    // Foto mostrada en grande.
    var fotoAmpliada by remember {
        mutableStateOf<String?>(null)
    }


    // Medicamento pendiente de eliminar.
    var medicamentoAEliminar by remember {
        mutableStateOf<Medicamento?>(null)
    }


    Scaffold(

        topBar = {

            TopAppBar(

                title = {

                    Text(
                        text = "Medicamentos",
                        color = textoOscuro,
                        fontWeight = FontWeight.Bold
                    )
                },

                navigationIcon = {

                    IconButton(
                        onClick = onBack
                    ) {

                        Icon(
                            imageVector =
                                Icons.Default.ArrowBack,

                            contentDescription =
                                "Regresar",

                            tint =
                                verdePrincipal
                        )
                    }
                },

                colors =
                    TopAppBarDefaults
                        .topAppBarColors(
                            containerColor =
                                fondo
                        )
            )
        },

        containerColor =
            fondo

    ) { paddingValues ->


        Column(

            modifier = Modifier
                .fillMaxSize()
                .padding(
                    paddingValues
                )
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(
                    horizontal = 20.dp
                ),

            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {


            Spacer(
                modifier =
                    Modifier.height(18.dp)
            )


            // =================================================================
            // ICONO
            // =================================================================

            Box(

                modifier = Modifier
                    .size(74.dp)
                    .background(
                        color =
                            Color.White.copy(
                                alpha = 0.75f
                            ),

                        shape =
                            RoundedCornerShape(
                                22.dp
                            )
                    ),

                contentAlignment =
                    Alignment.Center
            ) {


                Icon(
                    imageVector =
                        Icons.Default.Medication,

                    contentDescription =
                        null,

                    tint =
                        verdePrincipal,

                    modifier =
                        Modifier.size(44.dp)
                )
            }


            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )


            Text(
                text =
                    "Mis medicamentos",

                fontSize =
                    25.sp,

                fontWeight =
                    FontWeight.Bold,

                color =
                    textoOscuro
            )


            Spacer(
                modifier =
                    Modifier.height(6.dp)
            )


            Text(
                text = "Organiza tus medicamentos y recuerda tomarlos a tiempo.",

                fontSize = 14.sp,

                color =
                    Color(0xFF64748B),

                textAlign =
                    TextAlign.Center
            )


            Spacer(
                modifier =
                    Modifier.height(26.dp)
            )


            // =================================================================
            // LISTA VACÍA
            // =================================================================

            if (
                medicamentos.isEmpty()
            ) {


                Card(

                    modifier =
                        Modifier.fillMaxWidth(),

                    shape =
                        RoundedCornerShape(
                            24.dp
                        ),

                    colors =
                        CardDefaults.cardColors(
                            containerColor =
                                Color(0xFFFEFFF6)
                        ),

                    elevation =
                        CardDefaults.cardElevation(
                            defaultElevation =
                                5.dp
                        )
                ) {


                    Column(

                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(26.dp),

                        horizontalAlignment =
                            Alignment.CenterHorizontally
                    ) {


                        Icon(
                            imageVector =
                                Icons.Default.Medication,

                            contentDescription =
                                null,

                            tint =
                                Color(0xFF94A3B8),

                            modifier =
                                Modifier.size(46.dp)
                        )


                        Spacer(
                            modifier =
                                Modifier.height(12.dp)
                        )


                        Text(
                            text = "Aún no tienes medicamentos registrados",

                            color =
                                textoOscuro,

                            fontWeight =
                                FontWeight.Bold,

                            textAlign =
                                TextAlign.Center
                        )


                        Spacer(
                            modifier =
                                Modifier.height(6.dp)
                        )


                        Text(
                            text = "Agrega tu primer medicamento para comenzar tu seguimiento.",

                            color =
                                Color(0xFF64748B),

                            fontSize =
                                13.sp,

                            textAlign =
                                TextAlign.Center
                        )
                    }
                }

            } else {


                // =============================================================
                // MEDICAMENTOS
                // =============================================================

                medicamentos.forEach {
                        medicamento ->


                    MedicamentoCard(

                        medicamento =
                            medicamento,


                        onFotoClick = {

                            medicamento
                                .fotoUri
                                ?.let { uri ->

                                    fotoAmpliada =
                                        uri
                                }
                        },


                        onEditarClick = {

                            onEditarMedicamento(
                                medicamento
                            )
                        },


                        onEliminarClick = {

                            medicamentoAEliminar =
                                medicamento
                        }
                    )


                    Spacer(
                        modifier =
                            Modifier.height(16.dp)
                    )
                }
            }


            Spacer(
                modifier =
                    Modifier.height(10.dp)
            )


            // =================================================================
            // AGREGAR
            // =================================================================

            Button(

                onClick =
                    onAgregarMedicamentoClick,

                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),

                shape =
                    RoundedCornerShape(18.dp),

                colors =
                    ButtonDefaults.buttonColors(
                        containerColor =
                            verdeBoton,

                        contentColor =
                            Color.White
                    )
            ) {


                Icon(
                    imageVector =
                        Icons.Default.Add,

                    contentDescription =
                        null
                )


                Spacer(
                    modifier =
                        Modifier.width(8.dp)
                )


                Text(
                    text =
                        "Agregar medicamento",

                    fontSize =
                        16.sp,

                    fontWeight =
                        FontWeight.Bold
                )
            }


            Spacer(
                modifier =
                    Modifier.height(30.dp)
            )
        }
    }


    // ========================================================================
    // FOTO AMPLIADA
    // ========================================================================

    if (
        fotoAmpliada != null
    ) {


        Dialog(

            onDismissRequest = {

                fotoAmpliada =
                    null
            }

        ) {


            Card(

                modifier =
                    Modifier.fillMaxWidth(),

                shape =
                    RoundedCornerShape(28.dp),

                colors =
                    CardDefaults.cardColors(
                        containerColor =
                            Color.White
                    )
            ) {


                Column(

                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),

                    horizontalAlignment =
                        Alignment.CenterHorizontally
                ) {


                    Row(

                        modifier =
                            Modifier.fillMaxWidth(),

                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {


                        Text(
                            text =
                                "Foto del medicamento",

                            modifier =
                                Modifier.weight(1f),

                            fontWeight =
                                FontWeight.Bold,

                            fontSize =
                                17.sp
                        )


                        IconButton(

                            onClick = {

                                fotoAmpliada =
                                    null
                            }

                        ) {


                            Icon(
                                imageVector =
                                    Icons.Default.Close,

                                contentDescription =
                                    "Cerrar"
                            )
                        }
                    }


                    Image(

                        painter =
                            rememberAsyncImagePainter(
                                model =
                                    fotoAmpliada
                            ),

                        contentDescription =
                            "Foto ampliada",

                        modifier = Modifier
                            .fillMaxWidth()
                            .height(360.dp)
                            .clip(
                                RoundedCornerShape(
                                    22.dp
                                )
                            ),

                        contentScale =
                            ContentScale.Fit
                    )


                    Spacer(
                        modifier =
                            Modifier.height(14.dp)
                    )


                    Text(
                        text = "Utiliza esta fotografía como apoyo visual para identificar tu medicamento.",

                        fontSize =
                            13.sp,

                        color =
                            Color(0xFF64748B),

                        textAlign =
                            TextAlign.Center
                    )
                }
            }
        }
    }


    // ========================================================================
    // CONFIRMAR ELIMINACIÓN
    // ========================================================================

    medicamentoAEliminar?.let {
            medicamento ->


        AlertDialog(

            onDismissRequest = {

                medicamentoAEliminar =
                    null
            },


            title = {

                Text(
                    text =
                        "Eliminar medicamento",

                    fontWeight =
                        FontWeight.Bold
                )
            },


            text = {

                Text(
                    text =
                        "¿Deseas eliminar \"${medicamento.nombre}\" de tu agenda?"
                )
            },


            confirmButton = {

                TextButton(

                    onClick = {

                        onEliminarMedicamento(
                            medicamento
                        )

                        medicamentoAEliminar =
                            null
                    }

                ) {


                    Text(
                        text =
                            "Eliminar",

                        color =
                            Color(0xFFB91C1C),

                        fontWeight =
                            FontWeight.Bold
                    )
                }
            },


            dismissButton = {

                TextButton(

                    onClick = {

                        medicamentoAEliminar =
                            null
                    }

                ) {


                    Text(
                        text =
                            "Cancelar"
                    )
                }
            }
        )
    }
}


// ============================================================================
// TARJETA DEL MEDICAMENTO
// ============================================================================

@Composable
fun MedicamentoCard(

    medicamento: Medicamento,

    onFotoClick: () -> Unit,

    onEditarClick: () -> Unit,

    onEliminarClick: () -> Unit
) {


    Card(

        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(24.dp),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    Color(0xFFFEFFF6)
            ),

        elevation =
            CardDefaults.cardElevation(
                defaultElevation =
                    6.dp
            )
    ) {


        Column(

            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {


            // =================================================================
            // FOTO + DATOS
            // =================================================================

            Row(

                modifier =
                    Modifier.fillMaxWidth(),

                verticalAlignment =
                    Alignment.CenterVertically
            ) {


                Box(

                    modifier = Modifier
                        .size(74.dp)
                        .clip(
                            RoundedCornerShape(
                                18.dp
                            )
                        )
                        .background(
                            Color(0xFFE8F7EE)
                        )
                        .clickable {

                            onFotoClick()
                        },

                    contentAlignment =
                        Alignment.Center
                ) {


                    if (
                        medicamento.fotoUri != null
                    ) {


                        Image(

                            painter =
                                rememberAsyncImagePainter(
                                    model =
                                        medicamento.fotoUri
                                ),

                            contentDescription =
                                "Foto de ${medicamento.nombre}",

                            modifier = Modifier
                                .fillMaxSize()
                                .clip(
                                    RoundedCornerShape(
                                        18.dp
                                    )
                                ),

                            contentScale =
                                ContentScale.Crop
                        )

                    } else {


                        Icon(
                            imageVector =
                                Icons.Default.Medication,

                            contentDescription =
                                null,

                            tint =
                                Color(0xFF0F766E),

                            modifier =
                                Modifier.size(34.dp)
                        )
                    }
                }


                Spacer(
                    modifier =
                        Modifier.width(14.dp)
                )


                Column(

                    modifier =
                        Modifier.weight(1f)
                ) {


                    Text(
                        text =
                            medicamento.nombre,

                        fontSize =
                            17.sp,

                        fontWeight =
                            FontWeight.Bold,

                        color =
                            Color(0xFF0F172A)
                    )


                    Spacer(
                        modifier =
                            Modifier.height(3.dp)
                    )


                    Text(
                        text =
                            medicamento.dosis,

                        fontSize =
                            14.sp,

                        color =
                            Color(0xFF64748B)
                    )


                    if (
                        medicamento.presentacion.isNotBlank()
                    ) {


                        Spacer(
                            modifier =
                                Modifier.height(3.dp)
                        )


                        Text(
                            text =
                                medicamento.presentacion,

                            fontSize =
                                12.sp,

                            color =
                                Color(0xFF94A3B8)
                        )
                    }
                }
            }


            Spacer(
                modifier =
                    Modifier.height(16.dp)
            )


            // =================================================================
            // HORARIOS
            // =================================================================

            Row(

                modifier =
                    Modifier.fillMaxWidth(),

                verticalAlignment =
                    Alignment.CenterVertically
            ) {


                Icon(
                    imageVector =
                        Icons.Default.NotificationsActive,

                    contentDescription =
                        null,

                    tint =
                        Color(0xFF86A327),

                    modifier =
                        Modifier.size(20.dp)
                )


                Spacer(
                    modifier =
                        Modifier.width(8.dp)
                )


                Text(
                    text =
                        "Horarios: ${medicamento.horarios.joinToString(" • ")}",

                    fontSize =
                        14.sp,

                    fontWeight =
                        FontWeight.SemiBold,

                    color =
                        Color(0xFF334155),

                    modifier =
                        Modifier.weight(1f)
                )
            }


            // =================================================================
            // INDICACIONES
            // =================================================================

            if (
                medicamento.indicaciones.isNotBlank()
            ) {


                Spacer(
                    modifier =
                        Modifier.height(10.dp)
                )


                Text(
                    text =
                        "Indicaciones: ${medicamento.indicaciones}",

                    fontSize =
                        13.sp,

                    color =
                        Color(0xFF64748B)
                )
            }


            // =================================================================
            // FECHAS
            // =================================================================

            if (
                medicamento.fechaInicio.isNotBlank()
            ) {


                Spacer(
                    modifier =
                        Modifier.height(10.dp)
                )


                Text(

                    text =
                        if (
                            medicamento.fechaFin.isNotBlank()
                        ) {

                            "Tratamiento: ${medicamento.fechaInicio} - ${medicamento.fechaFin}"

                        } else {

                            "Inicio: ${medicamento.fechaInicio}"
                        },

                    fontSize =
                        12.sp,

                    color =
                        Color(0xFF64748B)
                )
            }


            Spacer(
                modifier =
                    Modifier.height(10.dp)
            )


            // =================================================================
            // RECORDATORIO
            // =================================================================

            Text(

                text =
                    if (
                        medicamento.recordatorioActivo
                    ) {

                        "Recordatorios activados"

                    } else {

                        "Recordatorios desactivados"
                    },

                fontSize =
                    12.sp,

                fontWeight =
                    FontWeight.SemiBold,

                color =
                    if (
                        medicamento.recordatorioActivo
                    ) {

                        Color(0xFF0F766E)

                    } else {

                        Color(0xFF94A3B8)
                    }
            )


            Spacer(
                modifier =
                    Modifier.height(16.dp)
            )


            // =================================================================
            // EDITAR / ELIMINAR
            // =================================================================

            Row(

                modifier =
                    Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.spacedBy(10.dp)
            ) {


                Button(

                    onClick =
                        onEditarClick,

                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp),

                    shape =
                        RoundedCornerShape(16.dp),

                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor =
                                Color(0xFF0F766E)
                        )
                ) {


                    Icon(
                        imageVector =
                            Icons.Default.Edit,

                        contentDescription =
                            null,

                        modifier =
                            Modifier.size(18.dp)
                    )


                    Spacer(
                        modifier =
                            Modifier.width(6.dp)
                    )


                    Text(
                        text =
                            "Editar"
                    )
                }


                OutlinedButton(

                    onClick =
                        onEliminarClick,

                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp),

                    shape =
                        RoundedCornerShape(16.dp)
                ) {


                    Icon(
                        imageVector =
                            Icons.Default.Delete,

                        contentDescription =
                            null,

                        tint =
                            Color(0xFFB91C1C),

                        modifier =
                            Modifier.size(18.dp)
                    )


                    Spacer(
                        modifier =
                            Modifier.width(6.dp)
                    )


                    Text(
                        text =
                            "Eliminar",

                        color =
                            Color(0xFFB91C1C)
                    )
                }
            }
        }
    }
}