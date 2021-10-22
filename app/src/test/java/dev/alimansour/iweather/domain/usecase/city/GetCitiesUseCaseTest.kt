package dev.alimansour.iweather.domain.usecase.city

import com.google.common.truth.Truth.assertThat
import dev.alimansour.iweather.TEST_CITY_LIST
import dev.alimansour.iweather.cairo
import dev.alimansour.iweather.data.local.entity.City
import dev.alimansour.iweather.data.mappers.CityMapper
import dev.alimansour.iweather.data.repository.FakeWeatherRepository
import dev.alimansour.iweather.data.repository.WeatherRepositoryImpl
import dev.alimansour.iweather.domain.repository.WeatherRepository
import dev.alimansour.iweather.giza
import dev.alimansour.iweather.luxor
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

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