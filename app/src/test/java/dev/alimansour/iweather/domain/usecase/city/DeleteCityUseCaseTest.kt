package dev.alimansour.iweather.domain.usecase.city

import com.google.common.truth.Truth
import dev.alimansour.iweather.cairo
import dev.alimansour.iweather.data.local.entity.City
import dev.alimansour.iweather.data.mappers.CityMapper
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
class DeleteCityUseCaseTest {
    private lateinit var weatherRepository: WeatherRepository
    private lateinit var deleteCityUseCase: DeleteCityUseCase
    private lateinit var cityMapper: CityMapper

    @Before
    fun setUp() {
        weatherRepository = FakeWeatherRepository()
        deleteCityUseCase = DeleteCityUseCase(weatherRepository)
        cityMapper = CityMapper()
    }

    @Test
    fun execute_deleteCity_returnTheRightListOfCities() = runBlocking {
        val cities = deleteCityUseCase.execute(cityMapper.mapFromEntity(cairo))
        val list = listOf(giza, luxor)
        Truth.assertThat(cities).isEqualTo(list)
    }
}