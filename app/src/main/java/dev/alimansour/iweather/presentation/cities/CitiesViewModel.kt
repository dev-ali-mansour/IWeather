package dev.alimansour.iweather.presentation.cities

import android.app.Application
import androidx.lifecycle.*
import dev.alimansour.iweather.data.mappers.CityMapper
import dev.alimansour.iweather.domain.model.CityData
import dev.alimansour.iweather.domain.usecase.city.AddCityUseCase
import dev.alimansour.iweather.domain.usecase.city.GetCitiesUseCase
import dev.alimansour.iweather.util.Resource
import dev.alimansour.iweather.util.isConnected
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
    private val cityMapper: CityMapper
) : AndroidViewModel(app) {
    private val _isCityAdded = MutableLiveData<Resource<Boolean>>()
    val isCityAdded: LiveData<Resource<Boolean>>
        get() = _isCityAdded
    private val _citiesData = MutableLiveData<Resource<List<CityData>>>()
    val citiesData: LiveData<Resource<List<CityData>>>
        get() = _citiesData

    fun addCity(cityName: String) = viewModelScope.launch(Dispatchers.IO) {
        if (app.isConnected()) {
            _isCityAdded.postValue(Resource.Loading(null))

            runCatching {
                addCityUseCase.execute(cityName)
                _isCityAdded.postValue(Resource.Success(true))
            }.onFailure { t ->
                _isCityAdded.postValue(Resource.Error(t.message.toString(), null))
            }

        } else {
            _isCityAdded.postValue(Resource.Error("Oops! You are not connected to the internet."))
        }
    }

    fun getCities() = viewModelScope.launch(Dispatchers.IO) {
        _citiesData.postValue(Resource.Loading(null))

        runCatching {
            _citiesData.postValue(
                Resource.Success(
                    cityMapper.mapFromEntity(getCitiesUseCase.execute())
                )
            )
        }.onFailure { t ->
            _citiesData.postValue(Resource.Error(t.message.toString(), null))
        }
    }
}

@Singleton
class CitiesViewModelFactory @Inject constructor(
    private val app: Application,
    private val getCitiesUseCase: GetCitiesUseCase,
    private val addCityUseCase: AddCityUseCase,
    private val cityMapper: CityMapper
) :
    ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CitiesViewModel::class.java)) {
            return CitiesViewModel(app, getCitiesUseCase, addCityUseCase, cityMapper) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
