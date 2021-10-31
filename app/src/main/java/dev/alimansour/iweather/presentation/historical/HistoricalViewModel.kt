package dev.alimansour.iweather.presentation.historical

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dev.alimansour.iweather.R
import dev.alimansour.iweather.domain.model.Historical
import dev.alimansour.iweather.domain.usecase.historical.GetHistoricalDataUseCase
import dev.alimansour.iweather.domain.usecase.historical.UpdateHistoricalDataUseCase
import dev.alimansour.iweather.util.ActionState
import dev.alimansour.iweather.util.ConnectivityManager
import dev.alimansour.iweather.util.Resource
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
class HistoricalViewModel(
    private val app: Application,
    private val connectivityManager: ConnectivityManager,
    private val getHistoricalDataUseCase: GetHistoricalDataUseCase,
    private val updateHistoricalDataUseCase: UpdateHistoricalDataUseCase,
    private val dispatcher: CoroutineDispatcher
) : AndroidViewModel(app) {
    private val _historicalFlow = MutableStateFlow<Resource<List<Historical>>>(Resource.Loading())
    val historicalFlow: StateFlow<Resource<List<Historical>>> = _historicalFlow

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, e ->
        _historicalFlow.value = Resource.Error(e.message.toString())
    }
    private val _actionFlow = MutableStateFlow<Resource<ActionState>>(Resource.Loading())
    val actionFlow: StateFlow<Resource<ActionState>> = _actionFlow

    private val coroutineActionExceptionHandler = CoroutineExceptionHandler { _, e ->
        _actionFlow.value = Resource.Error(e.message.toString())
    }

    /**
     * Get list of historical data for a city
     * @param cityId City Id
     */
    fun getHistorical(cityId: Int) =
        viewModelScope.launch(dispatcher + coroutineExceptionHandler) {
            _historicalFlow.value = Resource.Loading()
            getHistoricalDataUseCase.execute(cityId)
                .catch {
                    _historicalFlow.value = Resource.Error(it.message.toString())
                }.collect { list ->
                    _historicalFlow.value = Resource.Success(list)
                }
        }

    fun updateHistoricalData() =
        viewModelScope.launch(dispatcher + coroutineActionExceptionHandler) {
            if (connectivityManager.isConnected()) {
                _actionFlow.value = Resource.Loading()
                runCatching {
                    updateHistoricalDataUseCase.execute()
                    _actionFlow.value = Resource.Success(ActionState.UPDATE)
                }.onFailure { t ->
                    _actionFlow.value = Resource.Error(t.message.toString())
                }
            } else _actionFlow.value = Resource.Error(app.getString(R.string.device_not_connected))
        }
}

@Singleton
class HistoricalViewModelFactory @Inject constructor(
    private val app: Application,
    private val connectivityManager: ConnectivityManager,
    private val getHistoricalDataUseCase: GetHistoricalDataUseCase,
    private val updateHistoricalDataUseCase: UpdateHistoricalDataUseCase,
    private val dispatcher: CoroutineDispatcher
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoricalViewModel::class.java)) {
            return HistoricalViewModel(
                app,
                connectivityManager,
                getHistoricalDataUseCase,
                updateHistoricalDataUseCase,
                dispatcher
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}