package dev.alimansour.iweather.presentation.historical

import androidx.lifecycle.*
import dev.alimansour.iweather.data.mappers.HistoricalMapper
import dev.alimansour.iweather.domain.model.HistoricalData
import dev.alimansour.iweather.domain.usecase.city.GetHistoricalDataUseCase
import dev.alimansour.iweather.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

/**
 * WeatherApp Android Application developed by: Ali Mansour
 * ----------------- WeatherApp IS FREE SOFTWARE -------------------
 * https://www.alimansour.dev   |   mailto:dev.ali.mansour@gmail.com
 */
class HistoricalViewModel(
    private val getHistoricalDataUseCase: GetHistoricalDataUseCase,
    private val historicalMapper: HistoricalMapper
) : ViewModel() {

    private val _historicalData = MutableLiveData<Resource<List<HistoricalData>>>()
    val historicalData: LiveData<Resource<List<HistoricalData>>>
        get() = _historicalData

    /**
     * Get list of historical data for a city
     * @param cityId City Id
     */
    fun getHistoricalDataList(cityId: Int) = viewModelScope.launch(Dispatchers.IO) {
        _historicalData.postValue(Resource.Loading(null))

        runCatching {
            _historicalData.postValue(
                Resource.Success(
                    historicalMapper.mapFromEntity(
                        getHistoricalDataUseCase.execute(cityId)
                    )
                )
            )
        }.onFailure { t ->
            _historicalData.value = Resource.Error(t.message.toString(), null)
        }
    }
}

@Singleton
class HistoricalViewModelFactory @Inject constructor(
    private val getHistoricalDataUseCase: GetHistoricalDataUseCase,
    private val historicalMapper: HistoricalMapper
) :
    ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoricalViewModel::class.java)) {
            return HistoricalViewModel(getHistoricalDataUseCase, historicalMapper) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}