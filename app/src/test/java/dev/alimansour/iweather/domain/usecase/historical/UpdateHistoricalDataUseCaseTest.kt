package dev.alimansour.iweather.domain.usecase.historical

import com.google.common.truth.Truth.assertThat
import dev.alimansour.iweather.TestUtil.TEST_HISTORICAL_LIST
import dev.alimansour.iweather.TestUtil.TEST_UPDATED_HISTORICAL_LIST
import dev.alimansour.iweather.TestUtil.aswan
import dev.alimansour.iweather.TestUtil.cairo
import dev.alimansour.iweather.TestUtil.giza
import dev.alimansour.iweather.TestUtil.luxor
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
class UpdateHistoricalDataUseCaseTest {
    private lateinit var weatherRepository: WeatherRepository
    private lateinit var updateHistoricalDataUseCase: UpdateHistoricalDataUseCase

    @Before
    fun setUp() {
        weatherRepository = mock(WeatherRepository::class.java)
        updateHistoricalDataUseCase = UpdateHistoricalDataUseCase(weatherRepository)
    }

    @Test
    fun `when response is successful then return the right list of updated historical data`() =
        runBlocking {
            //GIVEN
            val list = TEST_HISTORICAL_LIST.toMutableList()
            val cairoData = list.filter { historical -> historical.city == cairo }
            val gizaData = list.filter { historical -> historical.city == giza }
            val luxorData = list.filter { historical -> historical.city == luxor }
            val aswanData = list.filter { historical -> historical.city == aswan }

            `when`(weatherRepository.updateHistoricalData()).then {
                list.clear()
                list.addAll(TEST_UPDATED_HISTORICAL_LIST)
            }
            `when`(weatherRepository.getHistoricalData(cairo.id))
                .thenReturn(flow { emit(cairoData) })
            `when`(weatherRepository.getHistoricalData(giza.id))
                .thenReturn(flow { emit(gizaData) })
            `when`(weatherRepository.getHistoricalData(luxor.id))
                .thenReturn(flow { emit(luxorData) })
            `when`(weatherRepository.getHistoricalData(aswan.id))
                .thenReturn(flow { emit(aswanData) })

            //WHEN
            updateHistoricalDataUseCase.execute()
            val cairoHistorical = weatherRepository.getHistoricalData(cairo.id).first()
            val gizaHistorical = weatherRepository.getHistoricalData(giza.id).first()
            val luxorHistorical = weatherRepository.getHistoricalData(luxor.id).first()
            val aswanHistorical = weatherRepository.getHistoricalData(aswan.id).first()

            //THEN
            assertThat(cairoHistorical).isEqualTo(cairoData)
            assertThat(gizaHistorical).isEqualTo(gizaData)
            assertThat(luxorHistorical).isEqualTo(luxorData)
            assertThat(aswanHistorical).isEqualTo(aswanData)
            assertThat(aswanHistorical).isEmpty()
        }

    @Test(expected = Exception::class)
    fun `when response is unsuccessful then exception will be thrown`() = runBlocking {
        //GIVEN
        `when`(weatherRepository.updateHistoricalData()).then { throw (Exception()) }

        //WHEN
        updateHistoricalDataUseCase.execute()

        //THEN Exception will be thrown
    }
}