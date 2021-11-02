package dev.alimansour.iweather.domain.usecase.city

import com.google.common.truth.Truth.assertThat
import dev.alimansour.iweather.TestUtil.TEST_CITY_LIST
import dev.alimansour.iweather.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

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
        weatherRepository = mock(WeatherRepository::class.java)
        getCitiesUseCase = GetCitiesUseCase(weatherRepository)
    }

    @Test
    fun `When response is not successful Then return the right list of cities`() = runBlocking {
        //GIVEN
        `when`(weatherRepository.getCities()).thenReturn(flow {
            emit(TEST_CITY_LIST)
        })

        //WHEN
        val cities = getCitiesUseCase.execute().first()

        //THEN
        assertThat(cities).isEqualTo(TEST_CITY_LIST)
    }

    @Test(expected = Exception::class)
    fun `When response is not successful Then throw exception`() = runBlocking {
        //GIVEN
        `when`(getCitiesUseCase.execute()).then { throw (Exception()) }

        //WHEN
        val cities = getCitiesUseCase.execute().first()

        //THEN
        assertThat(cities).isNull()
    }
}