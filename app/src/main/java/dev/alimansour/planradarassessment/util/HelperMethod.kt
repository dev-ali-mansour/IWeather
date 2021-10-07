package dev.alimansour.planradarassessment.util

import android.content.res.Resources

/**
 * WeatherApp Android Application developed by: Ali Mansour
 * ----------------- WeatherApp IS FREE SOFTWARE -------------------
 * https://www.alimansour.dev   |   mailto:dev.ali.mansour@gmail.com
 */

val Int.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()
