package dev.alimansour.iweather.domain.repository

import dev.alimansour.iweather.data.local.entity.City
import dev.alimansour.iweather.data.local.entity.Historical

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
    suspend fun addCity(cityName:String)

    /**
     * Retrieve city list from database
     * @return Resource<List<City>>
     */
    suspend fun getCities(): List<City>
    /**
     * Add list of historical data to database
     * @param list List of Historical
     */
    suspend fun addHistoricalData(list: List<Historical>)

    /**
     * Fetch historical weather data for specific city
     * @param id City Id
     * @return List<Historical>
     */
    suspend fun getHistoricalData(id: Int): List<Historical>
}