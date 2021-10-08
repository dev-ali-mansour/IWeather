package dev.alimansour.planradarassessment.presentation.dp

import dagger.Module
import dagger.Provides
import dev.alimansour.planradarassessment.data.local.LocalDataSource
import dev.alimansour.planradarassessment.data.local.LocalDataSourceImpl
import dev.alimansour.planradarassessment.data.local.WeatherDatabase
import dev.alimansour.planradarassessment.data.remote.RemoteDataSource
import dev.alimansour.planradarassessment.data.remote.RemoteDataSourceImpl
import dev.alimansour.planradarassessment.data.remote.WeatherAPIService
import dev.alimansour.planradarassessment.data.repository.WeatherRepositoryImpl
import dev.alimansour.planradarassessment.domain.repository.WeatherRepository
import javax.inject.Singleton

/**
 * WeatherApp Android Application developed by: Ali Mansour
 * ----------------- WeatherApp IS FREE SOFTWARE -------------------
 * https://www.alimansour.dev   |   mailto:dev.ali.mansour@gmail.com
 */
@Module(subcomponents = [MainComponent::class])
object AppModule {

    @Singleton
    @Provides
    fun provideRemoteDataSource(weatherAPIService: WeatherAPIService): RemoteDataSource {
        return RemoteDataSourceImpl(weatherAPIService)
    }

    @Singleton
    @Provides
    fun provideLocalDataSource(weatherDatabase: WeatherDatabase): LocalDataSource {
        return LocalDataSourceImpl(weatherDatabase)
    }

    @Singleton
    @Provides
    fun provideWeatherRepository(
        remoteDataSource: RemoteDataSource,
        localDataSource: LocalDataSource
    ): WeatherRepository {
        return WeatherRepositoryImpl(remoteDataSource, localDataSource)
    }


    /*@Singleton
    @Provides
    fun provideCountriesAdapter(@AppContext context: Context): CountriesAdapter {
        return CountriesAdapter(context, android.R.layout.simple_list_item_1)
    }*/

}