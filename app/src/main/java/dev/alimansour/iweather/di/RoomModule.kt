package dev.alimansour.iweather.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.alimansour.iweather.data.local.WeatherDatabase
import dev.alimansour.iweather.data.local.dao.CityDao
import dev.alimansour.iweather.data.local.dao.HistoricalDao
import javax.inject.Singleton

/**
 * WeatherApp Android Application developed by: Ali Mansour
 * ----------------- WeatherApp IS FREE SOFTWARE -------------------
 * https://www.alimansour.dev   |   mailto:dev.ali.mansour@gmail.com
 */
@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Singleton
    @Provides
    fun provideWeatherDatabase(@ApplicationContext context: Context): WeatherDatabase {
        return Room.databaseBuilder(
            context,
            WeatherDatabase::class.java,
            "weather.db"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideCityDao(db: WeatherDatabase): CityDao {
        return db.cityDao()
    }

    @Singleton
    @Provides
    fun provideHistoricalDao(db: WeatherDatabase): HistoricalDao {
        return db.historicalDao()
    }
}