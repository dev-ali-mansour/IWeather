package dev.alimansour.planradarassessment.domain.model

/**
 * WeatherApp Android Application developed by: Ali Mansour
 * ----------------- WeatherApp IS FREE SOFTWARE -------------------
 * https://www.alimansour.dev   |   mailto:dev.ali.mansour@gmail.com
 */
data class LocationData(
    val name: String,
    val country: String,
    val temperature: Double,
    val description: String,
    val humidity: Double,
    val windSpeed: Int
)