package dev.alimansour.iweather.data.local

import com.google.common.truth.Truth.assertThat
import dev.alimansour.iweather.TestUtil.TEST_ASWAN_HISTORICAL_LIST
import dev.alimansour.iweather.TestUtil.TEST_CITY_LIST
import dev.alimansour.iweather.TestUtil.TEST_HISTORICAL_LIST
import dev.alimansour.iweather.TestUtil.aswan
import dev.alimansour.iweather.TestUtil.cairo
import dev.alimansour.iweather.TestUtil.giza
import dev.alimansour.iweather.TestUtil.luxor
import dev.alimansour.iweather.data.local.dao.CityDao
import dev.alimansour.iweather.data.local.dao.HistoricalDao
import dev.alimansour.iweather.data.local.entity.CityEntity
import dev.alimansour.iweather.data.local.entity.HistoricalEntity
import dev.alimansour.iweather.data.local.entity.toEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

/**
 * WeatherApp Android Application developed by: Ali Mansour
 * ----------------- WeatherApp IS FREE SOFTWARE -------------------
 * https://www.alimansour.dev   |   mailto:dev.ali.mansour@gmail.com
 */
class LocalDataSourceImplTest {
    private lateinit var cityDao: CityDao
    private lateinit var historicalDao: HistoricalDao
    private lateinit var localDataSource: LocalDataSource
    private lateinit var savedCities: MutableList<CityEntity>
    private lateinit var savedHistorical: MutableList<HistoricalEntity>

    @Before
    fun setUp() {
        cityDao = Mockito.mock(CityDao::class.java)
        historicalDao = Mockito.mock(HistoricalDao::class.java)
        localDataSource = LocalDataSourceImpl(cityDao, historicalDao)
        savedCities = TEST_CITY_LIST.map { it.toEntity() }.toMutableList()
        savedHistorical = TEST_HISTORICAL_LIST.map { it.toEntity() }.toMutableList()
    }

    @Test
    fun `addCity() When getCities() Return List of saved cities`() = runBlocking {
        //GIVEN
        Mockito.`when`(cityDao.insert(aswan.toEntity())).then {
            savedCities.add(aswan.toEntity())
            savedHistorical.addAll(TEST_HISTORICAL_LIST.map { it.toEntity() }
                .filter { historical -> historical.cityEntity == aswan.toEntity() })
        }
        Mockito.`when`(cityDao.getCities()).thenReturn(flow { emit(savedCities) })
        Mockito.`when`(historicalDao.getHistoricalData(aswan.id))
            .thenReturn(flow { emit(savedHistorical.filter { it.cityEntity == aswan.toEntity() }) })

        //WHEN
        localDataSource.addCity(aswan.toEntity())
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
        Mockito.`when`(cityDao.insert(aswan.toEntity())).then { throw Exception() }

        //WHEN
        localDataSource.addCity(aswan.toEntity())
    }

    @Test
    fun `deleteCity() Then delete city and it's historical data from database`() = runBlocking {
        //GIVEN
        Mockito.`when`(cityDao.delete(cairo.toEntity())).then {
            savedCities.remove(cairo.toEntity())

        }
        Mockito.`when`(historicalDao.clearCityHistoricalData(cairo.id)).then {
            val cairoHistorical = savedHistorical.filter { it.cityEntity == cairo.toEntity() }
            savedHistorical.removeAll(cairoHistorical)
        }
        Mockito.`when`(cityDao.getCities()).thenReturn(flow { emit(savedCities) })
        Mockito.`when`(historicalDao.getHistoricalData(cairo.id))
            .thenReturn(flow { emit(savedHistorical.filter { it.cityEntity == cairo.toEntity() }) })

        //WHEN
        localDataSource.deleteCity(cairo.toEntity())
        val cities = localDataSource.getCities().first()
        val cairoHistorical = localDataSource.getHistoricalData(cairo.id).first()

        //THEN
        assertThat(cities).isEqualTo(savedCities)
        assertThat(cities).doesNotContain(cairo)
        assertThat(cairoHistorical).isEmpty()
    }

    @Test(expected = Exception::class)
    fun `deleteCity() When exception is thrown Then Catch that exception`() = runBlocking {
        //GIVEN
        Mockito.`when`(cityDao.delete(aswan.toEntity())).then { throw Exception() }

        //WHEN
        localDataSource.deleteCity(aswan.toEntity())
    }

    @Test(expected = Exception::class)
    fun `getCities() When exception is thrown Then Catch that exception`() = runBlocking {
        //GIVEN
        Mockito.`when`(cityDao.getCities()).then { throw Exception() }

        //WHEN
        val cities = localDataSource.getCities().first()

        //THEN
        assertThat(cities).isEmpty()
    }

    @Test
    fun `addHistoricalData() When getHistoricalData() Return List of correct saved historical data`() =
        runBlocking {
            //GIVEN
            Mockito.`when`(historicalDao.insertList(TEST_ASWAN_HISTORICAL_LIST)).then {
                savedHistorical.addAll(TEST_ASWAN_HISTORICAL_LIST)
            }
            Mockito.`when`(historicalDao.getHistoricalData(cairo.id))
                .thenReturn(flow { emit(savedHistorical.filter { it.cityEntity == cairo.toEntity() }) })
            Mockito.`when`(historicalDao.getHistoricalData(giza.id))
                .thenReturn(flow { emit(savedHistorical.filter { it.cityEntity == giza.toEntity() }) })
            Mockito.`when`(historicalDao.getHistoricalData(luxor.id))
                .thenReturn(flow { emit(savedHistorical.filter { it.cityEntity == luxor.toEntity() }) })
            Mockito.`when`(historicalDao.getHistoricalData(aswan.id))
                .thenReturn(flow { emit(savedHistorical.filter { it.cityEntity == aswan.toEntity() }) })

            //WHEN
            localDataSource.addHistoricalData(TEST_ASWAN_HISTORICAL_LIST)
            val cairoHistorical = localDataSource.getHistoricalData(cairo.id).first()
            val gizaHistorical = localDataSource.getHistoricalData(giza.id).first()
            val luxorHistorical = localDataSource.getHistoricalData(luxor.id).first()
            val aswanHistorical = localDataSource.getHistoricalData(aswan.id).first()

            //THEN
            assertThat(cairoHistorical)
                .isEqualTo(savedHistorical.filter { it.cityEntity == cairo.toEntity() })
            assertThat(gizaHistorical)
                .isEqualTo(savedHistorical.filter { it.cityEntity == giza.toEntity() })
            assertThat(luxorHistorical)
                .isEqualTo(savedHistorical.filter { it.cityEntity == luxor.toEntity() })
            assertThat(aswanHistorical)
                .isEqualTo(savedHistorical.filter { it.cityEntity == aswan.toEntity() })
        }

    @Test(expected = Exception::class)
    fun `addHistoricalData() When exception is thrown Then Catch that exception`() = runBlocking {
        //GIVEN
        Mockito.`when`(historicalDao.insertList(TEST_ASWAN_HISTORICAL_LIST))
            .then { throw Exception() }

        //WHEN
        localDataSource.addHistoricalData(TEST_ASWAN_HISTORICAL_LIST)
    }

    @Test
    fun `clearCachedHistoricalData() When getHistoricalData() Then Return empty list of historical data`() =
        runBlocking {
            //GIVEN
            Mockito.`when`(historicalDao.clearHistoricalData())
                .then { savedHistorical.clear() }
            Mockito.`when`(historicalDao.getHistoricalData(cairo.id))
                .thenReturn(flow { emit(savedHistorical.filter { it.cityEntity == cairo.toEntity() }) })
            Mockito.`when`(historicalDao.getHistoricalData(giza.id))
                .thenReturn(flow { emit(savedHistorical.filter { it.cityEntity == giza.toEntity() }) })

            //WHEN
            localDataSource.clearCachedHistoricalData()
            val cairoHistorical = localDataSource.getHistoricalData(cairo.id).first()
            val gizaHistorical = localDataSource.getHistoricalData(giza.id).first()

            //THEN
            assertThat(savedHistorical).isEmpty()
            assertThat(cairoHistorical).isEmpty()
            assertThat(gizaHistorical).isEmpty()
        }

    @Test(expected = Exception::class)
    fun `clearCachedHistoricalData() When exception is thrown Then Catch that exception`() =
        runBlocking {
            //GIVEN
            Mockito.`when`(historicalDao.clearHistoricalData())
                .then { throw Exception() }

            //WHEN
            localDataSource.clearCachedHistoricalData()
        }
}