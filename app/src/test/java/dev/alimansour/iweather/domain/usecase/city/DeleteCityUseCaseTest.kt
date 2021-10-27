package dev.alimansour.iweather.domain.usecase.city

import com.google.common.truth.Truth
import dev.alimansour.iweather.TestUtil.cairo
import dev.alimansour.iweather.TestUtil.giza
import dev.alimansour.iweather.TestUtil.luxor
import dev.alimansour.iweather.data.local.entity.toModel
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
class DeleteCityUseCaseTest {
    private lateinit var weatherRepository: WeatherRepository
    private lateinit var deleteCityUseCase: DeleteCityUseCase

    @Before
    fun setUp() {
        weatherRepository = FakeWeatherRepository()
        deleteCityUseCase = DeleteCityUseCase(weatherRepository)
    }

    @Test
    fun execute_deleteCity_returnTheRightListOfCities() = runBlocking {
        val cities = deleteCityUseCase.execute(cairo.toModel())
        val list = listOf(giza, luxor)
        Truth.assertThat(cities).isEqualTo(list)
    }
}