package dev.alimansour.iweather.data.local

import dev.alimansour.iweather.data.local.entity.CityEntity
import dev.alimansour.iweather.data.local.entity.HistoricalEntity
import kotlinx.coroutines.flow.Flow

/**
 * WeatherApp Android Application developed by: Ali Mansour
 * ----------------- WeatherApp IS FREE SOFTWARE -------------------
 * https://www.alimansour.dev   |   mailto:dev.ali.mansour@gmail.com
 */
interface LocalDataSource {

    /**
     * Add city data to database
     * @param cityEntity City
     */
    suspend fun addCity(cityEntity: CityEntity)

    /**
     * Delete saved city and it's historical data from the database
     * @param cityEntity City
     */
    suspend fun deleteCity(cityEntity: CityEntity)

    /**
     * Retrieve city list from database
     * @return Flow<Resource<List<City>>>
     */
    suspend fun getCities(): Flow<List<CityEntity>>

    /**
     * Add list of historical data to database
     * @param list List of Historical
     */
    suspend fun addHistoricalData(list: List<HistoricalEntity>)

    /**
     * Clear all historical data from database
     */
    suspend fun clearCachedHistoricalData()

    /**
     * Get historical weather data for specific city
     * @param id City Id
     * @return Flow<List<HistoricalEntity>>
     */
    suspend fun getHistoricalData(id: Int): Flow<List<HistoricalEntity>>
}