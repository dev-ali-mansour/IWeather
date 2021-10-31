package dev.alimansour.iweather.domain.usecase.city

import com.google.common.truth.Truth.assertThat
import dev.alimansour.iweather.TestUtil.TEST_CITY_LIST
import dev.alimansour.iweather.TestUtil.cairo
import dev.alimansour.iweather.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

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
        weatherRepository = Mockito.mock(WeatherRepository::class.java)
        deleteCityUseCase = DeleteCityUseCase(weatherRepository)
    }

    @Test
    fun `When response is successful Then return the right list of cities`() = runBlocking {
        //GIVEN
        val list = TEST_CITY_LIST.toMutableList()
        Mockito.`when`(weatherRepository.deleteCity(cairo)).then {
            list.remove(cairo)
        }
        Mockito.`when`(weatherRepository.getCities()).thenReturn(flow { emit(list) })

        //WHEN
        deleteCityUseCase.execute(cairo)
        val cities = weatherRepository.getCities().first()

        //THEN
        assertThat(cities).isEqualTo(list)
    }

    @Test(expected = Exception::class)
    fun `When response is not successful Then throw exception`() = runBlocking {
        //GIVEN
        Mockito.`when`(weatherRepository.deleteCity(cairo)).thenThrow(Exception())
        //WHEN
        deleteCityUseCase.execute(cairo)

        //THEN Exception is thrown
    }
}