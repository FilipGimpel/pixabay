package com.gimpel.pixabay.data.network

import retrofit2.http.GET
import retrofit2.http.Query


interface PixabayService {
    @GET("api")
    suspend fun get(@Query("q") searchQuery: String?): PixabayResponse
}