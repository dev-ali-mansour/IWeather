package dev.alimansour.iweather.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import dev.alimansour.iweather.domain.model.City

/**
 * WeatherApp Android Application developed by: Ali Mansour
 * ----------------- WeatherApp IS FREE SOFTWARE -------------------
 * https://www.alimansour.dev   |   mailto:dev.ali.mansour@gmail.com
 */
@Entity(tableName = "cities", indices = [Index(value = ["name", "country"], unique = true)])
data class CityEntity(
    @PrimaryKey(autoGenerate = true)
    var cityId: Int,
    @SerializedName("name")
    var name: String,
    @SerializedName("country")
    var country: String
)

fun CityEntity.toModel(): City = City(cityId, name, country)

fun City.toEntity(): CityEntity = CityEntity(id, name, country)
