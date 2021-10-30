package dev.alimansour.iweather.presentation.historical

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import dev.alimansour.iweather.R
import dev.alimansour.iweather.TestUtil
import dev.alimansour.iweather.TestUtil.cairo
import dev.alimansour.iweather.TestUtil.giza
import dev.alimansour.iweather.TestUtil.luxor
import dev.alimansour.iweather.data.local.entity.toModel
import dev.alimansour.iweather.data.repository.FakeWeatherRepository
import dev.alimansour.iweather.domain.usecase.historical.GetHistoricalDataUseCase
import dev.alimansour.iweather.domain.usecase.historical.UpdateHistoricalDataUseCase
import dev.alimansour.iweather.presentation.MyApplication
import dev.alimansour.iweather.util.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

/**
 * WeatherApp Android Application developed by: Ali Mansour
 * ----------------- WeatherApp IS FREE SOFTWARE -------------------
 * https://www.alimansour.dev   |   mailto:dev.ali.mansour@gmail.com
 */
@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
@Config(sdk = [Build.VERSION_CODES.Q])
class HistoricalViewModelTest {
    private lateinit var app: MyApplication
    private lateinit var weatherRepository: FakeWeatherRepository
    private lateinit var getHistoricalDataUseCase: GetHistoricalDataUseCase
    private lateinit var updateHistoricalDataUseCase: UpdateHistoricalDataUseCase
    private lateinit var historicalViewModel: HistoricalViewModel
    private val testDispatcher = TestCoroutineDispatcher()

    @get: Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        app = ApplicationProvider.getApplicationContext()
        app.isForTest = true
        weatherRepository = FakeWeatherRepository()
        getHistoricalDataUseCase = GetHistoricalDataUseCase(weatherRepository)
        updateHistoricalDataUseCase = UpdateHistoricalDataUseCase(weatherRepository)
        historicalViewModel =
            HistoricalViewModel(
                app, getHistoricalDataUseCase, updateHistoricalDataUseCase, testDispatcher
            )
    }

    @Test
    fun `getHistorical() When response is successful Then return list of historical data`() =
        runBlocking {
            //GIVEN
            val cachedHistorical =
                TestUtil.TEST_HISTORICAL_LIST
                    .filter { historical -> historical.cityEntity == cairo }
                    .map { it.toModel() }

            //WHEN
            historicalViewModel.getHistorical(cairo.cityId)
            val result = historicalViewModel.historicalFlow.first()

            //THEN
            assertThat(result).isInstanceOf(Resource.Success::class.java)
            assertThat(result.data).isEqualTo(cachedHistorical)
        }

    @Test
    fun `getHistorical() When response is un successful Then Resource Error is received`() =
        runBlocking {
            //GIVEN
            weatherRepository.setSuccessful(false)

            //WHEN
            historicalViewModel.getHistorical(cairo.cityId)
            val result = historicalViewModel.historicalFlow.first()

            //THEN
            assertThat(result).isInstanceOf(Resource.Error::class.java)
            assertThat(result.message).isEqualTo("Failed to get historical!")
        }

    @Test
    fun `updateHistoricalData() When device is disconnected Then Resource Error is received`() =
        runBlocking {
            //GIVEN
            app.connectedForTest = false

            //WHEN
            historicalViewModel.updateHistoricalData()
            val result = historicalViewModel.actionFlow.first()

            //THEN
            assertThat(result).isInstanceOf(Resource.Error::class.java)
            assertThat(result.message).isEqualTo(app.getString(R.string.device_not_connected))
        }

    @Test
    fun `updateHistoricalData() response is un successful Then Resource Error is received`() =
        runBlocking {
            //GIVEN
            weatherRepository.setSuccessful(false)

            //WHEN
            historicalViewModel.updateHistoricalData()
            val result = historicalViewModel.actionFlow.first()

            //THEN
            assertThat(result).isInstanceOf(Resource.Error::class.java)
            assertThat(result.message).isEqualTo("Failed to update historical!")
        }

    @Test
    fun `updateHistoricalData() When device is connected and response is successful Then return list of new cached historical data`() =
        runBlocking {
            //GIVEN
            val cairoData =
                TestUtil.TEST_UPDATED_HISTORICAL_LIST
                    .filter { historical -> historical.cityEntity == cairo }
                    .map { it.toModel() }
            val gizaData =
                TestUtil.TEST_UPDATED_HISTORICAL_LIST
                    .filter { historical -> historical.cityEntity == giza }
                    .map { it.toModel() }

            val luxorData =
                TestUtil.TEST_UPDATED_HISTORICAL_LIST
                    .filter { historical -> historical.cityEntity == luxor }
                    .map { it.toModel() }

            //WHEN
            historicalViewModel.updateHistoricalData()
            val stateFlow = historicalViewModel.actionFlow.first()
            historicalViewModel.getHistorical(cairo.cityId)
            val cairoFlow = historicalViewModel.historicalFlow.first()
            historicalViewModel.getHistorical(giza.cityId)
            val gizaFlow = historicalViewModel.historicalFlow.first()
            historicalViewModel.getHistorical(luxor.cityId)
            val luxorFlow = historicalViewModel.historicalFlow.first()

            //THEN
            assertThat(stateFlow).isInstanceOf(Resource.Success::class.java)
            assertThat(cairoFlow).isInstanceOf(Resource.Success::class.java)
            assertThat(gizaFlow).isInstanceOf(Resource.Success::class.java)
            assertThat(luxorFlow).isInstanceOf(Resource.Success::class.java)
            assertThat(cairoFlow.data).isEqualTo(cairoData)
            assertThat(gizaFlow.data).isEqualTo(gizaData)
            assertThat(luxorFlow.data).isEqualTo(luxorData)
        }
}