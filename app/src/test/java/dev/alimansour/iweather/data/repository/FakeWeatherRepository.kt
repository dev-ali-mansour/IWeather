package dev.alimansour.iweather.data.repository
import dev.alimansour.iweather.TestUtil.TEST_CITY_LIST
import dev.alimansour.iweather.TestUtil.TEST_HISTORICAL_LIST
import dev.alimansour.iweather.TestUtil.TEST_UPDATED_HISTORICAL_LIST
import dev.alimansour.iweather.data.local.entity.City
import dev.alimansour.iweather.data.local.entity.Historical
import dev.alimansour.iweather.data.local.entity.toEntity
import dev.alimansour.iweather.domain.model.CityData
import dev.alimansour.iweather.domain.repository.WeatherRepository
import java.lang.Exception

/**
 * WeatherApp Android Application developed by: Ali Mansour
 * ----------------- WeatherApp IS FREE SOFTWARE -------------------
 * https://www.alimansour.dev   |   mailto:dev.ali.mansour@gmail.com
 */
class FakeWeatherRepository : WeatherRepository {
    private val cities = arrayListOf<City>()
    private val historicalList = arrayListOf<Historical>()
    private var isSuccessful: Boolean = true

    init {
        cities.clear()
        cities.addAll(TEST_CITY_LIST)
        historicalList.addAll(TEST_HISTORICAL_LIST)
    }

    override suspend fun addCity(cityName: String): List<City> {
        if (isSuccessful) {
            cities.add(City(4, cityName, "EG"))
            return cities
        } else throw Exception("Failed to get results!")
    }

    override suspend fun deleteCity(city: CityData): List<City> {
        if (isSuccessful) {
            cities.remove(city.toEntity())
            return cities
        } else throw Exception("Failed to get results!")
    }

    override suspend fun getCities(): List<City> {
        if (isSuccessful) {
            return cities
        } else throw Exception("Failed to get results!")
    }

    override suspend fun updateHistoricalData() {
        if (isSuccessful) {
            historicalList.clear()
            historicalList.addAll(TEST_UPDATED_HISTORICAL_LIST)
        } else throw Exception("Failed to get results!")
    }

    override suspend fun getHistoricalData(id: Int): List<Historical> {
        if (isSuccessful) {
            return historicalList.filter { historical -> historical.city.cityId == id }
        } else throw Exception("Failed to get results!")
    }

    fun setSuccessful(isSuccessful: Boolean) {
        this.isSuccessful = isSuccessful
    }
}