package dev.alimansour.iweather.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.alimansour.iweather.data.local.converters.Converters
import dev.alimansour.iweather.data.local.dao.CityDao
import dev.alimansour.iweather.data.local.dao.HistoricalDao
import dev.alimansour.iweather.data.local.entity.CityEntity
import dev.alimansour.iweather.data.local.entity.HistoricalEntity

/**
 * WeatherApp Android Application developed by: Ali Mansour
 * ----------------- WeatherApp IS FREE SOFTWARE -------------------
 * https://www.alimansour.dev   |   mailto:dev.ali.mansour@gmail.com
 */
@Database(entities = [CityEntity::class, HistoricalEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun cityDao(): CityDao
    abstract fun historicalDao(): HistoricalDao
}