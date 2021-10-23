package dev.alimansour.iweather.data.remote

import com.google.common.truth.Truth.assertThat
import dev.alimansour.iweather.okHttpClient
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
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
class WeatherAPIServiceTest {
    private lateinit var service: WeatherAPIService
    private lateinit var server: MockWebServer

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

    private fun enqueueMockResponse(fileName: String) {
        val inputStream = javaClass.classLoader!!.getResourceAsStream(fileName)
        val source = inputStream.source().buffer()
        val mockResponse = MockResponse()
        mockResponse.setBody(source.readString(Charsets.UTF_8))
        server.enqueue(mockResponse)
    }

    @Test
    fun getHistoricalData_sentRequest_requestPathIsCorrect() {
        runBlocking {
            enqueueMockResponse("historical_response.json")
            service.getHistoricalData("cairo").body()
            val request = server.takeRequest()
            assertThat(request.path).isEqualTo("/forecast?q=cairo&appid=ddbbb032375d79f1670080b110a8f45e&units=metric")
        }
    }

    @Test
    fun getHistoricalData_receiveResponse_responseBodyIsNotNull() {
        runBlocking {
            enqueueMockResponse("historical_response.json")
            val responseBody = service.getHistoricalData("cairo").body()
            assertThat(responseBody).isNotNull()
        }
    }

    @Test
    fun getHistoricalDate_receivedResponse_contentIsCorrect() {
        runBlocking {
            enqueueMockResponse("historical_response.json")
            val responseBody = service.getHistoricalData("cairo").body()
            val historicalItems = responseBody!!.list
            val item = historicalItems!![0]
            assertThat(item.date).isEqualTo("2021-10-18 18:00:00")
            assertThat(item.weather[0].description).isEqualTo("scattered clouds")
            assertThat(item.main.temp).isEqualTo(25.3)
            assertThat(item.main.humidity).isEqualTo(47)
            assertThat(item.wind.speed).isEqualTo(7.2)
        }
    }
}