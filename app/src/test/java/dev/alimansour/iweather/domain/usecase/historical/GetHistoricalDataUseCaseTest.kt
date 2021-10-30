package dev.alimansour.iweather.domain.usecase.historical

import com.google.common.truth.Truth.assertThat
import dev.alimansour.iweather.TestUtil.TEST_HISTORICAL_LIST
import dev.alimansour.iweather.TestUtil.cairo
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
class GetHistoricalDataUseCaseTest {
    private lateinit var weatherRepository: FakeWeatherRepository
    private lateinit var getHistoricalDataUseCase: GetHistoricalDataUseCase

    @Before
    fun setUp() {
        weatherRepository = FakeWeatherRepository()
        getHistoricalDataUseCase = GetHistoricalDataUseCase(weatherRepository)
    }

    @Test
    fun `when response is successful then return the right list of updated historical data`() =
        runBlocking {
            //GIVEN
            val data = TEST_HISTORICAL_LIST
                .filter { historical -> historical.cityEntity == cairo }
                .map { it.toModel() }

            //WHEN
            val historicalData = getHistoricalDataUseCase.execute(cairo.cityId).first()

            //THEN
            assertThat(historicalData).isEqualTo(data)
        }

    @Test(expected = Exception::class)
    fun `when response is unsuccessful then exception will be thrown`() = runBlocking {
        //GIVEN
        weatherRepository.setSuccessful(false)

        //WHEN
        val historicalData = getHistoricalDataUseCase.execute(cairo.cityId).first()

        //THEN
        assertThat(historicalData).isNull()
    }
}