package com.example.vibralavida.trastornos_ritmo
import com.example.vibralavida.backgroundGradient
import com.example.vibralavida.api.HealthConnectRepository
import com.example.vibralavida.api.RitmoProgresoRepository
import com.example.vibralavida.api.modelos.HealthConnectSyncRequest
import com.example.vibralavida.api.modelos.RitmoIntentoRequest
import com.example.vibralavida.api.modelos.RitmoProgresoRequest

// ============================================================================
// ANDROID
// ============================================================================

import android.content.Context
import android.speech.tts.TextToSpeech

// ============================================================================
// ACTIVITY RESULT / HEALTH CONNECT
// ============================================================================

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.PermissionController

// ============================================================================
// COMPOSE - FOUNDATION
// ============================================================================

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll

// ============================================================================
// MATERIAL 3
// ============================================================================

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text

// ============================================================================
// COMPOSE - ESTADOS
// ============================================================================

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue

// ============================================================================
// UI
// ============================================================================

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ============================================================================
// CORRUTINAS
// ============================================================================

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// ============================================================================
// FECHA Y HORA
// ============================================================================

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import com.example.vibralavida.R


// ============================================================================
// ESTADOS DE SEMAFORIZACIÓN
// ============================================================================

/**
 * Estados visuales que utilizaremos para la semaforización.
 */
enum class HealthTrafficLight {

    // Estado favorable.
    GOOD,

    // Estado intermedio.
    REGULAR,

    // Estado que requiere atención.
    LOW
}



// ============================================================================
// FASE 1 - CONTENIDO EDUCATIVO DE TRASTORNOS DEL RITMO
// ============================================================================

private data class RhythmLearningTopic(
    val emoji: String,
    val title: String,
    val subtitle: String,
    val paragraphs: List<String>,
    val example: String,
    val keyMessage: String,
    val nubyRes: Int
)

private val rhythmLearningTopics = listOf(
    RhythmLearningTopic(
        "🫀",
        "Cómo late el corazón",
        "Conoce qué significa ritmo cardíaco.",
        listOf(
            "El corazón late gracias a señales eléctricas que coordinan sus contracciones.",
            "La frecuencia cardíaca indica cuántos latidos ocurren por minuto, mientras que el ritmo describe cómo se organizan esos latidos.",
            "La frecuencia puede cambiar con ejercicio, emociones, sueño, fiebre, hidratación y otras situaciones."
        ),
        "Es normal que el corazón lata más rápido durante ejercicio y que después disminuya gradualmente al descansar.",
        "Una frecuencia distinta no significa automáticamente que exista una arritmia.",
        R.drawable.nuby_idea
    ),
    RhythmLearningTopic(
        "💓",
        "Palpitaciones y arritmias",
        "No todas las palpitaciones significan lo mismo.",
        listOf(
            "Las palpitaciones son la sensación de notar los latidos rápidos, fuertes, irregulares o como si hubiera un salto.",
            "Una arritmia es una alteración del ritmo cardíaco. Algunas son benignas y otras requieren evaluación médica.",
            "Para saber qué ocurre se necesita contexto clínico y, en muchos casos, un electrocardiograma."
        ),
        "Sentir el corazón acelerado después de correr puede tener una explicación normal; episodios repetidos sin causa clara merecen comentarse con un profesional.",
        "La sensación de palpitaciones por sí sola no permite saber qué ritmo está ocurriendo.",
        R.drawable.nuby_esteto
    ),
    RhythmLearningTopic(
        "⚡",
        "Factores que pueden influir",
        "El ritmo puede cambiar por muchos motivos.",
        listOf(
            "Estrés, falta de sueño, deshidratación, fiebre y ejercicio pueden modificar temporalmente la frecuencia cardíaca.",
            "Cafeína, bebidas energéticas, nicotina y algunos estimulantes también pueden aumentar palpitaciones en algunas personas.",
            "Conviene observar patrones y evitar conclusiones basadas en una sola medición."
        ),
        "Si notas palpitaciones después de bebidas energéticas, registra el momento y coméntalo con un profesional si se repite.",
        "Busca patrones: cuándo ocurre, cuánto dura, qué hacías y qué síntomas acompañan.",
        R.drawable.nuby_pensando
    ),
    RhythmLearningTopic(
        "⌚",
        "Qué puede decir tu reloj",
        "Úsalo como apoyo, no como diagnóstico.",
        listOf(
            "Los wearables pueden registrar frecuencia cardíaca y ayudar a observar tendencias.",
            "El movimiento, un ajuste inadecuado o una mala lectura del sensor pueden producir datos poco confiables.",
            "Una medición inusual puede repetirse en reposo y, si persiste o hay síntomas, debe valorarse."
        ),
        "Si el reloj marca un valor extraño mientras te mueves, revisa la colocación y repite en reposo si no hay síntomas de alarma.",
        "Un reloj puede orientar y registrar tendencias, pero no confirma por sí solo una arritmia.",
        R.drawable.nuby_idea
    ),
    RhythmLearningTopic(
        "🚨",
        "Señales de alarma",
        "Reconoce cuándo no conviene esperar.",
        listOf(
            "Palpitaciones acompañadas de desmayo, dolor de pecho, dificultad importante para respirar o deterioro rápido requieren atención médica urgente.",
            "También merece valoración un episodio persistente de latidos muy rápidos o irregulares acompañado de mareo intenso o debilidad marcada.",
            "En una emergencia, la prioridad es pedir ayuda y no intentar diagnosticar el ritmo desde la aplicación."
        ),
        "Si una persona presenta palpitaciones y se desmaya, no debe esperar a que la app muestre otra medición para pedir ayuda.",
        "Los síntomas y el estado general importan más que un número aislado del reloj.",
        R.drawable.nuby_esteto
    ),
    RhythmLearningTopic(
        "📝",
        "Cómo registrar un episodio",
        "Un buen registro puede ayudar en la consulta.",
        listOf(
            "Anota la hora aproximada, duración y qué estabas haciendo cuando comenzó.",
            "Registra síntomas como mareo, dolor de pecho, falta de aire o desmayo.",
            "Si el wearable guardó una medición, conserva el dato para mostrarlo al profesional sin asumir que representa un diagnóstico."
        ),
        "Un registro como '7:20 pm, 5 minutos, sentado, mareo y frecuencia elevada' aporta más contexto que guardar solo un número.",
        "Contexto + síntomas + duración hacen el registro más útil.",
        R.drawable.nuby_idea
    )
)


// ============================================================================
// FASE 2 - GAMIFICACIÓN LOCAL
// ============================================================================

private data class RhythmLearningProgress(
    val xp: Int = 0,
    val completedTopics: Set<String> = emptySet(),
    val attempts: Int = 0,
    val bestScore: Int = 0,
    val topicsToReinforce: Set<String> = emptySet()
)

private object RhythmProgressLocal {
    private const val PREFS = "rhythm_learning_progress"
    private const val KEY_XP = "xp"
    private const val KEY_TOPICS = "topics"
    private const val KEY_ATTEMPTS = "attempts"
    private const val KEY_BEST = "best"
    private const val KEY_REINFORCE = "reinforce"

    fun load(context: Context): RhythmLearningProgress {
        val preferences =
            context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

        return RhythmLearningProgress(
            xp = preferences.getInt(KEY_XP, 0),
            completedTopics =
                preferences.getStringSet(KEY_TOPICS, emptySet())?.toSet()
                    ?: emptySet(),
            attempts = preferences.getInt(KEY_ATTEMPTS, 0),
            bestScore = preferences.getInt(KEY_BEST, 0),
            topicsToReinforce =
                preferences.getStringSet(KEY_REINFORCE, emptySet())?.toSet()
                    ?: emptySet()
        )
    }

    fun save(
        context: Context,
        progress: RhythmLearningProgress
    ) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit()
            .putInt(KEY_XP, progress.xp)
            .putStringSet(KEY_TOPICS, progress.completedTopics)
            .putInt(KEY_ATTEMPTS, progress.attempts)
            .putInt(KEY_BEST, progress.bestScore)
            .putStringSet(KEY_REINFORCE, progress.topicsToReinforce)
            .apply()
    }
}

private fun rhythmLevel(xp: Int): String =
    when {
        xp >= 110 -> "Guardián del ritmo"
        xp >= 80 -> "Explorador cardíaco"
        xp >= 50 -> "Observador del pulso"
        xp >= 20 -> "Aprendiz del corazón"
        else -> "Primer latido"
    }

private fun rhythmLevelProgress(xp: Int): Float {
    val range =
        when {
            xp >= 110 -> 110 to 140
            xp >= 80 -> 80 to 110
            xp >= 50 -> 50 to 80
            xp >= 20 -> 20 to 50
            else -> 0 to 20
        }

    return ((xp - range.first).toFloat() /
        (range.second - range.first).toFloat()).coerceIn(0f, 1f)
}

private fun rhythmXpToNext(xp: Int): Int =
    when {
        xp >= 110 -> 140 - xp
        xp >= 80 -> 110 - xp
        xp >= 50 -> 80 - xp
        xp >= 20 -> 50 - xp
        else -> 20 - xp
    }.coerceAtLeast(0)

private fun rhythmBadges(progress: RhythmLearningProgress): List<String> {
    val result = mutableListOf<String>()

    if (progress.completedTopics.isNotEmpty()) {
        result.add("💓 Primer latido")
    }
    if (progress.completedTopics.contains("Señales de alarma")) {
        result.add("🚨 Alerta informada")
    }
    if (progress.completedTopics.contains("Qué puede decir tu reloj")) {
        result.add("⌚ Observador responsable")
    }
    if (progress.bestScore >= 8) {
        result.add("🧠 Ritmo consciente")
    }
    if (progress.attempts >= 3) {
        result.add("⭐ Constancia Nuby")
    }
    if (
        progress.completedTopics.size >= rhythmLearningTopics.size &&
        progress.bestScore >= 8
    ) {
        result.add("🏆 Guardián del ritmo")
    }

    return result
}

private enum class RhythmDifficulty(
    val label: String
) {
    BASIC("Básico"),
    INTERMEDIATE("Intermedio"),
    ADVANCED("Reto Nuby")
}

private fun rhythmDifficultyFor(
    progress: RhythmLearningProgress
): RhythmDifficulty =
    when {
        progress.bestScore >= 8 -> RhythmDifficulty.ADVANCED
        progress.bestScore >= 5 -> RhythmDifficulty.INTERMEDIATE
        else -> RhythmDifficulty.BASIC
    }

private data class RhythmQuizQuestion(
    val topic: String,
    val difficulty: RhythmDifficulty,
    val question: String,
    val options: List<String>,
    val correctAnswer: Int,
    val explanation: String
)

private data class PreparedRhythmQuestion(
    val topic: String,
    val difficulty: RhythmDifficulty,
    val question: String,
    val options: List<String>,
    val correctAnswer: Int,
    val explanation: String
)

private data class RhythmQuizResultData(
    val score: Int,
    val total: Int,
    val difficulty: String,
    val failedTopics: Set<String>
)

private fun RhythmQuizQuestion.prepare(): PreparedRhythmQuestion {
    val correctText = options[correctAnswer]
    val mixed = options.shuffled()

    return PreparedRhythmQuestion(
        topic = topic,
        difficulty = difficulty,
        question = question,
        options = mixed,
        correctAnswer = mixed.indexOf(correctText),
        explanation = explanation
    )
}

private fun buildRhythmQuiz(
    progress: RhythmLearningProgress
): List<PreparedRhythmQuestion> {
    val difficulty = rhythmDifficultyFor(progress)

    val pool =
        rhythmQuestionBank.filter {
            it.difficulty == difficulty
        }

    val reinforced =
        pool.filter {
            progress.topicsToReinforce.contains(it.topic)
        }
            .shuffled()
            .take(4)

    val remaining =
        pool.filterNot {
            reinforced.contains(it)
        }
            .shuffled()
            .take(10 - reinforced.size)

    return (reinforced + remaining)
        .shuffled()
        .map { it.prepare() }
}

private val rhythmQuestionBank = listOf(

    // -------------------- BÁSICO --------------------

    RhythmQuizQuestion(
        "Cómo late el corazón",
        RhythmDifficulty.BASIC,
        "¿Frecuencia cardíaca y ritmo cardíaco significan exactamente lo mismo?",
        listOf("No", "Sí", "Solo durante ejercicio", "Solo durante sueño"),
        0,
        "La frecuencia indica cuántos latidos ocurren por minuto; el ritmo describe cómo se organizan."
    ),

    RhythmQuizQuestion(
        "Palpitaciones y arritmias",
        RhythmDifficulty.BASIC,
        "¿Una palpitación permite identificar por sí sola una arritmia?",
        listOf("No", "Sí", "Si dura segundos", "Si ocurre sentado"),
        0,
        "La sensación de palpitación no identifica por sí sola el ritmo cardíaco."
    ),

    RhythmQuizQuestion(
        "Factores que pueden influir",
        RhythmDifficulty.BASIC,
        "¿Qué puede modificar temporalmente la frecuencia cardíaca?",
        listOf("Ejercicio, estrés o fiebre", "Solo la edad", "Nada", "Solo dormir"),
        0,
        "Actividad, emociones, fiebre e hidratación pueden modificarla."
    ),

    RhythmQuizQuestion(
        "Qué puede decir tu reloj",
        RhythmDifficulty.BASIC,
        "¿Un smartwatch confirma por sí solo una arritmia?",
        listOf("No", "Sí", "Si mide varias veces", "Si es nuevo"),
        0,
        "El wearable puede mostrar tendencias, pero no sustituye una valoración clínica."
    ),

    RhythmQuizQuestion(
        "Señales de alarma",
        RhythmDifficulty.BASIC,
        "Palpitaciones con desmayo o dolor de pecho requieren:",
        listOf("Atención médica urgente", "Esperar días", "Solo revisar el reloj", "Ejercicio"),
        0,
        "Esos síntomas pueden indicar una situación importante."
    ),

    RhythmQuizQuestion(
        "Cómo registrar un episodio",
        RhythmDifficulty.BASIC,
        "¿Qué hace más útil el registro de un episodio?",
        listOf("Hora, duración, actividad y síntomas", "Solo un número", "Nada", "La batería"),
        0,
        "El contexto, duración y síntomas aportan información útil."
    ),

    RhythmQuizQuestion(
        "Qué puede decir tu reloj",
        RhythmDifficulty.BASIC,
        "¿Qué puede causar una lectura poco confiable del reloj?",
        listOf("Movimiento o mal ajuste", "Respirar normal", "Sentarse", "La pantalla apagada"),
        0,
        "Movimiento o mal contacto pueden afectar el sensor."
    ),

    RhythmQuizQuestion(
        "Qué puede decir tu reloj",
        RhythmDifficulty.BASIC,
        "Si aparece una medición extraña durante movimiento y no hay síntomas de alarma, puedes:",
        listOf("Repetir en reposo", "Asumir un diagnóstico", "Ignorar síntomas graves", "Duplicar ejercicio"),
        0,
        "Repetir en reposo puede ayudar a comprobar si el dato persiste."
    ),

    RhythmQuizQuestion(
        "Factores que pueden influir",
        RhythmDifficulty.BASIC,
        "¿Las bebidas energéticas pueden aumentar palpitaciones en algunas personas?",
        listOf("Sí", "Nunca", "Solo al dormir", "Solo en adultos mayores"),
        0,
        "Los estimulantes pueden aumentar palpitaciones en algunas personas."
    ),

    RhythmQuizQuestion(
        "Señales de alarma",
        RhythmDifficulty.BASIC,
        "Ante síntomas graves, ¿qué es prioritario?",
        listOf("Pedir ayuda", "Esperar otra lectura", "Cumplir pasos", "Reiniciar la app"),
        0,
        "En una posible emergencia, la prioridad es la persona."
    ),

    // -------------------- INTERMEDIO --------------------

    RhythmQuizQuestion(
        "Cómo late el corazón",
        RhythmDifficulty.INTERMEDIATE,
        "Después de correr, la frecuencia sube y luego baja gradualmente. ¿Qué interpretación es más adecuada?",
        listOf("Puede ser una respuesta normal al esfuerzo", "Siempre es una arritmia", "El reloj está dañado", "Debe ignorarse cualquier síntoma"),
        0,
        "La frecuencia normalmente aumenta con el ejercicio y puede bajar durante la recuperación."
    ),

    RhythmQuizQuestion(
        "Palpitaciones y arritmias",
        RhythmDifficulty.INTERMEDIATE,
        "Una persona nota 'saltos' en el pecho repetidamente. ¿Qué debe hacer la app?",
        listOf("Registrar contexto y recomendar valoración si persiste", "Diagnosticar extrasístoles", "Afirmar que es ansiedad", "Decir que no importa"),
        0,
        "La app no debe diagnosticar; puede ayudar a registrar y orientar."
    ),

    RhythmQuizQuestion(
        "Factores que pueden influir",
        RhythmDifficulty.INTERMEDIATE,
        "¿Qué dato ayuda a encontrar un patrón de palpitaciones?",
        listOf("Hora, sueño, ejercicio y estimulantes", "Color del reloj", "Marca del teléfono", "Batería"),
        0,
        "El contexto permite identificar asociaciones posibles."
    ),

    RhythmQuizQuestion(
        "Qué puede decir tu reloj",
        RhythmDifficulty.INTERMEDIATE,
        "El reloj marca un valor muy distinto mientras se mueve mucho el brazo. ¿Qué debe considerarse?",
        listOf("Puede existir artefacto por movimiento", "Es diagnóstico definitivo", "El movimiento mejora precisión", "No hace falta contexto"),
        0,
        "Los sensores ópticos pueden verse afectados por movimiento."
    ),

    RhythmQuizQuestion(
        "Señales de alarma",
        RhythmDifficulty.INTERMEDIATE,
        "Palpitaciones persistentes con mareo intenso merecen:",
        listOf("Valoración médica", "Solo registrar pasos", "Más cafeína", "Ejercicio intenso"),
        0,
        "Síntomas importantes junto con palpitaciones justifican valoración."
    ),

    RhythmQuizQuestion(
        "Cómo registrar un episodio",
        RhythmDifficulty.INTERMEDIATE,
        "¿Qué registro sería más útil en consulta?",
        listOf("8:15 pm, 7 min, sentado, mareo, frecuencia elevada", "Mi corazón estuvo raro", "Solo 120", "Un emoji"),
        0,
        "Duración, contexto y síntomas ayudan a interpretar el episodio."
    ),

    RhythmQuizQuestion(
        "Factores que pueden influir",
        RhythmDifficulty.INTERMEDIATE,
        "¿Qué combinación puede favorecer palpitaciones en algunas personas?",
        listOf("Poco sueño + estimulantes + estrés", "Dormir bien", "Leer sentado", "Música tranquila"),
        0,
        "Sueño insuficiente, estrés y estimulantes pueden influir."
    ),

    RhythmQuizQuestion(
        "Qué puede decir tu reloj",
        RhythmDifficulty.INTERMEDIATE,
        "¿Cuál uso del wearable es más responsable?",
        listOf("Observar tendencias y compartir datos relevantes", "Autodiagnosticarse", "Ignorar síntomas por un valor normal", "Cambiar medicamentos"),
        0,
        "El wearable funciona mejor como apoyo para observar y comunicar."
    ),

    RhythmQuizQuestion(
        "Palpitaciones y arritmias",
        RhythmDifficulty.INTERMEDIATE,
        "¿Qué prueba puede formar parte de la evaluación de un ritmo anormal?",
        listOf("Electrocardiograma", "Podómetro", "Contador de calorías", "Brújula"),
        0,
        "El electrocardiograma registra la actividad eléctrica del corazón."
    ),

    RhythmQuizQuestion(
        "Señales de alarma",
        RhythmDifficulty.INTERMEDIATE,
        "¿Un valor normal del reloj descarta una emergencia si hay dolor de pecho y dificultad para respirar?",
        listOf("No", "Sí", "Si el reloj es nuevo", "Solo en reposo"),
        0,
        "Los síntomas graves requieren atención aunque el wearable muestre un valor aparentemente normal."
    ),

    // -------------------- RETO NUBY --------------------

    RhythmQuizQuestion(
        "Cómo late el corazón",
        RhythmDifficulty.ADVANCED,
        "Alex hace ejercicio, su frecuencia aumenta y se recupera al descansar sin síntomas. ¿Qué interpretación educativa es mejor?",
        listOf("Puede ser una respuesta fisiológica al esfuerzo", "Es necesariamente una arritmia", "Debe dejar de moverse siempre", "La app puede diagnosticar"),
        0,
        "El ejercicio eleva normalmente la frecuencia; contexto y síntomas son esenciales."
    ),

    RhythmQuizQuestion(
        "Palpitaciones y arritmias",
        RhythmDifficulty.ADVANCED,
        "Sam siente palpitaciones ocasionales. ¿Qué dato NO basta por sí solo para diagnosticar una arritmia?",
        listOf("La sensación subjetiva de palpitación", "Un ECG clínicamente interpretado", "La evaluación médica", "Un registro clínico"),
        0,
        "La sensación de palpitación no identifica el mecanismo del ritmo."
    ),

    RhythmQuizQuestion(
        "Factores que pueden influir",
        RhythmDifficulty.ADVANCED,
        "Tras una noche sin dormir y varias bebidas energéticas aparecen palpitaciones. ¿Qué enfoque es más útil?",
        listOf("Registrar el patrón y reducir estimulantes", "Concluir una enfermedad específica", "Tomar más estimulantes", "Ignorar síntomas"),
        0,
        "El contexto sugiere factores posibles, pero no permite un diagnóstico."
    ),

    RhythmQuizQuestion(
        "Qué puede decir tu reloj",
        RhythmDifficulty.ADVANCED,
        "Un wearable muestra 180 lpm durante movimientos bruscos y luego mucho menos al detenerse. ¿Qué posibilidad debe considerarse?",
        listOf("Artefacto de lectura", "Diagnóstico definitivo", "Todo valor alto es normal", "El sensor nunca falla"),
        0,
        "El movimiento puede producir lecturas erróneas."
    ),

    RhythmQuizQuestion(
        "Señales de alarma",
        RhythmDifficulty.ADVANCED,
        "Una persona presenta palpitaciones, se desmaya y el reloj no registró nada. ¿Qué se prioriza?",
        listOf("Atención médica urgente", "Esperar al reloj", "Completar pasos", "Reiniciar Health Connect"),
        0,
        "El desmayo asociado a palpitaciones requiere prioridad clínica."
    ),

    RhythmQuizQuestion(
        "Cómo registrar un episodio",
        RhythmDifficulty.ADVANCED,
        "¿Qué combinación aporta mayor valor al profesional?",
        listOf("Síntomas + duración + contexto + mediciones", "Solo promedio mensual", "Solo modelo del reloj", "Solo pasos"),
        0,
        "Combinar síntomas, contexto y datos disponibles ofrece un registro más completo."
    ),

    RhythmQuizQuestion(
        "Qué puede decir tu reloj",
        RhythmDifficulty.ADVANCED,
        "¿Cuál afirmación evita sobreinterpretar Health Connect?",
        listOf("Los datos son apoyo, no diagnóstico", "Todo valor anormal es enfermedad", "Un dato normal elimina riesgos", "El reloj sustituye un ECG"),
        0,
        "Los datos digitales deben interpretarse dentro del contexto clínico."
    ),

    RhythmQuizQuestion(
        "Factores que pueden influir",
        RhythmDifficulty.ADVANCED,
        "Una persona nota episodios tras café pero nunca tras ejercicio. ¿Qué puede concluir la app?",
        listOf("Existe un patrón posible que conviene observar", "El café causó una arritmia específica", "El ejercicio cura arritmias", "No hace falta contexto"),
        0,
        "La app puede ayudar a detectar asociaciones, no establecer causalidad."
    ),

    RhythmQuizQuestion(
        "Palpitaciones y arritmias",
        RhythmDifficulty.ADVANCED,
        "¿Por qué una frecuencia normal no garantiza que el ritmo sea completamente normal?",
        listOf("Frecuencia y organización del ritmo son conceptos distintos", "El reloj siempre falla", "Toda persona tiene arritmia", "La frecuencia no existe"),
        0,
        "Puede haber alteraciones del ritmo con frecuencias que no parecen extremas."
    ),

    RhythmQuizQuestion(
        "Señales de alarma",
        RhythmDifficulty.ADVANCED,
        "¿Qué principio es más seguro ante síntomas intensos?",
        listOf("No retrasar ayuda por esperar más datos", "Esperar cinco mediciones", "Autodiagnosticarse", "Modificar medicación"),
        0,
        "En síntomas intensos, obtener más datos no debe retrasar la atención."
    )
)



// ============================================================================
// MAPEAR PROGRESO LOCAL A PETICIÓN REMOTA
// ============================================================================

private fun rhythmProgressToRequest(
    progress: RhythmLearningProgress
): RitmoProgresoRequest {
    return RitmoProgresoRequest(
        xp = progress.xp,
        nivel = rhythmLevel(progress.xp),
        temasCompletados = progress.completedTopics.toList(),
        intentos = progress.attempts,
        mejorPuntaje = progress.bestScore,
        temasAReforzar = progress.topicsToReinforce.toList()
    )
}

// ============================================================================
// PANTALLA PRINCIPAL
// ============================================================================

/**
 * Pantalla de monitoreo de Trastornos del ritmo.
 *
 * IMPORTANTE:
 *
 * Conservamos el nombre HealthConnectScreen porque MainActivity
 * ya navega correctamente hacia esta función.
 *
 * Esta pantalla muestra:
 *
 * - Estado de conexión con Health Connect.
 * - Pasos realizados durante el día.
 * - Meta de 10,000 pasos.
 * - Semaforización de actividad física.
 * - Frecuencia cardíaca del día.
 * - Gráfica interactiva.
 * - Valor mínimo.
 * - Valor máximo.
 * - Selección de puntos tocando o arrastrando.
 *
 * El sueño ya NO se muestra aquí.
 * Posteriormente lo mostraremos en Hábitos saludables.
 */
@Composable
fun HealthConnectScreen(
    onBackToMenu: () -> Unit,
    onMenuClick: () -> Unit
) {

    // Contexto actual.
    val context =
        LocalContext.current


    var selectedRhythmTopic by remember {
        mutableStateOf<RhythmLearningTopic?>(null)
    }


    var rhythmProgress by remember {
        mutableStateOf(
            RhythmProgressLocal.load(context)
        )
    }


    var rhythmSyncMessage by remember {
        mutableStateOf("Sincronizando progreso...")
    }

    LaunchedEffect(Unit) {
        RitmoProgresoRepository.obtenerProgreso(
            onSuccess = { remote ->
                if (remote != null) {
                    val remoteProgress =
                        RhythmLearningProgress(
                            xp = remote.xp,
                            completedTopics = remote.temasCompletados.toSet(),
                            attempts = remote.intentos,
                            bestScore = remote.mejorPuntaje,
                            topicsToReinforce = remote.temasAReforzar.toSet()
                        )

                    rhythmProgress = remoteProgress
                    RhythmProgressLocal.save(context, remoteProgress)
                    rhythmSyncMessage = "Progreso sincronizado"
                } else {
                    RitmoProgresoRepository.guardarProgreso(
                        progreso = rhythmProgressToRequest(rhythmProgress),
                        onSuccess = {
                            rhythmSyncMessage = "Progreso sincronizado"
                        },
                        onError = {
                            rhythmSyncMessage = "Sin conexión: usando progreso local"
                        }
                    )
                }
            },
            onError = {
                rhythmSyncMessage = "Sin conexión: usando progreso local"
            }
        )
    }


    // ------------------------------------------------------------------------
    // MANAGER DE HEALTH CONNECT
    // ------------------------------------------------------------------------

    val healthConnectManager =
        remember(
            context
        ) {

            HealthConnectManager(
                context.applicationContext
            )
        }


    // ------------------------------------------------------------------------
    // CORRUTINA
    // ------------------------------------------------------------------------

    val coroutineScope =
        rememberCoroutineScope()


    // ------------------------------------------------------------------------
    // SHARED PREFERENCES
    // ------------------------------------------------------------------------

    val preferences =
        remember {

            context.getSharedPreferences(
                "health_connect_preferences",
                Context.MODE_PRIVATE
            )
        }


    // ========================================================================
    // ESTADOS
    // ========================================================================

    // Indica si tenemos permisos.
    var hasPermissions by
    remember {

        mutableStateOf(false)
    }


    // Mensaje de estado.
    var message by
    remember {

        mutableStateOf(
            "Revisando conexión con Health Connect..."
        )
    }


    // Indica si estamos leyendo datos.
    var isLoading by
    remember {

        mutableStateOf(false)
    }


    // Pasos de hoy.
    var todaySteps by
    remember {

        mutableStateOf<Long?>(null)
    }


    // Todas las mediciones cardíacas del día.
    var heartRatePoints by
    remember {

        mutableStateOf<List<HeartRatePoint>>(
            emptyList()
        )
    }


    // Mediciones del último día histórico disponible.
    //
    // Se usan únicamente cuando hoy no existen datos.
    var historicalHeartRatePoints by
    remember {

        mutableStateOf<List<HeartRatePoint>>(
            emptyList()
        )
    }


    // ========================================================================
    // PERMISOS
    // ========================================================================

    suspend fun readHealthData(
        syncWithBackend: Boolean = false
    ) {

        try {

            isLoading =
                true


            hasPermissions =
                healthConnectManager
                    .hasAllPermissions()


            if (
                !hasPermissions
            ) {

                message =
                    "Primero concede los permisos de Health Connect."

                return
            }


            // ---------------------------------------------------------------
            // PASOS DE HOY
            // ---------------------------------------------------------------

            val pasosLeidos =
                healthConnectManager
                    .readTodaySteps()


            todaySteps =
                pasosLeidos


            // ---------------------------------------------------------------
            // FRECUENCIA CARDÍACA DEL DÍA
            // ---------------------------------------------------------------

            val puntosFrecuencia =
                healthConnectManager
                    .readTodayHeartRatePoints()


            heartRatePoints =
                puntosFrecuencia


            // Si hoy no hay datos, buscamos TODAS las mediciones
            // del último día disponible para poder dibujar la gráfica.
            historicalHeartRatePoints =
                if (
                    puntosFrecuencia.isEmpty()
                ) {

                    healthConnectManager
                        .readLatestHeartRateDayPointsFromLastDays(
                            days = 30
                        )

                } else {

                    emptyList()
                }


            // ---------------------------------------------------------------
            // ACTUALIZAR INTERFAZ
            // ---------------------------------------------------------------

            message =
                "Datos actualizados correctamente."


            // ---------------------------------------------------------------
            // SINCRONIZAR CON LA API
            // ---------------------------------------------------------------
            //
            // No hacemos una escritura en Firebase cada 60 segundos.
            //
            // Sincronizamos:
            //
            // - al abrir la pantalla;
            // - cuando el usuario pulsa "Actualizar datos";
            // - después de conceder permisos.
            //
            // ---------------------------------------------------------------

            if (
                syncWithBackend
            ) {


                // -----------------------------------------------------------
                // ÚLTIMA SESIÓN DE SUEÑO
                // -----------------------------------------------------------

                val sleepSummary =
                    healthConnectManager
                        .readLastSleepSummaryFromLastDays(
                            days = 30
                        )


                // -----------------------------------------------------------
                // FC ACTUAL / MÍNIMA / MÁXIMA
                // -----------------------------------------------------------

                val latestHeartRate =
                    puntosFrecuencia
                        .maxByOrNull {
                            it.time
                        }
                        ?.bpm


                val minimumHeartRate =
                    puntosFrecuencia
                        .minByOrNull {
                            it.bpm
                        }
                        ?.bpm


                val maximumHeartRate =
                    puntosFrecuencia
                        .maxByOrNull {
                            it.bpm
                        }
                        ?.bpm


                // -----------------------------------------------------------
                // REQUEST
                // -----------------------------------------------------------

                val request =
                    HealthConnectSyncRequest(

                        fecha =
                            LocalDate
                                .now(
                                    ZoneId.systemDefault()
                                )
                                .toString(),

                        pasos =
                            pasosLeidos,

                        frecuenciaCardiaca =
                            latestHeartRate,

                        frecuenciaCardiacaMinima =
                            minimumHeartRate,

                        frecuenciaCardiacaMaxima =
                            maximumHeartRate,

                        cantidadMedicionesFrecuenciaCardiaca =
                            puntosFrecuencia.size,

                        suenoMinutos =
                            sleepSummary
                                ?.totalMinutes,

                        inicioSueno =
                            sleepSummary
                                ?.startTime
                                ?.toString(),

                        finSueno =
                            sleepSummary
                                ?.endTime
                                ?.toString(),

                        suenoLigeroMinutos =
                            sleepSummary
                                ?.lightSleepMinutes,

                        suenoProfundoMinutos =
                            sleepSummary
                                ?.deepSleepMinutes,

                        suenoRemMinutos =
                            sleepSummary
                                ?.remSleepMinutes,

                        despiertoMinutos =
                            sleepSummary
                                ?.awakeMinutes,

                        fuente =
                            "Mi Fitness / Health Connect",

                        fechaLectura =
                            Instant
                                .now()
                                .toString()
                    )


                // -----------------------------------------------------------
                // ENVIAR A EXPRESS
                // -----------------------------------------------------------

                HealthConnectRepository
                    .sincronizar(

                        datos =
                            request,

                        onSuccess = {

                            message =
                                "Datos actualizados y sincronizados correctamente."
                        },

                        onUnauthorized = {

                            message =
                                "La sesión no es válida. Inicia sesión nuevamente."
                        },

                        onError = {
                                error ->

                            // Los datos locales sí se pudieron leer.
                            // Por eso no borramos lo que ya está en pantalla.
                            message =
                                "Datos actualizados. Sincronización pendiente: $error"
                        }
                    )
            }


        } catch (
            e: Exception
        ) {

            message =
                "Error al leer datos: ${e.message}"

        } finally {

            isLoading =
                false
        }
    }


    val permissionLauncher =
        rememberLauncherForActivityResult(

            contract =
                PermissionController
                    .createRequestPermissionResultContract()

        ) {

            coroutineScope.launch {

                hasPermissions =
                    healthConnectManager
                        .hasAllPermissions()


                if (hasPermissions) {

                    message =
                        "Health Connect conectado correctamente."


                    readHealthData(
                        syncWithBackend = true
                    )

                } else {

                    message =
                        "Faltan permisos para consultar tus datos."
                }
            }
        }


    // ========================================================================
    // LEER DATOS
    // ========================================================================

    /**
     * Consulta los datos necesarios para esta pantalla.
     *
     * Solamente necesitamos:
     *
     * - Pasos de hoy.
     * - Frecuencia cardíaca de hoy.
     */



    // ========================================================================
    // PRIMERA APERTURA
    // ========================================================================

    LaunchedEffect(Unit) {

        val status =
            healthConnectManager
                .getAvailabilityStatus()


        if (
            status !=
            HealthConnectClient.SDK_AVAILABLE
        ) {

            message =

                if (
                    status ==
                    HealthConnectClient.SDK_UNAVAILABLE_PROVIDER_UPDATE_REQUIRED
                ) {

                    "Health Connect necesita instalarse o actualizarse."

                } else {

                    "Health Connect no está disponible en este dispositivo."
                }


            return@LaunchedEffect
        }


        hasPermissions =
            healthConnectManager
                .hasAllPermissions()


        val alreadyAskedAutomatically =
            preferences.getBoolean(

                "asked_health_permissions_automatically",

                false
            )


        if (
            !hasPermissions &&
            !alreadyAskedAutomatically
        ) {

            preferences
                .edit()
                .putBoolean(

                    "asked_health_permissions_automatically",

                    true
                )
                .apply()


            message =
                "Solicitando permisos de Health Connect..."


            delay(
                600
            )


            permissionLauncher.launch(
                healthConnectManager.permissions
            )

        } else if (hasPermissions) {

            readHealthData(
                syncWithBackend = true
            )

        } else {

            message =
                "Los permisos no están concedidos."
        }
    }


    // ========================================================================
    // ACTUALIZACIÓN AUTOMÁTICA
    // ========================================================================

    /**
     * Mientras esta pantalla esté abierta,
     * actualizamos los datos cada 60 segundos.
     */
    LaunchedEffect(hasPermissions) {

        if (hasPermissions) {

            while (true) {

                readHealthData(
                    syncWithBackend = false
                )

                delay(
                    60_000
                )
            }
        }
    }


    // ========================================================================
    // INTERFAZ
    // ========================================================================

    Box(

        modifier =
            Modifier
                .fillMaxSize()
                .background(
                    backgroundGradient()
                )
                .statusBarsPadding()
                .navigationBarsPadding()
                .imePadding()

    ) {

        Column(

            modifier =
                Modifier
                    .fillMaxSize()
                    .verticalScroll(
                        rememberScrollState()
                    )
                    .padding(
                        horizontal = 18.dp,
                        vertical = 20.dp
                    ),

            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {


            // =================================================================
            // TARJETA PRINCIPAL
            // =================================================================

            Card(

                modifier =
                    Modifier
                        .fillMaxWidth()
                        .widthIn(
                            max = 470.dp
                        ),

                shape =
                    RoundedCornerShape(
                        30.dp
                    ),

                colors =
                    CardDefaults.cardColors(

                        containerColor =
                            Color(0xFFFEFFF6)
                    ),

                elevation =
                    CardDefaults.cardElevation(

                        defaultElevation =
                            10.dp
                    )
            ) {

                Column(

                    modifier =
                        Modifier.padding(
                            20.dp
                        ),

                    horizontalAlignment =
                        Alignment.CenterHorizontally
                ) {


                    // =========================================================
                    // TÍTULO
                    // =========================================================

                    Text(

                        text =
                            "Trastornos del ritmo",

                        fontSize =
                            27.sp,

                        fontWeight =
                            FontWeight.Bold,

                        color =
                            Color(0xFF0F766E),

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
                            "Monitorea tu actividad física y frecuencia cardíaca con los datos que Mi Fitness comparte con Health Connect.",

                        fontSize =
                            13.sp,

                        color =
                            Color(0xFF64748B),

                        textAlign =
                            TextAlign.Center,

                        lineHeight =
                            18.sp
                    )


                    Spacer(
                        modifier =
                            Modifier.height(
                                14.dp
                            )
                    )


                    // =========================================================
                    // ESTADO DE HEALTH CONNECT
                    // =========================================================

                    Text(

                        text =
                            message,

                        fontSize =
                            12.sp,

                        color =
                            Color(0xFF475569),

                        textAlign =
                            TextAlign.Center
                    )


                    if (isLoading) {

                        Spacer(
                            modifier =
                                Modifier.height(
                                    12.dp
                                )
                        )


                        CircularProgressIndicator(

                            modifier =
                                Modifier.size(
                                    26.dp
                                ),

                            color =
                                Color(0xFF0F766E),

                            strokeWidth =
                                3.dp
                        )
                    }


                    Spacer(
                        modifier =
                            Modifier.height(
                                20.dp
                            )
                    )


                    RhythmLearningSection(
                        progress = rhythmProgress,
                        syncMessage = rhythmSyncMessage,
                        selectedTopic = selectedRhythmTopic,
                        onTopicClick = { topic ->
                            selectedRhythmTopic =
                                if (selectedRhythmTopic == topic) null else topic
                        },
                        onCompleteTopic = { topic ->
                            if (!rhythmProgress.completedTopics.contains(topic.title)) {
                                val updated =
                                    rhythmProgress.copy(
                                        xp = rhythmProgress.xp + 10,
                                        completedTopics =
                                            rhythmProgress.completedTopics + topic.title
                                    )

                                rhythmProgress = updated
                                RhythmProgressLocal.save(context, updated)

                                rhythmSyncMessage = "Sincronizando progreso..."

                                RitmoProgresoRepository.guardarProgreso(
                                    progreso = rhythmProgressToRequest(updated),
                                    onSuccess = {
                                        rhythmSyncMessage = "Progreso sincronizado"
                                    },
                                    onError = {
                                        rhythmSyncMessage = "Sin conexión: usando progreso local"
                                    }
                                )
                            }
                        },
                        onQuizFinished = { result ->
                            val improved = result.score > rhythmProgress.bestScore
                            val earned =
                                when {
                                    rhythmProgress.attempts == 0 -> 20
                                    improved -> 10
                                    else -> 5
                                }

                            val updated =
                                rhythmProgress.copy(
                                    xp = rhythmProgress.xp + earned,
                                    attempts = rhythmProgress.attempts + 1,
                                    bestScore = maxOf(rhythmProgress.bestScore, result.score),
                                    topicsToReinforce = result.failedTopics
                                )

                            rhythmProgress = updated
                            RhythmProgressLocal.save(context, updated)

                            rhythmSyncMessage = "Sincronizando progreso..."

                            RitmoProgresoRepository.guardarProgreso(
                                progreso = rhythmProgressToRequest(updated),
                                onSuccess = {
                                    rhythmSyncMessage = "Progreso sincronizado"
                                },
                                onError = {
                                    rhythmSyncMessage = "Sin conexión: usando progreso local"
                                }
                            )

                            RitmoProgresoRepository.guardarIntento(
                                intento = RitmoIntentoRequest(
                                    puntaje = result.score,
                                    total = result.total,
                                    dificultad = result.difficulty,
                                    xpGanado = earned,
                                    temasFallados = result.failedTopics.toList()
                                ),
                                onSuccess = {},
                                onError = {}
                            )
                        },
                        onCloseTopic = {
                            selectedRhythmTopic = null
                        }
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    HorizontalDivider(color = Color(0xFFE2E8F0))

                    Spacer(modifier = Modifier.height(22.dp))

                    // =========================================================
                    // ACTIVIDAD FÍSICA
                    // =========================================================

                    StepsMonitoringCard(

                        steps =
                            todaySteps
                                ?: 0L
                    )


                    Spacer(
                        modifier =
                            Modifier.height(
                                22.dp
                            )
                    )


                    HorizontalDivider(

                        color =
                            Color(0xFFE2E8F0)
                    )


                    Spacer(
                        modifier =
                            Modifier.height(
                                22.dp
                            )
                    )


                    // =========================================================
                    // FRECUENCIA CARDÍACA
                    // =========================================================

                    HeartRateMonitoringCard(

                        points =
                            if (
                                heartRatePoints.isNotEmpty()
                            ) {

                                heartRatePoints

                            } else {

                                historicalHeartRatePoints
                            },

                        isHistorical =
                            heartRatePoints.isEmpty() &&
                                    historicalHeartRatePoints.isNotEmpty()
                    )


                    Spacer(
                        modifier =
                            Modifier.height(
                                22.dp
                            )
                    )


                    // =========================================================
                    // ACTUALIZAR
                    // =========================================================

                    Button(

                        onClick = {

                            coroutineScope.launch {

                                readHealthData(
                                    syncWithBackend = true
                                )
                            }
                        },

                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(
                                    52.dp
                                ),

                        shape =
                            RoundedCornerShape(
                                18.dp
                            ),

                        colors =
                            ButtonDefaults.buttonColors(

                                containerColor =
                                    Color(0xFF0F766E),

                                contentColor =
                                    Color.White
                            )
                    ) {

                        Icon(

                            imageVector =
                                Icons.Default.Refresh,

                            contentDescription =
                                null,

                            modifier =
                                Modifier.size(
                                    20.dp
                                )
                        )


                        Spacer(
                            modifier =
                                Modifier.size(
                                    8.dp
                                )
                        )


                        Text(

                            text =
                                "Actualizar datos",

                            fontWeight =
                                FontWeight.Bold
                        )
                    }


                    // =========================================================
                    // SOLICITAR PERMISOS
                    // =========================================================

                    if (!hasPermissions) {

                        Spacer(
                            modifier =
                                Modifier.height(
                                    12.dp
                                )
                        )


                        Button(

                            onClick = {

                                val status =
                                    healthConnectManager
                                        .getAvailabilityStatus()


                                if (
                                    status ==
                                    HealthConnectClient.SDK_AVAILABLE
                                ) {

                                    permissionLauncher.launch(
                                        healthConnectManager.permissions
                                    )

                                } else {

                                    message =

                                        if (
                                            status ==
                                            HealthConnectClient.SDK_UNAVAILABLE_PROVIDER_UPDATE_REQUIRED
                                        ) {

                                            "Health Connect necesita instalarse o actualizarse."

                                        } else {

                                            "Health Connect no está disponible en este dispositivo."
                                        }
                                }
                            },

                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .height(
                                        50.dp
                                    ),

                            shape =
                                RoundedCornerShape(
                                    18.dp
                                )
                        ) {

                            Text(
                                text =
                                    "Conceder permisos"
                            )
                        }
                    }


                    Spacer(
                        modifier =
                            Modifier.height(
                                14.dp
                            )
                    )


                    // =========================================================
                    // REGRESAR
                    // =========================================================

                    Button(

                        onClick =
                            onMenuClick,

                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(
                                    52.dp
                                ),

                        shape =
                            RoundedCornerShape(
                                18.dp
                            ),

                        colors =
                            ButtonDefaults.buttonColors(

                                containerColor =
                                    Color(0xFF86A327),

                                contentColor =
                                    Color.White
                            )
                    ) {

                        Text(

                            text =
                                "Volver al menú",

                            fontWeight =
                                FontWeight.Bold
                        )
                    }
                }
            }


            Spacer(
                modifier =
                    Modifier.height(
                        30.dp
                    )
            )
        }
    }
}


// ============================================================================
// TARJETA DE PASOS
// ============================================================================

/**
 * Muestra la actividad física diaria.
 *
 * La meta utilizada por la app es:
 *
 * 10,000 pasos.
 */

// ============================================================================
// APRENDE CON NUBY
// ============================================================================

@Composable
private fun RhythmLearningSection(
    progress: RhythmLearningProgress,
    syncMessage: String,
    selectedTopic: RhythmLearningTopic?,
    onTopicClick: (RhythmLearningTopic) -> Unit,
    onCompleteTopic: (RhythmLearningTopic) -> Unit,
    onQuizFinished: (RhythmQuizResultData) -> Unit,
    onCloseTopic: () -> Unit
) {
    var showQuiz by remember { mutableStateOf(false) }
    var lastResult by remember { mutableStateOf<RhythmQuizResultData?>(null) }

    val badges = rhythmBadges(progress)

    Column(modifier = Modifier.fillMaxWidth()) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.nuby_idea),
                contentDescription = "Nuby explicando",
                modifier = Modifier.size(76.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.size(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "Aprende con Nuby",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F766E)
                )
                Text(
                    "Conoce tu ritmo cardíaco antes de interpretar los datos.",
                    fontSize = 12.sp,
                    lineHeight = 17.sp,
                    color = Color(0xFF64748B)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFE0F2FE)
            )
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            "Tu progreso",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F766E)
                        )
                        Text(
                            rhythmLevel(progress.xp),
                            fontSize = 11.sp,
                            color = Color(0xFF0369A1)
                        )
                    }

                    Text(
                        "${progress.xp} XP",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F766E)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                LinearProgressIndicator(
                    progress = { rhythmLevelProgress(progress.xp) },
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFF0F766E),
                    trackColor = Color(0xFFD7EAF4)
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    "${rhythmXpToNext(progress.xp)} XP para el siguiente nivel",
                    fontSize = 10.sp,
                    color = Color(0xFF64748B)
                )

                Spacer(modifier = Modifier.height(7.dp))

                Text(
                    "Temas ${progress.completedTopics.size}/${rhythmLearningTopics.size} · Evaluaciones ${progress.attempts} · Mejor ${progress.bestScore}/10",
                    fontSize = 10.sp,
                    color = Color(0xFF475569)
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    "Dificultad: ${rhythmDifficultyFor(progress).label}",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0369A1)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    if (syncMessage == "Progreso sincronizado")
                        "☁️ $syncMessage"
                    else
                        syncMessage,
                    fontSize = 9.sp,
                    color = Color(0xFF64748B)
                )

                if (progress.topicsToReinforce.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        "🎯 Reforzar: ${progress.topicsToReinforce.joinToString(", ")}",
                        fontSize = 10.sp,
                        lineHeight = 15.sp,
                        color = Color(0xFF7C3AED)
                    )
                }

                if (badges.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        "Insignias: ${badges.joinToString("  ")}",
                        fontSize = 10.sp,
                        lineHeight = 15.sp,
                        color = Color(0xFF334155)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        rhythmLearningTopics.chunked(2).forEach { rowTopics ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                rowTopics.forEach { topic ->
                    RhythmTopicCard(
                        topic = topic,
                        selected = selectedTopic == topic,
                        completed = progress.completedTopics.contains(topic.title),
                        modifier = Modifier.weight(1f),
                        onClick = { onTopicClick(topic) }
                    )
                }

                if (rowTopics.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

        selectedTopic?.let { topic ->
            RhythmTopicDetail(
                topic = topic,
                completed = progress.completedTopics.contains(topic.title),
                onComplete = { onCompleteTopic(topic) },
                onClose = onCloseTopic
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFDDF7EE)
            )
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.nuby_pensando),
                    contentDescription = "Nuby evaluando",
                    modifier = Modifier.size(68.dp)
                )
                Spacer(modifier = Modifier.size(9.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Reto del ritmo",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F766E)
                    )
                    Text(
                        "10 preguntas para reforzar lo aprendido.",
                        fontSize = 11.sp,
                        color = Color(0xFF475569)
                    )
                    Spacer(modifier = Modifier.height(7.dp))
                    Button(
                        onClick = {
                            showQuiz = !showQuiz
                            lastResult = null
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF0F766E)
                        )
                    ) {
                        Text(if (showQuiz) "Cerrar evaluación" else "Comenzar evaluación")
                    }
                }
            }
        }

        if (showQuiz) {
            Spacer(modifier = Modifier.height(12.dp))
            RhythmQuiz(
                progress = progress,
                onFinished = { result ->
                    lastResult = result
                    onQuizFinished(result)
                    showQuiz = false
                }
            )
        }

        lastResult?.let { result ->
            Spacer(modifier = Modifier.height(12.dp))
            RhythmQuizResult(result, progress)
        }

        Spacer(modifier = Modifier.height(10.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFFFF4E5)
            )
        ) {
            Text(
                "ℹ️ Este contenido es educativo. Vibra la Vida no diagnostica arritmias ni sustituye una valoración médica.",
                modifier = Modifier.padding(13.dp),
                fontSize = 11.sp,
                lineHeight = 16.sp,
                color = Color(0xFF7C4A03)
            )
        }
    }
}

@Composable
private fun RhythmTopicCard(
    topic: RhythmLearningTopic,
    selected: Boolean,
    completed: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor =
                if (selected) Color(0xFFE0F2FE) else Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(13.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = topic.emoji, fontSize = 28.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = topic.title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F766E),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = topic.subtitle,
                fontSize = 10.sp,
                lineHeight = 14.sp,
                color = Color(0xFF64748B),
                textAlign = TextAlign.Center
            )

            if (completed) {
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    "✓ Completado",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F766E)
                )
            }
        }
    }
}

@Composable
private fun RhythmTopicDetail(
    topic: RhythmLearningTopic,
    completed: Boolean,
    onComplete: () -> Unit,
    onClose: () -> Unit
) {
    val textToRead = buildString {
        append(topic.title).append(". ")
        append(topic.subtitle).append(". ")
        topic.paragraphs.forEach { append(it).append(". ") }
        append("Ejemplo. ").append(topic.example).append(". ")
        append("Recuerda. ").append(topic.keyMessage)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFEFFF6)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = topic.nubyRes),
                    contentDescription = "Nuby acompañando el tema",
                    modifier = Modifier.size(78.dp),
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.size(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "${topic.emoji} ${topic.title}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F766E)
                    )
                    Text(
                        text = topic.subtitle,
                        fontSize = 11.sp,
                        lineHeight = 16.sp,
                        color = Color(0xFF64748B)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            RhythmTextToSpeechButton(textToRead)

            Spacer(modifier = Modifier.height(12.dp))

            topic.paragraphs.forEach { paragraph ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "✓",
                            color = Color(0xFF0F766E),
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Text(
                            text = paragraph,
                            modifier = Modifier.weight(1f),
                            fontSize = 12.sp,
                            lineHeight = 18.sp,
                            color = Color(0xFF334155)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFE0F2FE)
                )
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "💡 Ejemplo",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0369A1)
                    )
                    Text(
                        text = topic.example,
                        fontSize = 11.sp,
                        lineHeight = 17.sp,
                        color = Color(0xFF334155)
                    )
                }
            }

            Spacer(modifier = Modifier.height(9.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFDDF7EE)
                )
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "📌 Quédate con esto",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F766E)
                    )
                    Text(
                        text = topic.keyMessage,
                        fontSize = 11.sp,
                        lineHeight = 17.sp,
                        color = Color(0xFF334155)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor =
                        if (completed) Color(0xFFDDF7EE)
                        else Color(0xFFFFF7D6)
                )
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        if (completed) "✅ Tema completado"
                        else "¿Terminaste este tema?",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = Color(0xFF0F766E)
                    )

                    if (!completed) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Button(
                            onClick = onComplete,
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF0F766E)
                            )
                        ) {
                            Text("Completar tema · +10 XP")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedButton(
                onClick = onClose,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cerrar tema")
            }
        }
    }
}

@Composable
private fun RhythmTextToSpeechButton(
    textToRead: String
) {
    val context = LocalContext.current
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }
    var ready by remember { mutableStateOf(false) }

    DisposableEffect(context) {
        val engine = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = tts?.setLanguage(Locale("es", "MX"))
                ready =
                    result != TextToSpeech.LANG_MISSING_DATA &&
                    result != TextToSpeech.LANG_NOT_SUPPORTED
            }
        }

        tts = engine

        onDispose {
            engine.stop()
            engine.shutdown()
            tts = null
        }
    }

    Button(
        onClick = {
            if (ready) {
                tts?.speak(
                    textToRead,
                    TextToSpeech.QUEUE_FLUSH,
                    null,
                    "rhythm_tts"
                )
            }
        },
        enabled = ready,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF0F766E)
        )
    ) {
        Text("🔊 Escuchar")
    }
}



@Composable
private fun RhythmQuiz(
    progress: RhythmLearningProgress,
    onFinished: (RhythmQuizResultData) -> Unit
) {
    val questions =
        remember(
            progress.attempts,
            progress.bestScore,
            progress.topicsToReinforce
        ) {
            buildRhythmQuiz(progress)
        }

    var current by remember(questions) { mutableIntStateOf(0) }
    var selected by remember(questions) { mutableIntStateOf(-1) }
    var score by remember(questions) { mutableIntStateOf(0) }
    var checked by remember(questions) { mutableStateOf(false) }
    var failedTopics by remember(questions) {
        mutableStateOf(emptySet<String>())
    }

    if (questions.isEmpty()) {
        Text("No hay preguntas disponibles.")
        return
    }

    val question = questions[current]

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {

            Text(
                "${question.difficulty.label} · ${question.topic}",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF7C3AED)
            )

            Text(
                "Pregunta ${current + 1} de ${questions.size}",
                fontSize = 10.sp,
                color = Color(0xFF64748B)
            )

            Text(
                question.question,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 20.sp,
                color = Color(0xFF0F172A)
            )

            Spacer(modifier = Modifier.height(8.dp))

            RhythmTextToSpeechButton(
                buildString {
                    append(question.question).append(". ")
                    question.options.forEachIndexed { index, option ->
                        append("Opción ${index + 1}. $option. ")
                    }
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            question.options.forEachIndexed { index, option ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = selected == index,
                        enabled = !checked,
                        onClick = { selected = index },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = Color(0xFF0F766E)
                        )
                    )
                    Text(
                        option,
                        modifier = Modifier.weight(1f),
                        fontSize = 12.sp,
                        lineHeight = 17.sp
                    )
                }
            }

            if (checked) {
                val correct = selected == question.correctAnswer

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor =
                            if (correct) Color(0xFFDDF7EE)
                            else Color(0xFFFFF4E5)
                    )
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            if (correct) "¡Correcto!" else "Vamos a reforzarlo",
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            question.explanation,
                            fontSize = 11.sp,
                            lineHeight = 16.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        RhythmTextToSpeechButton(question.explanation)
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = {
                    if (!checked) {
                        if (selected == question.correctAnswer) {
                            score++
                        } else {
                            failedTopics = failedTopics + question.topic
                        }
                        checked = true
                    } else if (current < questions.lastIndex) {
                        current++
                        selected = -1
                        checked = false
                    } else {
                        onFinished(
                            RhythmQuizResultData(
                                score = score,
                                total = questions.size,
                                difficulty = rhythmDifficultyFor(progress).label,
                                failedTopics = failedTopics
                            )
                        )
                    }
                },
                enabled = selected >= 0,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0F766E)
                )
            ) {
                Text(
                    if (!checked) "Comprobar respuesta"
                    else if (current == questions.lastIndex) "Ver resultado"
                    else "Siguiente pregunta"
                )
            }
        }
    }
}

@Composable
private fun RhythmQuizResult(
    result: RhythmQuizResultData,
    progress: RhythmLearningProgress
) {
    val message =
        when {
            result.score >= 9 -> "¡Excelente! Identificas muy bien las ideas principales."
            result.score >= 7 -> "¡Muy bien! Repasa los temas que te generaron dudas."
            result.score >= 5 -> "Vas avanzando. Repasa señales de alarma y uso responsable del wearable."
            else -> "Cada intento cuenta. Repasa con Nuby y vuelve a intentarlo."
        }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE0F2FE)
        )
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.nuby_abrazo),
                contentDescription = "Nuby celebrando",
                modifier = Modifier.size(80.dp)
            )

            Text(
                "${result.score} / ${result.total}",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F766E)
            )

            Text(
                "${result.difficulty} · Nivel ${rhythmLevel(progress.xp)}",
                fontSize = 11.sp,
                color = Color(0xFF0369A1)
            )

            if (result.failedTopics.isNotEmpty()) {
                Spacer(modifier = Modifier.height(7.dp))
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFF3E8FF)
                    )
                ) {
                    Text(
                        "🎯 Temas para reforzar: ${result.failedTopics.joinToString(", ")}",
                        modifier = Modifier.padding(9.dp),
                        fontSize = 10.sp,
                        lineHeight = 15.sp,
                        color = Color(0xFF6D28D9)
                    )
                }
            }

            Spacer(modifier = Modifier.height(7.dp))

            Text(
                message,
                fontSize = 12.sp,
                lineHeight = 17.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(7.dp))

            RhythmTextToSpeechButton(
                buildString {
                    append("Obtuviste ${result.score} de ${result.total}. ")
                    if (result.failedTopics.isNotEmpty()) {
                        append("Te conviene reforzar: ${result.failedTopics.joinToString(", ")}. ")
                    }
                    append(message)
                }
            )
        }
    }
}


@Composable
fun StepsMonitoringCard(
    steps: Long
) {

    val target =
        10_000L


    // Porcentaje respecto a la meta.
    val progress =
        (steps.toFloat() / target.toFloat())
            .coerceIn(
                0f,
                1f
            )


    // Clasificamos los pasos.
    val status =
        classifySteps(
            steps
        )


    val statusColor =
        trafficLightColor(
            status
        )


    val statusText =
        when (status) {

            HealthTrafficLight.GOOD ->
                "Meta alcanzada"

            HealthTrafficLight.REGULAR ->
                "Progreso regular"

            HealthTrafficLight.LOW ->
                "Actividad baja"
        }


    val remainingSteps =
        (target - steps)
            .coerceAtLeast(
                0L
            )


    Card(

        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(
                24.dp
            ),

        colors =
            CardDefaults.cardColors(

                containerColor =
                    Color.White
            ),

        elevation =
            CardDefaults.cardElevation(

                defaultElevation =
                    4.dp
            )
    ) {

        Column(

            modifier =
                Modifier.padding(
                    18.dp
                )
        ) {


            Row(

                verticalAlignment =
                    Alignment.CenterVertically
            ) {


                Box(

                    modifier =
                        Modifier
                            .size(
                                50.dp
                            )
                            .background(

                                color =
                                    Color(0xFFF7FCEB),

                                shape =
                                    CircleShape
                            ),

                    contentAlignment =
                        Alignment.Center
                ) {

                    Icon(

                        imageVector =
                            Icons.Default.DirectionsWalk,

                        contentDescription =
                            null,

                        tint =
                            Color(0xFF86A327),

                        modifier =
                            Modifier.size(
                                30.dp
                            )
                    )
                }


                Spacer(
                    modifier =
                        Modifier.size(
                            12.dp
                        )
                )


                Column {

                    Text(

                        text =
                            "Actividad física",

                        fontSize =
                            18.sp,

                        fontWeight =
                            FontWeight.Bold,

                        color =
                            Color(0xFF0F172A)
                    )


                    Text(

                        text =
                            "Meta diaria: 10,000 pasos",

                        fontSize =
                            12.sp,

                        color =
                            Color(0xFF64748B)
                    )
                }
            }


            Spacer(
                modifier =
                    Modifier.height(
                        18.dp
                    )
            )


            Text(

                text =
                    "$steps pasos",

                fontSize =
                    28.sp,

                fontWeight =
                    FontWeight.Bold,

                color =
                    Color(0xFF0F766E)
            )


            Spacer(
                modifier =
                    Modifier.height(
                        10.dp
                    )
            )


            LinearProgressIndicator(

                progress = {
                    progress
                },

                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(
                            10.dp
                        ),

                color =
                    statusColor,

                trackColor =
                    Color(0xFFE2E8F0)
            )


            Spacer(
                modifier =
                    Modifier.height(
                        12.dp
                    )
            )


            Row(

                verticalAlignment =
                    Alignment.CenterVertically
            ) {


                // Punto de semáforo.
                Box(

                    modifier =
                        Modifier
                            .size(
                                12.dp
                            )
                            .background(

                                color =
                                    statusColor,

                                shape =
                                    CircleShape
                            )
                )


                Spacer(
                    modifier =
                        Modifier.size(
                            8.dp
                        )
                )


                Text(

                    text =
                        statusText,

                    fontSize =
                        14.sp,

                    fontWeight =
                        FontWeight.Bold,

                    color =
                        statusColor
                )
            }


            Spacer(
                modifier =
                    Modifier.height(
                        6.dp
                    )
            )


            Text(

                text =
                    if (
                        steps >= target
                    ) {

                        "¡Completaste tu meta de actividad del día!"

                    } else {

                        "Te faltan $remainingSteps pasos para alcanzar tu meta."
                    },

                fontSize =
                    12.sp,

                color =
                    Color(0xFF64748B)
            )
        }
    }
}


// ============================================================================
// CLASIFICAR PASOS
// ============================================================================

/**
 * Semaforización inicial de pasos.
 *
 * VERDE:
 * 10,000 o más.
 *
 * AMARILLO:
 * 7,000 a 9,999.
 *
 * ROJO:
 * Menos de 7,000.
 */
fun classifySteps(
    steps: Long
): HealthTrafficLight {

    return when {

        steps >= 10_000L ->
            HealthTrafficLight.GOOD

        steps >= 7_000L ->
            HealthTrafficLight.REGULAR

        else ->
            HealthTrafficLight.LOW
    }
}


// ============================================================================
// TARJETA DE FRECUENCIA CARDÍACA
// ============================================================================

@Composable
fun HeartRateMonitoringCard(

    points: List<HeartRatePoint>,

    isHistorical: Boolean = false
) {

    // Punto seleccionado por el usuario.
    var selectedPoint by
    remember(points) {

        mutableStateOf<HeartRatePoint?>(
            null
        )
    }


    // Buscamos mínimo.
    val minimum =
        remember(points) {

            points.minByOrNull {
                it.bpm
            }
        }


    // Buscamos máximo.
    val maximum =
        remember(points) {

            points.maxByOrNull {
                it.bpm
            }
        }


    Card(

        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(
                24.dp
            ),

        colors =
            CardDefaults.cardColors(

                containerColor =
                    Color.White
            ),

        elevation =
            CardDefaults.cardElevation(

                defaultElevation =
                    4.dp
            )
    ) {

        Column(

            modifier =
                Modifier.padding(
                    18.dp
                )
        ) {


            // =================================================================
            // ENCABEZADO
            // =================================================================

            Row(

                verticalAlignment =
                    Alignment.CenterVertically
            ) {


                Box(

                    modifier =
                        Modifier
                            .size(
                                50.dp
                            )
                            .background(

                                color =
                                    Color(0xFFFFEAEA),

                                shape =
                                    CircleShape
                            ),

                    contentAlignment =
                        Alignment.Center
                ) {

                    Icon(

                        imageVector =
                            Icons.Default.Favorite,

                        contentDescription =
                            null,

                        tint =
                            Color(0xFFEF4444),

                        modifier =
                            Modifier.size(
                                28.dp
                            )
                    )
                }


                Spacer(
                    modifier =
                        Modifier.size(
                            12.dp
                        )
                )


                Column {

                    Text(

                        text =
                            "Frecuencia cardíaca",

                        fontSize =
                            18.sp,

                        fontWeight =
                            FontWeight.Bold,

                        color =
                            Color(0xFF0F172A)
                    )


                    Text(

                        text =
                            if (
                                isHistorical
                            ) {

                                "Último día con mediciones disponibles"

                            } else {

                                "Mediciones registradas hoy"
                            },

                        fontSize =
                            12.sp,

                        color =
                            Color(0xFF64748B)
                    )
                }
            }


            Spacer(
                modifier =
                    Modifier.height(
                        18.dp
                    )
            )


            // =================================================================
            // SIN DATOS
            // =================================================================

            if (points.isEmpty()) {

                Text(

                    text =
                        "No hay mediciones de frecuencia cardíaca disponibles en los últimos 30 días.",

                    fontSize =
                        13.sp,

                    color =
                        Color(0xFF64748B),

                    textAlign =
                        TextAlign.Center,

                    modifier =
                        Modifier.fillMaxWidth()
                )

                return@Column
            }


            // =================================================================
            // AVISO DE DATOS HISTÓRICOS
            // =================================================================

            if (
                isHistorical
            ) {

                val historicalDate =
                    points
                        .first()
                        .time
                        .atZone(
                            ZoneId.systemDefault()
                        )
                        .format(
                            DateTimeFormatter.ofPattern(
                                "dd MMM yyyy",
                                Locale(
                                    "es",
                                    "MX"
                                )
                            )
                        )


                Text(

                    text =
                        "No hay mediciones de hoy. Mostrando datos del $historicalDate.",

                    fontSize =
                        11.sp,

                    color =
                        Color(0xFF64748B),

                    textAlign =
                        TextAlign.Center,

                    modifier =
                        Modifier.fillMaxWidth()
                )


                Spacer(
                    modifier =
                        Modifier.height(
                            10.dp
                        )
                )
            }


            // =================================================================
            // PUNTO SELECCIONADO
            // =================================================================

            selectedPoint?.let {
                    point ->


                SelectedHeartRateCard(
                    point = point
                )


                Spacer(
                    modifier =
                        Modifier.height(
                            12.dp
                        )
                )
            }


            // =================================================================
            // GRÁFICA
            // =================================================================

            HeartRateChart(

                points =
                    points,

                selectedPoint =
                    selectedPoint,

                onPointSelected = {
                        point ->

                    selectedPoint =
                        point
                }
            )


            Spacer(
                modifier =
                    Modifier.height(
                        8.dp
                    )
            )


            Text(

                text =
                    "Toca o arrastra el dedo sobre la gráfica para revisar cada medición.",

                fontSize =
                    11.sp,

                color =
                    Color(0xFF64748B),

                textAlign =
                    TextAlign.Center,

                modifier =
                    Modifier.fillMaxWidth()
            )


            Spacer(
                modifier =
                    Modifier.height(
                        18.dp
                    )
            )


            // =================================================================
            // MÍNIMO / MÁXIMO
            // =================================================================

            Row(

                modifier =
                    Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.spacedBy(
                        12.dp
                    )
            ) {


                HeartRateExtremeCard(

                    modifier =
                        Modifier.weight(
                            1f
                        ),

                    title =
                        "Mínimo",

                    point =
                        minimum,

                    color =
                        Color(0xFF0284C7)
                )


                HeartRateExtremeCard(

                    modifier =
                        Modifier.weight(
                            1f
                        ),

                    title =
                        "Máximo",

                    point =
                        maximum,

                    color =
                        Color(0xFFEF4444)
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
                    if (
                        isHistorical
                    ) {

                        "${points.size} mediciones del último día disponible"

                    } else {

                        "${points.size} mediciones registradas hoy"
                    },

                fontSize =
                    11.sp,

                color =
                    Color(0xFF64748B),

                textAlign =
                    TextAlign.Center,

                modifier =
                    Modifier.fillMaxWidth()
            )


            Spacer(
                modifier =
                    Modifier.height(
                        8.dp
                    )
            )


            Text(

                text =
                    "La semaforización es orientativa y no representa un diagnóstico médico.",

                fontSize =
                    10.sp,

                color =
                    Color(0xFF94A3B8),

                textAlign =
                    TextAlign.Center,

                modifier =
                    Modifier.fillMaxWidth()
            )
        }
    }
}


// ============================================================================
// GRÁFICA INTERACTIVA
// ============================================================================

@Composable
fun HeartRateChart(

    points: List<HeartRatePoint>,

    selectedPoint: HeartRatePoint?,

    onPointSelected:
        (HeartRatePoint) -> Unit
) {

    // Ancho real de la gráfica.
    var chartWidthPx by
    remember {

        mutableIntStateOf(
            1
        )
    }


    /**
     * Convierte la posición horizontal del dedo
     * en el punto de frecuencia cardiaca más cercano.
     */
    fun selectPointFromX(
        x: Float
    ) {

        if (
            points.isEmpty() ||
            chartWidthPx <= 0
        ) {

            return
        }


        val clampedX =
            x.coerceIn(
                0f,
                chartWidthPx.toFloat()
            )


        val percentage =
            clampedX /
                    chartWidthPx.toFloat()


        val index =
            (
                    percentage *
                            (points.size - 1)
                    )
                .toInt()
                .coerceIn(
                    0,
                    points.lastIndex
                )


        onPointSelected(
            points[index]
        )
    }


    Canvas(

        modifier =
            Modifier
                .fillMaxWidth()
                .height(
                    230.dp
                )

                // Guardamos el ancho real.
                .onSizeChanged {

                    chartWidthPx =
                        it.width
                }

                // ------------------------------------------------------------
                // TOCAR
                // ------------------------------------------------------------

                .pointerInput(points) {

                    detectTapGestures {
                            offset ->

                        selectPointFromX(
                            offset.x
                        )
                    }
                }

                // ------------------------------------------------------------
                // ARRASTRAR
                // ------------------------------------------------------------

                .pointerInput(points) {

                    detectDragGestures(

                        onDragStart = {
                                offset ->

                            selectPointFromX(
                                offset.x
                            )
                        },

                        onDrag = {
                                change,
                                _ ->

                            selectPointFromX(
                                change.position.x
                            )
                        }
                    )
                }

    ) {


        if (
            points.size < 2
        ) {

            return@Canvas
        }


        // ====================================================================
        // MÁXIMO Y MÍNIMO DE ESCALA
        // ====================================================================

        val minBpm =
            points
                .minOf {
                    it.bpm
                }
                .toFloat()


        val maxBpm =
            points
                .maxOf {
                    it.bpm
                }
                .toFloat()


        // Agregamos espacio vertical.
        val graphMin =
            (minBpm - 10f)
                .coerceAtLeast(
                    30f
                )


        val graphMax =
            maxBpm + 10f


        val range =
            (graphMax - graphMin)
                .coerceAtLeast(
                    1f
                )


        // ====================================================================
        // LÍNEAS HORIZONTALES DE REFERENCIA
        // ====================================================================

        repeat(
            4
        ) {
                index ->


            val y =
                size.height *
                        index /
                        3f


            drawLine(

                color =
                    Color(0xFFE2E8F0),

                start =
                    Offset(
                        0f,
                        y
                    ),

                end =
                    Offset(
                        size.width,
                        y
                    ),

                strokeWidth =
                    1.5f
            )
        }


        // ====================================================================
        // FUNCIÓN PARA OBTENER X
        // ====================================================================

        fun pointX(
            index: Int
        ): Float {

            return if (
                points.size <= 1
            ) {

                0f

            } else {

                index.toFloat() /
                        points.lastIndex.toFloat() *
                        size.width
            }
        }


        // ====================================================================
        // FUNCIÓN PARA OBTENER Y
        // ====================================================================

        fun pointY(
            bpm: Long
        ): Float {

            val normalized =
                (
                        bpm.toFloat() -
                                graphMin
                        ) /
                        range


            return size.height -
                    (
                            normalized *
                                    size.height
                            )
        }


        // ====================================================================
        // DIBUJAMOS LA LÍNEA POR SEGMENTOS
        // ====================================================================

        for (
        index in
        0 until points.lastIndex
        ) {

            val current =
                points[index]


            val next =
                points[index + 1]


            val start =
                Offset(

                    x =
                        pointX(
                            index
                        ),

                    y =
                        pointY(
                            current.bpm
                        )
                )


            val end =
                Offset(

                    x =
                        pointX(
                            index + 1
                        ),

                    y =
                        pointY(
                            next.bpm
                        )
                )


            // Color según la clasificación del punto.
            val segmentColor =
                heartRateColor(
                    current.bpm
                )


            drawLine(

                color =
                    segmentColor,

                start =
                    start,

                end =
                    end,

                strokeWidth =
                    6f,

                cap =
                    StrokeCap.Round
            )
        }


        // ====================================================================
        // PUNTOS DE MEDICIÓN
        // ====================================================================

        points.forEachIndexed {
                index,
                point ->


            val position =
                Offset(

                    x =
                        pointX(
                            index
                        ),

                    y =
                        pointY(
                            point.bpm
                        )
                )


            drawCircle(

                color =
                    heartRateColor(
                        point.bpm
                    ),

                radius =
                    5f,

                center =
                    position
            )
        }


        // ====================================================================
        // PUNTO SELECCIONADO
        // ====================================================================

        selectedPoint?.let {
                selected ->


            val selectedIndex =
                points.indexOf(
                    selected
                )


            if (
                selectedIndex >= 0
            ) {

                val x =
                    pointX(
                        selectedIndex
                    )


                val y =
                    pointY(
                        selected.bpm
                    )


                // Línea vertical.
                drawLine(

                    color =
                        Color(0xFF334155),

                    start =
                        Offset(
                            x,
                            0f
                        ),

                    end =
                        Offset(
                            x,
                            size.height
                        ),

                    strokeWidth =
                        2f
                )


                // Círculo exterior.
                drawCircle(

                    color =
                        Color.White,

                    radius =
                        12f,

                    center =
                        Offset(
                            x,
                            y
                        )
                )


                // Círculo interior.
                drawCircle(

                    color =
                        heartRateColor(
                            selected.bpm
                        ),

                    radius =
                        8f,

                    center =
                        Offset(
                            x,
                            y
                        )
                )
            }
        }
    }
}


// ============================================================================
// TARJETA DEL PUNTO SELECCIONADO
// ============================================================================

@Composable
fun SelectedHeartRateCard(
    point: HeartRatePoint
) {

    val color =
        heartRateColor(
            point.bpm
        )


    val status =
        heartRateStatusText(
            point.bpm
        )


    Card(

        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(
                18.dp
            ),

        colors =
            CardDefaults.cardColors(

                containerColor =
                    Color(0xFFF8FAFC)
            )
    ) {

        Row(

            modifier =
                Modifier.padding(
                    14.dp
                ),

            verticalAlignment =
                Alignment.CenterVertically
        ) {


            Box(

                modifier =
                    Modifier
                        .size(
                            12.dp
                        )
                        .background(

                            color =
                                color,

                            shape =
                                CircleShape
                        )
            )


            Spacer(
                modifier =
                    Modifier.size(
                        10.dp
                    )
            )


            Column(

                modifier =
                    Modifier.weight(
                        1f
                    )
            ) {

                Text(

                    text =
                        "${point.bpm} bpm",

                    fontSize =
                        20.sp,

                    fontWeight =
                        FontWeight.Bold,

                    color =
                        Color(0xFF0F172A)
                )


                Text(

                    text =
                        formatHeartRateTime(
                            point
                        ),

                    fontSize =
                        12.sp,

                    color =
                        Color(0xFF64748B)
                )
            }


            Text(

                text =
                    status,

                fontSize =
                    12.sp,

                fontWeight =
                    FontWeight.Bold,

                color =
                    color
            )
        }
    }
}


// ============================================================================
// TARJETAS MÍNIMO / MÁXIMO
// ============================================================================

@Composable
fun HeartRateExtremeCard(

    modifier: Modifier = Modifier,

    title: String,

    point: HeartRatePoint?,

    color: Color
) {

    Card(

        modifier =
            modifier,

        shape =
            RoundedCornerShape(
                20.dp
            ),

        colors =
            CardDefaults.cardColors(

                containerColor =
                    Color(0xFFF8FAFC)
            )
    ) {

        Column(

            modifier =
                Modifier.padding(
                    14.dp
                ),

            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            Text(

                text =
                    title,

                fontSize =
                    12.sp,

                fontWeight =
                    FontWeight.Bold,

                color =
                    Color(0xFF64748B)
            )


            Spacer(
                modifier =
                    Modifier.height(
                        5.dp
                    )
            )


            Text(

                text =
                    point?.let {
                        "${it.bpm} bpm"
                    }
                        ?: "--",

                fontSize =
                    20.sp,

                fontWeight =
                    FontWeight.Bold,

                color =
                    color
            )


            Spacer(
                modifier =
                    Modifier.height(
                        3.dp
                    )
            )


            Text(

                text =
                    point?.let {

                        formatHeartRateTime(
                            it
                        )

                    } ?: "--",

                fontSize =
                    11.sp,

                color =
                    Color(0xFF64748B)
            )
        }
    }
}


// ============================================================================
// FORMATEAR HORA
// ============================================================================

/**
 * Convierte Instant en una hora fácil de leer.
 *
 * Ejemplo:
 *
 * 2026-08-31T17:42:00Z
 *
 * podría mostrarse localmente como:
 *
 * 12:42
 */
fun formatHeartRateTime(
    point: HeartRatePoint
): String {

    val formatter =
        DateTimeFormatter.ofPattern(
            "HH:mm"
        )


    return point.time
        .atZone(
            ZoneId.systemDefault()
        )
        .format(
            formatter
        )
}



// ============================================================================
// FORMATEAR FECHA Y HORA
// ============================================================================

fun formatHeartRateDateTime(
    point: HeartRatePoint
): String {

    val formatter =
        DateTimeFormatter.ofPattern(
            "dd MMM · HH:mm",
            Locale(
                "es",
                "MX"
            )
        )

    return point.time
        .atZone(
            ZoneId.systemDefault()
        )
        .format(
            formatter
        )
}


// ============================================================================
// COLOR DE SEMAFORIZACIÓN DE FC
// ============================================================================

/**
 * CLASIFICACIÓN INICIAL.
 *
 * IMPORTANTE:
 *
 * Esta semaforización todavía es orientativa.
 *
 * Más adelante la mejoraremos utilizando:
 *
 * - Actividad física.
 * - Pasos.
 * - Duración del episodio.
 * - Frecuencia basal.
 * - Cambios bruscos.
 *
 * Por ahora sirve para comprobar visualmente
 * que la gráfica puede cambiar de color.
 */
fun heartRateColor(
    bpm: Long
): Color {

    return when {

        // Frecuencia muy baja.
        bpm < 40 -> {

            Color(0xFFDC2626)
        }


        // Frecuencia baja.
        bpm < 50 -> {

            Color(0xFFF59E0B)
        }


        // Rango inicialmente estable.
        bpm <= 100 -> {

            Color(0xFF22C55E)
        }


        // Frecuencia elevada.
        bpm <= 130 -> {

            Color(0xFFF59E0B)
        }


        // Frecuencia muy elevada.
        else -> {

            Color(0xFFDC2626)
        }
    }
}


// ============================================================================
// TEXTO DE ESTADO DE FC
// ============================================================================

fun heartRateStatusText(
    bpm: Long
): String {

    return when {

        bpm < 40 ->
            "Revisar"

        bpm < 50 ->
            "Baja"

        bpm <= 100 ->
            "Estable"

        bpm <= 130 ->
            "Elevada"

        else ->
            "Revisar"
    }
}


// ============================================================================
// COLOR GENERAL DE SEMÁFORO
// ============================================================================

fun trafficLightColor(
    status: HealthTrafficLight
): Color {

    return when (status) {

        HealthTrafficLight.GOOD ->

            Color(0xFF22C55E)


        HealthTrafficLight.REGULAR ->

            Color(0xFFF59E0B)


        HealthTrafficLight.LOW ->

            Color(0xFFDC2626)
    }
}