package dev.alimansour.iweather.data.local.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import dev.alimansour.iweather.domain.model.Historical

/**
 * WeatherApp Android Application developed by: Ali Mansour
 * ----------------- WeatherApp IS FREE SOFTWARE -------------------
 * https://www.alimansour.dev   |   mailto:dev.ali.mansour@gmail.com
 */

@Entity(tableName = "historical_data")
data class HistoricalEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    @Embedded
    var cityEntity: CityEntity,
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

fun HistoricalEntity.toModel() = Historical(
    id, cityEntity.toModel(), icon, date, description, temperature, humidity, windSpeed
)

fun Historical.toEntity() = HistoricalEntity(
    id, city.toEntity(), icon, date, description, temperature, humidity, windSpeed
)