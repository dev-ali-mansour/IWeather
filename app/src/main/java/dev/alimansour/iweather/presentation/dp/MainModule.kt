package dev.alimansour.iweather.presentation.dp

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
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