package dev.alimansour.planradarassessment.presentation.historical

import androidx.lifecycle.*
import dev.alimansour.planradarassessment.data.mappers.HistoricalMapper
import dev.alimansour.planradarassessment.domain.model.HistoricalData
import dev.alimansour.planradarassessment.domain.repository.WeatherRepository
import dev.alimansour.planradarassessment.util.Resource
import kotlinx.coroutines.launch

/**
 * WeatherApp Android Application developed by: Ali Mansour
 * ----------------- WeatherApp IS FREE SOFTWARE -------------------
 * https://www.alimansour.dev   |   mailto:dev.ali.mansour@gmail.com
 */
class HistoricalViewModel(private val repository: WeatherRepository) : ViewModel() {

    private val _historicalData = MutableLiveData<Resource<List<HistoricalData>>>()
    val historicalData: LiveData<Resource<List<HistoricalData>>>
        get() = _historicalData

    /**
     * Get list of historical data for a city
     * @param cityId City Id
     */
    fun getHistoricalDataList(cityId: Int) {
        viewModelScope.launch {
            _historicalData.value = Resource.loading(null)
            runCatching {
                _historicalData.postValue(
                    Resource.success(
                        HistoricalMapper.mapFromEntity(
                            repository.getHistoricalData(
                                cityId
                            )
                        )
                    )
                )
                /* val dataList = ArrayList<HistoricalData?>()
                 val resource = remoteDataSource.fetchHistoricalData(cityName)
                 resource.data?.let { response ->
                     val name = "${response.city.name}, ${response.city.country}"
                     response.list?.let { list ->
                         list.forEach { item ->
                             dataList.add(
                                 HistoricalData(
                                     1,
                                     CityData(
                                         response.city.id,
                                         response.city.name,
                                         response.city.country
                                     ),
                                     "\uF6C4",
                                     item.date,
                                     item.weather[0].description,
                                     item.main.temp,
                                     item.main.humidity,
                                     item.wind.speed
                                 )
                             )
                         }
                     }
                 }

                 _historicalData.postValue(Resource.success(dataList.toList()))*/

            }.onFailure { t ->
                val message = t.message ?: "Error on loading historical data"
                _historicalData.value = Resource.error(message, null, null)
            }
        }
    }
}

class HistoricalViewModelFactory(private val repository: WeatherRepository) :
    ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoricalViewModel::class.java)) {
            return HistoricalViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}