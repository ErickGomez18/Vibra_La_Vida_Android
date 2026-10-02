package com.example.vibralavida.receivers


// ============================================================================
// ANDROID
// ============================================================================

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent


// ============================================================================
// FIREBASE
// ============================================================================

import com.google.firebase.auth.FirebaseAuth


// ============================================================================
// API
// ============================================================================

import com.example.vibralavida.api.CitasRepository
import com.example.vibralavida.api.MedicamentosRepository


// ============================================================================
// RECORDATORIOS
// ============================================================================

import com.example.vibralavida.agenda.citas.ProgramadorRecordatoriosCita
import com.example.vibralavida.agenda.medicamentos.ProgramadorRecordatoriosMedicamento


// ============================================================================
// BOOT RECEIVER
// ============================================================================
//
// Este receiver se ejecuta cuando Android termina de iniciar.
//
// ¿Por qué lo necesitamos?
//
// AlarmManager elimina las alarmas programadas cuando el teléfono
// se reinicia.
//
// Por eso, después del reinicio:
//
// 1. Revisamos si existe una sesión de Firebase.
// 2. Recuperamos los medicamentos desde la API.
// 3. Volvemos a programar sus recordatorios.
// 4. Recuperamos las citas.
// 5. Volvemos a programar sus recordatorios.
//
// ============================================================================

class BootReceiver :
    BroadcastReceiver() {


    // ========================================================================
    // ON RECEIVE
    // ========================================================================

    override fun onReceive(
        context: Context,
        intent: Intent
    ) {


        // ====================================================================
        // VALIDAR ACCIÓN
        // ====================================================================

        if (
            intent.action !=
            Intent.ACTION_BOOT_COMPLETED
        ) {

            return
        }


        // ====================================================================
        // VERIFICAR SESIÓN
        // ====================================================================

        val usuarioActual =
            FirebaseAuth
                .getInstance()
                .currentUser


        // Si no existe una sesión iniciada,
        // no hay nada que reprogramar.

        if (
            usuarioActual == null
        ) {

            return
        }


        // ====================================================================
        // MANTENER RECEIVER ACTIVO
        // ====================================================================
        //
        // BroadcastReceiver tiene una vida muy corta.
        //
        // Como vamos a realizar consultas asíncronas a la API,
        // utilizamos goAsync().
        //
        // ====================================================================

        val pendingResult =
            goAsync()


        // ====================================================================
        // CONTROL DE OPERACIONES
        // ====================================================================

        var medicamentosTerminados =
            false


        var citasTerminadas =
            false


        // ====================================================================
        // FINALIZAR RECEIVER
        // ====================================================================

        fun comprobarFinalizacion() {

            if (
                medicamentosTerminados &&
                citasTerminadas
            ) {

                pendingResult.finish()
            }
        }


        // ====================================================================
        // MEDICAMENTOS
        // ====================================================================

        MedicamentosRepository
            .obtenerMedicamentos(

                onSuccess = {
                        medicamentos ->


                    medicamentos.forEach {
                            medicamento ->


                        ProgramadorRecordatoriosMedicamento
                            .programarMedicamento(

                                context =
                                    context.applicationContext,

                                medicamento =
                                    medicamento
                            )
                    }


                    medicamentosTerminados =
                        true


                    comprobarFinalizacion()
                },


                onError = {
                        mensaje ->


                    println(
                        "BootReceiver - medicamentos: $mensaje"
                    )


                    medicamentosTerminados =
                        true


                    comprobarFinalizacion()
                }
            )


        // ====================================================================
        // CITAS
        // ====================================================================

        CitasRepository
            .obtenerCitas(

                onSuccess = {
                        citas ->


                    ProgramadorRecordatoriosCita
                        .programarCitas(

                            context =
                                context.applicationContext,

                            citas =
                                citas
                        )


                    citasTerminadas =
                        true


                    comprobarFinalizacion()
                },


                onUnauthorized = {


                    println(
                        "BootReceiver - sesión no autorizada"
                    )


                    citasTerminadas =
                        true


                    comprobarFinalizacion()
                },


                onError = {
                        mensaje ->


                    println(
                        "BootReceiver - citas: $mensaje"
                    )


                    citasTerminadas =
                        true
                        true


                    comprobarFinalizacion()
                }
            )
    }
}