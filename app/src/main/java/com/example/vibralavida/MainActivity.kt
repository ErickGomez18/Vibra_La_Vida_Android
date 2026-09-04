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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn


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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import com.example.vibralavida.api.modelos.PerfilRequest
import com.example.vibralavida.api.modelos.UsuarioPerfil


// ============================================================================
// PANTALLAS PRINCIPALES
// ============================================================================

import com.example.vibralavida.pantallas_principales.InitialProfileScreen
import com.example.vibralavida.pantallas_principales.LoginScreen
import com.example.vibralavida.pantallas_principales.RegisterScreen
import com.example.vibralavida.pantallas_principales.HomeScreen
import com.example.vibralavida.pantallas_principales.ProfileScreen
import com.example.vibralavida.agenda.MiAgendaScreen


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
// AGENDA - MEDICAMENTOS
// ============================================================================

import com.example.vibralavida.agenda.medicamentos.Medicamento
import com.example.vibralavida.agenda.medicamentos.MedicamentosScreen
import com.example.vibralavida.agenda.medicamentos.AgregarMedicamentoScreen
import com.example.vibralavida.agenda.medicamentos.NotificacionMedicamento
import com.example.vibralavida.agenda.medicamentos.ProgramadorRecordatoriosMedicamento


// ============================================================================
// AGENDA - BITÁCORA
// ============================================================================

import com.example.vibralavida.agenda.bitacora.RegistroSalud
import com.example.vibralavida.agenda.bitacora.EstudioLaboratorio
import com.example.vibralavida.agenda.bitacora.BitacoraSaludScreen
import com.example.vibralavida.agenda.bitacora.AgregarBitacoraScreen


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
    // AGENDA
    // ========================================================================

    MiAgenda,

    Medicamentos,

    AgregarMedicamento,

    BitacoraSalud,

    AgregarBitacora
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
    // SESIÓN PERSISTENTE
    // ========================================================================
    //
    // Al abrir la app verificamos Firebase Authentication.
    //
    // Si currentUser existe:
    //
    // → el usuario ya tenía una sesión iniciada.
    // → obtenemos su perfil mediante la API.
    //
    // ========================================================================

    LaunchedEffect(Unit) {


        val usuarioFirebase =
            FirebaseAuth
                .getInstance()
                .currentUser


        // ====================================================================
        // NO EXISTE SESIÓN
        // ====================================================================

        if (
            usuarioFirebase == null
        ) {

            currentScreen =
                Screen.Splash

        } else {


            // =================================================================
            // EXISTE SESIÓN
            // =================================================================

            PerfilRepository
                .obtenerPerfil(


                    // =========================================================
                    // PERFIL ENCONTRADO
                    // =========================================================

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


                            // =================================================
                            // NOMBRE
                            // =================================================

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


                            // =================================================
                            // PERFIL
                            // =================================================

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


                            // =================================================
                            // ENFERMEDADES
                            // =================================================

                            userChronicDiseases =
                                perfil.enfermedadesCronicas


                            userOtherChronicDisease =
                                perfil
                                    .otraEnfermedadCronica
                                    .orEmpty()


                            // =================================================
                            // DECIDIR PANTALLA
                            // =================================================

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


                    // =========================================================
                    // TIENE AUTH PERO NO PERFIL
                    // =========================================================

                    onProfileNotFound = {

                        userName =
                            usuarioFirebase
                                .displayName
                                .orEmpty()


                        currentScreen =
                            Screen.InitialProfile
                    },


                    // =========================================================
                    // SESIÓN INVÁLIDA
                    // =========================================================

                    onUnauthorized = {

                        FirebaseAuth
                            .getInstance()
                            .signOut()


                        currentScreen =
                            Screen.Auth
                    },


                    // =========================================================
                    // ERROR DE CONEXIÓN
                    // =========================================================

                    onError = {
                            mensaje ->


                        Toast
                            .makeText(

                                context,

                                "No fue posible recuperar tu perfil: $mensaje",

                                Toast.LENGTH_LONG
                            )
                            .show()


                        // No cerramos Firebase porque el error
                        // puede ser simplemente que la API esté apagada.

                        currentScreen =
                            Screen.Auth
                    }
                )
        }
    }


    // ========================================================================
    // NAVEGACIÓN
    // ========================================================================

    when (
        currentScreen
    ) {


        // ====================================================================
        // SPLASH
        // ====================================================================

        Screen.Splash -> {

            SplashScreen(

                onLogoClick = {

                    currentScreen =
                        Screen.Auth
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

                onBack = {

                    currentScreen =
                        Screen.Auth
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

                    PerfilRepository
                        .guardarPerfil(

                            perfil =
                                perfilRequest,


                            // =================================================
                            // ÉXITO
                            // =================================================

                            onSuccess = {


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


                                userChronicDiseases =
                                    chronicDiseases


                                userOtherChronicDisease =
                                    otherChronicDisease


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

                    println(
                        "Ir a Diabetes Mellitus"
                    )
                },

                onAgendaClick = {

                    currentScreen =
                        Screen.MiAgenda
                }
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

                    currentScreen =
                        Screen.Auth
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

                    currentScreen =
                        Screen.HealthyHabits
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

                    currentScreen =
                        Screen.HealthyHabits
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

                    currentScreen =
                        Screen.MoodSurveyMenu
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

                    currentScreen =
                        Screen.MoodSurveyMenu
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

                    currentScreen =
                        Screen.MoodSurveyMenu
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

                    currentScreen =
                        Screen.HealthyHabits
                },

                onProfileClick = {

                    currentScreen =
                        Screen.Profile
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

                    currentScreen =
                        Screen.HealthyHabits
                },

                onProfileClick = {

                    currentScreen =
                        Screen.Profile
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

                    currentScreen =
                        Screen.HealthyHabits
                },

                onProfileClick = {

                    currentScreen =
                        Screen.Profile
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

                    currentScreen =
                        Screen.Medicamentos
                },

                onLaboratoriosClick = {

                    currentScreen =
                        Screen.BitacoraSalud
                },

                onCitasClick = {

                    println(
                        "Abrir citas"
                    )
                },

                onHistorialClick = {

                    println(
                        "Abrir historial"
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


                    // ========================================================
                    // CANCELAR RECORDATORIOS
                    // ========================================================

                    ProgramadorRecordatoriosMedicamento
                        .cancelarMedicamento(

                            context =
                                context,

                            medicamento =
                                medicamento
                        )


                    // ========================================================
                    // ELIMINAR
                    // ========================================================

                    medicamentos.removeAll {
                            actual ->

                        actual.id ==
                                medicamento.id
                    }
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
                    // EDITAR
                    // ========================================================

                    if (
                        medicamentoEnEdicion != null
                    ) {

                        val medicamentoOriginal =
                            medicamentoEnEdicion!!


                        // ----------------------------------------------------
                        // CANCELAR ALARMAS ANTERIORES
                        // ----------------------------------------------------

                        ProgramadorRecordatoriosMedicamento
                            .cancelarMedicamento(

                                context =
                                    context,

                                medicamento =
                                    medicamentoOriginal
                            )


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


                            // ------------------------------------------------
                            // PROGRAMAR NUEVAS ALARMAS
                            // ------------------------------------------------

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


                    } else {


                        // ====================================================
                        // NUEVO MEDICAMENTO
                        // ====================================================

                        val nuevoMedicamento =
                            Medicamento(

                                id =
                                    System
                                        .currentTimeMillis()
                                        .toString(),

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


                        medicamentos.add(
                            nuevoMedicamento
                        )


                        // ----------------------------------------------------
                        // PROGRAMAR RECORDATORIOS
                        // ----------------------------------------------------

                        ProgramadorRecordatoriosMedicamento
                            .programarMedicamento(

                                context =
                                    context,

                                medicamento =
                                    nuevoMedicamento
                            )
                    }


                    currentScreen =
                        Screen.Medicamentos
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


                    registrosSalud.removeAll {
                            actual ->

                        actual.id ==
                                registro.id
                    }
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


                    estudiosLaboratorio.removeAll {
                            actual ->

                        actual.id ==
                                estudio.id
                    }
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
                    // EDITAR
                    // ========================================================

                    if (
                        registroSaludEnEdicion != null
                    ) {

                        val original =
                            registroSaludEnEdicion!!


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


                    } else {


                        // ====================================================
                        // NUEVA MEDICIÓN
                        // ====================================================

                        val nuevoRegistro =
                            RegistroSalud(

                                id =
                                    System
                                        .currentTimeMillis()
                                        .toString(),

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


                        registrosSalud.add(
                            nuevoRegistro
                        )
                    }


                    estudioLaboratorioEnEdicion =
                        null


                    currentScreen =
                        Screen.BitacoraSalud
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
                    // EDITAR
                    // ========================================================

                    if (
                        estudioLaboratorioEnEdicion != null
                    ) {

                        val original =
                            estudioLaboratorioEnEdicion!!


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
                                        archivosUri,

                                    observaciones =
                                        observaciones
                                )
                        }


                        estudioLaboratorioEnEdicion =
                            null


                    } else {


                        // ====================================================
                        // NUEVO LABORATORIO
                        // ====================================================

                        val nuevoEstudio =
                            EstudioLaboratorio(

                                id =
                                    System
                                        .currentTimeMillis()
                                        .toString(),

                                tipoEstudio =
                                    tipoEstudio,

                                nombrePersonalizado =
                                    nombrePersonalizado,

                                fecha =
                                    fecha,

                                laboratorio =
                                    laboratorio,

                                archivosUri =
                                    archivosUri,

                                observaciones =
                                    observaciones
                            )


                        estudiosLaboratorio.add(
                            nuevoEstudio
                        )
                    }


                    registroSaludEnEdicion =
                        null


                    currentScreen =
                        Screen.BitacoraSalud
                }
            )
        }
    }
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

                Color(0xFFF0F4C3)
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