package com.gimpel.pixabay.search.data.network

import arrow.core.Either
import com.gimpel.pixabay.search.data.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton
import com.skydoves.retrofit.adapters.arrow.EitherCallAdapterFactory


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
        .addCallAdapterFactory(EitherCallAdapterFactory.create())
        .build()
        .create(PixabayService::class.java)

    override suspend fun get(searchQuery: String?, page: Int, perPage: Int): Either<Throwable, PixabayResponse> {
        return pixabayApi.get(searchQuery, page, perPage)
    }
}