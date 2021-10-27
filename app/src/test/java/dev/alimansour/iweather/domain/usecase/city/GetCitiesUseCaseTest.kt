package dev.alimansour.iweather.domain.usecase.city

import com.google.common.truth.Truth.assertThat
import dev.alimansour.iweather.TestUtil.TEST_CITY_LIST
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
class GetCitiesUseCaseTest {
    private lateinit var weatherRepository: WeatherRepository
    private lateinit var getCitiesUseCase: GetCitiesUseCase

    @Before
    fun setUp() {
        weatherRepository = FakeWeatherRepository()
        getCitiesUseCase = GetCitiesUseCase(weatherRepository)
    }

    @Test
    fun execute_returnTheRightListOfCities() = runBlocking {
        val cities = getCitiesUseCase.execute()
        assertThat(cities).isEqualTo(TEST_CITY_LIST)
    }
}