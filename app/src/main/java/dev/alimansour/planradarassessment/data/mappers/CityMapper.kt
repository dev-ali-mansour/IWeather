package dev.alimansour.planradarassessment.data.mappers

import dev.alimansour.planradarassessment.data.local.entity.City
import dev.alimansour.planradarassessment.domain.model.CityData

/**
 * WeatherApp Android Application developed by: Ali Mansour
 * ----------------- WeatherApp IS FREE SOFTWARE -------------------
 * https://www.alimansour.dev   |   mailto:dev.ali.mansour@gmail.com
 */
object CityMapper : Mapper<List<City>, List<CityData>> {

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