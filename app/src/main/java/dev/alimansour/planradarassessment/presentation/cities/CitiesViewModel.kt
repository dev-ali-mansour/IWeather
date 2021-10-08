package dev.alimansour.planradarassessment.presentation.cities

import androidx.lifecycle.*
import dev.alimansour.planradarassessment.data.mappers.CityMapper
import dev.alimansour.planradarassessment.domain.model.CityData
import dev.alimansour.planradarassessment.domain.repository.WeatherRepository
import dev.alimansour.planradarassessment.util.Resource
import kotlinx.coroutines.launch

/**
 * WeatherApp Android Application developed by: Ali Mansour
 * ----------------- WeatherApp IS FREE SOFTWARE -------------------
 * https://www.alimansour.dev   |   mailto:dev.ali.mansour@gmail.com
 */
class CitiesViewModel(private val repository: WeatherRepository) : ViewModel() {

    private val _citiesData = MutableLiveData<Resource<List<CityData>>>()
    val citiesData: LiveData<Resource<List<CityData>>>
        get() = _citiesData

    fun addCity(cityName: String) {
        viewModelScope.launch {
            repository.addCity(cityName)
            getCities()
        }
    }

    fun getCities() {
        _citiesData.value = Resource.loading(null)
        viewModelScope.launch {
            runCatching {
                _citiesData.postValue(
                    Resource.success(
                        CityMapper.mapFromEntity(repository.getCities())
                    )
                )
            }.onFailure { t ->
                val message = t.message ?: "Error on loading historical data"
                _citiesData.value = Resource.error(message, null, null)
            }
        }
    }
}

class CitiesViewModelFactory(private val repository: WeatherRepository) :
    ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CitiesViewModel::class.java)) {
            return CitiesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
