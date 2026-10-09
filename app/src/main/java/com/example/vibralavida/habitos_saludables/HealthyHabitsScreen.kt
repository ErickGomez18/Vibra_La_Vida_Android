package com.example.vibralavida.habitos_saludables
import com.example.vibralavida.R
import com.example.vibralavida.api.modelos.HabitosProgresoRequest
import com.example.vibralavida.api.modelos.HabitosIntentoRequest
import com.example.vibralavida.api.HabitosProgresoRepository
import com.example.vibralavida.trastornos_ritmo.SleepSummary
import com.example.vibralavida.pantallas_principales.BackgroundBlurCircle
import com.example.vibralavida.trastornos_ritmo.HealthConnectManager
import com.example.vibralavida.backgroundGradient

// ============================================================================
// ANDROID
// ============================================================================

import android.content.Context
import android.speech.tts.TextToSpeech


// ============================================================================
// COMPOSE FOUNDATION
// ============================================================================

import androidx.compose.foundation.Image
import androidx.compose.foundation.background

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll


// ============================================================================
// ICONOS
// ============================================================================

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MonitorHeart
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.VolumeUp


// ============================================================================
// MATERIAL 3
// ============================================================================

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text


// ============================================================================
// ESTADOS
// ============================================================================

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue


// ============================================================================
// UI
// ============================================================================

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


// ============================================================================
// FECHA Y HORA
// ============================================================================

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale


// ============================================================================
// ESTADO DE DURACIÓN DEL SUEÑO
// ============================================================================

/**
 * Representa el semáforo correspondiente
 * únicamente a la duración del sueño.
 */
enum class SleepTrafficLight {

    GOOD,

    REGULAR,

    LOW
}


// ============================================================================
// RESULTADO COMBINADO DE SUEÑO
// ============================================================================

/**
 * Representa el resultado después de combinar:
 *
 * - Duración del sueño.
 * - Resultado AIS.
 *
 * Más adelante agregaremos:
 *
 * - Ronquidos.
 * - Calidad de las etapas.
 * - Movimientos.
 * - Sleep Score.
 */
enum class CombinedSleepStatus {

    GOOD,

    REGULAR,

    LOW
}


// ============================================================================
// FASE 1 - CONTENIDO EDUCATIVO DE HÁBITOS SALUDABLES
// ============================================================================
//
// En esta primera fase agregamos:
//
// - Nuby dentro del contenido.
// - Temas educativos fáciles de explorar.
// - Ejemplos cotidianos.
// - Mensajes clave.
// - Text to Speech.
//
// Todavía NO agregamos XP, niveles ni Firebase.
// Eso quedará para las siguientes fases.
// ============================================================================

private data class HealthyLearningTopic(

    val emoji:
        String,

    val title:
        String,

    val subtitle:
        String,

    val paragraphs:
        List<String>,

    val example:
        String,

    val keyMessage:
        String,

    val nubyRes:
        Int
)


private val healthyLearningTopics =
    listOf(

        HealthyLearningTopic(

            emoji =
                "🥗",

            title =
                "Alimentación equilibrada",

            subtitle =
                "Aprende a combinar alimentos sin dietas extremas.",

            paragraphs =
                listOf(
                    "Una alimentación saludable busca variedad y equilibrio. No se trata de prohibir alimentos, sino de aprender a elegir y combinar mejor.",
                    "Frutas, verduras, cereales, leguminosas y fuentes de proteína pueden formar parte de una alimentación equilibrada.",
                    "Comer de forma regular y prestar atención al hambre y la saciedad puede ayudar a construir una relación más saludable con la comida."
                ),

            example =
                "En lugar de pensar que una comida debe ser 'perfecta', puedes intentar incluir varios grupos de alimentos y ajustar las porciones a tus necesidades.",

            keyMessage =
                "La salud se construye con hábitos sostenibles, no con restricciones extremas.",

            nubyRes =
                R.drawable.nuby_bowl
        ),


        HealthyLearningTopic(

            emoji =
                "🏃",

            title =
                "Actividad física",

            subtitle =
                "Moverte también cuenta fuera del gimnasio.",

            paragraphs =
                listOf(
                    "La actividad física incluye caminar, bailar, andar en bicicleta, practicar un deporte y otras formas de movimiento.",
                    "Moverse con regularidad beneficia al corazón, músculos, huesos, metabolismo y bienestar emocional.",
                    "No necesitas empezar con rutinas muy intensas. Incrementar el movimiento poco a poco puede ser una forma más sostenible de crear el hábito."
                ),

            example =
                "Caminar, subir escaleras o bailar durante el día también suma actividad física.",

            keyMessage =
                "El mejor movimiento es el que puedes realizar de manera segura y constante.",

            nubyRes =
                R.drawable.nuby_caminando
        ),


        HealthyLearningTopic(

            emoji =
                "💧",

            title =
                "Hidratación",

            subtitle =
                "El agua participa en muchas funciones del cuerpo.",

            paragraphs =
                listOf(
                    "Mantener una hidratación adecuada ayuda al funcionamiento normal del organismo.",
                    "Las necesidades de líquidos pueden variar según el clima, actividad física, alimentación y características personales.",
                    "La sed es una señal útil, pero durante actividad física, calor intenso o enfermedad puede ser necesario prestar todavía más atención a la hidratación."
                ),

            example =
                "Llevar una botella de agua puede ayudarte a recordar beber durante clases, trabajo o actividad física.",

            keyMessage =
                "No existe una cantidad idéntica de agua para todas las personas y situaciones.",

            nubyRes =
                R.drawable.nuby_agua
        ),


        HealthyLearningTopic(

            emoji =
                "😴",

            title =
                "Sueño saludable",

            subtitle =
                "Dormir también es parte del cuidado de la salud.",

            paragraphs =
                listOf(
                    "El sueño participa en procesos de recuperación, memoria, regulación emocional y funcionamiento físico.",
                    "Mantener horarios relativamente regulares puede ayudar al organismo a reconocer cuándo es momento de descansar.",
                    "Reducir estímulos intensos antes de dormir y mantener un ambiente cómodo puede favorecer una rutina de sueño."
                ),

            example =
                "Si te cuesta dormir, puedes probar una rutina tranquila antes de acostarte y disminuir el uso de pantallas justo antes de dormir.",

            keyMessage =
                "Dormir bien no es tiempo perdido: forma parte del cuidado diario.",

            nubyRes =
                R.drawable.nuby_dormido
        ),


        HealthyLearningTopic(

            emoji =
                "🧠",

            title =
                "Estrés y bienestar",

            subtitle =
                "Cuidar la mente también es cuidar el cuerpo.",

            paragraphs =
                listOf(
                    "Sentir estrés en algunos momentos es normal, pero cuando es intenso o persistente puede afectar el bienestar.",
                    "Descansar, hablar con alguien de confianza, organizar actividades y realizar movimiento físico pueden ser estrategias útiles.",
                    "Si el malestar emocional es intenso, dura mucho tiempo o interfiere con la vida cotidiana, es importante buscar apoyo profesional."
                ),

            example =
                "Antes de un examen puedes sentir estrés. Organizar el estudio, dormir y hacer pequeñas pausas puede ayudarte a manejar mejor esa situación.",

            keyMessage =
                "Pedir ayuda cuando la necesitas también es una forma de autocuidado.",

            nubyRes =
                R.drawable.nuby_abrazo
        ),


        HealthyLearningTopic(

            emoji =
                "📱",

            title =
                "Hábitos digitales",

            subtitle =
                "La tecnología también puede formar parte de una rutina equilibrada.",

            paragraphs =
                listOf(
                    "El celular, videojuegos y redes sociales pueden ser útiles y entretenidos, pero también pueden desplazar sueño, movimiento o convivencia.",
                    "Hacer pausas, cambiar de postura y reservar momentos sin pantalla puede ayudar a equilibrar el tiempo digital.",
                    "Antes de dormir, reducir contenido muy estimulante puede facilitar una rutina más tranquila."
                ),

            example =
                "Puedes establecer momentos concretos para dejar el teléfono, por ejemplo durante las comidas o poco antes de dormir.",

            keyMessage =
                "No se trata de abandonar la tecnología, sino de usarla sin desplazar otros hábitos importantes.",

            nubyRes =
                R.drawable.nuby_idea
        )
    )




// ============================================================================
// FASE 3 - GAMIFICACIÓN + PREGUNTAS ADAPTATIVAS
// ============================================================================

private data class HealthyLearningProgress(

    val xp:
        Int = 0,

    val completedTopics:
        Set<String> = emptySet(),

    val attempts:
        Int = 0,

    val bestScore:
        Int = 0,

    val topicsToReinforce:
        Set<String> = emptySet()
)


private object HealthyLearningProgressLocal {

    private const val PREFS =
        "healthy_habits_learning_progress"

    private const val KEY_XP =
        "xp"

    private const val KEY_TOPICS =
        "completed_topics"

    private const val KEY_ATTEMPTS =
        "attempts"

    private const val KEY_BEST_SCORE =
        "best_score"

    private const val KEY_REINFORCE =
        "topics_to_reinforce"


    fun load(
        context:
            Context
    ): HealthyLearningProgress {

        val preferences =
            context.getSharedPreferences(
                PREFS,
                Context.MODE_PRIVATE
            )


        return HealthyLearningProgress(

            xp =
                preferences.getInt(
                    KEY_XP,
                    0
                ),

            completedTopics =
                preferences
                    .getStringSet(
                        KEY_TOPICS,
                        emptySet()
                    )
                    ?.toSet()
                    ?: emptySet(),

            attempts =
                preferences.getInt(
                    KEY_ATTEMPTS,
                    0
                ),

            bestScore =
                preferences.getInt(
                    KEY_BEST_SCORE,
                    0
                ),

            topicsToReinforce =
                preferences
                    .getStringSet(
                        KEY_REINFORCE,
                        emptySet()
                    )
                    ?.toSet()
                    ?: emptySet()
        )
    }


    fun save(
        context:
            Context,

        progress:
            HealthyLearningProgress
    ) {

        context
            .getSharedPreferences(
                PREFS,
                Context.MODE_PRIVATE
            )
            .edit()
            .putInt(
                KEY_XP,
                progress.xp
            )
            .putStringSet(
                KEY_TOPICS,
                progress.completedTopics
            )
            .putInt(
                KEY_ATTEMPTS,
                progress.attempts
            )
            .putInt(
                KEY_BEST_SCORE,
                progress.bestScore
            )
            .putStringSet(
                KEY_REINFORCE,
                progress.topicsToReinforce
            )
            .apply()
    }
}


// ============================================================================
// NIVELES
// ============================================================================

private fun healthyLevelName(
    xp:
        Int
): String {

    return when {

        xp >= 110 ->
            "Embajador del bienestar"

        xp >= 80 ->
            "Guardián saludable"

        xp >= 50 ->
            "Explorador de hábitos"

        xp >= 20 ->
            "Constructor de bienestar"

        else ->
            "Primer paso"
    }
}


private fun healthyLevelProgress(
    xp:
        Int
): Float {

    val start:
        Int

    val next:
        Int


    when {

        xp >= 110 -> {
            start = 110
            next = 140
        }

        xp >= 80 -> {
            start = 80
            next = 110
        }

        xp >= 50 -> {
            start = 50
            next = 80
        }

        xp >= 20 -> {
            start = 20
            next = 50
        }

        else -> {
            start = 0
            next = 20
        }
    }


    return (
        (xp - start).toFloat() /
            (next - start).toFloat()
    ).coerceIn(
        0f,
        1f
    )
}


private fun healthyXpToNextLevel(
    xp:
        Int
): Int {

    val next =
        when {

            xp >= 110 ->
                140

            xp >= 80 ->
                110

            xp >= 50 ->
                80

            xp >= 20 ->
                50

            else ->
                20
        }


    return (
        next - xp
    ).coerceAtLeast(
        0
    )
}


// ============================================================================
// INSIGNIAS
// ============================================================================

private fun healthyBadges(
    progress:
        HealthyLearningProgress
): List<String> {

    val badges =
        mutableListOf<String>()


    if (
        progress.completedTopics
            .isNotEmpty()
    ) {
        badges.add(
            "🌱 Primer hábito"
        )
    }


    if (
        progress.completedTopics
            .contains(
                "Actividad física"
            )
    ) {
        badges.add(
            "🏃 En movimiento"
        )
    }


    if (
        progress.completedTopics
            .contains(
                "Sueño saludable"
            )
    ) {
        badges.add(
            "🌙 Guardián del descanso"
        )
    }


    if (
        progress.completedTopics
            .contains(
                "Hidratación"
            )
    ) {
        badges.add(
            "💧 Hidratación consciente"
        )
    }


    if (
        progress.bestScore >= 8
    ) {
        badges.add(
            "🧠 Mente saludable"
        )
    }


    if (
        progress.attempts >= 3
    ) {
        badges.add(
            "⭐ Constancia Nuby"
        )
    }


    if (
        progress.completedTopics.size >=
            healthyLearningTopics.size &&
        progress.bestScore >= 8
    ) {
        badges.add(
            "🏆 Embajador del bienestar"
        )
    }


    return badges
}


// ============================================================================
// DIFICULTAD ADAPTATIVA
// ============================================================================

private enum class HealthyDifficulty(
    val label:
        String
) {

    BASIC(
        "Básico"
    ),

    INTERMEDIATE(
        "Intermedio"
    ),

    ADVANCED(
        "Reto Nuby"
    )
}


private fun healthyDifficultyFor(
    progress:
        HealthyLearningProgress
): HealthyDifficulty {

    return when {

        progress.bestScore >= 8 ->
            HealthyDifficulty.ADVANCED

        progress.bestScore >= 5 ->
            HealthyDifficulty.INTERMEDIATE

        else ->
            HealthyDifficulty.BASIC
    }
}


// ============================================================================
// PREGUNTAS
// ============================================================================

private data class HealthyLearningQuestion(

    val topic:
        String,

    val difficulty:
        HealthyDifficulty,

    val question:
        String,

    val options:
        List<String>,

    val correctAnswer:
        Int,

    val explanation:
        String
)


private data class PreparedHealthyQuestion(

    val topic:
        String,

    val difficulty:
        HealthyDifficulty,

    val question:
        String,

    val options:
        List<String>,

    val correctAnswer:
        Int,

    val explanation:
        String
)


private data class HealthyQuizResult(

    val score:
        Int,

    val total:
        Int,

    val difficulty:
        String,

    val failedTopics:
        Set<String>
)


private fun HealthyLearningQuestion.prepare():
    PreparedHealthyQuestion {

    val correctText =
        options[
            correctAnswer
        ]


    val shuffledOptions =
        options.shuffled()


    return PreparedHealthyQuestion(

        topic =
            topic,

        difficulty =
            difficulty,

        question =
            question,

        options =
            shuffledOptions,

        correctAnswer =
            shuffledOptions.indexOf(
                correctText
            ),

        explanation =
            explanation
    )
}


private fun buildHealthyQuiz(
    progress:
        HealthyLearningProgress
): List<PreparedHealthyQuestion> {

    val difficulty =
        healthyDifficultyFor(
            progress
        )


    val pool =
        healthyLearningQuestions
            .filter {
                it.difficulty ==
                    difficulty
            }


    val reinforced =
        if (
            progress.topicsToReinforce
                .isNotEmpty()
        ) {

            pool
                .filter {
                    progress
                        .topicsToReinforce
                        .contains(
                            it.topic
                        )
                }
                .shuffled()
                .take(
                    4
                )

        } else {

            emptyList()
        }


    val remaining =
        pool
            .filterNot {
                reinforced.contains(
                    it
                )
            }
            .shuffled()
            .take(
                10 -
                    reinforced.size
            )


    return (
        reinforced +
            remaining
    )
        .shuffled()
        .map {
            it.prepare()
        }
}


private val healthyLearningQuestions =
    listOf(

        // ====================================================================
        // BÁSICO
        // ====================================================================

        HealthyLearningQuestion(
            topic = "Alimentación equilibrada",
            difficulty = HealthyDifficulty.BASIC,
            question = "¿Qué describe mejor una alimentación saludable?",
            options = listOf(
                "Variedad y equilibrio sostenibles",
                "Eliminar todos los carbohidratos",
                "Saltarse comidas todos los días",
                "Seguir dietas extremas"
            ),
            correctAnswer = 0,
            explanation = "Una alimentación saludable busca variedad y equilibrio, no restricciones extremas."
        ),

        HealthyLearningQuestion(
            topic = "Actividad física",
            difficulty = HealthyDifficulty.BASIC,
            question = "¿Cuál actividad también cuenta como movimiento físico?",
            options = listOf(
                "Caminar o bailar",
                "Solo levantar pesas",
                "Solo correr maratones",
                "Ninguna actividad cotidiana"
            ),
            correctAnswer = 0,
            explanation = "Caminar, bailar, subir escaleras y otras actividades cotidianas también suman movimiento."
        ),

        HealthyLearningQuestion(
            topic = "Hidratación",
            difficulty = HealthyDifficulty.BASIC,
            question = "¿Todas las personas necesitan exactamente la misma cantidad de agua?",
            options = listOf(
                "No",
                "Sí",
                "Solo los adolescentes",
                "Solo quienes hacen ejercicio"
            ),
            correctAnswer = 0,
            explanation = "Las necesidades de líquidos cambian según clima, actividad y características personales."
        ),

        HealthyLearningQuestion(
            topic = "Sueño saludable",
            difficulty = HealthyDifficulty.BASIC,
            question = "¿Por qué el sueño es importante?",
            options = listOf(
                "Participa en recuperación, memoria y bienestar",
                "Solo sirve para descansar los ojos",
                "No afecta la salud",
                "Siempre puede sustituirse con cafeína"
            ),
            correctAnswer = 0,
            explanation = "Dormir participa en recuperación física, memoria, regulación emocional y otras funciones."
        ),

        HealthyLearningQuestion(
            topic = "Estrés y bienestar",
            difficulty = HealthyDifficulty.BASIC,
            question = "Si el estrés es intenso y persiste, ¿qué puede ser recomendable?",
            options = listOf(
                "Buscar apoyo profesional",
                "Ignorarlo siempre",
                "Dormir cada vez menos",
                "Aislarse de todas las personas"
            ),
            correctAnswer = 0,
            explanation = "Cuando el malestar es intenso o afecta la vida diaria, buscar apoyo profesional es importante."
        ),

        HealthyLearningQuestion(
            topic = "Hábitos digitales",
            difficulty = HealthyDifficulty.BASIC,
            question = "¿Qué significa tener hábitos digitales equilibrados?",
            options = listOf(
                "Usar tecnología sin desplazar sueño, movimiento o convivencia",
                "No utilizar nunca un teléfono",
                "Usar pantallas toda la noche",
                "Revisar redes durante cada comida"
            ),
            correctAnswer = 0,
            explanation = "La meta es utilizar la tecnología sin desplazar otros hábitos importantes."
        ),

        HealthyLearningQuestion(
            topic = "Actividad física",
            difficulty = HealthyDifficulty.BASIC,
            question = "¿Qué forma de comenzar actividad física suele ser más sostenible?",
            options = listOf(
                "Aumentar el movimiento poco a poco",
                "Entrenar al máximo desde el primer día",
                "Ignorar cualquier dolor",
                "Hacer ejercicio solo una vez al mes"
            ),
            correctAnswer = 0,
            explanation = "Comenzar gradualmente puede ayudar a construir un hábito más seguro y sostenible."
        ),

        HealthyLearningQuestion(
            topic = "Alimentación equilibrada",
            difficulty = HealthyDifficulty.BASIC,
            question = "¿Cuál idea sobre la alimentación es más adecuada?",
            options = listOf(
                "No es necesario que cada comida sea perfecta",
                "Un alimento define toda tu salud",
                "Nunca debes escuchar tus señales de hambre",
                "Todos necesitan la misma dieta"
            ),
            correctAnswer = 0,
            explanation = "La salud depende de patrones sostenidos; una sola comida no define todo."
        ),

        HealthyLearningQuestion(
            topic = "Sueño saludable",
            difficulty = HealthyDifficulty.BASIC,
            question = "¿Qué puede favorecer una rutina de sueño?",
            options = listOf(
                "Horarios relativamente regulares",
                "Pantallas intensas hasta quedarse dormido",
                "Cafeína justo antes de dormir",
                "Cambiar completamente de horario cada noche"
            ),
            correctAnswer = 0,
            explanation = "Los horarios relativamente regulares pueden ayudar al organismo a reconocer cuándo descansar."
        ),

        HealthyLearningQuestion(
            topic = "Hábitos digitales",
            difficulty = HealthyDifficulty.BASIC,
            question = "¿Cuál es un ejemplo de pausa digital saludable?",
            options = listOf(
                "Dejar el teléfono durante una comida",
                "Dormir con videos reproduciéndose toda la noche",
                "Revisar redes mientras se cruza la calle",
                "Usar varias pantallas al mismo tiempo siempre"
            ),
            correctAnswer = 0,
            explanation = "Reservar momentos sin pantalla puede ayudar a equilibrar la rutina."
        ),

        // ====================================================================
        // INTERMEDIO
        // ====================================================================

        HealthyLearningQuestion(
            topic = "Alimentación equilibrada",
            difficulty = HealthyDifficulty.INTERMEDIATE,
            question = "Una persona quiere mejorar su alimentación sin hacer una dieta extrema. ¿Qué opción es más razonable?",
            options = listOf(
                "Hacer cambios pequeños y mantener variedad",
                "Eliminar grupos completos de alimentos sin indicación",
                "Saltarse comidas para compensar",
                "Seguir reglas rígidas encontradas en redes"
            ),
            correctAnswer = 0,
            explanation = "Los cambios graduales y sostenibles suelen ser más útiles que las restricciones extremas."
        ),

        HealthyLearningQuestion(
            topic = "Actividad física",
            difficulty = HealthyDifficulty.INTERMEDIATE,
            question = "Si una persona pasa muchas horas sentada, ¿qué estrategia puede sumar movimiento?",
            options = listOf(
                "Realizar pausas activas y caminar cuando sea posible",
                "Esperar al fin de semana para moverse",
                "Evitar levantarse durante el día",
                "Hacer ejercicio intenso aun con dolor"
            ),
            correctAnswer = 0,
            explanation = "Las pausas activas y el movimiento distribuido durante el día también cuentan."
        ),

        HealthyLearningQuestion(
            topic = "Hidratación",
            difficulty = HealthyDifficulty.INTERMEDIATE,
            question = "¿Qué situación puede aumentar la necesidad de prestar atención a la hidratación?",
            options = listOf(
                "Calor intenso o actividad física",
                "Estar sentado cinco minutos",
                "Leer un libro",
                "Escuchar música"
            ),
            correctAnswer = 0,
            explanation = "El calor y la actividad física pueden aumentar las pérdidas de líquidos."
        ),

        HealthyLearningQuestion(
            topic = "Sueño saludable",
            difficulty = HealthyDifficulty.INTERMEDIATE,
            question = "¿Qué combinación favorece mejor una rutina de sueño?",
            options = listOf(
                "Horario regular y ambiente tranquilo",
                "Cafeína nocturna y pantalla brillante",
                "Dormir a horas totalmente distintas cada día",
                "Ejercicio intenso justo antes de dormir siempre"
            ),
            correctAnswer = 0,
            explanation = "La regularidad y un ambiente tranquilo pueden favorecer el descanso."
        ),

        HealthyLearningQuestion(
            topic = "Estrés y bienestar",
            difficulty = HealthyDifficulty.INTERMEDIATE,
            question = "Antes de un examen, una persona se siente muy tensa. ¿Cuál estrategia es más saludable?",
            options = listOf(
                "Organizar tareas, hacer pausas y dormir",
                "No dormir para estudiar más",
                "Aislarse completamente",
                "Consumir estimulantes en exceso"
            ),
            correctAnswer = 0,
            explanation = "Organización, descanso y pausas pueden ayudar a manejar mejor el estrés."
        ),

        HealthyLearningQuestion(
            topic = "Hábitos digitales",
            difficulty = HealthyDifficulty.INTERMEDIATE,
            question = "¿Por qué puede ayudar reducir pantallas antes de dormir?",
            options = listOf(
                "Puede facilitar una rutina más tranquila",
                "Porque toda pantalla es dañina siempre",
                "Porque elimina la necesidad de dormir",
                "Porque reemplaza una rutina de descanso"
            ),
            correctAnswer = 0,
            explanation = "Reducir estímulos intensos antes de dormir puede facilitar la transición al descanso."
        ),

        HealthyLearningQuestion(
            topic = "Alimentación equilibrada",
            difficulty = HealthyDifficulty.INTERMEDIATE,
            question = "¿Qué idea refleja una relación más saludable con la comida?",
            options = listOf(
                "Escuchar señales de hambre y saciedad",
                "Sentirse culpable por cada alimento",
                "Compensar cada comida saltándose la siguiente",
                "Clasificar todos los alimentos como buenos o malos"
            ),
            correctAnswer = 0,
            explanation = "Atender señales corporales y evitar reglas rígidas puede favorecer una relación más saludable con la alimentación."
        ),

        HealthyLearningQuestion(
            topic = "Actividad física",
            difficulty = HealthyDifficulty.INTERMEDIATE,
            question = "¿Qué indica que una meta de movimiento puede ser sostenible?",
            options = listOf(
                "Se adapta a la capacidad y rutina de la persona",
                "Obliga a ignorar dolor o agotamiento",
                "Exige resultados inmediatos",
                "Solo funciona si es muy intensa"
            ),
            correctAnswer = 0,
            explanation = "Las metas sostenibles se adaptan a la persona y pueden mantenerse en el tiempo."
        ),

        HealthyLearningQuestion(
            topic = "Estrés y bienestar",
            difficulty = HealthyDifficulty.INTERMEDIATE,
            question = "¿Cuándo conviene considerar apoyo profesional por malestar emocional?",
            options = listOf(
                "Cuando es intenso, persistente o interfiere con la vida diaria",
                "Solo si otras personas lo notan",
                "Nunca",
                "Únicamente después de varios años"
            ),
            correctAnswer = 0,
            explanation = "El impacto y la persistencia del malestar son señales importantes para buscar apoyo."
        ),

        HealthyLearningQuestion(
            topic = "Hidratación",
            difficulty = HealthyDifficulty.INTERMEDIATE,
            question = "¿Cuál es una estrategia práctica para recordar hidratarse?",
            options = listOf(
                "Llevar una botella de agua",
                "Esperar siempre a tener sed intensa",
                "Evitar líquidos durante actividad física",
                "Tomar solo bebidas muy azucaradas"
            ),
            correctAnswer = 0,
            explanation = "Tener agua disponible puede facilitar una hidratación regular."
        ),

        // ====================================================================
        // RETO NUBY
        // ====================================================================

        HealthyLearningQuestion(
            topic = "Alimentación equilibrada",
            difficulty = HealthyDifficulty.ADVANCED,
            question = "Alex vio en redes una dieta que elimina varios grupos de alimentos y promete resultados rápidos. ¿Qué decisión es más responsable?",
            options = listOf(
                "Evitar cambios extremos y buscar orientación confiable si la necesita",
                "Seguirla de inmediato porque es popular",
                "Duplicar las restricciones",
                "Dejar de comer durante el día"
            ),
            correctAnswer = 0,
            explanation = "La popularidad de una dieta no garantiza seguridad. Los cambios sostenibles y la orientación profesional son preferibles."
        ),

        HealthyLearningQuestion(
            topic = "Actividad física",
            difficulty = HealthyDifficulty.ADVANCED,
            question = "Sam no puede ir al gimnasio esta semana. ¿Cuál opción mantiene mejor el enfoque de vida activa?",
            options = listOf(
                "Buscar oportunidades de caminar o moverse durante el día",
                "No moverse hasta volver al gimnasio",
                "Entrenar con dolor",
                "Duplicar el ejercicio la semana siguiente"
            ),
            correctAnswer = 0,
            explanation = "La actividad física no depende exclusivamente de un gimnasio."
        ),

        HealthyLearningQuestion(
            topic = "Hidratación",
            difficulty = HealthyDifficulty.ADVANCED,
            question = "Durante una tarde muy calurosa una persona realiza actividad física. ¿Qué concepto es más importante recordar?",
            options = listOf(
                "Las necesidades de líquidos pueden cambiar con el contexto",
                "Todas las personas deben tomar exactamente lo mismo",
                "La hidratación no importa si la actividad es corta",
                "Solo importa beber al final del día"
            ),
            correctAnswer = 0,
            explanation = "Clima, actividad y características personales modifican las necesidades de hidratación."
        ),

        HealthyLearningQuestion(
            topic = "Sueño saludable",
            difficulty = HealthyDifficulty.ADVANCED,
            question = "Una persona duerme a horas muy distintas y usa el celular hasta quedarse dormida. ¿Qué cambio inicial es más coherente?",
            options = listOf(
                "Crear una rutina más regular y reducir estímulos antes de dormir",
                "Dormir menos para cansarse más",
                "Tomar más cafeína por la tarde",
                "Cambiar todavía más el horario"
            ),
            correctAnswer = 0,
            explanation = "La regularidad y una transición tranquila pueden ayudar a construir mejores hábitos de sueño."
        ),

        HealthyLearningQuestion(
            topic = "Estrés y bienestar",
            difficulty = HealthyDifficulty.ADVANCED,
            question = "Una persona lleva semanas sintiéndose sobrepasada y ya afecta sus estudios y relaciones. ¿Qué respuesta es más adecuada?",
            options = listOf(
                "Buscar apoyo de confianza y considerar atención profesional",
                "Ocultarlo y seguir igual",
                "Dormir menos para ponerse al día",
                "Esperar indefinidamente aunque empeore"
            ),
            correctAnswer = 0,
            explanation = "Cuando el malestar es persistente e interfiere con la vida diaria, es importante buscar apoyo."
        ),

        HealthyLearningQuestion(
            topic = "Hábitos digitales",
            difficulty = HealthyDifficulty.ADVANCED,
            question = "Una persona usa redes hasta muy tarde y al día siguiente se siente cansada. ¿Qué cambio aborda mejor el problema?",
            options = listOf(
                "Definir un momento para dejar pantallas antes de dormir",
                "Aumentar el brillo del teléfono",
                "Dormir con notificaciones activas",
                "Usar dos dispositivos a la vez"
            ),
            correctAnswer = 0,
            explanation = "Establecer límites antes de dormir puede evitar que el uso digital desplace el descanso."
        ),

        HealthyLearningQuestion(
            topic = "Alimentación equilibrada",
            difficulty = HealthyDifficulty.ADVANCED,
            question = "Una comida fue diferente a lo planeado. ¿Qué pensamiento favorece un enfoque saludable?",
            options = listOf(
                "Una comida no define todo mi patrón de alimentación",
                "Arruiné completamente mi salud",
                "Debo saltarme la siguiente comida",
                "Necesito compensarlo con ejercicio intenso"
            ),
            correctAnswer = 0,
            explanation = "Los patrones sostenidos importan más que una comida aislada."
        ),

        HealthyLearningQuestion(
            topic = "Actividad física",
            difficulty = HealthyDifficulty.ADVANCED,
            question = "Una persona quiere empezar a moverse pero lleva meses inactiva. ¿Qué estrategia es más razonable?",
            options = listOf(
                "Empezar de forma gradual y adaptar la meta",
                "Intentar una rutina extrema desde el primer día",
                "Ignorar fatiga intensa",
                "Compararse con atletas"
            ),
            correctAnswer = 0,
            explanation = "Una progresión gradual facilita la adaptación y la constancia."
        ),

        HealthyLearningQuestion(
            topic = "Sueño saludable",
            difficulty = HealthyDifficulty.ADVANCED,
            question = "Si alguien duerme poco entre semana y trata de compensar de forma irregular, ¿qué hábito conviene priorizar?",
            options = listOf(
                "Construir horarios de sueño más consistentes",
                "Mantener cambios grandes de horario",
                "Usar cafeína como sustituto del sueño",
                "Eliminar por completo la actividad física"
            ),
            correctAnswer = 0,
            explanation = "La consistencia ayuda a organizar mejor la rutina de descanso."
        ),

        HealthyLearningQuestion(
            topic = "Hábitos digitales",
            difficulty = HealthyDifficulty.ADVANCED,
            question = "¿Cuál decisión muestra mejor equilibrio digital durante el estudio?",
            options = listOf(
                "Hacer pausas y limitar distracciones cuando sea necesario",
                "Responder cada notificación inmediatamente",
                "Mantener todas las redes abiertas",
                "Evitar levantarse durante horas"
            ),
            correctAnswer = 0,
            explanation = "Gestionar distracciones y hacer pausas puede ayudar a equilibrar concentración y bienestar."
        )
    )




// ============================================================================
// CONVERTIR PROGRESO LOCAL A PETICIÓN REMOTA
// ============================================================================

private fun healthyProgressToRequest(
    progress:
        HealthyLearningProgress
): HabitosProgresoRequest {

    return HabitosProgresoRequest(

        xp =
            progress.xp,

        nivel =
            healthyLevelName(
                progress.xp
            ),

        temasCompletados =
            progress.completedTopics
                .toList(),

        intentos =
            progress.attempts,

        mejorPuntaje =
            progress.bestScore,

        temasAReforzar =
            progress.topicsToReinforce
                .toList()
    )
}


// ============================================================================
// PANTALLA DE HÁBITOS SALUDABLES
// ============================================================================

@Composable
fun HealthyHabitsScreen(

    userName: String,

    userAge: String,

    sleepSurveyScore: Int?,

    onBackToMenu: () -> Unit,

    onMenuClick: () -> Unit,

    onProfileClick: () -> Unit,

    onMoodSurveyClick: () -> Unit,

    onSleepSurveyClick: () -> Unit,

    // Abre el nuevo Modo Sueño.
    onSleepModeClick: () -> Unit,

    onImcClick: () -> Unit,

    onCaloriesClick: () -> Unit,

    onCardioRiskClick: () -> Unit
) {

    // ========================================================================
    // NOMBRE DEL USUARIO
    // ========================================================================

    val firstName =
        userName
            .trim()
            .split(" ")
            .firstOrNull()
            .orEmpty()
            .ifBlank {

                "Usuario"
            }


    // ========================================================================
    // EDAD
    // ========================================================================

    val age =
        userAge.toIntOrNull()


    // ========================================================================
    // CONTEXTO
    // ========================================================================

    val context =
        LocalContext.current


    // ========================================================================
    // TEMA EDUCATIVO SELECCIONADO
    // ========================================================================

    var selectedLearningTopic by remember {

        mutableStateOf<HealthyLearningTopic?>(
            null
        )
    }


    // ========================================================================
    // PROGRESO EDUCATIVO LOCAL
    // ========================================================================

    var learningProgress by remember {

        mutableStateOf(
            HealthyLearningProgressLocal
                .load(
                    context
                )
        )
    }


    var learningSyncMessage by remember {

        mutableStateOf(
            "Sincronizando progreso..."
        )
    }


    // ========================================================================
    // SINCRONIZACIÓN INICIAL CON FIREBASE A TRAVÉS DE EXPRESS
    // ========================================================================

    LaunchedEffect(
        Unit
    ) {

        HabitosProgresoRepository
            .obtenerProgreso(

                onSuccess = {
                        remote ->

                    if (
                        remote != null
                    ) {

                        val remoteProgress =
                            HealthyLearningProgress(

                                xp =
                                    remote.xp,

                                completedTopics =
                                    remote
                                        .temasCompletados
                                        .toSet(),

                                attempts =
                                    remote.intentos,

                                bestScore =
                                    remote.mejorPuntaje,

                                topicsToReinforce =
                                    remote
                                        .temasAReforzar
                                        .toSet()
                            )


                        learningProgress =
                            remoteProgress


                        HealthyLearningProgressLocal
                            .save(
                                context,
                                remoteProgress
                            )


                        learningSyncMessage =
                            "Progreso sincronizado"

                    } else {

                        HabitosProgresoRepository
                            .guardarProgreso(

                                progreso =
                                    healthyProgressToRequest(
                                        learningProgress
                                    ),

                                onSuccess = {

                                    learningSyncMessage =
                                        "Progreso sincronizado"
                                },

                                onError = {

                                    learningSyncMessage =
                                        "Sin conexión: usando progreso local"
                                }
                            )
                    }
                },

                onError = {

                    learningSyncMessage =
                        "Sin conexión: usando progreso local"
                }
            )
    }


    // ========================================================================
    // HEALTH CONNECT
    // ========================================================================

    val healthConnectManager =
        remember {

            HealthConnectManager(
                context
            )
        }


    // ========================================================================
    // RESUMEN DEL SUEÑO
    // ========================================================================

    var sleepSummary by remember {

        mutableStateOf<SleepSummary?>(
            null
        )
    }


    // ========================================================================
    // ESTADO DE CARGA
    // ========================================================================

    var isSleepLoading by remember {

        mutableStateOf(
            true
        )
    }


    // ========================================================================
    // ERROR
    // ========================================================================

    var sleepError by remember {

        mutableStateOf<String?>(
            null
        )
    }


    // ========================================================================
    // LEER DATOS DE HEALTH CONNECT
    // ========================================================================

    LaunchedEffect(Unit) {

        try {

            isSleepLoading =
                true


            sleepError =
                null


            val hasPermissions =
                healthConnectManager
                    .hasAllPermissions()


            if (hasPermissions) {

                sleepSummary =
                    healthConnectManager
                        .readLastSleepSummaryFromLastDays(
                            days = 30
                        )

            } else {

                sleepError =
                    "No se concedieron permisos para consultar los datos de sueño."
            }

        } catch (_: Exception) {

            sleepError =
                "No fue posible obtener los datos de sueño."

        } finally {

            isSleepLoading =
                false
        }
    }


    // ========================================================================
    // CONTENEDOR PRINCIPAL
    // ========================================================================

    Box(

        modifier =
            Modifier
                .fillMaxSize()

                // ============================================================
                // CORREGIDO
                // ============================================================
                //
                // Esta pantalla ahora tiene su propio fondo
                // y ya NO depende de MainActivity.
                // ============================================================

                .background(
                    healthyHabitsBackgroundGradient()
                )

                .statusBarsPadding()

                .navigationBarsPadding()

                .imePadding()
    ) {


        // ====================================================================
        // DECORACIÓN SUPERIOR
        // ====================================================================

        BackgroundBlurCircle(

            modifier =
                Modifier
                    .align(
                        Alignment.TopCenter
                    )
                    .padding(
                        top = 36.dp
                    )
        )


        // ====================================================================
        // CONTENIDO CON SCROLL
        // ====================================================================

        Column(

            modifier =
                Modifier
                    .fillMaxSize()
                    .verticalScroll(
                        rememberScrollState()
                    )
                    .padding(
                        horizontal = 18.dp,
                        vertical = 16.dp
                    ),

            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {


            // =================================================================
            // TOP BAR
            // =================================================================

            HealthyTopBar(

                userName =
                    firstName,

                onMenuClick =
                    onMenuClick,

                onProfileClick =
                    onProfileClick
            )


            Spacer(

                modifier =
                    Modifier.height(
                        16.dp
                    )
            )


            // =================================================================
            // TARJETA GENERAL
            // =================================================================

            Card(

                modifier =
                    Modifier
                        .fillMaxWidth()
                        .widthIn(
                            max = 450.dp
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
                        Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 18.dp,
                                vertical = 22.dp
                            ),

                    horizontalAlignment =
                        Alignment.CenterHorizontally
                ) {


                    // =========================================================
                    // TÍTULO
                    // =========================================================

                    Text(

                        text =
                            "Hábitos saludables",

                        fontSize =
                            24.sp,

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
                                18.dp
                            )
                    )


                    // =========================================================
                    // INTRODUCCIÓN
                    // =========================================================

                    HealthyIntroCard()


                    Spacer(

                        modifier =
                            Modifier.height(
                                22.dp
                            )
                    )


                    // =========================================================
                    // SUEÑO DE ANOCHE
                    // =========================================================

                    SleepSummaryCard(

                        sleepSummary =
                            sleepSummary,

                        age =
                            age,

                        aisScore =
                            sleepSurveyScore,

                        isLoading =
                            isSleepLoading,

                        error =
                            sleepError,

                        onSleepSurveyClick =
                            onSleepSurveyClick
                    )


                    // =========================================================
                    // MODO SUEÑO
                    // =========================================================

                    Spacer(

                        modifier =
                            Modifier.height(
                                16.dp
                            )
                    )


                    SleepModeEntryCard(

                        onClick =
                            onSleepModeClick
                    )


                    Spacer(

                        modifier =
                            Modifier.height(
                                26.dp
                            )
                    )


                    HorizontalDivider(

                        color =
                            Color(0xFFE2E8F0)
                    )


                    Spacer(

                        modifier =
                            Modifier.height(
                                24.dp
                            )
                    )


                    // =========================================================
                    // BIENESTAR EMOCIONAL
                    // =========================================================
                    //
                    // Atenas ya NO está duplicada aquí.
                    //
                    // Se encuentra dentro del módulo de sueño.
                    // =========================================================

                    Text(

                        text =
                            "Evaluación de bienestar",

                        fontSize =
                            18.sp,

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
                                14.dp
                            )
                    )


                    HealthyMiniCard(

                        modifier =
                            Modifier.fillMaxWidth(),

                        title =
                            "¿Cómo te sientes?",

                        subtitle =
                            "Evalúa tu bienestar emocional",

                        backgroundColor =
                            Color(0xFFF7FCEB),

                        icon = {

                            Icon(

                                imageVector =
                                    Icons.Default.Favorite,

                                contentDescription =
                                    null,

                                tint =
                                    Color(0xFFEF4444),

                                modifier =
                                    Modifier.size(
                                        42.dp
                                    )
                            )
                        },

                        onClick =
                            onMoodSurveyClick
                    )


                    Spacer(

                        modifier =
                            Modifier.height(
                                26.dp
                            )
                    )


                    HorizontalDivider(

                        color =
                            Color(0xFFE2E8F0)
                    )


                    Spacer(

                        modifier =
                            Modifier.height(
                                24.dp
                            )
                    )


                    // =========================================================
                    // APRENDE CON NUBY
                    // =========================================================

                    HealthyLearningSection(

                        progress =
                            learningProgress,

                        syncMessage =
                            learningSyncMessage,

                        selectedTopic =
                            selectedLearningTopic,

                        onTopicClick = {
                                topic ->

                            selectedLearningTopic =
                                if (
                                    selectedLearningTopic ==
                                    topic
                                ) {
                                    null
                                } else {
                                    topic
                                }
                        },

                        onCompleteTopic = {
                                topic ->

                            if (
                                !learningProgress
                                    .completedTopics
                                    .contains(
                                        topic.title
                                    )
                            ) {

                                val updated =
                                    learningProgress.copy(

                                        xp =
                                            learningProgress.xp +
                                                10,

                                        completedTopics =
                                            learningProgress
                                                .completedTopics +
                                                topic.title
                                    )


                                learningProgress =
                                    updated


                                HealthyLearningProgressLocal
                                    .save(
                                        context,
                                        updated
                                    )


                                learningSyncMessage =
                                    "Sincronizando progreso..."


                                HabitosProgresoRepository
                                    .guardarProgreso(

                                        progreso =
                                            healthyProgressToRequest(
                                                updated
                                            ),

                                        onSuccess = {

                                            learningSyncMessage =
                                                "Progreso sincronizado"
                                        },

                                        onError = {

                                            learningSyncMessage =
                                                "Sin conexión: usando progreso local"
                                        }
                                    )
                            }
                        },

                        onQuizFinished = {
                                result ->

                            val improved =
                                result.score >
                                    learningProgress.bestScore


                            val xpEarned =
                                when {

                                    learningProgress.attempts == 0 ->
                                        20

                                    improved ->
                                        10

                                    else ->
                                        5
                                }


                            val updated =
                                learningProgress.copy(

                                    xp =
                                        learningProgress.xp +
                                            xpEarned,

                                    attempts =
                                        learningProgress.attempts +
                                            1,

                                    bestScore =
                                        maxOf(
                                            learningProgress.bestScore,
                                            result.score
                                        ),

                                    topicsToReinforce =
                                        result.failedTopics
                                )


                            learningProgress =
                                updated


                            HealthyLearningProgressLocal
                                .save(
                                    context,
                                    updated
                                )


                            learningSyncMessage =
                                "Sincronizando progreso..."


                            HabitosProgresoRepository
                                .guardarProgreso(

                                    progreso =
                                        healthyProgressToRequest(
                                            updated
                                        ),

                                    onSuccess = {

                                        learningSyncMessage =
                                            "Progreso sincronizado"
                                    },

                                    onError = {

                                        learningSyncMessage =
                                            "Sin conexión: usando progreso local"
                                    }
                                )


                            HabitosProgresoRepository
                                .guardarIntento(

                                    intento =
                                        HabitosIntentoRequest(

                                            puntaje =
                                                result.score,

                                            total =
                                                result.total,

                                            dificultad =
                                                result.difficulty,

                                            xpGanado =
                                                xpEarned,

                                            temasFallados =
                                                result.failedTopics
                                                    .toList()
                                        ),

                                    onSuccess = {
                                    },

                                    onError = {
                                    }
                                )
                        },

                        onCloseTopic = {

                            selectedLearningTopic =
                                null
                        }
                    )


                    Spacer(

                        modifier =
                            Modifier.height(
                                26.dp
                            )
                    )


                    // =========================================================
                    // NUBY CENTRAL
                    // =========================================================

                    HealthyCentralImagePlaceholder()


                    Spacer(

                        modifier =
                            Modifier.height(
                                26.dp
                            )
                    )


                    // =========================================================
                    // HERRAMIENTAS
                    // =========================================================

                    Text(

                        text =
                            "Herramientas para cuidar tu salud",

                        fontSize =
                            18.sp,

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
                                14.dp
                            )
                    )


                    // =========================================================
                    // IMC
                    // =========================================================

                    HealthToolCard(

                        title =
                            "Calculadora de IMC",

                        subtitle =
                            "Conoce tu índice de masa corporal",

                        backgroundColor =
                            Color(0xFFF7FCEB),

                        icon = {

                            Icon(

                                imageVector =
                                    Icons.Default.Calculate,

                                contentDescription =
                                    null,

                                tint =
                                    Color(0xFF86A327),

                                modifier =
                                    Modifier.size(
                                        38.dp
                                    )
                            )
                        },

                        onClick =
                            onImcClick
                    )


                    Spacer(

                        modifier =
                            Modifier.height(
                                14.dp
                            )
                    )


                    // =========================================================
                    // CALORÍAS
                    // =========================================================

                    HealthToolCard(

                        title =
                            "Calculadora de calorías",

                        subtitle =
                            "Estima tus necesidades energéticas",

                        backgroundColor =
                            Color(0xFFEAF8FF),

                        icon = {

                            Icon(

                                imageVector =
                                    Icons.Default.LocalFireDepartment,

                                contentDescription =
                                    null,

                                tint =
                                    Color(0xFF0284C7),

                                modifier =
                                    Modifier.size(
                                        38.dp
                                    )
                            )
                        },

                        onClick =
                            onCaloriesClick
                    )


                    Spacer(

                        modifier =
                            Modifier.height(
                                14.dp
                            )
                    )


                    // =========================================================
                    // RIESGO CARDIOVASCULAR
                    // =========================================================

                    HealthToolCard(

                        title =
                            "Calculadora de riesgo cardiovascular",

                        subtitle =
                            "Identifica factores de riesgo",

                        backgroundColor =
                            Color(0xFFFFEAEA),

                        icon = {

                            Icon(

                                imageVector =
                                    Icons.Default.MonitorHeart,

                                contentDescription =
                                    null,

                                tint =
                                    Color(0xFFEF4444),

                                modifier =
                                    Modifier.size(
                                        38.dp
                                    )
                            )
                        },

                        onClick =
                            onCardioRiskClick
                    )


                    Spacer(

                        modifier =
                            Modifier.height(
                                26.dp
                            )
                    )


                    // =========================================================
                    // VOLVER
                    // =========================================================

                    Button(

                        onClick =
                            onBackToMenu,

                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(
                                    54.dp
                                ),

                        shape =
                            RoundedCornerShape(
                                20.dp
                            ),

                        colors =
                            ButtonDefaults.buttonColors(

                                containerColor =
                                    Color(0xFF22C55E),

                                contentColor =
                                    Color(0xFF052E16)
                            )
                    ) {


                        Text(

                            text =
                                "Volver al menú",

                            fontSize =
                                16.sp,

                            fontWeight =
                                FontWeight.Bold
                        )
                    }
                }
            }


            Spacer(

                modifier =
                    Modifier.height(
                        28.dp
                    )
            )
        }
    }
}


// ============================================================================
// TARJETA PARA ENTRAR AL MODO SUEÑO
// ============================================================================

@Composable
fun SleepModeEntryCard(
    onClick: () -> Unit
) {


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
                    Color(0xFF0B2340)
            ),

        elevation =
            CardDefaults.cardElevation(

                defaultElevation =
                    5.dp
            )
    ) {


        Row(

            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(
                        18.dp
                    ),

            verticalAlignment =
                Alignment.CenterVertically
        ) {


            // =================================================================
            // ICONO
            // =================================================================

            Box(

                modifier =
                    Modifier
                        .size(
                            58.dp
                        )
                        .clip(
                            CircleShape
                        )
                        .background(

                            Color.White.copy(
                                alpha = 0.10f
                            )
                        ),

                contentAlignment =
                    Alignment.Center
            ) {


                Icon(

                    imageVector =
                        Icons.Default.Bedtime,

                    contentDescription =
                        null,

                    tint =
                        Color(0xFF7DD3FC),

                    modifier =
                        Modifier.size(
                            34.dp
                        )
                )
            }


            Spacer(

                modifier =
                    Modifier.width(
                        14.dp
                    )
            )


            // =================================================================
            // TEXTO
            // =================================================================

            Column(

                modifier =
                    Modifier.weight(
                        1f
                    )
            ) {


                Text(

                    text =
                        "Modo sueño",

                    color =
                        Color.White,

                    fontSize =
                        17.sp,

                    fontWeight =
                        FontWeight.Bold
                )


                Spacer(

                    modifier =
                        Modifier.height(
                            4.dp
                        )
                )


                Text(

                    text =
                        "Prepara el monitoreo de tu descanso para esta noche.",

                    color =
                        Color(0xFFCBD5E1),

                    fontSize =
                        11.sp,

                    lineHeight =
                        15.sp
                )
            }


            Spacer(

                modifier =
                    Modifier.width(
                        10.dp
                    )
            )


            // =================================================================
            // ABRIR
            // =================================================================

            Button(

                onClick =
                    onClick,

                shape =
                    RoundedCornerShape(
                        16.dp
                    ),

                colors =
                    ButtonDefaults.buttonColors(

                        containerColor =
                            Color(0xFF38BDF8),

                        contentColor =
                            Color(0xFF082F49)
                    )
            ) {


                Text(

                    text =
                        "Abrir",

                    fontWeight =
                        FontWeight.Bold
                )
            }
        }
    }
}


// ============================================================================
// RESUMEN DEL SUEÑO
// ============================================================================

@Composable
fun SleepSummaryCard(

    sleepSummary: SleepSummary?,

    age: Int?,

    aisScore: Int?,

    isLoading: Boolean,

    error: String?,

    onSleepSurveyClick: () -> Unit
) {


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
                    Color(0xFFEAF8FF)
            ),

        elevation =
            CardDefaults.cardElevation(

                defaultElevation =
                    5.dp
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
                                52.dp
                            )
                            .clip(
                                CircleShape
                            )
                            .background(
                                Color.White
                            ),

                    contentAlignment =
                        Alignment.Center
                ) {


                    Icon(

                        imageVector =
                            Icons.Default.Bedtime,

                        contentDescription =
                            null,

                        tint =
                            Color(0xFF0284C7),

                        modifier =
                            Modifier.size(
                                30.dp
                            )
                    )
                }


                Spacer(

                    modifier =
                        Modifier.width(
                            12.dp
                        )
                )


                Column {


                    Text(

                        text =
                            "Sueño de anoche",

                        fontSize =
                            18.sp,

                        fontWeight =
                            FontWeight.Bold,

                        color =
                            Color(0xFF0F172A)
                    )


                    Text(

                        text =
                            "Datos obtenidos de tu wearable",

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
            // CARGANDO
            // =================================================================

            if (isLoading) {


                Row(

                    verticalAlignment =
                        Alignment.CenterVertically
                ) {


                    CircularProgressIndicator(

                        modifier =
                            Modifier.size(
                                22.dp
                            ),

                        strokeWidth =
                            3.dp,

                        color =
                            Color(0xFF0284C7)
                    )


                    Spacer(

                        modifier =
                            Modifier.width(
                                10.dp
                            )
                    )


                    Text(

                        text =
                            "Consultando tu sueño...",

                        fontSize =
                            13.sp,

                        color =
                            Color(0xFF64748B)
                    )
                }


                return@Column
            }


            // =================================================================
            // ERROR
            // =================================================================

            if (error != null) {


                Text(

                    text =
                        error,

                    fontSize =
                        13.sp,

                    color =
                        Color(0xFF64748B)
                )


                return@Column
            }


            // =================================================================
            // SIN DATOS
            // =================================================================

            if (sleepSummary == null) {


                Text(

                    text =
                        "No se encontró una sesión de sueño del wearable.",

                    fontSize =
                        13.sp,

                    color =
                        Color(0xFF64748B)
                )


                Spacer(

                    modifier =
                        Modifier.height(
                            14.dp
                        )
                )


                Button(

                    onClick =
                        onSleepSurveyClick,

                    modifier =
                        Modifier.fillMaxWidth(),

                    colors =
                        ButtonDefaults.buttonColors(

                            containerColor =
                                Color(0xFF0284C7),

                            contentColor =
                                Color.White
                        )
                ) {


                    Text(

                        text =
                            "Responder Escala de Atenas"
                    )
                }


                return@Column
            }


            // =================================================================
            // DURACIÓN
            // =================================================================

            val hours =
                sleepSummary.totalMinutes / 60


            val minutes =
                sleepSummary.totalMinutes % 60


            val durationStatus =
                classifySleepDuration(

                    totalMinutes =
                        sleepSummary.totalMinutes,

                    age =
                        age
                )


            val durationColor =
                sleepTrafficLightColor(
                    durationStatus
                )


            Text(

                text =
                    "$hours h $minutes min",

                fontSize =
                    30.sp,

                fontWeight =
                    FontWeight.Bold,

                color =
                    Color(0xFF0F766E)
            )


            Spacer(

                modifier =
                    Modifier.height(
                        5.dp
                    )
            )


            // =================================================================
            // HORARIO
            // =================================================================

            Text(

                text =
                    "${formatSleepTime(sleepSummary.startTime)} - " +
                            formatSleepTime(
                                sleepSummary.endTime
                            ),

                fontSize =
                    12.sp,

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
            // SEMÁFORO
            // =================================================================

            Row(

                verticalAlignment =
                    Alignment.CenterVertically
            ) {


                Box(

                    modifier =
                        Modifier
                            .size(
                                13.dp
                            )
                            .background(

                                color =
                                    durationColor,

                                shape =
                                    CircleShape
                            )
                )


                Spacer(

                    modifier =
                        Modifier.width(
                            8.dp
                        )
                )


                Text(

                    text =
                        when (durationStatus) {


                            SleepTrafficLight.GOOD ->

                                "Duración adecuada"


                            SleepTrafficLight.REGULAR ->

                                "Duración por revisar"


                            SleepTrafficLight.LOW ->

                                "Duración fuera del rango esperado"
                        },

                    fontSize =
                        14.sp,

                    fontWeight =
                        FontWeight.Bold,

                    color =
                        durationColor
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
                    sleepRecommendationText(
                        age
                    ),

                fontSize =
                    12.sp,

                color =
                    Color(0xFF64748B),

                lineHeight =
                    17.sp
            )


            // =================================================================
            // ETAPAS
            // =================================================================

            Spacer(

                modifier =
                    Modifier.height(
                        18.dp
                    )
            )


            HorizontalDivider(

                color =
                    Color.White
            )


            Spacer(

                modifier =
                    Modifier.height(
                        16.dp
                    )
            )


            Text(

                text =
                    "Etapas del sueño",

                fontSize =
                    15.sp,

                fontWeight =
                    FontWeight.Bold,

                color =
                    Color(0xFF0F172A)
            )


            Spacer(

                modifier =
                    Modifier.height(
                        12.dp
                    )
            )


            if (
                sleepSummary.hasSleepStages
            ) {


                // =============================================================
                // LIGERO
                // =============================================================

                if (
                    sleepSummary.hasLightSleep
                ) {


                    SleepStageRow(

                        label =
                            "Sueño ligero",

                        minutes =
                            sleepSummary.lightSleepMinutes
                    )
                }


                // =============================================================
                // PROFUNDO
                // =============================================================

                if (
                    sleepSummary.hasDeepSleep
                ) {


                    SleepStageRow(

                        label =
                            "Sueño profundo",

                        minutes =
                            sleepSummary.deepSleepMinutes
                    )
                }


                // =============================================================
                // REM
                // =============================================================
                //
                // Solo aparece si el wearable realmente
                // proporciona esta etapa.
                // =============================================================

                if (
                    sleepSummary.hasRemSleep
                ) {


                    SleepStageRow(

                        label =
                            "Sueño REM",

                        minutes =
                            sleepSummary.remSleepMinutes
                    )
                }


                // =============================================================
                // DESPIERTO
                // =============================================================

                if (
                    sleepSummary.hasAwakeData
                ) {


                    SleepStageRow(

                        label =
                            "Despierto",

                        minutes =
                            sleepSummary.awakeMinutes
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
                        "${sleepSummary.stages.size} cambios de etapa registrados durante la sesión.",

                    fontSize =
                        11.sp,

                    color =
                        Color(0xFF94A3B8)
                )


                Spacer(

                    modifier =
                        Modifier.height(
                            6.dp
                        )
                )


                // =============================================================
                // ACLARACIÓN
                // =============================================================

                if (
                    !sleepSummary.hasRemSleep ||
                    !sleepSummary.hasAwakeData
                ) {


                    Text(

                        text =
                            "Se muestran únicamente las etapas que tu wearable comparte con Health Connect.",

                        fontSize =
                            11.sp,

                        color =
                            Color(0xFF64748B),

                        lineHeight =
                            15.sp
                    )
                }

            } else {


                Text(

                    text =
                        "El wearable registró el sueño, pero no proporcionó información sobre sus etapas.",

                    fontSize =
                        12.sp,

                    color =
                        Color(0xFF64748B),

                    lineHeight =
                        17.sp
                )
            }


            // =================================================================
            // ATENAS
            // =================================================================

            Spacer(

                modifier =
                    Modifier.height(
                        18.dp
                    )
            )


            HorizontalDivider(

                color =
                    Color.White
            )


            Spacer(

                modifier =
                    Modifier.height(
                        16.dp
                    )
            )


            Text(

                text =
                    "Percepción del sueño",

                fontSize =
                    15.sp,

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


            // =================================================================
            // SIN ATENAS
            // =================================================================

            if (aisScore == null) {


                Text(

                    text =
                        "Todavía no has respondido la Escala de Insomnio de Atenas.",

                    fontSize =
                        12.sp,

                    color =
                        Color(0xFF64748B),

                    lineHeight =
                        17.sp
                )


                Spacer(

                    modifier =
                        Modifier.height(
                            12.dp
                        )
                )


                Button(

                    onClick =
                        onSleepSurveyClick,

                    modifier =
                        Modifier.fillMaxWidth(),

                    shape =
                        RoundedCornerShape(
                            16.dp
                        ),

                    colors =
                        ButtonDefaults.buttonColors(

                            containerColor =
                                Color(0xFF0284C7),

                            contentColor =
                                Color.White
                        )
                ) {


                    Text(

                        text =
                            "Responder Escala de Atenas",

                        fontWeight =
                            FontWeight.Bold
                    )
                }

            } else {


                // =============================================================
                // RESULTADO AIS
                // =============================================================

                val aisFavorable =
                    aisScore < 6


                Text(

                    text =
                        "AIS: $aisScore de 24",

                    fontSize =
                        14.sp,

                    fontWeight =
                        FontWeight.Bold,

                    color =
                        if (aisFavorable) {

                            Color(0xFF22C55E)

                        } else {

                            Color(0xFFF59E0B)
                        }
                )


                Spacer(

                    modifier =
                        Modifier.height(
                            5.dp
                        )
                )


                Text(

                    text =
                        if (aisFavorable) {

                            "Tu percepción del sueño fue favorable."

                        } else {

                            "Reportaste dificultades relacionadas con el sueño."
                        },

                    fontSize =
                        12.sp,

                    color =
                        Color(0xFF64748B)
                )


                Spacer(

                    modifier =
                        Modifier.height(
                            16.dp
                        )
                )


                // =============================================================
                // RESULTADO COMBINADO
                // =============================================================

                val combinedStatus =
                    combineSleepResults(

                        durationStatus =
                            durationStatus,

                        aisScore =
                            aisScore
                    )


                CombinedSleepResultCard(

                    status =
                        combinedStatus,

                    message =
                        combinedSleepMessage(

                            durationStatus =
                                durationStatus,

                            aisScore =
                                aisScore
                        )
                )


                Spacer(

                    modifier =
                        Modifier.height(
                            12.dp
                        )
                )


                Button(

                    onClick =
                        onSleepSurveyClick,

                    modifier =
                        Modifier.fillMaxWidth(),

                    shape =
                        RoundedCornerShape(
                            16.dp
                        ),

                    colors =
                        ButtonDefaults.buttonColors(

                            containerColor =
                                Color(0xFF0284C7),

                            contentColor =
                                Color.White
                        )
                ) {


                    Text(

                        text =
                            "Volver a evaluar mi sueño",

                        fontWeight =
                            FontWeight.Bold
                    )
                }
            }


            Spacer(

                modifier =
                    Modifier.height(
                        12.dp
                    )
            )


            // =================================================================
            // AVISO
            // =================================================================

            Text(

                text =
                    "Los datos del wearable y el resultado de Atenas son orientativos y no constituyen un diagnóstico médico.",

                fontSize =
                    10.sp,

                color =
                    Color(0xFF94A3B8),

                textAlign =
                    TextAlign.Center,

                lineHeight =
                    14.sp,

                modifier =
                    Modifier.fillMaxWidth()
            )
        }
    }
}


// ============================================================================
// FILA DE ETAPA
// ============================================================================

@Composable
fun SleepStageRow(

    label: String,

    minutes: Long
) {


    val hours =
        minutes / 60


    val remainingMinutes =
        minutes % 60


    Row(

        modifier =
            Modifier
                .fillMaxWidth()
                .padding(
                    vertical = 6.dp
                ),

        verticalAlignment =
            Alignment.CenterVertically
    ) {


        Text(

            text =
                label,

            fontSize =
                13.sp,

            color =
                Color(0xFF334155),

            modifier =
                Modifier.weight(
                    1f
                )
        )


        Text(

            text =
                if (hours > 0) {

                    "$hours h $remainingMinutes min"

                } else {

                    "$remainingMinutes min"
                },

            fontSize =
                13.sp,

            fontWeight =
                FontWeight.Bold,

            color =
                Color(0xFF0F172A)
        )
    }
}


// ============================================================================
// RESULTADO COMBINADO
// ============================================================================

@Composable
fun CombinedSleepResultCard(

    status: CombinedSleepStatus,

    message: String
) {


    val statusColor =
        combinedSleepStatusColor(
            status
        )


    val title =
        when (status) {


            CombinedSleepStatus.GOOD ->

                "Buen descanso"


            CombinedSleepStatus.REGULAR ->

                "Descanso regular"


            CombinedSleepStatus.LOW ->

                "Descanso por mejorar"
        }


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
                    Color.White
            )
    ) {


        Column(

            modifier =
                Modifier.padding(
                    14.dp
                )
        ) {


            Text(

                text =
                    "Resultado combinado",

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
                        8.dp
                    )
            )


            Row(

                verticalAlignment =
                    Alignment.CenterVertically
            ) {


                Box(

                    modifier =
                        Modifier
                            .size(
                                14.dp
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
                        Modifier.width(
                            8.dp
                        )
                )


                Text(

                    text =
                        title,

                    fontSize =
                        17.sp,

                    fontWeight =
                        FontWeight.Bold,

                    color =
                        statusColor
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
                    message,

                fontSize =
                    12.sp,

                color =
                    Color(0xFF334155),

                lineHeight =
                    17.sp
            )
        }
    }
}


// ============================================================================
// CLASIFICACIÓN POR DURACIÓN
// ============================================================================

fun classifySleepDuration(

    totalMinutes: Long,

    age: Int?

): SleepTrafficLight {


    val hours =
        totalMinutes /
                60.0


    return when {


        // ====================================================================
        // MENORES DE 18
        // ====================================================================

        age != null &&
                age <= 17 -> {


            when {


                hours in 8.0..10.0 ->

                    SleepTrafficLight.GOOD


                hours >= 7.0 &&
                        hours < 8.0 ->

                    SleepTrafficLight.REGULAR


                hours > 10.0 &&
                        hours <= 11.0 ->

                    SleepTrafficLight.REGULAR


                else ->

                    SleepTrafficLight.LOW
            }
        }


        // ====================================================================
        // 18 AÑOS O MÁS
        // ====================================================================

        else -> {


            when {


                hours in 7.0..9.0 ->

                    SleepTrafficLight.GOOD


                hours >= 6.0 &&
                        hours < 7.0 ->

                    SleepTrafficLight.REGULAR


                hours > 9.0 &&
                        hours <= 10.0 ->

                    SleepTrafficLight.REGULAR


                else ->

                    SleepTrafficLight.LOW
            }
        }
    }
}


// ============================================================================
// COMBINAR DURACIÓN + ATENAS
// ============================================================================

fun combineSleepResults(

    durationStatus: SleepTrafficLight,

    aisScore: Int

): CombinedSleepStatus {


    val aisFavorable =
        aisScore < 6


    return when {


        // ====================================================================
        // DURACIÓN ADECUADA + AIS FAVORABLE
        // ====================================================================

        durationStatus ==
                SleepTrafficLight.GOOD &&
                aisFavorable ->

            CombinedSleepStatus.GOOD


        // ====================================================================
        // DURACIÓN ADECUADA + AIS DESFAVORABLE
        // ====================================================================

        durationStatus ==
                SleepTrafficLight.GOOD &&
                !aisFavorable ->

            CombinedSleepStatus.REGULAR


        // ====================================================================
        // DURACIÓN REGULAR
        // ====================================================================

        durationStatus ==
                SleepTrafficLight.REGULAR ->

            CombinedSleepStatus.REGULAR


        // ====================================================================
        // DURACIÓN FUERA DEL RANGO + AIS FAVORABLE
        // ====================================================================

        durationStatus ==
                SleepTrafficLight.LOW &&
                aisFavorable ->

            CombinedSleepStatus.REGULAR


        // ====================================================================
        // DURACIÓN FUERA DEL RANGO + AIS DESFAVORABLE
        // ====================================================================

        else ->

            CombinedSleepStatus.LOW
    }
}


// ============================================================================
// MENSAJE COMBINADO
// ============================================================================

fun combinedSleepMessage(

    durationStatus: SleepTrafficLight,

    aisScore: Int

): String {


    val favorable =
        aisScore < 6


    return when {


        durationStatus ==
                SleepTrafficLight.GOOD &&
                favorable ->

            "La duración registrada y tu percepción del descanso fueron favorables."


        durationStatus ==
                SleepTrafficLight.GOOD &&
                !favorable ->

            "Dormiste una cantidad adecuada de horas, pero reportaste dificultades relacionadas con tu descanso."


        durationStatus ==
                SleepTrafficLight.LOW &&
                favorable ->

            "La duración registrada estuvo fuera del rango esperado, aunque tu percepción del descanso fue favorable."


        durationStatus ==
                SleepTrafficLight.LOW &&
                !favorable ->

            "La duración estuvo fuera del rango esperado y también reportaste dificultades relacionadas con el sueño."


        durationStatus ==
                SleepTrafficLight.REGULAR &&
                favorable ->

            "La duración estuvo cerca del rango esperado y tu percepción del descanso fue favorable."


        else ->

            "La duración del sueño requiere revisión y la Escala de Atenas también identificó dificultades relacionadas con el descanso."
    }
}


// ============================================================================
// COLORES
// ============================================================================

fun sleepTrafficLightColor(
    status: SleepTrafficLight
): Color {


    return when (status) {


        SleepTrafficLight.GOOD ->

            Color(0xFF22C55E)


        SleepTrafficLight.REGULAR ->

            Color(0xFFF59E0B)


        SleepTrafficLight.LOW ->

            Color(0xFFDC2626)
    }
}


fun combinedSleepStatusColor(
    status: CombinedSleepStatus
): Color {


    return when (status) {


        CombinedSleepStatus.GOOD ->

            Color(0xFF22C55E)


        CombinedSleepStatus.REGULAR ->

            Color(0xFFF59E0B)


        CombinedSleepStatus.LOW ->

            Color(0xFFDC2626)
    }
}


// ============================================================================
// RECOMENDACIÓN DE HORAS
// ============================================================================

fun sleepRecommendationText(
    age: Int?
): String {


    return when {


        age != null &&
                age <= 17 ->

            "Para este grupo de edad se utiliza como referencia una duración de 8 a 10 horas."


        age != null ->

            "Para este grupo de edad se utiliza como referencia una duración habitual de 7 a 9 horas."


        else ->

            "La duración recomendada del sueño depende de la edad."
    }
}


// ============================================================================
// FORMATEAR HORA
// ============================================================================

fun formatSleepTime(
    instant: Instant
): String {


    val formatter =
        DateTimeFormatter.ofPattern(
            "HH:mm"
        )


    return instant
        .atZone(
            ZoneId.systemDefault()
        )
        .format(
            formatter
        )
}


// ============================================================================
// BARRA SUPERIOR
// ============================================================================

@Composable
fun HealthyTopBar(

    userName: String,

    onMenuClick: () -> Unit,

    onProfileClick: () -> Unit
) {


    Row(

        modifier =
            Modifier
                .fillMaxWidth()
                .widthIn(
                    max = 450.dp
                )
                .defaultMinSize(
                    minHeight = 56.dp
                ),

        verticalAlignment =
            Alignment.CenterVertically
    ) {


        IconButton(

            onClick =
                onMenuClick
        ) {


            Icon(

                imageVector =
                    Icons.Default.Menu,

                contentDescription =
                    "Abrir menú",

                tint =
                    Color(0xFF6B8E23),

                modifier =
                    Modifier.size(
                        30.dp
                    )
            )
        }


        Text(

            text =
                "Hola, $userName",

            color =
                Color(0xFF0F172A),

            fontSize =
                17.sp,

            fontWeight =
                FontWeight.Bold,

            modifier =
                Modifier.weight(
                    1f
                )
        )


        Box(

            modifier =
                Modifier
                    .size(
                        46.dp
                    )
                    .clip(
                        CircleShape
                    )
                    .background(
                        Color(0xFFFFE7C7)
                    ),

            contentAlignment =
                Alignment.Center
        ) {


            IconButton(

                onClick =
                    onProfileClick
            ) {


                Icon(

                    imageVector =
                        Icons.Default.AccountCircle,

                    contentDescription =
                        "Perfil",

                    tint =
                        Color(0xFF7C4A2D),

                    modifier =
                        Modifier.size(
                            34.dp
                        )
                )
            }
        }
    }
}


// ============================================================================
// INTRODUCCIÓN
// ============================================================================

@Composable
fun HealthyIntroCard() {


    val introText =
        """
        Los hábitos de vida saludable se construyen con acciones pequeñas que se repiten todos los días.

        Dormir bien, comer de forma equilibrada, hidratarse, moverse y reducir el estrés ayudan a mejorar el bienestar.
        """.trimIndent()


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
                    14.dp
                )
        ) {


            Row(

                verticalAlignment =
                    Alignment.CenterVertically
            ) {


                HealthyMascotPlaceholder(

                    modifier =
                        Modifier.size(
                            112.dp
                        )
                )


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


                    Text(

                        text =
                            "¿Sabías que...?",

                        fontSize =
                            15.sp,

                        fontWeight =
                            FontWeight.Bold,

                        color =
                            Color(0xFF0F766E)
                    )


                    Spacer(

                        modifier =
                            Modifier.height(
                                6.dp
                            )
                    )


                    Text(

                        text =
                            "Los hábitos saludables se relacionan con dormir bien, comer equilibradamente y manejar el estrés.",

                        fontSize =
                            13.sp,

                        color =
                            Color(0xFF334155),

                        lineHeight =
                            18.sp
                    )
                }
            }


            Spacer(

                modifier =
                    Modifier.height(
                        14.dp
                    )
            )


            TextToSpeechSmallButton(

                textToRead =
                    introText
            )
        }
    }
}


// ============================================================================
// MASCOTA
// ============================================================================

@Composable
fun HealthyMascotPlaceholder(
    modifier: Modifier = Modifier
) {

    Box(

        modifier =
            modifier
                .clip(
                    RoundedCornerShape(
                        24.dp
                    )
                )
                .background(

                    Brush.radialGradient(

                        colors =
                            listOf(

                                Color(0xFFB8F7E8),

                                Color(0xFFF7FCEB)
                            )
                    )
                ),

        contentAlignment =
            Alignment.Center
    ) {

        Image(

            painter =
                painterResource(
                    id =
                        R.drawable.nuby_saludo
                ),

            contentDescription =
                "Nuby saludando",

            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(
                        8.dp
                    ),

            contentScale =
                ContentScale.Fit
        )
    }
}


// ============================================================================
// TEXT TO SPEECH
// ============================================================================

@Composable
fun TextToSpeechSmallButton(
    textToRead: String
) {


    val context =
        LocalContext.current


    var textToSpeech by remember {

        mutableStateOf<TextToSpeech?>(
            null
        )
    }


    var ready by remember {

        mutableStateOf(
            false
        )
    }


    DisposableEffect(Unit) {


        val tts =
            TextToSpeech(
                context
            ) { status ->


                if (
                    status ==
                    TextToSpeech.SUCCESS
                ) {


                    ready =
                        true
                }
            }


        textToSpeech =
            tts


        onDispose {


            tts.stop()


            tts.shutdown()
        }
    }


    Button(

        onClick = {


            if (ready) {


                textToSpeech?.language =
                    Locale.forLanguageTag(
                        "es-MX"
                    )


                textToSpeech?.speak(

                    textToRead,

                    TextToSpeech.QUEUE_FLUSH,

                    null,

                    "habitos_saludables_intro"
                )
            }
        },

        modifier =
            Modifier.height(
                40.dp
            ),

        shape =
            RoundedCornerShape(
                16.dp
            ),

        colors =
            ButtonDefaults.buttonColors(

                containerColor =
                    Color(0xFF0F766E),

                contentColor =
                    Color.White
            ),

        contentPadding =
            PaddingValues(
                horizontal = 14.dp
            )
    ) {


        Icon(

            imageVector =
                Icons.Default.VolumeUp,

            contentDescription =
                null,

            modifier =
                Modifier.size(
                    18.dp
                )
        )


        Spacer(

            modifier =
                Modifier.width(
                    8.dp
                )
        )


        Text(

            text =
                "Escuchar",

            fontSize =
                13.sp,

            fontWeight =
                FontWeight.Bold
        )
    }
}


// ============================================================================
// TARJETA DE BIENESTAR
// ============================================================================

@Composable
fun HealthyMiniCard(

    modifier: Modifier = Modifier,

    title: String,

    subtitle: String,

    backgroundColor: Color,

    icon:
    @Composable () -> Unit,

    onClick: () -> Unit
) {


    Card(

        modifier =
            modifier.defaultMinSize(
                minHeight = 150.dp
            ),

        shape =
            RoundedCornerShape(
                24.dp
            ),

        colors =
            CardDefaults.cardColors(

                containerColor =
                    backgroundColor
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
                        14.dp
                    ),

            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {


            Box(

                modifier =
                    Modifier
                        .size(
                            58.dp
                        )
                        .clip(
                            RoundedCornerShape(
                                18.dp
                            )
                        )
                        .background(

                            Color.White.copy(
                                alpha = 0.75f
                            )
                        ),

                contentAlignment =
                    Alignment.Center
            ) {


                icon()
            }


            Spacer(

                modifier =
                    Modifier.height(
                        10.dp
                    )
            )


            Text(

                text =
                    title,

                fontSize =
                    14.sp,

                fontWeight =
                    FontWeight.Bold,

                textAlign =
                    TextAlign.Center
            )


            Spacer(

                modifier =
                    Modifier.height(
                        5.dp
                    )
            )


            Text(

                text =
                    subtitle,

                fontSize =
                    12.sp,

                color =
                    Color(0xFF64748B),

                textAlign =
                    TextAlign.Center
            )


            Spacer(

                modifier =
                    Modifier.height(
                        12.dp
                    )
            )


            Button(

                onClick =
                    onClick,

                shape =
                    RoundedCornerShape(
                        16.dp
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
                        "Vamos"
                )
            }
        }
    }
}




// ============================================================================
// APRENDE CON NUBY
// ============================================================================

@Composable
private fun HealthyLearningSection(

    progress:
        HealthyLearningProgress,

    syncMessage:
        String,

    selectedTopic:
        HealthyLearningTopic?,

    onTopicClick:
        (HealthyLearningTopic) -> Unit,

    onCompleteTopic:
        (HealthyLearningTopic) -> Unit,

    onQuizFinished:
        (HealthyQuizResult) -> Unit,

    onCloseTopic:
        () -> Unit
) {

    var showQuiz by remember {
        mutableStateOf(
            false
        )
    }


    var lastResult by remember {
        mutableStateOf<HealthyQuizResult?>(
            null
        )
    }


    val badges =
        healthyBadges(
            progress
        )


    Column(

        modifier =
            Modifier.fillMaxWidth(),

        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        Row(

            modifier =
                Modifier.fillMaxWidth(),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Image(

                painter =
                    painterResource(
                        id =
                            R.drawable.nuby_idea
                    ),

                contentDescription =
                    "Nuby explicando hábitos saludables",

                modifier =
                    Modifier.size(
                        72.dp
                    ),

                contentScale =
                    ContentScale.Fit
            )


            Spacer(

                modifier =
                    Modifier.width(
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
                        "Aprende con Nuby",

                    fontSize =
                        20.sp,

                    fontWeight =
                        FontWeight.Bold,

                    color =
                        Color(0xFF0F766E)
                )


                Text(

                    text =
                        "Explora hábitos que puedes aplicar en tu vida diaria.",

                    fontSize =
                        13.sp,

                    lineHeight =
                        18.sp,

                    color =
                        Color(0xFF475569)
                )
            }
        }


        Spacer(

            modifier =
                Modifier.height(
                    14.dp
                )
        )


        // ====================================================================
        // PROGRESO
        // ====================================================================

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
                        Color(0xFFEAF8FF)
                )
        ) {

            Column(

                modifier =
                    Modifier.padding(
                        15.dp
                    )
            ) {

                Row(

                    modifier =
                        Modifier.fillMaxWidth(),

                    horizontalArrangement =
                        androidx.compose.foundation.layout.Arrangement.SpaceBetween,

                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Column {

                        Text(

                            text =
                                "Tu progreso",

                            fontSize =
                                16.sp,

                            fontWeight =
                                FontWeight.Bold,

                            color =
                                Color(0xFF0F766E)
                        )


                        Text(

                            text =
                                healthyLevelName(
                                    progress.xp
                                ),

                            fontSize =
                                12.sp,

                            fontWeight =
                                FontWeight.Bold,

                            color =
                                Color(0xFF0369A1)
                        )
                    }


                    Text(

                        text =
                            "${progress.xp} XP",

                        fontSize =
                            18.sp,

                        fontWeight =
                            FontWeight.Bold,

                        color =
                            Color(0xFF0F766E)
                    )
                }


                Spacer(

                    modifier =
                        Modifier.height(
                            9.dp
                        )
                )


                LinearProgressIndicator(

                    progress = {
                        healthyLevelProgress(
                            progress.xp
                        )
                    },

                    modifier =
                        Modifier.fillMaxWidth(),

                    color =
                        Color(0xFF0F766E),

                    trackColor =
                        Color(0xFFD7EAF4)
                )


                Spacer(

                    modifier =
                        Modifier.height(
                            6.dp
                        )
                )


                Text(

                    text =
                        "${healthyXpToNextLevel(progress.xp)} XP para el siguiente nivel",

                    fontSize =
                        10.sp,

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
                        "Temas: ${progress.completedTopics.size}/${healthyLearningTopics.size} · Evaluaciones: ${progress.attempts} · Mejor resultado: ${progress.bestScore}/10",

                    fontSize =
                        11.sp,

                    lineHeight =
                        16.sp,

                    color =
                        Color(0xFF475569)
                )


                Spacer(

                    modifier =
                        Modifier.height(
                            5.dp
                        )
                )


                Text(

                    text =
                        if (
                            syncMessage ==
                                "Progreso sincronizado"
                        ) {
                            "☁️ $syncMessage"
                        } else {
                            syncMessage
                        },

                    fontSize =
                        10.sp,

                    color =
                        if (
                            syncMessage ==
                                "Progreso sincronizado"
                        ) {
                            Color(0xFF0F766E)
                        } else {
                            Color(0xFF64748B)
                        }
                )


                Spacer(

                    modifier =
                        Modifier.height(
                            6.dp
                        )
                )


                Text(

                    text =
                        "Dificultad actual: ${healthyDifficultyFor(progress).label}",

                    fontSize =
                        11.sp,

                    fontWeight =
                        FontWeight.Bold,

                    color =
                        Color(0xFF0369A1)
                )


                if (
                    progress.topicsToReinforce
                        .isNotEmpty()
                ) {

                    Spacer(

                        modifier =
                            Modifier.height(
                                6.dp
                            )
                    )


                    Text(

                        text =
                            "🎯 Nuby reforzará: ${progress.topicsToReinforce.joinToString(", ")}",

                        fontSize =
                            11.sp,

                        lineHeight =
                            16.sp,

                        color =
                            Color(0xFF7C3AED)
                    )
                }


                if (
                    badges.isNotEmpty()
                ) {

                    Spacer(

                        modifier =
                            Modifier.height(
                                7.dp
                            )
                    )


                    Text(

                        text =
                            "Insignias: ${badges.joinToString("  ")}",

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


        Spacer(

            modifier =
                Modifier.height(
                    16.dp
                )
        )


        // ====================================================================
        // TEMAS
        // ====================================================================

        healthyLearningTopics
            .chunked(
                2
            )
            .forEach {
                    rowTopics ->

                Row(

                    modifier =
                        Modifier.fillMaxWidth(),

                    horizontalArrangement =
                        androidx.compose.foundation.layout.Arrangement.spacedBy(
                            10.dp
                        )
                ) {

                    rowTopics.forEach {
                            topic ->

                        HealthyLearningTopicCard(

                            topic =
                                topic,

                            selected =
                                selectedTopic ==
                                    topic,

                            completed =
                                progress
                                    .completedTopics
                                    .contains(
                                        topic.title
                                    ),

                            modifier =
                                Modifier.weight(
                                    1f
                                ),

                            onClick = {

                                onTopicClick(
                                    topic
                                )
                            }
                        )
                    }


                    if (
                        rowTopics.size ==
                            1
                    ) {

                        Spacer(

                            modifier =
                                Modifier.weight(
                                    1f
                                )
                        )
                    }
                }


                Spacer(

                    modifier =
                        Modifier.height(
                            10.dp
                        )
                )
            }


        selectedTopic?.let {
                topic ->

            Spacer(

                modifier =
                    Modifier.height(
                        6.dp
                    )
            )


            HealthyLearningDetailCard(

                topic =
                    topic,

                completed =
                    progress
                        .completedTopics
                        .contains(
                            topic.title
                        ),

                onComplete = {

                    onCompleteTopic(
                        topic
                    )
                },

                onClose =
                    onCloseTopic
            )
        }


        Spacer(

            modifier =
                Modifier.height(
                    16.dp
                )
        )


        // ====================================================================
        // EVALUACIÓN
        // ====================================================================

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
                        Color(0xFFDDF7EE)
                )
        ) {

            Row(

                modifier =
                    Modifier.padding(
                        16.dp
                    ),

                verticalAlignment =
                    Alignment.CenterVertically
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
                            76.dp
                        ),

                    contentScale =
                        ContentScale.Fit
                )


                Spacer(

                    modifier =
                        Modifier.width(
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
                            "¿Qué aprendí sobre hábitos?",

                        fontSize =
                            16.sp,

                        fontWeight =
                            FontWeight.Bold,

                        color =
                            Color(0xFF0F766E)
                    )


                    Spacer(

                        modifier =
                            Modifier.height(
                                4.dp
                            )
                    )


                    Text(

                        text =
                            "Pon a prueba lo que aprendiste y gana XP.",

                        fontSize =
                            12.sp,

                        lineHeight =
                            17.sp,

                        color =
                            Color(0xFF475569)
                    )


                    Spacer(

                        modifier =
                            Modifier.height(
                                9.dp
                            )
                    )


                    Button(

                        onClick = {

                            showQuiz =
                                !showQuiz

                            lastResult =
                                null
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
                                    Color(0xFF0F766E)
                            )
                    ) {

                        Text(

                            text =
                                if (
                                    showQuiz
                                ) {
                                    "Cerrar evaluación"
                                } else if (
                                    progress.attempts ==
                                        0
                                ) {
                                    "Comenzar evaluación"
                                } else {
                                    "Intentar nuevamente"
                                },

                            fontWeight =
                                FontWeight.Bold
                        )
                    }
                }
            }
        }


        if (
            showQuiz
        ) {

            Spacer(

                modifier =
                    Modifier.height(
                        14.dp
                    )
            )


            HealthyLearningQuiz(

                progress =
                    progress,

                onFinished = {
                        result ->

                    lastResult =
                        result

                    onQuizFinished(
                        result
                    )

                    showQuiz =
                        false
                }
            )
        }


        lastResult?.let {
                result ->

            Spacer(

                modifier =
                    Modifier.height(
                        14.dp
                    )
            )


            HealthyLearningResult(

                result =
                    result,

                progress =
                    progress
            )
        }
    }
}




// ============================================================================
// EVALUACIÓN DE APRENDIZAJE
// ============================================================================

@Composable
private fun HealthyLearningQuiz(

    progress:
        HealthyLearningProgress,

    onFinished:
        (HealthyQuizResult) -> Unit
) {

    val quizQuestions =
        remember(
            progress.attempts,
            progress.bestScore,
            progress.topicsToReinforce
        ) {

            buildHealthyQuiz(
                progress
            )
        }


    var currentIndex by remember(
        quizQuestions
    ) {
        mutableIntStateOf(
            0
        )
    }


    var selectedAnswer by remember(
        quizQuestions
    ) {
        mutableIntStateOf(
            -1
        )
    }


    var score by remember(
        quizQuestions
    ) {
        mutableIntStateOf(
            0
        )
    }


    var showExplanation by remember(
        quizQuestions
    ) {
        mutableStateOf(
            false
        )
    }


    var failedTopics by remember(
        quizQuestions
    ) {
        mutableStateOf(
            emptySet<String>()
        )
    }


    if (
        quizQuestions.isEmpty()
    ) {

        Text(
            text =
                "No hay preguntas disponibles para esta evaluación."
        )

        return
    }


    val question =
        quizQuestions[
            currentIndex
        ]


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
                    5.dp
            )
    ) {

        Column(

            modifier =
                Modifier.padding(
                    16.dp
                )
        ) {

            Row(

                verticalAlignment =
                    Alignment.CenterVertically
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
                            62.dp
                        ),

                    contentScale =
                        ContentScale.Fit
                )


                Spacer(

                    modifier =
                        Modifier.width(
                            9.dp
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
                            "${question.difficulty.label} · ${question.topic}",

                        fontSize =
                            10.sp,

                        fontWeight =
                            FontWeight.Bold,

                        color =
                            Color(0xFF7C3AED)
                    )


                    Text(

                        text =
                            "Pregunta ${currentIndex + 1} de ${quizQuestions.size}",

                        fontSize =
                            11.sp,

                        color =
                            Color(0xFF64748B)
                    )


                    Text(

                        text =
                            question.question,

                        fontSize =
                            16.sp,

                        fontWeight =
                            FontWeight.Bold,

                        lineHeight =
                            21.sp,

                        color =
                            Color(0xFF0F172A)
                    )
                }
            }


            Spacer(

                modifier =
                    Modifier.height(
                        10.dp
                    )
            )


            LinearProgressIndicator(

                progress = {
                    (
                        currentIndex + 1
                    ).toFloat() /
                        quizQuestions.size.toFloat()
                },

                modifier =
                    Modifier.fillMaxWidth(),

                color =
                    Color(0xFF0F766E),

                trackColor =
                    Color(0xFFDDF7EE)
            )


            Spacer(

                modifier =
                    Modifier.height(
                        10.dp
                    )
            )


            TextToSpeechSmallButton(

                textToRead =
                    buildString {

                        append(
                            question.question
                        )

                        append(
                            ". "
                        )


                        question.options.forEachIndexed {
                                index,
                                option ->

                            append(
                                "Opción ${index + 1}. $option. "
                            )
                        }
                    }
            )


            Spacer(

                modifier =
                    Modifier.height(
                        12.dp
                    )
            )


            question.options.forEachIndexed {
                    index,
                    option ->

                Row(

                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(
                                vertical =
                                    3.dp
                            ),

                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    RadioButton(

                        selected =
                            selectedAnswer ==
                                index,

                        onClick = {

                            if (
                                !showExplanation
                            ) {

                                selectedAnswer =
                                    index
                            }
                        },

                        enabled =
                            !showExplanation,

                        colors =
                            RadioButtonDefaults.colors(
                                selectedColor =
                                    Color(0xFF0F766E)
                            )
                    )


                    Text(

                        text =
                            option,

                        modifier =
                            Modifier.weight(
                                1f
                            ),

                        fontSize =
                            13.sp,

                        lineHeight =
                            18.sp,

                        color =
                            Color(0xFF334155)
                    )
                }
            }


            if (
                showExplanation
            ) {

                Spacer(

                    modifier =
                        Modifier.height(
                            9.dp
                        )
                )


                val correct =
                    selectedAnswer ==
                        question.correctAnswer


                Card(

                    modifier =
                        Modifier.fillMaxWidth(),

                    shape =
                        RoundedCornerShape(
                            17.dp
                        ),

                    colors =
                        CardDefaults.cardColors(

                            containerColor =
                                if (
                                    correct
                                ) {
                                    Color(0xFFDDF7EE)
                                } else {
                                    Color(0xFFFFF3E5)
                                }
                        )
                ) {

                    Column(

                        modifier =
                            Modifier.padding(
                                13.dp
                            )
                    ) {

                        Text(

                            text =
                                if (
                                    correct
                                ) {
                                    "¡Correcto!"
                                } else {
                                    "Vamos a reforzarlo"
                                },

                            fontWeight =
                                FontWeight.Bold,

                            color =
                                if (
                                    correct
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
                                question.explanation,

                            fontSize =
                                12.sp,

                            lineHeight =
                                17.sp,

                            color =
                                Color(0xFF475569)
                        )


                        Spacer(

                            modifier =
                                Modifier.height(
                                    7.dp
                                )
                        )


                        TextToSpeechSmallButton(

                            textToRead =
                                question.explanation
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


            if (
                !showExplanation
            ) {

                Button(

                    onClick = {

                        if (
                            selectedAnswer ==
                                question.correctAnswer
                        ) {

                            score++

                        } else {

                            failedTopics =
                                failedTopics +
                                    question.topic
                        }


                        showExplanation =
                            true
                    },

                    enabled =
                        selectedAnswer >=
                            0,

                    modifier =
                        Modifier.fillMaxWidth(),

                    shape =
                        RoundedCornerShape(
                            16.dp
                        ),

                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor =
                                Color(0xFF0F766E)
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
                            currentIndex <
                                quizQuestions.lastIndex
                        ) {

                            currentIndex++

                            selectedAnswer =
                                -1

                            showExplanation =
                                false

                        } else {

                            onFinished(

                                HealthyQuizResult(

                                    score =
                                        score,

                                    total =
                                        quizQuestions.size,

                                    difficulty =
                                        healthyDifficultyFor(
                                            progress
                                        ).label,

                                    failedTopics =
                                        failedTopics
                                )
                            )
                        }
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
                                Color(0xFF0F766E)
                        )
                ) {

                    Text(

                        text =
                            if (
                                currentIndex ==
                                    quizQuestions.lastIndex
                            ) {
                                "Ver resultado"
                            } else {
                                "Siguiente pregunta"
                            }
                    )
                }
            }
        }
    }
}


// ============================================================================
// RESULTADO
// ============================================================================

@Composable
private fun HealthyLearningResult(

    result:
        HealthyQuizResult,

    progress:
        HealthyLearningProgress
) {

    val percentage =
        if (
            result.total > 0
        ) {
            (
                result.score * 100
            ) /
                result.total
        } else {
            0
        }


    val message =
        when {

            percentage >= 90 ->
                "¡Excelente! Ya reconoces muy bien varios hábitos que ayudan a cuidar tu bienestar."

            percentage >= 70 ->
                "¡Muy bien! Tienes una buena base. Repasar algunos temas puede ayudarte a reforzarla."

            percentage >= 50 ->
                "¡Vas avanzando! Revisa los temas que te parecieron más difíciles y vuelve a intentarlo."

            else ->
                "Cada intento cuenta. Repasa con calma y vuelve cuando estés listo. Nuby te acompaña."
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
                    Color(0xFFEAF8FF)
            )
    ) {

        Column(

            modifier =
                Modifier.padding(
                    16.dp
                ),

            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            Image(

                painter =
                    painterResource(
                        id =
                            R.drawable.nuby_abrazo
                    ),

                contentDescription =
                    "Nuby celebrando",

                modifier =
                    Modifier.size(
                        90.dp
                    ),

                contentScale =
                    ContentScale.Fit
            )


            Text(

                text =
                    "${result.score} / ${result.total}",

                fontSize =
                    28.sp,

                fontWeight =
                    FontWeight.Bold,

                color =
                    Color(0xFF0F766E)
            )


            Text(

                text =
                    "$percentage% · ${result.difficulty}",

                fontSize =
                    12.sp,

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
                    "Nivel actual: ${healthyLevelName(progress.xp)}",

                fontSize =
                    14.sp,

                fontWeight =
                    FontWeight.Bold,

                color =
                    Color(0xFF0369A1)
            )


            if (
                result.failedTopics
                    .isNotEmpty()
            ) {

                Spacer(

                    modifier =
                        Modifier.height(
                            8.dp
                        )
                )


                Card(

                    shape =
                        RoundedCornerShape(
                            16.dp
                        ),

                    colors =
                        CardDefaults.cardColors(
                            containerColor =
                                Color(0xFFF3E8FF)
                        )
                ) {

                    Text(

                        text =
                            "🎯 Temas para reforzar: ${result.failedTopics.joinToString(", ")}",

                        modifier =
                            Modifier.padding(
                                10.dp
                            ),

                        fontSize =
                            11.sp,

                        lineHeight =
                            16.sp,

                        color =
                            Color(0xFF6D28D9)
                    )
                }
            }


            Spacer(

                modifier =
                    Modifier.height(
                        8.dp
                    )
            )


            Text(

                text =
                    message,

                fontSize =
                    13.sp,

                lineHeight =
                    19.sp,

                textAlign =
                    TextAlign.Center,

                color =
                    Color(0xFF334155)
            )


            Spacer(

                modifier =
                    Modifier.height(
                        8.dp
                    )
            )


            TextToSpeechSmallButton(

                textToRead =
                    buildString {

                        append(
                            "Obtuviste ${result.score} de ${result.total} respuestas correctas. "
                        )

                        append(
                            "La dificultad fue ${result.difficulty}. "
                        )

                        if (
                            result.failedTopics
                                .isNotEmpty()
                        ) {

                            append(
                                "Te conviene reforzar: ${result.failedTopics.joinToString(", ")}. "
                            )
                        }

                        append(
                            message
                        )
                    }
            )
        }
    }
}


// ============================================================================
// TARJETA DE TEMA EDUCATIVO
// ============================================================================

@Composable
private fun HealthyLearningTopicCard(

    topic:
        HealthyLearningTopic,

    selected:
        Boolean,

    completed:
        Boolean,

    modifier:
        Modifier = Modifier,

    onClick:
        () -> Unit
) {

    Card(

        onClick =
            onClick,

        modifier =
            modifier
                .defaultMinSize(
                    minHeight =
                        145.dp
                ),

        shape =
            RoundedCornerShape(
                22.dp
            ),

        colors =
            CardDefaults.cardColors(

                containerColor =
                    if (
                        selected
                    ) {
                        Color(0xFFDDF7EE)
                    } else {
                        Color.White
                    }
            ),

        elevation =
            CardDefaults.cardElevation(

                defaultElevation =
                    if (
                        selected
                    ) {
                        7.dp
                    } else {
                        3.dp
                    }
            )
    ) {

        Column(

            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(
                        14.dp
                    ),

            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            Text(

                text =
                    topic.emoji,

                fontSize =
                    30.sp
            )


            Spacer(

                modifier =
                    Modifier.height(
                        7.dp
                    )
            )


            Text(

                text =
                    topic.title,

                fontSize =
                    14.sp,

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
                        5.dp
                    )
            )


            Text(

                text =
                    topic.subtitle,

                fontSize =
                    11.sp,

                lineHeight =
                    15.sp,

                color =
                    Color(0xFF64748B),

                textAlign =
                    TextAlign.Center
            )


            if (
                completed
            ) {

                Spacer(

                    modifier =
                        Modifier.height(
                            6.dp
                        )
                )


                Text(

                    text =
                        "✓ Completado",

                    fontSize =
                        10.sp,

                    fontWeight =
                        FontWeight.Bold,

                    color =
                        Color(0xFF0F766E)
                )
            }
        }
    }
}


// ============================================================================
// DETALLE DEL TEMA EDUCATIVO
// ============================================================================

@Composable
private fun HealthyLearningDetailCard(

    topic:
        HealthyLearningTopic,

    completed:
        Boolean,

    onComplete:
        () -> Unit,

    onClose:
        () -> Unit
) {

    val textoCompleto =
        buildString {

            append(
                topic.title
            )

            append(
                ". "
            )

            append(
                topic.subtitle
            )

            append(
                ". "
            )


            topic.paragraphs.forEach {
                    paragraph ->

                append(
                    paragraph
                )

                append(
                    ". "
                )
            }


            append(
                "Ejemplo. "
            )

            append(
                topic.example
            )

            append(
                ". Recuerda. "
            )

            append(
                topic.keyMessage
            )
        }


    Card(

        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(
                26.dp
            ),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    Color(0xFFFEFFF6)
            ),

        elevation =
            CardDefaults.cardElevation(
                defaultElevation =
                    6.dp
            )
    ) {

        Column(

            modifier =
                Modifier.padding(
                    17.dp
                )
        ) {

            Row(

                modifier =
                    Modifier.fillMaxWidth(),

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Image(

                    painter =
                        painterResource(
                            id =
                                topic.nubyRes
                        ),

                    contentDescription =
                        "Nuby acompañando el tema",

                    modifier =
                        Modifier.size(
                            78.dp
                        ),

                    contentScale =
                        ContentScale.Fit
                )


                Spacer(

                    modifier =
                        Modifier.width(
                            12.dp
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
                            "${topic.emoji} ${topic.title}",

                        fontSize =
                            18.sp,

                        fontWeight =
                            FontWeight.Bold,

                        color =
                            Color(0xFF0F766E)
                    )


                    Spacer(

                        modifier =
                            Modifier.height(
                                4.dp
                            )
                    )


                    Text(

                        text =
                            topic.subtitle,

                        fontSize =
                            12.sp,

                        lineHeight =
                            17.sp,

                        color =
                            Color(0xFF64748B)
                    )
                }
            }


            Spacer(

                modifier =
                    Modifier.height(
                        12.dp
                    )
            )


            TextToSpeechSmallButton(

                textToRead =
                    textoCompleto
            )


            Spacer(

                modifier =
                    Modifier.height(
                        14.dp
                    )
            )


            topic.paragraphs.forEach {
                    paragraph ->

                Card(

                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(
                                vertical =
                                    5.dp
                            ),

                    shape =
                        RoundedCornerShape(
                            17.dp
                        ),

                    colors =
                        CardDefaults.cardColors(
                            containerColor =
                                Color.White
                        )
                ) {

                    Row(

                        modifier =
                            Modifier.padding(
                                13.dp
                            )
                    ) {

                        Text(

                            text =
                                "✓",

                            color =
                                Color(0xFF22C55E),

                            fontWeight =
                                FontWeight.Bold
                        )


                        Spacer(

                            modifier =
                                Modifier.width(
                                    9.dp
                                )
                        )


                        Text(

                            text =
                                paragraph,

                            modifier =
                                Modifier.weight(
                                    1f
                                ),

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


            Spacer(

                modifier =
                    Modifier.height(
                        8.dp
                    )
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
                            Color(0xFFEAF8FF)
                    )
            ) {

                Column(

                    modifier =
                        Modifier.padding(
                            14.dp
                        )
                ) {

                    Text(

                        text =
                            "💡 Ejemplo",

                        fontWeight =
                            FontWeight.Bold,

                        color =
                            Color(0xFF0369A1)
                    )


                    Spacer(

                        modifier =
                            Modifier.height(
                                4.dp
                            )
                    )


                    Text(

                        text =
                            topic.example,

                        fontSize =
                            12.sp,

                        lineHeight =
                            18.sp,

                        color =
                            Color(0xFF334155)
                    )
                }
            }


            Spacer(

                modifier =
                    Modifier.height(
                        10.dp
                    )
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
                            Color(0xFFDDF7EE)
                    )
            ) {

                Column(

                    modifier =
                        Modifier.padding(
                            14.dp
                        )
                ) {

                    Text(

                        text =
                            "📌 Quédate con esto",

                        fontWeight =
                            FontWeight.Bold,

                        color =
                            Color(0xFF0F766E)
                    )


                    Spacer(

                        modifier =
                            Modifier.height(
                                4.dp
                            )
                    )


                    Text(

                        text =
                            topic.keyMessage,

                        fontSize =
                            12.sp,

                        lineHeight =
                            18.sp,

                        color =
                            Color(0xFF334155)
                    )
                }
            }


            Spacer(

                modifier =
                    Modifier.height(
                        12.dp
                    )
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
                            if (
                                completed
                            ) {
                                Color(0xFFDDF7EE)
                            } else {
                                Color(0xFFFFF7D6)
                            }
                    )
            ) {

                Column(

                    modifier =
                        Modifier.padding(
                            13.dp
                        )
                ) {

                    Text(

                        text =
                            if (
                                completed
                            ) {
                                "✅ Tema completado"
                            } else {
                                "¿Terminaste de revisar este tema?"
                            },

                        fontSize =
                            13.sp,

                        fontWeight =
                            FontWeight.Bold,

                        color =
                            Color(0xFF0F766E)
                    )


                    Spacer(

                        modifier =
                            Modifier.height(
                                4.dp
                            )
                    )


                    Text(

                        text =
                            if (
                                completed
                            ) {
                                "Ya recibiste los 10 XP de este tema. Puedes repasarlo cuando quieras."
                            } else {
                                "Marca el tema como aprendido y gana 10 XP."
                            },

                        fontSize =
                            11.sp,

                        lineHeight =
                            16.sp,

                        color =
                            Color(0xFF475569)
                    )


                    if (
                        !completed
                    ) {

                        Spacer(

                            modifier =
                                Modifier.height(
                                    9.dp
                                )
                        )


                        Button(

                            onClick =
                                onComplete,

                            modifier =
                                Modifier.fillMaxWidth(),

                            shape =
                                RoundedCornerShape(
                                    16.dp
                                ),

                            colors =
                                ButtonDefaults.buttonColors(
                                    containerColor =
                                        Color(0xFF0F766E)
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


            Spacer(

                modifier =
                    Modifier.height(
                        12.dp
                    )
            )


            OutlinedButton(

                onClick =
                    onClose,

                modifier =
                    Modifier.fillMaxWidth(),

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

                Text(

                    text =
                        "Cerrar tema",

                    fontWeight =
                        FontWeight.Bold
                )
            }
        }
    }
}


// ============================================================================
// IMAGEN CENTRAL
// ============================================================================

@Composable
fun HealthyCentralImagePlaceholder() {

    Card(

        modifier =
            Modifier
                .fillMaxWidth()
                .widthIn(
                    max = 360.dp
                ),

        shape =
            RoundedCornerShape(
                26.dp
            ),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    Color(0xFFEAF8FF)
            )
    ) {

        Row(

            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(
                        16.dp
                    ),

            verticalAlignment =
                Alignment.CenterVertically
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
                        88.dp
                    ),

                contentScale =
                    ContentScale.Fit
            )


            Spacer(

                modifier =
                    Modifier.width(
                        12.dp
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
                        "Pequeños cambios cuentan",

                    fontWeight =
                        FontWeight.Bold,

                    fontSize =
                        16.sp,

                    color =
                        Color(0xFF0F766E)
                )


                Spacer(

                    modifier =
                        Modifier.height(
                            5.dp
                        )
                )


                Text(

                    text =
                        "No necesitas cambiar todo en un día. Elige un hábito que puedas mantener y avanza poco a poco.",

                    fontSize =
                        13.sp,

                    lineHeight =
                        18.sp,

                    color =
                        Color(0xFF334155)
                )
            }
        }
    }
}


// ============================================================================
// TARJETAS DE HERRAMIENTAS
// ============================================================================

@Composable
fun HealthToolCard(

    title: String,

    subtitle: String,

    backgroundColor: Color,

    icon:
    @Composable () -> Unit,

    onClick: () -> Unit
) {


    Card(

        modifier =
            Modifier
                .fillMaxWidth()
                .defaultMinSize(
                    minHeight = 120.dp
                ),

        shape =
            RoundedCornerShape(
                24.dp
            ),

        colors =
            CardDefaults.cardColors(

                containerColor =
                    backgroundColor
            ),

        elevation =
            CardDefaults.cardElevation(

                defaultElevation =
                    5.dp
            )
    ) {


        Row(

            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(
                        18.dp
                    ),

            verticalAlignment =
                Alignment.CenterVertically
        ) {


            Box(

                modifier =
                    Modifier
                        .size(
                            60.dp
                        )
                        .clip(
                            RoundedCornerShape(
                                18.dp
                            )
                        )
                        .background(
                            Color.White
                        ),

                contentAlignment =
                    Alignment.Center
            ) {


                icon()
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


                Text(

                    text =
                        title,

                    fontSize =
                        15.sp,

                    fontWeight =
                        FontWeight.Bold
                )


                Spacer(

                    modifier =
                        Modifier.height(
                            4.dp
                        )
                )


                Text(

                    text =
                        subtitle,

                    fontSize =
                        12.sp,

                    color =
                        Color(0xFF64748B)
                )
            }


            Button(

                onClick =
                    onClick,

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
                        "Vamos"
                )
            }
        }
    }
}


// ============================================================================
// FONDO PROPIO DE HÁBITOS SALUDABLES
// ============================================================================

/**
 * Fondo utilizado por HealthyHabitsScreen.
 *
 * Esta función se encuentra dentro de este mismo archivo
 * para que HealthyHabitsScreen no dependa de la función
 * backgroundGradient() declarada en MainActivity.
 *
 * Esto corrige el error:
 *
 * Unresolved reference 'backgroundGradient'
 */
fun healthyHabitsBackgroundGradient(): Brush {


    return Brush.radialGradient(

        colors =
            listOf(

                // Azul claro.
                Color(0xFFE0F7FA),

                // Verde agua.
                Color(0xFFD1F5E8),

                // Verde lima muy claro.
                Color(0xFFF0F4C3)
            )
    )
}