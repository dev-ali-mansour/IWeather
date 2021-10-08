package dev.alimansour.planradarassessment.data.repository

import dev.alimansour.planradarassessment.data.local.LocalDataSource
import dev.alimansour.planradarassessment.data.local.entity.City
import dev.alimansour.planradarassessment.data.local.entity.Historical
import dev.alimansour.planradarassessment.data.remote.RemoteDataSource
import dev.alimansour.planradarassessment.domain.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

/**
 * WeatherApp Android Application developed by: Ali Mansour
 * ----------------- WeatherApp IS FREE SOFTWARE -------------------
 * https://www.alimansour.dev   |   mailto:dev.ali.mansour@gmail.com
 */
class WeatherRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) :
    WeatherRepository {
    override suspend fun addCity(cityName: String) {
        withContext(Dispatchers.IO) {
            runCatching {
                val dataList = ArrayList<Historical>()
                val resource = remoteDataSource.fetchHistoricalData(cityName)
                resource.data?.let { response ->
                    val city = City(
                        response.city.id,
                        response.city.name,
                        response.city.country
                    )
                    localDataSource.addCity(city)
                    response.list?.let { list ->
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

                        localDataSource.addHistoricalData(dataList)
                    }
                }


            }.onFailure { t -> Timber.e(t.message) }
        }
    }

    override suspend fun getCities(): List<City> = localDataSource.getCities()

    override suspend fun addHistoricalData(list: List<Historical>) =
        localDataSource.addHistoricalData(list)

    override suspend fun fetchHistoricalData(id: Int): Historical =
        localDataSource.fetchHistoricalData(id)
}