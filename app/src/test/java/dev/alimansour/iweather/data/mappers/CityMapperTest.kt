package dev.alimansour.iweather.data.mappers

import com.google.common.truth.Truth.assertThat
import dev.alimansour.iweather.data.local.entity.City
import dev.alimansour.iweather.domain.model.CityData

import org.junit.Test

/**
 * WeatherApp Android Application developed by: Ali Mansour
 * ----------------- WeatherApp IS FREE SOFTWARE -------------------
 * https://www.alimansour.dev   |   mailto:dev.ali.mansour@gmail.com
 */
class CityMapperTest {
    private val cityMapper = CityMapper()

    @Test
    fun mapFromEntity_UsingListOfCity_returnListOfCityData() {
        val cityList: List<City> = listOf(
            City(1, "Cairo", "EG"),
            City(2, "London", "GB")
        )
        val cityDataList: List<CityData> = listOf(
            CityData(1, "Cairo", "EG"),
            CityData(2, "London", "GB")
        )

        val result = cityMapper.mapFromEntity(cityList)

        assertThat(result).isEqualTo(cityDataList)
    }

    @Test
    fun mapToEntity_usingListOfCityData_returnListOfCity() {
        val cityList: List<City> = listOf(
            City(1, "Cairo", "EG"),
            City(2, "London", "GB")
        )
        val cityDataList: List<CityData> = listOf(
            CityData(1, "Cairo", "EG"),
            CityData(2, "London", "GB")
        )

        val result = cityMapper.mapToEntity(cityDataList)

        assertThat(result).isEqualTo(cityList)
    }
}