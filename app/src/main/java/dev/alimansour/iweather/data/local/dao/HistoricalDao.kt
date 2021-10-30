package dev.alimansour.iweather.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.alimansour.iweather.data.local.entity.HistoricalEntity
import kotlinx.coroutines.flow.Flow

/**
 * WeatherApp Android Application developed by: Ali Mansour
 * ----------------- WeatherApp IS FREE SOFTWARE -------------------
 * https://www.alimansour.dev   |   mailto:dev.ali.mansour@gmail.com
 */
@Dao
interface HistoricalDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(historicalEntityList: List<HistoricalEntity>)

    @Query("DELETE FROM historical_data")
    fun clearHistoricalData()

    @Query("DELETE FROM historical_data WHERE cityId = :cityId")
    fun clearCityHistoricalData(cityId: Int)

    @Query("SELECT * FROM historical_data WHERE cityId = :cityId")
    fun getHistoricalData(cityId: Int): Flow<List<HistoricalEntity>>
}