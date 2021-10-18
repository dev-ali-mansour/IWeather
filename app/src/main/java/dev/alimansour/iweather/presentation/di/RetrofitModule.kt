package dev.alimansour.iweather.presentation.di

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.alimansour.iweather.BuildConfig
import dev.alimansour.iweather.data.remote.WeatherAPIService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.DateFormat
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * WeatherApp Android Application developed by: Ali Mansour
 * ----------------- WeatherApp IS FREE SOFTWARE -------------------
 * https://www.alimansour.dev   |   mailto:dev.ali.mansour@gmail.com
 */
@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {
    private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"

    @Singleton
    @Provides
    fun providesHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
            redactHeader("Authorization")
            redactHeader("Cookie")
        }
    }

    @Singleton
    @Provides
    fun provideRequestInterceptor(): Interceptor {
        return Interceptor { chain ->

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
    }

    @Singleton
    @Provides
    fun providesOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        requestInterceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
            .retryOnConnectionFailure(true)
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(requestInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun providesGson(): Gson {
        return GsonBuilder()
            .enableComplexMapKeySerialization()
            .serializeNulls()
            .setDateFormat(DateFormat.LONG)
            .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
            .setPrettyPrinting()
            .setVersion(1.0)
            .create()
    }

    @Singleton
    @Provides
    fun providesRetrofitClient(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(WeatherAPIService::class.java)

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun provideWeatherAPIService(retrofit: Retrofit): WeatherAPIService {
        return retrofit.create(WeatherAPIService::class.java)
    }
}