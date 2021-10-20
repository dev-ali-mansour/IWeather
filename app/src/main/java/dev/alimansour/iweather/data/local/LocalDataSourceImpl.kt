package dev.alimansour.iweather.data.local

import dev.alimansour.iweather.data.local.dao.CityDao
import dev.alimansour.iweather.data.local.dao.HistoricalDao
import dev.alimansour.iweather.data.local.entity.City
import dev.alimansour.iweather.data.local.entity.Historical

/**
 * WeatherApp Android Application developed by: Ali Mansour
 * ----------------- WeatherApp IS FREE SOFTWARE -------------------
 * https://www.alimansour.dev   |   mailto:dev.ali.mansour@gmail.com
 */
class LocalDataSourceImpl(
    private val cityDao: CityDao,
    private val historicalDao: HistoricalDao
) :
    LocalDataSource {

    override suspend fun addCity(city: City) = cityDao.insert(city)

    override suspend fun deleteCity(city: City) {
        cityDao.delete(city)
        historicalDao.clearCityHistoricalData(city.cityId)
    }

    override suspend fun getCities(): List<City> = cityDao.getCities()

    override suspend fun addHistoricalData(list: List<Historical>) =
        historicalDao.insertList(list)

    override suspend fun clearCachedHistoricalData() = historicalDao.clearHistoricalData()

    override suspend fun getHistoricalData(id: Int): List<Historical> =
        historicalDao.getHistoricalData(id)
}