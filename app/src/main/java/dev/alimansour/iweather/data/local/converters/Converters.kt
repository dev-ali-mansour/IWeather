package dev.alimansour.iweather.data.local.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * WeatherApp Android Application developed by: Ali Mansour
 * ----------------- WeatherApp IS FREE SOFTWARE -------------------
 * https://www.alimansour.dev   |   mailto:dev.ali.mansour@gmail.com
 */
class Converters {
    @TypeConverter
    fun restoreList(listOfString: String?): List<String?>? =
        Gson().fromJson(listOfString, object : TypeToken<List<String?>?>() {}.type)

    @TypeConverter
    fun saveList(listOfString: List<String?>?): String? = Gson().toJson(listOfString)
}