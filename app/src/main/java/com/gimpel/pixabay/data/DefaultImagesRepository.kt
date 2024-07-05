package com.gimpel.pixabay.data

import com.gimpel.pixabay.data.local.HitEntity
import com.gimpel.pixabay.data.local.PixabayDao
import com.gimpel.pixabay.data.local.SearchQueryWithHitEntity
import com.gimpel.pixabay.data.network.HitDTO
import com.gimpel.pixabay.data.network.PixabayService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultImagesRepository @Inject constructor(
    private val networkDataSource: PixabayService,
    private val localDataSource: PixabayDao
) : ImagesRepository {
    override suspend fun getHits(query: String): Result<List<HitDTO>> {
        var hits = getHitsForQuery(query).toDTO()

        // If local data is empty, fetch from network
        if (hits.isEmpty()) {
            val result = networkDataSource.get(query)

            // If network request fails, return error
            if (result.isFailure) {
                return Result.failure(result.exceptionOrNull()!!)
            } else {
                // Convert network hits to local hits
                hits = result.getOrNull()!!.hits

                // Insert hits into local database
                insertAll(hits, query)
            }
        }

        return Result.success(hits.sortedBy { it.id })
    }

    private suspend fun insertAll(hits: List<HitDTO>, query: String) {
        var hitEntities = hits.map { it.toEntity() }
        var hitWithTagEntities = hits.map { hit ->
            SearchQueryWithHitEntity(hitId = hit.id, searchQuery = query)
        }

        // Insert hits into local database
        localDataSource.insertAll(hitEntities)
        localDataSource.insertQueryWithHit(hitWithTagEntities)
    }

    private suspend fun getHitsForQuery(query : String): List<HitEntity> {
        return localDataSource.getHitsForQuery(query)
    }
    override suspend fun getHit(id: Int): HitDTO {
        return localDataSource.getHit(id).toDTO()
    }
}