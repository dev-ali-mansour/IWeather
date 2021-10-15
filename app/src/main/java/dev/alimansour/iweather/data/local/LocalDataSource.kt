package dev.alimansour.iweather.data.local

import dev.alimansour.iweather.data.local.entity.City
import dev.alimansour.iweather.data.local.entity.Historical

/**
 * WeatherApp Android Application developed by: Ali Mansour
 * ----------------- WeatherApp IS FREE SOFTWARE -------------------
 * https://www.alimansour.dev   |   mailto:dev.ali.mansour@gmail.com
 */
interface LocalDataSource {

    /**
     * Add city data to database
     * @param city City
     */
    suspend fun addCity(city: City)

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
     * Get historical weather data for specific city
     * @param id City Id
     * @return CityAndHistorical
     */
    suspend fun getHistoricalData(id: Int): List<Historical>
}