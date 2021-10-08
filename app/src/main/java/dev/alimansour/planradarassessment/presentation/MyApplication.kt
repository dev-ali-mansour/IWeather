package dev.alimansour.planradarassessment.presentation

import androidx.multidex.BuildConfig
import androidx.multidex.MultiDexApplication
import dev.alimansour.planradarassessment.presentation.dp.AppComponent
import dev.alimansour.planradarassessment.presentation.dp.DaggerAppComponent
import timber.log.Timber

/**
 * WeatherApp Android Application developed by: Ali Mansour
 * ----------------- WeatherApp IS FREE SOFTWARE -------------------
 * https://www.alimansour.dev   |   mailto:dev.ali.mansour@gmail.com
 */
open class MyApplication : MultiDexApplication() {

    // Instance of the AppComponent that will be used by all the Activities in the project
    val appComponent: AppComponent by lazy {
        initializeComponent()
    }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    open fun initializeComponent(): AppComponent {
        // Creates an instance of AppComponent using its Factory constructor
        // We pass the applicationContext that will be used as Context in the graph
        return DaggerAppComponent.builder().context(applicationContext).build()
    }
}