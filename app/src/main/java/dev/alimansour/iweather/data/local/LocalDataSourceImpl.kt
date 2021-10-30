package dev.alimansour.iweather.data.local

import dev.alimansour.iweather.data.local.dao.CityDao
import dev.alimansour.iweather.data.local.dao.HistoricalDao
import dev.alimansour.iweather.data.local.entity.CityEntity
import dev.alimansour.iweather.data.local.entity.HistoricalEntity
import kotlinx.coroutines.flow.Flow

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

    override suspend fun addCity(cityEntity: CityEntity) = cityDao.insert(cityEntity)

    override suspend fun deleteCity(cityEntity: CityEntity) {
        cityDao.delete(cityEntity)
        historicalDao.clearCityHistoricalData(cityEntity.cityId)
    }

    override suspend fun getCities(): Flow<List<CityEntity>> = cityDao.getCities()

    override suspend fun addHistoricalData(list: List<HistoricalEntity>) =
        historicalDao.insertList(list)

    override suspend fun clearCachedHistoricalData() = historicalDao.clearHistoricalData()

    override suspend fun getHistoricalData(id: Int): Flow<List<HistoricalEntity>> =
        historicalDao.getHistoricalData(id)
}