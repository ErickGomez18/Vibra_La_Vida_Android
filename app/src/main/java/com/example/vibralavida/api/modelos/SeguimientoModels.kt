package com.example.vibralavida.api.modelos

// ============================================================================
// MODELOS - SEGUIMIENTO PROFESIONAL
// ============================================================================
//
// Estos modelos representan los datos públicos del profesional y el estado
// del vínculo doctor-paciente.
//
// IMPORTANTE:
// Aquí NO recibimos información privada del paciente.
// ============================================================================

data class ProfesionalSeguimientoResponse(

    val uid: String? = null,

    val vinculoId: String? = null,

    val estado: String? = null,

    val fechaSolicitud: String? = null,

    val fechaVinculacion: String? = null,

    val nombreCompleto: String? = null,

    val profesionRegistrada: String? = null,

    val especialidad: String? = null,

    val cedulaProfesional: String? = null,

    val cedulaVerificada: Boolean? = null,

    val formacionAdicional: String? = null,

    val tieneConsultorio: Boolean? = null,

    val consultorio: ConsultorioProfesionalResponse? = null
)


data class ConsultorioProfesionalResponse(

    val nombre: String? = null,

    val direccion: String? = null,

    val telefono: String? = null,

    val horario: String? = null
)


data class MisVinculosSeguimientoResponse(

    val success: Boolean = false,

    val solicitudesPendientes:
        List<ProfesionalSeguimientoResponse> = emptyList(),

    val equipoSalud:
        List<ProfesionalSeguimientoResponse> = emptyList(),

    val message: String? = null
)


data class CodigoSeguimientoResponse(

    val success: Boolean = false,

    val codigo: String? = null,

    val expiraEn: String? = null,

    val minutosValidez: Int? = null,

    val message: String? = null
)


data class AutorizarSeguimientoRequest(

    val codigo: String
)


data class OperacionSeguimientoResponse(

    val success: Boolean = false,

    val estado: String? = null,

    val message: String? = null
)
