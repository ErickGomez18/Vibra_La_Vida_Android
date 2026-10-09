package com.example.vibralavida.seguimiento

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.vibralavida.api.SeguimientoPacienteRepository
import com.example.vibralavida.api.modelos.ProfesionalSeguimientoResponse


// ============================================================================
// MI EQUIPO DE SALUD - ANDROID
// ============================================================================
//
// Esta pantalla usa la MISMA API de seguimiento que la página web.
//
// El paciente puede:
// - ver solicitudes pendientes;
// - ver datos públicos del profesional;
// - recibir el OTP dentro de Vibra la Vida;
// - autorizar con el OTP;
// - rechazar la solicitud;
// - ver profesionales ya autorizados.
//
// NO usa SMS.
// ============================================================================

@Composable
fun MiEquipoSaludScreen(

    onBack: () -> Unit,

    onMenuClick: () -> Unit,

    onUnauthorized: () -> Unit

) {

    var cargando by remember {
        mutableStateOf(
            true
        )
    }

    var error by remember {
        mutableStateOf<String?>(
            null
        )
    }

    var mensaje by remember {
        mutableStateOf<String?>(
            null
        )
    }

    var solicitudes by remember {
        mutableStateOf(
            emptyList<ProfesionalSeguimientoResponse>()
        )
    }

    var equipo by remember {
        mutableStateOf(
            emptyList<ProfesionalSeguimientoResponse>()
        )
    }

    // Solicitud para la cual actualmente mostramos el OTP.
    var solicitudConCodigo by remember {
        mutableStateOf<String?>(
            null
        )
    }

    var codigoMostrado by remember {
        mutableStateOf(
            ""
        )
    }

    var codigoEscrito by remember {
        mutableStateOf(
            ""
        )
    }

    var procesando by remember {
        mutableStateOf(
            false
        )
    }


    // ========================================================================
    // CARGAR DATOS
    // ========================================================================

    fun cargar() {

        cargando = true
        error = null

        SeguimientoPacienteRepository
            .obtenerMisVinculos(

                onSuccess = {
                        response ->

                    solicitudes =
                        response.solicitudesPendientes

                    equipo =
                        response.equipoSalud

                    cargando = false
                },

                onUnauthorized = {

                    cargando = false

                    onUnauthorized()
                },

                onError = {
                        mensajeError ->

                    cargando = false
                    error = mensajeError
                }
            )
    }


    LaunchedEffect(
        Unit
    ) {

        cargar()
    }


    // ========================================================================
    // UI
    // ========================================================================

    Column(

        modifier =
            Modifier
                .fillMaxSize()
                .background(
                    Color(0xFFF7FAFC)
                )
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(
                    18.dp
                )
    ) {

        TextButton(
            onClick =
                onMenuClick
        ) {

            Text(
                text =
                    "☰ Menú",

                fontWeight =
                    FontWeight.Bold
            )
        }


        Spacer(
            modifier =
                Modifier.height(
                    8.dp
                )
        )


        Text(

            text =
                "Mi equipo de salud",

            color =
                Color(0xFF123B5D),

            fontSize =
                30.sp,

            fontWeight =
                FontWeight.Bold
        )


        Text(

            text =
                "Revisa quién solicita darte seguimiento y controla quién puede acceder a tus datos.",

            color =
                Color(0xFF64748B),

            fontSize =
                14.sp
        )


        Spacer(
            modifier =
                Modifier.height(
                    22.dp
                )
        )


        if (cargando) {

            CircularProgressIndicator(

                modifier =
                    Modifier.align(
                        Alignment.CenterHorizontally
                    )
            )

            return@Column
        }


        error?.let {

            MensajeEstado(
                texto =
                    it,

                esError =
                    true
            )


            Spacer(
                modifier =
                    Modifier.height(
                        12.dp
                    )
            )


            OutlinedButton(
                onClick = {
                    cargar()
                }
            ) {

                Text(
                    text =
                        "Volver a intentar"
                )
            }
        }


        mensaje?.let {

            MensajeEstado(
                texto =
                    it,

                esError =
                    false
            )


            Spacer(
                modifier =
                    Modifier.height(
                        14.dp
                    )
            )
        }


        // ====================================================================
        // SOLICITUDES PENDIENTES
        // ====================================================================

        Text(

            text =
                "Solicitudes de seguimiento",

            color =
                Color(0xFF0F766E),

            fontSize =
                14.sp,

            fontWeight =
                FontWeight.Bold
        )


        Spacer(
            modifier =
                Modifier.height(
                    10.dp
                )
        )


        if (solicitudes.isEmpty()) {

            Card(

                modifier =
                    Modifier.fillMaxWidth(),

                colors =
                    CardDefaults.cardColors(
                        containerColor =
                            Color.White
                    )
            ) {

                Text(

                    text =
                        "No tienes solicitudes pendientes.",

                    modifier =
                        Modifier.padding(
                            18.dp
                        ),

                    color =
                        Color(0xFF64748B)
                )
            }

        } else {

            solicitudes.forEach {
                    profesional ->

                SolicitudProfesionalCard(

                    profesional =
                        profesional,

                    codigoMostrado =
                        if (
                            solicitudConCodigo ==
                            profesional.vinculoId
                        ) {
                            codigoMostrado
                        } else {
                            ""
                        },

                    codigoEscrito =
                        if (
                            solicitudConCodigo ==
                            profesional.vinculoId
                        ) {
                            codigoEscrito
                        } else {
                            ""
                        },

                    procesando =
                        procesando,

                    onCodigoEscritoChange = {
                            nuevoCodigo ->

                        if (
                            solicitudConCodigo ==
                            profesional.vinculoId
                        ) {

                            codigoEscrito =
                                nuevoCodigo
                                    .filter {
                                        it.isDigit()
                                    }
                                    .take(
                                        6
                                    )
                        }
                    },

                    onRecibirCodigo = {

                        val id =
                            profesional.vinculoId
                                ?: return@SolicitudProfesionalCard

                        procesando = true
                        error = null
                        mensaje = null

                        SeguimientoPacienteRepository
                            .obtenerCodigo(

                                seguimientoId =
                                    id,

                                onSuccess = {
                                        response ->

                                    solicitudConCodigo =
                                        id

                                    codigoMostrado =
                                        response.codigo.orEmpty()

                                    codigoEscrito = ""

                                    mensaje =
                                        "Código generado. Es válido durante ${response.minutosValidez ?: 10} minutos."

                                    procesando = false
                                },

                                onUnauthorized = {

                                    procesando = false

                                    onUnauthorized()
                                },

                                onError = {
                                        mensajeError ->

                                    procesando = false
                                    error = mensajeError
                                }
                            )
                    },

                    onAutorizar = {

                        val id =
                            profesional.vinculoId
                                ?: return@SolicitudProfesionalCard

                        if (
                            codigoEscrito.length != 6
                        ) {

                            error =
                                "Escribe el código de 6 dígitos."

                            return@SolicitudProfesionalCard
                        }

                        procesando = true
                        error = null
                        mensaje = null

                        SeguimientoPacienteRepository
                            .autorizar(

                                seguimientoId =
                                    id,

                                codigo =
                                    codigoEscrito,

                                onSuccess = {
                                        response ->

                                    mensaje =
                                        response.message
                                            ?: "Seguimiento autorizado."

                                    solicitudConCodigo = null
                                    codigoMostrado = ""
                                    codigoEscrito = ""
                                    procesando = false

                                    cargar()
                                },

                                onUnauthorized = {

                                    procesando = false

                                    onUnauthorized()
                                },

                                onError = {
                                        mensajeError ->

                                    procesando = false
                                    error = mensajeError
                                }
                            )
                    },

                    onRechazar = {

                        val id =
                            profesional.vinculoId
                                ?: return@SolicitudProfesionalCard

                        procesando = true
                        error = null
                        mensaje = null

                        SeguimientoPacienteRepository
                            .rechazar(

                                seguimientoId =
                                    id,

                                onSuccess = {
                                        response ->

                                    mensaje =
                                        response.message
                                            ?: "Solicitud rechazada."

                                    procesando = false

                                    cargar()
                                },

                                onUnauthorized = {

                                    procesando = false

                                    onUnauthorized()
                                },

                                onError = {
                                        mensajeError ->

                                    procesando = false
                                    error = mensajeError
                                }
                            )
                    }
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
                    26.dp
                )
        )


        // ====================================================================
        // PROFESIONALES ACTIVOS
        // ====================================================================

        Text(

            text =
                "Profesionales autorizados",

            color =
                Color(0xFF0F766E),

            fontSize =
                14.sp,

            fontWeight =
                FontWeight.Bold
        )


        Spacer(
            modifier =
                Modifier.height(
                    10.dp
                )
        )


        if (equipo.isEmpty()) {

            Card(

                modifier =
                    Modifier.fillMaxWidth(),

                colors =
                    CardDefaults.cardColors(
                        containerColor =
                            Color.White
                    )
            ) {

                Text(

                    text =
                        "Todavía no tienes profesionales autorizados.",

                    modifier =
                        Modifier.padding(
                            18.dp
                        ),

                    color =
                        Color(0xFF64748B)
                )
            }

        } else {

            equipo.forEach {
                    profesional ->

                ProfesionalActivoCard(
                    profesional =
                        profesional
                )


                Spacer(
                    modifier =
                        Modifier.height(
                            12.dp
                        )
                )
            }
        }
    }
}


// ============================================================================
// TARJETA SOLICITUD
// ============================================================================

@Composable
private fun SolicitudProfesionalCard(

    profesional: ProfesionalSeguimientoResponse,

    codigoMostrado: String,

    codigoEscrito: String,

    procesando: Boolean,

    onCodigoEscritoChange: (String) -> Unit,

    onRecibirCodigo: () -> Unit,

    onAutorizar: () -> Unit,

    onRechazar: () -> Unit

) {

    Card(

        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(
                20.dp
            ),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    Color.White
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
                    profesional.nombreCompleto
                        ?: "Profesional de salud",

                color =
                    Color(0xFF123B5D),

                fontSize =
                    20.sp,

                fontWeight =
                    FontWeight.Bold
            )


            Text(

                text =
                    profesional.especialidad
                        ?: profesional.profesionRegistrada
                        ?: "Profesional de salud",

                color =
                    Color(0xFF64748B)
            )


            profesional.cedulaProfesional
                ?.takeIf {
                    it.isNotBlank()
                }
                ?.let {
                        cedula ->

                    Spacer(
                        modifier =
                            Modifier.height(
                                8.dp
                            )
                    )


                    Text(
                        text =
                            if (
                                profesional.cedulaVerificada == true
                            ) {
                                "Cédula profesional: $cedula · Verificada"
                            } else {
                                "Cédula profesional: $cedula"
                            },

                        color =
                            Color(0xFF334155),

                        fontSize =
                            13.sp
                    )
                }


            profesional.consultorio
                ?.direccion
                ?.takeIf {
                    it.isNotBlank()
                }
                ?.let {
                        direccion ->

                    Text(
                        text =
                            "Consultorio: $direccion",

                        color =
                            Color(0xFF334155),

                        fontSize =
                            13.sp
                    )
                }


            Spacer(
                modifier =
                    Modifier.height(
                        14.dp
                    )
            )


            Text(

                text =
                    "Este profesional todavía no puede consultar tus datos. Tú decides si autorizas el seguimiento.",

                color =
                    Color(0xFF475569),

                fontSize =
                    13.sp
            )


            Spacer(
                modifier =
                    Modifier.height(
                        14.dp
                    )
            )


            if (
                codigoMostrado.isBlank()
            ) {

                Row(

                    horizontalArrangement =
                        Arrangement.spacedBy(
                            10.dp
                        )
                ) {

                    OutlinedButton(

                        enabled =
                            !procesando,

                        onClick =
                            onRechazar
                    ) {

                        Text(
                            text =
                                "Rechazar"
                        )
                    }


                    Button(

                        enabled =
                            !procesando,

                        onClick =
                            onRecibirCodigo,

                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor =
                                    Color(0xFF15803D)
                            )
                    ) {

                        Text(
                            text =
                                "Recibir código"
                        )
                    }
                }

            } else {

                Text(

                    text =
                        "Tu código de autorización",

                    color =
                        Color(0xFF64748B),

                    fontSize =
                        12.sp
                )


                Text(

                    text =
                        codigoMostrado,

                    color =
                        Color(0xFF0F766E),

                    fontSize =
                        32.sp,

                    fontWeight =
                        FontWeight.Bold
                )


                Spacer(
                    modifier =
                        Modifier.height(
                            10.dp
                        )
                )


                OutlinedTextField(

                    value =
                        codigoEscrito,

                    onValueChange =
                        onCodigoEscritoChange,

                    modifier =
                        Modifier.fillMaxWidth(),

                    label = {

                        Text(
                            text =
                                "Escribe el código"
                        )
                    },

                    singleLine =
                        true,

                    keyboardOptions =
                        KeyboardOptions(
                            keyboardType =
                                KeyboardType.Number
                        )
                )


                Spacer(
                    modifier =
                        Modifier.height(
                            10.dp
                        )
                )


                Button(

                    modifier =
                        Modifier.fillMaxWidth(),

                    enabled =
                        !procesando &&
                                codigoEscrito.length == 6,

                    onClick =
                        onAutorizar,

                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor =
                                Color(0xFF15803D)
                        )
                ) {

                    Text(
                        text =
                            "Autorizar seguimiento"
                    )
                }
            }
        }
    }
}


// ============================================================================
// TARJETA PROFESIONAL ACTIVO
// ============================================================================

@Composable
private fun ProfesionalActivoCard(

    profesional:
    ProfesionalSeguimientoResponse

) {

    Card(

        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(
                20.dp
            ),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    Color.White
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
                    profesional.nombreCompleto
                        ?: "Profesional de salud",

                color =
                    Color(0xFF123B5D),

                fontSize =
                    19.sp,

                fontWeight =
                    FontWeight.Bold
            )


            Text(

                text =
                    profesional.especialidad
                        ?: profesional.profesionRegistrada
                        ?: "Profesional autorizado",

                color =
                    Color(0xFF64748B)
            )


            Spacer(
                modifier =
                    Modifier.height(
                        8.dp
                    )
            )


            Text(

                text =
                    "Seguimiento autorizado",

                color =
                    Color(0xFF15803D),

                fontWeight =
                    FontWeight.Bold,

                fontSize =
                    13.sp
            )
        }
    }
}


// ============================================================================
// MENSAJE
// ============================================================================

@Composable
private fun MensajeEstado(

    texto: String,

    esError: Boolean

) {

    Card(

        modifier =
            Modifier.fillMaxWidth(),

        colors =
            CardDefaults.cardColors(

                containerColor =
                    if (esError) {
                        Color(0xFFFFE8E8)
                    } else {
                        Color(0xFFE7F8EC)
                    }
            )
    ) {

        Text(

            text =
                texto,

            modifier =
                Modifier.padding(
                    14.dp
                ),

            color =
                if (esError) {
                    Color(0xFFB91C1C)
                } else {
                    Color(0xFF166534)
                },

            fontWeight =
                FontWeight.SemiBold
        )
    }
}
