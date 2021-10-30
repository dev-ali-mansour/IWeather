package dev.alimansour.iweather.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * WeatherApp Android Application developed by: Ali Mansour
 * ----------------- WeatherApp IS FREE SOFTWARE -------------------
 * https://www.alimansour.dev   |   mailto:dev.ali.mansour@gmail.com
 */
@Parcelize
data class City(
    val id: Int,
    val name: String,
    val country: String
) : Parcelable