package dev.alimansour.iweather.data.remote

import dev.alimansour.iweather.data.remote.response.HistoricalResponse
import retrofit2.Response

/**
 * WeatherApp Android Application developed by: Ali Mansour
 * ----------------- WeatherApp IS FREE SOFTWARE -------------------
 * https://www.alimansour.dev   |   mailto:dev.ali.mansour@gmail.com
 */
interface RemoteDataSource {

    /**
     * Fetch historical weather data for specific city
     * @param cityName City Name
     * @return Resource<HistoricalResponse>
     */
    suspend fun fetchHistoricalData(cityName: String): Response<HistoricalResponse>
}