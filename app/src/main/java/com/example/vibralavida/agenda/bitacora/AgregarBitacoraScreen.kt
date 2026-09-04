package com.example.vibralavida.agenda.bitacora

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import java.util.Calendar


// ============================================================================
// AGREGAR / EDITAR BITÁCORA
// ============================================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgregarBitacoraScreen(

    registroInicial: RegistroSalud? = null,

    estudioInicial: EstudioLaboratorio? = null,

    onBack: () -> Unit,

    onGuardarMedicion: (
        tipo: String,
        fecha: String,
        hora: String,
        valorPrincipal: String,
        valorSecundario: String,
        unidad: String,
        condicion: String,
        observaciones: String
    ) -> Unit,

    onGuardarLaboratorio: (
        tipoEstudio: String,
        nombrePersonalizado: String,
        fecha: String,
        laboratorio: String,
        archivosUri: List<String>,
        observaciones: String
    ) -> Unit

) {

    val context =
        LocalContext.current


    val fondo =
        Color(0xFFF4F8CE)

    val verdePrincipal =
        Color(0xFF0F766E)

    val verdeBoton =
        Color(0xFF86A327)


    // ========================================================================
    // TIPO DE ENTRADA
    // ========================================================================

    var tipoEntrada by remember {

        mutableStateOf<TipoEntradaBitacora?>(

            when {

                registroInicial != null ->
                    TipoEntradaBitacora.MEDICION

                estudioInicial != null ->
                    TipoEntradaBitacora.LABORATORIO

                else ->
                    null
            }
        )
    }


    // ========================================================================
    // DATOS DE MEDICIÓN
    // ========================================================================

    var tipoMedicion by remember(
        registroInicial
    ) {

        mutableStateOf(
            registroInicial?.tipo
                ?: "Glucosa"
        )
    }


    var tipoPersonalizado by remember {

        mutableStateOf("")
    }


    var fechaMedicion by remember(
        registroInicial
    ) {

        mutableStateOf(
            registroInicial?.fecha
                ?: ""
        )
    }


    var horaMedicion by remember(
        registroInicial
    ) {

        mutableStateOf(
            registroInicial?.hora
                ?: ""
        )
    }


    var valorPrincipal by remember(
        registroInicial
    ) {

        mutableStateOf(
            registroInicial?.valorPrincipal
                ?: ""
        )
    }


    var valorSecundario by remember(
        registroInicial
    ) {

        mutableStateOf(
            registroInicial?.valorSecundario
                ?: ""
        )
    }


    var unidad by remember(
        registroInicial
    ) {

        mutableStateOf(
            registroInicial?.unidad
                ?: unidadPorTipo(
                    tipoMedicion
                )
        )
    }


    var condicion by remember(
        registroInicial
    ) {

        mutableStateOf(
            registroInicial?.condicion
                ?: ""
        )
    }


    var observacionesMedicion by remember(
        registroInicial
    ) {

        mutableStateOf(
            registroInicial?.observaciones
                ?: ""
        )
    }


    // ========================================================================
    // DATOS DE LABORATORIO
    // ========================================================================

    var tipoEstudio by remember(
        estudioInicial
    ) {

        mutableStateOf(
            estudioInicial?.tipoEstudio
                ?: "Química sanguínea"
        )
    }


    var nombrePersonalizadoEstudio by remember(
        estudioInicial
    ) {

        mutableStateOf(
            estudioInicial?.nombrePersonalizado
                ?: ""
        )
    }


    var fechaEstudio by remember(
        estudioInicial
    ) {

        mutableStateOf(
            estudioInicial?.fecha
                ?: ""
        )
    }


    var nombreLaboratorio by remember(
        estudioInicial
    ) {

        mutableStateOf(
            estudioInicial?.laboratorio
                ?: ""
        )
    }


    var observacionesEstudio by remember(
        estudioInicial
    ) {

        mutableStateOf(
            estudioInicial?.observaciones
                ?: ""
        )
    }


    val archivosAdjuntos =
        remember(
            estudioInicial
        ) {

            mutableStateListOf<String>()
                .apply {

                    addAll(
                        estudioInicial
                            ?.archivosUri
                            ?: emptyList()
                    )
                }
        }


    // ========================================================================
    // ERROR
    // ========================================================================

    var mensajeError by remember {

        mutableStateOf("")
    }


    // ========================================================================
    // SELECTOR DE ARCHIVOS
    // ========================================================================

    val selectorArchivos =
        rememberLauncherForActivityResult(

            contract =
                ActivityResultContracts.OpenMultipleDocuments()

        ) {
                uris ->


            uris.forEach {
                    uri ->


                try {

                    context.contentResolver
                        .takePersistableUriPermission(

                            uri,

                            Intent.FLAG_GRANT_READ_URI_PERMISSION
                        )

                } catch (
                    e: Exception
                ) {

                    e.printStackTrace()
                }


                val uriTexto =
                    uri.toString()


                if (
                    !archivosAdjuntos.contains(
                        uriTexto
                    )
                ) {

                    archivosAdjuntos.add(
                        uriTexto
                    )
                }
            }
        }


    // ========================================================================
    // FECHA
    // ========================================================================

    fun abrirFecha(
        onFechaSeleccionada: (String) -> Unit
    ) {

        val calendario =
            Calendar.getInstance()


        DatePickerDialog(

            context,

            {
                    _,
                    year,
                    month,
                    day ->


                val fecha =
                    String.format(

                        "%02d/%02d/%04d",

                        day,

                        month + 1,

                        year
                    )


                onFechaSeleccionada(
                    fecha
                )
            },

            calendario.get(
                Calendar.YEAR
            ),

            calendario.get(
                Calendar.MONTH
            ),

            calendario.get(
                Calendar.DAY_OF_MONTH
            )

        ).show()
    }


    // ========================================================================
    // HORA
    // ========================================================================

    fun abrirHora() {

        val calendario =
            Calendar.getInstance()


        TimePickerDialog(

            context,

            {
                    _,
                    hour,
                    minute ->


                horaMedicion =
                    String.format(

                        "%02d:%02d",

                        hour,

                        minute
                    )
            },

            calendario.get(
                Calendar.HOUR_OF_DAY
            ),

            calendario.get(
                Calendar.MINUTE
            ),

            true

        ).show()
    }


    // ========================================================================
    // PANTALLA
    // ========================================================================

    Scaffold(

        containerColor =
            fondo,

        topBar = {

            TopAppBar(

                title = {

                    Text(

                        text =
                            when {

                                registroInicial != null ->
                                    "Editar medición"

                                estudioInicial != null ->
                                    "Editar laboratorio"

                                else ->
                                    "Agregar registro"
                            },

                        fontWeight =
                            FontWeight.Bold
                    )
                },

                navigationIcon = {

                    IconButton(
                        onClick =
                            onBack
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
                    TopAppBarDefaults.topAppBarColors(

                        containerColor =
                            fondo
                    )
            )
        }

    ) {
            paddingValues ->


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
                    Modifier.height(
                        20.dp
                    )
            )


            // =================================================================
            // ELEGIR TIPO
            // =================================================================

            if (
                tipoEntrada == null
            ) {

                Text(

                    text =
                        "¿Qué deseas registrar?",

                    fontSize =
                        23.sp,

                    fontWeight =
                        FontWeight.Bold,

                    color =
                        Color(0xFF0F172A)
                )


                Spacer(
                    modifier =
                        Modifier.height(
                            8.dp
                        )
                )


                Text(

                    text =
                        "Puedes capturar una medición individual o guardar un estudio de laboratorio completo.",

                    color =
                        Color(0xFF64748B),

                    textAlign =
                        TextAlign.Center
                )


                Spacer(
                    modifier =
                        Modifier.height(
                            25.dp
                        )
                )


                Button(

                    onClick = {

                        tipoEntrada =
                            TipoEntradaBitacora.MEDICION
                    },

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(
                            70.dp
                        ),

                    shape =
                        RoundedCornerShape(
                            20.dp
                        ),

                    colors =
                        ButtonDefaults.buttonColors(

                            containerColor =
                                Color(0xFF0F766E)
                        )
                ) {

                    Column(

                        horizontalAlignment =
                            Alignment.CenterHorizontally
                    ) {

                        Text(

                            text =
                                "Medición individual",

                            fontWeight =
                                FontWeight.Bold
                        )


                        Text(

                            text =
                                "Glucosa, presión, peso, SpO₂...",

                            fontSize =
                                12.sp
                        )
                    }
                }


                Spacer(
                    modifier =
                        Modifier.height(
                            15.dp
                        )
                )


                Button(

                    onClick = {

                        tipoEntrada =
                            TipoEntradaBitacora.LABORATORIO
                    },

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(
                            70.dp
                        ),

                    shape =
                        RoundedCornerShape(
                            20.dp
                        ),

                    colors =
                        ButtonDefaults.buttonColors(

                            containerColor =
                                verdeBoton
                        )
                ) {

                    Column(

                        horizontalAlignment =
                            Alignment.CenterHorizontally
                    ) {

                        Text(

                            text =
                                "Estudio de laboratorio",

                            fontWeight =
                                FontWeight.Bold
                        )


                        Text(

                            text =
                                "PDF o fotografías del estudio completo",

                            fontSize =
                                12.sp
                        )
                    }
                }
            }


            // =================================================================
            // MEDICIÓN
            // =================================================================

            if (
                tipoEntrada ==
                TipoEntradaBitacora.MEDICION
            ) {

                FormularioMedicion(

                    tipoMedicion =
                        tipoMedicion,

                    onTipoMedicionChange = {
                            nuevoTipo ->


                        tipoMedicion =
                            nuevoTipo


                        unidad =
                            unidadPorTipo(
                                nuevoTipo
                            )
                    },

                    tipoPersonalizado =
                        tipoPersonalizado,

                    onTipoPersonalizadoChange = {

                        tipoPersonalizado =
                            it
                    },

                    fecha =
                        fechaMedicion,

                    onSeleccionarFecha = {

                        abrirFecha {

                            fechaMedicion =
                                it
                        }
                    },

                    hora =
                        horaMedicion,

                    onSeleccionarHora = {

                        abrirHora()
                    },

                    valorPrincipal =
                        valorPrincipal,

                    onValorPrincipalChange = {

                        valorPrincipal =
                            it
                    },

                    valorSecundario =
                        valorSecundario,

                    onValorSecundarioChange = {

                        valorSecundario =
                            it
                    },

                    unidad =
                        unidad,

                    onUnidadChange = {

                        unidad =
                            it
                    },

                    condicion =
                        condicion,

                    onCondicionChange = {

                        condicion =
                            it
                    },

                    observaciones =
                        observacionesMedicion,

                    onObservacionesChange = {

                        observacionesMedicion =
                            it
                    },

                    onGuardar = {


                        val tipoFinal =
                            if (
                                tipoMedicion == "Otro"
                            ) {

                                tipoPersonalizado.trim()

                            } else {

                                tipoMedicion
                            }


                        when {

                            tipoFinal.isBlank() -> {

                                mensajeError =
                                    "Especifica qué medición estás registrando."
                            }


                            fechaMedicion.isBlank() -> {

                                mensajeError =
                                    "Selecciona la fecha."
                            }


                            horaMedicion.isBlank() -> {

                                mensajeError =
                                    "Selecciona la hora."
                            }


                            valorPrincipal.isBlank() -> {

                                mensajeError =
                                    "Escribe el resultado."
                            }


                            tipoMedicion ==
                                    "Presión arterial" &&
                                    valorSecundario.isBlank() -> {

                                mensajeError =
                                    "Escribe la presión diastólica."
                            }


                            else -> {

                                mensajeError =
                                    ""


                                onGuardarMedicion(

                                    tipoFinal,

                                    fechaMedicion,

                                    horaMedicion,

                                    valorPrincipal.trim(),

                                    valorSecundario.trim(),

                                    unidad.trim(),

                                    condicion.trim(),

                                    observacionesMedicion.trim()
                                )
                            }
                        }
                    }
                )
            }


            // =================================================================
            // LABORATORIO
            // =================================================================

            if (
                tipoEntrada ==
                TipoEntradaBitacora.LABORATORIO
            ) {

                FormularioLaboratorio(

                    tipoEstudio =
                        tipoEstudio,

                    onTipoEstudioChange = {

                        tipoEstudio =
                            it
                    },

                    nombrePersonalizado =
                        nombrePersonalizadoEstudio,

                    onNombrePersonalizadoChange = {

                        nombrePersonalizadoEstudio =
                            it
                    },

                    fecha =
                        fechaEstudio,

                    onSeleccionarFecha = {

                        abrirFecha {

                            fechaEstudio =
                                it
                        }
                    },

                    laboratorio =
                        nombreLaboratorio,

                    onLaboratorioChange = {

                        nombreLaboratorio =
                            it
                    },

                    archivos =
                        archivosAdjuntos,

                    onAgregarArchivos = {

                        selectorArchivos.launch(

                            arrayOf(

                                "application/pdf",

                                "image/*"
                            )
                        )
                    },

                    onEliminarArchivo = {
                            archivo ->


                        archivosAdjuntos.remove(
                            archivo
                        )
                    },

                    observaciones =
                        observacionesEstudio,

                    onObservacionesChange = {

                        observacionesEstudio =
                            it
                    },

                    onGuardar = {


                        when {

                            tipoEstudio ==
                                    "Otro" &&
                                    nombrePersonalizadoEstudio
                                        .isBlank() -> {

                                mensajeError =
                                    "Escribe el nombre del estudio."
                            }


                            fechaEstudio.isBlank() -> {

                                mensajeError =
                                    "Selecciona la fecha del estudio."
                            }


                            archivosAdjuntos.isEmpty() -> {

                                mensajeError =
                                    "Adjunta al menos un PDF o fotografía."
                            }


                            else -> {

                                mensajeError =
                                    ""


                                onGuardarLaboratorio(

                                    tipoEstudio,

                                    nombrePersonalizadoEstudio.trim(),

                                    fechaEstudio,

                                    nombreLaboratorio.trim(),

                                    archivosAdjuntos.toList(),

                                    observacionesEstudio.trim()
                                )
                            }
                        }
                    }
                )
            }


            // =================================================================
            // ERROR
            // =================================================================

            if (
                mensajeError.isNotBlank()
            ) {

                Spacer(
                    modifier =
                        Modifier.height(
                            12.dp
                        )
                )


                Text(

                    text =
                        mensajeError,

                    color =
                        Color(0xFFB91C1C),

                    fontWeight =
                        FontWeight.SemiBold,

                    textAlign =
                        TextAlign.Center
                )
            }


            // =================================================================
            // CAMBIAR TIPO
            // =================================================================

            if (
                registroInicial == null &&
                estudioInicial == null &&
                tipoEntrada != null
            ) {

                Spacer(
                    modifier =
                        Modifier.height(
                            15.dp
                        )
                )


                TextButton(

                    onClick = {

                        tipoEntrada =
                            null


                        mensajeError =
                            ""
                    }
                ) {

                    Text(
                        "Elegir otro tipo de registro"
                    )
                }
            }


            Spacer(
                modifier =
                    Modifier.height(
                        40.dp
                    )
            )
        }
    }
}


// ============================================================================
// FORMULARIO DE MEDICIÓN
// ============================================================================

@Composable
private fun FormularioMedicion(

    tipoMedicion: String,

    onTipoMedicionChange: (String) -> Unit,

    tipoPersonalizado: String,

    onTipoPersonalizadoChange: (String) -> Unit,

    fecha: String,

    onSeleccionarFecha: () -> Unit,

    hora: String,

    onSeleccionarHora: () -> Unit,

    valorPrincipal: String,

    onValorPrincipalChange: (String) -> Unit,

    valorSecundario: String,

    onValorSecundarioChange: (String) -> Unit,

    unidad: String,

    onUnidadChange: (String) -> Unit,

    condicion: String,

    onCondicionChange: (String) -> Unit,

    observaciones: String,

    onObservacionesChange: (String) -> Unit,

    onGuardar: () -> Unit

) {

    val opciones =
        listOf(

            "Glucosa",

            "Presión arterial",

            "Peso",

            "Frecuencia cardiaca",

            "SpO₂",

            "Otro"
        )


    Text(

        text =
            "Medición individual",

        fontSize =
            23.sp,

        fontWeight =
            FontWeight.Bold,

        color =
            Color(0xFF0F172A)
    )


    Spacer(
        modifier =
            Modifier.height(
                18.dp
            )
    )


    SelectorOpciones(

        titulo =
            "Tipo de medición",

        seleccionado =
            tipoMedicion,

        opciones =
            opciones,

        onSeleccionado =
            onTipoMedicionChange
    )


    if (
        tipoMedicion == "Otro"
    ) {

        Spacer(
            modifier =
                Modifier.height(
                    12.dp
                )
        )


        OutlinedTextField(

            value =
                tipoPersonalizado,

            onValueChange =
                onTipoPersonalizadoChange,

            label = {

                Text(
                    "Nombre de la medición"
                )
            },

            modifier =
                Modifier.fillMaxWidth()
        )
    }


    Spacer(
        modifier =
            Modifier.height(
                12.dp
            )
    )


    BotonSeleccion(

        texto =
            if (
                fecha.isBlank()
            ) {

                "Seleccionar fecha"

            } else {

                "Fecha: $fecha"
            },

        onClick =
            onSeleccionarFecha
    )


    Spacer(
        modifier =
            Modifier.height(
                12.dp
            )
    )


    BotonSeleccion(

        texto =
            if (
                hora.isBlank()
            ) {

                "Seleccionar hora"

            } else {

                "Hora: $hora"
            },

        onClick =
            onSeleccionarHora
    )


    Spacer(
        modifier =
            Modifier.height(
                12.dp
            )
    )


    // ========================================================================
    // PRESIÓN ARTERIAL
    // ========================================================================

    if (
        tipoMedicion ==
        "Presión arterial"
    ) {

        OutlinedTextField(

            value =
                valorPrincipal,

            onValueChange =
                onValorPrincipalChange,

            label = {

                Text(
                    "Presión sistólica"
                )
            },

            placeholder = {

                Text(
                    "Ej. 120"
                )
            },

            modifier =
                Modifier.fillMaxWidth()
        )


        Spacer(
            modifier =
                Modifier.height(
                    12.dp
                )
        )


        OutlinedTextField(

            value =
                valorSecundario,

            onValueChange =
                onValorSecundarioChange,

            label = {

                Text(
                    "Presión diastólica"
                )
            },

            placeholder = {

                Text(
                    "Ej. 80"
                )
            },

            modifier =
                Modifier.fillMaxWidth()
        )

    } else {


        OutlinedTextField(

            value =
                valorPrincipal,

            onValueChange =
                onValorPrincipalChange,

            label = {

                Text(
                    "Resultado"
                )
            },

            modifier =
                Modifier.fillMaxWidth()
        )
    }


    Spacer(
        modifier =
            Modifier.height(
                12.dp
            )
    )


    OutlinedTextField(

        value =
            unidad,

        onValueChange =
            onUnidadChange,

        label = {

            Text(
                "Unidad"
            )
        },

        modifier =
            Modifier.fillMaxWidth()
    )


    Spacer(
        modifier =
            Modifier.height(
                12.dp
            )
    )


    OutlinedTextField(

        value =
            condicion,

        onValueChange =
            onCondicionChange,

        label = {

            Text(
                "Condición (opcional)"
            )
        },

        placeholder = {

            Text(

                when (
                    tipoMedicion
                ) {

                    "Glucosa" ->
                        "Ej. En ayuno"

                    "Presión arterial" ->
                        "Ej. En reposo"

                    else ->
                        "Ej. Antes de desayunar"
                }
            )
        },

        modifier =
            Modifier.fillMaxWidth()
    )


    Spacer(
        modifier =
            Modifier.height(
                12.dp
            )
    )


    OutlinedTextField(

        value =
            observaciones,

        onValueChange =
            onObservacionesChange,

        label = {

            Text(
                "Observaciones"
            )
        },

        modifier = Modifier
            .fillMaxWidth()
            .height(
                110.dp
            )
    )


    Spacer(
        modifier =
            Modifier.height(
                22.dp
            )
    )


    BotonGuardar(

        texto =
            "Guardar medición",

        onClick =
            onGuardar
    )
}


// ============================================================================
// FORMULARIO DE LABORATORIO
// ============================================================================

@Composable
private fun FormularioLaboratorio(

    tipoEstudio: String,

    onTipoEstudioChange: (String) -> Unit,

    nombrePersonalizado: String,

    onNombrePersonalizadoChange: (String) -> Unit,

    fecha: String,

    onSeleccionarFecha: () -> Unit,

    laboratorio: String,

    onLaboratorioChange: (String) -> Unit,

    archivos: List<String>,

    onAgregarArchivos: () -> Unit,

    onEliminarArchivo: (String) -> Unit,

    observaciones: String,

    onObservacionesChange: (String) -> Unit,

    onGuardar: () -> Unit

) {

    val tipos =
        listOf(

            "Química sanguínea",

            "Biometría hemática",

            "Perfil lipídico",

            "Perfil tiroideo",

            "Examen general de orina",

            "Perfil hepático",

            "Otro"
        )


    Text(

        text =
            "Estudio de laboratorio",

        fontSize =
            23.sp,

        fontWeight =
            FontWeight.Bold,

        color =
            Color(0xFF0F172A)
    )


    Spacer(
        modifier =
            Modifier.height(
                18.dp
            )
    )


    SelectorOpciones(

        titulo =
            "Tipo de estudio",

        seleccionado =
            tipoEstudio,

        opciones =
            tipos,

        onSeleccionado =
            onTipoEstudioChange
    )


    if (
        tipoEstudio == "Otro"
    ) {

        Spacer(
            modifier =
                Modifier.height(
                    12.dp
                )
        )


        OutlinedTextField(

            value =
                nombrePersonalizado,

            onValueChange =
                onNombrePersonalizadoChange,

            label = {

                Text(
                    "Nombre del estudio"
                )
            },

            modifier =
                Modifier.fillMaxWidth()
        )
    }


    Spacer(
        modifier =
            Modifier.height(
                12.dp
            )
    )


    BotonSeleccion(

        texto =
            if (
                fecha.isBlank()
            ) {

                "Seleccionar fecha"

            } else {

                "Fecha: $fecha"
            },

        onClick =
            onSeleccionarFecha
    )


    Spacer(
        modifier =
            Modifier.height(
                12.dp
            )
    )


    OutlinedTextField(

        value =
            laboratorio,

        onValueChange =
            onLaboratorioChange,

        label = {

            Text(
                "Nombre del laboratorio (opcional)"
            )
        },

        modifier =
            Modifier.fillMaxWidth()
    )


    Spacer(
        modifier =
            Modifier.height(
                15.dp
            )
    )


    OutlinedButton(

        onClick =
            onAgregarArchivos,

        modifier =
            Modifier.fillMaxWidth()
    ) {

        Text(
            "Adjuntar PDF o fotografías"
        )
    }


    // ========================================================================
    // ARCHIVOS
    // ========================================================================

    archivos.forEach {
            archivo ->


        Spacer(
            modifier =
                Modifier.height(
                    8.dp
                )
        )


        Row(

            modifier =
                Modifier.fillMaxWidth(),

            verticalAlignment =
                Alignment.CenterVertically
        ) {


            Text(

                text =
                    Uri.parse(
                        archivo
                    ).lastPathSegment
                        ?: "Archivo adjunto",

                modifier =
                    Modifier.weight(
                        1f
                    ),

                fontSize =
                    12.sp,

                color =
                    Color(0xFF475569)
            )


            TextButton(

                onClick = {

                    onEliminarArchivo(
                        archivo
                    )
                }
            ) {

                Text(

                    text =
                        "Quitar",

                    color =
                        Color(0xFFB91C1C)
                )
            }
        }
    }


    Spacer(
        modifier =
            Modifier.height(
                12.dp
            )
    )


    OutlinedTextField(

        value =
            observaciones,

        onValueChange =
            onObservacionesChange,

        label = {

            Text(
                "Observaciones"
            )
        },

        modifier = Modifier
            .fillMaxWidth()
            .height(
                110.dp
            )
    )


    Spacer(
        modifier =
            Modifier.height(
                22.dp
            )
    )


    BotonGuardar(

        texto =
            "Guardar estudio",

        onClick =
            onGuardar
    )
}


// ============================================================================
// SELECTOR
// ============================================================================

@Composable
private fun SelectorOpciones(

    titulo: String,

    seleccionado: String,

    opciones: List<String>,

    onSeleccionado: (String) -> Unit

) {

    var abierto by remember {

        mutableStateOf(
            false
        )
    }


    Column(

        modifier =
            Modifier.fillMaxWidth()
    ) {


        Text(

            text =
                titulo,

            fontSize =
                13.sp,

            color =
                Color(0xFF64748B)
        )


        Spacer(
            modifier =
                Modifier.height(
                    5.dp
                )
        )


        androidx.compose.foundation.layout.Box(

            modifier =
                Modifier.fillMaxWidth()
        ) {


            OutlinedButton(

                onClick = {

                    abierto =
                        true
                },

                modifier =
                    Modifier.fillMaxWidth()
            ) {


                Text(

                    text =
                        seleccionado,

                    modifier =
                        Modifier.weight(
                            1f
                        )
                )


                Text(
                    "▼"
                )
            }


            DropdownMenu(

                expanded =
                    abierto,

                onDismissRequest = {

                    abierto =
                        false
                }
            ) {


                opciones.forEach {
                        opcion ->


                    DropdownMenuItem(

                        text = {

                            Text(
                                opcion
                            )
                        },

                        onClick = {

                            onSeleccionado(
                                opcion
                            )


                            abierto =
                                false
                        }
                    )
                }
            }
        }
    }
}


// ============================================================================
// BOTÓN DE SELECCIÓN
// ============================================================================

@Composable
private fun BotonSeleccion(

    texto: String,

    onClick: () -> Unit

) {

    OutlinedButton(

        onClick =
            onClick,

        modifier = Modifier
            .fillMaxWidth()
            .height(
                54.dp
            )
    ) {

        Text(
            texto
        )
    }
}


// ============================================================================
// GUARDAR
// ============================================================================

@Composable
private fun BotonGuardar(

    texto: String,

    onClick: () -> Unit

) {

    Button(

        onClick =
            onClick,

        modifier = Modifier
            .fillMaxWidth()
            .height(
                56.dp
            ),

        shape =
            RoundedCornerShape(
                18.dp
            ),

        colors =
            ButtonDefaults.buttonColors(

                containerColor =
                    Color(0xFF86A327)
            )
    ) {

        Text(

            text =
                texto,

            fontWeight =
                FontWeight.Bold
        )
    }
}


// ============================================================================
// UNIDAD AUTOMÁTICA
// ============================================================================

private fun unidadPorTipo(
    tipo: String
): String {

    return when (
        tipo
    ) {

        "Glucosa" ->
            "mg/dL"

        "Presión arterial" ->
            "mmHg"

        "Peso" ->
            "kg"

        "Frecuencia cardiaca" ->
            "lpm"

        "SpO₂" ->
            "%"

        else ->
            ""
    }
}