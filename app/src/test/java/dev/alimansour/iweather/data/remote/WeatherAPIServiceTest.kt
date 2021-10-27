package dev.alimansour.iweather.data.remote

import com.google.common.truth.Truth.assertThat
import dev.alimansour.iweather.BuildConfig
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * WeatherApp Android Application developed by: Ali Mansour
 * ----------------- WeatherApp IS FREE SOFTWARE -------------------
 * https://www.alimansour.dev   |   mailto:dev.ali.mansour@gmail.com
 */
class WeatherAPIServiceTest {
    private lateinit var service: WeatherAPIService
    private lateinit var server: MockWebServer
    private val fileName = "historical_response.json"
    private val interceptor = Interceptor { chain ->
        val url = chain.request()
            .url
            .newBuilder()
            .addQueryParameter("appid", BuildConfig.API_KEY)
            .addQueryParameter("units", "metric")
            .build()
        val request = chain.request()
            .newBuilder()
            .url(url)
            .build()

        return@Interceptor chain.proceed(request)
    }

    val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(1, TimeUnit.MINUTES)
        .readTimeout(1, TimeUnit.MINUTES)
        .writeTimeout(1, TimeUnit.MINUTES)
        .retryOnConnectionFailure(true)
        .addInterceptor(interceptor)
        .build()

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
            enqueueMockResponse(fileName)
            service.getHistoricalData("cairo").body()
            val request = server.takeRequest()
            assertThat(request.path).isEqualTo("/forecast?q=cairo&appid=ddbbb032375d79f1670080b110a8f45e&units=metric")
        }
    }

    @Test
    fun getHistoricalData_receiveResponse_responseBodyIsNotNull() {
        runBlocking {
            enqueueMockResponse(fileName)
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