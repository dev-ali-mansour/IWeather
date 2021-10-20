package dev.alimansour.iweather.data.local

import dev.alimansour.iweather.data.local.entity.City
import dev.alimansour.iweather.data.local.entity.Historical

/**
 * WeatherApp Android Application developed by: Ali Mansour
 * ----------------- WeatherApp IS FREE SOFTWARE -------------------
 * https://www.alimansour.dev   |   mailto:dev.ali.mansour@gmail.com
 */
class LocalDataSourceImpl(private val database: WeatherDatabase) : LocalDataSource {

    override suspend fun addCity(city: City) = database.cityDao().insert(city)

    override suspend fun getCities(): List<City> =
        database.cityDao().getCities()

    override suspend fun addHistoricalData(list: List<Historical>) =
        database.historicalDao().insertList(list)

    override suspend fun clearCachedHistoricalData() {
        database.historicalDao().clearHistoricalData()
    }

    override suspend fun getHistoricalData(id: Int): List<Historical> =
        database.historicalDao().getHistoricalData(id)
}