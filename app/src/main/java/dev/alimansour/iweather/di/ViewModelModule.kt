package dev.alimansour.iweather.di

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dev.alimansour.iweather.presentation.cities.CitiesViewModel
import dev.alimansour.iweather.presentation.cities.CitiesViewModelFactory
import dev.alimansour.iweather.presentation.historical.HistoricalViewModel
import dev.alimansour.iweather.presentation.historical.HistoricalViewModelFactory

/**
 * WeatherApp Android Application developed by: Ali Mansour
 * ----------------- WeatherApp IS FREE SOFTWARE -------------------
 * https://www.alimansour.dev   |   mailto:dev.ali.mansour@gmail.com
 */
@Module
@InstallIn(ActivityComponent::class)
object ViewModelModule {

    @Provides
    fun provideCitiesViewModel(
        activity: FragmentActivity,
        citiesViewModelFactory: CitiesViewModelFactory
    ): CitiesViewModel {
        return ViewModelProvider(activity, citiesViewModelFactory)
            .get(CitiesViewModel::class.java)
    }

    @Provides
    fun provideHistoricalViewModel(
        activity: FragmentActivity,
        historicalViewModelFactory: HistoricalViewModelFactory
    ): HistoricalViewModel {
        return ViewModelProvider(activity, historicalViewModelFactory)
            .get(HistoricalViewModel::class.java)
    }
}