package dev.alimansour.iweather.presentation.historical

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import dev.alimansour.iweather.R
import dev.alimansour.iweather.TestUtil
import dev.alimansour.iweather.TestUtil.TEST_HISTORICAL_LIST
import dev.alimansour.iweather.TestUtil.TEST_UPDATED_HISTORICAL_LIST
import dev.alimansour.iweather.TestUtil.cairo
import dev.alimansour.iweather.TestUtil.giza
import dev.alimansour.iweather.TestUtil.luxor
import dev.alimansour.iweather.domain.repository.WeatherRepository
import dev.alimansour.iweather.domain.usecase.historical.GetHistoricalDataUseCase
import dev.alimansour.iweather.domain.usecase.historical.UpdateHistoricalDataUseCase
import dev.alimansour.iweather.presentation.MyApplication
import dev.alimansour.iweather.util.ConnectivityManager
import dev.alimansour.iweather.util.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
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
    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var weatherRepository: WeatherRepository
    private lateinit var getHistoricalDataUseCase: GetHistoricalDataUseCase
    private lateinit var updateHistoricalDataUseCase: UpdateHistoricalDataUseCase
    private lateinit var historicalViewModel: HistoricalViewModel
    private val testDispatcher = TestCoroutineDispatcher()

    @get: Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        app = ApplicationProvider.getApplicationContext()
        connectivityManager = Mockito.mock(ConnectivityManager::class.java)
        weatherRepository = Mockito.mock(WeatherRepository::class.java)
        getHistoricalDataUseCase = GetHistoricalDataUseCase(weatherRepository)
        updateHistoricalDataUseCase = UpdateHistoricalDataUseCase(weatherRepository)
        historicalViewModel =
            HistoricalViewModel(
                app,
                connectivityManager,
                getHistoricalDataUseCase,
                updateHistoricalDataUseCase,
                testDispatcher
            )
    }

    @Test
    fun `getHistorical() When response is successful Then return list of historical data`() =
        runBlocking {
            //GIVEN
            val data = TEST_HISTORICAL_LIST.filter { historical -> historical.city == cairo }
            Mockito.`when`(weatherRepository.getHistoricalData(cairo.id))
                .thenReturn(flow { emit(data) })

            //WHEN
            historicalViewModel.getHistorical(cairo.id)
            val result = historicalViewModel.historicalFlow.first()

            //THEN
            assertThat(result).isInstanceOf(Resource.Success::class.java)
            assertThat(result.data).isEqualTo(data)
        }

    @Test
    fun `getHistorical() When response is un successful Then Resource Error is received`() =
        runBlocking {
            //GIVEN
            Mockito.`when`(weatherRepository.getHistoricalData(cairo.id))
                .then { throw Exception("Failed to get historical!") }

            //WHEN
            historicalViewModel.getHistorical(cairo.id)
            val result = historicalViewModel.historicalFlow.first()

            //THEN
            assertThat(result).isInstanceOf(Resource.Error::class.java)
            assertThat(result.message).isEqualTo("Failed to get historical!")
        }

    @Test
    fun `updateHistoricalData() When device is disconnected Then Resource Error is received`() =
        runBlocking {
            //GIVEN
            Mockito.`when`(connectivityManager.isConnected()).thenReturn(false)

            //WHEN
            historicalViewModel.updateHistoricalData()
            val result = historicalViewModel.actionFlow.first()

            //THEN
            assertThat(result).isInstanceOf(Resource.Error::class.java)
            assertThat(result.message).isEqualTo(app.getString(R.string.device_not_connected))
        }

    @Test
    fun `updateHistoricalData() When device is connected and response is un successful Then Resource Error is received`() =
        runBlocking {
            //GIVEN
            Mockito.`when`(connectivityManager.isConnected()).thenReturn(true)
            Mockito.`when`(weatherRepository.updateHistoricalData()).then {
                throw  Exception("Failed to update historical!")
            }

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
            val list = TEST_HISTORICAL_LIST.toMutableList()
            val cairoData = list.filter { historical -> historical.city == cairo }
            val gizaData = list.filter { historical -> historical.city == giza }
            val luxorData = list.filter { historical -> historical.city == luxor }
            val aswanData = list.filter { historical -> historical.city == TestUtil.aswan }

            Mockito.`when`(connectivityManager.isConnected()).thenReturn(true)
            Mockito.`when`(weatherRepository.updateHistoricalData()).then {
                list.clear()
                list.addAll(TEST_UPDATED_HISTORICAL_LIST)
            }
            Mockito.`when`(weatherRepository.getHistoricalData(cairo.id))
                .thenReturn(flow { emit(cairoData) })
            Mockito.`when`(weatherRepository.getHistoricalData(giza.id))
                .thenReturn(flow { emit(gizaData) })
            Mockito.`when`(weatherRepository.getHistoricalData(luxor.id))
                .thenReturn(flow { emit(luxorData) })
            Mockito.`when`(weatherRepository.getHistoricalData(TestUtil.aswan.id))
                .thenReturn(flow { emit(aswanData) })

            //WHEN
            historicalViewModel.updateHistoricalData()
            val stateFlow = historicalViewModel.actionFlow.first()
            historicalViewModel.getHistorical(cairo.id)
            val cairoFlow = historicalViewModel.historicalFlow.first()
            historicalViewModel.getHistorical(giza.id)
            val gizaFlow = historicalViewModel.historicalFlow.first()
            historicalViewModel.getHistorical(luxor.id)
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