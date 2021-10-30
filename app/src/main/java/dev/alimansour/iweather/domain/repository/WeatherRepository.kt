package dev.alimansour.iweather.domain.repository

import dev.alimansour.iweather.domain.model.City
import dev.alimansour.iweather.domain.model.Historical
import kotlinx.coroutines.flow.Flow

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
    suspend fun deleteCity(city: City)

    /**
     * Retrieve city list from database
     * @return Flow<List<CityEntity>>
     */
    suspend fun getCities(): Flow<List<City>>

    /**
     * Update historical data of saved cities into database
     */
    suspend fun updateHistoricalData()

    /**
     * Fetch historical weather data for specific city
     * @param id City Id
     * @return Flow<List<HistoricalEntity>>
     */
    suspend fun getHistoricalData(id: Int): Flow<List<Historical>>
}