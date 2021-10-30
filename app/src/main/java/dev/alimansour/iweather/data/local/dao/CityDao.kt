package dev.alimansour.iweather.data.local.dao

import androidx.room.*
import dev.alimansour.iweather.data.local.entity.CityEntity
import kotlinx.coroutines.flow.Flow

/**
 * WeatherApp Android Application developed by: Ali Mansour
 * ----------------- WeatherApp IS FREE SOFTWARE -------------------
 * https://www.alimansour.dev   |   mailto:dev.ali.mansour@gmail.com
 */
@Dao
interface CityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(cityEntity: CityEntity)

    @Delete
    fun delete(cityEntity: CityEntity)

    @Query("SELECT * FROM cities")
    fun getCities(): Flow<List<CityEntity>>
}