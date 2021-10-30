package dev.alimansour.iweather.data.local.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import dev.alimansour.iweather.TestUtil.TEST_CITY_ENTITY_LIST
import dev.alimansour.iweather.TestUtil.cairo
import dev.alimansour.iweather.TestUtil.giza
import dev.alimansour.iweather.TestUtil.luxor
import dev.alimansour.iweather.data.local.WeatherDatabase
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
        //WHEN
        TEST_CITY_ENTITY_LIST.forEach { city -> dao.insert(city) }
        val allCities = dao.getCities().first()

        //THEN
        assertThat(allCities).isNotNull()
        assertThat(allCities).isEqualTo(TEST_CITY_ENTITY_LIST)
    }

    @Test
    fun insertListOfCities_deleteCity_returnRightListOfCities() = runBlocking {
        //GIVEN
        val updatedList = listOf(cairo, luxor)

        //WHEN
        TEST_CITY_ENTITY_LIST.forEach { city -> dao.insert(city) }
        dao.delete(giza)
        val allCities = dao.getCities().first()

        //THEN
        assertThat(allCities).isEqualTo(updatedList)
    }
}