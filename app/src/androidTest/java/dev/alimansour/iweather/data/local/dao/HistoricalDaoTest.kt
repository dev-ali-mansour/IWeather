package dev.alimansour.iweather.data.local.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import dev.alimansour.iweather.*
import dev.alimansour.iweather.TestUtil.TEST_CITY_LIST
import dev.alimansour.iweather.TestUtil.TEST_HISTORICAL_LIST
import dev.alimansour.iweather.TestUtil.TEST_UPDATED_HISTORICAL_LIST
import dev.alimansour.iweather.TestUtil.cairo
import dev.alimansour.iweather.TestUtil.giza
import dev.alimansour.iweather.TestUtil.luxor
import dev.alimansour.iweather.data.local.WeatherDatabase
import dev.alimansour.iweather.data.local.entity.City
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 * WeatherApp Android Application developed by: Ali Mansour
 * ----------------- WeatherApp IS FREE SOFTWARE -------------------
 * https://www.alimansour.dev   |   mailto:dev.ali.mansour@gmail.com
 */
@RunWith(AndroidJUnit4::class)
class HistoricalDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    private lateinit var dao: HistoricalDao
    private lateinit var database: WeatherDatabase

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WeatherDatabase::class.java
        ).build()
        dao = database.historicalDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertListOfHistoricalData_returnListOfInsertedHistoricalData() = runBlocking {
        dao.insertList(TEST_HISTORICAL_LIST)

        val cairoHistorical = dao.getHistoricalData(cairo.cityId)
        val gizaHistorical = dao.getHistoricalData(giza.cityId)
        val luxorHistorical = dao.getHistoricalData(luxor.cityId)

        val cairoList = TEST_HISTORICAL_LIST.filter { it.city.cityId == cairo.cityId }
        val gizaList = TEST_HISTORICAL_LIST.filter { it.city.cityId == giza.cityId }

        assertThat(cairoHistorical).isNotNull()
        assertThat(gizaHistorical).isNotNull()
        assertThat(luxorHistorical).isNotNull()
        assertThat(cairoHistorical).isEqualTo(cairoList)
        assertThat(gizaHistorical).isEqualTo(gizaList)
        assertThat(luxorHistorical).isEmpty()
    }

    @Test
    fun insertListOfHistoricalData_clearAllHistoricalData_returnEmptyHistoricalForAnyCity() =
        runBlocking {
            dao.insertList(TEST_HISTORICAL_LIST)
            dao.clearHistoricalData()

            TEST_CITY_LIST.forEach { city ->
                val historical = dao.getHistoricalData(city.cityId)
                assertThat(historical).isEmpty()
            }
        }

    @Test
    fun insertListOfHistoricalData_clearCityHistoricalData_returnEmptyHistoricalForThisCity() =
        runBlocking {
            dao.insertList(TEST_HISTORICAL_LIST)
            dao.clearCityHistoricalData(cairo.cityId)

            val historical = dao.getHistoricalData(cairo.cityId)
            assertThat(historical).isEmpty()
        }

    @Test
    fun insertListOfHistoricalData_updateHistoricalData_returnUpdatedHistorical() =
        runBlocking {

            dao.insertList(TEST_HISTORICAL_LIST)
            dao.clearHistoricalData()

            dao.insertList(TEST_UPDATED_HISTORICAL_LIST)

            val cairoHistorical = dao.getHistoricalData(cairo.cityId)
            val gizaHistorical = dao.getHistoricalData(giza.cityId)
            val luxorHistorical = dao.getHistoricalData(luxor.cityId)

            val cairoList = TEST_UPDATED_HISTORICAL_LIST.filter { it.city.cityId == cairo.cityId }
            val gizaList = TEST_UPDATED_HISTORICAL_LIST.filter { it.city.cityId == giza.cityId }
            val luxorList = TEST_UPDATED_HISTORICAL_LIST.filter { it.city.cityId == luxor.cityId }

            assertThat(cairoHistorical).isNotNull()
            assertThat(gizaHistorical).isNotNull()
            assertThat(luxorHistorical).isNotNull()
            assertThat(cairoHistorical).isEqualTo(cairoList)
            assertThat(gizaHistorical).isEqualTo(gizaList)
            assertThat(luxorHistorical).isEqualTo(luxorList)
        }
}