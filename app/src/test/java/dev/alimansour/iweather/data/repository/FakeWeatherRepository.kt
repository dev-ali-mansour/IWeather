package dev.alimansour.iweather.data.repository

import dev.alimansour.iweather.TestUtil.TEST_CITY_ENTITY_LIST
import dev.alimansour.iweather.TestUtil.TEST_HISTORICAL_LIST
import dev.alimansour.iweather.TestUtil.TEST_UPDATED_HISTORICAL_LIST
import dev.alimansour.iweather.data.local.entity.CityEntity
import dev.alimansour.iweather.data.local.entity.HistoricalEntity
import dev.alimansour.iweather.data.local.entity.toEntity
import dev.alimansour.iweather.data.local.entity.toModel
import dev.alimansour.iweather.domain.model.City
import dev.alimansour.iweather.domain.model.Historical
import dev.alimansour.iweather.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * WeatherApp Android Application developed by: Ali Mansour
 * ----------------- WeatherApp IS FREE SOFTWARE -------------------
 * https://www.alimansour.dev   |   mailto:dev.ali.mansour@gmail.com
 */
class FakeWeatherRepository : WeatherRepository {
    private val cities = mutableListOf<CityEntity>()
    private val historicalList = mutableListOf<HistoricalEntity>()
    private var isSuccessful: Boolean = true

    init {
        cities.clear()
        cities.addAll(TEST_CITY_ENTITY_LIST)
        historicalList.addAll(TEST_HISTORICAL_LIST)
    }

    override suspend fun addCity(cityName: String) {
        if (isSuccessful) {
            cities.add(CityEntity(4, cityName, "EG"))
        } else throw Exception("Failed to add city!")
    }

    override suspend fun deleteCity(city: City) {
        if (isSuccessful) {
            cities.remove(city.toEntity())
        } else throw Exception("Failed to delete city!")
    }

    override suspend fun getCities(): Flow<List<City>> = flow {
        if (isSuccessful) emit(cities.map { it.toModel() })
        else throw Exception("Failed to get cities!")
    }

    override suspend fun updateHistoricalData() {
        if (isSuccessful) {
            historicalList.clear()
            historicalList.addAll(TEST_UPDATED_HISTORICAL_LIST)
        } else throw Exception("Failed to update historical!")
    }

    override suspend fun getHistoricalData(id: Int): Flow<List<Historical>> = flow {
        if (isSuccessful) {
            emit(historicalList.map { it.toModel() }.filter { historical ->
                historical.city.id == id
            })
        } else throw Exception("Failed to get historical!")
    }

    fun setSuccessful(isSuccessful: Boolean) {
        this.isSuccessful = isSuccessful
    }
}