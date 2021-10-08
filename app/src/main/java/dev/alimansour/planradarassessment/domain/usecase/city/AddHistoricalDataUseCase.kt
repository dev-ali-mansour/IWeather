package dev.alimansour.planradarassessment.domain.usecase.city

import dev.alimansour.planradarassessment.data.local.entity.Historical
import dev.alimansour.planradarassessment.domain.repository.WeatherRepository
import javax.inject.Inject

/**
 * WeatherApp Android Application developed by: Ali Mansour
 * ----------------- WeatherApp IS FREE SOFTWARE -------------------
 * https://www.alimansour.dev   |   mailto:dev.ali.mansour@gmail.com
 */
class AddHistoricalDataUseCase @Inject constructor(private val weatherRepository: WeatherRepository) {

    suspend fun execute(list: List<Historical>) = weatherRepository.addHistoricalData(list)

}