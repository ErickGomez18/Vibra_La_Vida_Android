package com.example.vibralavida.agenda

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Science
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MiAgendaScreen(
    onBack: () -> Unit,
    onMedicamentosClick: () -> Unit,
    onLaboratoriosClick: () -> Unit = {},
    onCitasClick: () -> Unit = {},
    onHistorialClick: () -> Unit = {}
) {

    // Colores similares a los que ya utiliza Vibra la vida
    val verdePrincipal = Color(0xFF16877D)
    val verdeClaro = Color(0xFFE8F6EC)
    val amarilloClaro = Color(0xFFFFFDE1)
    val azulClaro = Color(0xFFEAF7FD)
    val textoOscuro = Color(0xFF243332)

    Scaffold(

        // Barra superior de la pantalla
        topBar = {
            TopAppBar(

                title = {
                    Text(
                        text = "Mi Agenda",
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

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = amarilloClaro
                )
            )
        },

        containerColor = amarilloClaro

    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),

            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(24.dp))

            // Título principal
            Text(
                text = "Tu agenda de salud",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = textoOscuro
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Organiza tus medicamentos, estudios y citas médicas.",
                fontSize = 16.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(30.dp))

            // ---------------------------------------------------------
            // TARJETA DE MEDICAMENTOS
            // ---------------------------------------------------------

            TarjetaAgenda(
                titulo = "Medicamentos",
                descripcion = "Consulta tus medicamentos y próximos horarios.",
                icono = {
                    Icon(
                        imageVector = Icons.Default.MedicalServices,
                        contentDescription = null,
                        tint = verdePrincipal,
                        modifier = Modifier.size(38.dp)
                    )
                },
                colorFondo = verdeClaro,
                colorBoton = verdePrincipal,
                onClick = onMedicamentosClick
            )

            Spacer(modifier = Modifier.height(18.dp))

            // ---------------------------------------------------------
            // TARJETA DE LABORATORIOS
            // ---------------------------------------------------------

            TarjetaAgenda(
                titulo = "Laboratorios",
                descripcion = "Registra y consulta los resultados de tus estudios.",
                icono = {
                    Icon(
                        imageVector = Icons.Default.Science,
                        contentDescription = null,
                        tint = verdePrincipal,
                        modifier = Modifier.size(38.dp)
                    )
                },
                colorFondo = azulClaro,
                colorBoton = verdePrincipal,
                onClick = onLaboratoriosClick
            )

            Spacer(modifier = Modifier.height(18.dp))

            // ---------------------------------------------------------
            // TARJETA DE CITAS
            // ---------------------------------------------------------

            TarjetaAgenda(
                titulo = "Citas médicas",
                descripcion = "Consulta tus próximas citas y recordatorios.",
                icono = {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = null,
                        tint = verdePrincipal,
                        modifier = Modifier.size(38.dp)
                    )
                },
                colorFondo = verdeClaro,
                colorBoton = verdePrincipal,
                onClick = onCitasClick
            )

            Spacer(modifier = Modifier.height(18.dp))

            // ---------------------------------------------------------
            // TARJETA DE HISTORIAL
            // ---------------------------------------------------------

            TarjetaAgenda(
                titulo = "Historial",
                descripcion = "Revisa tus registros y actividades anteriores.",
                icono = {
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = null,
                        tint = verdePrincipal,
                        modifier = Modifier.size(38.dp)
                    )
                },
                colorFondo = azulClaro,
                colorBoton = verdePrincipal,
                onClick = onHistorialClick
            )

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}


// -------------------------------------------------------------------------
// COMPONENTE REUTILIZABLE PARA LAS TARJETAS DE LA AGENDA
// -------------------------------------------------------------------------

@Composable
fun TarjetaAgenda(
    titulo: String,
    descripcion: String,
    icono: @Composable () -> Unit,
    colorFondo: Color,
    colorBoton: Color,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth(),

        shape = RoundedCornerShape(26.dp),

        colors = CardDefaults.cardColors(
            containerColor = colorFondo
        ),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(22.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                // Ícono de la tarjeta
                Surface(
                    modifier = Modifier.size(65.dp),
                    shape = RoundedCornerShape(20.dp),
                    color = Color.White.copy(alpha = 0.75f)
                ) {

                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        icono()
                    }
                }

                Spacer(modifier = Modifier.width(18.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = titulo,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF243332)
                    )

                    Spacer(modifier = Modifier.height(5.dp))

                    Text(
                        text = descripcion,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Button(
                onClick = onClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),

                shape = RoundedCornerShape(18.dp),

                colors = ButtonDefaults.buttonColors(
                    containerColor = colorBoton
                )
            ) {

                Text(
                    text = "Ver",
                    fontSize = 16.sp,
                    color = Color.White
                )
            }
        }
    }
}