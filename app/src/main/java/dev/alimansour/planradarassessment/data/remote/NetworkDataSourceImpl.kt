package dev.alimansour.planradarassessment.data.remote

import dev.alimansour.planradarassessment.data.remote.response.HistoricalResponse
import dev.alimansour.planradarassessment.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * WeatherApp Android Application developed by: Ali Mansour
 * ----------------- WeatherApp IS FREE SOFTWARE -------------------
 * https://www.alimansour.dev   |   mailto:dev.ali.mansour@gmail.com
 */
class NetworkDataSourceImpl(private val WeatherAPIService: WeatherAPIService) :
    NetworkDataSource {

    override suspend fun fetchHistoricalData(cityName: String): Resource<HistoricalResponse> {
        return runCatching {
            var resource: Resource<HistoricalResponse>

            withContext(Dispatchers.IO) {
                val response = WeatherAPIService.getHistoricalData(cityName)
                resource = when {
                    response.isSuccessful -> Resource.success(response.body())
                    else -> Resource.error(response.message(), response.code(), null)
                }
            }

            resource
        }.getOrElse { throwable ->
            throwable.message?.let {
                Resource.error(it, null, null)
            } ?: run {
                Resource.error("Server Error!", null, null)
            }
        }
    }
}