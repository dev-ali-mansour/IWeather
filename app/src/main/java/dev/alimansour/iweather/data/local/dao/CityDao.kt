package dev.alimansour.iweather.data.local.dao

import androidx.room.*
import dev.alimansour.iweather.data.local.entity.City

/**
 * WeatherApp Android Application developed by: Ali Mansour
 * ----------------- WeatherApp IS FREE SOFTWARE -------------------
 * https://www.alimansour.dev   |   mailto:dev.ali.mansour@gmail.com
 */
@Dao
interface CityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(city: City)

    @Delete
    fun delete(city: City)

    @Query("SELECT * FROM cities")
    fun getCities(): List<City>
}