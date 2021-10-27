package dev.alimansour.iweather.data.local.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import dev.alimansour.iweather.domain.model.HistoricalData

/**
 * WeatherApp Android Application developed by: Ali Mansour
 * ----------------- WeatherApp IS FREE SOFTWARE -------------------
 * https://www.alimansour.dev   |   mailto:dev.ali.mansour@gmail.com
 */

@Entity(tableName = "historical_data")
data class Historical(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    @Embedded
    var city: City,
    @SerializedName("icon")
    val icon: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("temperature")
    val temperature: Double,
    @SerializedName("humidity")
    val humidity: Double,
    @SerializedName("wind_speed")
    val windSpeed: Double,
)

fun Historical.toModel() = HistoricalData(
    id, city.toModel(), icon, date, description, temperature, humidity, windSpeed
)

fun HistoricalData.toEntity() = Historical(
    id, city.toEntity(), icon, date, description, temperature, humidity, windSpeed
)