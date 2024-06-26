package com.gimpel.pixabay.data.network

import com.gimpel.pixabay.BuildConfig
import com.skydoves.retrofit.adapters.result.ResultCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class DefaultPixabayService @Inject constructor(
    converterFactory: Converter.Factory
) : PixabayService {

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val apiKeyInterceptor = Interceptor { chain ->
        val originalRequest = chain.request()
        val originalUrl = originalRequest.url
        val newUrl = originalUrl.newBuilder()
            .addQueryParameter("key", BuildConfig.PIXABAY_API_KEY)
            .build()
        val newRequest = originalRequest.newBuilder()
            .url(newUrl)
            .build()
        chain.proceed(newRequest)
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(apiKeyInterceptor)
        .build()

    private val pixabayApi = Retrofit.Builder()
        .baseUrl(BuildConfig.PIXABAY_API_URL)
        .client(client)
        .addConverterFactory(converterFactory)
        .addCallAdapterFactory(ResultCallAdapterFactory.create())
        .build()
        .create(PixabayService::class.java)

    override suspend fun get(searchQuery: String?): Result<PixabayResponse> {
        return pixabayApi.get(searchQuery)
    }
}