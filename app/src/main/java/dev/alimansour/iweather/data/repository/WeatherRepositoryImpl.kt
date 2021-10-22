package dev.alimansour.iweather.data.repository

import dev.alimansour.iweather.data.local.LocalDataSource
import dev.alimansour.iweather.data.local.entity.City
import dev.alimansour.iweather.data.local.entity.Historical
import dev.alimansour.iweather.data.mappers.CityMapper
import dev.alimansour.iweather.data.remote.RemoteDataSource
import dev.alimansour.iweather.data.remote.response.HistoricalResponse
import dev.alimansour.iweather.data.remote.response.WeatherData
import dev.alimansour.iweather.domain.model.CityData
import dev.alimansour.iweather.domain.repository.WeatherRepository
import dev.alimansour.iweather.util.Resource
import retrofit2.Response

/**
 * WeatherApp Android Application developed by: Ali Mansour
 * ----------------- WeatherApp IS FREE SOFTWARE -------------------
 * https://www.alimansour.dev   |   mailto:dev.ali.mansour@gmail.com
 */
class WeatherRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val mapper: CityMapper
) :
    WeatherRepository {
    override suspend fun addCity(cityName: String): Boolean {
        val resource = responseToResource(remoteDataSource.fetchHistoricalData(cityName))
        resource.data?.let { response ->
            val city = City(
                response.city.id,
                response.city.name,
                response.city.country
            )
            localDataSource.addCity(city)
            response.list?.let { list ->
                val dataList = getHistoricalList(city, list)
                localDataSource.addHistoricalData(dataList)
            }
            return true
        }
        return false
    }

    override suspend fun deleteCity(city: CityData) =
        localDataSource.deleteCity(mapper.mapToEntity(city))

    override suspend fun getCities(): List<City> = localDataSource.getCities()

    override suspend fun updateHistoricalData() {
        localDataSource.clearCachedHistoricalData()
        val cities = getCities()
        cities.forEach { city ->
            val resource = responseToResource(remoteDataSource.fetchHistoricalData(city.name))
            resource.data?.let { response ->
                response.list?.let { list ->
                    val dataList = getHistoricalList(city, list)
                    localDataSource.addHistoricalData(dataList)
                }
            }
        }
    }

    override suspend fun getHistoricalData(id: Int): List<Historical> =
        localDataSource.getHistoricalData(id)

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
     * @param city City
     * @param list List of WeatherData
     * @return List of Historical
     */
    private fun getHistoricalList(city: City, list: List<WeatherData>): List<Historical> {
        val dataList = ArrayList<Historical>()
        list.forEach { item ->
            dataList.add(
                Historical(
                    0,
                    city,
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