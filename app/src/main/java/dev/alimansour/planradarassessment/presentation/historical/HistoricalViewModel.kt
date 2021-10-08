package dev.alimansour.planradarassessment.presentation.historical

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.alimansour.planradarassessment.data.remote.RemoteDataSource
import dev.alimansour.planradarassessment.data.remote.RemoteDataSourceImpl
import dev.alimansour.planradarassessment.data.remote.WeatherApi
import dev.alimansour.planradarassessment.domain.model.CityData
import dev.alimansour.planradarassessment.domain.model.HistoricalData
import dev.alimansour.planradarassessment.util.Resource
import kotlinx.coroutines.launch

/**
 * WeatherApp Android Application developed by: Ali Mansour
 * ----------------- WeatherApp IS FREE SOFTWARE -------------------
 * https://www.alimansour.dev   |   mailto:dev.ali.mansour@gmail.com
 */
class HistoricalViewModel : ViewModel() {
    private val remoteDataSource: RemoteDataSource =
        RemoteDataSourceImpl(WeatherApi.retrofitService)

    private val _historicalData = MutableLiveData<Resource<List<HistoricalData?>>>()
    val historicalData: LiveData<Resource<List<HistoricalData?>>>
        get() = _historicalData

    fun getHistoricalData(cityName: String) {
        viewModelScope.launch {
            _historicalData.value = Resource.loading(null)
            runCatching {
                val dataList = ArrayList<HistoricalData?>()
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

                _historicalData.postValue(Resource.success(dataList.toList()))

            }.onFailure { t ->
                val message = t.message ?: "Error on loading historical data"
                _historicalData.value = Resource.error(message, null, null)
            }
        }
    }
}
