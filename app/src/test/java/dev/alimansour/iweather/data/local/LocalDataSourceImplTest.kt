package dev.alimansour.iweather.data.local

import com.google.common.truth.Truth.assertThat
import dev.alimansour.iweather.TestUtil.TEST_CITY_LIST
import dev.alimansour.iweather.TestUtil.TEST_HISTORICAL_LIST
import dev.alimansour.iweather.TestUtil.aswan
import dev.alimansour.iweather.TestUtil.cairo
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
    fun `addCity() When getCities() Return List of saved cities`() = runBlocking() {
        //GIVEN
        Mockito.`when`(cityDao.insert(aswan.toEntity())).then {
            savedCities.add(aswan.toEntity())
            savedHistorical.addAll(TEST_HISTORICAL_LIST.map { it.toEntity() }
                .filter { historical -> historical.cityEntity.cityId == aswan.describeContents() })
        }
        Mockito.`when`(cityDao.getCities()).thenReturn(flow { emit(savedCities) })
        Mockito.`when`(historicalDao.getHistoricalData(aswan.id))
            .thenReturn(flow { emit(savedHistorical.filter { it.cityEntity.cityId == aswan.id }) })

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
    fun `addCity() When exception is thrown Then Catch that exception`() = runBlocking() {
        //GIVEN
        Mockito.`when`(cityDao.insert(aswan.toEntity())).then { throw Exception() }

        //WHEN
        localDataSource.addCity(aswan.toEntity())
    }

    @Test
    fun `deleteCity() Return the correct list of cities after deletion`() = runBlocking() {
        //GIVEN
        Mockito.`when`(cityDao.delete(cairo.toEntity())).then {
            savedCities.remove(cairo.toEntity())
            val cairoHistorical = savedHistorical.filter { it.cityEntity.cityId == cairo.id }
            savedHistorical.removeAll(cairoHistorical)
        }
        Mockito.`when`(cityDao.getCities()).thenReturn(flow { emit(savedCities) })
        Mockito.`when`(historicalDao.getHistoricalData(cairo.id))
            .thenReturn(flow { emit(listOf<HistoricalEntity>()) })

        //WHEN
        localDataSource.deleteCity(aswan.toEntity())
        val cities = localDataSource.getCities().first()
        val cairoHistorical = localDataSource.getHistoricalData(cairo.id).first()

        //THEN
        assertThat(cities).isEqualTo(savedCities)
        assertThat(cities).doesNotContain(cairo)
        assertThat(cairoHistorical).isEmpty()
    }

    @Test(expected = Exception::class)
    fun `deleteCity() When exception is thrown Then Catch that exception`() = runBlocking() {
        //GIVEN
        Mockito.`when`(cityDao.delete(aswan.toEntity())).then { throw Exception() }

        //WHEN
        localDataSource.deleteCity(aswan.toEntity())
    }

    @Test(expected = Exception::class)
    fun `getCities() When exception is thrown Then Catch that exception`() = runBlocking() {
        //GIVEN
        Mockito.`when`(cityDao.getCities()).then { throw Exception() }

        //WHEN
        val cities = localDataSource.getCities().first()

        //THEN
        assertThat(cities).isEmpty()
    }
}