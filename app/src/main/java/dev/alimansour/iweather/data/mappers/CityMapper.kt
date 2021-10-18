package dev.alimansour.iweather.data.mappers

import dev.alimansour.iweather.data.local.entity.City
import dev.alimansour.iweather.domain.model.CityData
import javax.inject.Inject
import javax.inject.Singleton

/**
 * WeatherApp Android Application developed by: Ali Mansour
 * ----------------- WeatherApp IS FREE SOFTWARE -------------------
 * https://www.alimansour.dev   |   mailto:dev.ali.mansour@gmail.com
 */
@Singleton
class CityMapper @Inject constructor() : Mapper<List<City>, List<CityData>> {

    override fun mapFromEntity(type: List<City>): List<CityData> {
        val data = mutableListOf<CityData>()
        type.forEach {
            data.add(
                CityData(it.cityId, it.name, it.country)
            )
        }
        return data
    }

    override fun mapToEntity(type: List<CityData>): List<City> {
        val data = mutableListOf<City>()
        type.forEach {
            data.add(
                City(it.id, it.name, it.country)
            )
        }
        return data
    }
}