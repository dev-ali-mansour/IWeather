package dev.alimansour.iweather.domain.usecase.historical

import com.google.common.truth.Truth.assertThat
import dev.alimansour.iweather.TestUtil.TEST_UPDATED_HISTORICAL_LIST
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
class UpdateHistoricalDataUseCaseTest {
    private lateinit var weatherRepository: FakeWeatherRepository
    private lateinit var updateHistoricalDataUseCase: UpdateHistoricalDataUseCase

    @Before
    fun setUp() {
        weatherRepository = FakeWeatherRepository()
        updateHistoricalDataUseCase = UpdateHistoricalDataUseCase(weatherRepository)
    }

    @Test
    fun `when response is successful then return the right list of updated historical data`() =
        runBlocking {
            //GIVEN
            val cairoData =
                TEST_UPDATED_HISTORICAL_LIST
                    .filter { historical -> historical.cityEntity == cairo }
                    .map { it.toModel() }
            val gizaData =
                TEST_UPDATED_HISTORICAL_LIST
                    .filter { historical -> historical.cityEntity == giza }
                    .map { it.toModel() }
            val luxorData =
                TEST_UPDATED_HISTORICAL_LIST
                    .filter { historical -> historical.cityEntity == luxor }
                    .map { it.toModel() }

            //WHEN
            updateHistoricalDataUseCase.execute()
            val cairoHistorical = weatherRepository.getHistoricalData(cairo.cityId).first()
            val gizaHistorical = weatherRepository.getHistoricalData(giza.cityId).first()
            val luxorHistorical = weatherRepository.getHistoricalData(luxor.cityId).first()

            //THEN
            assertThat(cairoHistorical).isEqualTo(cairoData)
            assertThat(gizaHistorical).isEqualTo(gizaData)
            assertThat(luxorHistorical).isEqualTo(luxorData)
        }

    @Test(expected = Exception::class)
    fun `when response is unsuccessful then exception will be thrown`() = runBlocking {
        //GIVEN
        weatherRepository.setSuccessful(false)

        //WHEN
        updateHistoricalDataUseCase.execute()

        //THEN Exception will be thrown
    }
}