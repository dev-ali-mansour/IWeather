package dev.alimansour.iweather.presentation.historical

import android.app.Application
import androidx.lifecycle.*
import dev.alimansour.iweather.R
import dev.alimansour.iweather.data.mappers.HistoricalMapper
import dev.alimansour.iweather.domain.model.HistoricalData
import dev.alimansour.iweather.domain.usecase.historical.GetHistoricalDataUseCase
import dev.alimansour.iweather.domain.usecase.historical.UpdateHistoricalDataUseCase
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
class HistoricalViewModel(
    private val app: Application,
    private val getHistoricalDataUseCase: GetHistoricalDataUseCase,
    private val updateHistoricalDataUseCase: UpdateHistoricalDataUseCase,
    private val historicalMapper: HistoricalMapper
) : AndroidViewModel(app) {

    private val _historicalData = MutableLiveData<Resource<List<HistoricalData>>>()
    val historicalData: LiveData<Resource<List<HistoricalData>>>
        get() = _historicalData

    private val _historicalDataUpdated = MutableLiveData<Resource<Boolean>>()
    val historicalDataUpdated: LiveData<Resource<Boolean>>
        get() = _historicalDataUpdated

    /**
     * Get list of historical data for a city
     * @param cityId City Id
     */
    fun getHistoricalDataList(cityId: Int) = viewModelScope.launch(Dispatchers.IO) {
        _historicalData.postValue(Resource.Loading())
        runCatching {
            _historicalData.postValue(
                Resource.Success(
                    historicalMapper.mapFromEntity(
                        getHistoricalDataUseCase.execute(cityId)
                    )
                )
            )
        }.onFailure { t ->
            _historicalData.postValue(Resource.Error(t.message.toString()))
        }
    }

    fun updateHistoricalData() = viewModelScope.launch(Dispatchers.IO) {
        if (app.isConnected()) {
            _historicalDataUpdated.postValue(Resource.Loading())
            runCatching {
                updateHistoricalDataUseCase.execute()
                _historicalDataUpdated.postValue(Resource.Success(true))
            }.onFailure { t ->
                _historicalDataUpdated.postValue(Resource.Error(t.message.toString()))
            }
        } else {
            _historicalDataUpdated.postValue(Resource.Error(app.getString(R.string.device_not_connected)))

        }
    }
}

@Singleton
class HistoricalViewModelFactory @Inject constructor(
    private val app: Application,
    private val getHistoricalDataUseCase: GetHistoricalDataUseCase,
    private val updateHistoricalDataUseCase: UpdateHistoricalDataUseCase,
    private val historicalMapper: HistoricalMapper
) :
    ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoricalViewModel::class.java)) {
            return HistoricalViewModel(
                app,
                getHistoricalDataUseCase,
                updateHistoricalDataUseCase,
                historicalMapper
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}