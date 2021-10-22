package dev.alimansour.iweather.data.local.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import dev.alimansour.iweather.TEST_CITY_LIST
import dev.alimansour.iweather.cairo
import dev.alimansour.iweather.data.local.WeatherDatabase
import dev.alimansour.iweather.giza
import dev.alimansour.iweather.luxor
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
class CityDaoTest {

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
        TEST_CITY_LIST.forEach { city ->
            dao.insert(city)
        }

        val allCities = dao.getCities()

        assertThat(allCities).isNotNull()
        assertThat(allCities).isEqualTo(TEST_CITY_LIST)
    }

    @Test
    fun insertListOfCities_deleteCity_returnRightListOfCities() = runBlocking {
        //insert
        TEST_CITY_LIST.forEach { city ->
            dao.insert(city)
        }

        dao.delete(giza)
        val allCities = dao.getCities()
        val updatedList = listOf(cairo, luxor)

        assertThat(allCities).isEqualTo(updatedList)
    }
}