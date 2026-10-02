package com.example.vibralavida.ia

data class RecommendationProfileSummary(
    val age: Int? = null,
    val isMinor: Boolean = false,
    val isOlderAdult: Boolean = false,
    val activityCategory: ActivityCategory = ActivityCategory.UNKNOWN,
    val bmi: Double? = null,
    val lowBmiFlag: Boolean = false
)

enum class ActivityCategory {
    SEDENTARY,
    LIGHT,
    MODERATE,
    ACTIVE,
    UNKNOWN
}
