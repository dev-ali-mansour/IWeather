package dev.alimansour.iweather.data.remote

import dev.alimansour.iweather.data.remote.response.HistoricalResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * WeatherApp Android Application developed by: Ali Mansour
 * ----------------- WeatherApp IS FREE SOFTWARE -------------------
 * https://www.alimansour.dev   |   mailto:dev.ali.mansour@gmail.com
 */
interface WeatherAPIService {

    @GET("forecast")
    suspend fun getHistoricalData(@Query("q") query: String): Response<HistoricalResponse>
}