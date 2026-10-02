package com.example.vibralavida.agenda.historial

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Science
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vibralavida.agenda.bitacora.EstudioLaboratorio
import com.example.vibralavida.agenda.bitacora.RegistroSalud
import com.example.vibralavida.agenda.citas.Cita
import com.example.vibralavida.agenda.medicamentos.RegistroAdherenciaMedicamento


// ============================================================================
// FILTROS DEL HISTORIAL
// ============================================================================

enum class FiltroHistorialAgenda {

    TODOS,

    MEDICAMENTOS,

    CITAS,

    SALUD
}


// ============================================================================
// HISTORIAL DE AGENDA
// ============================================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistorialAgendaScreen(

    registrosAdherencia:
    List<RegistroAdherenciaMedicamento>,

    citas:
    List<Cita>,

    registrosSalud:
    List<RegistroSalud>,

    estudiosLaboratorio:
    List<EstudioLaboratorio>,

    onBack:
        () -> Unit
) {

    // ------------------------------------------------------------------------
    // COLORES
    // ------------------------------------------------------------------------

    val verdePrincipal =
        Color(0xFF16877D)

    val amarilloClaro =
        Color(0xFFFFFDE1)

    val verdeClaro =
        Color(0xFFE8F6EC)

    val azulClaro =
        Color(0xFFEAF7FD)

    val textoOscuro =
        Color(0xFF243332)


    // ------------------------------------------------------------------------
    // FILTRO
    // ------------------------------------------------------------------------

    var filtroSeleccionado by remember {

        mutableStateOf(
            FiltroHistorialAgenda.TODOS
        )
    }


    Scaffold(

        topBar = {

            TopAppBar(

                title = {

                    Text(
                        text = "Historial",
                        color = textoOscuro,
                        fontWeight = FontWeight.SemiBold
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
                    TopAppBarDefaults
                        .topAppBarColors(
                            containerColor = amarilloClaro
                        )
            )
        },

        containerColor =
            amarilloClaro

    ) { paddingValues ->


        Column(

            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 18.dp)
        ) {


            Spacer(
                modifier = Modifier.height(20.dp)
            )


            Text(
                text = "Historial de tu agenda",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = textoOscuro
            )


            Spacer(
                modifier = Modifier.height(6.dp)
            )


            Text(
                text = "Consulta medicamentos, citas, mediciones y estudios anteriores.",
                fontSize = 15.sp,
                color = Color.Gray
            )


            Spacer(
                modifier = Modifier.height(20.dp)
            )


            // =================================================================
            // FILTROS
            // =================================================================

            Row(

                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(
                        rememberScrollState()
                    ),

                horizontalArrangement =
                    Arrangement.spacedBy(
                        8.dp
                    )
            ) {

                FiltroHistorialButton(
                    texto = "Todos",
                    seleccionado =
                        filtroSeleccionado ==
                                FiltroHistorialAgenda.TODOS,
                    onClick = {
                        filtroSeleccionado =
                            FiltroHistorialAgenda.TODOS
                    }
                )


                FiltroHistorialButton(
                    texto = "Medicamentos",
                    seleccionado =
                        filtroSeleccionado ==
                                FiltroHistorialAgenda.MEDICAMENTOS,
                    onClick = {
                        filtroSeleccionado =
                            FiltroHistorialAgenda.MEDICAMENTOS
                    }
                )


                FiltroHistorialButton(
                    texto = "Citas",
                    seleccionado =
                        filtroSeleccionado ==
                                FiltroHistorialAgenda.CITAS,
                    onClick = {
                        filtroSeleccionado =
                            FiltroHistorialAgenda.CITAS
                    }
                )


                FiltroHistorialButton(
                    texto = "Salud",
                    seleccionado =
                        filtroSeleccionado ==
                                FiltroHistorialAgenda.SALUD,
                    onClick = {
                        filtroSeleccionado =
                            FiltroHistorialAgenda.SALUD
                    }
                )
            }


            Spacer(
                modifier = Modifier.height(18.dp)
            )


            // =================================================================
            // LISTADO
            // =================================================================

            LazyColumn(

                modifier =
                    Modifier.fillMaxSize(),

                verticalArrangement =
                    Arrangement.spacedBy(
                        12.dp
                    ),

                contentPadding =
                    PaddingValues(
                        bottom = 30.dp
                    )
            ) {


                // =============================================================
                // MEDICAMENTOS
                // =============================================================

                if (
                    filtroSeleccionado ==
                    FiltroHistorialAgenda.TODOS
                    ||
                    filtroSeleccionado ==
                    FiltroHistorialAgenda.MEDICAMENTOS
                ) {

                    items(
                        registrosAdherencia
                    ) { registro ->

                        TarjetaHistorialMedicamento(
                            registro = registro,
                            colorFondo = verdeClaro,
                            verdePrincipal = verdePrincipal
                        )
                    }
                }


                // =============================================================
                // CITAS
                // =============================================================

                if (
                    filtroSeleccionado ==
                    FiltroHistorialAgenda.TODOS
                    ||
                    filtroSeleccionado ==
                    FiltroHistorialAgenda.CITAS
                ) {

                    items(
                        citas
                    ) { cita ->

                        TarjetaHistorialCita(
                            cita = cita,
                            colorFondo = azulClaro,
                            verdePrincipal = verdePrincipal
                        )
                    }
                }


                // =============================================================
                // MEDICIONES
                // =============================================================

                if (
                    filtroSeleccionado ==
                    FiltroHistorialAgenda.TODOS
                    ||
                    filtroSeleccionado ==
                    FiltroHistorialAgenda.SALUD
                ) {

                    items(
                        registrosSalud
                    ) { registro ->

                        TarjetaHistorialMedicion(
                            registro = registro,
                            colorFondo = verdeClaro,
                            verdePrincipal = verdePrincipal
                        )
                    }


                    // =========================================================
                    // LABORATORIOS
                    // =========================================================

                    items(
                        estudiosLaboratorio
                    ) { estudio ->

                        TarjetaHistorialLaboratorio(
                            estudio = estudio,
                            colorFondo = azulClaro,
                            verdePrincipal = verdePrincipal
                        )
                    }
                }


                // =============================================================
                // VACÍO
                // =============================================================

                val mostrarVacio =

                    when (
                        filtroSeleccionado
                    ) {

                        FiltroHistorialAgenda.TODOS ->

                            registrosAdherencia.isEmpty() &&
                                    citas.isEmpty() &&
                                    registrosSalud.isEmpty() &&
                                    estudiosLaboratorio.isEmpty()


                        FiltroHistorialAgenda.MEDICAMENTOS ->

                            registrosAdherencia.isEmpty()


                        FiltroHistorialAgenda.CITAS ->

                            citas.isEmpty()


                        FiltroHistorialAgenda.SALUD ->

                            registrosSalud.isEmpty() &&
                                    estudiosLaboratorio.isEmpty()
                    }


                if (
                    mostrarVacio
                ) {

                    item {

                        Box(

                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    top = 50.dp
                                ),

                            contentAlignment =
                                Alignment.Center
                        ) {

                            Text(
                                text = "Aún no hay registros en esta sección.",
                                color = Color.Gray,
                                fontSize = 15.sp
                            )
                        }
                    }
                }
            }
        }
    }
}


// ============================================================================
// BOTÓN DE FILTRO
// ============================================================================

@Composable
private fun FiltroHistorialButton(

    texto:
    String,

    seleccionado:
    Boolean,

    onClick:
        () -> Unit
) {

    val verdePrincipal =
        Color(0xFF16877D)


    Button(

        onClick =
            onClick,

        shape =
            RoundedCornerShape(
                16.dp
            ),

        contentPadding =
            PaddingValues(
                horizontal = 16.dp,
                vertical = 10.dp
            ),

        colors =
            ButtonDefaults.buttonColors(

                containerColor =
                    if (
                        seleccionado
                    ) {

                        verdePrincipal

                    } else {

                        Color.White
                    },

                contentColor =
                    if (
                        seleccionado
                    ) {

                        Color.White

                    } else {

                        verdePrincipal
                    }
            )
    ) {

        Text(
            text = texto,
            fontSize = 13.sp
        )
    }
}


// ============================================================================
// MEDICAMENTO
// ============================================================================

@Composable
private fun TarjetaHistorialMedicamento(

    registro:
    RegistroAdherenciaMedicamento,

    colorFondo:
    Color,

    verdePrincipal:
    Color
) {

    TarjetaBaseHistorial(

        icono = {

            Icon(
                imageVector = Icons.Default.MedicalServices,
                contentDescription = null,
                tint = verdePrincipal
            )
        },

        colorFondo =
            colorFondo

    ) {

        Text(
            text = registro.nombreMedicamento.ifBlank {
                "Medicamento"
            },
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold
        )


        if (
            registro.dosis.isNotBlank()
        ) {

            Spacer(
                modifier = Modifier.height(3.dp)
            )


            Text(
                text = registro.dosis,
                fontSize = 14.sp,
                color = Color.Gray
            )
        }


        Spacer(
            modifier = Modifier.height(5.dp)
        )


        Text(
            text = "${registro.fecha} • ${registro.horarioProgramado}",
            fontSize = 13.sp,
            color = Color.Gray
        )


        Spacer(
            modifier = Modifier.height(5.dp)
        )


        val textoEstado =

            when (
                registro.estado.lowercase()
            ) {

                "tomado" ->
                    "Reportado como tomado"

                "omitido" ->
                    "Reportado como omitido"

                else ->
                    "Pendiente"
            }


        Text(
            text = textoEstado,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = verdePrincipal
        )
    }
}


// ============================================================================
// CITA
// ============================================================================

@Composable
private fun TarjetaHistorialCita(

    cita:
    Cita,

    colorFondo:
    Color,

    verdePrincipal:
    Color
) {

    TarjetaBaseHistorial(

        icono = {

            Icon(
                imageVector = Icons.Default.CalendarMonth,
                contentDescription = null,
                tint = verdePrincipal
            )
        },

        colorFondo =
            colorFondo

    ) {

        Text(
            text = cita.nombreEspecialista.ifBlank {
                "Cita médica"
            },
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold
        )


        if (
            cita.especialidad.isNotBlank()
        ) {

            Spacer(
                modifier = Modifier.height(3.dp)
            )


            Text(
                text = cita.especialidad,
                fontSize = 14.sp,
                color = Color.Gray
            )
        }


        Spacer(
            modifier = Modifier.height(5.dp)
        )


        Text(
            text = "${cita.fecha} • ${cita.hora}",
            fontSize = 13.sp,
            color = Color.Gray
        )


        Spacer(
            modifier = Modifier.height(5.dp)
        )


        val estadoPaciente =

            when (
                cita.estadoPaciente.lowercase()
            ) {

                "confirmada" ->
                    "Asistencia confirmada"

                "solicitud_reagendar" ->
                    "Reagendación solicitada"

                "solicitud_cancelar" ->
                    "Cancelación solicitada"

                else ->
                    cita.estado.replaceFirstChar {
                        it.uppercase()
                    }
            }


        Text(
            text = estadoPaciente,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = verdePrincipal
        )
    }
}


// ============================================================================
// MEDICIÓN
// ============================================================================

@Composable
private fun TarjetaHistorialMedicion(

    registro:
    RegistroSalud,

    colorFondo:
    Color,

    verdePrincipal:
    Color
) {

    TarjetaBaseHistorial(

        icono = {

            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = null,
                tint = verdePrincipal
            )
        },

        colorFondo =
            colorFondo

    ) {

        Text(
            text = registro.tipo.ifBlank {
                "Medición de salud"
            },
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold
        )


        Spacer(
            modifier = Modifier.height(4.dp)
        )


        val valor =

            buildString {

                append(
                    registro.valorPrincipal
                )


                if (
                    registro.valorSecundario.isNotBlank()
                ) {

                    append(
                        " / ${registro.valorSecundario}"
                    )
                }


                if (
                    registro.unidad.isNotBlank()
                ) {

                    append(
                        " ${registro.unidad}"
                    )
                }
            }


        if (
            valor.isNotBlank()
        ) {

            Text(
                text = valor,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = verdePrincipal
            )
        }


        Spacer(
            modifier = Modifier.height(5.dp)
        )


        Text(
            text = "${registro.fecha} • ${registro.hora}",
            fontSize = 13.sp,
            color = Color.Gray
        )


        if (
            registro.condicion.isNotBlank()
        ) {

            Spacer(
                modifier = Modifier.height(5.dp)
            )


            Text(
                text = registro.condicion,
                fontSize = 13.sp,
                color = Color.Gray
            )
        }
    }
}


// ============================================================================
// LABORATORIO
// ============================================================================

@Composable
private fun TarjetaHistorialLaboratorio(

    estudio:
    EstudioLaboratorio,

    colorFondo:
    Color,

    verdePrincipal:
    Color
) {

    TarjetaBaseHistorial(

        icono = {

            Icon(
                imageVector = Icons.Default.Science,
                contentDescription = null,
                tint = verdePrincipal
            )
        },

        colorFondo =
            colorFondo

    ) {

        val nombreEstudio =

            estudio.nombrePersonalizado
                .takeIf {
                    it.isNotBlank()
                }
                ?: estudio.tipoEstudio.ifBlank {
                    "Estudio de laboratorio"
                }


        Text(
            text = nombreEstudio,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold
        )


        Spacer(
            modifier = Modifier.height(5.dp)
        )


        Text(
            text = estudio.fecha,
            fontSize = 13.sp,
            color = Color.Gray
        )


        if (
            estudio.laboratorio.isNotBlank()
        ) {

            Spacer(
                modifier = Modifier.height(4.dp)
            )


            Text(
                text = estudio.laboratorio,
                fontSize = 13.sp,
                color = Color.Gray
            )
        }


        if (
            estudio.archivosUri.isNotEmpty()
        ) {

            Spacer(
                modifier = Modifier.height(5.dp)
            )


            Text(
                text = if (
                    estudio.archivosUri.size == 1
                ) {
                    "1 archivo asociado"
                } else {
                    "${estudio.archivosUri.size} archivos asociados"
                },
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = verdePrincipal
            )
        }
    }
}


// ============================================================================
// TARJETA BASE
// ============================================================================

@Composable
private fun TarjetaBaseHistorial(

    icono:
    @Composable () -> Unit,

    colorFondo:
    Color,

    contenido:
    @Composable () -> Unit
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
                containerColor = colorFondo
            ),

        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 3.dp
            )
    ) {

        Row(

            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    18.dp
                ),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Surface(

                modifier =
                    Modifier.size(
                        52.dp
                    ),

                shape =
                    RoundedCornerShape(
                        16.dp
                    ),

                color =
                    Color.White.copy(
                        alpha = 0.80f
                    )
            ) {

                Box(
                    contentAlignment =
                        Alignment.Center
                ) {

                    icono()
                }
            }


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

                contenido()
            }
        }
    }
}
