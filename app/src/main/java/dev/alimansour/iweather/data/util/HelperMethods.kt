package dev.alimansour.iweather.data.util

import dev.alimansour.iweather.util.Resource
import retrofit2.Response
/**
 * WeatherApp Android Application developed by: Ali Mansour
 * ----------------- WeatherApp IS FREE SOFTWARE -------------------
 * https://www.alimansour.dev   |   mailto:dev.ali.mansour@gmail.com
 */


/**
 * Convert Retrofit response to Resource
 * @return Resource of data
 */
fun <T> Response<T>.toResource(): Resource<T> {
    if (this.isSuccessful) {
        this.body()?.let { result ->
            return Resource.Success(result)
        }
    }
    return Resource.Error(this.message())
}