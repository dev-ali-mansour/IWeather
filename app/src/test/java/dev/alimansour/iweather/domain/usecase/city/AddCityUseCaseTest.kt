package dev.alimansour.iweather.domain.usecase.city

import com.google.common.truth.Truth.assertThat
import dev.alimansour.iweather.TestUtil.aswan
import dev.alimansour.iweather.TestUtil.cairo
import dev.alimansour.iweather.TestUtil.giza
import dev.alimansour.iweather.TestUtil.luxor
import dev.alimansour.iweather.data.repository.FakeWeatherRepository
import dev.alimansour.iweather.domain.repository.WeatherRepository
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
        val cities = addCityUseCase.execute(aswan.name)
        val list = listOf(cairo, giza, luxor, aswan)
        assertThat(cities).isEqualTo(list)
    }
}