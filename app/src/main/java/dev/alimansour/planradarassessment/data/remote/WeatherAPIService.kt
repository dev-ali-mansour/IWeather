package dev.alimansour.planradarassessment.data.remote

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dev.alimansour.planradarassessment.BuildConfig
import dev.alimansour.planradarassessment.data.remote.response.HistoricalResponse
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.text.DateFormat

/**
 * WeatherApp Android Application developed by: Ali Mansour
 * ----------------- WeatherApp IS FREE SOFTWARE -------------------
 * https://www.alimansour.dev   |   mailto:dev.ali.mansour@gmail.com
 */
private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
private const val API_KEY = BuildConfig.API_KEY

val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
    setLevel(HttpLoggingInterceptor.Level.BODY)
    redactHeader("Authorization")
    redactHeader("Cookie")
}
val requestInterceptor = Interceptor { chain ->

    val url = chain.request()
        .url
        .newBuilder()
        .addQueryParameter("appid", API_KEY)
        .addQueryParameter("units", "metric")
        .build()
    val request = chain.request()
        .newBuilder()
        .url(url)
        .build()

    return@Interceptor chain.proceed(request)
}

val okHttpClient = OkHttpClient.Builder()
    .addInterceptor(httpLoggingInterceptor)
    .addInterceptor(requestInterceptor)
    .build()

val gson: Gson = GsonBuilder()
    .enableComplexMapKeySerialization()
    .serializeNulls()
    .setDateFormat(DateFormat.LONG)
    .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
    .setPrettyPrinting()
    .setVersion(1.0)
    .create()


interface WeatherAPIService {

    @GET("forecast")
    suspend fun getHistoricalData(@Query("q") query: String): Response<HistoricalResponse>
}

object WeatherApi {
    val retrofitService: WeatherAPIService by lazy {
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(WeatherAPIService::class.java)
    }
}