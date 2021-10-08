package dev.alimansour.planradarassessment.presentation.dp

import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.multidex.MultiDexApplication
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
import dev.alimansour.planradarassessment.presentation.cities.CitiesViewModel
import dev.alimansour.planradarassessment.presentation.cities.CitiesViewModelFactory
import dev.alimansour.planradarassessment.presentation.historical.HistoricalViewModel
import dev.alimansour.planradarassessment.presentation.historical.HistoricalViewModelFactory
import javax.inject.Singleton

/**
 * WeatherApp Android Application developed by: Ali Mansour
 * ----------------- WeatherApp IS FREE SOFTWARE -------------------
 * https://www.alimansour.dev   |   mailto:dev.ali.mansour@gmail.com
 */
@Module
object MainModule {


    @MainScope
    @Provides
    fun provideCitiesViewModel(
        activity: FragmentActivity,
        citiesViewModelFactory: CitiesViewModelFactory
    ): CitiesViewModel {
        return ViewModelProvider(activity, citiesViewModelFactory)
            .get(CitiesViewModel::class.java)
    }

    @MainScope
    @Provides
    fun provideHistoricalViewModelFactory(
        activity: FragmentActivity,
        historicalViewModelFactory: HistoricalViewModelFactory
    ): HistoricalViewModel {
        return ViewModelProvider(activity, historicalViewModelFactory)
            .get(HistoricalViewModel::class.java)
    }
}