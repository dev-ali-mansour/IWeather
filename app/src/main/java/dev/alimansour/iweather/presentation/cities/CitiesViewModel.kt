package dev.alimansour.iweather.presentation.cities

import android.app.Application
import androidx.lifecycle.*
import dev.alimansour.iweather.R
import dev.alimansour.iweather.data.local.entity.toModel
import dev.alimansour.iweather.domain.model.CityData
import dev.alimansour.iweather.domain.usecase.city.AddCityUseCase
import dev.alimansour.iweather.domain.usecase.city.DeleteCityUseCase
import dev.alimansour.iweather.domain.usecase.city.GetCitiesUseCase
import dev.alimansour.iweather.util.Resource
import dev.alimansour.iweather.util.isConnected
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
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
    private val _citiesData = MutableLiveData<Resource<List<CityData>>>()
    val citiesData: LiveData<Resource<List<CityData>>>
        get() = _citiesData

    fun addCity(cityName: String) = viewModelScope.launch(dispatcher) {
        if (app.isConnected()) {
            _citiesData.postValue(Resource.Loading())
            runCatching {
                val cities = addCityUseCase.execute(cityName).map { it.toModel() }
                _citiesData.postValue(Resource.Success(cities))
            }.onFailure { t ->
                _citiesData.postValue(Resource.Error(t.message.toString()))
            }
        } else {
            _citiesData.postValue(Resource.Error(app.getString(R.string.device_not_connected)))
        }
    }

    fun getCities() = viewModelScope.launch(Dispatchers.IO) {
        _citiesData.postValue(Resource.Loading())

        runCatching {
            _citiesData.postValue(Resource.Success(getCitiesUseCase.execute().map { it.toModel() }))
        }.onFailure { t ->
            _citiesData.postValue(Resource.Error(t.message.toString()))
        }
    }

    fun deleteCity(city: CityData) = viewModelScope.launch(Dispatchers.IO) {
        _citiesData.postValue(Resource.Loading())

        runCatching {
            _citiesData.postValue(
                Resource.Success(deleteCityUseCase.execute(city).map { it.toModel() })
            )
        }.onFailure { t ->
            _citiesData.postValue(Resource.Error(t.message.toString()))
        }
        deleteCityUseCase.execute(city)
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
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
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
