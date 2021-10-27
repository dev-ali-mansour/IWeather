package dev.alimansour.iweather

import dev.alimansour.iweather.data.local.entity.City
import dev.alimansour.iweather.data.local.entity.Historical

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

    val historical1 =
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
    val historical2 = historical1.copy(id = 2, date = "2021-10-20 09:00:00")
    val historical3 = historical1.copy(id = 3, date = "2021-10-20 12:00:00")
    val historical4 = historical1.copy(id = 4, city = giza)

    val TEST_HISTORICAL_LIST = listOf(historical1, historical2, historical3, historical4)

    val historical5 = historical1.copy(id = 5, date = "2021-10-22 06:00:00")
    val historical6 = historical1.copy(id = 6, city = giza)
    val historical7 = historical5.copy(id = 7, city = luxor)
    val TEST_UPDATED_HISTORICAL_LIST = listOf(historical5, historical6, historical7)
}