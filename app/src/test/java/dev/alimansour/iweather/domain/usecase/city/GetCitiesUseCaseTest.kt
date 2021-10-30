package dev.alimansour.iweather.domain.usecase.city

import com.google.common.truth.Truth.assertThat
import dev.alimansour.iweather.TestUtil.TEST_CITY_ENTITY_LIST
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
class GetCitiesUseCaseTest {
    private lateinit var weatherRepository: FakeWeatherRepository
    private lateinit var getCitiesUseCase: GetCitiesUseCase

    @Before
    fun setUp() {
        weatherRepository = FakeWeatherRepository()
        getCitiesUseCase = GetCitiesUseCase(weatherRepository)
    }

    @Test
    fun `When response is not successful Then return the right list of cities`() = runBlocking {
        //GIVEN
        val list = TEST_CITY_ENTITY_LIST.map { it.toModel() }

        //WHEN
        val cities = getCitiesUseCase.execute().first()

        //THEN
        assertThat(cities).isEqualTo(list)
    }

    @Test(expected = Exception::class)
    fun `When response is not successful Then throw exception`() = runBlocking {
        //GIVEN
        weatherRepository.setSuccessful(false)

        //WHEN
        val cities = getCitiesUseCase.execute().first()

        //THEN
        assertThat(cities).isNull()
    }
}