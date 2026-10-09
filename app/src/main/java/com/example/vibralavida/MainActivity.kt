package com.example.vibralavida


// ============================================================================
// ANDROID
// ============================================================================

import android.Manifest
import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast


// ============================================================================
// ACTIVITY
// ============================================================================

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.BackHandler
import androidx.activity.result.contract.ActivityResultContracts


// ============================================================================
// ANIMACIONES
// ============================================================================

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween


// ============================================================================
// FOUNDATION
// ============================================================================

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable


// ============================================================================
// LAYOUT
// ============================================================================

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.verticalScroll


// ============================================================================
// FORMAS
// ============================================================================

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape


// ============================================================================
// MATERIAL 3
// ============================================================================

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.lightColorScheme


// ============================================================================
// COMPOSE STATE
// ============================================================================

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue


// ============================================================================
// UI
// ============================================================================

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch


// ============================================================================
// CORE
// ============================================================================

import androidx.core.content.ContextCompat


// ============================================================================
// FIREBASE
// ============================================================================

import com.google.firebase.auth.FirebaseAuth


// ============================================================================
// API
// ============================================================================

import com.example.vibralavida.api.PerfilRepository
import com.example.vibralavida.api.MedicamentosRepository
import com.example.vibralavida.api.BitacoraRepository
import com.example.vibralavida.api.CloudinaryRepository
import com.example.vibralavida.api.CitasRepository
import com.example.vibralavida.api.CitasPacienteRepository
import com.example.vibralavida.api.AdherenciaMedicamentosRepository
import com.example.vibralavida.api.ResultsRepository
import com.example.vibralavida.api.modelos.PerfilRequest
import com.example.vibralavida.api.modelos.MedicamentoRequest
import com.example.vibralavida.api.modelos.RegistroSaludRequest
import com.example.vibralavida.api.modelos.EstudioLaboratorioRequest
import com.example.vibralavida.api.modelos.AdherenciaMedicamentoRequest
import com.example.vibralavida.api.modelos.ResultadoRequest
import com.example.vibralavida.api.modelos.UsuarioPerfil


// ============================================================================
// PANTALLAS PRINCIPALES
// ============================================================================

import com.example.vibralavida.pantallas_principales.InitialProfileScreen
import com.example.vibralavida.pantallas_principales.LoginScreen
import com.example.vibralavida.pantallas_principales.RegisterScreen
import com.example.vibralavida.pantallas_principales.HomeScreen
import com.example.vibralavida.pantallas_principales.ProfileScreen
import com.example.vibralavida.mapa_profesionales.MapaProfesionalesScreen
import com.example.vibralavida.seguimiento.MiEquipoSaludScreen
import com.example.vibralavida.diabetes_mellitus.DiabetesMellitusScreen
import com.example.vibralavida.agenda.MiAgendaScreen
import com.example.vibralavida.agenda.historial.HistorialAgendaScreen


// ============================================================================
// HÁBITOS SALUDABLES
// ============================================================================

import com.example.vibralavida.habitos_saludables.HealthyHabitsScreen
import com.example.vibralavida.habitos_saludables.SleepModeScreen
import com.example.vibralavida.habitos_saludables.SleepSurveyScreen
import com.example.vibralavida.habitos_saludables.MoodSurveyMenuScreen
import com.example.vibralavida.habitos_saludables.DepressionSurveyScreen
import com.example.vibralavida.habitos_saludables.AnxietySurveyScreen
import com.example.vibralavida.habitos_saludables.StressSurveyScreen
import com.example.vibralavida.habitos_saludables.ImcCalculatorScreen
import com.example.vibralavida.habitos_saludables.CaloriesCalculatorScreen
import com.example.vibralavida.habitos_saludables.CardiovascularRiskCalculatorScreen


// ============================================================================
// TRASTORNOS DEL RITMO
// ============================================================================

import com.example.vibralavida.trastornos_ritmo.HealthConnectScreen


// ============================================================================
// NUBY - GUÍA INTERACTIVA
// ============================================================================

import com.example.vibralavida.ia.NubyChatScreen


// ============================================================================
// AGENDA - MEDICAMENTOS
// ============================================================================

import com.example.vibralavida.agenda.medicamentos.Medicamento
import com.example.vibralavida.agenda.medicamentos.MedicamentosScreen
import com.example.vibralavida.agenda.medicamentos.AgregarMedicamentoScreen
import com.example.vibralavida.agenda.medicamentos.NotificacionMedicamento
import com.example.vibralavida.agenda.medicamentos.ProgramadorRecordatoriosMedicamento
import com.example.vibralavida.agenda.medicamentos.RegistroAdherenciaMedicamento


// ============================================================================
// AGENDA - BITÁCORA
// ============================================================================

import com.example.vibralavida.agenda.bitacora.RegistroSalud
import com.example.vibralavida.agenda.bitacora.EstudioLaboratorio
import com.example.vibralavida.agenda.bitacora.BitacoraSaludScreen
import com.example.vibralavida.agenda.bitacora.AgregarBitacoraScreen


// ============================================================================
// AGENDA - CITAS
// ============================================================================

import com.example.vibralavida.agenda.citas.Cita
import com.example.vibralavida.agenda.citas.CitasScreen
import com.example.vibralavida.agenda.citas.NotificacionCita
import com.example.vibralavida.agenda.citas.ProgramadorRecordatoriosCita


// ============================================================================
// FECHA Y HORA
// ============================================================================

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


// ============================================================================
// MAIN ACTIVITY
// ============================================================================

class MainActivity : ComponentActivity() {


    // ========================================================================
    // PERMISO DE NOTIFICACIONES
    // ========================================================================

    private val solicitarNotificacionesLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { permitido ->

            if (permitido) {

                println(
                    "Permiso de notificaciones concedido"
                )

            } else {

                println(
                    "Permiso de notificaciones rechazado"
                )
            }
        }


    // ========================================================================
    // ON CREATE
    // ========================================================================

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(
            savedInstanceState
        )


        // ====================================================================
        // CANAL DE NOTIFICACIONES
        // ====================================================================

        NotificacionMedicamento
            .crearCanal(
                this
            )


        // ====================================================================
        // CANAL DE NOTIFICACIONES DE CITAS
        // ====================================================================

        NotificacionCita
            .crearCanal(
                this
            )


        // ====================================================================
        // PERMISO DE NOTIFICACIONES
        // ====================================================================

        solicitarPermisoNotificaciones()


        // ====================================================================
        // PERMISO DE ALARMAS EXACTAS
        // ====================================================================

        comprobarPermisoAlarmasExactas()


        // ====================================================================
        // INTERFAZ
        // ====================================================================

        setContent {

            VibraLaVidaTheme {

                Surface(

                    modifier =
                        Modifier.fillMaxSize(),

                    color =
                        MaterialTheme
                            .colorScheme
                            .background
                ) {

                    AppScreen()
                }
            }
        }
    }


    // ========================================================================
    // SOLICITAR PERMISO DE NOTIFICACIONES
    // ========================================================================

    private fun solicitarPermisoNotificaciones() {

        if (
            Build.VERSION.SDK_INT >=
            Build.VERSION_CODES.TIRAMISU
        ) {

            val permisoActual =
                ContextCompat.checkSelfPermission(

                    this,

                    Manifest.permission.POST_NOTIFICATIONS
                )


            if (
                permisoActual !=
                PackageManager.PERMISSION_GRANTED
            ) {

                solicitarNotificacionesLauncher
                    .launch(
                        Manifest.permission.POST_NOTIFICATIONS
                    )
            }
        }
    }


    // ========================================================================
    // COMPROBAR PERMISO DE ALARMAS EXACTAS
    // ========================================================================

    private fun comprobarPermisoAlarmasExactas() {

        if (
            Build.VERSION.SDK_INT >=
            Build.VERSION_CODES.S
        ) {

            val alarmManager =
                getSystemService(
                    Context.ALARM_SERVICE
                ) as AlarmManager


            if (
                alarmManager.canScheduleExactAlarms()
            ) {

                return
            }


            try {

                val intent =
                    Intent(
                        Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
                    ).apply {

                        data =
                            Uri.parse(
                                "package:$packageName"
                            )
                    }


                startActivity(
                    intent
                )

            } catch (
                e: Exception
            ) {

                e.printStackTrace()
            }
        }
    }
}


// ============================================================================
// PANTALLAS
// ============================================================================

enum class Screen {

    Splash,

    Auth,

    Login,

    Register,

    InitialProfile,

    Home,

    Profile,

    HealthyHabits,

    SleepMode,

    MoodSurveyMenu,

    SleepSurvey,

    DepressionSurvey,

    AnxietySurvey,

    StressSurvey,

    ImcCalculator,

    CaloriesCalculator,

    CardioRiskCalculator,

    HealthConnect,

    // ========================================================================
    // DIABETES MELLITUS
    // ========================================================================

    DiabetesMellitus,


    // ========================================================================
    // NUBY - GUÍA INTERACTIVA
    // ========================================================================

    NubyChat,

    // ========================================================================
    // MAPA DE PROFESIONALES
    // ========================================================================

    MapaProfesionales,

    // ========================================================================
    // MI EQUIPO DE SALUD
    // ========================================================================

    MiEquipoSalud,


    // ========================================================================
    // AGENDA
    // ========================================================================

    MiAgenda,

    Medicamentos,

    AgregarMedicamento,

    BitacoraSalud,

    AgregarBitacora,

    Citas,

    HistorialAgenda
}


// ============================================================================
// NAVEGACIÓN GLOBAL
// ============================================================================
//
// Estas funciones permiten:
//
// - conservar un historial real entre pantallas;
// - usar correctamente el botón Atrás de Android;
// - saber qué pantallas forman parte de la sesión principal.
//
// ============================================================================

private fun Screen.isAuthenticatedAppScreen(): Boolean {

    return this !in setOf(

        Screen.Splash,

        Screen.Auth,

        Screen.Login,

        Screen.Register,

        Screen.InitialProfile
    )
}


// ============================================================================
// SECCIÓN PRINCIPAL DEL DRAWER
// ============================================================================
//
// Si estamos en una subpantalla, resaltamos su módulo principal.
//
// ============================================================================

private fun Screen.drawerSection(): Screen {

    return when (this) {

        Screen.SleepMode,
        Screen.MoodSurveyMenu,
        Screen.SleepSurvey,
        Screen.DepressionSurvey,
        Screen.AnxietySurvey,
        Screen.StressSurvey,
        Screen.ImcCalculator,
        Screen.CaloriesCalculator,
        Screen.CardioRiskCalculator ->
            Screen.HealthyHabits

        Screen.Medicamentos,
        Screen.AgregarMedicamento,
        Screen.BitacoraSalud,
        Screen.AgregarBitacora,
        Screen.Citas,
        Screen.HistorialAgenda ->
            Screen.MiAgenda

        else ->
            this
    }
}


// ============================================================================
// PERFIL COMPLETO
// ============================================================================
//
// Determina si un usuario ya terminó la pantalla
// "Queremos conocerte".
//
// No exigimos enfermedades crónicas porque una lista vacía
// es totalmente válida si el usuario indicó que no padece ninguna.
//
// ============================================================================

fun perfilEstaCompleto(
    perfil: UsuarioPerfil
): Boolean {

    return !perfil.edad.isNullOrBlank() &&
            !perfil.genero.isNullOrBlank() &&
            !perfil.peso.isNullOrBlank() &&
            !perfil.estatura.isNullOrBlank() &&
            !perfil.nivelActividad.isNullOrBlank()
}


// ============================================================================
// APP SCREEN
// ============================================================================

@Composable
fun AppScreen() {


    // ========================================================================
    // CONTEXTO
    // ========================================================================

    val context =
        LocalContext.current


    // ========================================================================
    // PANTALLA ACTUAL
    // ========================================================================

    var currentScreen by remember {

        mutableStateOf(
            Screen.Splash
        )
    }


    // ========================================================================
    // DRAWER GLOBAL
    // ========================================================================

    val appDrawerState =
        rememberDrawerState(
            initialValue =
                DrawerValue.Closed
        )


    val appDrawerScope =
        rememberCoroutineScope()


    // ========================================================================
    // HISTORIAL DE NAVEGACIÓN
    // ========================================================================
    //
    // No necesitamos cambiar todas las asignaciones currentScreen = ...
    // que ya existen en el proyecto.
    //
    // Observamos los cambios de pantalla y guardamos automáticamente
    // la pantalla anterior.
    //
    // ========================================================================

    val navigationHistory =
        remember {

            mutableStateListOf<Screen>()
        }


    var lastObservedScreen by remember {

        mutableStateOf(
            currentScreen
        )
    }


    var skipNextHistoryPush by remember {

        mutableStateOf(
            false
        )
    }


    LaunchedEffect(
        currentScreen
    ) {

        if (
            currentScreen !=
            lastObservedScreen
        ) {

            if (
                skipNextHistoryPush
            ) {

                skipNextHistoryPush =
                    false

            } else if (
                lastObservedScreen
                    .isAuthenticatedAppScreen() &&
                currentScreen
                    .isAuthenticatedAppScreen()
            ) {

                if (
                    navigationHistory
                        .lastOrNull() !=
                    lastObservedScreen
                ) {

                    navigationHistory.add(
                        lastObservedScreen
                    )
                }

            } else if (
                !currentScreen
                    .isAuthenticatedAppScreen()
            ) {

                navigationHistory.clear()
            }


            lastObservedScreen =
                currentScreen
        }
    }


    // ========================================================================
    // BOTÓN ATRÁS DE ANDROID
    // ========================================================================
    //
    // 1. Si el menú lateral está abierto, lo cierra.
    // 2. Si existe historial, regresa una pantalla.
    // 3. Si estamos en una subpantalla sin historial, regresa a Inicio.
    // 4. Si estamos en Inicio sin historial, Android conserva su
    //    comportamiento normal y puede salir/minimizar la app.
    //
    // ========================================================================

    BackHandler(

        enabled =
            appDrawerState.isOpen ||
            (
                currentScreen
                    .isAuthenticatedAppScreen() &&
                (
                    navigationHistory
                        .isNotEmpty() ||
                    currentScreen !=
                        Screen.Home
                )
            )
    ) {

        if (
            appDrawerState.isOpen
        ) {

            appDrawerScope.launch {

                appDrawerState.close()
            }

        } else if (
            navigationHistory
                .isNotEmpty()
        ) {

            val previousScreen =
                navigationHistory.removeAt(
                    navigationHistory.lastIndex
                )


            skipNextHistoryPush =
                true


            currentScreen =
                previousScreen

        } else if (
            currentScreen !=
                Screen.Home
        ) {

            skipNextHistoryPush =
                true


            currentScreen =
                Screen.Home
        }
    }


    // ========================================================================
    // PERFIL
    // ========================================================================

    var userName by remember {

        mutableStateOf("")
    }


    var userAge by remember {

        mutableStateOf("")
    }


    var userGender by remember {

        mutableStateOf("")
    }


    var userWeight by remember {

        mutableStateOf("")
    }


    var userHeight by remember {

        mutableStateOf("")
    }


    var userActivityLevel by remember {

        mutableStateOf("")
    }


    // ========================================================================
    // RESULTADOS DE LA CALCULADORA DE CALORÍAS
    // ========================================================================
    //
    // Se conservan únicamente durante la sesión actual.
    //
    // Nuby los recibe de forma selectiva solo cuando la pregunta está
    // relacionada con alimentación, peso o calorías.
    //
    // ========================================================================

    var userLoseCalories by remember {

        mutableStateOf<Int?>(
            null
        )
    }


    var userMaintainCalories by remember {

        mutableStateOf<Int?>(
            null
        )
    }


    var userGainCalories by remember {

        mutableStateOf<Int?>(
            null
        )
    }


    // ========================================================================
    // ENFERMEDADES CRÓNICAS
    // ========================================================================

    var userChronicDiseases by remember {

        mutableStateOf<List<String>>(
            emptyList()
        )
    }


    var userOtherChronicDisease by remember {

        mutableStateOf("")
    }


    // ========================================================================
    // FOTO DE PERFIL
    // ========================================================================

    var profileImageUri by remember {

        mutableStateOf<Uri?>(
            null
        )
    }


    // ========================================================================
    // MODO DE EDICIÓN DEL PERFIL
    // ========================================================================
    //
    // false = el usuario está completando su perfil por primera vez.
    // true  = el usuario ya tenía perfil y entró desde "Editar perfil".
    //
    // Esto evita que el botón "Volver" deje una sesión incompleta activa.
    //
    // ========================================================================

    var editandoPerfilExistente by remember {

        mutableStateOf(false)
    }


    // ========================================================================
    // GUARDANDO PERFIL
    // ========================================================================
    //
    // Permite mostrar un indicador de carga en "Queremos conocerte"
    // mientras Render/Firebase terminan de guardar la información.
    //
    // ========================================================================

    var guardandoPerfil by remember {

        mutableStateOf(false)
    }


    // ========================================================================
    // ESCALA DE ATENAS
    // ========================================================================

    var sleepSurveyScore by remember {

        mutableStateOf<Int?>(
            null
        )
    }


    // ========================================================================
    // MEDICAMENTOS
    // ========================================================================

    val medicamentos =
        remember {

            mutableStateListOf<Medicamento>()
        }


    var medicamentoEnEdicion by remember {

        mutableStateOf<Medicamento?>(
            null
        )
    }


    // ========================================================================
    // ADHERENCIA DE MEDICAMENTOS
    // ========================================================================
    //
    // Guarda temporalmente en memoria los registros que el paciente reportó
    // como "tomado" u "omitido".
    //
    // La fuente persistente sigue siendo Firestore mediante la API.
    //
    // ========================================================================

    val registrosAdherencia =
        remember {

            mutableStateListOf<RegistroAdherenciaMedicamento>()
        }


    // ========================================================================
    // BITÁCORA DE SALUD
    // ========================================================================

    val registrosSalud =
        remember {

            mutableStateListOf<RegistroSalud>()
        }


    val estudiosLaboratorio =
        remember {

            mutableStateListOf<EstudioLaboratorio>()
        }


    var registroSaludEnEdicion by remember {

        mutableStateOf<RegistroSalud?>(
            null
        )
    }


    var estudioLaboratorioEnEdicion by remember {

        mutableStateOf<EstudioLaboratorio?>(
            null
        )
    }


    // ========================================================================
    // CITAS MÉDICAS
    // ========================================================================
    //
    // Se cargan desde:
    //
    // GET /api/citas
    //
    // El backend identifica al paciente mediante su token de Firebase.
    //
    // ========================================================================

    val citas =
        remember {

            mutableStateListOf<Cita>()
        }


    // ========================================================================
    // SESIÓN PERSISTENTE
    // ========================================================================
    //
    // IMPORTANTE:
    //
    // La aplicación SIEMPRE inicia visualmente en Splash.
    //
    // La sesión persistente se revisa cuando el usuario toca el logo.
    // Así evitamos que una sesión antigua o incompleta cambie de pantalla
    // antes de que el usuario interactúe con la app.
    //
    // ========================================================================


    // ========================================================================
    // NAVEGACIÓN
    // ========================================================================

    ModalNavigationDrawer(

        drawerState =
            appDrawerState,

        gesturesEnabled =
            currentScreen
                .isAuthenticatedAppScreen() &&
            currentScreen !=
                Screen.Home,

        drawerContent = {

            AppGlobalDrawerContent(

                currentScreen =
                    currentScreen,

                onNavigate = {
                        destination ->

                    appDrawerScope.launch {

                        appDrawerState.close()
                    }


                    if (
                        destination !=
                            currentScreen
                    ) {

                        currentScreen =
                            destination
                    }
                },

                onClose = {

                    appDrawerScope.launch {

                        appDrawerState.close()
                    }
                }
            )
        }
    ) {

        when (
            currentScreen
        ) {


        // ====================================================================
        // SPLASH
        // ====================================================================

        Screen.Splash -> {

            SplashScreen(

                onLogoClick = {

                    val usuarioFirebase =
                        FirebaseAuth
                            .getInstance()
                            .currentUser


                    // ========================================================
                    // NO HAY SESIÓN GUARDADA
                    // ========================================================

                    if (
                        usuarioFirebase == null
                    ) {

                        currentScreen =
                            Screen.Auth

                    } else {


                        // ====================================================
                        // HAY SESIÓN: VALIDAR PERFIL CON LA API
                        // ====================================================

                        PerfilRepository
                            .obtenerPerfil(

                                onSuccess = {
                                        response ->


                                    val perfil =
                                        response.user


                                    if (
                                        perfil != null &&
                                        perfilEstaCompleto(
                                            perfil
                                        )
                                    ) {


                                        // ------------------------------------
                                        // CARGAR NOMBRE
                                        // ------------------------------------

                                        userName =

                                            perfil.nombreCompleto
                                                ?.takeIf {
                                                    it.isNotBlank()
                                                }

                                                ?: perfil.nombre
                                                    ?.takeIf {
                                                        it.isNotBlank()
                                                    }

                                                        ?: usuarioFirebase
                                                    .displayName
                                                    .orEmpty()


                                        // ------------------------------------
                                        // CARGAR PERFIL
                                        // ------------------------------------

                                        userAge =
                                            perfil.edad.orEmpty()

                                        userGender =
                                            perfil.genero.orEmpty()

                                        userWeight =
                                            perfil.peso.orEmpty()

                                        userHeight =
                                            perfil.estatura.orEmpty()

                                        userActivityLevel =
                                            perfil.nivelActividad.orEmpty()

                                        userChronicDiseases =
                                            perfil.enfermedadesCronicas

                                        userOtherChronicDisease =
                                            perfil.otraEnfermedadCronica.orEmpty()


                                        editandoPerfilExistente =
                                            false


                                        currentScreen =
                                            Screen.Home


                                    } else {


                                        // ------------------------------------
                                        // SESIÓN ANTIGUA / PERFIL INCOMPLETO
                                        // ------------------------------------
                                        //
                                        // No mandamos automáticamente a
                                        // "Queremos conocerte".
                                        //
                                        // Cerramos la sesión antigua y
                                        // mostramos las opciones de acceso.
                                        //
                                        // ------------------------------------

                                        FirebaseAuth
                                            .getInstance()
                                            .signOut()


                                        currentScreen =
                                            Screen.Auth
                                    }
                                },


                                onProfileNotFound = {


                                    // El usuario existe en Auth, pero no tiene
                                    // un perfil guardado en Firestore.
                                    //
                                    // Para evitar el salto inesperado a
                                    // "Queremos conocerte", cerramos esta
                                    // sesión antigua y mostramos Auth.

                                    FirebaseAuth
                                        .getInstance()
                                        .signOut()


                                    currentScreen =
                                        Screen.Auth
                                },


                                onUnauthorized = {

                                    FirebaseAuth
                                        .getInstance()
                                        .signOut()


                                    currentScreen =
                                        Screen.Auth
                                },


                                onError = {
                                        mensaje ->


                                    Toast
                                        .makeText(

                                            context,

                                            "No fue posible recuperar tu sesión: $mensaje",

                                            Toast.LENGTH_LONG
                                        )
                                        .show()


                                    // No cerramos sesión por un error temporal
                                    // de conexión o porque Render esté despertando.

                                    currentScreen =
                                        Screen.Auth
                                }
                            )
                    }
                }
            )
        }


        // ====================================================================
        // AUTH
        // ====================================================================

        Screen.Auth -> {

            AuthScreen(

                onBack = {

                    currentScreen =
                        Screen.Splash
                },

                onLoginClick = {

                    currentScreen =
                        Screen.Login
                },

                onRegisterClick = {

                    currentScreen =
                        Screen.Register
                }
            )
        }


        // ====================================================================
        // LOGIN
        // ====================================================================

        Screen.Login -> {

            LoginScreen(

                onBack = {

                    currentScreen =
                        Screen.Auth
                },


                // ============================================================
                // LOGIN CORRECTO
                // ============================================================

                onLoginSuccess = {


                    val usuarioFirebase =
                        FirebaseAuth
                            .getInstance()
                            .currentUser


                    // ========================================================
                    // COMPROBAR SESIÓN
                    // ========================================================

                    if (
                        usuarioFirebase == null
                    ) {

                        Toast
                            .makeText(

                                context,

                                "No existe una sesión activa.",

                                Toast.LENGTH_LONG
                            )
                            .show()

                    } else {


                        // ====================================================
                        // OBTENER PERFIL
                        // ====================================================

                        PerfilRepository
                            .obtenerPerfil(


                                // =============================================
                                // PERFIL ENCONTRADO
                                // =============================================

                                onSuccess = {
                                        response ->


                                    val perfil =
                                        response.user


                                    if (
                                        perfil == null
                                    ) {

                                        userName =
                                            usuarioFirebase
                                                .displayName
                                                .orEmpty()


                                        currentScreen =
                                            Screen.InitialProfile

                                    } else {


                                        // =====================================
                                        // NOMBRE
                                        // =====================================

                                        userName =

                                            perfil.nombreCompleto
                                                ?.takeIf {
                                                    it.isNotBlank()
                                                }

                                                ?: perfil.nombre
                                                    ?.takeIf {
                                                        it.isNotBlank()
                                                    }

                                                        ?: usuarioFirebase
                                                    .displayName
                                                    .orEmpty()


                                        // =====================================
                                        // PERFIL
                                        // =====================================

                                        userAge =
                                            perfil.edad.orEmpty()


                                        userGender =
                                            perfil.genero.orEmpty()


                                        userWeight =
                                            perfil.peso.orEmpty()


                                        userHeight =
                                            perfil.estatura.orEmpty()


                                        userActivityLevel =
                                            perfil
                                                .nivelActividad
                                                .orEmpty()


                                        // =====================================
                                        // ENFERMEDADES
                                        // =====================================

                                        userChronicDiseases =
                                            perfil
                                                .enfermedadesCronicas


                                        userOtherChronicDisease =
                                            perfil
                                                .otraEnfermedadCronica
                                                .orEmpty()


                                        // =====================================
                                        // DECIDIR PANTALLA
                                        // =====================================

                                        if (
                                            perfilEstaCompleto(
                                                perfil
                                            )
                                        ) {

                                            currentScreen =
                                                Screen.Home

                                        } else {

                                            currentScreen =
                                                Screen.InitialProfile
                                        }
                                    }
                                },


                                // =============================================
                                // NO TIENE PERFIL
                                // =============================================

                                onProfileNotFound = {

                                    userName =
                                        usuarioFirebase
                                            .displayName
                                            .orEmpty()


                                    currentScreen =
                                        Screen.InitialProfile
                                },


                                // =============================================
                                // TOKEN INVÁLIDO
                                // =============================================

                                onUnauthorized = {

                                    FirebaseAuth
                                        .getInstance()
                                        .signOut()


                                    Toast
                                        .makeText(

                                            context,

                                            "Tu sesión no es válida. Inicia sesión nuevamente.",

                                            Toast.LENGTH_LONG
                                        )
                                        .show()


                                    currentScreen =
                                        Screen.Login
                                },


                                // =============================================
                                // ERROR
                                // =============================================

                                onError = {
                                        mensaje ->


                                    Toast
                                        .makeText(

                                            context,

                                            mensaje,

                                            Toast.LENGTH_LONG
                                        )
                                        .show()
                                }
                            )
                    }
                },


                // ============================================================
                // IR AL REGISTRO
                // ============================================================

                onGoToRegister = {

                    currentScreen =
                        Screen.Register
                }
            )
        }


        // ====================================================================
        // REGISTRO
        // ====================================================================

        Screen.Register -> {

            RegisterScreen(

                onBack = {

                    currentScreen =
                        Screen.Auth
                },

                onRegisterSuccess = {


                    // ========================================================
                    // FIREBASE YA DEJÓ AL USUARIO AUTENTICADO
                    // ========================================================

                    userName =
                        FirebaseAuth
                            .getInstance()
                            .currentUser
                            ?.displayName
                            .orEmpty()


                    editandoPerfilExistente =
                        false


                    currentScreen =
                        Screen.InitialProfile
                }
            )
        }


        // ====================================================================
        // PERFIL INICIAL
        // ====================================================================

        Screen.InitialProfile -> {

            InitialProfileScreen(

                isSavingProfile =
                    guardandoPerfil,

                onBack = {


                    // ========================================================
                    // SI VENIMOS DE "EDITAR PERFIL"
                    // ========================================================

                    if (
                        editandoPerfilExistente
                    ) {

                        editandoPerfilExistente =
                            false


                        currentScreen =
                            Screen.Profile

                    } else {


                        // ====================================================
                        // PERFIL INICIAL NO TERMINADO
                        // ====================================================
                        //
                        // Si el usuario sale de "Queremos conocerte" antes de
                        // guardar, cerramos Firebase Auth.
                        //
                        // Esto evita que al abrir nuevamente la app quede una
                        // sesión incompleta y salte directo a esta pantalla.
                        //
                        // ====================================================

                        FirebaseAuth
                            .getInstance()
                            .signOut()


                        currentScreen =
                            Screen.Auth
                    }
                },


                // ============================================================
                // TERMINAR PERFIL
                // ============================================================

                onFinish = {
                        age,
                        gender,
                        weight,
                        height,
                        activityLevel,
                        chronicDiseases,
                        otherChronicDisease ->


                    // ========================================================
                    // CREAR REQUEST PARA LA API
                    // ========================================================

                    val perfilRequest =
                        PerfilRequest(

                            edad =
                                age,

                            genero =
                                gender,

                            peso =
                                weight,

                            estatura =
                                height,

                            nivelActividad =
                                activityLevel,

                            enfermedadesCronicas =
                                chronicDiseases,

                            otraEnfermedadCronica =
                                otherChronicDisease
                        )


                    // ========================================================
                    // GUARDAR PERFIL
                    // ========================================================

                    guardandoPerfil =
                        true


                    PerfilRepository
                        .guardarPerfil(

                            perfil =
                                perfilRequest,


                            // =================================================
                            // ÉXITO
                            // =================================================

                            onSuccess = {


                                // --------------------------------------------
                                // TERMINÓ LA CARGA
                                // --------------------------------------------

                                guardandoPerfil =
                                    false


                                // --------------------------------------------
                                // NOMBRE
                                // --------------------------------------------

                                userName =
                                    FirebaseAuth
                                        .getInstance()
                                        .currentUser
                                        ?.displayName
                                        .orEmpty()


                                // --------------------------------------------
                                // PERFIL LOCAL
                                // --------------------------------------------

                                userAge =
                                    age


                                userGender =
                                    gender


                                userWeight =
                                    weight


                                userHeight =
                                    height


                                userActivityLevel =
                                    activityLevel


                                // --------------------------------------------
                                // INVALIDAR RESULTADOS DE CALORÍAS ANTERIORES
                                // --------------------------------------------
                                //
                                // Si cambió el perfil, una estimación anterior
                                // ya puede no corresponder a los nuevos datos.
                                //
                                // --------------------------------------------

                                userLoseCalories =
                                    null


                                userMaintainCalories =
                                    null


                                userGainCalories =
                                    null


                                userChronicDiseases =
                                    chronicDiseases


                                userOtherChronicDisease =
                                    otherChronicDisease


                                editandoPerfilExistente =
                                    false


                                // --------------------------------------------
                                // MENSAJE
                                // --------------------------------------------

                                Toast
                                    .makeText(

                                        context,

                                        "Perfil guardado correctamente",

                                        Toast.LENGTH_SHORT
                                    )
                                    .show()


                                // --------------------------------------------
                                // HOME
                                // --------------------------------------------

                                currentScreen =
                                    Screen.Home
                            },


                            // =================================================
                            // ERROR
                            // =================================================

                            onError = {
                                    mensaje ->


                                guardandoPerfil =
                                    false


                                Toast
                                    .makeText(

                                        context,

                                        "Error: $mensaje",

                                        Toast.LENGTH_LONG
                                    )
                                    .show()
                            }
                        )
                }
            )
        }


        // ====================================================================
        // HOME
        // ====================================================================

        Screen.Home -> {

            HomeScreen(

                userName =
                    userName.ifBlank {
                        "Usuario"
                    },

                onProfileClick = {

                    currentScreen =
                        Screen.Profile
                },

                onRhythmClick = {

                    currentScreen =
                        Screen.HealthConnect
                },

                onHealthyLifeClick = {

                    currentScreen =
                        Screen.HealthyHabits
                },

                onDiabetesClick = {

                    currentScreen =
                        Screen.DiabetesMellitus
                },

                onAgendaClick = {

                    currentScreen =
                        Screen.MiAgenda
                },


                // ============================================================
                // NUBY - GUÍA INTERACTIVA
                // ============================================================

                onNubyClick = {

                    currentScreen =
                        Screen.NubyChat
                },


                // ============================================================
                // MAPA DE PROFESIONALES
                // ============================================================

                onProfessionalsMapClick = {

                    currentScreen =
                        Screen.MapaProfesionales
                },


                // ============================================================
                // MI EQUIPO DE SALUD
                // ============================================================

                onHealthTeamClick = {

                    currentScreen =
                        Screen.MiEquipoSalud
                }


            )
        }


        // ====================================================================
        // MAPA DE PROFESIONALES
        // ====================================================================

        Screen.MapaProfesionales -> {

            MapaProfesionalesScreen(

                onBack = {

                    currentScreen =
                        Screen.Home
                }
            )
        }


        // ====================================================================
        // MI EQUIPO DE SALUD
        // ====================================================================

        Screen.MiEquipoSalud -> {

            MiEquipoSaludScreen(

                onBack = {

                    currentScreen =
                        Screen.Home
                },

                onMenuClick = {

                    appDrawerScope.launch {

                        appDrawerState.open()
                    }
                },

                onUnauthorized = {

                    FirebaseAuth
                        .getInstance()
                        .signOut()


                    Toast
                        .makeText(

                            context,

                            "Tu sesión no es válida. Inicia sesión nuevamente.",

                            Toast.LENGTH_LONG
                        )
                        .show()


                    currentScreen =
                        Screen.Login
                }
            )
        }


        // ====================================================================
        // NUBY - GUÍA INTERACTIVA
        // ====================================================================

        Screen.NubyChat -> {

            NubyChatScreen(

                onBack = {

                    currentScreen =
                        Screen.Home
                },

                onMenuClick = {

                    appDrawerScope.launch {

                        appDrawerState.open()
                    }
                },

                userAge =
                    userAge,

                userGender =
                    userGender,

                userWeight =
                    userWeight,

                userHeight =
                    userHeight,

                userActivityLevel =
                    userActivityLevel,

                userLoseCalories =
                    userLoseCalories,

                userMaintainCalories =
                    userMaintainCalories,

                userGainCalories =
                    userGainCalories
            )
        }


        // ====================================================================
        // PERFIL
        // ====================================================================

        Screen.Profile -> {

            ProfileScreen(

                userName =
                    userName.ifBlank {
                        "Usuario"
                    },

                age =
                    userAge,

                weight =
                    userWeight,

                height =
                    userHeight,

                activityLevel =
                    userActivityLevel,

                profileImageUri =
                    profileImageUri,


                // ============================================================
                // REGRESAR
                // ============================================================

                onBack = {

                    currentScreen =
                        Screen.Home
                },


                // ============================================================
                // EDITAR PERFIL
                // ============================================================

                onEditProfile = {

                    editandoPerfilExistente =
                        true


                    currentScreen =
                        Screen.InitialProfile
                },


                // ============================================================
                // CAMBIAR FOTO
                // ============================================================

                onImageSelected = {
                        uri ->


                    profileImageUri =
                        uri
                },


                // ============================================================
                // CERRAR SESIÓN
                // ============================================================
                //
                // Aquí sí cerramos realmente Firebase Authentication.
                //
                // No solamente regresamos a Login.
                //
                // ============================================================

                onLogout = {


                    // ========================================================
                    // FIREBASE AUTH
                    // ========================================================

                    FirebaseAuth
                        .getInstance()
                        .signOut()


                    // ========================================================
                    // LIMPIAR PERFIL EN MEMORIA
                    // ========================================================

                    userName =
                        ""


                    userAge =
                        ""


                    userGender =
                        ""


                    userWeight =
                        ""


                    userHeight =
                        ""


                    userActivityLevel =
                        ""


                    userLoseCalories =
                        null


                    userMaintainCalories =
                        null


                    userGainCalories =
                        null


                    userChronicDiseases =
                        emptyList()


                    userOtherChronicDisease =
                        ""


                    // ========================================================
                    // FOTO
                    // ========================================================

                    profileImageUri =
                        null


                    // ========================================================
                    // ENCUESTAS
                    // ========================================================

                    sleepSurveyScore =
                        null


                    // ========================================================
                    // MEDICAMENTOS
                    // ========================================================
                    //
                    // Por ahora estos datos viven solamente en memoria.
                    //
                    // Los limpiamos para evitar que otro usuario
                    // vea información del usuario anterior.
                    //
                    // ========================================================

                    medicamentos.clear()


                    medicamentoEnEdicion =
                        null


                    registrosAdherencia.clear()


                    // ========================================================
                    // BITÁCORA
                    // ========================================================

                    registrosSalud.clear()


                    estudiosLaboratorio.clear()


                    registroSaludEnEdicion =
                        null


                    estudioLaboratorioEnEdicion =
                        null


                    // ========================================================
                    // CITAS
                    // ========================================================

                    citas.clear()


                    // ========================================================
                    // MENSAJE
                    // ========================================================

                    Toast
                        .makeText(

                            context,

                            "Sesión cerrada correctamente",

                            Toast.LENGTH_SHORT
                        )
                        .show()


                    // ========================================================
                    // REGRESAR AL INICIO DE AUTENTICACIÓN
                    // ========================================================

                    editandoPerfilExistente =
                        false


                    guardandoPerfil =
                        false


                    currentScreen =
                        Screen.Auth
                },


                // ============================================================
                // ELIMINAR CUENTA
                // ============================================================
                //
                // ProfileScreen muestra la confirmación y el indicador de
                // carga. Aquí hacemos la eliminación real mediante la API.
                //
                // ============================================================

                onDeleteAccount = {
                        onFinished ->


                    PerfilRepository
                        .eliminarCuenta(

                            // =================================================
                            // CUENTA ELIMINADA CORRECTAMENTE
                            // =================================================

                            onSuccess = {


                                // --------------------------------------------
                                // LIMPIAR SESIÓN LOCAL DE FIREBASE
                                // --------------------------------------------

                                FirebaseAuth
                                    .getInstance()
                                    .signOut()


                                // --------------------------------------------
                                // LIMPIAR PERFIL EN MEMORIA
                                // --------------------------------------------

                                userName =
                                    ""


                                userAge =
                                    ""


                                userGender =
                                    ""


                                userWeight =
                                    ""


                                userHeight =
                                    ""


                                userActivityLevel =
                                    ""


                                userChronicDiseases =
                                    emptyList()


                                userOtherChronicDisease =
                                    ""


                                profileImageUri =
                                    null


                                // --------------------------------------------
                                // LIMPIAR ESTADOS DE LA APP
                                // --------------------------------------------

                                sleepSurveyScore =
                                    null


                                medicamentos.clear()


                                medicamentoEnEdicion =
                                    null


                                registrosAdherencia.clear()


                                registrosSalud.clear()


                                estudiosLaboratorio.clear()


                                registroSaludEnEdicion =
                                    null


                                estudioLaboratorioEnEdicion =
                                    null


                                citas.clear()


                                editandoPerfilExistente =
                                    false


                                guardandoPerfil =
                                    false


                                // --------------------------------------------
                                // TERMINAR CARGA
                                // --------------------------------------------

                                onFinished()


                                Toast
                                    .makeText(

                                        context,

                                        "Cuenta eliminada correctamente",

                                        Toast.LENGTH_SHORT
                                    )
                                    .show()


                                currentScreen =
                                    Screen.Auth
                            },


                            // =================================================
                            // CUENTA MULTIRROL
                            // =================================================
                            //
                            // El backend detectó que esta persona también
                            // tiene acceso como especialista.
                            //
                            // NO cerramos sesión.
                            // NO borramos datos locales.
                            // NO cambiamos de pantalla.
                            //
                            // =================================================

                            onMultiRoleAccount = {
                                    mensaje ->


                                onFinished()


                                Toast
                                    .makeText(

                                        context,

                                        mensaje,

                                        Toast.LENGTH_LONG
                                    )
                                    .show()
                            },


                            // =================================================
                            // SESIÓN INVÁLIDA
                            // =================================================

                            onUnauthorized = {


                                onFinished()


                                FirebaseAuth
                                    .getInstance()
                                    .signOut()


                                Toast
                                    .makeText(

                                        context,

                                        "Tu sesión no es válida. Inicia sesión nuevamente.",

                                        Toast.LENGTH_LONG
                                    )
                                    .show()


                                currentScreen =
                                    Screen.Login
                            },


                            // =================================================
                            // ERROR AL ELIMINAR
                            // =================================================

                            onError = {
                                    mensaje ->


                                onFinished()


                                Toast
                                    .makeText(

                                        context,

                                        "No fue posible eliminar la cuenta: $mensaje",

                                        Toast.LENGTH_LONG
                                    )
                                    .show()
                            }
                        )
                }
            )
        }


        // ====================================================================
        // HÁBITOS SALUDABLES
        // ====================================================================

        Screen.HealthyHabits -> {

            HealthyHabitsScreen(

                userName =
                    userName.ifBlank {
                        "Usuario"
                    },

                userAge =
                    userAge,

                sleepSurveyScore =
                    sleepSurveyScore,

                onBackToMenu = {

                    currentScreen =
                        Screen.Home
                },

                onMenuClick = {

                    appDrawerScope.launch {

                        appDrawerState.open()
                    }
                },

                onProfileClick = {

                    currentScreen =
                        Screen.Profile
                },

                onMoodSurveyClick = {

                    currentScreen =
                        Screen.MoodSurveyMenu
                },

                onSleepSurveyClick = {

                    currentScreen =
                        Screen.SleepSurvey
                },

                onSleepModeClick = {

                    currentScreen =
                        Screen.SleepMode
                },

                onImcClick = {

                    currentScreen =
                        Screen.ImcCalculator
                },

                onCaloriesClick = {

                    currentScreen =
                        Screen.CaloriesCalculator
                },

                onCardioRiskClick = {

                    currentScreen =
                        Screen.CardioRiskCalculator
                }
            )
        }


        // ====================================================================
        // MODO SUEÑO
        // ====================================================================

        Screen.SleepMode -> {

            SleepModeScreen(

                onBack = {

                    currentScreen =
                        Screen.HealthyHabits
                },

                onSleepFinished = {

                    currentScreen =
                        Screen.HealthyHabits
                }
            )
        }


        // ====================================================================
        // ESCALA DE ATENAS
        // ====================================================================

        Screen.SleepSurvey -> {

            SleepSurveyScreen(

                userName =
                    userName.ifBlank {
                        "Usuario"
                    },

                onBackToMenu = {

                    appDrawerScope.launch {

                        appDrawerState.open()
                    }
                },

                onProfileClick = {

                    currentScreen =
                        Screen.Profile
                },

                onResultCalculated = {
                        score ->


                    sleepSurveyScore =
                        score
                }
            )
        }


        // ====================================================================
        // MENÚ ESTADO DE ÁNIMO
        // ====================================================================

        Screen.MoodSurveyMenu -> {

            MoodSurveyMenuScreen(

                userName =
                    userName.ifBlank {
                        "Usuario"
                    },

                onBackToMenu = {

                    appDrawerScope.launch {

                        appDrawerState.open()
                    }
                },

                onProfileClick = {

                    currentScreen =
                        Screen.Profile
                },

                onDailyMoodClick = {

                    currentScreen =
                        Screen.DepressionSurvey
                },

                onAnxietyClick = {

                    currentScreen =
                        Screen.AnxietySurvey
                },

                onStressClick = {

                    currentScreen =
                        Screen.StressSurvey
                }
            )
        }


        // ====================================================================
        // DEPRESIÓN
        // ====================================================================

        Screen.DepressionSurvey -> {

            DepressionSurveyScreen(

                userName =
                    userName.ifBlank {
                        "Usuario"
                    },

                onBackToMenu = {

                    appDrawerScope.launch {

                        appDrawerState.open()
                    }
                },

                onProfileClick = {

                    currentScreen =
                        Screen.Profile
                }
            )
        }


        // ====================================================================
        // ANSIEDAD
        // ====================================================================

        Screen.AnxietySurvey -> {

            AnxietySurveyScreen(

                userName =
                    userName.ifBlank {
                        "Usuario"
                    },

                onBackToMenu = {

                    appDrawerScope.launch {

                        appDrawerState.open()
                    }
                },

                onProfileClick = {

                    currentScreen =
                        Screen.Profile
                }
            )
        }


        // ====================================================================
        // ESTRÉS
        // ====================================================================

        Screen.StressSurvey -> {

            StressSurveyScreen(

                userName =
                    userName.ifBlank {
                        "Usuario"
                    },

                onBackToMenu = {

                    appDrawerScope.launch {

                        appDrawerState.open()
                    }
                },

                onProfileClick = {

                    currentScreen =
                        Screen.Profile
                }
            )
        }


        // ====================================================================
        // IMC
        // ====================================================================

        Screen.ImcCalculator -> {

            ImcCalculatorScreen(

                userName =
                    userName.ifBlank {
                        "Usuario"
                    },

                onBackToMenu = {

                    appDrawerScope.launch {

                        appDrawerState.open()
                    }
                },

                onProfileClick = {

                    currentScreen =
                        Screen.Profile
                },


                // ============================================================
                // GUARDAR RESULTADO DEL IMC
                // ============================================================
                //
                // CalculatorScreens calcula y muestra el resultado como antes.
                // Además, nos entrega aquí los datos para guardarlos mediante
                // POST /api/results usando el token de Firebase del usuario.
                //
                // El backend lo almacena en:
                // usuarios/{uid}/resultados/{id}
                //
                // ============================================================

                onImcCalculated = {
                        edad,
                        genero,
                        pesoKg,
                        alturaCm,
                        imc,
                        categoria,
                        descripcion ->


                    val resultadoRequest =
                        ResultadoRequest(

                            tipo =
                                "imc",

                            categoria =
                                categoria,

                            puntaje =
                                imc,

                            clasificacion =
                                categoria,

                            descripcion =
                                descripcion,

                            respuestas =
                                null,

                            datosExtra =
                                mapOf(
                                    "nombreHerramienta" to
                                            "Calculadora de IMC",
                                    "edad" to
                                            edad,
                                    "genero" to
                                            genero,
                                    "pesoKg" to
                                            pesoKg,
                                    "alturaCm" to
                                            alturaCm,
                                    "imc" to
                                            imc,
                                    "categoria" to
                                            categoria
                                )
                        )


                    ResultsRepository
                        .guardarResultado(

                            resultado =
                                resultadoRequest,

                            onSuccess = {

                                Toast
                                    .makeText(

                                        context,

                                        "Resultado de IMC guardado",

                                        Toast.LENGTH_SHORT
                                    )
                                    .show()
                            },

                            onUnauthorized = {

                                FirebaseAuth
                                    .getInstance()
                                    .signOut()


                                Toast
                                    .makeText(

                                        context,

                                        "Tu sesión no es válida. Inicia sesión nuevamente.",

                                        Toast.LENGTH_LONG
                                    )
                                    .show()


                                currentScreen =
                                    Screen.Login
                            },

                            onError = {
                                    mensaje ->


                                Toast
                                    .makeText(

                                        context,

                                        "El IMC se calculó, pero no pudo guardarse: $mensaje",

                                        Toast.LENGTH_LONG
                                    )
                                    .show()
                            }
                        )
                }
            )
        }


        // ====================================================================
        // CALORÍAS
        // ====================================================================

        Screen.CaloriesCalculator -> {

            CaloriesCalculatorScreen(

                userName =
                    userName.ifBlank {
                        "Usuario"
                    },

                onBackToMenu = {

                    appDrawerScope.launch {

                        appDrawerState.open()
                    }
                },

                onProfileClick = {

                    currentScreen =
                        Screen.Profile
                },


                // ============================================================
                // GUARDAR RESULTADOS PARA NUBY
                // ============================================================

                onCaloriesCalculated = {
                        lose,
                        maintain,
                        gain ->


                    userLoseCalories =
                        lose


                    userMaintainCalories =
                        maintain


                    userGainCalories =
                        gain
                }
            )
        }


        // ====================================================================
        // RIESGO CARDIOVASCULAR
        // ====================================================================

        Screen.CardioRiskCalculator -> {

            CardiovascularRiskCalculatorScreen(

                userName =
                    userName.ifBlank {
                        "Usuario"
                    },

                onBackToMenu = {

                    appDrawerScope.launch {

                        appDrawerState.open()
                    }
                },

                onProfileClick = {

                    currentScreen =
                        Screen.Profile
                }
            )
        }


        // ====================================================================
        // DIABETES MELLITUS
        // ====================================================================

        Screen.DiabetesMellitus -> {

            DiabetesMellitusScreen(

                onBack = {

                    currentScreen =
                        Screen.Home
                },

                onMenuClick = {

                    appDrawerScope.launch {

                        appDrawerState.open()
                    }
                }
            )
        }


        // ====================================================================
        // HEALTH CONNECT
        // ====================================================================

        Screen.HealthConnect -> {

            HealthConnectScreen(

                onBackToMenu = {

                    currentScreen =
                        Screen.Home
                },

                onMenuClick = {

                    appDrawerScope.launch {

                        appDrawerState.open()
                    }
                }
            )
        }


        // ====================================================================
        // MI AGENDA
        // ====================================================================

        Screen.MiAgenda -> {

            MiAgendaScreen(

                onBack = {

                    currentScreen =
                        Screen.Home
                },

                onMedicamentosClick = {

                    // Abrimos primero la pantalla de medicamentos.
                    currentScreen =
                        Screen.Medicamentos


                    // ========================================================
                    // CARGAR MEDICAMENTOS DESDE FIRESTORE
                    // ========================================================

                    MedicamentosRepository
                        .obtenerMedicamentos(

                            onSuccess = {
                                    lista ->


                                medicamentos.clear()

                                medicamentos.addAll(
                                    lista
                                )


                                // Reprogramamos recordatorios usando los datos
                                // persistidos del usuario.

                                lista.forEach {
                                        medicamento ->


                                    ProgramadorRecordatoriosMedicamento
                                        .programarMedicamento(

                                            context =
                                                context,

                                            medicamento =
                                                medicamento
                                        )
                                }
                            },

                            onError = {
                                    mensaje ->


                                Toast
                                    .makeText(

                                        context,

                                        "No fue posible cargar los medicamentos: $mensaje",

                                        Toast.LENGTH_LONG
                                    )
                                    .show()
                            }
                        )


                    // ========================================================
                    // CARGAR ADHERENCIA DE MEDICAMENTOS
                    // ========================================================
                    //
                    // Recuperamos lo que el paciente ya reportó anteriormente
                    // para que al volver a abrir la pantalla se mantenga el
                    // estado "Tomado" u "Omitido".
                    //
                    // ========================================================

                    AdherenciaMedicamentosRepository
                        .obtenerHistorial(

                            onSuccess = {
                                    lista ->


                                registrosAdherencia.clear()


                                registrosAdherencia.addAll(
                                    lista
                                )
                            },

                            onError = {
                                    mensaje ->


                                Toast
                                    .makeText(

                                        context,

                                        "No fue posible cargar el historial de medicamentos: $mensaje",

                                        Toast.LENGTH_LONG
                                    )
                                    .show()
                            }
                        )
                },

                onLaboratoriosClick = {

                    currentScreen =
                        Screen.BitacoraSalud


                    // ========================================================
                    // CARGAR MEDICIONES DESDE FIRESTORE
                    // ========================================================

                    BitacoraRepository
                        .obtenerRegistros(

                            onSuccess = {
                                    lista ->


                                registrosSalud.clear()

                                registrosSalud.addAll(
                                    lista
                                )
                            },

                            onError = {
                                    mensaje ->


                                Toast
                                    .makeText(

                                        context,

                                        "No fue posible cargar las mediciones: $mensaje",

                                        Toast.LENGTH_LONG
                                    )
                                    .show()
                            }
                        )


                    // ========================================================
                    // CARGAR LABORATORIOS DESDE FIRESTORE
                    // ========================================================

                    BitacoraRepository
                        .obtenerLaboratorios(

                            onSuccess = {
                                    lista ->


                                estudiosLaboratorio.clear()

                                estudiosLaboratorio.addAll(
                                    lista
                                )
                            },

                            onError = {
                                    mensaje ->


                                Toast
                                    .makeText(

                                        context,

                                        "No fue posible cargar los laboratorios: $mensaje",

                                        Toast.LENGTH_LONG
                                    )
                                    .show()
                            }
                        )
                },

                onCitasClick = {

                    // ========================================================
                    // ABRIR PANTALLA DE CITAS
                    // ========================================================

                    currentScreen =
                        Screen.Citas


                    // ========================================================
                    // CARGAR CITAS DESDE LA API
                    // ========================================================
                    //
                    // La app móvil solamente consulta las citas del paciente.
                    // La creación/edición se realiza desde el panel web.
                    //
                    // ========================================================

                    CitasRepository
                        .obtenerCitas(

                            onSuccess = {
                                    lista ->


                                citas.clear()


                                citas.addAll(
                                    lista
                                )


                                // --------------------------------------------
                                // PROGRAMAR RECORDATORIOS LOCALES
                                // --------------------------------------------

                                ProgramadorRecordatoriosCita
                                    .programarCitas(

                                        context =
                                            context,

                                        citas =
                                            lista
                                    )
                            },

                            onUnauthorized = {


                                FirebaseAuth
                                    .getInstance()
                                    .signOut()


                                Toast
                                    .makeText(

                                        context,

                                        "Tu sesión no es válida. Inicia sesión nuevamente.",

                                        Toast.LENGTH_LONG
                                    )
                                    .show()


                                currentScreen =
                                    Screen.Login
                            },

                            onError = {
                                    mensaje ->


                                Toast
                                    .makeText(

                                        context,

                                        "No fue posible cargar las citas: $mensaje",

                                        Toast.LENGTH_LONG
                                    )
                                    .show()
                            }
                        )
                },

                onHistorialClick = {

                    // ========================================================
                    // ABRIR HISTORIAL DE AGENDA
                    // ========================================================

                    currentScreen =
                        Screen.HistorialAgenda


                    // ========================================================
                    // CARGAR ADHERENCIA DE MEDICAMENTOS
                    // ========================================================

                    AdherenciaMedicamentosRepository
                        .obtenerHistorial(

                            onSuccess = {
                                    lista ->


                                registrosAdherencia.clear()


                                registrosAdherencia.addAll(
                                    lista
                                )
                            },

                            onError = {
                                    mensaje ->


                                Toast
                                    .makeText(

                                        context,

                                        "No fue posible cargar el historial de medicamentos: $mensaje",

                                        Toast.LENGTH_LONG
                                    )
                                    .show()
                            }
                        )


                    // ========================================================
                    // CARGAR CITAS
                    // ========================================================

                    CitasRepository
                        .obtenerCitas(

                            onSuccess = {
                                    lista ->


                                citas.clear()


                                citas.addAll(
                                    lista
                                )


                                // --------------------------------------------
                                // PROGRAMAR RECORDATORIOS LOCALES
                                // --------------------------------------------

                                ProgramadorRecordatoriosCita
                                    .programarCitas(

                                        context =
                                            context,

                                        citas =
                                            lista
                                    )
                            },

                            onUnauthorized = {


                                FirebaseAuth
                                    .getInstance()
                                    .signOut()


                                Toast
                                    .makeText(

                                        context,

                                        "Tu sesión no es válida. Inicia sesión nuevamente.",

                                        Toast.LENGTH_LONG
                                    )
                                    .show()


                                currentScreen =
                                    Screen.Login
                            },

                            onError = {
                                    mensaje ->


                                Toast
                                    .makeText(

                                        context,

                                        "No fue posible cargar el historial de citas: $mensaje",

                                        Toast.LENGTH_LONG
                                    )
                                    .show()
                            }
                        )


                    // ========================================================
                    // CARGAR MEDICIONES DE SALUD
                    // ========================================================

                    BitacoraRepository
                        .obtenerRegistros(

                            onSuccess = {
                                    lista ->


                                registrosSalud.clear()


                                registrosSalud.addAll(
                                    lista
                                )
                            },

                            onError = {
                                    mensaje ->


                                Toast
                                    .makeText(

                                        context,

                                        "No fue posible cargar las mediciones del historial: $mensaje",

                                        Toast.LENGTH_LONG
                                    )
                                    .show()
                            }
                        )


                    // ========================================================
                    // CARGAR LABORATORIOS
                    // ========================================================

                    BitacoraRepository
                        .obtenerLaboratorios(

                            onSuccess = {
                                    lista ->


                                estudiosLaboratorio.clear()


                                estudiosLaboratorio.addAll(
                                    lista
                                )
                            },

                            onError = {
                                    mensaje ->


                                Toast
                                    .makeText(

                                        context,

                                        "No fue posible cargar los laboratorios del historial: $mensaje",

                                        Toast.LENGTH_LONG
                                    )
                                    .show()
                            }
                        )
                }
            )
        }



        // ====================================================================
        // HISTORIAL DE AGENDA
        // ====================================================================

        Screen.HistorialAgenda -> {

            HistorialAgendaScreen(

                registrosAdherencia =
                    registrosAdherencia,

                citas =
                    citas,

                registrosSalud =
                    registrosSalud,

                estudiosLaboratorio =
                    estudiosLaboratorio,

                onBack = {

                    currentScreen =
                        Screen.MiAgenda
                }
            )
        }


        // ====================================================================
        // CITAS MÉDICAS
        // ====================================================================

        Screen.Citas -> {

            CitasScreen(

                citas =
                    citas,

                onBack = {

                    currentScreen =
                        Screen.MiAgenda
                },


                // ============================================================
                // CONFIRMAR ASISTENCIA
                // ============================================================
                //
                // El paciente confirma que planea asistir a la cita.
                //
                // Esto NO cambia directamente el estado general de la cita.
                // Se guarda por separado en "estadoPaciente".
                //
                // ============================================================

                onConfirmarAsistencia = {
                        cita,
                        contrasena ->


                    CitasPacienteRepository
                        .confirmarCita(

                            citaId =
                                cita.id,

                            contrasena =
                                contrasena,

                            onSuccess = {
                                    citaActualizada,
                                    mensaje ->


                                val indice =
                                    citas.indexOfFirst {
                                            actual ->

                                        actual.id ==
                                                citaActualizada.id
                                    }


                                if (
                                    indice != -1
                                ) {

                                    citas[indice] =
                                        citaActualizada
                                }


                                Toast
                                    .makeText(

                                        context,

                                        mensaje,

                                        Toast.LENGTH_SHORT
                                    )
                                    .show()
                            },

                            onUnauthorized = {

                                FirebaseAuth
                                    .getInstance()
                                    .signOut()


                                Toast
                                    .makeText(

                                        context,

                                        "Tu sesión no es válida. Inicia sesión nuevamente.",

                                        Toast.LENGTH_LONG
                                    )
                                    .show()


                                currentScreen =
                                    Screen.Login
                            },

                            onError = {
                                    mensaje ->

                                Toast
                                    .makeText(

                                        context,

                                        mensaje,

                                        Toast.LENGTH_LONG
                                    )
                                    .show()
                            }
                        )
                },


                // ============================================================
                // SOLICITAR REAGENDACIÓN
                // ============================================================
                //
                // El paciente propone una nueva fecha y hora.
                //
                // La cita original NO se modifica automáticamente.
                // El profesional debe revisar la solicitud.
                //
                // ============================================================

                onSolicitarReagendacion = {
                        cita,
                        fecha,
                        hora,
                        motivo ->


                    CitasPacienteRepository
                        .solicitarReagenda(

                            citaId =
                                cita.id,

                            motivo =
                                motivo,

                            fechaSolicitada =
                                fecha,

                            horaSolicitada =
                                hora,

                            onSuccess = {
                                    citaActualizada,
                                    mensaje ->


                                val indice =
                                    citas.indexOfFirst {
                                            actual ->

                                        actual.id ==
                                                citaActualizada.id
                                    }


                                if (
                                    indice != -1
                                ) {

                                    citas[indice] =
                                        citaActualizada
                                }


                                Toast
                                    .makeText(

                                        context,

                                        mensaje,

                                        Toast.LENGTH_SHORT
                                    )
                                    .show()
                            },

                            onUnauthorized = {

                                FirebaseAuth
                                    .getInstance()
                                    .signOut()


                                currentScreen =
                                    Screen.Login
                            },

                            onError = {
                                    mensaje ->

                                Toast
                                    .makeText(

                                        context,

                                        mensaje,

                                        Toast.LENGTH_LONG
                                    )
                                    .show()
                            }
                        )
                },


                // ============================================================
                // SOLICITAR CANCELACIÓN
                // ============================================================
                //
                // La cita NO se cancela automáticamente.
                //
                // Guardamos una solicitud para que el profesional la revise.
                //
                // ============================================================

                onSolicitarCancelacion = {
                        cita,
                        motivo ->


                    CitasPacienteRepository
                        .cancelarCita(

                            citaId =
                                cita.id,

                            motivo =
                                motivo,

                            onSuccess = {
                                    citaActualizada,
                                    mensaje ->


                                // Quitamos la cita cancelada de la agenda activa.
                                citas.removeAll {
                                        actual ->

                                    actual.id ==
                                        citaActualizada.id
                                }


                                Toast
                                    .makeText(

                                        context,

                                        mensaje,

                                        Toast.LENGTH_SHORT
                                    )
                                    .show()
                            },

                            onUnauthorized = {

                                FirebaseAuth
                                    .getInstance()
                                    .signOut()


                                currentScreen =
                                    Screen.Login
                            },

                            onError = {
                                    mensaje ->

                                Toast
                                    .makeText(

                                        context,

                                        mensaje,

                                        Toast.LENGTH_LONG
                                    )
                                    .show()
                            }
                        )
                }


            )
        }


        // ====================================================================
        // MEDICAMENTOS
        // ====================================================================

        Screen.Medicamentos -> {

            MedicamentosScreen(

                medicamentos =
                    medicamentos,

                onBack = {

                    currentScreen =
                        Screen.MiAgenda
                },

                onAgregarMedicamentoClick = {

                    medicamentoEnEdicion =
                        null

                    currentScreen =
                        Screen.AgregarMedicamento
                },

                onEditarMedicamento = {
                        medicamento ->

                    medicamentoEnEdicion =
                        medicamento

                    currentScreen =
                        Screen.AgregarMedicamento
                },

                onEliminarMedicamento = {
                        medicamento ->

                    MedicamentosRepository
                        .eliminarMedicamento(

                            medicamentoId =
                                medicamento.id,

                            onSuccess = {

                                ProgramadorRecordatoriosMedicamento
                                    .cancelarMedicamento(

                                        context =
                                            context,

                                        medicamento =
                                            medicamento
                                    )


                                medicamentos.removeAll {
                                        actual ->

                                    actual.id ==
                                        medicamento.id
                                }


                                Toast
                                    .makeText(

                                        context,

                                        "Medicamento eliminado",

                                        Toast.LENGTH_SHORT
                                    )
                                    .show()
                            },

                            onError = {
                                    mensaje ->

                                Toast
                                    .makeText(

                                        context,

                                        "No fue posible eliminar el medicamento: $mensaje",

                                        Toast.LENGTH_LONG
                                    )
                                    .show()
                            }
                        )
                }
            )
        }


        // ====================================================================
        // AGREGAR / EDITAR MEDICAMENTO
        // ====================================================================

        Screen.AgregarMedicamento -> {

            AgregarMedicamentoScreen(

                medicamentoInicial =
                    medicamentoEnEdicion,

                onBack = {

                    medicamentoEnEdicion =
                        null


                    currentScreen =
                        Screen.Medicamentos
                },

                onGuardar = {
                        nombre,
                        dosis,
                        presentacion,
                        horarios,
                        fechaInicio,
                        fechaFin,
                        indicaciones,
                        recordatorioActivo,
                        fotoUri ->


                    // ========================================================
                    // REQUEST PARA LA API
                    // ========================================================

                    val medicamentoRequest =
                        MedicamentoRequest(

                            nombre =
                                nombre,

                            dosis =
                                dosis,

                            presentacion =
                                presentacion,

                            horarios =
                                horarios,

                            fechaInicio =
                                fechaInicio,

                            fechaFin =
                                fechaFin,

                            indicaciones =
                                indicaciones,

                            recordatorioActivo =
                                recordatorioActivo,

                            fotoUri =
                                fotoUri
                        )


                    // ========================================================
                    // EDITAR
                    // ========================================================

                    if (
                        medicamentoEnEdicion != null
                    ) {

                        val medicamentoOriginal =
                            medicamentoEnEdicion!!


                        MedicamentosRepository
                            .actualizarMedicamento(

                                medicamentoId =
                                    medicamentoOriginal.id,

                                medicamento =
                                    medicamentoRequest,

                                onSuccess = {


                                    // ----------------------------------------
                                    // CANCELAR ALARMAS ANTERIORES
                                    // ----------------------------------------

                                    ProgramadorRecordatoriosMedicamento
                                        .cancelarMedicamento(

                                            context =
                                                context,

                                            medicamento =
                                                medicamentoOriginal
                                        )


                                    // ----------------------------------------
                                    // ACTUALIZAR LISTA LOCAL
                                    // ----------------------------------------

                                    val indice =
                                        medicamentos
                                            .indexOfFirst {
                                                    medicamento ->

                                                medicamento.id ==
                                                        medicamentoOriginal.id
                                            }


                                    if (
                                        indice != -1
                                    ) {

                                        val medicamentoActualizado =
                                            medicamentoOriginal.copy(

                                                nombre =
                                                    nombre,

                                                dosis =
                                                    dosis,

                                                presentacion =
                                                    presentacion,

                                                horarios =
                                                    horarios,

                                                fechaInicio =
                                                    fechaInicio,

                                                fechaFin =
                                                    fechaFin,

                                                indicaciones =
                                                    indicaciones,

                                                recordatorioActivo =
                                                    recordatorioActivo,

                                                fotoUri =
                                                    fotoUri
                                            )


                                        medicamentos[indice] =
                                            medicamentoActualizado


                                        // ------------------------------------
                                        // PROGRAMAR NUEVAS ALARMAS
                                        // ------------------------------------

                                        ProgramadorRecordatoriosMedicamento
                                            .programarMedicamento(

                                                context =
                                                    context,

                                                medicamento =
                                                    medicamentoActualizado
                                            )
                                    }


                                    medicamentoEnEdicion =
                                        null


                                    Toast
                                        .makeText(

                                            context,

                                            "Medicamento actualizado",

                                            Toast.LENGTH_SHORT
                                        )
                                        .show()


                                    currentScreen =
                                        Screen.Medicamentos
                                },

                                onError = {
                                        mensaje ->


                                    Toast
                                        .makeText(

                                            context,

                                            "No fue posible actualizar el medicamento: $mensaje",

                                            Toast.LENGTH_LONG
                                        )
                                        .show()
                                }
                            )

                    } else {


                        // ====================================================
                        // NUEVO MEDICAMENTO
                        // ====================================================
                        //
                        // Ya NO generamos el ID con System.currentTimeMillis().
                        //
                        // Firestore genera el ID real y la API nos devuelve el
                        // medicamento completo.
                        //
                        // ====================================================

                        MedicamentosRepository
                            .crearMedicamento(

                                medicamento =
                                    medicamentoRequest,

                                onSuccess = {
                                        medicamentoCreado ->


                                    medicamentos.add(
                                        medicamentoCreado
                                    )


                                    ProgramadorRecordatoriosMedicamento
                                        .programarMedicamento(

                                            context =
                                                context,

                                            medicamento =
                                                medicamentoCreado
                                        )


                                    Toast
                                        .makeText(

                                            context,

                                            "Medicamento guardado",

                                            Toast.LENGTH_SHORT
                                        )
                                        .show()


                                    currentScreen =
                                        Screen.Medicamentos
                                },

                                onError = {
                                        mensaje ->


                                    Toast
                                        .makeText(

                                            context,

                                            "No fue posible guardar el medicamento: $mensaje",

                                            Toast.LENGTH_LONG
                                        )
                                        .show()
                                }
                            )
                    }
                }
            )
        }


        // ====================================================================
        // BITÁCORA DE SALUD
        // ====================================================================

        Screen.BitacoraSalud -> {

            BitacoraSaludScreen(

                registrosSalud =
                    registrosSalud,

                estudiosLaboratorio =
                    estudiosLaboratorio,

                onBack = {

                    currentScreen =
                        Screen.MiAgenda
                },

                onAgregarClick = {

                    registroSaludEnEdicion =
                        null


                    estudioLaboratorioEnEdicion =
                        null


                    currentScreen =
                        Screen.AgregarBitacora
                },

                onEditarRegistro = {
                        registro ->


                    registroSaludEnEdicion =
                        registro


                    estudioLaboratorioEnEdicion =
                        null


                    currentScreen =
                        Screen.AgregarBitacora
                },

                onEliminarRegistro = {
                        registro ->


                    BitacoraRepository
                        .eliminarRegistro(

                            registroId =
                                registro.id,

                            onSuccess = {


                                registrosSalud.removeAll {
                                        actual ->

                                    actual.id ==
                                            registro.id
                                }


                                Toast
                                    .makeText(

                                        context,

                                        "Medición eliminada",

                                        Toast.LENGTH_SHORT
                                    )
                                    .show()
                            },

                            onError = {
                                    mensaje ->


                                Toast
                                    .makeText(

                                        context,

                                        "No fue posible eliminar la medición: $mensaje",

                                        Toast.LENGTH_LONG
                                    )
                                    .show()
                            }
                        )
                },

                onEditarEstudio = {
                        estudio ->


                    estudioLaboratorioEnEdicion =
                        estudio


                    registroSaludEnEdicion =
                        null


                    currentScreen =
                        Screen.AgregarBitacora
                },

                onEliminarEstudio = {
                        estudio ->


                    BitacoraRepository
                        .eliminarLaboratorio(

                            estudioId =
                                estudio.id,

                            onSuccess = {


                                estudiosLaboratorio.removeAll {
                                        actual ->

                                    actual.id ==
                                            estudio.id
                                }


                                Toast
                                    .makeText(

                                        context,

                                        "Estudio eliminado",

                                        Toast.LENGTH_SHORT
                                    )
                                    .show()
                            },

                            onError = {
                                    mensaje ->


                                Toast
                                    .makeText(

                                        context,

                                        "No fue posible eliminar el estudio: $mensaje",

                                        Toast.LENGTH_LONG
                                    )
                                    .show()
                            }
                        )
                }
            )
        }


        // ====================================================================
        // AGREGAR / EDITAR BITÁCORA
        // ====================================================================

        Screen.AgregarBitacora -> {

            AgregarBitacoraScreen(

                registroInicial =
                    registroSaludEnEdicion,

                estudioInicial =
                    estudioLaboratorioEnEdicion,

                onBack = {

                    registroSaludEnEdicion =
                        null


                    estudioLaboratorioEnEdicion =
                        null


                    currentScreen =
                        Screen.BitacoraSalud
                },


                // ============================================================
                // GUARDAR MEDICIÓN
                // ============================================================

                onGuardarMedicion = {
                        tipo,
                        fecha,
                        hora,
                        valorPrincipal,
                        valorSecundario,
                        unidad,
                        condicion,
                        observaciones ->


                    // ========================================================
                    // REQUEST PARA LA API
                    // ========================================================

                    val registroRequest =
                        RegistroSaludRequest(

                            tipo =
                                tipo,

                            fecha =
                                fecha,

                            hora =
                                hora,

                            valorPrincipal =
                                valorPrincipal,

                            valorSecundario =
                                valorSecundario,

                            unidad =
                                unidad,

                            condicion =
                                condicion,

                            observaciones =
                                observaciones
                        )


                    // ========================================================
                    // EDITAR
                    // ========================================================

                    if (
                        registroSaludEnEdicion != null
                    ) {

                        val original =
                            registroSaludEnEdicion!!


                        BitacoraRepository
                            .actualizarRegistro(

                                registroId =
                                    original.id,

                                registro =
                                    registroRequest,

                                onSuccess = {


                                    val indice =
                                        registrosSalud
                                            .indexOfFirst {
                                                    registro ->

                                                registro.id ==
                                                        original.id
                                            }


                                    if (
                                        indice != -1
                                    ) {

                                        registrosSalud[indice] =
                                            original.copy(

                                                tipo =
                                                    tipo,

                                                fecha =
                                                    fecha,

                                                hora =
                                                    hora,

                                                valorPrincipal =
                                                    valorPrincipal,

                                                valorSecundario =
                                                    valorSecundario,

                                                unidad =
                                                    unidad,

                                                condicion =
                                                    condicion,

                                                observaciones =
                                                    observaciones
                                            )
                                    }


                                    registroSaludEnEdicion =
                                        null


                                    estudioLaboratorioEnEdicion =
                                        null


                                    Toast
                                        .makeText(

                                            context,

                                            "Medición actualizada",

                                            Toast.LENGTH_SHORT
                                        )
                                        .show()


                                    currentScreen =
                                        Screen.BitacoraSalud
                                },

                                onError = {
                                        mensaje ->


                                    Toast
                                        .makeText(

                                            context,

                                            "No fue posible actualizar la medición: $mensaje",

                                            Toast.LENGTH_LONG
                                        )
                                        .show()
                                }
                            )

                    } else {


                        // ====================================================
                        // NUEVA MEDICIÓN
                        // ====================================================

                        BitacoraRepository
                            .crearRegistro(

                                registro =
                                    registroRequest,

                                onSuccess = {
                                        registroCreado ->


                                    registrosSalud.add(
                                        registroCreado
                                    )


                                    registroSaludEnEdicion =
                                        null


                                    estudioLaboratorioEnEdicion =
                                        null


                                    Toast
                                        .makeText(

                                            context,

                                            "Medición guardada",

                                            Toast.LENGTH_SHORT
                                        )
                                        .show()


                                    currentScreen =
                                        Screen.BitacoraSalud
                                },

                                onError = {
                                        mensaje ->


                                    Toast
                                        .makeText(

                                            context,

                                            "No fue posible guardar la medición: $mensaje",

                                            Toast.LENGTH_LONG
                                        )
                                        .show()
                                }
                            )
                    }
                },


                // ============================================================
                // GUARDAR LABORATORIO
                // ============================================================

                onGuardarLaboratorio = {
                        tipoEstudio,
                        nombrePersonalizado,
                        fecha,
                        laboratorio,
                        archivosUri,
                        observaciones ->


                    // ========================================================
                    // ARCHIVOS DEL ESTUDIO
                    // ========================================================
                    //
                    // En edición podemos tener dos tipos de valores:
                    //
                    // 1. URL de Cloudinary:
                    //    https://res.cloudinary.com/...
                    //
                    // 2. Archivo nuevo del teléfono:
                    //    content://...
                    //
                    // Las URL existentes NO se vuelven a subir.
                    // Solamente mandamos a Cloudinary los archivos locales.
                    //
                    // ========================================================

                    val archivosYaSubidos =
                        archivosUri.filter {
                                archivo ->

                            archivo.startsWith(
                                "https://"
                            ) ||
                                    archivo.startsWith(
                                        "http://"
                                    )
                        }


                    val archivosLocales =
                        archivosUri.filter {
                                archivo ->

                            !archivo.startsWith(
                                "https://"
                            ) &&
                                    !archivo.startsWith(
                                        "http://"
                                    )
                        }


                    // ========================================================
                    // FUNCIÓN LOCAL PARA GUARDAR EN FIRESTORE
                    // ========================================================
                    //
                    // Esta función se ejecuta DESPUÉS de que los archivos
                    // locales ya fueron convertidos en URL de Cloudinary.
                    //
                    // ========================================================

                    fun guardarEstudioConUrls(
                        urlsFinales: List<String>
                    ) {


                        val estudioRequest =
                            EstudioLaboratorioRequest(

                                tipoEstudio =
                                    tipoEstudio,

                                nombrePersonalizado =
                                    nombrePersonalizado,

                                fecha =
                                    fecha,

                                laboratorio =
                                    laboratorio,

                                archivosUri =
                                    urlsFinales,

                                observaciones =
                                    observaciones
                            )


                        // ====================================================
                        // EDITAR ESTUDIO
                        // ====================================================

                        if (
                            estudioLaboratorioEnEdicion != null
                        ) {

                            val original =
                                estudioLaboratorioEnEdicion!!


                            BitacoraRepository
                                .actualizarLaboratorio(

                                    estudioId =
                                        original.id,

                                    estudio =
                                        estudioRequest,

                                    onSuccess = {


                                        val indice =
                                            estudiosLaboratorio
                                                .indexOfFirst {
                                                        estudio ->

                                                    estudio.id ==
                                                            original.id
                                                }


                                        if (
                                            indice != -1
                                        ) {

                                            estudiosLaboratorio[indice] =
                                                original.copy(

                                                    tipoEstudio =
                                                        tipoEstudio,

                                                    nombrePersonalizado =
                                                        nombrePersonalizado,

                                                    fecha =
                                                        fecha,

                                                    laboratorio =
                                                        laboratorio,

                                                    archivosUri =
                                                        urlsFinales,

                                                    observaciones =
                                                        observaciones
                                                )
                                        }


                                        estudioLaboratorioEnEdicion =
                                            null


                                        registroSaludEnEdicion =
                                            null


                                        Toast
                                            .makeText(

                                                context,

                                                "Estudio actualizado",

                                                Toast.LENGTH_SHORT
                                            )
                                            .show()


                                        currentScreen =
                                            Screen.BitacoraSalud
                                    },

                                    onError = {
                                            mensaje ->


                                        Toast
                                            .makeText(

                                                context,

                                                "No fue posible actualizar el estudio: $mensaje",

                                                Toast.LENGTH_LONG
                                            )
                                            .show()
                                    }
                                )

                        } else {


                            // ================================================
                            // NUEVO ESTUDIO
                            // ================================================

                            BitacoraRepository
                                .crearLaboratorio(

                                    estudio =
                                        estudioRequest,

                                    onSuccess = {
                                            estudioCreado ->


                                        estudiosLaboratorio.add(
                                            estudioCreado
                                        )


                                        estudioLaboratorioEnEdicion =
                                            null


                                        registroSaludEnEdicion =
                                            null


                                        Toast
                                            .makeText(

                                                context,

                                                "Estudio guardado",

                                                Toast.LENGTH_SHORT
                                            )
                                            .show()


                                        currentScreen =
                                            Screen.BitacoraSalud
                                    },

                                    onError = {
                                            mensaje ->


                                        Toast
                                            .makeText(

                                                context,

                                                "No fue posible guardar el estudio: $mensaje",

                                                Toast.LENGTH_LONG
                                            )
                                            .show()
                                    }
                                )
                        }
                    }


                    // ========================================================
                    // SI NO HAY ARCHIVOS LOCALES
                    // ========================================================
                    //
                    // Esto pasa, por ejemplo, cuando editamos un estudio
                    // que ya tiene todos sus archivos en Cloudinary.
                    //
                    // ========================================================

                    if (
                        archivosLocales.isEmpty()
                    ) {

                        guardarEstudioConUrls(
                            archivosYaSubidos
                        )

                    } else {


                        // ====================================================
                        // SUBIR ARCHIVOS NUEVOS A CLOUDINARY
                        // ====================================================

                        Toast
                            .makeText(

                                context,

                                "Subiendo archivos...",

                                Toast.LENGTH_SHORT
                            )
                            .show()


                        CloudinaryRepository
                            .subirArchivos(

                                context =
                                    context,

                                uris =
                                    archivosLocales,

                                onSuccess = {
                                        nuevasUrls ->


                                    // ----------------------------------------
                                    // CONSERVAR URL ANTERIORES + NUEVAS
                                    // ----------------------------------------

                                    val urlsFinales =
                                        archivosYaSubidos +
                                                nuevasUrls


                                    guardarEstudioConUrls(
                                        urlsFinales
                                    )
                                },

                                onError = {
                                        mensaje ->


                                    Toast
                                        .makeText(

                                            context,

                                            "No fue posible subir los archivos: $mensaje",

                                            Toast.LENGTH_LONG
                                        )
                                        .show()
                                }
                            )
                    }
                }
            )
        }
    }
    }
}




// ============================================================================
// MENÚ LATERAL GLOBAL
// ============================================================================
//
// Este menú se utiliza en las pantallas principales e internas.
//
// El Home conserva su propio drawer visual, pero ambos menús apuntan
// exactamente a las mismas secciones.
//
// ============================================================================

@Composable
private fun AppGlobalDrawerContent(

    currentScreen:
        Screen,

    onNavigate:
        (Screen) -> Unit,

    onClose:
        () -> Unit
) {

    val section =
        currentScreen
            .drawerSection()


    Column(

        modifier =
            Modifier
                .fillMaxHeight()
                .width(
                    290.dp
                )
                .background(
                    Color(0xFFFEFFF6)
                )
                .verticalScroll(
                    androidx.compose.foundation.rememberScrollState()
                )
                .padding(
                    18.dp
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
                        "Vibra la vida",

                    fontSize =
                        22.sp,

                    fontWeight =
                        FontWeight.Bold,

                    color =
                        Color(0xFF0F766E)
                )


                Text(

                    text =
                        "Ir directamente a",

                    fontSize =
                        12.sp,

                    color =
                        Color(0xFF64748B)
                )
            }


            Text(

                text =
                    "✕",

                modifier =
                    Modifier
                        .clickable {
                            onClose()
                        }
                        .padding(
                            10.dp
                        ),

                fontSize =
                    20.sp,

                fontWeight =
                    FontWeight.Bold,

                color =
                    Color(0xFF64748B)
            )
        }


        Spacer(

            modifier =
                Modifier.height(
                    18.dp
                )
        )


        AppDrawerDestination(

            emoji =
                "🏠",

            title =
                "Inicio",

            selected =
                section ==
                    Screen.Home,

            onClick = {

                onNavigate(
                    Screen.Home
                )
            }
        )


        AppDrawerDestination(

            emoji =
                "💚",

            title =
                "Hábitos saludables",

            selected =
                section ==
                    Screen.HealthyHabits,

            onClick = {

                onNavigate(
                    Screen.HealthyHabits
                )
            }
        )


        AppDrawerDestination(

            emoji =
                "🫀",

            title =
                "Trastornos del ritmo",

            selected =
                section ==
                    Screen.HealthConnect,

            onClick = {

                onNavigate(
                    Screen.HealthConnect
                )
            }
        )


        AppDrawerDestination(

            emoji =
                "🩸",

            title =
                "Diabetes mellitus",

            selected =
                section ==
                    Screen.DiabetesMellitus,

            onClick = {

                onNavigate(
                    Screen.DiabetesMellitus
                )
            }
        )


        AppDrawerDestination(

            emoji =
                "📅",

            title =
                "Mi agenda",

            selected =
                section ==
                    Screen.MiAgenda,

            onClick = {

                onNavigate(
                    Screen.MiAgenda
                )
            }
        )


        AppDrawerDestination(

            emoji =
                "🩺",

            title =
                "Mi equipo de salud",

            selected =
                section ==
                    Screen.MiEquipoSalud,

            onClick = {

                onNavigate(
                    Screen.MiEquipoSalud
                )
            }
        )


        AppDrawerDestination(

            emoji =
                "📍",

            title =
                "Profesionales cercanos",

            selected =
                section ==
                    Screen.MapaProfesionales,

            onClick = {

                onNavigate(
                    Screen.MapaProfesionales
                )
            }
        )


        AppDrawerDestination(

            emoji =
                "🤖",

            title =
                "Hablar con Nuby",

            selected =
                section ==
                    Screen.NubyChat,

            onClick = {

                onNavigate(
                    Screen.NubyChat
                )
            }
        )


        AppDrawerDestination(

            emoji =
                "👤",

            title =
                "Mi perfil",

            selected =
                section ==
                    Screen.Profile,

            onClick = {

                onNavigate(
                    Screen.Profile
                )
            }
        )


        Spacer(

            modifier =
                Modifier.height(
                    20.dp
                )
        )


        Text(

            text =
                "Tip: el botón Atrás del teléfono ahora regresa a la pantalla anterior.",

            fontSize =
                11.sp,

            lineHeight =
                16.sp,

            color =
                Color(0xFF64748B)
        )
    }
}


// ============================================================================
// OPCIÓN DEL MENÚ GLOBAL
// ============================================================================

@Composable
private fun AppDrawerDestination(

    emoji:
        String,

    title:
        String,

    selected:
        Boolean,

    onClick:
        () -> Unit
) {

    Row(

        modifier =
            Modifier
                .fillMaxWidth()
                .clip(
                    RoundedCornerShape(
                        16.dp
                    )
                )
                .background(

                    if (
                        selected
                    ) {
                        Color(0xFFDDF7EE)
                    } else {
                        Color.Transparent
                    }
                )
                .clickable {
                    onClick()
                }
                .padding(
                    horizontal =
                        13.dp,
                    vertical =
                        12.dp
                ),

        verticalAlignment =
            Alignment.CenterVertically
    ) {

        Text(

            text =
                emoji,

            fontSize =
                20.sp
        )


        Spacer(

            modifier =
                Modifier.width(
                    11.dp
                )
        )


        Text(

            text =
                title,

            fontSize =
                14.sp,

            fontWeight =
                if (
                    selected
                ) {
                    FontWeight.Bold
                } else {
                    FontWeight.Medium
                },

            color =
                if (
                    selected
                ) {
                    Color(0xFF0F766E)
                } else {
                    Color(0xFF334155)
                }
        )
    }


    Spacer(

        modifier =
            Modifier.height(
                4.dp
            )
    )
}


// ============================================================================
// SPLASH
// ============================================================================

@Composable
fun SplashScreen(
    onLogoClick: () -> Unit
) {

    val infiniteTransition =
        rememberInfiniteTransition(
            label = "pulse_animation"
        )


    val scale by
    infiniteTransition.animateFloat(

        initialValue =
            1f,

        targetValue =
            1.08f,

        animationSpec =
            infiniteRepeatable(

                animation =
                    tween(

                        durationMillis =
                            1500,

                        easing =
                            LinearEasing
                    ),

                repeatMode =
                    RepeatMode.Reverse
            ),

        label =
            "logo_scale"
    )


    Box(

        modifier =
            Modifier
                .fillMaxSize()
                .background(
                    backgroundGradient()
                ),

        contentAlignment =
            Alignment.Center
    ) {


        Column(

            horizontalAlignment =
                Alignment.CenterHorizontally,

            modifier =
                Modifier.padding(
                    28.dp
                )
        ) {


            LogoCircle(

                size =
                    180,

                imagePadding =
                    28,

                scale =
                    scale,

                clickable =
                    true,

                onClick =
                    onLogoClick
            )


            Spacer(

                modifier =
                    Modifier.height(
                        28.dp
                    )
            )


            Text(

                text =
                    "Vibra la vida",

                color =
                    Color(0xFF0F766E),

                fontSize =
                    30.sp,

                fontWeight =
                    FontWeight.Bold,

                textAlign =
                    TextAlign.Center
            )


            SloganText(

                fontSize =
                    17,

                textColor =
                    Color(0xFF0D9488)
            )


            LoadingDots()
        }
    }
}


// ============================================================================
// AUTH
// ============================================================================

@Composable
fun AuthScreen(

    onBack: () -> Unit,

    onLoginClick: () -> Unit,

    onRegisterClick: () -> Unit

) {

    Box(

        modifier =
            Modifier
                .fillMaxSize()
                .background(
                    backgroundGradient()
                ),

        contentAlignment =
            Alignment.Center
    ) {


        Column(

            horizontalAlignment =
                Alignment.CenterHorizontally,

            modifier =
                Modifier
                    .padding(
                        28.dp
                    )
                    .widthIn(
                        max = 390.dp
                    )
        ) {


            LogoCircle(

                size =
                    108,

                imagePadding =
                    16,

                scale =
                    1f,

                clickable =
                    false,

                onClick = {}
            )


            Spacer(

                modifier =
                    Modifier.height(
                        30.dp
                    )
            )


            Text(

                text =
                    "Bienvenido",

                fontSize =
                    25.sp,

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
                        8.dp
                    )
            )


            Text(

                text =
                    "Selecciona una opción para continuar",

                fontSize =
                    14.sp,

                color =
                    Color(0xFF64748B),

                textAlign =
                    TextAlign.Center
            )


            Spacer(

                modifier =
                    Modifier.height(
                        28.dp
                    )
            )


            GradientButton(

                text =
                    "Iniciar sesión",

                onClick =
                    onLoginClick
            )


            Spacer(

                modifier =
                    Modifier.height(
                        14.dp
                    )
            )


            OutlinedButton(

                onClick =
                    onRegisterClick,

                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(
                            54.dp
                        ),

                shape =
                    RoundedCornerShape(
                        16.dp
                    ),

                colors =
                    ButtonDefaults
                        .outlinedButtonColors(

                            containerColor =
                                Color.White,

                            contentColor =
                                Color(0xFF0F766E)
                        ),

                border =
                    BorderStroke(

                        width =
                            2.dp,

                        color =
                            Color(0xFFBFEA7C)
                    )

            ) {


                Text(

                    text =
                        "Crear cuenta",

                    fontSize =
                        16.sp,

                    fontWeight =
                        FontWeight.SemiBold
                )
            }


            Spacer(

                modifier =
                    Modifier.height(
                        10.dp
                    )
            )


            TextButton(

                onClick =
                    onBack,

                modifier =
                    Modifier.fillMaxWidth()

            ) {


                Text(

                    text =
                        "Volver",

                    color =
                        Color(0xFF64748B),

                    fontSize =
                        14.sp
                )
            }


            Spacer(

                modifier =
                    Modifier.height(
                        20.dp
                    )
            )


            Text(

                text =
                    "Porque ahora cuidarte está al alcance de tus manos",

                fontSize =
                    13.sp,

                color =
                    Color(0xFF0D9488),

                textAlign =
                    TextAlign.Center,

                lineHeight =
                    19.sp,

                modifier =
                    Modifier.padding(
                        horizontal = 18.dp
                    )
            )
        }
    }
}


// ============================================================================
// LOGO
// ============================================================================

@Composable
fun LogoCircle(

    size: Int,

    imagePadding: Int,

    scale: Float,

    clickable: Boolean,

    onClick: () -> Unit

) {

    Box(

        contentAlignment =
            Alignment.Center

    ) {


        Box(

            modifier =
                Modifier
                    .size(
                        (size + 22).dp
                    )
                    .scale(
                        scale
                    )
                    .blur(
                        34.dp
                    )
                    .background(

                        brush =
                            Brush.radialGradient(

                                colors =
                                    listOf(

                                        Color(0x66CDDC39),

                                        Color(0x6606B6D4),

                                        Color.Transparent
                                    )
                            ),

                        shape =
                            CircleShape
                    )
        )


        Surface(

            modifier =
                Modifier
                    .size(
                        size.dp
                    )
                    .then(

                        if (
                            clickable
                        ) {

                            Modifier.clickable {

                                onClick()
                            }

                        } else {

                            Modifier
                        }
                    ),

            shape =
                CircleShape,

            color =
                Color.White,

            shadowElevation =
                14.dp

        ) {


            Box(

                modifier =
                    Modifier.padding(
                        imagePadding.dp
                    ),

                contentAlignment =
                    Alignment.Center

            ) {


                Image(

                    painter =
                        painterResource(
                            id = R.drawable.logo
                        ),

                    contentDescription =
                        "Logo de Vibra la vida",

                    modifier =
                        Modifier.fillMaxSize(),

                    contentScale =
                        ContentScale.Fit
                )
            }
        }
    }
}


// ============================================================================
// BOTÓN DEGRADADO
// ============================================================================

@Composable
fun GradientButton(

    text: String,

    onClick: () -> Unit

) {

    Button(

        onClick =
            onClick,

        modifier =
            Modifier
                .fillMaxWidth()
                .height(
                    54.dp
                ),

        shape =
            RoundedCornerShape(
                16.dp
            ),

        colors =
            ButtonDefaults.buttonColors(

                containerColor =
                    Color.Transparent,

                contentColor =
                    Color.White
            ),

        contentPadding =
            PaddingValues(
                0.dp
            ),

        elevation =
            ButtonDefaults.buttonElevation(

                defaultElevation =
                    8.dp,

                pressedElevation =
                    4.dp
            )

    ) {


        Box(

            modifier =
                Modifier
                    .fillMaxSize()
                    .clip(
                        RoundedCornerShape(
                            16.dp
                        )
                    )
                    .background(

                        brush =
                            Brush.horizontalGradient(

                                colors =
                                    listOf(

                                        Color(0xFFCDDC39),

                                        Color(0xFF06B6D4)
                                    )
                            )
                    ),

            contentAlignment =
                Alignment.Center

        ) {


            Text(

                text =
                    text,

                color =
                    Color.White,

                fontSize =
                    16.sp,

                fontWeight =
                    FontWeight.SemiBold
            )
        }
    }
}


// ============================================================================
// SLOGAN
// ============================================================================

@Composable
fun SloganText(

    fontSize: Int,

    textColor: Color

) {

    Column(

        horizontalAlignment =
            Alignment.CenterHorizontally,

        modifier =
            Modifier.padding(
                vertical = 18.dp
            )

    ) {


        DecorativeLine()


        Spacer(

            modifier =
                Modifier.height(
                    10.dp
                )
        )


        Text(

            text =
                "porque ahora cuidarte está al alcance de tus manos",

            fontSize =
                fontSize.sp,

            color =
                textColor,

            textAlign =
                TextAlign.Center,

            lineHeight =
                24.sp,

            modifier =
                Modifier.padding(
                    horizontal = 16.dp
                )
        )


        Spacer(

            modifier =
                Modifier.height(
                    10.dp
                )
        )


        DecorativeLine()
    }
}


// ============================================================================
// LÍNEA DECORATIVA
// ============================================================================

@Composable
fun DecorativeLine() {

    Box(

        modifier =
            Modifier
                .width(
                    54.dp
                )
                .height(
                    4.dp
                )
                .background(

                    brush =
                        Brush.horizontalGradient(

                            colors =
                                listOf(

                                    Color(0xFFCDDC39),

                                    Color(0xFF06B6D4)
                                )
                        ),

                    shape =
                        RoundedCornerShape(
                            2.dp
                        )
                )
    )
}


// ============================================================================
// LOADING
// ============================================================================

@Composable
fun LoadingDots() {

    Row(

        horizontalArrangement =
            Arrangement.spacedBy(
                8.dp
            ),

        modifier =
            Modifier.padding(
                top = 12.dp
            )

    ) {


        repeat(
            3
        ) {
                index ->


            AnimatedDot(

                delay =
                    index * 150
            )
        }
    }
}


// ============================================================================
// PUNTO ANIMADO
// ============================================================================

@Composable
fun AnimatedDot(
    delay: Int
) {

    val infiniteTransition =
        rememberInfiniteTransition(
            label = "dot_animation"
        )


    val offsetY by
    infiniteTransition.animateFloat(

        initialValue =
            0f,

        targetValue =
            -16f,

        animationSpec =
            infiniteRepeatable(

                animation =
                    tween(

                        durationMillis =
                            600,

                        delayMillis =
                            delay,

                        easing =
                            LinearEasing
                    ),

                repeatMode =
                    RepeatMode.Reverse
            ),

        label =
            "dot_offset"
    )


    val color =
        when (
            delay
        ) {

            0 ->
                Color(0xFFCDDC39)

            150 ->
                Color(0xFF06B6D4)

            else ->
                Color(0xFF0D9488)
        }


    Box(

        modifier =
            Modifier
                .size(
                    8.dp
                )
                .offset(
                    y = offsetY.dp
                )
                .background(

                    color =
                        color,

                    shape =
                        CircleShape
                )
    )
}


// ============================================================================
// FONDO
// ============================================================================

fun backgroundGradient(): Brush {

    return Brush.radialGradient(

        colors =
            listOf(

                Color(0xFFE0F7FA),

                Color(0xFFD1F5E8),

                Color(0xF6ECECFF),
            )
    )
}


// ============================================================================
// TEMA
// ============================================================================

@Composable
fun VibraLaVidaTheme(

    content:
    @Composable () -> Unit

) {

    MaterialTheme(

        colorScheme =
            lightColorScheme(

                primary =
                    Color(0xFF0D9488),

                secondary =
                    Color(0xFFCDDC39),

                background =
                    Color(0xFFE0F7FA)
            ),

        content =
            content
    )
}
