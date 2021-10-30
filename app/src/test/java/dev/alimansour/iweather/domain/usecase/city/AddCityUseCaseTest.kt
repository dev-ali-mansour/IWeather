package dev.alimansour.iweather.domain.usecase.city

import com.google.common.truth.Truth.assertThat
import dev.alimansour.iweather.TestUtil.aswan
import dev.alimansour.iweather.TestUtil.cairo
import dev.alimansour.iweather.TestUtil.giza
import dev.alimansour.iweather.TestUtil.luxor
import dev.alimansour.iweather.data.local.entity.toModel
import dev.alimansour.iweather.data.repository.FakeWeatherRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

/**
 * WeatherApp Android Application developed by: Ali Mansour
 * ----------------- WeatherApp IS FREE SOFTWARE -------------------
 * https://www.alimansour.dev   |   mailto:dev.ali.mansour@gmail.com
 */
class AddCityUseCaseTest {
    private lateinit var weatherRepository: FakeWeatherRepository
    private lateinit var addCityUseCase: AddCityUseCase

    @Before
    fun setUp() {
        weatherRepository = FakeWeatherRepository()
        addCityUseCase = AddCityUseCase(weatherRepository)
    }

    @Test
    fun `When response is successful Then return the right list of cities`() = runBlocking {
        //GIVEN
        val list = listOf(cairo, giza, luxor, aswan).map { it.toModel() }

        //WHEN
        addCityUseCase.execute(aswan.name)
        val cities = weatherRepository.getCities().first()

        //THEN
        assertThat(cities).isEqualTo(list)
    }

    @Test(expected = Exception::class)
    fun `When response is not successful Then throw exception`() = runBlocking {
        //GIVEN
        weatherRepository.setSuccessful(false)

        //WHEN
        addCityUseCase.execute(aswan.name)

        //THEN Exception is thrown
    }
}