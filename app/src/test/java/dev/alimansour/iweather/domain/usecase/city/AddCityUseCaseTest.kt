package dev.alimansour.iweather.domain.usecase.city

import com.google.common.truth.Truth.assertThat
import dev.alimansour.iweather.cairo
import dev.alimansour.iweather.data.local.entity.City
import dev.alimansour.iweather.data.repository.FakeWeatherRepository
import dev.alimansour.iweather.domain.repository.WeatherRepository
import dev.alimansour.iweather.giza
import dev.alimansour.iweather.luxor
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

/**
 * WeatherApp Android Application developed by: Ali Mansour
 * ----------------- WeatherApp IS FREE SOFTWARE -------------------
 * https://www.alimansour.dev   |   mailto:dev.ali.mansour@gmail.com
 */
class AddCityUseCaseTest {
    private lateinit var weatherRepository: WeatherRepository
    private lateinit var addCityUseCase: AddCityUseCase

    @Before
    fun setUp() {
        weatherRepository = FakeWeatherRepository()
        addCityUseCase = AddCityUseCase(weatherRepository)
    }

    @Test
    fun execute_addNewCity_returnTheRightListOfCities() = runBlocking {
        val cities = addCityUseCase.execute("Aswan")

        val aswan = City(5, "Aswan", "EG")
        val list = listOf(cairo, giza, luxor,aswan)
        assertThat(cities).isEqualTo(list)
    }
}