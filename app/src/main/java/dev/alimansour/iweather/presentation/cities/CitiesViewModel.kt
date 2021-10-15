package dev.alimansour.iweather.presentation.cities

import androidx.lifecycle.*
import dev.alimansour.iweather.data.mappers.CityMapper
import dev.alimansour.iweather.domain.model.CityData
import dev.alimansour.iweather.domain.usecase.city.AddCityUseCase
import dev.alimansour.iweather.domain.usecase.city.GetCitiesUseCase
import dev.alimansour.iweather.util.Resource
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * WeatherApp Android Application developed by: Ali Mansour
 * ----------------- WeatherApp IS FREE SOFTWARE -------------------
 * https://www.alimansour.dev   |   mailto:dev.ali.mansour@gmail.com
 */
class CitiesViewModel(
    private val getCitiesUseCase: GetCitiesUseCase,
    private val addCityUseCase: AddCityUseCase
) : ViewModel() {

    private val _citiesData = MutableLiveData<Resource<List<CityData>>>()
    val citiesData: LiveData<Resource<List<CityData>>>
        get() = _citiesData

    fun addCity(cityName: String) {
        viewModelScope.launch {
            addCityUseCase.execute(cityName)
            getCities()
        }
    }

    fun getCities() {
        _citiesData.value = Resource.loading(null)
        viewModelScope.launch {
            runCatching {
                _citiesData.postValue(
                    Resource.success(
                        CityMapper.mapFromEntity(getCitiesUseCase.execute())
                    )
                )
            }.onFailure { t ->
                val message = t.message ?: "Error on loading historical data"
                _citiesData.value = Resource.error(message, null, null)
            }
        }
    }
}

class CitiesViewModelFactory @Inject constructor(
    private val getCitiesUseCase: GetCitiesUseCase,
    private val addCityUseCase: AddCityUseCase
) :
    ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CitiesViewModel::class.java)) {
            return CitiesViewModel(getCitiesUseCase, addCityUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
