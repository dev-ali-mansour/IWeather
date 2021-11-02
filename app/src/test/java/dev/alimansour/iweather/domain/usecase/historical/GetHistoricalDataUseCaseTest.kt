package dev.alimansour.iweather.domain.usecase.historical

import com.google.common.truth.Truth.assertThat
import dev.alimansour.iweather.TestUtil.TEST_HISTORICAL_LIST
import dev.alimansour.iweather.TestUtil.cairo
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
class GetHistoricalDataUseCaseTest {
    private lateinit var weatherRepository: WeatherRepository
    private lateinit var getHistoricalDataUseCase: GetHistoricalDataUseCase

    @Before
    fun setUp() {
        weatherRepository = mock(WeatherRepository::class.java)
        getHistoricalDataUseCase = GetHistoricalDataUseCase(weatherRepository)
    }

    @Test
    fun `when response is successful then return the right list of updated historical data`() =
        runBlocking {
            //GIVEN
            val data = TEST_HISTORICAL_LIST.filter { historical -> historical.city == cairo }
            `when`(weatherRepository.getHistoricalData(cairo.id))
                .thenReturn(flow { emit(data) })

            //WHEN
            val historicalData = getHistoricalDataUseCase.execute(cairo.id).first()

            //THEN
            assertThat(historicalData).isEqualTo(data)
        }

    @Test(expected = Exception::class)
    fun `when response is unsuccessful then exception will be thrown`() = runBlocking {
        //GIVEN
        `when`(weatherRepository.getHistoricalData(cairo.id)).then { throw (Exception()) }

        //WHEN
        val historicalData = getHistoricalDataUseCase.execute(cairo.id).first()

        //THEN
        assertThat(historicalData).isNull()
    }
}