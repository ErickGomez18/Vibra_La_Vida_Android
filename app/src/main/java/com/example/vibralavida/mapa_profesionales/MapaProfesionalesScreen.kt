package com.example.vibralavida.mapa_profesionales

// ============================================================================
// ANDROID
// ============================================================================

import android.util.Log

// ============================================================================
// API / MODELOS
// ============================================================================

import com.example.vibralavida.api.ProfesionalesMapaRepository
import com.example.vibralavida.api.modelos.ProfesionalMapaResponse
import com.example.vibralavida.backgroundGradient

// ============================================================================
// COMPOSE
// ============================================================================

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Refresh

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView

// ============================================================================
// FIREBASE
// ============================================================================

import com.google.firebase.auth.FirebaseAuth

// ============================================================================
// MAPLIBRE
// ============================================================================

import org.maplibre.android.MapLibre
import org.maplibre.android.annotations.MarkerOptions
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.MapLibreMap
import org.maplibre.android.maps.MapView


// ============================================================================
// MAPA DE PROFESIONALES
// ============================================================================

@Composable
fun MapaProfesionalesScreen(

    onBack: () -> Unit
) {


    // ========================================================================
    // CONTEXTO
    // ========================================================================

    val context =
        LocalContext.current


    // ========================================================================
    // ESTADOS DE LA API
    // ========================================================================

    var profesionales by
    remember {

        mutableStateOf<List<ProfesionalMapaResponse>>(
            emptyList()
        )
    }


    var cargando by
    remember {

        mutableStateOf(
            true
        )
    }


    var mensaje by
    remember {

        mutableStateOf(
            "Buscando profesionales..."
        )
    }


    var profesionalSeleccionado by
    remember {

        mutableStateOf<ProfesionalMapaResponse?>(
            null
        )
    }


    // ========================================================================
    // MAPA
    // ========================================================================

    var mapa by
    remember {

        mutableStateOf<MapLibreMap?>(
            null
        )
    }


    var estiloCargado by
    remember {

        mutableStateOf(
            false
        )
    }


    // Relaciona ID del marcador con profesional.
    val profesionalesPorMarcador =
        remember {

            mutableStateMapOf<Long, ProfesionalMapaResponse>()
        }


    // ========================================================================
    // INICIALIZAR MAPLIBRE
    // ========================================================================

    remember(
        context
    ) {

        MapLibre.getInstance(
            context.applicationContext
        )
    }


    // ========================================================================
    // MAP VIEW
    // ========================================================================

    val mapView =
        remember(
            context
        ) {

            MapView(
                context
            ).apply {


                onCreate(
                    null
                )


                addOnDidFailLoadingMapListener {
                        errorMessage ->

                    Log.e(
                        "MAPLIBRE_VIBRA",
                        "Error cargando mapa: $errorMessage"
                    )
                }


                getMapAsync {
                        mapLibreMap ->


                    mapa =
                        mapLibreMap


                    mapLibreMap.setStyle(
                        "https://tiles.openfreemap.org/styles/liberty"
                    ) {


                        estiloCargado =
                            true


                        // ----------------------------------------------------
                        // POSICIÓN INICIAL
                        // ----------------------------------------------------
                        //
                        // Mientras no tengamos profesionales cargados,
                        // centramos el mapa en Chetumal.
                        //
                        // ----------------------------------------------------

                        val chetumal =
                            LatLng(
                                18.5001,
                                -88.2961
                            )


                        mapLibreMap.cameraPosition =
                            CameraPosition
                                .Builder()
                                .target(
                                    chetumal
                                )
                                .zoom(
                                    12.0
                                )
                                .build()


                        // ----------------------------------------------------
                        // TOCAR MARCADOR
                        // ----------------------------------------------------

                        mapLibreMap.setOnMarkerClickListener {
                                marker ->


                            profesionalSeleccionado =
                                profesionalesPorMarcador[
                                    marker.id
                                ]


                            // true:
                            // nosotros manejamos la selección.
                            true
                        }


                        Log.d(
                            "MAPLIBRE_VIBRA",
                            "Mapa y estilo cargados"
                        )
                    }
                }
            }
        }


    // ========================================================================
    // CICLO DE VIDA DEL MAP VIEW
    // ========================================================================

    DisposableEffect(
        mapView
    ) {

        mapView.onStart()

        mapView.onResume()


        onDispose {

            mapView.onPause()

            mapView.onStop()

            mapView.onDestroy()
        }
    }


    // ========================================================================
    // FUNCIÓN PARA CONSULTAR PROFESIONALES
    // ========================================================================

    fun cargarProfesionales() {


        cargando =
            true


        mensaje =
            "Buscando profesionales..."


        ProfesionalesMapaRepository
            .obtenerProfesionales(

                onSuccess = {
                        lista ->


                    profesionales =
                        lista


                    cargando =
                        false


                    mensaje =

                        if (
                            lista.isEmpty()
                        ) {

                            "Aún no hay consultorios registrados en el mapa."

                        } else {

                            "${lista.size} profesional(es) encontrado(s)."
                        }
                },


                onUnauthorized = {


                    cargando =
                        false


                    mensaje =
                        "Tu sesión ya no es válida. Inicia sesión nuevamente."
                },


                onError = {
                        error ->


                    cargando =
                        false


                    mensaje =
                        "No fue posible cargar los profesionales: $error"
                }
            )
    }


    // ========================================================================
    // PRIMERA CARGA
    // ========================================================================

    LaunchedEffect(
        Unit
    ) {

        cargarProfesionales()
    }


    // ========================================================================
    // DIBUJAR MARCADORES REALES
    // ========================================================================

    LaunchedEffect(
        profesionales,
        estiloCargado
    ) {


        val mapLibreMap =
            mapa


        if (
            mapLibreMap == null ||
            !estiloCargado
        ) {

            return@LaunchedEffect
        }


        // ====================================================================
        // BORRAR MARCADORES ANTERIORES
        // ====================================================================

        mapLibreMap.clear()

        profesionalesPorMarcador.clear()

        profesionalSeleccionado =
            null


        // ====================================================================
        // AGREGAR UN MARCADOR POR PROFESIONAL
        // ====================================================================

        profesionales.forEach {
                profesional ->


            val consultorio =
                profesional.consultorio


            val posicion =
                LatLng(
                    consultorio.latitud,
                    consultorio.longitud
                )


            val marcador =
                mapLibreMap.addMarker(

                    MarkerOptions()
                        .position(
                            posicion
                        )
                        .title(
                            profesional.nombre
                        )
                        .snippet(
                            profesional.especialidad.ifBlank {
                                profesional.profesion
                            }
                        )
                )


            profesionalesPorMarcador[
                marcador.id
            ] =
                profesional
        }


        // ====================================================================
        // CENTRAR EN EL PRIMER CONSULTORIO
        // ====================================================================

        profesionales
            .firstOrNull()
            ?.let {
                    primero ->


                mapLibreMap.cameraPosition =
                    CameraPosition
                        .Builder()
                        .target(
                            LatLng(
                                primero.consultorio.latitud,
                                primero.consultorio.longitud
                            )
                        )
                        .zoom(
                            13.0
                        )
                        .build()
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
    ) {


        // ====================================================================
        // MAPA
        // ====================================================================

        AndroidView(

            factory = {

                mapView
            },

            modifier =
                Modifier.fillMaxSize()
        )


        // ====================================================================
        // BOTÓN VOLVER
        // ====================================================================

        Button(

            onClick =
                onBack,

            modifier =
                Modifier
                    .align(
                        Alignment.TopStart
                    )
                    .padding(
                        16.dp
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
                    Icons.Default.ArrowBack,

                contentDescription =
                    "Volver"
            )


            Spacer(

                modifier =
                    Modifier.width(
                        6.dp
                    )
            )


            Text(

                text =
                    "Volver"
            )
        }


        // ====================================================================
        // BOTÓN ACTUALIZAR
        // ====================================================================

        Button(

            onClick = {

                cargarProfesionales()
            },

            modifier =
                Modifier
                    .align(
                        Alignment.TopEnd
                    )
                    .padding(
                        16.dp
                    ),

            shape =
                RoundedCornerShape(
                    18.dp
                ),

            colors =
                ButtonDefaults.buttonColors(

                    containerColor =
                        Color.White,

                    contentColor =
                        Color(0xFF0F766E)
                )
        ) {


            Icon(

                imageVector =
                    Icons.Default.Refresh,

                contentDescription =
                    "Actualizar"
            )
        }


        // ====================================================================
        // CARGANDO
        // ====================================================================

        if (
            cargando
        ) {


            Card(

                modifier =
                    Modifier
                        .align(
                            Alignment.Center
                        )
                        .padding(
                            22.dp
                        ),

                shape =
                    RoundedCornerShape(
                        20.dp
                    ),

                colors =
                    CardDefaults.cardColors(

                        containerColor =
                            Color(0xF7FFFFFF)
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


                    CircularProgressIndicator(

                        color =
                            Color(0xFF0F766E)
                    )


                    Spacer(

                        modifier =
                            Modifier.height(
                                10.dp
                            )
                    )


                    Text(

                        text =
                            mensaje,

                        color =
                            Color(0xFF475569)
                    )
                }
            }
        }


        // ====================================================================
        // TARJETA DE PROFESIONAL SELECCIONADO
        // ====================================================================

        profesionalSeleccionado?.let {
                profesional ->


            ProfesionalMapaCard(

                profesional =
                    profesional,

                modifier =
                    Modifier
                        .align(
                            Alignment.BottomCenter
                        )
                        .padding(
                            16.dp
                        )
            )

        } ?: run {


            // ================================================================
            // TARJETA DE ESTADO
            // ================================================================

            if (
                !cargando
            ) {


                Card(

                    modifier =
                        Modifier
                            .align(
                                Alignment.BottomCenter
                            )
                            .fillMaxWidth()
                            .padding(
                                16.dp
                            ),

                    shape =
                        RoundedCornerShape(
                            22.dp
                        ),

                    colors =
                        CardDefaults.cardColors(

                            containerColor =
                                Color(0xF7FFFFFF)
                        )
                ) {


                    Column(

                        modifier =
                            Modifier.padding(
                                16.dp
                            )
                    ) {


                        Text(

                            text =
                                "Profesionales cerca de ti",

                            color =
                                Color(0xFF0F766E),

                            fontSize =
                                16.sp,

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
                                mensaje,

                            color =
                                Color(0xFF64748B),

                            fontSize =
                                12.sp
                        )
                    }
                }
            }
        }
    }
}


// ============================================================================
// TARJETA DE PROFESIONAL
// ============================================================================

@Composable
fun ProfesionalMapaCard(

    profesional: ProfesionalMapaResponse,

    modifier: Modifier = Modifier
) {


    val consultorio =
        profesional.consultorio


    Card(

        modifier =
            modifier
                .fillMaxWidth(),

        shape =
            RoundedCornerShape(
                24.dp
            ),

        colors =
            CardDefaults.cardColors(

                containerColor =
                    Color(0xFAFFFFFF)
            ),

        elevation =
            CardDefaults.cardElevation(

                defaultElevation =
                    8.dp
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


                Icon(

                    imageVector =
                        Icons.Default.LocationOn,

                    contentDescription =
                        null,

                    tint =
                        Color(0xFF0F766E)
                )


                Spacer(

                    modifier =
                        Modifier.width(
                            8.dp
                        )
                )


                Column {


                    Text(

                        text =
                            profesional.nombre,

                        fontSize =
                            17.sp,

                        fontWeight =
                            FontWeight.Bold,

                        color =
                            Color(0xFF0F172A)
                    )


                    Text(

                        text =
                            profesional.especialidad.ifBlank {

                                profesional.profesion.ifBlank {

                                    "Profesional de salud"
                                }
                            },

                        fontSize =
                            13.sp,

                        color =
                            Color(0xFF0F766E)
                    )
                }
            }


            Spacer(

                modifier =
                    Modifier.height(
                        12.dp
                    )
            )


            Text(

                text =
                    consultorio.nombre.ifBlank {

                        "Consultorio"
                    },

                fontWeight =
                    FontWeight.SemiBold,

                color =
                    Color(0xFF334155)
            )


            consultorio.direccion
                ?.takeIf {
                    it.isNotBlank()
                }
                ?.let {
                        direccion ->


                    Spacer(

                        modifier =
                            Modifier.height(
                                4.dp
                            )
                    )


                    Text(

                        text =
                            direccion,

                        fontSize =
                            12.sp,

                        color =
                            Color(0xFF64748B)
                    )
                }


            consultorio.horario
                ?.takeIf {
                    it.isNotBlank()
                }
                ?.let {
                        horario ->


                    Spacer(

                        modifier =
                            Modifier.height(
                                4.dp
                            )
                    )


                    Text(

                        text =
                            "Horario: $horario",

                        fontSize =
                            12.sp,

                        color =
                            Color(0xFF64748B)
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
                    "Toca otro marcador para consultar otro profesional.",

                fontSize =
                    11.sp,

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
