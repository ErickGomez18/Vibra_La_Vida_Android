package com.example.vibralavida.ia.cartilla

object CartillaAgeSelector {

    fun select(
        age: Int?,
        gender: String?
    ): CartillaAgeGroup {

        if (age == null || age < 0) {
            return CartillaAgeGroup.DESCONOCIDO
        }

        return when (age) {

            in 0..9 ->
                CartillaAgeGroup.NINAS_NINOS_0_9

            in 10..19 ->
                CartillaAgeGroup.ADOLESCENTES_10_19

            in 20..59 -> {

                val normalized =
                    gender
                        ?.trim()
                        ?.lowercase()
                        .orEmpty()

                when {

                    normalized.contains("mujer") ||
                    normalized.contains("femen") ->
                        CartillaAgeGroup.MUJERES_20_59

                    normalized.contains("hombre") ||
                    normalized.contains("mascul") ->
                        CartillaAgeGroup.HOMBRES_20_59

                    else ->
                        CartillaAgeGroup.DESCONOCIDO
                }
            }

            else ->
                CartillaAgeGroup.PERSONAS_MAYORES_60_MAS
        }
    }
}
