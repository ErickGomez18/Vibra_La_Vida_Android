package com.example.vibralavida.diabetes_mellitus

import android.content.Context
import android.speech.tts.TextToSpeech

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.vibralavida.R

import com.example.vibralavida.api.DiabetesProgresoRepository
import com.example.vibralavida.api.modelos.DiabetesProgresoRequest
import com.example.vibralavida.api.modelos.DiabetesIntentoRequest

import java.util.Locale


// ============================================================================
// COLORES DEL MÓDULO
// ============================================================================

private val FondoDiabetes =
    Color(0xFFF4F8CE)

private val VerdePrincipal =
    Color(0xFF0F766E)

private val AzulTitulo =
    Color(0xFF153B5B)

private val TextoSecundario =
    Color(0xFF64748B)

private val VerdeSuave =
    Color(0xFFE7F7F4)

private val AmarilloSuave =
    Color(0xFFFFF7E8)

private val AzulSuave =
    Color(0xFFEFF6FF)


// ============================================================================
// MODELOS
// ============================================================================

private data class TemaDiabetes(

    val titulo:
        String,

    val descripcion:
        String,

    val contenido:
        List<String>,

    val ejemplo:
        String? = null,

    val datoClave:
        String? = null,

    val nubyRes:
        Int = R.drawable.nuby_tranquilo
)


private enum class DificultadPregunta {
    BASICA,
    INTERMEDIA,
    AVANZADA
}


private data class PreguntaDiabetes(

    val tema:
        String,

    val dificultad:
        DificultadPregunta,

    val pregunta:
        String,

    val opciones:
        List<String>,

    val respuestaCorrecta:
        Int,

    val explicacion:
        String
)


private data class PreguntaSesion(

    val tema:
        String,

    val dificultad:
        DificultadPregunta,

    val pregunta:
        String,

    val opciones:
        List<String>,

    val respuestaCorrecta:
        Int,

    val explicacion:
        String
)


private enum class SeccionDiabetes {
    INICIO,
    TEMA,
    QUIZ,
    RESULTADO
}


// ============================================================================
// PROGRESO LOCAL DEL MÓDULO
// ============================================================================
//
// Fase 2:
// - XP
// - niveles
// - insignias
// - intentos de evaluación
// - mejor puntaje
//
// Por ahora se guarda localmente en SharedPreferences.
// Más adelante lo sincronizaremos con Firebase.
// ============================================================================

private data class ProgresoDiabetes(

    val xp: Int = 0,

    val temasCompletados: Set<String> =
        emptySet(),

    val intentos: Int = 0,

    val mejorPuntaje: Int = 0,

    val temasAReforzar: Set<String> =
        emptySet()
)


private object ProgresoDiabetesLocal {

    private const val PREFS =
        "diabetes_learning_progress"

    private const val KEY_XP =
        "xp"

    private const val KEY_TEMAS =
        "temas_completados"

    private const val KEY_INTENTOS =
        "intentos"

    private const val KEY_MEJOR =
        "mejor_puntaje"

    private const val KEY_REFORZAR =
        "temas_a_reforzar"


    fun cargar(
        context: Context
    ): ProgresoDiabetes {

        val prefs =
            context.getSharedPreferences(
                PREFS,
                Context.MODE_PRIVATE
            )

        return ProgresoDiabetes(

            xp =
                prefs.getInt(
                    KEY_XP,
                    0
                ),

            temasCompletados =
                prefs
                    .getStringSet(
                        KEY_TEMAS,
                        emptySet()
                    )
                    ?.toSet()
                    ?: emptySet(),

            intentos =
                prefs.getInt(
                    KEY_INTENTOS,
                    0
                ),

            mejorPuntaje =
                prefs.getInt(
                    KEY_MEJOR,
                    0
                ),

            temasAReforzar =
                prefs
                    .getStringSet(
                        KEY_REFORZAR,
                        emptySet()
                    )
                    ?.toSet()
                    ?: emptySet()
        )
    }


    fun guardar(
        context: Context,
        progreso: ProgresoDiabetes
    ) {

        context
            .getSharedPreferences(
                PREFS,
                Context.MODE_PRIVATE
            )
            .edit()
            .putInt(
                KEY_XP,
                progreso.xp
            )
            .putStringSet(
                KEY_TEMAS,
                progreso.temasCompletados
            )
            .putInt(
                KEY_INTENTOS,
                progreso.intentos
            )
            .putInt(
                KEY_MEJOR,
                progreso.mejorPuntaje
            )
            .putStringSet(
                KEY_REFORZAR,
                progreso.temasAReforzar
            )
            .apply()
    }
}


// ============================================================================
// NIVELES E INSIGNIAS
// ============================================================================

private fun nombreNivel(
    xp: Int
): String {

    return when {

        xp >= 160 ->
            "Mentor Nuby"

        xp >= 120 ->
            "Experto en diabetes"

        xp >= 80 ->
            "Guardián de la glucosa"

        xp >= 40 ->
            "Explorador metabólico"

        else ->
            "Aprendiz de salud"
    }
}


private fun progresoDelNivel(
    xp: Int
): Float {

    val inicio: Int
    val siguiente: Int

    when {

        xp >= 160 -> {
            inicio = 160
            siguiente = 200
        }

        xp >= 120 -> {
            inicio = 120
            siguiente = 160
        }

        xp >= 80 -> {
            inicio = 80
            siguiente = 120
        }

        xp >= 40 -> {
            inicio = 40
            siguiente = 80
        }

        else -> {
            inicio = 0
            siguiente = 40
        }
    }

    return (
        (xp - inicio).toFloat() /
            (siguiente - inicio).toFloat()
    ).coerceIn(
        0f,
        1f
    )
}


private fun xpParaSiguienteNivel(
    xp: Int
): Int {

    val siguiente =
        when {

            xp >= 160 ->
                200

            xp >= 120 ->
                160

            xp >= 80 ->
                120

            xp >= 40 ->
                80

            else ->
                40
        }

    return (
        siguiente - xp
    ).coerceAtLeast(
        0
    )
}


private fun insigniasDesbloqueadas(
    progreso: ProgresoDiabetes
): List<String> {

    val insignias =
        mutableListOf<String>()


    if (
        progreso.temasCompletados
            .isNotEmpty()
    ) {
        insignias.add(
            "🌱 Primer paso"
        )
    }


    if (
        progreso.temasCompletados
            .contains(
                "Mitos y realidades"
            )
    ) {
        insignias.add(
            "🕵️ Cazador de mitos"
        )
    }


    if (
        progreso.temasCompletados
            .contains(
                "Prevención y hábitos saludables"
            )
    ) {
        insignias.add(
            "🛡️ Prevención activa"
        )
    }


    if (
        progreso.mejorPuntaje >= 8
    ) {
        insignias.add(
            "🧠 Mente saludable"
        )
    }


    if (
        progreso.intentos >= 3
    ) {
        insignias.add(
            "⭐ Constancia Nuby"
        )
    }


    if (
        progreso.temasCompletados.size >=
        temasDiabetes.size &&
        progreso.mejorPuntaje >= 8
    ) {
        insignias.add(
            "🏆 Maestro del módulo"
        )
    }


    return insignias
}




// ============================================================================
// CONVERTIR PROGRESO LOCAL A REQUEST DE API
// ============================================================================

private fun progresoARequest(
    progreso: ProgresoDiabetes
): DiabetesProgresoRequest {

    return DiabetesProgresoRequest(

        xp =
            progreso.xp,

        nivel =
            nombreNivel(
                progreso.xp
            ),

        temasCompletados =
            progreso
                .temasCompletados
                .toList(),

        intentos =
            progreso.intentos,

        mejorPuntaje =
            progreso.mejorPuntaje,

        temasAReforzar =
            progreso
                .temasAReforzar
                .toList()
    )
}

// ============================================================================
// TEXTO PARA LECTURA EN VOZ ALTA
// ============================================================================

private fun textoCompletoTema(
    tema: TemaDiabetes
): String {

    val partes =
        mutableListOf<String>()

    partes.add(
        tema.titulo
    )

    partes.add(
        tema.descripcion
    )

    partes.addAll(
        tema.contenido
    )

    tema.ejemplo?.let {
        partes.add(
            "Ejemplo. $it"
        )
    }

    tema.datoClave?.let {
        partes.add(
            "Dato importante. $it"
        )
    }

    return partes.joinToString(
        separator = ". "
    )
}


// ============================================================================
// CONTENIDO
// ============================================================================

private val temasDiabetes =
    listOf(

        TemaDiabetes(
            titulo =
                "¿Qué es la diabetes?",

            descripcion =
                "Conoce qué hacen la glucosa y la insulina.",

            contenido =
                listOf(
                    "La glucosa es una fuente importante de energía para el cuerpo.",
                    "La insulina es una hormona producida por el páncreas que ayuda a que la glucosa entre a las células.",
                    "La diabetes aparece cuando el cuerpo no produce suficiente insulina, no la utiliza bien o ambas cosas.",
                    "La glucosa elevada durante mucho tiempo puede afectar vasos sanguíneos, nervios, ojos, riñones y corazón."
                ),

            ejemplo =
                "Imagina que la glucosa es el combustible y la insulina es una llave que ayuda a que ese combustible entre a las células.",

            datoClave =
                "La diabetes no significa simplemente «tener demasiada azúcar». El problema está relacionado con cómo el cuerpo maneja la glucosa y la insulina.",

            nubyRes =
                R.drawable.nuby_idea
        ),


        TemaDiabetes(
            titulo =
                "Tipo 1, tipo 2 y otros tipos",

            descripcion =
                "La diabetes no es una sola enfermedad.",

            contenido =
                listOf(
                    "En la diabetes tipo 1 el cuerpo produce muy poca o ninguna insulina y se necesita insulina para vivir.",
                    "En la diabetes tipo 2 el cuerpo no utiliza bien la insulina y con el tiempo puede producir menos de la necesaria.",
                    "La diabetes tipo 2 también puede aparecer en adolescentes y jóvenes.",
                    "La diabetes gestacional aparece durante el embarazo y requiere seguimiento médico.",
                    "Actualmente no se conoce una forma de prevenir la diabetes tipo 1. Muchos casos de tipo 2 sí pueden prevenirse o retrasarse."
                ),

            ejemplo =
                "Dos personas pueden tener diabetes y necesitar cuidados distintos. Por eso no se debe comparar el tratamiento de una persona con el de otra.",

            datoClave =
                "Tipo 1 y tipo 2 son diferentes. Ninguna de las dos se explica simplemente por «comer dulces».",

            nubyRes =
                R.drawable.nuby_esteto
        ),


        TemaDiabetes(
            titulo =
                "Resistencia a la insulina y prediabetes",

            descripcion =
                "No significan exactamente lo mismo que tener diabetes.",

            contenido =
                listOf(
                    "La resistencia a la insulina ocurre cuando las células responden peor a la insulina.",
                    "El páncreas puede compensarlo durante un tiempo produciendo más insulina.",
                    "Prediabetes significa que la glucosa está por encima de lo normal, pero todavía no alcanza criterios de diabetes.",
                    "Tener resistencia a la insulina no significa automáticamente tener diabetes.",
                    "La actividad física, una alimentación saludable, buen descanso y seguimiento profesional pueden ayudar a reducir el riesgo."
                ),

            ejemplo =
                "Una persona puede tener resistencia a la insulina y todavía no cumplir criterios de diabetes. Por eso el diagnóstico requiere valoración profesional.",

            datoClave =
                "Prediabetes no significa que inevitablemente desarrollarás diabetes tipo 2.",

            nubyRes =
                R.drawable.nuby_pensando
        ),


        TemaDiabetes(
            titulo =
                "¿Por qué puede aparecer?",

            descripcion =
                "Conoce los factores que pueden influir.",

            contenido =
                listOf(
                    "La diabetes tipo 2 es multifactorial: no existe una sola causa.",
                    "La genética y los antecedentes familiares pueden aumentar el riesgo.",
                    "La inactividad física, algunos patrones de alimentación, el tabaquismo y otros factores metabólicos también pueden contribuir.",
                    "Tener un factor de riesgo no significa que necesariamente desarrollarás diabetes."
                ),

            ejemplo =
                "Tener familiares con diabetes puede aumentar el riesgo, pero no significa que necesariamente tú también la desarrollarás.",

            datoClave =
                "No es correcto decir que la diabetes es únicamente hereditaria ni únicamente adquirida.",

            nubyRes =
                R.drawable.nuby_pensando
        ),


        TemaDiabetes(
            titulo =
                "Prevención y hábitos saludables",

            descripcion =
                "Hábitos sostenibles pueden proteger tu salud metabólica.",

            contenido =
                listOf(
                    "La diabetes tipo 1 actualmente no tiene una estrategia conocida de prevención.",
                    "La diabetes tipo 2 puede prevenirse o retrasarse en muchas personas.",
                    "La actividad física regular ayuda al cuerpo a utilizar mejor la glucosa.",
                    "Una alimentación variada y equilibrada es más útil que las dietas extremas.",
                    "Dormir bien y evitar el tabaco también forman parte del cuidado."
                ),

            ejemplo =
                "Caminar, bailar, andar en bicicleta o practicar algún deporte son formas de actividad física. No necesitas hacer ejercicio extremo para cuidar tu salud.",

            datoClave =
                "Los hábitos saludables funcionan mejor cuando son sostenibles, no cuando son extremos.",

            nubyRes =
                R.drawable.nuby_bowl
        ),


        TemaDiabetes(
            titulo =
                "Síntomas y diagnóstico",

            descripcion =
                "Reconoce señales sin autodiagnosticarte.",

            contenido =
                listOf(
                    "Algunos síntomas posibles son mucha sed, orinar con frecuencia, cansancio, visión borrosa y pérdida de peso involuntaria.",
                    "En tipo 1 los síntomas pueden aparecer rápido; en tipo 2 pueden ser leves durante mucho tiempo.",
                    "Los síntomas no confirman por sí solos un diagnóstico.",
                    "El diagnóstico se realiza con pruebas de laboratorio e interpretación profesional.",
                    "Vibra la Vida no diagnostica diabetes."
                ),

            ejemplo =
                "Si una persona tiene mucha sed y orina con frecuencia, eso no significa automáticamente que tenga diabetes. Lo correcto es buscar valoración médica.",

            datoClave =
                "Los síntomas orientan, pero no sustituyen las pruebas ni la valoración profesional.",

            nubyRes =
                R.drawable.nuby_esteto
        ),


        TemaDiabetes(
            titulo =
                "Si me diagnostican diabetes",

            descripcion =
                "Un diagnóstico no significa que tu vida normal termine.",

            contenido =
                listOf(
                    "El tratamiento depende del tipo de diabetes y de cada persona.",
                    "Nunca suspendas insulina o medicamentos por tu cuenta.",
                    "La alimentación, la actividad física, el descanso y el seguimiento médico forman parte del tratamiento.",
                    "Un buen control puede prevenir o retrasar complicaciones."
                ),

            ejemplo =
                "Recibir un diagnóstico puede generar dudas o miedo. Preparar preguntas para la consulta puede ayudarte a comprender mejor tu tratamiento.",

            datoClave =
                "Tener diabetes no significa que hayas fracasado ni que no puedas llevar una vida activa.",

            nubyRes =
                R.drawable.nuby_esteto
        ),


        TemaDiabetes(
            titulo =
                "¿Cómo llevar un buen control?",

            descripcion =
                "Cuidarte va más allá de un solo número.",

            contenido =
                listOf(
                    "Las metas de glucosa y HbA1c deben acordarse con el equipo de salud.",
                    "También importan presión arterial, colesterol, actividad física, alimentación, medicamentos y revisiones preventivas.",
                    "No compares tus metas o tratamiento con los de otra persona.",
                    "Si tienes dificultades para seguir un tratamiento, coméntalo con tu profesional de salud."
                ),

            ejemplo =
                "Dos pacientes pueden tener metas diferentes de glucosa o HbA1c. El tratamiento debe individualizarse.",

            datoClave =
                "Buen control no significa perfección; significa seguimiento y decisiones seguras junto con tu equipo de salud.",

            nubyRes =
                R.drawable.nuby_esteto
        ),


        TemaDiabetes(
            titulo =
                "Mitos y realidades",

            descripcion =
                "Aclara ideas que suelen confundirse.",

            contenido =
                listOf(
                    "MITO: «Un susto causa diabetes». El estrés puede modificar temporalmente la glucosa, pero no explica por sí solo la enfermedad.",
                    "MITO: «Comer azúcar causa directamente diabetes». La diabetes tipo 2 es multifactorial.",
                    "MITO: «Solo las personas con obesidad tienen diabetes tipo 2». El peso puede influir, pero no es el único factor.",
                    "MITO: «Usar insulina significa que fracasaste». La insulina es un tratamiento y en tipo 1 es indispensable.",
                    "MITO: «Si me siento bien, mi glucosa está bien». La diabetes tipo 2 puede tener pocos síntomas durante años."
                ),

            ejemplo =
                "Cuando escuches una afirmación sobre diabetes, pregunta: ¿viene de una fuente confiable o solo se ha repetido muchas veces?",

            datoClave =
                "Un mito repetido muchas veces sigue siendo un mito.",

            nubyRes =
                R.drawable.nuby_idea
        ),


        TemaDiabetes(
            titulo =
                "¿Cuándo buscar ayuda?",

            descripcion =
                "Aprende cuándo es importante pedir orientación.",

            contenido =
                listOf(
                    "Consulta si presentas síntomas persistentes o tienes factores de riesgo que te preocupan.",
                    "Si ya tienes diabetes, pide orientación si tus mediciones se encuentran repetidamente fuera de los rangos que te indicaron.",
                    "Busca atención urgente ante deterioro rápido, alteración importante del estado de conciencia, dificultad para respirar, vómitos persistentes o deshidratación intensa.",
                    "Ante una emergencia, Vibra la Vida no sustituye los servicios médicos de urgencias."
                ),

            ejemplo =
                "Si tienes dudas sobre un síntoma o resultado, es mejor preguntar a un profesional que intentar interpretarlo por tu cuenta.",

            datoClave =
                "Ante síntomas graves o deterioro rápido, la prioridad es buscar atención médica.",

            nubyRes =
                R.drawable.nuby_esteto
        )
    )


// ============================================================================
// EVALUACIÓN
// ============================================================================

private const val PREGUNTAS_POR_INTENTO =
    10


private val preguntasDiabetes =
    listOf(

        // ====================================================================
        // NIVEL BÁSICO
        // ====================================================================

        PreguntaDiabetes(
            tema = "¿Qué es la diabetes?",
            dificultad = DificultadPregunta.BASICA,
            pregunta = "¿Cuál es una función de la insulina?",
            opciones = listOf(
                "Ayudar a que la glucosa entre a las células",
                "Eliminar toda la glucosa del cuerpo",
                "Producir glóbulos rojos",
                "Sustituir al páncreas"
            ),
            respuestaCorrecta = 0,
            explicacion = "La insulina ayuda a que la glucosa entre a las células para utilizarse como energía."
        ),

        PreguntaDiabetes(
            tema = "¿Qué es la diabetes?",
            dificultad = DificultadPregunta.BASICA,
            pregunta = "¿Qué es la glucosa?",
            opciones = listOf(
                "Una fuente importante de energía para el cuerpo",
                "Una vitamina",
                "Una bacteria",
                "Un tipo de insulina"
            ),
            respuestaCorrecta = 0,
            explicacion = "La glucosa es una fuente importante de energía para el organismo."
        ),

        PreguntaDiabetes(
            tema = "Tipo 1, tipo 2 y otros tipos",
            dificultad = DificultadPregunta.BASICA,
            pregunta = "¿Qué necesita una persona con diabetes tipo 1 para vivir?",
            opciones = listOf(
                "Insulina",
                "Solo agua",
                "Antibióticos",
                "Ningún tratamiento"
            ),
            respuestaCorrecta = 0,
            explicacion = "En diabetes tipo 1 se necesita insulina porque el organismo produce muy poca o ninguna."
        ),

        PreguntaDiabetes(
            tema = "Tipo 1, tipo 2 y otros tipos",
            dificultad = DificultadPregunta.BASICA,
            pregunta = "¿La diabetes tipo 2 puede aparecer en jóvenes?",
            opciones = listOf(
                "Sí",
                "No, solo en mayores de 40",
                "Solo durante el embarazo",
                "Solo en deportistas"
            ),
            respuestaCorrecta = 0,
            explicacion = "Sí. La diabetes tipo 2 también puede presentarse en adolescentes y jóvenes."
        ),

        PreguntaDiabetes(
            tema = "Resistencia a la insulina y prediabetes",
            dificultad = DificultadPregunta.BASICA,
            pregunta = "¿Tener resistencia a la insulina significa automáticamente tener diabetes?",
            opciones = listOf(
                "No",
                "Sí, siempre",
                "Solo en adolescentes",
                "Solo con antecedentes familiares"
            ),
            respuestaCorrecta = 0,
            explicacion = "No. Puede existir resistencia a la insulina sin cumplir criterios de diabetes."
        ),

        PreguntaDiabetes(
            tema = "Resistencia a la insulina y prediabetes",
            dificultad = DificultadPregunta.BASICA,
            pregunta = "¿Qué significa prediabetes?",
            opciones = listOf(
                "Glucosa elevada sin alcanzar criterios de diabetes",
                "Diabetes tipo 1 avanzada",
                "Glucosa siempre baja",
                "Que nunca habrá diabetes"
            ),
            respuestaCorrecta = 0,
            explicacion = "En prediabetes la glucosa está por encima de lo normal, pero aún no alcanza criterios de diabetes."
        ),

        PreguntaDiabetes(
            tema = "Prevención y hábitos saludables",
            dificultad = DificultadPregunta.BASICA,
            pregunta = "¿Qué hábito puede ayudar a prevenir o retrasar diabetes tipo 2?",
            opciones = listOf(
                "Actividad física regular",
                "Dormir menos",
                "Eliminar todos los carbohidratos",
                "Saltarse comidas"
            ),
            respuestaCorrecta = 0,
            explicacion = "La actividad física regular forma parte de las medidas que pueden reducir el riesgo de diabetes tipo 2."
        ),

        PreguntaDiabetes(
            tema = "Síntomas y diagnóstico",
            dificultad = DificultadPregunta.BASICA,
            pregunta = "¿Cuál puede ser un síntoma de diabetes?",
            opciones = listOf(
                "Mucha sed y orinar con frecuencia",
                "Cambiar de color de cabello",
                "Aumentar de estatura",
                "Tener sueño una sola noche"
            ),
            respuestaCorrecta = 0,
            explicacion = "Mucha sed y orinar con frecuencia pueden aparecer, aunque no confirman por sí solos el diagnóstico."
        ),

        PreguntaDiabetes(
            tema = "Mitos y realidades",
            dificultad = DificultadPregunta.BASICA,
            pregunta = "¿Un susto causa diabetes por sí solo?",
            opciones = listOf(
                "No",
                "Sí",
                "Solo si dura más de una hora",
                "Solo en diabetes tipo 1"
            ),
            respuestaCorrecta = 0,
            explicacion = "No. El estrés puede modificar temporalmente la glucosa, pero un susto no causa diabetes por sí solo."
        ),

        PreguntaDiabetes(
            tema = "Mitos y realidades",
            dificultad = DificultadPregunta.BASICA,
            pregunta = "¿Usar insulina significa que una persona fracasó en su tratamiento?",
            opciones = listOf(
                "No",
                "Sí",
                "Solo en tipo 2",
                "Solo si es joven"
            ),
            respuestaCorrecta = 0,
            explicacion = "No. La insulina es un tratamiento y en diabetes tipo 1 es indispensable."
        ),

        PreguntaDiabetes(
            tema = "Si me diagnostican diabetes",
            dificultad = DificultadPregunta.BASICA,
            pregunta = "¿Se deben suspender los medicamentos para diabetes por cuenta propia?",
            opciones = listOf(
                "No",
                "Sí, si te sientes bien",
                "Sí, después de una semana",
                "Solo si no hay síntomas"
            ),
            respuestaCorrecta = 0,
            explicacion = "No se deben suspender insulina o medicamentos sin indicación del equipo de salud."
        ),

        PreguntaDiabetes(
            tema = "¿Cuándo buscar ayuda?",
            dificultad = DificultadPregunta.BASICA,
            pregunta = "¿Esta evaluación de Vibra la Vida puede diagnosticar diabetes?",
            opciones = listOf(
                "No",
                "Sí",
                "Solo si obtengo menos de 5",
                "Solo si tengo síntomas"
            ),
            respuestaCorrecta = 0,
            explicacion = "No. La evaluación mide aprendizaje; el diagnóstico requiere valoración clínica y pruebas."
        ),


        // ====================================================================
        // NIVEL INTERMEDIO
        // ====================================================================

        PreguntaDiabetes(
            tema = "¿Qué es la diabetes?",
            dificultad = DificultadPregunta.INTERMEDIA,
            pregunta = "¿Qué puede ocurrir si la glucosa permanece elevada durante mucho tiempo?",
            opciones = listOf(
                "Puede afectar vasos sanguíneos, nervios y órganos",
                "Siempre mejora la energía",
                "Convierte la glucosa en vitaminas",
                "No produce ningún efecto"
            ),
            respuestaCorrecta = 0,
            explicacion = "La hiperglucemia mantenida puede afectar vasos sanguíneos, nervios, ojos, riñones, corazón y otros órganos."
        ),

        PreguntaDiabetes(
            tema = "Tipo 1, tipo 2 y otros tipos",
            dificultad = DificultadPregunta.INTERMEDIA,
            pregunta = "¿Cuál diferencia describe mejor tipo 1 y tipo 2?",
            opciones = listOf(
                "En tipo 1 falta insulina; en tipo 2 el cuerpo la utiliza peor",
                "Son exactamente la misma enfermedad",
                "Tipo 1 solo ocurre en adultos",
                "Tipo 2 siempre necesita insulina desde el diagnóstico"
            ),
            respuestaCorrecta = 0,
            explicacion = "En tipo 1 existe una producción muy baja o ausente de insulina; en tipo 2 predomina el uso ineficaz de la insulina."
        ),

        PreguntaDiabetes(
            tema = "Tipo 1, tipo 2 y otros tipos",
            dificultad = DificultadPregunta.INTERMEDIA,
            pregunta = "¿Cuál tipo de diabetes aparece durante el embarazo?",
            opciones = listOf(
                "Diabetes gestacional",
                "Diabetes tipo 1 exclusivamente",
                "Prediabetes neonatal",
                "Resistencia muscular"
            ),
            respuestaCorrecta = 0,
            explicacion = "La diabetes gestacional aparece durante el embarazo y necesita seguimiento médico."
        ),

        PreguntaDiabetes(
            tema = "Resistencia a la insulina y prediabetes",
            dificultad = DificultadPregunta.INTERMEDIA,
            pregunta = "¿Qué puede hacer el páncreas al inicio de la resistencia a la insulina?",
            opciones = listOf(
                "Producir más insulina para compensar",
                "Dejar de funcionar inmediatamente",
                "Producir antibióticos",
                "Eliminar toda la glucosa"
            ),
            respuestaCorrecta = 0,
            explicacion = "Al principio el páncreas puede compensar produciendo más insulina."
        ),

        PreguntaDiabetes(
            tema = "¿Por qué puede aparecer?",
            dificultad = DificultadPregunta.INTERMEDIA,
            pregunta = "¿Qué significa que la diabetes tipo 2 sea multifactorial?",
            opciones = listOf(
                "Que varios factores pueden contribuir a su desarrollo",
                "Que siempre tiene una sola causa",
                "Que únicamente depende de la genética",
                "Que solo depende del consumo de azúcar"
            ),
            respuestaCorrecta = 0,
            explicacion = "Multifactorial significa que intervienen varios factores, como genética, metabolismo, actividad física y otros hábitos."
        ),

        PreguntaDiabetes(
            tema = "Prevención y hábitos saludables",
            dificultad = DificultadPregunta.INTERMEDIA,
            pregunta = "¿Cuál estrategia es más recomendable para cuidar la salud metabólica?",
            opciones = listOf(
                "Hábitos sostenibles de actividad, alimentación y descanso",
                "Dietas extremas por periodos cortos",
                "Eliminar grupos completos de alimentos sin indicación",
                "Ejercicio intenso aunque cause lesión"
            ),
            respuestaCorrecta = 0,
            explicacion = "Los hábitos sostenibles suelen ser más seguros y útiles que las medidas extremas."
        ),

        PreguntaDiabetes(
            tema = "Síntomas y diagnóstico",
            dificultad = DificultadPregunta.INTERMEDIA,
            pregunta = "Si una persona tiene mucha sed y orina frecuentemente, ¿qué es lo correcto?",
            opciones = listOf(
                "Buscar valoración profesional, porque los síntomas no bastan para diagnosticar",
                "Asumir que tiene diabetes",
                "Tomar medicamentos sin valoración",
                "Ignorarlo siempre"
            ),
            respuestaCorrecta = 0,
            explicacion = "Los síntomas pueden orientar, pero el diagnóstico requiere valoración y pruebas."
        ),

        PreguntaDiabetes(
            tema = "Síntomas y diagnóstico",
            dificultad = DificultadPregunta.INTERMEDIA,
            pregunta = "¿Cuál de estas pruebas puede utilizarse para evaluar diabetes?",
            opciones = listOf(
                "HbA1c",
                "Radiografía de brazo",
                "Prueba auditiva",
                "Electroencefalograma"
            ),
            respuestaCorrecta = 0,
            explicacion = "La HbA1c es una de las pruebas utilizadas en la evaluación de diabetes."
        ),

        PreguntaDiabetes(
            tema = "Si me diagnostican diabetes",
            dificultad = DificultadPregunta.INTERMEDIA,
            pregunta = "¿Qué acción puede ayudar después de recibir un diagnóstico?",
            opciones = listOf(
                "Preparar preguntas y conocer el tratamiento indicado",
                "Comparar tu tratamiento con el de otra persona",
                "Suspender los medicamentos si no hay síntomas",
                "Evitar todas las consultas"
            ),
            respuestaCorrecta = 0,
            explicacion = "Comprender el tratamiento y resolver dudas con el equipo de salud facilita el autocuidado."
        ),

        PreguntaDiabetes(
            tema = "¿Cómo llevar un buen control?",
            dificultad = DificultadPregunta.INTERMEDIA,
            pregunta = "¿El buen control de diabetes depende únicamente de la glucosa?",
            opciones = listOf(
                "No, también importan otros factores de salud",
                "Sí, solo importa un valor",
                "Solo depende del peso",
                "Solo depende de la actividad física"
            ),
            respuestaCorrecta = 0,
            explicacion = "El seguimiento también puede incluir presión arterial, colesterol, alimentación, actividad, medicamentos y revisiones preventivas."
        ),

        PreguntaDiabetes(
            tema = "Mitos y realidades",
            dificultad = DificultadPregunta.INTERMEDIA,
            pregunta = "¿Comer azúcar causa directamente diabetes tipo 2 en todos los casos?",
            opciones = listOf(
                "No, la diabetes tipo 2 es multifactorial",
                "Sí, siempre",
                "Solo en adolescentes",
                "Solo una vez al año"
            ),
            respuestaCorrecta = 0,
            explicacion = "La diabetes tipo 2 tiene múltiples factores; no existe una relación simple de 'comer azúcar = tener diabetes'."
        ),

        PreguntaDiabetes(
            tema = "¿Cuándo buscar ayuda?",
            dificultad = DificultadPregunta.INTERMEDIA,
            pregunta = "Si una persona con diabetes tiene mediciones repetidamente fuera de sus metas, ¿qué conviene hacer?",
            opciones = listOf(
                "Consultar a su equipo de salud",
                "Duplicar la medicación por cuenta propia",
                "Ignorarlo",
                "Suspender toda la alimentación"
            ),
            respuestaCorrecta = 0,
            explicacion = "Las mediciones repetidamente fuera de las metas deben comentarse con el equipo de salud."
        ),


        // ====================================================================
        // NIVEL AVANZADO - CASOS
        // ====================================================================

        PreguntaDiabetes(
            tema = "¿Qué es la diabetes?",
            dificultad = DificultadPregunta.AVANZADA,
            pregunta = "Caso: después de comer, la glucosa aumenta. ¿Qué función cumple normalmente la insulina?",
            opciones = listOf(
                "Facilitar que la glucosa entre a las células",
                "Evitar que las células utilicen energía",
                "Transformar la glucosa en antibiótico",
                "Impedir que el páncreas funcione"
            ),
            respuestaCorrecta = 0,
            explicacion = "La insulina facilita la entrada de glucosa a las células para que pueda utilizarse como energía."
        ),

        PreguntaDiabetes(
            tema = "Tipo 1, tipo 2 y otros tipos",
            dificultad = DificultadPregunta.AVANZADA,
            pregunta = "Caso: un adolescente con diabetes tipo 1 piensa dejar la insulina porque se siente bien. ¿Qué debería hacer?",
            opciones = listOf(
                "Continuar el tratamiento y hablar con su equipo de salud",
                "Suspenderla inmediatamente",
                "Tomarla solo cuando tenga síntomas",
                "Sustituirla por ejercicio"
            ),
            respuestaCorrecta = 0,
            explicacion = "En diabetes tipo 1 la insulina es indispensable; nunca debe suspenderse por cuenta propia."
        ),

        PreguntaDiabetes(
            tema = "Resistencia a la insulina y prediabetes",
            dificultad = DificultadPregunta.AVANZADA,
            pregunta = "Caso: una persona tiene prediabetes. ¿Cuál afirmación es más adecuada?",
            opciones = listOf(
                "Tiene mayor riesgo, pero la progresión a tipo 2 no es inevitable",
                "Ya tiene necesariamente diabetes tipo 1",
                "Desarrollará tipo 2 sin importar lo que haga",
                "No necesita seguimiento nunca"
            ),
            respuestaCorrecta = 0,
            explicacion = "La prediabetes aumenta el riesgo, pero la progresión no es inevitable y los hábitos saludables pueden ayudar."
        ),

        PreguntaDiabetes(
            tema = "¿Por qué puede aparecer?",
            dificultad = DificultadPregunta.AVANZADA,
            pregunta = "Caso: Ana tiene antecedentes familiares de diabetes. ¿Qué significa esto?",
            opciones = listOf(
                "Su riesgo puede ser mayor, pero no determina que necesariamente tendrá diabetes",
                "Seguro desarrollará diabetes",
                "La genética no influye en absoluto",
                "Debe tomar medicamentos preventivos sin valoración"
            ),
            respuestaCorrecta = 0,
            explicacion = "Los antecedentes familiares pueden aumentar el riesgo, pero no determinan por sí solos el desarrollo de diabetes."
        ),

        PreguntaDiabetes(
            tema = "Prevención y hábitos saludables",
            dificultad = DificultadPregunta.AVANZADA,
            pregunta = "Caso: Luis quiere prevenir diabetes tipo 2 y propone dejar de comer casi todo. ¿Qué orientación es mejor?",
            opciones = listOf(
                "Elegir hábitos sostenibles y buscar orientación si necesita cambios importantes",
                "Mantener una dieta extrema",
                "Dormir menos para hacer más ejercicio",
                "Eliminar todos los carbohidratos"
            ),
            respuestaCorrecta = 0,
            explicacion = "La prevención se basa en hábitos saludables y sostenibles; las medidas extremas pueden ser innecesarias o inseguras."
        ),

        PreguntaDiabetes(
            tema = "Síntomas y diagnóstico",
            dificultad = DificultadPregunta.AVANZADA,
            pregunta = "Caso: Sofía tiene sed, cansancio y visión borrosa. ¿Qué conclusión es correcta?",
            opciones = listOf(
                "Los síntomas justifican valoración, pero no confirman diabetes",
                "Tiene diabetes con certeza",
                "Debe iniciar insulina sin estudios",
                "Los síntomas nunca se relacionan con glucosa"
            ),
            respuestaCorrecta = 0,
            explicacion = "Los síntomas pueden ser compatibles con diabetes, pero el diagnóstico requiere valoración profesional y pruebas."
        ),

        PreguntaDiabetes(
            tema = "Si me diagnostican diabetes",
            dificultad = DificultadPregunta.AVANZADA,
            pregunta = "Caso: dos amigos con diabetes tienen tratamientos diferentes. ¿Cuál explicación es más adecuada?",
            opciones = listOf(
                "El tratamiento depende del tipo de diabetes y de cada persona",
                "Uno de los dos tratamientos necesariamente está mal",
                "Todos deben recibir exactamente lo mismo",
                "El tratamiento depende solo de la edad"
            ),
            respuestaCorrecta = 0,
            explicacion = "El manejo de diabetes se individualiza según tipo, necesidades, metas y contexto clínico."
        ),

        PreguntaDiabetes(
            tema = "¿Cómo llevar un buen control?",
            dificultad = DificultadPregunta.AVANZADA,
            pregunta = "Caso: una persona compara su meta de HbA1c con la de un familiar. ¿Qué debería recordar?",
            opciones = listOf(
                "Las metas pueden individualizarse y deben acordarse con el equipo de salud",
                "Todas las personas deben tener exactamente la misma meta",
                "La HbA1c no tiene relación con seguimiento",
                "Debe copiar el tratamiento del familiar"
            ),
            respuestaCorrecta = 0,
            explicacion = "Las metas deben individualizarse; no es adecuado copiar objetivos o tratamientos de otra persona."
        ),

        PreguntaDiabetes(
            tema = "Mitos y realidades",
            dificultad = DificultadPregunta.AVANZADA,
            pregunta = "Caso: alguien afirma que solo las personas con obesidad pueden tener diabetes tipo 2. ¿Qué responderías?",
            opciones = listOf(
                "Es un mito; el peso puede influir, pero no es el único factor",
                "Es correcto en todos los casos",
                "La diabetes tipo 2 depende únicamente del peso",
                "Solo ocurre en personas que no hacen ejercicio"
            ),
            respuestaCorrecta = 0,
            explicacion = "El exceso de peso puede aumentar el riesgo, pero no es el único factor y personas sin obesidad también pueden desarrollar tipo 2."
        ),

        PreguntaDiabetes(
            tema = "Mitos y realidades",
            dificultad = DificultadPregunta.AVANZADA,
            pregunta = "Caso: una persona dice que como se siente bien no necesita controles. ¿Qué es más correcto?",
            opciones = listOf(
                "La diabetes tipo 2 puede tener pocos síntomas, por eso el seguimiento sigue siendo importante",
                "Sentirse bien garantiza glucosa normal",
                "Los controles solo sirven si existe dolor",
                "El seguimiento puede suspenderse siempre"
            ),
            respuestaCorrecta = 0,
            explicacion = "La diabetes tipo 2 puede permanecer con pocos síntomas durante años; sentirse bien no sustituye el seguimiento."
        ),

        PreguntaDiabetes(
            tema = "¿Cuándo buscar ayuda?",
            dificultad = DificultadPregunta.AVANZADA,
            pregunta = "Caso: una persona con diabetes presenta vómitos persistentes, dificultad para respirar y deterioro rápido. ¿Qué debe hacer?",
            opciones = listOf(
                "Buscar atención médica urgente",
                "Esperar varios días",
                "Duplicar medicamentos sin indicación",
                "Solo repetir la evaluación de la app"
            ),
            respuestaCorrecta = 0,
            explicacion = "Ante síntomas graves o deterioro rápido debe buscarse atención médica urgente."
        ),

        PreguntaDiabetes(
            tema = "¿Cuándo buscar ayuda?",
            dificultad = DificultadPregunta.AVANZADA,
            pregunta = "Caso: una persona obtiene 3/10 en esta evaluación. ¿Qué significa?",
            opciones = listOf(
                "Que necesita reforzar aprendizaje, no que tenga diabetes",
                "Que tiene diabetes",
                "Que necesita insulina",
                "Que tiene prediabetes"
            ),
            respuestaCorrecta = 0,
            explicacion = "El puntaje evalúa conocimientos del módulo, no riesgo ni diagnóstico médico."
        )
    )


// ============================================================================
// GENERADOR ADAPTATIVO DE PREGUNTAS
// ============================================================================

private fun dificultadActual(
    progreso: ProgresoDiabetes
): DificultadPregunta {

    return when {

        progreso.mejorPuntaje >= 8 ->
            DificultadPregunta.AVANZADA

        progreso.mejorPuntaje >= 5 ->
            DificultadPregunta.INTERMEDIA

        else ->
            DificultadPregunta.BASICA
    }
}


private fun nombreDificultad(
    dificultad: DificultadPregunta
): String {

    return when (
        dificultad
    ) {

        DificultadPregunta.BASICA ->
            "Básico"

        DificultadPregunta.INTERMEDIA ->
            "Intermedio"

        DificultadPregunta.AVANZADA ->
            "Reto Nuby"
    }
}


private fun convertirASesion(
    pregunta: PreguntaDiabetes
): PreguntaSesion {

    val respuestaTexto =
        pregunta.opciones[
            pregunta.respuestaCorrecta
        ]


    val opcionesBarajadas =
        pregunta.opciones
            .shuffled()


    val nuevoIndiceCorrecto =
        opcionesBarajadas
            .indexOf(
                respuestaTexto
            )


    return PreguntaSesion(

        tema =
            pregunta.tema,

        dificultad =
            pregunta.dificultad,

        pregunta =
            pregunta.pregunta,

        opciones =
            opcionesBarajadas,

        respuestaCorrecta =
            nuevoIndiceCorrecto,

        explicacion =
            pregunta.explicacion
    )
}


private fun crearPreguntasSesion(
    progreso: ProgresoDiabetes
): List<PreguntaSesion> {

    val dificultad =
        dificultadActual(
            progreso
        )


    val seleccionadas =
        mutableListOf<PreguntaDiabetes>()


    // ------------------------------------------------------------------------
    // 1. PRIORIZAMOS HASTA 4 PREGUNTAS DE TEMAS FALLADOS
    // ------------------------------------------------------------------------

    if (
        progreso.temasAReforzar
            .isNotEmpty()
    ) {

        val refuerzo =
            preguntasDiabetes
                .filter {
                        pregunta ->

                    pregunta.tema in
                        progreso.temasAReforzar &&
                    pregunta.dificultad ==
                        dificultad
                }
                .shuffled()
                .take(
                    4
                )


        seleccionadas.addAll(
            refuerzo
        )
    }


    // ------------------------------------------------------------------------
    // 2. COMPLETAMOS CON PREGUNTAS DEL NIVEL ACTUAL
    // ------------------------------------------------------------------------

    val faltantesNivel =
        PREGUNTAS_POR_INTENTO -
            seleccionadas.size


    if (
        faltantesNivel >
        0
    ) {

        seleccionadas.addAll(

            preguntasDiabetes
                .filter {
                        pregunta ->

                    pregunta.dificultad ==
                        dificultad &&
                    pregunta !in
                        seleccionadas
                }
                .shuffled()
                .take(
                    faltantesNivel
                )
        )
    }


    // ------------------------------------------------------------------------
    // 3. RESPALDO: SI FALTARAN PREGUNTAS, TOMAMOS DE TODO EL BANCO
    // ------------------------------------------------------------------------

    val faltantesFinales =
        PREGUNTAS_POR_INTENTO -
            seleccionadas.size


    if (
        faltantesFinales >
        0
    ) {

        seleccionadas.addAll(

            preguntasDiabetes
                .filter {
                        pregunta ->

                    pregunta !in
                        seleccionadas
                }
                .shuffled()
                .take(
                    faltantesFinales
                )
        )
    }


    return seleccionadas
        .shuffled()
        .map {
            convertirASesion(
                it
            )
        }
}


// ============================================================================
// PANTALLA PRINCIPAL
// ============================================================================

@Composable
fun DiabetesMellitusScreen(

    onBack:
        () -> Unit,

    onMenuClick:
        () -> Unit

) {

    val context =
        LocalContext.current


    // ========================================================================
    // TEXT TO SPEECH
    // ========================================================================

    var tts by remember {
        mutableStateOf<TextToSpeech?>(
            null
        )
    }

    var ttsListo by remember {
        mutableStateOf(
            false
        )
    }


    DisposableEffect(
        Unit
    ) {

        val engine =
            TextToSpeech(
                context
            ) {
                    status ->

                if (
                    status ==
                    TextToSpeech.SUCCESS
                ) {

                    val resultadoIdioma =
                        tts
                            ?.setLanguage(
                                Locale(
                                    "es",
                                    "MX"
                                )
                            )

                    ttsListo =
                        resultadoIdioma !=
                            TextToSpeech.LANG_MISSING_DATA &&
                        resultadoIdioma !=
                            TextToSpeech.LANG_NOT_SUPPORTED
                }
            }


        tts =
            engine


        onDispose {

            engine.stop()

            engine.shutdown()

            tts =
                null
        }
    }


    val hablar:
        (String) -> Unit = {
            texto ->

        if (
            ttsListo &&
            texto.isNotBlank()
        ) {

            tts
                ?.speak(
                    texto,
                    TextToSpeech.QUEUE_FLUSH,
                    null,
                    "diabetes_tts"
                )
        }
    }


    // ========================================================================
    // NAVEGACIÓN INTERNA
    // ========================================================================

    var seccion by remember {
        mutableStateOf(
            SeccionDiabetes.INICIO
        )
    }


    var tema by remember {
        mutableStateOf<TemaDiabetes?>(
            null
        )
    }


    var puntaje by remember {
        mutableIntStateOf(
            0
        )
    }


    var xpGanadoUltimo by remember {
        mutableIntStateOf(
            0
        )
    }


    // ========================================================================
    // PROGRESO LOCAL
    // ========================================================================

    var progreso by remember {

        mutableStateOf(
            ProgresoDiabetesLocal
                .cargar(
                    context
                )
        )
    }


    // ========================================================================
    // ESTADO DE SINCRONIZACIÓN CON FIREBASE
    // ========================================================================

    var sincronizandoFirebase by remember {
        mutableStateOf(
            true
        )
    }


    var mensajeSincronizacion by remember {
        mutableStateOf(
            "Sincronizando progreso..."
        )
    }


    // ========================================================================
    // CARGAR PROGRESO REMOTO
    // ========================================================================
    //
    // 1. Si Firebase ya tiene progreso, se usa como fuente principal.
    // 2. Si todavía no existe progreso remoto, subimos el progreso local.
    // 3. Si falla Internet/API, seguimos usando SharedPreferences.
    // ========================================================================

    LaunchedEffect(
        Unit
    ) {

        DiabetesProgresoRepository
            .obtenerProgreso(

                onSuccess = {
                        remoto ->

                    sincronizandoFirebase =
                        false


                    if (
                        remoto != null
                    ) {

                        val convertido =
                            ProgresoDiabetes(

                                xp =
                                    remoto.xp,

                                temasCompletados =
                                    remoto
                                        .temasCompletados
                                        .toSet(),

                                intentos =
                                    remoto.intentos,

                                mejorPuntaje =
                                    remoto.mejorPuntaje,

                                temasAReforzar =
                                    remoto
                                        .temasAReforzar
                                        .toSet()
                            )


                        progreso =
                            convertido


                        ProgresoDiabetesLocal
                            .guardar(
                                context,
                                convertido
                            )


                        mensajeSincronizacion =
                            "Progreso sincronizado"

                    } else {

                        // ----------------------------------------------------
                        // PRIMERA VEZ EN FIREBASE:
                        // migramos lo que ya existía localmente.
                        // ----------------------------------------------------

                        DiabetesProgresoRepository
                            .guardarProgreso(

                                progreso =
                                    progresoARequest(
                                        progreso
                                    ),

                                onSuccess = {

                                    sincronizandoFirebase =
                                        false

                                    mensajeSincronizacion =
                                        "Progreso guardado en Firebase"
                                },

                                onError = {

                                    sincronizandoFirebase =
                                        false

                                    mensajeSincronizacion =
                                        "Usando progreso local"
                                }
                            )
                    }
                },

                onUnauthorized = {

                    sincronizandoFirebase =
                        false

                    mensajeSincronizacion =
                        "Sesión no válida"
                },

                onError = {

                    sincronizandoFirebase =
                        false

                    mensajeSincronizacion =
                        "Sin conexión: usando progreso local"
                }
            )
    }


    when (
        seccion
    ) {

        SeccionDiabetes.INICIO -> {

            DiabetesInicio(

                progreso =
                    progreso,

                sincronizandoFirebase =
                    sincronizandoFirebase,

                mensajeSincronizacion =
                    mensajeSincronizacion,

                ttsListo =
                    ttsListo,

                onSpeak =
                    hablar,

                onBack =
                    onBack,

                onMenuClick =
                    onMenuClick,

                onTemaClick = {
                        seleccionado ->

                    tema =
                        seleccionado

                    seccion =
                        SeccionDiabetes.TEMA
                },

                onQuizClick = {

                    seccion =
                        SeccionDiabetes.QUIZ
                }
            )
        }


        SeccionDiabetes.TEMA -> {

            val temaActual =
                tema
                    ?: temasDiabetes.first()


            DiabetesTemaDetalle(

                tema =
                    temaActual,

                completado =
                    progreso
                        .temasCompletados
                        .contains(
                            temaActual.titulo
                        ),

                ttsListo =
                    ttsListo,

                onSpeak =
                    hablar,

                onCompletar = {

                    if (
                        !progreso
                            .temasCompletados
                            .contains(
                                temaActual.titulo
                            )
                    ) {

                        val actualizado =
                            progreso.copy(

                                xp =
                                    progreso.xp +
                                        10,

                                temasCompletados =
                                    progreso
                                        .temasCompletados +
                                        temaActual.titulo
                            )


                        progreso =
                            actualizado


                        ProgresoDiabetesLocal
                            .guardar(
                                context,
                                actualizado
                            )


                        DiabetesProgresoRepository
                            .guardarProgreso(

                                progreso =
                                    progresoARequest(
                                        actualizado
                                    ),

                                onSuccess = {

                                    mensajeSincronizacion =
                                        "Progreso sincronizado"
                                },

                                onError = {

                                    mensajeSincronizacion =
                                        "Guardado local; Firebase pendiente"
                                }
                            )
                    }
                },

                onBack = {

                    seccion =
                        SeccionDiabetes.INICIO
                }
            )
        }


        SeccionDiabetes.QUIZ -> {

            DiabetesQuiz(

                progreso =
                    progreso,

                ttsListo =
                    ttsListo,

                onSpeak =
                    hablar,

                onBack = {

                    seccion =
                        SeccionDiabetes.INICIO
                },

                onFinish = {
                        resultado,
                        temasFallados ->

                    puntaje =
                        resultado


                    val mejora =
                        resultado >
                            progreso.mejorPuntaje


                    val xpGanado =
                        when {

                            progreso.intentos == 0 ->
                                20

                            mejora ->
                                10

                            else ->
                                5
                        }


                    val actualizado =
                        progreso.copy(

                            xp =
                                progreso.xp +
                                    xpGanado,

                            intentos =
                                progreso.intentos +
                                    1,

                            mejorPuntaje =
                                maxOf(
                                    progreso.mejorPuntaje,
                                    resultado
                                ),

                            temasAReforzar =
                                temasFallados
                        )


                    xpGanadoUltimo =
                        xpGanado


                    progreso =
                        actualizado


                    ProgresoDiabetesLocal
                        .guardar(
                            context,
                            actualizado
                        )


                    // --------------------------------------------------------
                    // GUARDAR RESUMEN DEL PROGRESO EN FIREBASE
                    // --------------------------------------------------------

                    DiabetesProgresoRepository
                        .guardarProgreso(

                            progreso =
                                progresoARequest(
                                    actualizado
                                ),

                            onSuccess = {

                                mensajeSincronizacion =
                                    "Progreso sincronizado"
                            },

                            onError = {

                                mensajeSincronizacion =
                                    "Guardado local; Firebase pendiente"
                            }
                        )


                    // --------------------------------------------------------
                    // GUARDAR HISTORIAL DEL INTENTO
                    // --------------------------------------------------------

                    DiabetesProgresoRepository
                        .guardarIntento(

                            intento =
                                DiabetesIntentoRequest(

                                    puntaje =
                                        resultado,

                                    total =
                                        PREGUNTAS_POR_INTENTO,

                                    dificultad =
                                        nombreDificultad(
                                            dificultadActual(
                                                progreso
                                            )
                                        ),

                                    xpGanado =
                                        xpGanado,

                                    temasFallados =
                                        temasFallados.toList()
                                ),

                            onSuccess = {
                                // No necesitamos cambiar pantalla.
                            },

                            onError = {
                                // El resumen local ya quedó guardado.
                            }
                        )


                    seccion =
                        SeccionDiabetes.RESULTADO
                }
            )
        }


        SeccionDiabetes.RESULTADO -> {

            DiabetesResultado(

                puntaje =
                    puntaje,

                total =
                    PREGUNTAS_POR_INTENTO,

                progreso =
                    progreso,

                xpGanado =
                    xpGanadoUltimo,

                ttsListo =
                    ttsListo,

                onSpeak =
                    hablar,

                onRepeat = {

                    seccion =
                        SeccionDiabetes.QUIZ
                },

                onGoHome = {

                    seccion =
                        SeccionDiabetes.INICIO
                }
            )
        }
    }
}


// ============================================================================
// INICIO REDISEÑADO
// ============================================================================

@Composable
private fun DiabetesInicio(

    progreso:
        ProgresoDiabetes,

    sincronizandoFirebase:
        Boolean,

    mensajeSincronizacion:
        String,

    ttsListo:
        Boolean,

    onSpeak:
        (String) -> Unit,

    onBack:
        () -> Unit,

    onMenuClick:
        () -> Unit,

    onTemaClick:
        (TemaDiabetes) -> Unit,

    onQuizClick:
        () -> Unit

) {

    val insignias =
        insigniasDesbloqueadas(
            progreso
        )


    Column(

        modifier =
            Modifier
                .fillMaxSize()
                .background(
                    FondoDiabetes
                )
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(
                    20.dp
                ),

        verticalArrangement =
            Arrangement.spacedBy(
                14.dp
            )
    ) {


        TextButton(
            onClick =
                onMenuClick
        ) {

            Text(
                text =
                    "☰ Menú",

                color =
                    VerdePrincipal,

                fontWeight =
                    FontWeight.Bold
            )
        }


        // ====================================================================
        // PORTADA CON NUBY
        // ====================================================================

        Card(

            colors =
                CardDefaults.cardColors(
                    containerColor =
                        Color.White
                ),

            shape =
                RoundedCornerShape(
                    28.dp
                )
        ) {

            Column(

                modifier =
                    Modifier.padding(
                        18.dp
                    ),

                verticalArrangement =
                    Arrangement.spacedBy(
                        12.dp
                    )
            ) {

                Row(

                    modifier =
                        Modifier.fillMaxWidth(),

                    verticalAlignment =
                        Alignment.CenterVertically,

                    horizontalArrangement =
                        Arrangement.spacedBy(
                            14.dp
                        )
                ) {

                    Image(

                        painter =
                            painterResource(
                                id =
                                    R.drawable.nuby_saludando
                            ),

                        contentDescription =
                            "Nuby dando la bienvenida",

                        modifier =
                            Modifier.size(
                                105.dp
                            ),

                        contentScale =
                            ContentScale.Fit
                    )


                    Column(

                        modifier =
                            Modifier.weight(
                                1f
                            )
                    ) {

                        Text(

                            text =
                                "Diabetes Mellitus",

                            fontSize =
                                27.sp,

                            fontWeight =
                                FontWeight.Bold,

                            color =
                                AzulTitulo
                        )


                        Spacer(
                            modifier =
                                Modifier.height(
                                    5.dp
                                )
                        )


                        Text(

                            text =
                                "Aprende con Nuby qué es, cómo prevenir riesgos y cómo cuidar tu salud.",

                            fontSize =
                                14.sp,

                            lineHeight =
                                20.sp,

                            color =
                                TextoSecundario
                        )
                    }
                }


                BotonEscuchar(

                    textoBoton =
                        "Escuchar introducción",

                    habilitado =
                        ttsListo,

                    onClick = {

                        onSpeak(
                            "Diabetes Mellitus. Aprende con Nuby qué es la diabetes, cómo prevenir riesgos y cómo cuidar tu salud. Este módulo es educativo y no sustituye una consulta médica."
                        )
                    }
                )
            }
        }


        // ====================================================================
        // PROGRESO
        // ====================================================================

        Card(

            colors =
                CardDefaults.cardColors(
                    containerColor =
                        AzulSuave
                ),

            shape =
                RoundedCornerShape(
                    24.dp
                )
        ) {

            Column(

                modifier =
                    Modifier.padding(
                        17.dp
                    ),

                verticalArrangement =
                    Arrangement.spacedBy(
                        8.dp
                    )
            ) {

                Row(

                    modifier =
                        Modifier.fillMaxWidth(),

                    horizontalArrangement =
                        Arrangement.SpaceBetween,

                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Column {

                        Text(

                            text =
                                "Tu progreso",

                            fontWeight =
                                FontWeight.Bold,

                            color =
                                AzulTitulo,

                            fontSize =
                                17.sp
                        )


                        Text(

                            text =
                                nombreNivel(
                                    progreso.xp
                                ),

                            color =
                                VerdePrincipal,

                            fontWeight =
                                FontWeight.Bold,

                            fontSize =
                                13.sp
                        )
                    }


                    Text(

                        text =
                            "${progreso.xp} XP",

                        color =
                            VerdePrincipal,

                        fontWeight =
                            FontWeight.Bold,

                        fontSize =
                            18.sp
                    )
                }


                Text(

                    text =
                        if (
                            sincronizandoFirebase
                        ) {
                            "☁️ Sincronizando..."
                        } else {
                            "☁️ $mensajeSincronizacion"
                        },

                    fontSize =
                        11.sp,

                    color =
                        if (
                            sincronizandoFirebase
                        ) {
                            TextoSecundario
                        } else {
                            VerdePrincipal
                        }
                )


                LinearProgressIndicator(

                    progress = {
                        progresoDelNivel(
                            progreso.xp
                        )
                    },

                    modifier =
                        Modifier.fillMaxWidth(),

                    color =
                        VerdePrincipal,

                    trackColor =
                        Color(0xFFDCE8F2)
                )


                Text(

                    text =
                        "${xpParaSiguienteNivel(progreso.xp)} XP para el siguiente nivel",

                    fontSize =
                        11.sp,

                    color =
                        TextoSecundario
                )


                Text(

                    text =
                        "Temas completados: ${progreso.temasCompletados.size}/${temasDiabetes.size} · Evaluaciones: ${progreso.intentos} · Mejor resultado: ${progreso.mejorPuntaje}/10",

                    fontSize =
                        11.sp,

                    lineHeight =
                        16.sp,

                    color =
                        TextoSecundario
                )


                if (
                    progreso.temasAReforzar
                        .isNotEmpty()
                ) {

                    Text(

                        text =
                            "🎯 Próximo intento: Nuby reforzará ${progreso.temasAReforzar.size} tema(s) que necesitan práctica.",

                        fontSize =
                            11.sp,

                        lineHeight =
                            16.sp,

                        color =
                            VerdePrincipal,

                        fontWeight =
                            FontWeight.SemiBold
                    )
                }


                if (
                    insignias.isNotEmpty()
                ) {

                    Text(

                        text =
                            "Insignias: ${insignias.joinToString("  ")}",

                        fontSize =
                            11.sp,

                        lineHeight =
                            17.sp,

                        color =
                            Color(0xFF334155)
                    )
                }
            }
        }


        // ====================================================================
        // QUÉ APRENDERÁS
        // ====================================================================

        Text(

            text =
                "¿Qué aprenderás?",

            fontSize =
                20.sp,

            fontWeight =
                FontWeight.Bold,

            color =
                AzulTitulo
        )


        Row(

            modifier =
                Modifier.fillMaxWidth(),

            horizontalArrangement =
                Arrangement.spacedBy(
                    10.dp
                )
        ) {

            MiniDatoCard(
                emoji = "🧠",
                titulo = "Entender",
                texto = "Glucosa e insulina",
                modifier = Modifier.weight(1f)
            )

            MiniDatoCard(
                emoji = "🛡️",
                titulo = "Prevenir",
                texto = "Hábitos saludables",
                modifier = Modifier.weight(1f)
            )
        }


        Row(

            modifier =
                Modifier.fillMaxWidth(),

            horizontalArrangement =
                Arrangement.spacedBy(
                    10.dp
                )
        ) {

            MiniDatoCard(
                emoji = "💬",
                titulo = "Desmentir",
                texto = "Mitos frecuentes",
                modifier = Modifier.weight(1f)
            )

            MiniDatoCard(
                emoji = "❤️",
                titulo = "Cuidarte",
                texto = "Control y seguimiento",
                modifier = Modifier.weight(1f)
            )
        }


        Card(

            colors =
                CardDefaults.cardColors(
                    containerColor =
                        VerdeSuave
                ),

            shape =
                RoundedCornerShape(
                    20.dp
                )
        ) {

            Row(

                modifier =
                    Modifier.padding(
                        16.dp
                    ),

                horizontalArrangement =
                    Arrangement.spacedBy(
                        10.dp
                    )
            ) {

                Text(
                    text =
                        "ℹ️",

                    fontSize =
                        20.sp
                )


                Text(

                    text =
                        "Este módulo es educativo. No diagnostica diabetes ni sustituye una consulta médica.",

                    modifier =
                        Modifier.weight(
                            1f
                        ),

                    color =
                        Color(0xFF0F5D56),

                    fontSize =
                        13.sp,

                    lineHeight =
                        19.sp
                )
            }
        }


        Text(

            text =
                "Explora los temas",

            fontSize =
                20.sp,

            fontWeight =
                FontWeight.Bold,

            color =
                AzulTitulo
        )


        temasDiabetes.forEachIndexed {
                index,
                item ->

            val completado =
                progreso
                    .temasCompletados
                    .contains(
                        item.titulo
                    )


            TemaCard(

                numero =
                    index + 1,

                tema =
                    item,

                completado =
                    completado,

                onClick = {

                    onTemaClick(
                        item
                    )
                }
            )
        }


        // ====================================================================
        // EVALUACIÓN
        // ====================================================================

        Card(

            colors =
                CardDefaults.cardColors(
                    containerColor =
                        AzulSuave
                ),

            shape =
                RoundedCornerShape(
                    26.dp
                )
        ) {

            Row(

                modifier =
                    Modifier.padding(
                        18.dp
                    ),

                verticalAlignment =
                    Alignment.CenterVertically,

                horizontalArrangement =
                    Arrangement.spacedBy(
                        12.dp
                    )
            ) {

                Image(

                    painter =
                        painterResource(
                            id =
                                R.drawable.nuby_feliz
                        ),

                    contentDescription =
                        "Nuby feliz",

                    modifier =
                        Modifier.size(
                            82.dp
                        ),

                    contentScale =
                        ContentScale.Fit
                )


                Column(

                    modifier =
                        Modifier.weight(
                            1f
                        ),

                    verticalArrangement =
                        Arrangement.spacedBy(
                            8.dp
                        )
                ) {

                    Text(

                        text =
                            "¿Qué aprendí sobre diabetes?",

                        fontSize =
                            18.sp,

                        fontWeight =
                            FontWeight.Bold,

                        color =
                            AzulTitulo
                    )


                    Text(

                        text =
                            "Cada intento genera preguntas diferentes. Nuby adapta la dificultad y refuerza los temas que más te cuestan.",

                        fontSize =
                            13.sp,

                        color =
                            TextoSecundario,

                        lineHeight =
                            18.sp
                    )


                    Button(

                        onClick =
                            onQuizClick,

                        modifier =
                            Modifier.fillMaxWidth(),

                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor =
                                    VerdePrincipal
                            )
                    ) {

                        Text(
                            text =
                                if (
                                    progreso.intentos == 0
                                ) {
                                    "Comenzar evaluación"
                                } else {
                                    "Volver a intentarlo"
                                }
                        )
                    }
                }
            }
        }


        Text(

            text =
                "Fuentes educativas de referencia: OMS y NIDDK.",

            fontSize =
                11.sp,

            color =
                Color(0xFF94A3B8),

            modifier =
                Modifier.padding(
                    vertical = 8.dp
                )
        )
    }
}


// ============================================================================
// MINI CARD
// ============================================================================

@Composable
private fun MiniDatoCard(

    emoji:
        String,

    titulo:
        String,

    texto:
        String,

    modifier:
        Modifier = Modifier

) {

    Card(

        modifier =
            modifier,

        colors =
            CardDefaults.cardColors(
                containerColor =
                    Color.White
            ),

        shape =
            RoundedCornerShape(
                18.dp
            )
    ) {

        Column(

            modifier =
                Modifier.padding(
                    14.dp
                ),

            verticalArrangement =
                Arrangement.spacedBy(
                    5.dp
                )
        ) {

            Text(
                text =
                    emoji,

                fontSize =
                    22.sp
            )


            Text(

                text =
                    titulo,

                fontWeight =
                    FontWeight.Bold,

                color =
                    AzulTitulo,

                fontSize =
                    14.sp
            )


            Text(

                text =
                    texto,

                fontSize =
                    11.sp,

                color =
                    TextoSecundario,

                lineHeight =
                    15.sp
            )
        }
    }
}


// ============================================================================
// TARJETA DE TEMA
// ============================================================================

@Composable
private fun TemaCard(

    numero:
        Int,

    tema:
        TemaDiabetes,

    completado:
        Boolean = false,

    onClick:
        () -> Unit

) {

    Card(

        modifier =
            Modifier
                .fillMaxWidth()
                .clickable {
                    onClick()
                },

        colors =
            CardDefaults.cardColors(
                containerColor =
                    Color.White
            ),

        shape =
            RoundedCornerShape(
                22.dp
            )
    ) {

        Row(

            modifier =
                Modifier.padding(
                    14.dp
                ),

            verticalAlignment =
                Alignment.CenterVertically,

            horizontalArrangement =
                Arrangement.spacedBy(
                    12.dp
                )
        ) {

            Box(

                modifier =
                    Modifier
                        .size(
                            44.dp
                        )
                        .background(
                            color =
                                VerdeSuave,

                            shape =
                                CircleShape
                        ),

                contentAlignment =
                    Alignment.Center
            ) {

                Text(

                    text =
                        numero.toString(),

                    color =
                        VerdePrincipal,

                    fontWeight =
                        FontWeight.Bold
                )
            }


            Column(

                modifier =
                    Modifier.weight(
                        1f
                    )
            ) {

                Text(

                    text =
                        tema.titulo,

                    fontSize =
                        15.sp,

                    fontWeight =
                        FontWeight.Bold,

                    color =
                        AzulTitulo
                )


                Spacer(
                    modifier =
                        Modifier.height(
                            3.dp
                        )
                )


                Text(

                    text =
                        tema.descripcion,

                    fontSize =
                        12.sp,

                    color =
                        TextoSecundario,

                    lineHeight =
                        17.sp
                )
            }


            Column(

                horizontalAlignment =
                    Alignment.CenterHorizontally
            ) {

                Image(

                    painter =
                        painterResource(
                            id =
                                tema.nubyRes
                        ),

                    contentDescription =
                        null,

                    modifier =
                        Modifier.size(
                            48.dp
                        ),

                    contentScale =
                        ContentScale.Fit
                )


                if (
                    completado
                ) {

                    Text(

                        text =
                            "✓",

                        color =
                            VerdePrincipal,

                        fontWeight =
                            FontWeight.Bold,

                        fontSize =
                            16.sp
                    )
                }
            }
        }
    }
}


// ============================================================================
// DETALLE VISUAL DEL TEMA
// ============================================================================

@Composable
private fun DiabetesTemaDetalle(

    tema:
        TemaDiabetes,

    completado:
        Boolean,

    ttsListo:
        Boolean,

    onSpeak:
        (String) -> Unit,

    onCompletar:
        () -> Unit,

    onBack:
        () -> Unit

) {

    Column(

        modifier =
            Modifier
                .fillMaxSize()
                .background(
                    FondoDiabetes
                )
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(
                    20.dp
                ),

        verticalArrangement =
            Arrangement.spacedBy(
                14.dp
            )
    ) {


        TextButton(
            onClick =
                onBack
        ) {

            Text(
                text =
                    "← Volver a Diabetes Mellitus",

                color =
                    VerdePrincipal
            )
        }


        // ====================================================================
        // CABECERA DEL TEMA
        // ====================================================================

        Card(

            colors =
                CardDefaults.cardColors(
                    containerColor =
                        Color.White
                ),

            shape =
                RoundedCornerShape(
                    26.dp
                )
        ) {

            Column(

                modifier =
                    Modifier.padding(
                        18.dp
                    ),

                verticalArrangement =
                    Arrangement.spacedBy(
                        12.dp
                    )
            ) {

                Row(

                    modifier =
                        Modifier.fillMaxWidth(),

                    verticalAlignment =
                        Alignment.CenterVertically,

                    horizontalArrangement =
                        Arrangement.spacedBy(
                            14.dp
                        )
                ) {

                    Image(

                        painter =
                            painterResource(
                                id =
                                    tema.nubyRes
                            ),

                        contentDescription =
                            "Nuby acompañando el tema",

                        modifier =
                            Modifier.size(
                                92.dp
                            ),

                        contentScale =
                            ContentScale.Fit
                    )


                    Column(

                        modifier =
                            Modifier.weight(
                                1f
                            )
                    ) {

                        Text(

                            text =
                                tema.titulo,

                            fontSize =
                                23.sp,

                            fontWeight =
                                FontWeight.Bold,

                            color =
                                AzulTitulo,

                            lineHeight =
                                28.sp
                        )


                        Spacer(
                            modifier =
                                Modifier.height(
                                    5.dp
                                )
                        )


                        Text(

                            text =
                                tema.descripcion,

                            fontSize =
                                14.sp,

                            color =
                                TextoSecundario,

                            lineHeight =
                                20.sp
                        )
                    }
                }


                BotonEscuchar(

                    textoBoton =
                        "Escuchar este tema",

                    habilitado =
                        ttsListo,

                    onClick = {

                        onSpeak(
                            textoCompletoTema(
                                tema
                            )
                        )
                    }
                )
            }
        }


        when (
            tema.titulo
        ) {

            "¿Qué es la diabetes?" -> {
                FlujoGlucosaVisual()
            }

            "Tipo 1, tipo 2 y otros tipos" -> {
                ComparacionTiposVisual()
            }

            "Resistencia a la insulina y prediabetes" -> {
                CaminoMetabolicoVisual()
            }

            "Mitos y realidades" -> {
                MitosVisual(
                    tema =
                        tema
                )
            }
        }


        if (
            tema.titulo !=
            "Mitos y realidades"
        ) {

            tema.contenido.forEach {
                    parrafo ->

                Card(

                    colors =
                        CardDefaults.cardColors(
                            containerColor =
                                Color.White
                        ),

                    shape =
                        RoundedCornerShape(
                            18.dp
                        )
                ) {

                    Row(

                        modifier =
                            Modifier.padding(
                                16.dp
                            ),

                        horizontalArrangement =
                            Arrangement.spacedBy(
                                10.dp
                            )
                    ) {

                        Box(

                            modifier =
                                Modifier
                                    .size(
                                        28.dp
                                    )
                                    .background(
                                        VerdeSuave,
                                        CircleShape
                                    ),

                            contentAlignment =
                                Alignment.Center
                        ) {

                            Text(
                                text =
                                    "✓",

                                color =
                                    VerdePrincipal,

                                fontWeight =
                                    FontWeight.Bold
                            )
                        }


                        Text(

                            text =
                                parrafo,

                            modifier =
                                Modifier.weight(
                                    1f
                                ),

                            fontSize =
                                14.sp,

                            lineHeight =
                                21.sp,

                            color =
                                Color(0xFF334155)
                        )
                    }
                }
            }
        }


        tema.ejemplo?.let {
                ejemplo ->

            Card(

                colors =
                    CardDefaults.cardColors(
                        containerColor =
                            AzulSuave
                    ),

                shape =
                    RoundedCornerShape(
                        20.dp
                    )
            ) {

                Column(

                    modifier =
                        Modifier.padding(
                            16.dp
                        ),

                    verticalArrangement =
                        Arrangement.spacedBy(
                            7.dp
                        )
                ) {

                    Text(

                        text =
                            "💡 Ejemplo",

                        fontWeight =
                            FontWeight.Bold,

                        color =
                            AzulTitulo
                    )


                    Text(

                        text =
                            ejemplo,

                        fontSize =
                            13.sp,

                        lineHeight =
                            20.sp,

                        color =
                            Color(0xFF334155)
                    )
                }
            }
        }


        tema.datoClave?.let {
                dato ->

            Card(

                colors =
                    CardDefaults.cardColors(
                        containerColor =
                            VerdeSuave
                    ),

                shape =
                    RoundedCornerShape(
                        20.dp
                    )
            ) {

                Row(

                    modifier =
                        Modifier.padding(
                            16.dp
                        ),

                    horizontalArrangement =
                        Arrangement.spacedBy(
                            10.dp
                        )
                ) {

                    Text(
                        text =
                            "📌",

                        fontSize =
                            20.sp
                    )


                    Column(

                        modifier =
                            Modifier.weight(
                                1f
                            )
                    ) {

                        Text(

                            text =
                                "Quédate con esto",

                            fontWeight =
                                FontWeight.Bold,

                            color =
                                VerdePrincipal
                        )


                        Spacer(
                            modifier =
                                Modifier.height(
                                    4.dp
                                )
                        )


                        Text(

                            text =
                                dato,

                            fontSize =
                                13.sp,

                            lineHeight =
                                19.sp,

                            color =
                                Color(0xFF334155)
                        )
                    }
                }
            }
        }


        // ====================================================================
        // COMPLETAR TEMA
        // ====================================================================

        Card(

            colors =
                CardDefaults.cardColors(
                    containerColor =
                        if (
                            completado
                        ) {
                            Color(0xFFE9F8EF)
                        } else {
                            Color.White
                        }
                ),

            shape =
                RoundedCornerShape(
                    20.dp
                )
        ) {

            Column(

                modifier =
                    Modifier.padding(
                        16.dp
                    ),

                verticalArrangement =
                    Arrangement.spacedBy(
                        8.dp
                    )
            ) {

                Text(

                    text =
                        if (
                            completado
                        ) {
                            "✅ Tema aprendido"
                        } else {
                            "¿Terminaste de revisar el tema?"
                        },

                    fontWeight =
                        FontWeight.Bold,

                    color =
                        AzulTitulo
                )


                Text(

                    text =
                        if (
                            completado
                        ) {
                            "Ya recibiste los 10 XP de este tema. Puedes repasarlo cuando quieras."
                        } else {
                            "Marca el tema como aprendido y gana 10 XP."
                        },

                    fontSize =
                        12.sp,

                    color =
                        TextoSecundario
                )


                if (
                    !completado
                ) {

                    Button(

                        onClick =
                            onCompletar,

                        modifier =
                            Modifier.fillMaxWidth(),

                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor =
                                    VerdePrincipal
                            )
                    ) {

                        Text(
                            text =
                                "Completar tema · +10 XP"
                        )
                    }
                }
            }
        }


        Card(

            colors =
                CardDefaults.cardColors(
                    containerColor =
                        AmarilloSuave
                ),

            shape =
                RoundedCornerShape(
                    18.dp
                )
        ) {

            Text(

                text =
                    "Recuerda: esta información es educativa. Si tienes síntomas, dudas sobre resultados o un diagnóstico previo, consulta a un profesional de salud.",

                modifier =
                    Modifier.padding(
                        16.dp
                    ),

                fontSize =
                    12.sp,

                lineHeight =
                    18.sp,

                color =
                    Color(0xFF8A5A00)
            )
        }
    }
}


// ============================================================================
// VISUAL: GLUCOSA + INSULINA
// ============================================================================

@Composable
private fun FlujoGlucosaVisual() {

    Card(

        colors =
            CardDefaults.cardColors(
                containerColor =
                    Color.White
            ),

        shape =
            RoundedCornerShape(
                22.dp
            )
    ) {

        Column(

            modifier =
                Modifier.padding(
                    16.dp
                ),

            verticalArrangement =
                Arrangement.spacedBy(
                    12.dp
                )
        ) {

            Text(

                text =
                    "¿Cómo funciona normalmente?",

                fontWeight =
                    FontWeight.Bold,

                color =
                    AzulTitulo,

                fontSize =
                    17.sp
            )


            Row(

                modifier =
                    Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.SpaceBetween,

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                PasoVisual(
                    emoji = "🍞",
                    texto = "Alimento"
                )

                Text(
                    text =
                        "→",
                    color =
                        VerdePrincipal
                )

                PasoVisual(
                    emoji = "🩸",
                    texto = "Glucosa"
                )

                Text(
                    text =
                        "→",
                    color =
                        VerdePrincipal
                )

                PasoVisual(
                    emoji = "🔑",
                    texto = "Insulina"
                )

                Text(
                    text =
                        "→",
                    color =
                        VerdePrincipal
                )

                PasoVisual(
                    emoji = "⚡",
                    texto = "Energía"
                )
            }
        }
    }
}


// ============================================================================
// VISUAL: TIPOS DE DIABETES
// ============================================================================

@Composable
private fun ComparacionTiposVisual() {

    Column(

        verticalArrangement =
            Arrangement.spacedBy(
                10.dp
            )
    ) {

        Text(

            text =
                "Comparación rápida",

            fontWeight =
                FontWeight.Bold,

            color =
                AzulTitulo,

            fontSize =
                17.sp
        )


        Row(

            modifier =
                Modifier.fillMaxWidth(),

            horizontalArrangement =
                Arrangement.spacedBy(
                    10.dp
                )
        ) {

            TipoDiabetesCard(

                titulo =
                    "Tipo 1",

                emoji =
                    "💉",

                punto1 =
                    "Muy poca o ninguna insulina",

                punto2 =
                    "Necesita insulina para vivir",

                modifier =
                    Modifier.weight(
                        1f
                    )
            )


            TipoDiabetesCard(

                titulo =
                    "Tipo 2",

                emoji =
                    "🧬",

                punto1 =
                    "El cuerpo usa peor la insulina",

                punto2 =
                    "Puede prevenirse o retrasarse en muchos casos",

                modifier =
                    Modifier.weight(
                        1f
                    )
            )
        }
    }
}


// ============================================================================
// VISUAL: RESISTENCIA → PREDIABETES → TIPO 2
// ============================================================================

@Composable
private fun CaminoMetabolicoVisual() {

    Card(

        colors =
            CardDefaults.cardColors(
                containerColor =
                    Color.White
            ),

        shape =
            RoundedCornerShape(
                22.dp
            )
    ) {

        Column(

            modifier =
                Modifier.padding(
                    16.dp
                ),

            verticalArrangement =
                Arrangement.spacedBy(
                    10.dp
                )
        ) {

            Text(

                text =
                    "No es una escalera obligatoria",

                fontWeight =
                    FontWeight.Bold,

                color =
                    AzulTitulo,

                fontSize =
                    17.sp
            )


            Text(

                text =
                    "Puede existir una progresión, pero no todas las personas avanzan de la misma forma.",

                fontSize =
                    12.sp,

                color =
                    TextoSecundario
            )


            CaminoPaso(
                numero = "1",
                titulo = "Resistencia a la insulina",
                texto = "Las células responden peor a la insulina."
            )

            CaminoPaso(
                numero = "2",
                titulo = "Prediabetes",
                texto = "La glucosa sube, pero aún no cumple criterios de diabetes."
            )

            CaminoPaso(
                numero = "3",
                titulo = "Diabetes tipo 2",
                texto = "Puede aparecer si el problema metabólico progresa."
            )
        }
    }
}


// ============================================================================
// VISUAL: MITOS
// ============================================================================

@Composable
private fun MitosVisual(

    tema:
        TemaDiabetes

) {

    Column(

        verticalArrangement =
            Arrangement.spacedBy(
                10.dp
            )
    ) {

        tema.contenido.forEach {
                mito ->

            Card(

                colors =
                    CardDefaults.cardColors(
                        containerColor =
                            Color.White
                    ),

                shape =
                    RoundedCornerShape(
                        20.dp
                    )
            ) {

                Column(

                    modifier =
                        Modifier.padding(
                            16.dp
                        ),

                    verticalArrangement =
                        Arrangement.spacedBy(
                            7.dp
                        )
                ) {

                    Text(

                        text =
                            "❌ MITO",

                        color =
                            Color(0xFFB91C1C),

                        fontWeight =
                            FontWeight.Bold,

                        fontSize =
                            12.sp
                    )


                    val partes =
                        mito.split(
                            "».",
                            limit = 2
                        )


                    Text(

                        text =
                            if (
                                partes.size >
                                1
                            ) {
                                partes[0] + "»"
                            } else {
                                mito
                            },

                        fontWeight =
                            FontWeight.Bold,

                        color =
                            AzulTitulo,

                        fontSize =
                            14.sp
                    )


                    if (
                        partes.size >
                        1
                    ) {

                        Text(

                            text =
                                "✅ REALIDAD",

                            color =
                                VerdePrincipal,

                            fontWeight =
                                FontWeight.Bold,

                            fontSize =
                                12.sp
                        )


                        Text(

                            text =
                                partes[1]
                                    .trim(),

                            color =
                                Color(0xFF334155),

                            fontSize =
                                13.sp,

                            lineHeight =
                                19.sp
                        )
                    }
                }
            }
        }
    }
}


// ============================================================================
// COMPONENTES VISUALES AUXILIARES
// ============================================================================

@Composable
private fun PasoVisual(

    emoji:
        String,

    texto:
        String

) {

    Column(

        horizontalAlignment =
            Alignment.CenterHorizontally,

        verticalArrangement =
            Arrangement.spacedBy(
                3.dp
            )
    ) {

        Box(

            modifier =
                Modifier
                    .size(
                        42.dp
                    )
                    .background(
                        VerdeSuave,
                        CircleShape
                    ),

            contentAlignment =
                Alignment.Center
        ) {

            Text(
                text =
                    emoji,

                fontSize =
                    20.sp
            )
        }


        Text(

            text =
                texto,

            fontSize =
                10.sp,

            color =
                TextoSecundario,

            textAlign =
                TextAlign.Center
        )
    }
}


@Composable
private fun TipoDiabetesCard(

    titulo:
        String,

    emoji:
        String,

    punto1:
        String,

    punto2:
        String,

    modifier:
        Modifier = Modifier

) {

    Card(

        modifier =
            modifier,

        colors =
            CardDefaults.cardColors(
                containerColor =
                    Color.White
            ),

        shape =
            RoundedCornerShape(
                20.dp
            )
    ) {

        Column(

            modifier =
                Modifier.padding(
                    14.dp
                ),

            verticalArrangement =
                Arrangement.spacedBy(
                    7.dp
                )
        ) {

            Text(
                text =
                    emoji,

                fontSize =
                    26.sp
            )


            Text(

                text =
                    titulo,

                fontWeight =
                    FontWeight.Bold,

                color =
                    AzulTitulo,

                fontSize =
                    17.sp
            )


            Text(

                text =
                    "• $punto1",

                fontSize =
                    11.sp,

                lineHeight =
                    16.sp,

                color =
                    Color(0xFF334155)
            )


            Text(

                text =
                    "• $punto2",

                fontSize =
                    11.sp,

                lineHeight =
                    16.sp,

                color =
                    Color(0xFF334155)
            )
        }
    }
}


@Composable
private fun CaminoPaso(

    numero:
        String,

    titulo:
        String,

    texto:
        String

) {

    Row(

        verticalAlignment =
            Alignment.CenterVertically,

        horizontalArrangement =
            Arrangement.spacedBy(
                10.dp
            )
    ) {

        Box(

            modifier =
                Modifier
                    .size(
                        36.dp
                    )
                    .background(
                        VerdeSuave,
                        CircleShape
                    ),

            contentAlignment =
                Alignment.Center
        ) {

            Text(

                text =
                    numero,

                color =
                    VerdePrincipal,

                fontWeight =
                    FontWeight.Bold
            )
        }


        Column(

            modifier =
                Modifier.weight(
                    1f
                )
        ) {

            Text(

                text =
                    titulo,

                fontWeight =
                    FontWeight.Bold,

                color =
                    AzulTitulo,

                fontSize =
                    13.sp
            )


            Text(

                text =
                    texto,

                fontSize =
                    11.sp,

                lineHeight =
                    16.sp,

                color =
                    TextoSecundario
            )
        }
    }
}




// ============================================================================
// BOTÓN REUTILIZABLE DE TEXT TO SPEECH
// ============================================================================

@Composable
private fun BotonEscuchar(

    textoBoton:
        String,

    habilitado:
        Boolean,

    onClick:
        () -> Unit

) {

    OutlinedButton(

        onClick =
            onClick,

        enabled =
            habilitado,

        modifier =
            Modifier.fillMaxWidth()
    ) {

        Text(

            text =
                if (
                    habilitado
                ) {
                    "🔊 $textoBoton"
                } else {
                    "🔊 Preparando voz..."
                },

            color =
                if (
                    habilitado
                ) {
                    VerdePrincipal
                } else {
                    TextoSecundario
                }
        )
    }
}


// ============================================================================
// QUIZ
// ============================================================================

@Composable
private fun DiabetesQuiz(

    progreso:
        ProgresoDiabetes,

    ttsListo:
        Boolean,

    onSpeak:
        (String) -> Unit,

    onBack:
        () -> Unit,

    onFinish:
        (Int, Set<String>) -> Unit

) {

    // ========================================================================
    // GENERAMOS UNA SESIÓN NUEVA CADA VEZ QUE SE ABRE EL QUIZ
    // ========================================================================

    val preguntasSesion =
        remember {

            crearPreguntasSesion(
                progreso
            )
        }


    val dificultad =
        remember {

            dificultadActual(
                progreso
            )
        }


    var indiceActual by remember {
        mutableIntStateOf(
            0
        )
    }


    var seleccion by remember {
        mutableIntStateOf(
            -1
        )
    }


    var puntaje by remember {
        mutableIntStateOf(
            0
        )
    }


    var mostrarExplicacion by remember {
        mutableStateOf(
            false
        )
    }


    val temasFallados =
        remember {

            mutableSetOf<String>()
        }


    val pregunta =
        preguntasSesion[
            indiceActual
        ]


    Column(

        modifier =
            Modifier
                .fillMaxSize()
                .background(
                    FondoDiabetes
                )
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(
                    20.dp
                ),

        verticalArrangement =
            Arrangement.spacedBy(
                14.dp
            )
    ) {


        TextButton(
            onClick =
                onBack
        ) {

            Text(
                text =
                    "← Salir de la evaluación",

                color =
                    VerdePrincipal
            )
        }


        Row(

            verticalAlignment =
                Alignment.CenterVertically,

            horizontalArrangement =
                Arrangement.spacedBy(
                    10.dp
                )
        ) {

            Image(

                painter =
                    painterResource(
                        id =
                            R.drawable.nuby_pensando
                    ),

                contentDescription =
                    "Nuby acompañando la evaluación",

                modifier =
                    Modifier.size(
                        70.dp
                    ),

                contentScale =
                    ContentScale.Fit
            )


            Column {

                Text(

                    text =
                        "¿Qué aprendí sobre diabetes?",

                    fontSize =
                        21.sp,

                    fontWeight =
                        FontWeight.Bold,

                    color =
                        AzulTitulo
                )


                Text(

                    text =
                        "Pregunta ${indiceActual + 1} de ${preguntasSesion.size}",

                    fontSize =
                        12.sp,

                    color =
                        TextoSecundario
                )


                Text(

                    text =
                        "Nivel: ${nombreDificultad(dificultad)}",

                    fontSize =
                        12.sp,

                    color =
                        VerdePrincipal,

                    fontWeight =
                        FontWeight.Bold
                )
            }
        }


        // ====================================================================
        // AVISO DE ADAPTACIÓN
        // ====================================================================

        if (
            progreso.temasAReforzar
                .isNotEmpty()
        ) {

            Card(

                colors =
                    CardDefaults.cardColors(
                        containerColor =
                            AzulSuave
                    ),

                shape =
                    RoundedCornerShape(
                        18.dp
                    )
            ) {

                Text(

                    text =
                        "🎯 Nuby incluyó preguntas de refuerzo de tus intentos anteriores.",

                    modifier =
                        Modifier.padding(
                            12.dp
                        ),

                    fontSize =
                        12.sp,

                    lineHeight =
                        17.sp,

                    color =
                        AzulTitulo
                )
            }
        }


        LinearProgressIndicator(

            progress = {
                (
                    indiceActual + 1
                ).toFloat() /
                    preguntasSesion.size.toFloat()
            },

            modifier =
                Modifier.fillMaxWidth(),

            color =
                VerdePrincipal,

            trackColor =
                Color(0xFFDDE7BF)
        )


        BotonEscuchar(

            textoBoton =
                "Escuchar pregunta y respuestas",

            habilitado =
                ttsListo,

            onClick = {

                val opcionesTexto =
                    pregunta.opciones
                        .mapIndexed {
                                index,
                                opcion ->

                            "Opción ${index + 1}. $opcion"
                        }
                        .joinToString(
                            ". "
                        )


                onSpeak(
                    "${pregunta.pregunta}. $opcionesTexto"
                )
            }
        )


        Card(

            colors =
                CardDefaults.cardColors(
                    containerColor =
                        Color.White
                ),

            shape =
                RoundedCornerShape(
                    24.dp
                )
        ) {

            Column(

                modifier =
                    Modifier.padding(
                        18.dp
                    ),

                verticalArrangement =
                    Arrangement.spacedBy(
                        12.dp
                    )
            ) {

                Text(

                    text =
                        pregunta.pregunta,

                    fontSize =
                        18.sp,

                    fontWeight =
                        FontWeight.Bold,

                    color =
                        AzulTitulo,

                    lineHeight =
                        24.sp
                )


                Text(

                    text =
                        pregunta.tema,

                    fontSize =
                        11.sp,

                    color =
                        VerdePrincipal,

                    fontWeight =
                        FontWeight.SemiBold
                )


                pregunta.opciones.forEachIndexed {
                        index,
                        opcion ->

                    Row(

                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .clickable(
                                    enabled =
                                        !mostrarExplicacion
                                ) {

                                    seleccion =
                                        index
                                }
                                .padding(
                                    vertical =
                                        4.dp
                                ),

                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        RadioButton(

                            selected =
                                seleccion ==
                                    index,

                            onClick = {

                                if (
                                    !mostrarExplicacion
                                ) {

                                    seleccion =
                                        index
                                }
                            },

                            colors =
                                RadioButtonDefaults.colors(
                                    selectedColor =
                                        VerdePrincipal
                                )
                        )


                        Text(

                            text =
                                opcion,

                            modifier =
                                Modifier.weight(
                                    1f
                                ),

                            fontSize =
                                14.sp,

                            lineHeight =
                                20.sp,

                            color =
                                Color(0xFF334155)
                        )
                    }
                }
            }
        }


        if (
            mostrarExplicacion
        ) {

            val correcta =
                seleccion ==
                    pregunta.respuestaCorrecta


            Card(

                colors =
                    CardDefaults.cardColors(
                        containerColor =
                            if (
                                correcta
                            ) {
                                Color(0xFFE9F8EF)
                            } else {
                                Color(0xFFFFF3E5)
                            }
                    ),

                shape =
                    RoundedCornerShape(
                        18.dp
                    )
            ) {

                Row(

                    modifier =
                        Modifier.padding(
                            16.dp
                        ),

                    verticalAlignment =
                        Alignment.CenterVertically,

                    horizontalArrangement =
                        Arrangement.spacedBy(
                            10.dp
                        )
                ) {

                    Image(

                        painter =
                            painterResource(
                                id =
                                    if (
                                        correcta
                                    ) {
                                        R.drawable.nuby_abrazo
                                    } else {
                                        R.drawable.nuby_pensando
                                    }
                            ),

                        contentDescription =
                            null,

                        modifier =
                            Modifier.size(
                                62.dp
                            ),

                        contentScale =
                            ContentScale.Fit
                    )


                    Column(

                        modifier =
                            Modifier.weight(
                                1f
                            )
                    ) {

                        Text(

                            text =
                                if (
                                    correcta
                                ) {
                                    "¡Correcto!"
                                } else {
                                    "Este tema volverá a aparecer"
                                },

                            fontWeight =
                                FontWeight.Bold,

                            color =
                                if (
                                    correcta
                                ) {
                                    Color(0xFF166534)
                                } else {
                                    Color(0xFF9A5A00)
                                }
                        )


                        Spacer(
                            modifier =
                                Modifier.height(
                                    4.dp
                                )
                        )


                        Text(

                            text =
                                pregunta.explicacion,

                            fontSize =
                                13.sp,

                            lineHeight =
                                19.sp,

                            color =
                                Color(0xFF475569)
                        )
                    }
                }
            }


            BotonEscuchar(

                textoBoton =
                    "Escuchar explicación",

                habilitado =
                    ttsListo,

                onClick = {

                    onSpeak(
                        pregunta.explicacion
                    )
                }
            )
        }


        if (
            !mostrarExplicacion
        ) {

            Button(

                onClick = {

                    if (
                        seleccion ==
                        pregunta.respuestaCorrecta
                    ) {

                        puntaje++

                    } else {

                        temasFallados.add(
                            pregunta.tema
                        )
                    }


                    mostrarExplicacion =
                        true
                },

                enabled =
                    seleccion >= 0,

                modifier =
                    Modifier.fillMaxWidth(),

                colors =
                    ButtonDefaults.buttonColors(
                        containerColor =
                            VerdePrincipal
                    )
            ) {

                Text(
                    text =
                        "Comprobar respuesta"
                )
            }

        } else {

            Button(

                onClick = {

                    if (
                        indiceActual <
                        preguntasSesion.lastIndex
                    ) {

                        indiceActual++

                        seleccion =
                            -1

                        mostrarExplicacion =
                            false

                    } else {

                        onFinish(
                            puntaje,
                            temasFallados.toSet()
                        )
                    }
                },

                modifier =
                    Modifier.fillMaxWidth(),

                colors =
                    ButtonDefaults.buttonColors(
                        containerColor =
                            VerdePrincipal
                    )
            ) {

                Text(

                    text =
                        if (
                            indiceActual ==
                            preguntasSesion.lastIndex
                        ) {
                            "Ver mi resultado"
                        } else {
                            "Siguiente pregunta"
                        }
                )
            }
        }
    }
}


// ============================================================================
// RESULTADO
// ============================================================================

@Composable
private fun DiabetesResultado(

    puntaje:
        Int,

    total:
        Int,

    progreso:
        ProgresoDiabetes,

    xpGanado:
        Int,

    ttsListo:
        Boolean,

    onSpeak:
        (String) -> Unit,

    onRepeat:
        () -> Unit,

    onGoHome:
        () -> Unit

) {

    val porcentaje =
        if (
            total > 0
        ) {
            (
                puntaje * 100
            ) / total
        } else {
            0
        }


    val mensajeNuby =
        when {

            porcentaje >= 90 ->
                "¡Excelente! Dominas muy bien los conceptos principales. Sigue usando lo que aprendiste para cuidar tu salud."

            porcentaje >= 70 ->
                "¡Muy bien! Ya tienes una base sólida. Un pequeño repaso puede ayudarte a reforzar algunos conceptos."

            porcentaje >= 50 ->
                "¡Vas por buen camino! Ya reconoces varios conceptos importantes. Repasa los temas que te costaron."

            else ->
                "Aprender toma tiempo y cada intento cuenta. Repasa con calma y vuelve a intentarlo. Yo te acompaño."
        }


    val insignias =
        insigniasDesbloqueadas(
            progreso
        )


    Column(

        modifier =
            Modifier
                .fillMaxSize()
                .background(
                    FondoDiabetes
                )
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(
                    20.dp
                ),

        horizontalAlignment =
            Alignment.CenterHorizontally,

        verticalArrangement =
            Arrangement.spacedBy(
                16.dp
            )
    ) {


        Image(

            painter =
                painterResource(
                    id =
                        R.drawable.nuby_abrazo
                ),

            contentDescription =
                "Nuby feliz",

            modifier =
                Modifier.size(
                    145.dp
                ),

            contentScale =
                ContentScale.Fit
        )


        Text(

            text =
                "¡Evaluación terminada!",

            fontSize =
                27.sp,

            fontWeight =
                FontWeight.Bold,

            color =
                AzulTitulo,

            textAlign =
                TextAlign.Center
        )


        Card(

            modifier =
                Modifier.fillMaxWidth(),

            colors =
                CardDefaults.cardColors(
                    containerColor =
                        Color.White
                ),

            shape =
                RoundedCornerShape(
                    24.dp
                )
        ) {

            Column(

                modifier =
                    Modifier.padding(
                        20.dp
                    ),

                horizontalAlignment =
                    Alignment.CenterHorizontally,

                verticalArrangement =
                    Arrangement.spacedBy(
                        8.dp
                    )
            ) {

                Text(

                    text =
                        "$puntaje / $total",

                    fontSize =
                        38.sp,

                    fontWeight =
                        FontWeight.Bold,

                    color =
                        VerdePrincipal
                )


                Text(

                    text =
                        "$porcentaje% de respuestas correctas",

                    color =
                        TextoSecundario,

                    fontSize =
                        13.sp
                )


                Text(

                    text =
                        "+$xpGanado XP",

                    color =
                        Color(0xFFB7791F),

                    fontWeight =
                        FontWeight.Bold,

                    fontSize =
                        18.sp
                )


                Text(

                    text =
                        "Nivel: ${nombreNivel(progreso.xp)}",

                    fontSize =
                        17.sp,

                    fontWeight =
                        FontWeight.Bold,

                    color =
                        AzulTitulo
                )


                LinearProgressIndicator(

                    progress = {
                        progresoDelNivel(
                            progreso.xp
                        )
                    },

                    modifier =
                        Modifier.fillMaxWidth(),

                    color =
                        VerdePrincipal,

                    trackColor =
                        Color(0xFFDCE8F2)
                )


                Text(

                    text =
                        "${progreso.xp} XP acumulados · Mejor resultado ${progreso.mejorPuntaje}/10",

                    color =
                        TextoSecundario,

                    fontSize =
                        11.sp
                )
            }
        }


        if (
            progreso.temasAReforzar
                .isNotEmpty()
        ) {

            Card(

                modifier =
                    Modifier.fillMaxWidth(),

                colors =
                    CardDefaults.cardColors(
                        containerColor =
                            AmarilloSuave
                    ),

                shape =
                    RoundedCornerShape(
                        22.dp
                    )
            ) {

                Column(

                    modifier =
                        Modifier.padding(
                            16.dp
                        ),

                    verticalArrangement =
                        Arrangement.spacedBy(
                            7.dp
                        )
                ) {

                    Text(

                        text =
                            "🎯 Temas para reforzar",

                        fontWeight =
                            FontWeight.Bold,

                        color =
                            AzulTitulo
                    )


                    Text(

                        text =
                            "En tu próximo intento Nuby dará prioridad a estos temas:",

                        fontSize =
                            12.sp,

                        color =
                            TextoSecundario
                    )


                    progreso
                        .temasAReforzar
                        .forEach {
                                tema ->

                            Text(

                                text =
                                    "• $tema",

                                fontSize =
                                    12.sp,

                                color =
                                    Color(0xFF334155)
                            )
                        }
                }
            }
        }


        if (
            insignias.isNotEmpty()
        ) {

            Card(

                modifier =
                    Modifier.fillMaxWidth(),

                colors =
                    CardDefaults.cardColors(
                        containerColor =
                            AzulSuave
                    ),

                shape =
                    RoundedCornerShape(
                        22.dp
                    )
            ) {

                Column(

                    modifier =
                        Modifier.padding(
                            16.dp
                        ),

                    verticalArrangement =
                        Arrangement.spacedBy(
                            7.dp
                        )
                ) {

                    Text(

                        text =
                            "Insignias desbloqueadas",

                        fontWeight =
                            FontWeight.Bold,

                        color =
                            AzulTitulo
                    )


                    insignias.forEach {
                            insignia ->

                        Text(

                            text =
                                insignia,

                            color =
                                Color(0xFF334155),

                            fontSize =
                                13.sp
                        )
                    }
                }
            }
        }


        Card(

            modifier =
                Modifier.fillMaxWidth(),

            colors =
                CardDefaults.cardColors(
                    containerColor =
                        VerdeSuave
                ),

            shape =
                RoundedCornerShape(
                    22.dp
                )
        ) {

            Column(

                modifier =
                    Modifier.padding(
                        18.dp
                    ),

                verticalArrangement =
                    Arrangement.spacedBy(
                        8.dp
                    )
            ) {

                Text(

                    text =
                        "Nuby dice:",

                    fontWeight =
                        FontWeight.Bold,

                    color =
                        VerdePrincipal
                )


                Text(

                    text =
                        mensajeNuby,

                    fontSize =
                        14.sp,

                    lineHeight =
                        21.sp,

                    color =
                        Color(0xFF334155)
                )


                BotonEscuchar(

                    textoBoton =
                        "Escuchar a Nuby",

                    habilitado =
                        ttsListo,

                    onClick = {

                        onSpeak(
                            "Obtuviste $puntaje de $total respuestas correctas. Ganaste $xpGanado puntos de experiencia. Tu nivel actual es ${nombreNivel(progreso.xp)}. $mensajeNuby"
                        )
                    }
                )
            }
        }


        Button(

            onClick =
                onRepeat,

            modifier =
                Modifier.fillMaxWidth(),

            colors =
                ButtonDefaults.buttonColors(
                    containerColor =
                        VerdePrincipal
                )
        ) {

            Text(
                text =
                    if (
                        progreso.temasAReforzar.isNotEmpty()
                    ) {
                        "Practicar con refuerzo"
                    } else {
                        "Generar nuevo reto"
                    }
            )
        }


        OutlinedButton(

            onClick =
                onGoHome,

            modifier =
                Modifier.fillMaxWidth()
        ) {

            Text(

                text =
                    "Volver al módulo",

                color =
                    VerdePrincipal
            )
        }


        Text(

            text =
                "Esta evaluación mide aprendizaje, no riesgo ni diagnóstico de diabetes.",

            fontSize =
                11.sp,

            color =
                Color(0xFF94A3B8),

            textAlign =
                TextAlign.Center
        )
    }
}
