package dev.alimansour.planradarassessment.data.local.dao

import androidx.room.*
import dev.alimansour.planradarassessment.data.local.entity.City
import dev.alimansour.planradarassessment.data.local.entity.Historical

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