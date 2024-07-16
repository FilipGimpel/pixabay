package com.gimpel.pixabay.search.data.network

import arrow.core.Either
import com.gimpel.pixabay.search.domain.model.Hit

interface RemoteHitDataSource {
    suspend fun getHitsForQuery(query: String, perPage: Int = 20, page: Int = 0): Either<Throwable, List<Hit>>
}