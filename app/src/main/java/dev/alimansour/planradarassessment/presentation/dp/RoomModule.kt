package dev.alimansour.planradarassessment.presentation.dp

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dev.alimansour.planradarassessment.data.local.WeatherDatabase
import dev.alimansour.planradarassessment.data.local.dao.CityDao
import dev.alimansour.planradarassessment.data.local.dao.HistoricalDao
import javax.inject.Singleton

/**
 * WeatherApp Android Application developed by: Ali Mansour
 * ----------------- WeatherApp IS FREE SOFTWARE -------------------
 * https://www.alimansour.dev   |   mailto:dev.ali.mansour@gmail.com
 */
@Module
object RoomModule {

    @Singleton
    @Provides
    fun provideWeatherDatabase(@AppContext context: Context): WeatherDatabase {
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