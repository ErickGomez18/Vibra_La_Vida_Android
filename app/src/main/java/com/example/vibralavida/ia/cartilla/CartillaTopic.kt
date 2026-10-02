package com.example.vibralavida.ia.cartilla

// ============================================================================
// TEMAS DE LAS CARTILLAS NACIONALES DE SALUD
// ============================================================================
//
// El id debe coincidir con los topics guardados en los JSON de assets.
//
// ============================================================================

enum class CartillaTopic(
    val id: String,
    val displayName: String
) {

    ALIMENTACION("alimentacion", "Alimentación y nutrición"),
    ACTIVIDAD_FISICA("actividad_fisica", "Actividad física"),
    SUENO("sueno", "Sueño y descanso"),
    SALUD_MENTAL("salud_mental", "Salud mental y bienestar emocional"),
    PREVENCION_ADICCIONES("prevencion_adicciones", "Prevención de adicciones"),
    SALUD_SEXUAL_REPRODUCTIVA("salud_sexual_reproductiva", "Salud sexual y reproductiva"),
    SALUD_MENSTRUAL("salud_menstrual", "Salud menstrual"),
    HIGIENE_PERSONAL("higiene_personal", "Higiene personal"),
    SALUD_BUCAL("salud_bucal", "Salud bucal"),
    SALUD_VISUAL("salud_visual", "Salud visual"),
    SALUD_AUDITIVA("salud_auditiva", "Salud auditiva"),
    VACUNACION("vacunacion", "Vacunación"),
    DETECCION_RIESGOS("deteccion_riesgos", "Detección y vigilancia"),
    ENFERMEDADES_CRONICAS("enfermedades_cronicas", "Enfermedades crónicas"),
    CANCER("cancer", "Prevención y detección de cáncer"),
    PREVENCION_VIOLENCIAS("prevencion_violencias", "Prevención de violencias"),
    PREVENCION_ACCIDENTES("prevencion_accidentes", "Prevención de accidentes y lesiones"),
    DESARROLLO_INFANTIL("desarrollo_infantil", "Desarrollo infantil"),
    SUENO_SEGURO("sueno_seguro", "Sueño seguro"),
    LACTANCIA_CRIANZA("lactancia_crianza", "Lactancia, alimentación complementaria y crianza"),
    SIGNOS_ALARMA("signos_alarma", "Signos de alarma"),
    ENVEJECIMIENTO_FUNCIONALIDAD("envejecimiento_funcionalidad", "Envejecimiento saludable y funcionalidad"),
    PREVENCION_CAIDAS("prevencion_caidas", "Prevención de caídas"),
    ENTORNOS_SALUDABLES("entornos_saludables", "Entornos saludables"),
    GENERAL("general", "Promoción y prevención de la salud")
}
