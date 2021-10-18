package dev.alimansour.iweather.data.mappers

import com.google.common.truth.Truth.assertThat
import dev.alimansour.iweather.data.local.entity.City
import dev.alimansour.iweather.data.local.entity.Historical
import dev.alimansour.iweather.domain.model.CityData
import dev.alimansour.iweather.domain.model.HistoricalData
import org.junit.Before

import org.junit.Test

/**
 * WeatherApp Android Application developed by: Ali Mansour
 * ----------------- WeatherApp IS FREE SOFTWARE -------------------
 * https://www.alimansour.dev   |   mailto:dev.ali.mansour@gmail.com
 */
class HistoricalMapperTest {
    private lateinit var historicalMapper: HistoricalMapper
    private lateinit var historicalList: List<Historical>
    private lateinit var historicalDataList: List<HistoricalData>

    @Before
    fun setUp() {
        historicalMapper = HistoricalMapper()
        historicalList = listOf(
            Historical(
                1,
                City(1, "Cairo", "EG"),
                "clear.png",
                "2021-10-18 18:00:00",
                "Clear",
                25.3,
                47.0,
                7.2
            ),
            Historical(
                2,
                City(1, "Cairo", "EG"),
                "clear.png",
                "2021-10-18 19:00:00",
                "Clear",
                26.0,
                48.0,
                7.5
            )
        )

        historicalDataList = listOf(
            HistoricalData(
                1,
                CityData(1, "Cairo", "EG"),
                "clear.png",
                "2021-10-18 18:00:00",
                "Clear",
                25.3,
                47.0,
                7.2
            ),
            HistoricalData(
                2,
                CityData(1, "Cairo", "EG"),
                "clear.png",
                "2021-10-18 19:00:00",
                "Clear",
                26.0,
                48.0,
                7.5
            )
        )
    }

    @Test
    fun mapFromEntity_UsingListOfHistorical_returnListOfHistoricalData() {
        val result = historicalMapper.mapFromEntity(historicalList)

        assertThat(result).isEqualTo(historicalDataList)
    }

    @Test
    fun mapToEntity_usingListOfHistoricalData_returnListOfHistorical() {
        val result = historicalMapper.mapToEntity(historicalDataList)

        assertThat(result).isEqualTo(historicalList)
    }
}