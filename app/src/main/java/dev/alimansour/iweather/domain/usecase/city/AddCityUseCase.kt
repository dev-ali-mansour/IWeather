package dev.alimansour.iweather.domain.usecase.city

import dev.alimansour.iweather.domain.repository.WeatherRepository
import javax.inject.Inject

/**
 * WeatherApp Android Application developed by: Ali Mansour
 * ----------------- WeatherApp IS FREE SOFTWARE -------------------
 * https://www.alimansour.dev   |   mailto:dev.ali.mansour@gmail.com
 */
class AddCityUseCase @Inject constructor(private val weatherRepository: WeatherRepository) {

    suspend fun execute(cityName: String)= weatherRepository.addCity(cityName)

}