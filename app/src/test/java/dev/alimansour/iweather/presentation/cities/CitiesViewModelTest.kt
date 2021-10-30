package dev.alimansour.iweather.presentation.cities

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import dev.alimansour.iweather.R
import dev.alimansour.iweather.TestUtil.TEST_CITY_ENTITY_LIST
import dev.alimansour.iweather.TestUtil.aswan
import dev.alimansour.iweather.TestUtil.cairo
import dev.alimansour.iweather.TestUtil.giza
import dev.alimansour.iweather.TestUtil.luxor
import dev.alimansour.iweather.data.local.entity.toModel
import dev.alimansour.iweather.data.repository.FakeWeatherRepository
import dev.alimansour.iweather.domain.usecase.city.AddCityUseCase
import dev.alimansour.iweather.domain.usecase.city.DeleteCityUseCase
import dev.alimansour.iweather.domain.usecase.city.GetCitiesUseCase
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
    fun `getCities() When response is successful Then return list of saved cities`() = runBlocking {
        //GIVEN
        val savedCities = TEST_CITY_ENTITY_LIST.map { it.toModel() }

        //WHEN
        citiesViewModel.getCities()
        val result = citiesViewModel.citiesFlow.first()

        //THEN
        assertThat(result).isInstanceOf(Resource.Success::class.java)
        assertThat(result.data).isEqualTo(savedCities)
    }

    @Test
    fun `getCities() When response is un successful Then Resource Error is received`() =
        runBlocking {
            //GIVEN
            weatherRepository.setSuccessful(false)

            //WHEN
            citiesViewModel.getCities()
            val result = citiesViewModel.citiesFlow.first()

            //THEN
            assertThat(result).isInstanceOf(Resource.Error::class.java)
            assertThat(result.message).isEqualTo("Failed to get cities!")
        }

    @Test
    fun `addCity() When device is disconnected Then Resource Error is received`() = runBlocking {
        //GIVEN
        app.connectedForTest = false

        //WHEN
        citiesViewModel.addCity(aswan.name)
        val stateFlow = citiesViewModel.actionFlow.first()

        //THEN
        assertThat(stateFlow).isInstanceOf(Resource.Error::class.java)
        assertThat(stateFlow.message).isEqualTo(app.getString(R.string.device_not_connected))
    }

    @Test
    fun `addCity() response is un successful Then Resource Error is received`() = runBlocking {
        //GIVEN
        weatherRepository.setSuccessful(false)

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
            val savedCities = listOf(cairo, giza, luxor, aswan).map { it.toModel() }

            //WHEN
            citiesViewModel.addCity(aswan.name)
            val stateFlow = citiesViewModel.actionFlow.first()
            citiesViewModel.getCities()
            val citiesFlow = citiesViewModel.citiesFlow.first()

            //THEN
            assertThat(stateFlow).isInstanceOf(Resource.Success::class.java)
            assertThat(citiesFlow).isInstanceOf(Resource.Success::class.java)
            assertThat(citiesFlow.data).isEqualTo(savedCities)
        }

    @Test
    fun `deleteCity() response is un successful Then Resource Error is received`() = runBlocking {
        //GIVEN
        weatherRepository.setSuccessful(false)

        //WHEN
        citiesViewModel.deleteCity(cairo.toModel())
        val stateFlow = citiesViewModel.actionFlow.first()

        //THEN
        assertThat(stateFlow).isInstanceOf(Resource.Error::class.java)
        assertThat(stateFlow.message).isEqualTo("Failed to delete city!")
    }

    @Test
    fun `deleteCity() When response is successful Then return list of updated saved cities After delete`() =
        runBlocking {
            //GIVEN
            val savedCities = listOf(giza, luxor).map { it.toModel() }

            //WHEN
            citiesViewModel.deleteCity(cairo.toModel())
            val stateFlow = citiesViewModel.actionFlow.first()
            citiesViewModel.getCities()
            val citiesFlow = citiesViewModel.citiesFlow.first()

            //THEN
            assertThat(stateFlow).isInstanceOf(Resource.Success::class.java)
            assertThat(citiesFlow).isInstanceOf(Resource.Success::class.java)
            assertThat(citiesFlow.data).isEqualTo(savedCities)
        }
}