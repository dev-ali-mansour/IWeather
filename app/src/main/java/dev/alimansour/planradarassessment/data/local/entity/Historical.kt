package dev.alimansour.planradarassessment.data.local.entity

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime
import java.util.*

/**
 * WeatherApp Android Application developed by: Ali Mansour
 * ----------------- WeatherApp IS FREE SOFTWARE -------------------
 * https://www.alimansour.dev   |   mailto:dev.ali.mansour@gmail.com
 */

@Entity(tableName = "historical_data")
data class Historical(
    @PrimaryKey(autoGenerate = false)
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