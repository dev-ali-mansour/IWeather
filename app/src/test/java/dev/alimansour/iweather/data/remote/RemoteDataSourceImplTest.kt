package dev.alimansour.iweather.data.remote

import com.google.common.truth.Truth.assertThat
import dev.alimansour.iweather.BuildConfig
import dev.alimansour.iweather.data.remote.response.Main
import dev.alimansour.iweather.data.remote.response.WeatherData
import dev.alimansour.iweather.data.remote.response.WeatherItem
import dev.alimansour.iweather.data.remote.response.Wind
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
class RemoteDataSourceImplTest {
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

    private val okHttpClient = OkHttpClient.Builder()
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

    @Test
    fun `getHistoricalData() When send request Then request path is correct`() {
        runBlocking {
            //GIVEN
            val path = "/forecast?q=cairo&appid=ddbbb032375d79f1670080b110a8f45e&units=metric"

            //WHEN
            enqueueMockResponse()
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
            enqueueMockResponse()
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
            enqueueMockResponse()
            val responseBody = service.getHistoricalData("cairo").body()
            val historicalItems = responseBody!!.list
            val item = historicalItems!![0]

            //THEN
            assertThat(item).isEqualTo(data)
        }
    }

    private fun enqueueMockResponse() {
        val inputStream = javaClass.classLoader!!.getResourceAsStream(fileName)
        val source = inputStream.source().buffer()
        val mockResponse = MockResponse()
        mockResponse.setBody(source.readString(Charsets.UTF_8))
        server.enqueue(mockResponse)
    }
}