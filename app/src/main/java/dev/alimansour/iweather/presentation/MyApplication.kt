package dev.alimansour.iweather.presentation

import androidx.multidex.MultiDexApplication
import dagger.hilt.android.HiltAndroidApp
import dev.alimansour.iweather.BuildConfig
import timber.log.Timber

/**
 * WeatherApp Android Application developed by: Ali Mansour
 * ----------------- WeatherApp IS FREE SOFTWARE -------------------
 * https://www.alimansour.dev   |   mailto:dev.ali.mansour@gmail.com
 */

@HiltAndroidApp
class MyApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}