package dev.alimansour.planradarassessment.presentation.historical

import androidx.lifecycle.*
import dev.alimansour.planradarassessment.data.mappers.HistoricalMapper
import dev.alimansour.planradarassessment.domain.model.HistoricalData
import dev.alimansour.planradarassessment.domain.repository.WeatherRepository
import dev.alimansour.planradarassessment.domain.usecase.city.GetHistoricalDataUseCase
import dev.alimansour.planradarassessment.util.Resource
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * WeatherApp Android Application developed by: Ali Mansour
 * ----------------- WeatherApp IS FREE SOFTWARE -------------------
 * https://www.alimansour.dev   |   mailto:dev.ali.mansour@gmail.com
 */
class HistoricalViewModel(
    private val getHistoricalDataUseCase: GetHistoricalDataUseCase
) : ViewModel() {

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
                            getHistoricalDataUseCase.execute(cityId)
                        )
                    )
                )
            }.onFailure { t ->
                val message = t.message ?: "Error on loading historical data"
                _historicalData.value = Resource.error(message, null, null)
            }
        }
    }
}

class HistoricalViewModelFactory @Inject constructor(private val getHistoricalDataUseCase: GetHistoricalDataUseCase) :
    ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoricalViewModel::class.java)) {
            return HistoricalViewModel(getHistoricalDataUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}