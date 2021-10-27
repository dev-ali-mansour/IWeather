package dev.alimansour.iweather.presentation.cities

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import dev.alimansour.iweather.MainCoroutineRule
import dev.alimansour.iweather.R
import dev.alimansour.iweather.TestUtil.aswan
import dev.alimansour.iweather.data.repository.FakeWeatherRepository
import dev.alimansour.iweather.domain.usecase.city.AddCityUseCase
import dev.alimansour.iweather.domain.usecase.city.DeleteCityUseCase
import dev.alimansour.iweather.domain.usecase.city.GetCitiesUseCase
import dev.alimansour.iweather.getOrAwaitValue
import dev.alimansour.iweather.presentation.MyApplication
import dev.alimansour.iweather.util.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
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
class CitiesViewModelTest {
    private lateinit var app: MyApplication
    private lateinit var weatherRepository: FakeWeatherRepository
    private lateinit var getCitiesUseCase: GetCitiesUseCase
    private lateinit var addCityUseCae: AddCityUseCase
    private lateinit var deleteCityUseCase: DeleteCityUseCase
    private lateinit var citiesViewModel: CitiesViewModel
    private val testDispatcher = TestCoroutineDispatcher()

    @get: Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get: Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setUp() {
        app = ApplicationProvider.getApplicationContext()
        app.isForTest = true
        weatherRepository = FakeWeatherRepository()
        getCitiesUseCase = GetCitiesUseCase(weatherRepository)
        addCityUseCae = AddCityUseCase(weatherRepository)
        deleteCityUseCase = DeleteCityUseCase(weatherRepository)
        citiesViewModel =
            CitiesViewModel(app, getCitiesUseCase, addCityUseCae, deleteCityUseCase, testDispatcher)
    }

    @Test
    fun addCity_withDisConnectedDeviceShowErrorMessage() =
        mainCoroutineRule.runBlockingTest {
            app.connectedForTest = false
            citiesViewModel.addCity(aswan.name)
            val result = citiesViewModel.citiesData.getOrAwaitValue()
            assertThat(result).isInstanceOf(Resource.Error::class.java)
            assertThat(result.message).isEqualTo(app.getString(R.string.device_not_connected))
        }

    @Test
    fun addCity_withConnectedDeviceShowLoadingWhileAddingCity_thenReturnListOfCities() =
        mainCoroutineRule.runBlockingTest {
            mainCoroutineRule.pauseDispatcher()
            citiesViewModel.addCity(aswan.name)
            assertThat(citiesViewModel.citiesData.getOrAwaitValue())
                .isInstanceOf(Resource.Loading::class.java)
            //delay(1000)
            mainCoroutineRule.resumeDispatcher()
            assertThat(citiesViewModel.citiesData.getOrAwaitValue())
                .isInstanceOf(Resource.Success::class.java)
        }

    @Test
    fun addCity_whenResponseIsNotSuccessful_thenShowErrorMessage() =
        mainCoroutineRule.runBlockingTest {
            weatherRepository.setSuccessful(false)
            citiesViewModel.addCity(aswan.name)
            assertThat(citiesViewModel.citiesData.getOrAwaitValue())
                .isInstanceOf(Resource.Loading::class.java)
            delay(1000)
            assertThat(citiesViewModel.citiesData.getOrAwaitValue())
                .isInstanceOf(Resource.Error::class.java)
            assertThat(citiesViewModel.citiesData.getOrAwaitValue().message)
                .isEqualTo("Failed to get results!")
        }
}