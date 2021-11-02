package dev.alimansour.iweather

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import java.util.concurrent.TimeUnit

val interceptor = Interceptor { chain ->
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

fun enqueueMockResponse(fileName: String, server: MockWebServer) {
    val inputStream = server.javaClass.classLoader!!.getResourceAsStream(fileName)
    val source = inputStream.source().buffer()
    val mockResponse = MockResponse()
    mockResponse.setBody(source.readString(Charsets.UTF_8))
    server.enqueue(mockResponse)
}
