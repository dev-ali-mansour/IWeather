package dev.alimansour.iweather

import androidx.annotation.VisibleForTesting
import dev.alimansour.iweather.data.local.entity.toEntity
import dev.alimansour.iweather.data.remote.response.*
import dev.alimansour.iweather.domain.model.City
import dev.alimansour.iweather.domain.model.Historical

/**
 * WeatherApp Android Application developed by: Ali Mansour
 * ----------------- WeatherApp IS FREE SOFTWARE -------------------
 * https://www.alimansour.dev   |   mailto:dev.ali.mansour@gmail.com
 */
object TestUtil {
    val cairo = City(1, "Cairo", "EG")
    val giza = City(2, "Giza", "EG")
    val luxor = City(3, "Luxor", "EG")
    val aswan = City(4, "Aswan", "EG")
    val TEST_CITY_LIST: List<City> = listOf(cairo, giza, luxor)

    private val historical1 =
        Historical(
            1,
            cairo,
            "clear.png",
            "2021-10-20 06:00:00",
            "Clear",
            27.2,
            47.0,
            75.0
        )
    private val data = WeatherData(
        date = "2021-10-18 18:00:00",
        weather = listOf(WeatherItem(icon = "03n", description = "scattered clouds")),
        main = Main(temp = 25.3, humidity = 47.0),
        wind = Wind(speed = 7.2)
    )
    private val historical2 = historical1.copy(id = 2, date = "2021-10-20 09:00:00")
    private val historical3 = historical1.copy(id = 3, date = "2021-10-20 12:00:00")
    private val historical4 = historical1.copy(id = 4, city = giza)
    private val historical5 = historical1.copy(id = 5, date = "2021-10-22 06:00:00")
    private val historical6 = historical1.copy(id = 6, city = giza)
    private val historical7 = historical5.copy(id = 7, city = luxor)
    private val historical8 = historical4.copy(id = 8, city = aswan)
    private val historical9 = historical8.copy(id = 9)

    val TEST_HISTORICAL_LIST = listOf(historical1, historical2, historical3, historical4)
    val TEST_ASWAN_HISTORICAL_LIST = listOf(historical8, historical9).map { it.toEntity() }
    val TEST_SAVED_HISTORICAL_LIST =
        listOf(historical1, historical2, historical3, historical4, historical8, historical9)
    val TEST_UPDATED_HISTORICAL_LIST = listOf(historical5, historical6, historical7)
}

@VisibleForTesting(otherwise = VisibleForTesting.NONE)
fun List<Historical>.toHistoricalResponse(): HistoricalResponse {
    val city = City(this[0].city.country, this[0].city.name, this[0].city.id)
    val historicalData = mutableListOf<WeatherData>()
    this.forEach { historical ->
        historicalData.add(
            WeatherData(
                historical.date,
                listOf(WeatherItem(historical.icon, historical.description)),
                Main(historical.temperature, historical.humidity),
                Wind(historical.windSpeed)
            )
        )
    }
    return HistoricalResponse(city, "", 0, historicalData)
}
