package com.gimpel.pixabay.search.domain.repository

import arrow.core.Either
import com.gimpel.pixabay.search.domain.model.Hit

interface ImagesRepository {
    suspend fun getHits(query: String, page: Int = 1, perPage: Int = 20): Either<Throwable, List<Hit>>
    suspend fun getHit(id: Int): Hit
}