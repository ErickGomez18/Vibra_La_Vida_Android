package com.example.vibralavida.agenda.citas

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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Videocam

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
// PANTALLA DE CITAS
// ============================================================================
//
// Pantalla para el paciente.
//
// La app móvil solamente consulta las citas creadas por el profesional
// desde el panel web.
//
// El paciente puede:
//
// - Confirmar asistencia.
// - Solicitar reagendación.
// - Solicitar cancelación.
//
// IMPORTANTE:
//
// Una solicitud de reagendación o cancelación NO modifica directamente
// la cita.
//
// El profesional debe revisar la solicitud desde el panel web.
//
// ============================================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitasScreen(

    citas: List<Cita>,

    onBack: () -> Unit,

    onConfirmarAsistencia:
        (Cita) -> Unit = {},

    onSolicitarReagendacion:
        (
        Cita,
        String,
        String,
        String
    ) -> Unit = {
            _,
            _,
            _,
            _ ->
    },

    onSolicitarCancelacion:
        (
        Cita,
        String
    ) -> Unit = {
            _,
            _ ->
    }

) {

    // ========================================================================
    // COLORES
    // ========================================================================

    val fondo =
        Color(0xFFF4F8CE)

    val verdePrincipal =
        Color(0xFF0F766E)

    val textoOscuro =
        Color(0xFF0F172A)

    val textoSecundario =
        Color(0xFF64748B)


    // ========================================================================
    // ORDENAR CITAS
    // ========================================================================
    //
    // Soportamos:
    //
    // dd/MM/yyyy
    //
    // y también:
    //
    // yyyy-MM-dd
    //
    // porque el panel web puede guardar la fecha desde un input type="date".
    //
    // ========================================================================

    val citasOrdenadas =
        citas.sortedBy {

            obtenerFechaHoraMillis(

                fecha =
                    it.fecha,

                hora =
                    it.hora
            )
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
                            "Mis citas",

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
                                Icons.AutoMirrored.Filled.ArrowBack,

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
        }

    ) { paddingValues ->

        Column(

            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(
                        paddingValues
                    )
                    .verticalScroll(
                        rememberScrollState()
                    )
                    .padding(
                        horizontal = 20.dp,
                        vertical = 18.dp
                    ),

            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            // =================================================================
            // TÍTULO
            // =================================================================

            Text(

                text =
                    "Tus citas médicas",

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
                    Modifier.height(
                        6.dp
                    )
            )


            Text(

                text =
                    "Consulta tus citas y responde a las indicaciones del profesional.",

                fontSize =
                    14.sp,

                color =
                    textoSecundario,

                textAlign =
                    TextAlign.Center
            )


            Spacer(

                modifier =
                    Modifier.height(
                        24.dp
                    )
            )


            // =================================================================
            // CONTENIDO
            // =================================================================

            if (
                citasOrdenadas.isEmpty()
            ) {

                TarjetaSinCitas()

            } else {

                citasOrdenadas.forEach {
                        cita ->


                    TarjetaCita(

                        cita =
                            cita,

                        onConfirmarAsistencia =
                            onConfirmarAsistencia,

                        onSolicitarReagendacion =
                            onSolicitarReagendacion,

                        onSolicitarCancelacion =
                            onSolicitarCancelacion
                    )


                    Spacer(

                        modifier =
                            Modifier.height(
                                14.dp
                            )
                    )
                }
            }


            Spacer(

                modifier =
                    Modifier.height(
                        24.dp
                    )
            )
        }
    }
}


// ============================================================================
// TARJETA SIN CITAS
// ============================================================================

@Composable
private fun TarjetaSinCitas() {

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

                defaultElevation =
                    5.dp
            )
    ) {

        Column(

            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(
                        28.dp
                    ),

            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            Icon(

                imageVector =
                    Icons.Default.CalendarMonth,

                contentDescription =
                    null,

                tint =
                    Color(0xFF0F766E)
            )


            Spacer(

                modifier =
                    Modifier.height(
                        12.dp
                    )
            )


            Text(

                text =
                    "No tienes citas programadas",

                fontSize =
                    17.sp,

                fontWeight =
                    FontWeight.Bold,

                color =
                    Color(0xFF0F172A),

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
                    "Cuando un profesional programe una cita, aparecerá aquí.",

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


// ============================================================================
// TARJETA DE CITA
// ============================================================================

@Composable
private fun TarjetaCita(

    cita:
    Cita,

    onConfirmarAsistencia:
        (Cita) -> Unit,

    onSolicitarReagendacion:
        (
        Cita,
        String,
        String,
        String
    ) -> Unit,

    onSolicitarCancelacion:
        (
        Cita,
        String
    ) -> Unit

) {

    val textoOscuro =
        Color(0xFF0F172A)

    val textoSecundario =
        Color(0xFF64748B)

    val verdePrincipal =
        Color(0xFF0F766E)


    // ========================================================================
    // DIÁLOGOS
    // ========================================================================

    var mostrarDialogoReagendar by remember {

        mutableStateOf(
            false
        )
    }


    var mostrarDialogoCancelar by remember {

        mutableStateOf(
            false
        )
    }


    // ========================================================================
    // ESTADO GENERAL DE LA CITA
    // ========================================================================

    val estadoTexto =
        when (
            cita.estado.lowercase()
        ) {

            "confirmada" ->
                "Confirmada"

            "completada" ->
                "Completada"

            "cancelada" ->
                "Cancelada"

            else ->
                "Pendiente"
        }


    val estadoColor =
        when (
            cita.estado.lowercase()
        ) {

            "confirmada" ->
                Color(0xFF15803D)

            "completada" ->
                Color(0xFF475569)

            "cancelada" ->
                Color(0xFFB91C1C)

            else ->
                Color(0xFFB45309)
        }


    // ========================================================================
    // ESTADO DEL PACIENTE
    // ========================================================================

    val estadoPacienteTexto =
        when (
            cita.estadoPaciente.lowercase()
        ) {

            "confirmada" ->
                "Asistencia confirmada"

            "solicitud_reagendar" ->
                "Solicitud de reagendación enviada"

            "solicitud_cancelar" ->
                "Solicitud de cancelación enviada"

            else ->
                "Sin respuesta del paciente"
        }


    val estadoPacienteColor =
        when (
            cita.estadoPaciente.lowercase()
        ) {

            "confirmada" ->
                Color(0xFF15803D)

            "solicitud_reagendar" ->
                Color(0xFFB45309)

            "solicitud_cancelar" ->
                Color(0xFFB91C1C)

            else ->
                textoSecundario
        }


    // ========================================================================
    // ¿SE PUEDEN MOSTRAR ACCIONES?
    // ========================================================================

    val citaCerrada =
        cita.estado.equals(
            "completada",
            ignoreCase = true
        ) ||
                cita.estado.equals(
                    "cancelada",
                    ignoreCase = true
                )


    // ========================================================================
    // TARJETA
    // ========================================================================

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

                defaultElevation =
                    5.dp
            )
    ) {

        Column(

            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(
                        18.dp
                    ),

            verticalArrangement =
                Arrangement.spacedBy(
                    10.dp
                )
        ) {

            // =================================================================
            // ESTADO GENERAL
            // =================================================================

            Text(

                text =
                    "CITA: ${estadoTexto.uppercase()}",

                fontSize =
                    11.sp,

                fontWeight =
                    FontWeight.Bold,

                color =
                    estadoColor
            )


            // =================================================================
            // ESTADO DEL PACIENTE
            // =================================================================

            Text(

                text =
                    estadoPacienteTexto,

                fontSize =
                    12.sp,

                fontWeight =
                    FontWeight.SemiBold,

                color =
                    estadoPacienteColor
            )


            // =================================================================
            // ESPECIALISTA
            // =================================================================

            Row(

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Icon(

                    imageVector =
                        Icons.Default.Person,

                    contentDescription =
                        null,

                    tint =
                        verdePrincipal
                )


                Spacer(

                    modifier =
                        Modifier.width(
                            8.dp
                        )
                )


                Column {

                    Text(

                        text =
                            cita.nombreEspecialista.ifBlank {
                                "Profesional de salud"
                            },

                        fontSize =
                            18.sp,

                        fontWeight =
                            FontWeight.Bold,

                        color =
                            textoOscuro
                    )


                    if (
                        cita.especialidad.isNotBlank()
                    ) {

                        Text(

                            text =
                                cita.especialidad,

                            fontSize =
                                13.sp,

                            color =
                                textoSecundario
                        )
                    }
                }
            }


            // =================================================================
            // FECHA
            // =================================================================

            FilaDatoCita(

                icono =
                    Icons.Default.CalendarMonth,

                texto =
                    cita.fecha.ifBlank {
                        "Fecha no especificada"
                    }
            )


            // =================================================================
            // HORA
            // =================================================================

            FilaDatoCita(

                icono =
                    Icons.Default.Schedule,

                texto =
                    cita.hora.ifBlank {
                        "Hora no especificada"
                    }
            )


            // =================================================================
            // MODALIDAD
            // =================================================================

            if (
                cita.modalidad.isNotBlank()
            ) {

                FilaDatoCita(

                    icono =
                        Icons.Default.Videocam,

                    texto =
                        cita.modalidad
                )
            }


            // =================================================================
            // LUGAR
            // =================================================================

            if (
                cita.lugar.isNotBlank()
            ) {

                FilaDatoCita(

                    icono =
                        Icons.Default.LocationOn,

                    texto =
                        cita.lugar
                )
            }


            // =================================================================
            // MOTIVO
            // =================================================================

            if (
                cita.motivo.isNotBlank()
            ) {

                Text(

                    text =
                        "Motivo",

                    fontSize =
                        12.sp,

                    fontWeight =
                        FontWeight.Bold,

                    color =
                        verdePrincipal
                )


                Text(

                    text =
                        cita.motivo,

                    fontSize =
                        14.sp,

                    color =
                        textoOscuro
                )
            }


            // =================================================================
            // NOTAS
            // =================================================================

            if (
                cita.notas.isNotBlank()
            ) {

                Text(

                    text =
                        "Notas: ${cita.notas}",

                    fontSize =
                        13.sp,

                    color =
                        textoSecundario
                )
            }


            // =================================================================
            // ACCIONES DEL PACIENTE
            // =================================================================

            if (
                !citaCerrada
            ) {

                Spacer(

                    modifier =
                        Modifier.height(
                            4.dp
                        )
                )


                // -------------------------------------------------------------
                // CONFIRMAR ASISTENCIA
                // -------------------------------------------------------------

                if (
                    !cita.estadoPaciente.equals(
                        "confirmada",
                        ignoreCase = true
                    )
                ) {

                    Button(

                        onClick = {

                            onConfirmarAsistencia(
                                cita
                            )
                        },

                        modifier =
                            Modifier.fillMaxWidth(),

                        shape =
                            RoundedCornerShape(
                                16.dp
                            ),

                        colors =
                            ButtonDefaults.buttonColors(

                                containerColor =
                                    verdePrincipal
                            )
                    ) {

                        Text(
                            text =
                                "Confirmar asistencia"
                        )
                    }
                }


                // -------------------------------------------------------------
                // REAGENDAR
                // -------------------------------------------------------------

                if (
                    !cita.estadoPaciente.equals(
                        "solicitud_reagendar",
                        ignoreCase = true
                    )
                ) {

                    OutlinedButton(

                        onClick = {

                            mostrarDialogoReagendar =
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
                                "Solicitar reagendación"
                        )
                    }
                }


                // -------------------------------------------------------------
                // CANCELAR
                // -------------------------------------------------------------

                if (
                    !cita.estadoPaciente.equals(
                        "solicitud_cancelar",
                        ignoreCase = true
                    )
                ) {

                    TextButton(

                        onClick = {

                            mostrarDialogoCancelar =
                                true
                        },

                        modifier =
                            Modifier.fillMaxWidth()
                    ) {

                        Text(

                            text =
                                "Solicitar cancelación",

                            color =
                                Color(0xFFB91C1C)
                        )
                    }
                }
            }
        }
    }


    // ========================================================================
    // DIÁLOGO REAGENDAR
    // ========================================================================

    if (
        mostrarDialogoReagendar
    ) {

        DialogoReagendarCita(

            cita =
                cita,

            onDismiss = {

                mostrarDialogoReagendar =
                    false
            },

            onConfirmar = {
                    fecha,
                    hora,
                    motivo ->


                mostrarDialogoReagendar =
                    false


                onSolicitarReagendacion(

                    cita,

                    fecha,

                    hora,

                    motivo
                )
            }
        )
    }


    // ========================================================================
    // DIÁLOGO CANCELAR
    // ========================================================================

    if (
        mostrarDialogoCancelar
    ) {

        DialogoCancelarCita(

            onDismiss = {

                mostrarDialogoCancelar =
                    false
            },

            onConfirmar = {
                    motivo ->


                mostrarDialogoCancelar =
                    false


                onSolicitarCancelacion(

                    cita,

                    motivo
                )
            }
        )
    }
}


// ============================================================================
// DIÁLOGO PARA REAGENDAR
// ============================================================================

@Composable
private fun DialogoReagendarCita(

    cita:
    Cita,

    onDismiss:
        () -> Unit,

    onConfirmar:
        (
        String,
        String,
        String
    ) -> Unit

) {

    var fecha by remember {

        mutableStateOf(
            ""
        )
    }


    var hora by remember {

        mutableStateOf(
            ""
        )
    }


    var motivo by remember {

        mutableStateOf(
            ""
        )
    }


    AlertDialog(

        onDismissRequest =
            onDismiss,

        title = {

            Text(
                text =
                    "Solicitar reagendación"
            )
        },

        text = {

            Column(

                verticalArrangement =
                    Arrangement.spacedBy(
                        10.dp
                    )
            ) {

                Text(

                    text =
                        "Cita actual: ${cita.fecha} a las ${cita.hora}",

                    fontSize =
                        13.sp,

                    color =
                        Color(0xFF64748B)
                )


                OutlinedTextField(

                    value =
                        fecha,

                    onValueChange = {

                        fecha =
                            it
                    },

                    label = {

                        Text(
                            text =
                                "Nueva fecha"
                        )
                    },

                    placeholder = {

                        Text(
                            text =
                                "dd/MM/yyyy"
                        )
                    },

                    singleLine =
                        true,

                    modifier =
                        Modifier.fillMaxWidth()
                )


                OutlinedTextField(

                    value =
                        hora,

                    onValueChange = {

                        hora =
                            it
                    },

                    label = {

                        Text(
                            text =
                                "Nueva hora"
                        )
                    },

                    placeholder = {

                        Text(
                            text =
                                "HH:mm"
                        )
                    },

                    singleLine =
                        true,

                    modifier =
                        Modifier.fillMaxWidth()
                )


                OutlinedTextField(

                    value =
                        motivo,

                    onValueChange = {

                        motivo =
                            it
                    },

                    label = {

                        Text(
                            text =
                                "Motivo"
                        )
                    },

                    modifier =
                        Modifier.fillMaxWidth()
                )
            }
        },

        confirmButton = {

            TextButton(

                onClick = {

                    if (
                        fecha.isNotBlank() &&
                        hora.isNotBlank()
                    ) {

                        onConfirmar(

                            fecha.trim(),

                            hora.trim(),

                            motivo.trim()
                        )
                    }
                },

                enabled =
                    fecha.isNotBlank() &&
                            hora.isNotBlank()
            ) {

                Text(
                    text =
                        "Enviar solicitud"
                )
            }
        },

        dismissButton = {

            TextButton(

                onClick =
                    onDismiss
            ) {

                Text(
                    text =
                        "Cerrar"
                )
            }
        }
    )
}


// ============================================================================
// DIÁLOGO PARA CANCELAR
// ============================================================================

@Composable
private fun DialogoCancelarCita(

    onDismiss:
        () -> Unit,

    onConfirmar:
        (String) -> Unit

) {

    var motivo by remember {

        mutableStateOf(
            ""
        )
    }


    AlertDialog(

        onDismissRequest =
            onDismiss,

        title = {

            Text(
                text =
                    "Solicitar cancelación"
            )
        },

        text = {

            Column(

                verticalArrangement =
                    Arrangement.spacedBy(
                        10.dp
                    )
            ) {

                Text(

                    text =
                        "La cita no se cancelará automáticamente. El profesional recibirá tu solicitud.",

                    fontSize =
                        13.sp,

                    color =
                        Color(0xFF64748B)
                )


                OutlinedTextField(

                    value =
                        motivo,

                    onValueChange = {

                        motivo =
                            it
                    },

                    label = {

                        Text(
                            text =
                                "Motivo"
                        )
                    },

                    modifier =
                        Modifier.fillMaxWidth()
                )
            }
        },

        confirmButton = {

            TextButton(

                onClick = {

                    if (
                        motivo.isNotBlank()
                    ) {

                        onConfirmar(
                            motivo.trim()
                        )
                    }
                },

                enabled =
                    motivo.isNotBlank()
            ) {

                Text(

                    text =
                        "Enviar solicitud",

                    color =
                        Color(0xFFB91C1C)
                )
            }
        },

        dismissButton = {

            TextButton(

                onClick =
                    onDismiss
            ) {

                Text(
                    text =
                        "Cerrar"
                )
            }
        }
    )
}


// ============================================================================
// FILA DE INFORMACIÓN
// ============================================================================

@Composable
private fun FilaDatoCita(

    icono:
    ImageVector,

    texto:
    String

) {

    Row(

        verticalAlignment =
            Alignment.CenterVertically
    ) {

        Icon(

            imageVector =
                icono,

            contentDescription =
                null,

            tint =
                Color(0xFF0F766E)
        )


        Spacer(

            modifier =
                Modifier.width(
                    8.dp
                )
        )


        Text(

            text =
                texto,

            fontSize =
                14.sp,

            color =
                Color(0xFF475569)
        )
    }
}


// ============================================================================
// CONVERTIR FECHA Y HORA
// ============================================================================
//
// Acepta:
//
// dd/MM/yyyy
//
// y:
//
// yyyy-MM-dd
//
// ============================================================================

private fun obtenerFechaHoraMillis(

    fecha:
    String,

    hora:
    String

): Long {

    return try {

        val formatosFecha =
            listOf(

                DateTimeFormatter.ofPattern(
                    "dd/MM/yyyy"
                ),

                DateTimeFormatter.ofPattern(
                    "yyyy-MM-dd"
                )
            )


        var fechaLocal:
                LocalDate? =
            null


        for (
        formato in formatosFecha
        ) {

            try {

                fechaLocal =
                    LocalDate.parse(
                        fecha,
                        formato
                    )


                break

            } catch (
                ignored: Exception
            ) {

                // Probamos el siguiente formato.
            }
        }


        if (
            fechaLocal == null
        ) {

            return Long.MAX_VALUE
        }


        val formatoHora =
            DateTimeFormatter.ofPattern(
                "HH:mm"
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

        Long.MAX_VALUE
    }
}
