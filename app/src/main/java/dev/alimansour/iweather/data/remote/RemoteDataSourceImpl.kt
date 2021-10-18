package dev.alimansour.iweather.data.remote

import dev.alimansour.iweather.data.remote.response.HistoricalResponse
import retrofit2.Response

/**
 * WeatherApp Android Application developed by: Ali Mansour
 * ----------------- WeatherApp IS FREE SOFTWARE -------------------
 * https://www.alimansour.dev   |   mailto:dev.ali.mansour@gmail.com
 */
class RemoteDataSourceImpl(private val weatherAPIService: WeatherAPIService) : RemoteDataSource {

    override suspend fun fetchHistoricalData(cityName: String): Response<HistoricalResponse> {
        return weatherAPIService.getHistoricalData(cityName)
    }
}