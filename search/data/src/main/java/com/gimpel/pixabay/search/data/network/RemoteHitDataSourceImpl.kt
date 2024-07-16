package com.gimpel.pixabay.search.data.network

import arrow.core.Either
import arrow.core.right
import com.gimpel.pixabay.search.data.toHit
import com.gimpel.pixabay.search.domain.model.Hit
import javax.inject.Inject

class RemoteHitDataSourceImpl @Inject constructor(
    private val pixabayService: PixabayService
) : RemoteHitDataSource {
    override suspend fun getHitsForQuery(query: String, perPage: Int, page: Int): Either<Throwable, List<Hit>> =
        when (val networkResult = pixabayService.get(query, page, perPage)) {
            is Either.Left -> networkResult
            is Either.Right -> networkResult.value.hits.toHit().sortedBy(Hit::id).right()
        }

}