package dev.alimansour.iweather.domain.usecase.city

import dev.alimansour.iweather.data.local.entity.City
import dev.alimansour.iweather.domain.repository.WeatherRepository
import javax.inject.Inject

/**
 * WeatherApp Android Application developed by: Ali Mansour
 * ----------------- WeatherApp IS FREE SOFTWARE -------------------
 * https://www.alimansour.dev   |   mailto:dev.ali.mansour@gmail.com
 */
class GetCitiesUseCase @Inject constructor(private val weatherRepository: WeatherRepository) {

    suspend fun execute(): List<City> = weatherRepository.getCities()

}