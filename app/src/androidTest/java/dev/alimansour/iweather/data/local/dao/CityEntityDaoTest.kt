package dev.alimansour.iweather.data.local.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import dev.alimansour.iweather.TestUtil.TEST_CITY_LIST
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
class CityEntityDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    private lateinit var dao: CityDao
    private lateinit var database: WeatherDatabase

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WeatherDatabase::class.java
        ).build()
        dao = database.cityDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertCitiesThenGetCities_returnListOfInsertedCities() = runBlocking {
        //GIVEN
        val list = TEST_CITY_LIST.map { it.toEntity() }

        //WHEN
        TEST_CITY_LIST.forEach { city -> dao.insert(city.toEntity()) }
        val allCities = dao.getCities().first()

        //THEN
        assertThat(allCities).isNotNull()
        assertThat(allCities).isEqualTo(list)
    }

    @Test
    fun insertListOfCities_deleteCity_returnRightListOfCities() = runBlocking {
        //GIVEN
        val updatedList = listOf(cairo, luxor).map { it.toEntity() }

        //WHEN
        TEST_CITY_LIST.forEach { city -> dao.insert(city.toEntity()) }
        dao.delete(giza.toEntity())
        val allCities = dao.getCities().first()

        //THEN
        assertThat(allCities).isEqualTo(updatedList)
    }
}