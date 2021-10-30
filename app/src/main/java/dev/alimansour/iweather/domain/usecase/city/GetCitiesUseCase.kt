package dev.alimansour.iweather.domain.usecase.city

import dev.alimansour.iweather.domain.model.City
import dev.alimansour.iweather.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * WeatherApp Android Application developed by: Ali Mansour
 * ----------------- WeatherApp IS FREE SOFTWARE -------------------
 * https://www.alimansour.dev   |   mailto:dev.ali.mansour@gmail.com
 */
class GetCitiesUseCase @Inject constructor(private val weatherRepository: WeatherRepository) {

    suspend fun execute(): Flow<List<City>> = weatherRepository.getCities()

}