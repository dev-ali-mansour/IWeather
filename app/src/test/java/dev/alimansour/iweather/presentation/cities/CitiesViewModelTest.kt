package dev.alimansour.iweather.presentation.cities

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import dev.alimansour.iweather.R
import dev.alimansour.iweather.TestUtil.TEST_CITY_LIST
import dev.alimansour.iweather.TestUtil.aswan
import dev.alimansour.iweather.TestUtil.cairo
import dev.alimansour.iweather.domain.model.City
import dev.alimansour.iweather.domain.repository.WeatherRepository
import dev.alimansour.iweather.domain.usecase.city.AddCityUseCase
import dev.alimansour.iweather.domain.usecase.city.DeleteCityUseCase
import dev.alimansour.iweather.domain.usecase.city.GetCitiesUseCase
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
import org.mockito.junit.MockitoJUnitRunner

/**
 * WeatherApp Android Application developed by: Ali Mansour
 * ----------------- WeatherApp IS FREE SOFTWARE -------------------
 * https://www.alimansour.dev   |   mailto:dev.ali.mansour@gmail.com
 */
@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
//@Config(sdk = [Build.VERSION_CODES.Q])
class CitiesViewModelTest {
    private lateinit var app: Application
    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var weatherRepository: WeatherRepository
    private lateinit var getCitiesUseCase: GetCitiesUseCase
    private lateinit var addCityUseCae: AddCityUseCase
    private lateinit var deleteCityUseCase: DeleteCityUseCase
    private lateinit var citiesViewModel: CitiesViewModel
    private val testDispatcher = TestCoroutineDispatcher()

    @get: Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        app = Mockito.mock(Application::class.java)
        connectivityManager = Mockito.mock(ConnectivityManager::class.java)
        weatherRepository = Mockito.mock(WeatherRepository::class.java)
        getCitiesUseCase = GetCitiesUseCase(weatherRepository)
        addCityUseCae = AddCityUseCase(weatherRepository)
        deleteCityUseCase = DeleteCityUseCase(weatherRepository)
        citiesViewModel =
            CitiesViewModel(
                app,
                connectivityManager,
                getCitiesUseCase,
                addCityUseCae,
                deleteCityUseCase,
                testDispatcher
            )
    }

    @Test
    fun `addCity() When device is disconnected Then Resource Error is received`() = runBlocking {
        //GIVEN
        Mockito.`when`(connectivityManager.isConnected()).thenReturn(false)
        Mockito.`when`(app.getString(R.string.device_not_connected))
            .thenReturn("Oops! You are not connected to the internet.")

        //WHEN
        citiesViewModel.addCity(aswan.name)
        val stateFlow = citiesViewModel.actionFlow.first()

        //THEN
        assertThat(stateFlow).isInstanceOf(Resource.Error::class.java)
        assertThat(stateFlow.message).isEqualTo(app.getString(R.string.device_not_connected))
    }

    @Test
    fun `addCity() When device is connected and response is un successful Then Resource Error is received`() =
        runBlocking {
            //GIVEN
            Mockito.`when`(connectivityManager.isConnected()).thenReturn(true)
            Mockito.`when`(weatherRepository.addCity(aswan.name))
                .then { throw Exception("Failed to add city!") }

            //WHEN
            citiesViewModel.addCity(aswan.name)
            val stateFlow = citiesViewModel.actionFlow.first()

            //THEN
            assertThat(stateFlow).isInstanceOf(Resource.Error::class.java)
            assertThat(stateFlow.message).isEqualTo("Failed to add city!")
        }

    @Test
    fun `addCity() When device is connected and response is successful Then return list of new saved cities`() =
        runBlocking {
            //GIVEN
            Mockito.`when`(connectivityManager.isConnected()).thenReturn(true)
            val list = TEST_CITY_LIST.toMutableList()
            Mockito.`when`(weatherRepository.addCity(aswan.name)).then {
                list.add(City(aswan.id, aswan.name, aswan.country))
            }
            Mockito.`when`(weatherRepository.getCities()).thenReturn(flow { emit(list) })

            //WHEN
            citiesViewModel.addCity(aswan.name)
            val stateFlow = citiesViewModel.actionFlow.first()
            citiesViewModel.getCities()
            val citiesFlow = citiesViewModel.citiesFlow.first()

            //THEN
            assertThat(stateFlow).isInstanceOf(Resource.Success::class.java)
            assertThat(citiesFlow).isInstanceOf(Resource.Success::class.java)
            assertThat(citiesFlow.data).isEqualTo(list)
        }

    @Test
    fun `deleteCity() response is un successful Then Resource Error is received`() = runBlocking {
        //GIVEN
        Mockito.`when`(weatherRepository.deleteCity(cairo))
            .then { throw Exception("Failed to delete city!") }

        //WHEN
        citiesViewModel.deleteCity(cairo)
        val stateFlow = citiesViewModel.actionFlow.first()

        //THEN
        assertThat(stateFlow).isInstanceOf(Resource.Error::class.java)
        assertThat(stateFlow.message).isEqualTo("Failed to delete city!")
    }

    @Test
    fun `deleteCity() When response is successful Then return list of updated saved cities After delete`() =
        runBlocking {
            //GIVEN
            val list = TEST_CITY_LIST.toMutableList()
            Mockito.`when`(weatherRepository.deleteCity(cairo)).then {
                list.remove(cairo)
            }
            Mockito.`when`(weatherRepository.getCities()).thenReturn(flow { emit(list) })

            //WHEN
            citiesViewModel.deleteCity(cairo)
            val stateFlow = citiesViewModel.actionFlow.first()
            citiesViewModel.getCities()
            val citiesFlow = citiesViewModel.citiesFlow.first()

            //THEN
            assertThat(stateFlow).isInstanceOf(Resource.Success::class.java)
            assertThat(citiesFlow).isInstanceOf(Resource.Success::class.java)
            assertThat(citiesFlow.data).isEqualTo(list)
        }

    @Test
    fun `getCities() When response is successful Then return list of saved cities`() = runBlocking {
        //GIVEN
        Mockito.`when`(weatherRepository.getCities()).thenReturn(flow {
            emit(TEST_CITY_LIST)
        })

        //WHEN
        citiesViewModel.getCities()
        val result = citiesViewModel.citiesFlow.first()

        //THEN
        assertThat(result).isInstanceOf(Resource.Success::class.java)
        assertThat(result.data).isEqualTo(TEST_CITY_LIST)
    }

    @Test
    fun `getCities() When response is un successful Then Resource Error is received`() =
        runBlocking {
            //GIVEN
            Mockito.`when`(weatherRepository.getCities())
                .then { throw Exception("Failed to get cities!") }

            //WHEN
            citiesViewModel.getCities()
            val result = citiesViewModel.citiesFlow.first()

            //THEN
            assertThat(result).isInstanceOf(Resource.Error::class.java)
            assertThat(result.message).isEqualTo("Failed to get cities!")
        }
}