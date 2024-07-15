package com.gimpel.pixabay.search.data.network

import arrow.core.Either
import retrofit2.http.GET
import retrofit2.http.Query

interface PixabayService {
    @GET("api")
    suspend fun get(
        @Query("q") searchQuery: String?,
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 20,
    ): Either<Throwable, PixabayResponse>
}