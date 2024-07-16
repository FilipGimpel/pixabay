package com.gimpel.pixabay.search.data

import arrow.core.Either
import arrow.core.right
import com.gimpel.pixabay.search.data.local.HitAndTagCrossRef
import com.gimpel.pixabay.search.data.local.LocalHitDataSource
import com.gimpel.pixabay.search.data.local.PixabayDao
import com.gimpel.pixabay.search.data.local.SearchQueryWithHitEntity
import com.gimpel.pixabay.search.data.local.TagEntity
import com.gimpel.pixabay.search.data.network.HitDTO
import com.gimpel.pixabay.search.data.network.PixabayService
import com.gimpel.pixabay.search.data.network.RemoteHitDataSource
import com.gimpel.pixabay.search.domain.model.Hit
import com.gimpel.pixabay.search.domain.repository.ImagesRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultImagesRepository @Inject constructor(
    private val localHitDataSource: LocalHitDataSource,
    private val remoteHitDataSource: RemoteHitDataSource
) : ImagesRepository {
    override suspend fun getHits(query: String, page: Int, perPage: Int): Either<Throwable, List<Hit>> {
        val localHits = localHitDataSource.getHitsForQuery(query, perPage, page)

        return if (localHits.isNotEmpty()) localHits.right()
            else {
            when (val networkResult = remoteHitDataSource.getHitsForQuery(query, perPage, page)) { // todo fold?
                    is Either.Left -> networkResult
                    is Either.Right -> {
                        // Insert hits into local database
                        localHitDataSource.insertAll(networkResult.value, query)

                        networkResult.value.right()
                    }
                }
            }
    }

    override suspend fun getHit(id: Int): Hit {
        return localHitDataSource.getHit(id)
    }
}