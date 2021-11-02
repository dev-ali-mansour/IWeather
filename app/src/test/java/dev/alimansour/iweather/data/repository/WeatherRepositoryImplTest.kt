package dev.alimansour.iweather.data.repository

import com.google.common.truth.Truth.assertThat
import dev.alimansour.iweather.TestUtil.TEST_ASWAN_HISTORICAL_LIST
import dev.alimansour.iweather.TestUtil.TEST_CITY_LIST
import dev.alimansour.iweather.TestUtil.TEST_HISTORICAL_LIST
import dev.alimansour.iweather.TestUtil.aswan
import dev.alimansour.iweather.TestUtil.cairo
import dev.alimansour.iweather.data.local.LocalDataSource
import dev.alimansour.iweather.data.local.entity.CityEntity
import dev.alimansour.iweather.data.local.entity.HistoricalEntity
import dev.alimansour.iweather.data.local.entity.toEntity
import dev.alimansour.iweather.data.local.entity.toModel
import dev.alimansour.iweather.data.remote.RemoteDataSource
import dev.alimansour.iweather.toHistoricalResponse
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import retrofit2.Response

/**
 * WeatherApp Android Application developed by: Ali Mansour
 * ----------------- WeatherApp IS FREE SOFTWARE -------------------
 * https://www.alimansour.dev   |   mailto:dev.ali.mansour@gmail.com
 */
class WeatherRepositoryImplTest {
    private lateinit var remoteDataSource: RemoteDataSource
    private lateinit var localDataSource: LocalDataSource
    private lateinit var weatherRepository: WeatherRepositoryImpl
    private lateinit var savedCities: MutableList<CityEntity>
    private lateinit var savedHistorical: MutableList<HistoricalEntity>

    @Before
    fun setUp() {
        remoteDataSource = mock(RemoteDataSource::class.java)
        localDataSource = mock(LocalDataSource::class.java)
        weatherRepository = WeatherRepositoryImpl(remoteDataSource, localDataSource)
        savedCities = TEST_CITY_LIST.map { it.toEntity() }.toMutableList()
        savedHistorical = TEST_HISTORICAL_LIST.map { it.toEntity() }.toMutableList()
    }

    @Test
    fun `addCity() When getCities() Return List of saved cities`() = runBlocking {
        //GIVEN
        `when`(localDataSource.addCity(aswan.toEntity())).then {
            savedCities.add(aswan.toEntity())
            savedHistorical.addAll(TEST_HISTORICAL_LIST.map { it.toEntity() }
                .filter { historical -> historical.cityEntity == aswan.toEntity() })
        }
        `when`(localDataSource.getCities()).thenReturn(flow { emit(savedCities) })
        `when`(localDataSource.getHistoricalData(aswan.id))
            .thenReturn(flow { emit(savedHistorical.filter { it.cityEntity == aswan.toEntity() }) })

        //WHEN
        `when`(remoteDataSource.fetchHistoricalData(aswan.name)).thenReturn(
            Response.success(
                TEST_ASWAN_HISTORICAL_LIST.map { it.toModel() }.toHistoricalResponse()
            )
        )
        weatherRepository.addCity(aswan.name)
        val cities = localDataSource.getCities().first()
        val aswanHistorical = localDataSource.getHistoricalData(aswan.id).first()

        //THEN
        assertThat(cities).isEqualTo(savedCities)
        assertThat(aswanHistorical)
            .isEqualTo(savedHistorical.filter { it.cityEntity.cityId == aswan.id })
    }

    @Test(expected = Exception::class)
    fun `addCity() When exception is thrown Then Catch that exception`() = runBlocking {
        //GIVEN
        `when`(localDataSource.addCity(aswan.toEntity())).then { throw Exception() }

        //WHEN
        weatherRepository.addCity(aswan.name)
    }

    @Test
    fun `deleteCity() When getCities() Return list of saved cities after deletion`() = runBlocking {
        //GIVEN
        val list = TEST_CITY_LIST.toMutableList()
        `when`(localDataSource.deleteCity(cairo.toEntity())).then {
            list.remove(cairo)
        }
        `when`(localDataSource.getCities()).thenReturn(flow { emit(list.map { it.toEntity() }) })
        //WHEN
        weatherRepository.deleteCity(cairo)
        val cities = weatherRepository.getCities().first()

        //THEN
        assertThat(cities).isEqualTo(list)
    }

    @Test(expected = Exception::class)
    fun `deleteCity() When exception is thrown Then Catch that exception`() = runBlocking {
        //GIVEN
        `when`(localDataSource.deleteCity(aswan.toEntity())).then { throw Exception() }

        //WHEN
        weatherRepository.deleteCity(aswan)
    }

    @Test
    fun getCities() = runBlocking {
        //GIVEN
        val list = TEST_CITY_LIST.toMutableList()
        `when`(localDataSource.getCities()).thenReturn(flow { emit(list.map { it.toEntity() }) })
        //WHEN
        weatherRepository.deleteCity(cairo)
        val cities = weatherRepository.getCities().first()

        //THEN
        assertThat(cities).isEqualTo(list)
    }

    @Test(expected = Exception::class)
    fun `getCities() When exception is thrown Then Catch that exception`() = runBlocking {
        //GIVEN
        `when`(localDataSource.getCities()).then { throw Exception() }

        //WHEN
        val cities = weatherRepository.getCities()

        //THEN
        assertThat(cities).isNull()
    }

    @Test
    fun updateHistoricalData() = runBlocking {
    }

    @Test(expected = Exception::class)
    fun `updateHistoricalData() When exception is thrown Then Catch that exception`() =
        runBlocking {
            //GIVEN
            `when`(localDataSource.clearCachedHistoricalData()).then { throw Exception() }

            //WHEN
            weatherRepository.updateHistoricalData()
        }

    @Test
    fun getHistoricalData() = runBlocking {
        //GIVEN
        val data = TEST_HISTORICAL_LIST.filter { historical -> historical.city == cairo }
        `when`(localDataSource.getHistoricalData(cairo.id))
            .thenReturn(flow { emit(data.map { it.toEntity() }) })

        //WHEN
        val historicalData = weatherRepository.getHistoricalData(cairo.id).first()

        //THEN
        assertThat(historicalData).isEqualTo(data)
    }

    @Test(expected = Exception::class)
    fun `getHistoricalData() When exception is thrown Then Catch that exception`() = runBlocking {
        //GIVEN
        `when`(localDataSource.getHistoricalData(aswan.id)).then { throw Exception() }

        //WHEN
        val historicalData = weatherRepository.getHistoricalData(aswan.id)

        //THEN
        assertThat(historicalData).isNull()
    }
}