package com.example.vibralavida.agenda.medicamentos

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.net.Uri

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts

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
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.Schedule

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import coil.compose.rememberAsyncImagePainter

import java.util.Calendar


// ============================================================================
// AGREGAR / EDITAR MEDICAMENTO
// ============================================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgregarMedicamentoScreen(

    // Si es null = estamos agregando.
    // Si contiene un medicamento = estamos editando.
    medicamentoInicial: Medicamento? = null,

    // Regresar.
    onBack: () -> Unit,

    // Guardar datos.
    onGuardar: (
        nombre: String,
        dosis: String,
        presentacion: String,
        horarios: List<String>,
        fechaInicio: String,
        fechaFin: String,
        indicaciones: String,
        recordatorioActivo: Boolean,
        fotoUri: String?
    ) -> Unit
) {

    // ========================================================================
    // COLORES
    // ========================================================================

    val fondo = Color(0xFFF4F8CE)
    val verdePrincipal = Color(0xFF0F766E)
    val verdeBoton = Color(0xFF86A327)
    val textoOscuro = Color(0xFF0F172A)

    val context = LocalContext.current


    // ========================================================================
    // DATOS
    // ========================================================================

    var nombre by remember(medicamentoInicial) {
        mutableStateOf(
            medicamentoInicial?.nombre ?: ""
        )
    }


    var dosis by remember(medicamentoInicial) {
        mutableStateOf(
            medicamentoInicial?.dosis ?: ""
        )
    }


    var presentacion by remember(medicamentoInicial) {
        mutableStateOf(
            medicamentoInicial?.presentacion ?: ""
        )
    }


    var horarioSeleccionado by remember {
        mutableStateOf("")
    }


    var fechaInicio by remember(medicamentoInicial) {
        mutableStateOf(
            medicamentoInicial?.fechaInicio ?: ""
        )
    }


    var fechaFin by remember(medicamentoInicial) {
        mutableStateOf(
            medicamentoInicial?.fechaFin ?: ""
        )
    }


    var indicaciones by remember(medicamentoInicial) {
        mutableStateOf(
            medicamentoInicial?.indicaciones ?: ""
        )
    }


    var recordatorioActivo by remember(medicamentoInicial) {
        mutableStateOf(
            medicamentoInicial?.recordatorioActivo ?: true
        )
    }


    var mensajeError by remember {
        mutableStateOf("")
    }


    // ========================================================================
    // HORARIOS
    // ========================================================================

    val horarios = remember(medicamentoInicial) {

        mutableStateListOf<String>().apply {

            addAll(
                medicamentoInicial?.horarios
                    ?: emptyList()
            )
        }
    }


    // ========================================================================
    // FOTO
    // ========================================================================

    var fotoUri by remember(medicamentoInicial) {

        mutableStateOf(

            medicamentoInicial
                ?.fotoUri
                ?.let {
                    Uri.parse(it)
                }
        )
    }


    // ========================================================================
    // SELECTOR DE FOTO
    // ========================================================================

    val selectorImagen = rememberLauncherForActivityResult(

        contract = ActivityResultContracts.GetContent()

    ) { uri ->

        if (uri != null) {

            fotoUri = uri
        }
    }


    // ========================================================================
    // SELECTOR DE HORA
    // ========================================================================

    fun abrirSelectorHora() {

        val calendario = Calendar.getInstance()

        val horaActual =
            calendario.get(Calendar.HOUR_OF_DAY)

        val minutoActual =
            calendario.get(Calendar.MINUTE)


        TimePickerDialog(

            context,

            { _, hora, minuto ->

                horarioSeleccionado = String.format(
                    "%02d:%02d",
                    hora,
                    minuto
                )

                mensajeError = ""
            },

            horaActual,

            minutoActual,

            true

        ).show()
    }


    // ========================================================================
    // SELECTOR DE FECHA
    // ========================================================================

    fun abrirSelectorFecha(
        alSeleccionar: (String) -> Unit
    ) {

        val calendario =
            Calendar.getInstance()


        val año =
            calendario.get(Calendar.YEAR)

        val mes =
            calendario.get(Calendar.MONTH)

        val dia =
            calendario.get(Calendar.DAY_OF_MONTH)


        DatePickerDialog(

            context,

            { _, añoSeleccionado, mesSeleccionado, diaSeleccionado ->

                val fecha = String.format(
                    "%02d/%02d/%04d",
                    diaSeleccionado,
                    mesSeleccionado + 1,
                    añoSeleccionado
                )


                alSeleccionar(fecha)

                mensajeError = ""
            },

            año,
            mes,
            dia

        ).show()
    }


    // ========================================================================
    // PANTALLA
    // ========================================================================

    Scaffold(

        topBar = {

            TopAppBar(

                title = {

                    Text(

                        text =
                            if (medicamentoInicial == null) {

                                "Agregar medicamento"

                            } else {

                                "Editar medicamento"
                            },

                        color = textoOscuro,

                        fontWeight = FontWeight.Bold
                    )
                },


                navigationIcon = {

                    IconButton(
                        onClick = onBack
                    ) {

                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Regresar",
                            tint = verdePrincipal
                        )
                    }
                },


                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = fondo
                    )
            )
        },


        containerColor = fondo

    ) { paddingValues ->


        Column(

            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
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
                modifier = Modifier.height(20.dp)
            )


            // =================================================================
            // FOTO
            // =================================================================

            Card(

                modifier = Modifier
                    .size(150.dp)
                    .clickable {

                        if (fotoUri == null) {

                            selectorImagen.launch(
                                "image/*"
                            )
                        }
                    },

                shape =
                    RoundedCornerShape(28.dp),

                colors =
                    CardDefaults.cardColors(
                        containerColor = Color.White
                    ),

                elevation =
                    CardDefaults.cardElevation(
                        defaultElevation = 6.dp
                    )
            ) {


                Box(

                    modifier =
                        Modifier.fillMaxSize(),

                    contentAlignment =
                        Alignment.Center
                ) {


                    if (fotoUri != null) {

                        Image(

                            painter =
                                rememberAsyncImagePainter(
                                    model = fotoUri
                                ),

                            contentDescription =
                                "Foto del medicamento",

                            modifier = Modifier
                                .fillMaxSize()
                                .clip(
                                    RoundedCornerShape(
                                        28.dp
                                    )
                                ),

                            contentScale =
                                ContentScale.Crop
                        )

                    } else {

                        Column(

                            horizontalAlignment =
                                Alignment.CenterHorizontally,

                            verticalArrangement =
                                Arrangement.Center
                        ) {


                            Icon(
                                imageVector =
                                    Icons.Default.AddAPhoto,

                                contentDescription =
                                    "Agregar foto",

                                tint =
                                    verdePrincipal,

                                modifier =
                                    Modifier.size(44.dp)
                            )


                            Spacer(
                                modifier =
                                    Modifier.height(8.dp)
                            )


                            Text(
                                text = "Agregar foto",
                                color = verdePrincipal,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }


            // =================================================================
            // CAMBIAR / ELIMINAR FOTO
            // =================================================================

            if (fotoUri != null) {

                Spacer(
                    modifier =
                        Modifier.height(8.dp)
                )


                Row(

                    horizontalArrangement =
                        Arrangement.spacedBy(8.dp)
                ) {


                    TextButton(

                        onClick = {

                            selectorImagen.launch(
                                "image/*"
                            )
                        }

                    ) {


                        Icon(
                            imageVector =
                                Icons.Default.AddAPhoto,

                            contentDescription = null
                        )


                        Spacer(
                            modifier =
                                Modifier.width(5.dp)
                        )


                        Text(
                            text = "Cambiar foto"
                        )
                    }


                    TextButton(

                        onClick = {

                            fotoUri = null
                        }

                    ) {


                        Icon(
                            imageVector =
                                Icons.Default.Delete,

                            contentDescription = null
                        )


                        Spacer(
                            modifier =
                                Modifier.width(5.dp)
                        )


                        Text(
                            text = "Eliminar"
                        )
                    }
                }
            }


            Spacer(
                modifier =
                    Modifier.height(22.dp)
            )


            // =================================================================
            // INFORMACIÓN
            // =================================================================

            Text(

                text =
                    if (medicamentoInicial == null) {

                        "Información del medicamento"

                    } else {

                        "Editar información"
                    },

                fontSize = 21.sp,

                fontWeight = FontWeight.Bold,

                color = textoOscuro
            )


            Spacer(
                modifier =
                    Modifier.height(18.dp)
            )


            // =================================================================
            // NOMBRE
            // =================================================================

            OutlinedTextField(

                value = nombre,

                onValueChange = {

                    nombre = it

                    mensajeError = ""
                },

                label = {

                    Text(
                        "Nombre del medicamento"
                    )
                },

                placeholder = {

                    Text(
                        "Ej. Metformina"
                    )
                },

                modifier =
                    Modifier.fillMaxWidth(),

                singleLine = true
            )


            Spacer(
                modifier =
                    Modifier.height(14.dp)
            )


            // =================================================================
            // DOSIS
            // =================================================================

            OutlinedTextField(

                value = dosis,

                onValueChange = {

                    dosis = it

                    mensajeError = ""
                },

                label = {

                    Text(
                        "Dosis"
                    )
                },

                placeholder = {

                    Text(
                        "Ej. 500 mg"
                    )
                },

                modifier =
                    Modifier.fillMaxWidth(),

                singleLine = true
            )


            Spacer(
                modifier =
                    Modifier.height(14.dp)
            )


            // =================================================================
            // PRESENTACIÓN
            // =================================================================

            OutlinedTextField(

                value = presentacion,

                onValueChange = {

                    presentacion = it
                },

                label = {

                    Text(
                        "Presentación"
                    )
                },

                placeholder = {

                    Text(
                        "Ej. Tableta, cápsula, jarabe"
                    )
                },

                modifier =
                    Modifier.fillMaxWidth(),

                singleLine = true
            )


            Spacer(
                modifier =
                    Modifier.height(24.dp)
            )


            // =================================================================
            // HORARIOS
            // =================================================================

            Text(

                text = "Horarios de toma",

                modifier =
                    Modifier.fillMaxWidth(),

                fontSize = 17.sp,

                fontWeight =
                    FontWeight.Bold,

                color =
                    textoOscuro
            )


            Spacer(
                modifier =
                    Modifier.height(10.dp)
            )


            Row(

                modifier =
                    Modifier.fillMaxWidth(),

                verticalAlignment =
                    Alignment.CenterVertically
            ) {


                // -------------------------------------------------------------
                // SELECTOR HORA
                // -------------------------------------------------------------

                Card(

                    modifier = Modifier
                        .weight(1f)
                        .height(58.dp)
                        .clickable {

                            abrirSelectorHora()
                        },

                    shape =
                        RoundedCornerShape(16.dp),

                    colors =
                        CardDefaults.cardColors(
                            containerColor =
                                Color.White.copy(
                                    alpha = 0.25f
                                )
                        )
                ) {


                    Row(

                        modifier = Modifier
                            .fillMaxSize()
                            .padding(
                                horizontal = 14.dp
                            ),

                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {


                        Icon(
                            imageVector =
                                Icons.Default.Schedule,

                            contentDescription = null,

                            tint =
                                verdePrincipal
                        )


                        Spacer(
                            modifier =
                                Modifier.width(10.dp)
                        )


                        Text(

                            text =
                                if (
                                    horarioSeleccionado.isBlank()
                                ) {

                                    "Seleccionar hora"

                                } else {

                                    horarioSeleccionado
                                },

                            color =
                                if (
                                    horarioSeleccionado.isBlank()
                                ) {

                                    Color(0xFF64748B)

                                } else {

                                    textoOscuro
                                },

                            fontWeight =
                                FontWeight.Medium
                        )
                    }
                }


                Spacer(
                    modifier =
                        Modifier.width(10.dp)
                )


                // -------------------------------------------------------------
                // AGREGAR HORARIO
                // -------------------------------------------------------------

                Button(

                    onClick = {

                        if (
                            horarioSeleccionado.isNotBlank() &&
                            horarioSeleccionado !in horarios
                        ) {

                            horarios.add(
                                horarioSeleccionado
                            )

                            horarioSeleccionado = ""

                            mensajeError = ""
                        }
                    },

                    modifier =
                        Modifier.size(58.dp),

                    shape =
                        RoundedCornerShape(16.dp),

                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor =
                                verdePrincipal
                        )
                ) {


                    Icon(
                        imageVector =
                            Icons.Default.Add,

                        contentDescription =
                            "Agregar horario"
                    )
                }
            }


            // =================================================================
            // HORARIOS EXISTENTES
            // =================================================================

            if (horarios.isNotEmpty()) {

                Spacer(
                    modifier =
                        Modifier.height(12.dp)
                )


                Column(

                    modifier =
                        Modifier.fillMaxWidth(),

                    verticalArrangement =
                        Arrangement.spacedBy(8.dp)
                ) {


                    horarios.forEachIndexed {
                            index,
                            horario ->


                        Card(

                            modifier =
                                Modifier.fillMaxWidth(),

                            shape =
                                RoundedCornerShape(
                                    16.dp
                                ),

                            colors =
                                CardDefaults.cardColors(
                                    containerColor =
                                        Color(0xFFE8F7EE)
                                )
                        ) {


                            Row(

                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        horizontal = 14.dp,
                                        vertical = 8.dp
                                    ),

                                verticalAlignment =
                                    Alignment.CenterVertically
                            ) {


                                Icon(
                                    imageVector =
                                        Icons.Default.Schedule,

                                    contentDescription = null,

                                    tint =
                                        verdePrincipal
                                )


                                Spacer(
                                    modifier =
                                        Modifier.width(10.dp)
                                )


                                Text(
                                    text = horario,

                                    modifier =
                                        Modifier.weight(1f),

                                    fontWeight =
                                        FontWeight.SemiBold,

                                    color =
                                        textoOscuro
                                )


                                IconButton(

                                    onClick = {

                                        horarios.removeAt(
                                            index
                                        )
                                    }

                                ) {


                                    Icon(
                                        imageVector =
                                            Icons.Default.Delete,

                                        contentDescription =
                                            "Eliminar horario",

                                        tint =
                                            Color(0xFFB45309)
                                    )
                                }
                            }
                        }
                    }
                }
            }


            Spacer(
                modifier =
                    Modifier.height(24.dp)
            )


            // =================================================================
            // FECHA INICIO
            // =================================================================

            Card(

                modifier = Modifier
                    .fillMaxWidth()
                    .height(62.dp)
                    .clickable {

                        abrirSelectorFecha { fecha ->

                            fechaInicio = fecha
                        }
                    },

                shape =
                    RoundedCornerShape(16.dp),

                colors =
                    CardDefaults.cardColors(
                        containerColor =
                            Color.White.copy(
                                alpha = 0.25f
                            )
                    )
            ) {


                Row(

                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            horizontal = 14.dp
                        ),

                    verticalAlignment =
                        Alignment.CenterVertically
                ) {


                    Icon(
                        imageVector =
                            Icons.Default.CalendarMonth,

                        contentDescription = null,

                        tint =
                            verdePrincipal
                    )


                    Spacer(
                        modifier =
                            Modifier.width(10.dp)
                    )


                    Column {


                        Text(
                            text = "Fecha de inicio",
                            fontSize = 12.sp,
                            color = Color(0xFF64748B)
                        )


                        Text(

                            text =
                                if (
                                    fechaInicio.isBlank()
                                ) {

                                    "Seleccionar fecha"

                                } else {

                                    fechaInicio
                                },

                            fontSize = 15.sp,

                            fontWeight =
                                FontWeight.Medium,

                            color =
                                textoOscuro
                        )
                    }
                }
            }


            Spacer(
                modifier =
                    Modifier.height(14.dp)
            )


            // =================================================================
            // FECHA FINAL
            // =================================================================

            Card(

                modifier = Modifier
                    .fillMaxWidth()
                    .height(62.dp)
                    .clickable {

                        abrirSelectorFecha { fecha ->

                            fechaFin = fecha
                        }
                    },

                shape =
                    RoundedCornerShape(16.dp),

                colors =
                    CardDefaults.cardColors(
                        containerColor =
                            Color.White.copy(
                                alpha = 0.25f
                            )
                    )
            ) {


                Row(

                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            horizontal = 14.dp
                        ),

                    verticalAlignment =
                        Alignment.CenterVertically
                ) {


                    Icon(
                        imageVector =
                            Icons.Default.CalendarMonth,

                        contentDescription = null,

                        tint =
                            verdePrincipal
                    )


                    Spacer(
                        modifier =
                            Modifier.width(10.dp)
                    )


                    Column(

                        modifier =
                            Modifier.weight(1f)
                    ) {


                        Text(
                            text =
                                "Fecha de finalización",

                            fontSize = 12.sp,

                            color =
                                Color(0xFF64748B)
                        )


                        Text(

                            text =
                                if (
                                    fechaFin.isBlank()
                                ) {

                                    "Sin fecha"

                                } else {

                                    fechaFin
                                },

                            fontSize = 15.sp,

                            fontWeight =
                                FontWeight.Medium,

                            color =
                                textoOscuro
                        )
                    }


                    if (
                        fechaFin.isNotBlank()
                    ) {


                        IconButton(

                            onClick = {

                                fechaFin = ""
                            }

                        ) {


                            Icon(
                                imageVector =
                                    Icons.Default.Delete,

                                contentDescription =
                                    "Eliminar fecha final",

                                tint =
                                    Color(0xFFB45309)
                            )
                        }
                    }
                }
            }


            Spacer(
                modifier =
                    Modifier.height(14.dp)
            )


            // =================================================================
            // INDICACIONES
            // =================================================================

            OutlinedTextField(

                value = indicaciones,

                onValueChange = {

                    indicaciones = it
                },

                label = {

                    Text(
                        "Indicaciones"
                    )
                },

                placeholder = {

                    Text(
                        "Ej. Tomar después de comer"
                    )
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            )


            Spacer(
                modifier =
                    Modifier.height(18.dp)
            )


            // =================================================================
            // RECORDATORIO
            // =================================================================

            Card(

                modifier =
                    Modifier.fillMaxWidth(),

                shape =
                    RoundedCornerShape(20.dp),

                colors =
                    CardDefaults.cardColors(
                        containerColor =
                            Color(0xFFFEFFF6)
                    )
            ) {


                Row(

                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),

                    verticalAlignment =
                        Alignment.CenterVertically
                ) {


                    Column(

                        modifier =
                            Modifier.weight(1f)
                    ) {


                        Text(
                            text = "Recordatorio",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = textoOscuro
                        )


                        Spacer(
                            modifier =
                                Modifier.height(3.dp)
                        )


                        Text(
                            text = "Recibir una alerta a la hora de cada toma.",
                            fontSize = 13.sp,
                            color = Color(0xFF64748B)
                        )
                    }


                    Switch(

                        checked =
                            recordatorioActivo,

                        onCheckedChange = {

                            recordatorioActivo = it
                        }
                    )
                }
            }


            Spacer(
                modifier =
                    Modifier.height(28.dp)
            )


            // =================================================================
            // GUARDAR
            // =================================================================

            Button(

                onClick = {


                    // Si seleccionó una hora pero
                    // olvidó presionar el botón +,
                    // la agregamos automáticamente.
                    if (
                        horarioSeleccionado.isNotBlank() &&
                        horarioSeleccionado !in horarios
                    ) {

                        horarios.add(
                            horarioSeleccionado
                        )

                        horarioSeleccionado = ""
                    }


                    // ---------------------------------------------------------
                    // VALIDACIÓN
                    // ---------------------------------------------------------

                    when {


                        nombre.isBlank() -> {

                            mensajeError =
                                "Escribe el nombre del medicamento."
                        }


                        dosis.isBlank() -> {

                            mensajeError =
                                "Escribe la dosis del medicamento."
                        }


                        horarios.isEmpty() -> {

                            mensajeError =
                                "Selecciona al menos un horario."
                        }


                        fechaInicio.isBlank() -> {

                            mensajeError =
                                "Selecciona la fecha de inicio."
                        }


                        else -> {

                            mensajeError = ""


                            onGuardar(

                                nombre.trim(),

                                dosis.trim(),

                                presentacion.trim(),

                                horarios.toList(),

                                fechaInicio,

                                fechaFin,

                                indicaciones.trim(),

                                recordatorioActivo,

                                fotoUri?.toString()
                            )
                        }
                    }
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),

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
                        Icons.Default.Medication,

                    contentDescription = null
                )


                Spacer(
                    modifier =
                        Modifier.width(8.dp)
                )


                Text(

                    text =
                        if (
                            medicamentoInicial == null
                        ) {

                            "Guardar medicamento"

                        } else {

                            "Guardar cambios"
                        },

                    fontSize = 16.sp,

                    fontWeight =
                        FontWeight.Bold
                )
            }


            // =================================================================
            // MENSAJE DE ERROR
            // =================================================================

            if (
                mensajeError.isNotBlank()
            ) {


                Spacer(
                    modifier =
                        Modifier.height(10.dp)
                )


                Text(
                    text =
                        mensajeError,

                    color =
                        Color(0xFFB91C1C),

                    fontSize =
                        13.sp,

                    fontWeight =
                        FontWeight.SemiBold,

                    textAlign =
                        TextAlign.Center
                )
            }


            Spacer(
                modifier =
                    Modifier.height(40.dp)
            )
        }
    }
}