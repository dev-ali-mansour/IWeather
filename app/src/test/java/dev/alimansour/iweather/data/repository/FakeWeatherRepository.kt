package dev.alimansour.iweather.data.repository

import dev.alimansour.iweather.TEST_CITY_LIST
import dev.alimansour.iweather.TEST_HISTORICAL_LIST
import dev.alimansour.iweather.TEST_UPDATED_HISTORICAL_LIST
import dev.alimansour.iweather.data.local.entity.City
import dev.alimansour.iweather.data.local.entity.Historical
import dev.alimansour.iweather.data.mappers.CityMapper
import dev.alimansour.iweather.domain.model.CityData
import dev.alimansour.iweather.domain.repository.WeatherRepository

class FakeWeatherRepository : WeatherRepository {
    private val cities = arrayListOf<City>()
    private val historicalList = arrayListOf<Historical>()
    private val cityMapper = CityMapper()

    init {
        cities.clear()
        cities.addAll(TEST_CITY_LIST)
        historicalList.addAll(TEST_HISTORICAL_LIST)
    }

    override suspend fun addCity(cityName: String): List<City> {
        cities.add(City(5, cityName, "EG"))
        return cities
    }

    override suspend fun deleteCity(city: CityData): List<City> {
        cities.remove(cityMapper.mapToEntity(city))
        return cities
    }

    override suspend fun getCities(): List<City> = cities

    override suspend fun updateHistoricalData() {
        historicalList.clear()
        historicalList.addAll(TEST_UPDATED_HISTORICAL_LIST)
    }

    override suspend fun getHistoricalData(id: Int): List<Historical> =
        historicalList.filter { historical -> historical.city.cityId == id }
}