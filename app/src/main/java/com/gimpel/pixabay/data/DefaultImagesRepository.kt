package com.gimpel.pixabay.data

import com.gimpel.pixabay.data.local.HitAndTagCrossRef
import com.gimpel.pixabay.data.local.PixabayDao
import com.gimpel.pixabay.data.local.SearchQueryWithHitEntity
import com.gimpel.pixabay.data.local.TagEntity
import com.gimpel.pixabay.data.network.HitDTO
import com.gimpel.pixabay.data.network.PixabayService
import com.gimpel.pixabay.model.Hit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultImagesRepository @Inject constructor(
    private val networkDataSource: PixabayService,
    private val localDataSource: PixabayDao
) : ImagesRepository {
    override suspend fun getHits(query: String, page: Int, perPage: Int): Result<List<Hit>> {
        var hits = localDataSource.getHitsWithTagsForQuery(query, perPage, (page-1) * perPage).toHit()

        // If local data is empty, fetch from network
        if (hits.isEmpty()) {
            val networkResult = networkDataSource.get(query, page, perPage)

            // If network request fails, return error
            if (networkResult.isFailure) {
                return Result.failure(networkResult.exceptionOrNull()!!)
            } else {
                // Convert network hits to local hits
                val networkHits = networkResult.getOrNull()!!.hits

                // Insert hits into local database
                insertAll(networkHits, query)

                // Convert network hits to model
                hits = networkHits.map { it.toModel() }
            }
        }

        return Result.success(hits.sortedBy { it.id })
    }


    private suspend fun insertAll(hits: List<HitDTO>, query: String) {
        // Insert Hits
        var hitEntities = hits.map { it.toEntity() }
        localDataSource.insertAll(hitEntities)

        // Insert SearchQueryWithHitEntity
        // One to many relationship between SearchQuery and Hit
        var hitWithSearchQueryEntities = hits.map { hit ->
            SearchQueryWithHitEntity(hitId = hit.id, searchQuery = query)
        }
        localDataSource.insertQueryWithHit(hitWithSearchQueryEntities)

        // Insert Tags
        // Many to many relationship between Hit and Tag
        hits.forEach { hit ->
            hit.tags.forEach { tag ->
                localDataSource.insertTag(TagEntity(tag))
                localDataSource.insertHitAndTag(HitAndTagCrossRef(hitId = hit.id, tagId = tag))
            }
        }
    }

    override suspend fun getHit(id: Int): Hit {
        return localDataSource.getHitWithTags(id).toHit()
    }
}