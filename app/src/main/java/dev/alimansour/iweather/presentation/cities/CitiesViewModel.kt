package dev.alimansour.iweather.presentation.cities

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dev.alimansour.iweather.R
import dev.alimansour.iweather.domain.model.City
import dev.alimansour.iweather.domain.usecase.city.AddCityUseCase
import dev.alimansour.iweather.domain.usecase.city.DeleteCityUseCase
import dev.alimansour.iweather.domain.usecase.city.GetCitiesUseCase
import dev.alimansour.iweather.util.ActionState
import dev.alimansour.iweather.util.Resource
import dev.alimansour.iweather.util.isConnected
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

/**
 * WeatherApp Android Application developed by: Ali Mansour
 * ----------------- WeatherApp IS FREE SOFTWARE -------------------
 * https://www.alimansour.dev   |   mailto:dev.ali.mansour@gmail.com
 */
class CitiesViewModel(
    private val app: Application,
    private val getCitiesUseCase: GetCitiesUseCase,
    private val addCityUseCase: AddCityUseCase,
    private val deleteCityUseCase: DeleteCityUseCase,
    private val dispatcher: CoroutineDispatcher
) : AndroidViewModel(app) {
    private val _citiesFlow = MutableStateFlow<Resource<List<City>>>(Resource.Loading())
    val citiesFlow: StateFlow<Resource<List<City>>> = _citiesFlow

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, e ->
        _citiesFlow.value = Resource.Error(e.message.toString())
    }
    private val _actionFlow =
        MutableStateFlow<Resource<ActionState>>(Resource.Loading())
    val actionFlow: StateFlow<Resource<ActionState>> = _actionFlow

    private val coroutineActionExceptionHandler = CoroutineExceptionHandler { _, e ->
        _actionFlow.value = Resource.Error(e.message.toString())
    }

    fun addCity(cityName: String) =
        viewModelScope.launch(dispatcher + coroutineActionExceptionHandler) {
            if (app.isConnected()) {
                runCatching {
                    addCityUseCase.execute(cityName)
                    _actionFlow.value = Resource.Success(ActionState.Add)
                }.onFailure { _actionFlow.value = Resource.Error(it.message.toString()) }
            } else _actionFlow.value = Resource.Error(app.getString(R.string.device_not_connected))
        }

    fun getCities() = viewModelScope.launch(dispatcher + coroutineExceptionHandler) {
        _citiesFlow.value = Resource.Loading()
        getCitiesUseCase.execute()
            .catch {
                _citiesFlow.value = Resource.Error(it.message.toString())
            }.collect { list ->
                _citiesFlow.value = Resource.Success(list)
            }
    }

    fun deleteCity(city: City) =
        viewModelScope.launch(dispatcher + coroutineActionExceptionHandler) {
            runCatching {
                deleteCityUseCase.execute(city)
                _actionFlow.value = Resource.Success(ActionState.DELETE)
            }.onFailure { _actionFlow.value = Resource.Error(it.message.toString()) }
        }
}

@Singleton
class CitiesViewModelFactory @Inject constructor(
    private val app: Application,
    private val getCitiesUseCase: GetCitiesUseCase,
    private val addCityUseCase: AddCityUseCase,
    private val deleteCityUseCase: DeleteCityUseCase,
    private val dispatcher: CoroutineDispatcher
) :
    ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CitiesViewModel::class.java)) {
            return CitiesViewModel(
                app,
                getCitiesUseCase,
                addCityUseCase,
                deleteCityUseCase,
                dispatcher
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}