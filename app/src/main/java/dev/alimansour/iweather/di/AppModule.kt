package dev.alimansour.iweather.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.alimansour.iweather.data.local.LocalDataSource
import dev.alimansour.iweather.data.local.LocalDataSourceImpl
import dev.alimansour.iweather.data.local.dao.CityDao
import dev.alimansour.iweather.data.local.dao.HistoricalDao
import dev.alimansour.iweather.data.remote.RemoteDataSource
import dev.alimansour.iweather.data.remote.RemoteDataSourceImpl
import dev.alimansour.iweather.data.remote.WeatherAPIService
import dev.alimansour.iweather.data.repository.WeatherRepositoryImpl
import dev.alimansour.iweather.domain.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

/**
 * WeatherApp Android Application developed by: Ali Mansour
 * ----------------- WeatherApp IS FREE SOFTWARE -------------------
 * https://www.alimansour.dev   |   mailto:dev.ali.mansour@gmail.com
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRemoteDataSource(weatherAPIService: WeatherAPIService): RemoteDataSource =
        RemoteDataSourceImpl(weatherAPIService)

    @Singleton
    @Provides
    fun provideLocalDataSource(cityDao: CityDao, historicalDao: HistoricalDao): LocalDataSource =
        LocalDataSourceImpl(cityDao, historicalDao)

    @Singleton
    @Provides
    fun provideWeatherRepository(
        remoteDataSource: RemoteDataSource,
        localDataSource: LocalDataSource,
    ): WeatherRepository = WeatherRepositoryImpl(remoteDataSource, localDataSource)

    @Singleton
    @Provides
    fun provideCoroutineDispatcher() = Dispatchers.IO
}