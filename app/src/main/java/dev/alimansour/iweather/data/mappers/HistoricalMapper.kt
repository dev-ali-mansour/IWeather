package dev.alimansour.iweather.data.mappers

import dev.alimansour.iweather.data.local.entity.City
import dev.alimansour.iweather.data.local.entity.Historical
import dev.alimansour.iweather.domain.model.CityData
import dev.alimansour.iweather.domain.model.HistoricalData
import javax.inject.Inject
import javax.inject.Singleton

/**
 * WeatherApp Android Application developed by: Ali Mansour
 * ----------------- WeatherApp IS FREE SOFTWARE -------------------
 * https://www.alimansour.dev   |   mailto:dev.ali.mansour@gmail.com
 */
@Singleton
class HistoricalMapper @Inject constructor() : Mapper<List<Historical>, List<HistoricalData>> {

    override fun mapFromEntity(type: List<Historical>): List<HistoricalData> {
        val data = mutableListOf<HistoricalData>()
        type.forEach {
            data.add(
                HistoricalData(
                    it.id,
                    CityData(it.city.cityId, it.city.name, it.city.country),
                    it.icon,
                    it.date,
                    it.description,
                    it.temperature,
                    it.humidity,
                    it.windSpeed
                )
            )
        }
        return data
    }

    override fun mapToEntity(type: List<HistoricalData>): List<Historical> {
        val data = mutableListOf<Historical>()
        type.forEach {
            data.add(
                Historical(
                    it.id,
                    City(it.city.id, it.city.name, it.city.country),
                    it.icon,
                    it.date,
                    it.description,
                    it.temperature,
                    it.humidity,
                    it.windSpeed
                )
            )
        }
        return data
    }
}