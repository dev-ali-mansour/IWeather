package dev.alimansour.iweather.domain.repository

import dev.alimansour.iweather.data.local.entity.City
import dev.alimansour.iweather.data.local.entity.Historical
import dev.alimansour.iweather.domain.model.CityData

/**
 * WeatherApp Android Application developed by: Ali Mansour
 * ----------------- WeatherApp IS FREE SOFTWARE -------------------
 * https://www.alimansour.dev   |   mailto:dev.ali.mansour@gmail.com
 */
interface WeatherRepository {

    /**
     * Add city data to database
     * @param cityName City Name
     */
    suspend fun addCity(cityName: String)

    /**
     * Delete saved city and it's historical data from the database
     * @param city City
     */
    suspend fun deleteCity(city: CityData)

    /**
     * Todo Return Flow
     * Retrieve city list from database
     * @return Resource<List<City>>
     */
    suspend fun getCities(): List<City>

    /**
     * Update historical data of saved cities into database
     */
    suspend fun updateHistoricalData()

    /**
     * Todo Return Flow
     * Fetch historical weather data for specific city
     * @param id City Id
     * @return List<Historical>
     */
    suspend fun getHistoricalData(id: Int): List<Historical>
}