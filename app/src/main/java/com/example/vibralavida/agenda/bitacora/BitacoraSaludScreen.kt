package com.example.vibralavida.agenda.bitacora

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


// ============================================================================
// ELEMENTO INTERNO PARA ORDENAR LA BITÁCORA
// ============================================================================

private data class EntradaBitacoraVisual(

    val registroSalud: RegistroSalud? = null,

    val estudioLaboratorio: EstudioLaboratorio? = null,

    val fechaMillis: Long = 0L
)


// ============================================================================
// BITÁCORA DE SALUD
// ============================================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BitacoraSaludScreen(

    registrosSalud: List<RegistroSalud>,

    estudiosLaboratorio: List<EstudioLaboratorio>,

    onBack: () -> Unit,

    onAgregarClick: () -> Unit,

    onEditarRegistro: (RegistroSalud) -> Unit,

    onEliminarRegistro: (RegistroSalud) -> Unit,

    onEditarEstudio: (EstudioLaboratorio) -> Unit,

    onEliminarEstudio: (EstudioLaboratorio) -> Unit

) {


    // ========================================================================
    // COLORES
    // ========================================================================

    val fondo =
        Color(0xFFF4F8CE)

    val verdePrincipal =
        Color(0xFF0F766E)

    val verdeBoton =
        Color(0xFF86A327)

    val textoOscuro =
        Color(0xFF0F172A)


    // ========================================================================
    // FILTRO
    // ========================================================================

    var filtroSeleccionado by remember {

        mutableStateOf(
            "Todos"
        )
    }


    var menuFiltroAbierto by remember {

        mutableStateOf(
            false
        )
    }


    // ========================================================================
    // REGISTROS A ELIMINAR
    // ========================================================================

    var registroAEliminar by remember {

        mutableStateOf<RegistroSalud?>(
            null
        )
    }


    var estudioAEliminar by remember {

        mutableStateOf<EstudioLaboratorio?>(
            null
        )
    }


    // ========================================================================
    // OPCIONES DEL FILTRO
    // ========================================================================

    val tiposRegistrados =
        registrosSalud
            .map {
                it.tipo
            }
            .filter {
                it.isNotBlank()
            }
            .distinct()


    val opcionesFiltro =
        listOf(

            "Todos",

            "Mediciones",

            "Laboratorios"

        ) + tiposRegistrados


    // ========================================================================
    // CREAR LISTA COMBINADA
    // ========================================================================

    val entradas =
        mutableListOf<EntradaBitacoraVisual>()


    // ------------------------------------------------------------------------
    // MEDICIONES
    // ------------------------------------------------------------------------

    registrosSalud.forEach {
            registro ->


        val mostrar =
            when (filtroSeleccionado) {

                "Todos" ->
                    true

                "Mediciones" ->
                    true

                "Laboratorios" ->
                    false

                else ->
                    registro.tipo ==
                            filtroSeleccionado
            }


        if (mostrar) {

            entradas.add(

                EntradaBitacoraVisual(

                    registroSalud =
                        registro,

                    fechaMillis =
                        obtenerFechaMillis(

                            fecha =
                                registro.fecha,

                            hora =
                                registro.hora
                        )
                )
            )
        }
    }


    // ------------------------------------------------------------------------
    // LABORATORIOS
    // ------------------------------------------------------------------------

    if (
        filtroSeleccionado == "Todos" ||
        filtroSeleccionado == "Laboratorios"
    ) {

        estudiosLaboratorio.forEach {
                estudio ->


            entradas.add(

                EntradaBitacoraVisual(

                    estudioLaboratorio =
                        estudio,

                    fechaMillis =
                        obtenerFechaMillis(

                            fecha =
                                estudio.fecha,

                            hora =
                                "12:00"
                        )
                )
            )
        }
    }


    // ------------------------------------------------------------------------
    // ORDENAR DE MÁS RECIENTE A MÁS ANTIGUO
    // ------------------------------------------------------------------------

    val entradasOrdenadas =
        entradas.sortedByDescending {
            it.fechaMillis
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
                            "Bitácora de salud",

                        fontWeight =
                            FontWeight.Bold,

                        color =
                            textoOscuro
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
                    Modifier.height(12.dp)
            )


            // =================================================================
            // INTRODUCCIÓN
            // =================================================================

            Text(

                text =
                    "Tus mediciones y estudios en un solo lugar",

                fontSize =
                    22.sp,

                fontWeight =
                    FontWeight.Bold,

                color =
                    textoOscuro,

                textAlign =
                    TextAlign.Center
            )


            Spacer(
                modifier =
                    Modifier.height(6.dp)
            )


            Text(

                text =
                    "Registra tus mediciones o conserva los resultados completos de tus estudios de laboratorio.",

                fontSize =
                    14.sp,

                color =
                    Color(0xFF64748B),

                textAlign =
                    TextAlign.Center
            )


            Spacer(
                modifier =
                    Modifier.height(22.dp)
            )


            // =================================================================
            // FILTRO
            // =================================================================

            Box(
                modifier =
                    Modifier.fillMaxWidth()
            ) {


                OutlinedButton(

                    onClick = {

                        menuFiltroAbierto =
                            true
                    },

                    modifier =
                        Modifier.fillMaxWidth(),

                    shape =
                        RoundedCornerShape(
                            16.dp
                        )
                ) {

                    Text(

                        text =
                            "Mostrar: $filtroSeleccionado",

                        modifier =
                            Modifier.weight(1f),

                        color =
                            verdePrincipal
                    )


                    Text(
                        text =
                            "▼",

                        color =
                            verdePrincipal
                    )
                }


                DropdownMenu(

                    expanded =
                        menuFiltroAbierto,

                    onDismissRequest = {

                        menuFiltroAbierto =
                            false
                    }
                ) {


                    opcionesFiltro.forEach {
                            opcion ->


                        DropdownMenuItem(

                            text = {

                                Text(
                                    opcion
                                )
                            },

                            onClick = {

                                filtroSeleccionado =
                                    opcion


                                menuFiltroAbierto =
                                    false
                            }
                        )
                    }
                }
            }


            Spacer(
                modifier =
                    Modifier.height(18.dp)
            )


            // =================================================================
            // SIN REGISTROS
            // =================================================================

            if (
                entradasOrdenadas.isEmpty()
            ) {

                Card(

                    modifier =
                        Modifier.fillMaxWidth(),

                    shape =
                        RoundedCornerShape(
                            22.dp
                        ),

                    colors =
                        CardDefaults.cardColors(

                            containerColor =
                                Color(0xFFFEFFF6)
                        )
                ) {

                    Column(

                        modifier =
                            Modifier.padding(
                                26.dp
                            ),

                        horizontalAlignment =
                            Alignment.CenterHorizontally
                    ) {

                        Text(

                            text =
                                "Todavía no hay registros",

                            fontWeight =
                                FontWeight.Bold,

                            color =
                                textoOscuro
                        )


                        Spacer(
                            modifier =
                                Modifier.height(7.dp)
                        )


                        Text(

                            text =
                                "Puedes comenzar agregando una medición o un estudio de laboratorio.",

                            color =
                                Color(0xFF64748B),

                            textAlign =
                                TextAlign.Center,

                            fontSize =
                                13.sp
                        )
                    }
                }

            } else {


                // =============================================================
                // LÍNEA DE TIEMPO
                // =============================================================

                entradasOrdenadas.forEach {
                        entrada ->


                    entrada.registroSalud?.let {
                            registro ->


                        TarjetaRegistroSalud(

                            registro =
                                registro,

                            onEditar = {

                                onEditarRegistro(
                                    registro
                                )
                            },

                            onEliminar = {

                                registroAEliminar =
                                    registro
                            }
                        )
                    }


                    entrada.estudioLaboratorio?.let {
                            estudio ->


                        TarjetaEstudioLaboratorio(

                            estudio =
                                estudio,

                            onEditar = {

                                onEditarEstudio(
                                    estudio
                                )
                            },

                            onEliminar = {

                                estudioAEliminar =
                                    estudio
                            }
                        )
                    }


                    Spacer(
                        modifier =
                            Modifier.height(14.dp)
                    )
                }
            }


            Spacer(
                modifier =
                    Modifier.height(16.dp)
            )


            // =================================================================
            // AGREGAR
            // =================================================================

            Button(

                onClick =
                    onAgregarClick,

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
                        Modifier.width(
                            8.dp
                        )
                )


                Text(

                    text =
                        "Agregar registro",

                    fontWeight =
                        FontWeight.Bold,

                    fontSize =
                        16.sp
                )
            }


            Spacer(
                modifier =
                    Modifier.height(35.dp)
            )
        }
    }


    // ========================================================================
    // ELIMINAR MEDICIÓN
    // ========================================================================

    registroAEliminar?.let {
            registro ->


        AlertDialog(

            onDismissRequest = {

                registroAEliminar =
                    null
            },

            title = {

                Text(
                    "Eliminar medición"
                )
            },

            text = {

                Text(

                    text =
                        "¿Deseas eliminar este registro de ${registro.tipo}?"
                )
            },

            confirmButton = {

                TextButton(

                    onClick = {

                        onEliminarRegistro(
                            registro
                        )


                        registroAEliminar =
                            null
                    }

                ) {

                    Text(

                        text =
                            "Eliminar",

                        color =
                            Color(0xFFB91C1C)
                    )
                }
            },

            dismissButton = {

                TextButton(

                    onClick = {

                        registroAEliminar =
                            null
                    }

                ) {

                    Text(
                        "Cancelar"
                    )
                }
            }
        )
    }


    // ========================================================================
    // ELIMINAR LABORATORIO
    // ========================================================================

    estudioAEliminar?.let {
            estudio ->


        AlertDialog(

            onDismissRequest = {

                estudioAEliminar =
                    null
            },

            title = {

                Text(
                    "Eliminar estudio"
                )
            },

            text = {

                Text(

                    text =
                        "¿Deseas eliminar este estudio de ${nombreVisibleEstudio(estudio)}?"
                )
            },

            confirmButton = {

                TextButton(

                    onClick = {

                        onEliminarEstudio(
                            estudio
                        )


                        estudioAEliminar =
                            null
                    }

                ) {

                    Text(

                        text =
                            "Eliminar",

                        color =
                            Color(0xFFB91C1C)
                    )
                }
            },

            dismissButton = {

                TextButton(

                    onClick = {

                        estudioAEliminar =
                            null
                    }

                ) {

                    Text(
                        "Cancelar"
                    )
                }
            }
        )
    }
}


// ============================================================================
// TARJETA DE MEDICIÓN
// ============================================================================

@Composable
private fun TarjetaRegistroSalud(

    registro: RegistroSalud,

    onEditar: () -> Unit,

    onEliminar: () -> Unit

) {

    Card(

        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(
                22.dp
            ),

        colors =
            CardDefaults.cardColors(

                containerColor =
                    Color(0xFFFEFFF6)
            ),

        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 5.dp
            )
    ) {


        Column(

            modifier =
                Modifier.padding(
                    18.dp
                )
        ) {


            Text(

                text =
                    "MEDICIÓN",

                fontSize =
                    11.sp,

                fontWeight =
                    FontWeight.Bold,

                color =
                    Color(0xFF86A327)
            )


            Spacer(
                modifier =
                    Modifier.height(
                        4.dp
                    )
            )


            Text(

                text =
                    registro.tipo,

                fontSize =
                    19.sp,

                fontWeight =
                    FontWeight.Bold,

                color =
                    Color(0xFF0F172A)
            )


            Spacer(
                modifier =
                    Modifier.height(
                        4.dp
                    )
            )


            Text(

                text =
                    "${registro.fecha} · ${registro.hora}",

                fontSize =
                    13.sp,

                color =
                    Color(0xFF64748B)
            )


            Spacer(
                modifier =
                    Modifier.height(
                        12.dp
                    )
            )


            // =================================================================
            // VALOR
            // =================================================================

            val valorVisible =
                if (
                    registro.valorSecundario.isNotBlank()
                ) {

                    "${registro.valorPrincipal}/${registro.valorSecundario}"

                } else {

                    registro.valorPrincipal
                }


            Text(

                text =
                    buildString {

                        append(
                            valorVisible
                        )

                        if (
                            registro.unidad.isNotBlank()
                        ) {

                            append(
                                " ${registro.unidad}"
                            )
                        }
                    },

                fontSize =
                    23.sp,

                fontWeight =
                    FontWeight.Bold,

                color =
                    Color(0xFF0F766E)
            )


            if (
                registro.condicion.isNotBlank()
            ) {

                Spacer(
                    modifier =
                        Modifier.height(
                            5.dp
                        )
                )


                Text(

                    text =
                        registro.condicion,

                    fontSize =
                        13.sp,

                    color =
                        Color(0xFF64748B)
                )
            }


            if (
                registro.observaciones.isNotBlank()
            ) {

                Spacer(
                    modifier =
                        Modifier.height(
                            9.dp
                        )
                )


                Text(

                    text =
                        "Observaciones: ${registro.observaciones}",

                    fontSize =
                        13.sp,

                    color =
                        Color(0xFF64748B)
                )
            }


            Spacer(
                modifier =
                    Modifier.height(
                        15.dp
                    )
            )


            BotonesEditarEliminar(

                onEditar =
                    onEditar,

                onEliminar =
                    onEliminar
            )
        }
    }
}


// ============================================================================
// TARJETA DE LABORATORIO
// ============================================================================

@Composable
private fun TarjetaEstudioLaboratorio(

    estudio: EstudioLaboratorio,

    onEditar: () -> Unit,

    onEliminar: () -> Unit

) {

    Card(

        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(
                22.dp
            ),

        colors =
            CardDefaults.cardColors(

                containerColor =
                    Color(0xFFFEFFF6)
            ),

        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 5.dp
            )
    ) {


        Column(

            modifier =
                Modifier.padding(
                    18.dp
                )
        ) {


            Text(

                text =
                    "ESTUDIO DE LABORATORIO",

                fontSize =
                    11.sp,

                fontWeight =
                    FontWeight.Bold,

                color =
                    Color(0xFF0D9488)
            )


            Spacer(
                modifier =
                    Modifier.height(
                        4.dp
                    )
            )


            Text(

                text =
                    nombreVisibleEstudio(
                        estudio
                    ),

                fontSize =
                    19.sp,

                fontWeight =
                    FontWeight.Bold,

                color =
                    Color(0xFF0F172A)
            )


            Spacer(
                modifier =
                    Modifier.height(
                        4.dp
                    )
            )


            Text(

                text =
                    estudio.fecha,

                fontSize =
                    13.sp,

                color =
                    Color(0xFF64748B)
            )


            if (
                estudio.laboratorio.isNotBlank()
            ) {

                Spacer(
                    modifier =
                        Modifier.height(
                            8.dp
                        )
                )


                Text(

                    text =
                        "Laboratorio: ${estudio.laboratorio}",

                    fontSize =
                        13.sp,

                    color =
                        Color(0xFF64748B)
                )
            }


            Spacer(
                modifier =
                    Modifier.height(
                        10.dp
                    )
            )


            Text(

                text =
                    when {

                        estudio.archivosUri.isEmpty() ->

                            "Sin archivos adjuntos"


                        estudio.archivosUri.size == 1 ->

                            "1 archivo adjunto"


                        else ->

                            "${estudio.archivosUri.size} archivos adjuntos"
                    },

                fontWeight =
                    FontWeight.SemiBold,

                color =
                    Color(0xFF0F766E)
            )


            if (
                estudio.observaciones.isNotBlank()
            ) {

                Spacer(
                    modifier =
                        Modifier.height(
                            8.dp
                        )
                )


                Text(

                    text =
                        "Observaciones: ${estudio.observaciones}",

                    fontSize =
                        13.sp,

                    color =
                        Color(0xFF64748B)
                )
            }


            Spacer(
                modifier =
                    Modifier.height(
                        15.dp
                    )
            )


            BotonesEditarEliminar(

                onEditar =
                    onEditar,

                onEliminar =
                    onEliminar
            )
        }
    }
}


// ============================================================================
// BOTONES
// ============================================================================

@Composable
private fun BotonesEditarEliminar(

    onEditar: () -> Unit,

    onEliminar: () -> Unit

) {

    Row(

        modifier =
            Modifier.fillMaxWidth(),

        horizontalArrangement =
            Arrangement.spacedBy(
                10.dp
            )
    ) {


        Button(

            onClick =
                onEditar,

            modifier =
                Modifier.weight(
                    1f
                ),

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
                    null
            )


            Spacer(
                modifier =
                    Modifier.width(
                        5.dp
                    )
            )


            Text(
                "Editar"
            )
        }


        OutlinedButton(

            onClick =
                onEliminar,

            modifier =
                Modifier.weight(
                    1f
                )
        ) {


            Icon(

                imageVector =
                    Icons.Default.Delete,

                contentDescription =
                    null,

                tint =
                    Color(0xFFB91C1C)
            )


            Spacer(
                modifier =
                    Modifier.width(
                        5.dp
                    )
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


// ============================================================================
// NOMBRE DEL ESTUDIO
// ============================================================================

private fun nombreVisibleEstudio(
    estudio: EstudioLaboratorio
): String {

    return if (
        estudio.tipoEstudio == "Otro" &&
        estudio.nombrePersonalizado.isNotBlank()
    ) {

        estudio.nombrePersonalizado

    } else {

        estudio.tipoEstudio
    }
}


// ============================================================================
// CONVERTIR FECHA A MILISEGUNDOS
// ============================================================================

private fun obtenerFechaMillis(

    fecha: String,

    hora: String

): Long {

    return try {

        val formatoFecha =
            DateTimeFormatter.ofPattern(
                "dd/MM/yyyy"
            )


        val formatoHora =
            DateTimeFormatter.ofPattern(
                "HH:mm"
            )


        val fechaLocal =
            LocalDate.parse(
                fecha,
                formatoFecha
            )


        val horaLocal =
            LocalTime.parse(
                hora,
                formatoHora
            )


        LocalDateTime
            .of(
                fechaLocal,
                horaLocal
            )
            .atZone(
                ZoneId.systemDefault()
            )
            .toInstant()
            .toEpochMilli()

    } catch (
        e: Exception
    ) {

        0L
    }
}