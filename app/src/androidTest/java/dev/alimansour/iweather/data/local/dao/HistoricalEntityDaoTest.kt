package dev.alimansour.iweather.data.local.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import dev.alimansour.iweather.TestUtil.TEST_CITY_LIST
import dev.alimansour.iweather.TestUtil.TEST_HISTORICAL_LIST
import dev.alimansour.iweather.TestUtil.TEST_UPDATED_HISTORICAL_LIST
import dev.alimansour.iweather.TestUtil.cairo
import dev.alimansour.iweather.TestUtil.giza
import dev.alimansour.iweather.TestUtil.luxor
import dev.alimansour.iweather.data.local.WeatherDatabase
import dev.alimansour.iweather.data.local.entity.toEntity
import kotlinx.coroutines.flow.first
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
class HistoricalEntityDaoTest {

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
        //GIVEN
        val cairoList = TEST_HISTORICAL_LIST.filter { it.city.id == cairo.id }.map { it.toEntity() }
        val gizaList = TEST_HISTORICAL_LIST.filter { it.city.id == giza.id }.map { it.toEntity() }

        //WHEN
        dao.insertList(TEST_HISTORICAL_LIST.map { it.toEntity() })
        val cairoHistorical = dao.getHistoricalData(cairo.id).first()
        val gizaHistorical = dao.getHistoricalData(giza.id).first()
        val luxorHistorical = dao.getHistoricalData(luxor.id).first()

        //THEN
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
            //WHEN
            dao.insertList(TEST_HISTORICAL_LIST.map { it.toEntity() })
            dao.clearHistoricalData()

            //THEN
            TEST_CITY_LIST.forEach { city ->
                val historical = dao.getHistoricalData(city.id).first()
                assertThat(historical).isEmpty()
            }
        }

    @Test
    fun insertListOfHistoricalData_clearCityHistoricalData_returnEmptyHistoricalForThisCity() =
        runBlocking {
            //WHEN
            dao.insertList(TEST_HISTORICAL_LIST.map { it.toEntity() })
            dao.clearCityHistoricalData(cairo.id)
            val historical = dao.getHistoricalData(cairo.id).first()

            //THEN
            assertThat(historical).isEmpty()
        }

    @Test
    fun insertListOfHistoricalData_updateHistoricalData_returnUpdatedHistorical() =
        runBlocking {
            //GIVEN
            val cairoList =
                TEST_UPDATED_HISTORICAL_LIST.filter { it.city.id == cairo.id }.map { it.toEntity() }
            val gizaList =
                TEST_UPDATED_HISTORICAL_LIST.filter { it.city.id == giza.id }.map { it.toEntity() }
            val luxorList =
                TEST_UPDATED_HISTORICAL_LIST.filter { it.city.id == luxor.id }.map { it.toEntity() }

            //WHEN
            dao.insertList(TEST_HISTORICAL_LIST.map { it.toEntity() })
            dao.clearHistoricalData()
            dao.insertList(TEST_UPDATED_HISTORICAL_LIST.map { it.toEntity() })
            val cairoHistorical = dao.getHistoricalData(cairo.id).first()
            val gizaHistorical = dao.getHistoricalData(giza.id).first()
            val luxorHistorical = dao.getHistoricalData(luxor.id).first()

            //THEN
            assertThat(cairoHistorical).isNotNull()
            assertThat(gizaHistorical).isNotNull()
            assertThat(luxorHistorical).isNotNull()
            assertThat(cairoHistorical).isEqualTo(cairoList)
            assertThat(gizaHistorical).isEqualTo(gizaList)
            assertThat(luxorHistorical).isEqualTo(luxorList)
        }
}