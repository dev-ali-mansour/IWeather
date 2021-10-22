package dev.alimansour.iweather.domain.usecase.historical

import com.google.common.truth.Truth
import dev.alimansour.iweather.TEST_CITY_LIST
import dev.alimansour.iweather.TEST_HISTORICAL_LIST
import dev.alimansour.iweather.cairo
import dev.alimansour.iweather.data.repository.FakeWeatherRepository
import dev.alimansour.iweather.domain.repository.WeatherRepository
import dev.alimansour.iweather.giza
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

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
        weatherRepository = FakeWeatherRepository()
        getHistoricalDataUseCase = GetHistoricalDataUseCase(weatherRepository)
    }

    @Test
    fun execute_returnTheRightListOfHistoricalData() = runBlocking {
        val historicalData = getHistoricalDataUseCase.execute(cairo.cityId)
        val data =
            TEST_HISTORICAL_LIST.filter { historical -> historical.city == cairo }
        Truth.assertThat(historicalData).isEqualTo(data)
    }
}