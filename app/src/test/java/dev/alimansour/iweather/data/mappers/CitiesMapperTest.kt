package dev.alimansour.iweather.data.mappers

import com.google.common.truth.Truth.assertThat
import dev.alimansour.iweather.data.local.entity.City
import dev.alimansour.iweather.domain.model.CityData
import org.junit.Before

import org.junit.Test

/**
 * WeatherApp Android Application developed by: Ali Mansour
 * ----------------- WeatherApp IS FREE SOFTWARE -------------------
 * https://www.alimansour.dev   |   mailto:dev.ali.mansour@gmail.com
 */
class CitiesMapperTest {
    private lateinit var citiesMapper: CitiesMapper
    private lateinit var cityList: List<City>
    private lateinit var cityDataList: List<CityData>

    @Before
    fun setUp() {
        citiesMapper = CitiesMapper()
        cityList = listOf(
            City(1, "Cairo", "EG"),
            City(2, "London", "GB")
        )
        cityDataList = listOf(
            CityData(1, "Cairo", "EG"),
            CityData(2, "London", "GB")
        )
    }

    @Test
    fun mapFromEntity_UsingListOfCity_returnListOfCityData() {
        val result = citiesMapper.mapFromEntity(cityList)

        assertThat(result).isEqualTo(cityDataList)
    }

    @Test
    fun mapToEntity_usingListOfCityData_returnListOfCity() {
        val result = citiesMapper.mapToEntity(cityDataList)

        assertThat(result).isEqualTo(cityList)
    }
}