package dev.alimansour.iweather.domain.usecase.city

import com.google.common.truth.Truth.assertThat
import dev.alimansour.iweather.TestUtil.TEST_CITY_LIST
import dev.alimansour.iweather.TestUtil.aswan
import dev.alimansour.iweather.TestUtil.cairo
import dev.alimansour.iweather.TestUtil.giza
import dev.alimansour.iweather.TestUtil.luxor
import dev.alimansour.iweather.domain.model.City
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
class AddCityUseCaseTest {
    private lateinit var weatherRepository: WeatherRepository
    private lateinit var addCityUseCase: AddCityUseCase

    @Before
    fun setUp() {
        weatherRepository = Mockito.mock(WeatherRepository::class.java)
        addCityUseCase = AddCityUseCase(weatherRepository)
    }

    @Test
    fun `When response is successful Then return the right list of cities`() = runBlocking {
        //GIVEN
        val list = TEST_CITY_LIST.toMutableList()
        Mockito.`when`(weatherRepository.addCity(aswan.name)).then {
            list.add(City(aswan.id, aswan.name, aswan.country))
        }
        Mockito.`when`(weatherRepository.getCities()).thenReturn(flow { emit(list) })

        //WHEN
        addCityUseCase.execute(aswan.name)
        val cities = weatherRepository.getCities().first()

        //THEN
        assertThat(cities).isEqualTo(listOf(cairo, giza, luxor, aswan))
    }

    @Test(expected = Exception::class)
    fun `When response is not successful Then throw exception`() = runBlocking {
        //GIVEN
        Mockito.`when`(addCityUseCase.execute(aswan.name)).thenThrow(Exception())

        //WHEN
        addCityUseCase.execute(aswan.name)

        //THEN Exception is thrown
    }
}