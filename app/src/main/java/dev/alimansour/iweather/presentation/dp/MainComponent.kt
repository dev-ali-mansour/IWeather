package dev.alimansour.iweather.presentation.dp

import android.content.Context
import androidx.fragment.app.FragmentActivity
import dagger.BindsInstance
import dagger.Subcomponent
import dev.alimansour.iweather.presentation.MainActivity
import dev.alimansour.iweather.presentation.cities.CitiesFragment
import dev.alimansour.iweather.presentation.details.DetailsActivity
import dev.alimansour.iweather.presentation.historical.HistoricalFragment

/**
 * WeatherApp Android Application developed by: Ali Mansour
 * ----------------- WeatherApp IS FREE SOFTWARE -------------------
 * https://www.alimansour.dev   |   mailto:dev.ali.mansour@gmail.com
 */
@MainScope
@Subcomponent(modules = [MainModule::class])
interface MainComponent {


    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun context(@MainContext context: Context): Builder

        @BindsInstance
        fun activity(activity: FragmentActivity): Builder

        fun build(): MainComponent
    }

    // Classes that can be injected by this Component
    fun inject(mainActivity: MainActivity)
    fun inject(citiesFragment: CitiesFragment)
    fun inject(historicalFragment: HistoricalFragment)
    fun inject(detailsActivity: DetailsActivity)
}