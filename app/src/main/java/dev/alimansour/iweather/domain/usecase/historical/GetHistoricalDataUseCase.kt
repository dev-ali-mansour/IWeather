package dev.alimansour.iweather.domain.usecase.historical

import dev.alimansour.iweather.domain.model.Historical
import dev.alimansour.iweather.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * WeatherApp Android Application developed by: Ali Mansour
 * ----------------- WeatherApp IS FREE SOFTWARE -------------------
 * https://www.alimansour.dev   |   mailto:dev.ali.mansour@gmail.com
 */
class GetHistoricalDataUseCase @Inject constructor(private val weatherRepository: WeatherRepository) {

    suspend fun execute(id: Int): Flow<List<Historical>> =
        weatherRepository.getHistoricalData(id)
}