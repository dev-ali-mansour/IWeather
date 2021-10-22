package dev.alimansour.iweather.domain.usecase.historical

import com.google.common.truth.Truth
import dev.alimansour.iweather.*
import dev.alimansour.iweather.data.repository.FakeWeatherRepository
import dev.alimansour.iweather.domain.repository.WeatherRepository
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

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
        weatherRepository = FakeWeatherRepository()
        updateHistoricalDataUseCase = UpdateHistoricalDataUseCase(weatherRepository)
    }

    @Test
    fun execute_returnTheRightListOfUpdatedHistoricalData() = runBlocking {
        updateHistoricalDataUseCase.execute()
        val cairoHistorical = weatherRepository.getHistoricalData(cairo.cityId)
        val cairoData =
            TEST_UPDATED_HISTORICAL_LIST.filter { historical -> historical.city == cairo }
        val gizaHistorical = weatherRepository.getHistoricalData(giza.cityId)
        val gizaData =
            TEST_UPDATED_HISTORICAL_LIST.filter { historical -> historical.city == giza }
        val luxorHistorical = weatherRepository.getHistoricalData(luxor.cityId)
        val luxorData =
            TEST_UPDATED_HISTORICAL_LIST.filter { historical -> historical.city == luxor }
        Truth.assertThat(cairoHistorical).isEqualTo(cairoData)
        Truth.assertThat(gizaHistorical).isEqualTo(gizaData)
        Truth.assertThat(luxorHistorical).isEqualTo(luxorData)
    }
}