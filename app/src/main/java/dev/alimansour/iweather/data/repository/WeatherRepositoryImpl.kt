package dev.alimansour.iweather.data.repository

import dev.alimansour.iweather.data.local.LocalDataSource
import dev.alimansour.iweather.data.local.entity.CityEntity
import dev.alimansour.iweather.data.local.entity.HistoricalEntity
import dev.alimansour.iweather.data.local.entity.toEntity
import dev.alimansour.iweather.data.local.entity.toModel
import dev.alimansour.iweather.data.remote.RemoteDataSource
import dev.alimansour.iweather.data.remote.response.HistoricalResponse
import dev.alimansour.iweather.data.remote.response.WeatherData
import dev.alimansour.iweather.domain.model.City
import dev.alimansour.iweather.domain.model.Historical
import dev.alimansour.iweather.domain.repository.WeatherRepository
import dev.alimansour.iweather.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import retrofit2.Response

/**
 * WeatherApp Android Application developed by: Ali Mansour
 * ----------------- WeatherApp IS FREE SOFTWARE -------------------
 * https://www.alimansour.dev   |   mailto:dev.ali.mansour@gmail.com
 */
class WeatherRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
) :
    WeatherRepository {
    override suspend fun addCity(cityName: String) {
        val resource = responseToResource(remoteDataSource.fetchHistoricalData(cityName))
        resource.data?.let { response ->
            val city = CityEntity(
                response.city.id,
                response.city.name,
                response.city.country
            )
            localDataSource.addCity(city)
            response.list?.let { list ->
                val dataList = getHistoricalList(city, list)
                localDataSource.addHistoricalData(dataList)
            }
        }
    }

    override suspend fun deleteCity(city: City) {
        localDataSource.deleteCity(city.toEntity())
    }

    override suspend fun getCities(): Flow<List<City>> =
        localDataSource.getCities().map { list -> list.map { it.toModel() } }

    override suspend fun updateHistoricalData() {
        localDataSource.clearCachedHistoricalData()
        localDataSource.getCities().collect { cities ->
            cities.map { city ->
                val resource = responseToResource(remoteDataSource.fetchHistoricalData(city.name))
                resource.data?.let { response ->
                    response.list?.let { list ->
                        val dataList = getHistoricalList(city, list)
                        localDataSource.addHistoricalData(dataList)
                    }
                }
            }
        }
    }

    override suspend fun getHistoricalData(id: Int): Flow<List<Historical>> =
        localDataSource.getHistoricalData(id).map { list ->
            list.map { it.toModel() }
        }

    /**
     * Convert Retrofit response to Resource
     * @param response Retrofit Response of HistoricalResponse
     * @return Resource of HistoricalResponse
     */
    private fun responseToResource(response: Response<HistoricalResponse>): Resource<HistoricalResponse> {
        if (response.isSuccessful) {
            response.body()?.let { result ->
                return Resource.Success(result)
            }
        }
        return Resource.Error(response.message())
    }

    /**
     * Get list of historical items using city and it's list of WeatherData
     * @param cityEntity City
     * @param list List of WeatherData
     * @return List of Historical
     */
    private fun getHistoricalList(
        cityEntity: CityEntity,
        list: List<WeatherData>
    ): List<HistoricalEntity> {
        val dataList = ArrayList<HistoricalEntity>()
        list.forEach { item ->
            dataList.add(
                HistoricalEntity(
                    0,
                    cityEntity,
                    item.weather[0].icon,
                    item.date,
                    item.weather[0].description,
                    item.main.temp,
                    item.main.humidity,
                    item.wind.speed
                )
            )
        }
        return dataList
    }
}