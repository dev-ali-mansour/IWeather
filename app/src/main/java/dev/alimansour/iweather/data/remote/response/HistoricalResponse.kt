package dev.alimansour.iweather.data.remote.response

import com.google.gson.annotations.SerializedName

data class HistoricalResponse(

    @field:SerializedName("city")
    val city: City,

    @field:SerializedName("cod")
    val cod: String,

    @field:SerializedName("message")
    val message: Int,

    @field:SerializedName("list")
    val list: List<ListItem>? = null
)

data class City(

    @field:SerializedName("country")
    val country: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("id")
    val id: Int
)

data class Wind(
    @field:SerializedName("speed")
    val speed: Double
)

data class ListItem(
    @field:SerializedName("dt_txt")
    val date: String,

    @field:SerializedName("weather")
    val weather: List<WeatherItem>,

    @field:SerializedName("main")
    val main: Main,

    @field:SerializedName("wind")
    val wind: Wind
)

data class Main(

    @field:SerializedName("temp")
    val temp: Double,

    @field:SerializedName("humidity")
    val humidity: Double
)

data class WeatherItem(

    @field:SerializedName("icon")
    val icon: String,

    @field:SerializedName("description")
    val description: String
)