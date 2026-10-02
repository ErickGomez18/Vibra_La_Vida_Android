package com.example.vibralavida.ia

object HydrationRecommendationEngine {

    fun build(userContext: AiUserContext): String {
        val profile =
            RecommendationProfileInterpreter.interpret(userContext)

        return when (profile.activityCategory) {
            ActivityCategory.SEDENTARY ->
                "De acuerdo con tu perfil, procura tomar agua simple de forma regular durante el día, por ejemplo con tus comidas y entre ellas. No necesitas forzarte a una cantidad fija universal; aumenta tu consumo si tienes sed, hace mucho calor o realizas más actividad física."

            ActivityCategory.LIGHT ->
                "De acuerdo con tu nivel de actividad, procura beber agua simple durante el día y llevar agua contigo cuando salgas o hagas ejercicio. La cantidad exacta puede variar, así que usa la sed, el calor y tu actividad como señales para beber con mayor frecuencia."

            ActivityCategory.MODERATE ->
                "Como tienes actividad moderada, procura beber agua regularmente durante el día y tomar más alrededor de tus periodos de ejercicio, especialmente si hace calor o sudas bastante. No te recomendaría una cantidad fija de litros solo con los datos actuales de tu perfil."

            ActivityCategory.ACTIVE ->
                "Como tu nivel de actividad es alto, conviene mantener una hidratación frecuente antes, durante y después de tus sesiones, especialmente con calor o sudoración intensa. La cantidad exacta depende de tu actividad y pérdidas de líquido, por eso Nuby no usa un número universal de litros."

            ActivityCategory.UNKNOWN ->
                "Procura beber agua simple regularmente durante el día y aumentar la frecuencia cuando tengas sed, realices actividad física o estés expuesto a mucho calor. La cantidad exacta varía entre personas, así que no conviene usar un número fijo universal sin más información."
        }
    }
}
