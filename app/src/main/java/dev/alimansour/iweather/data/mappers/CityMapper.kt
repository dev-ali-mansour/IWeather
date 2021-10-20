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
class CityMapper @Inject constructor() : Mapper<City, CityData> {

    override fun mapFromEntity(type: City): CityData =
        CityData(type.cityId, type.name, type.country)

    override fun mapToEntity(type: CityData): City =
        City(type.id, type.name, type.country)

}