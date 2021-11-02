package dev.alimansour.iweather.data.remote

import com.google.common.truth.Truth.assertThat
import dev.alimansour.iweather.data.remote.response.Main
import dev.alimansour.iweather.data.remote.response.WeatherData
import dev.alimansour.iweather.data.remote.response.WeatherItem
import dev.alimansour.iweather.data.remote.response.Wind
import dev.alimansour.iweather.enqueueMockResponse
import dev.alimansour.iweather.okHttpClient
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * WeatherApp Android Application developed by: Ali Mansour
 * ----------------- WeatherApp IS FREE SOFTWARE -------------------
 * https://www.alimansour.dev   |   mailto:dev.ali.mansour@gmail.com
 */
class RemoteDataSourceImplTest {
    private lateinit var service: WeatherAPIService
    private lateinit var server: MockWebServer
    private val fileName = "historical_response.json"

    @Before
    fun setUp() {
        server = MockWebServer()
        service = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(server.url(""))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherAPIService::class.java)
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun `getHistoricalData() When send request Then request path is correct`() {
        runBlocking {
            //GIVEN
            val path = "/forecast?q=cairo&appid=ddbbb032375d79f1670080b110a8f45e&units=metric"

            //WHEN
            enqueueMockResponse(fileName,server)
            service.getHistoricalData("cairo").body()
            val request = server.takeRequest()

            //THEN
            assertThat(request.path).isEqualTo(path)
        }
    }

    @Test
    fun `getHistoricalData() When receiveResponse then response body is not null`() {
        runBlocking {
            //WHEN
            enqueueMockResponse(fileName,server)
            val responseBody = service.getHistoricalData("cairo").body()

            //THEN
            assertThat(responseBody).isNotNull()
        }
    }

    @Test
    fun `getHistoricalDate() When receive response Then the response is correct`() {
        runBlocking {
            //GIVEN
            val data = WeatherData(
                date = "2021-10-18 18:00:00",
                weather = listOf(WeatherItem(icon = "03n", description = "scattered clouds")),
                main = Main(temp = 25.3, humidity = 47.0),
                wind = Wind(speed = 7.2)
            )

            //WHEN
            enqueueMockResponse(fileName,server)
            val responseBody = service.getHistoricalData("cairo").body()
            val historicalItems = responseBody!!.list
            val item = historicalItems!![0]

            //THEN
            assertThat(item).isEqualTo(data)
        }
    }
}