package com.example.vibralavida.ia

import android.content.Intent
import android.net.Uri

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.vibralavida.api.nuby.EspecialistaNuby


// ============================================================================
// TARJETA DE PROFESIONAL SUGERIDO POR NUBY
// ============================================================================

@Composable
fun NubySpecialistCard(

    specialist: EspecialistaNuby,

    modifier: Modifier = Modifier
) {


    val context =
        LocalContext.current


    Card(

        modifier =
            modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(
                16.dp
            ),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    Color(0xFFF8FAFC)
            ),

        elevation =
            CardDefaults.cardElevation(
                defaultElevation =
                    2.dp
            )
    ) {


        Column(

            modifier =
                Modifier.padding(
                    12.dp
                ),

            verticalArrangement =
                Arrangement.spacedBy(
                    4.dp
                )
        ) {


            Text(

                text =
                    specialist.nombre,

                fontSize =
                    14.sp,

                fontWeight =
                    FontWeight.Bold,

                color =
                    Color(0xFF0F766E)
            )


            if (
                specialist.especialidad.isNotBlank()
            ) {

                Text(
                    text =
                        specialist.especialidad,
                    fontSize =
                        12.sp
                )
            }


            if (
                specialist.profesion.isNotBlank()
            ) {

                Text(
                    text =
                        specialist.profesion,
                    fontSize =
                        11.sp,
                    color =
                        Color(0xFF475569)
                )
            }


            if (
                specialist.cedulaProfesional.isNotBlank()
            ) {

                Text(
                    text =
                        "Cédula profesional: ${specialist.cedulaProfesional}",
                    fontSize =
                        10.sp,
                    color =
                        Color(0xFF64748B)
                )
            }


            if (
                specialist.modalidad.isNotEmpty()
            ) {

                Text(
                    text =
                        "Modalidad: ${specialist.modalidad.joinToString(", ")}",
                    fontSize =
                        10.sp,
                    color =
                        Color(0xFF64748B)
                )
            }


            specialist.ubicacion
                ?.takeIf {
                    it.isNotBlank()
                }
                ?.let {
                        ubicacion ->

                    Text(
                        text =
                            "Ubicación: $ubicacion",
                        fontSize =
                            10.sp,
                        color =
                            Color(0xFF64748B)
                    )
                }


            specialist.horarioAtencion
                ?.takeIf {
                    it.isNotBlank()
                }
                ?.let {
                        horario ->

                    Text(
                        text =
                            "Horario: $horario",
                        fontSize =
                            10.sp,
                        color =
                            Color(0xFF64748B)
                    )
                }


            specialist.telefono
                ?.takeIf {
                    it.isNotBlank()
                }
                ?.let {
                        telefono ->


                    Spacer(
                        modifier =
                            Modifier.height(
                                4.dp
                            )
                    )


                    Row(
                        modifier =
                            Modifier.fillMaxWidth()
                    ) {


                        Button(

                            onClick = {

                                val intent =
                                    Intent(
                                        Intent.ACTION_DIAL,
                                        Uri.parse(
                                            "tel:$telefono"
                                        )
                                    )

                                context.startActivity(
                                    intent
                                )
                            },

                            colors =
                                ButtonDefaults.buttonColors(
                                    containerColor =
                                        Color(0xFF0F766E)
                                )
                        ) {

                            Text(
                                text =
                                    "Contactar",
                                fontSize =
                                    11.sp
                            )
                        }
                    }
                }
        }
    }
}
