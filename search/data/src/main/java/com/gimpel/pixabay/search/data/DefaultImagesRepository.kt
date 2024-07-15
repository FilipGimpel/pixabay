package com.gimpel.pixabay.search.data

import arrow.core.Either
import arrow.core.right
import com.gimpel.pixabay.search.data.local.HitAndTagCrossRef
import com.gimpel.pixabay.search.data.local.PixabayDao
import com.gimpel.pixabay.search.data.local.SearchQueryWithHitEntity
import com.gimpel.pixabay.search.data.local.TagEntity
import com.gimpel.pixabay.search.data.network.HitDTO
import com.gimpel.pixabay.search.data.network.PixabayService
import com.gimpel.pixabay.search.domain.model.Hit
import com.gimpel.pixabay.search.domain.repository.ImagesRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultImagesRepository @Inject constructor(
    private val networkDataSource: PixabayService,
    private val localDataSource: PixabayDao
) : ImagesRepository {
    override suspend fun getHits(query: String, page: Int, perPage: Int): Either<Throwable, List<Hit>> {
        val localHits = localDataSource.getHitsWithTagsForQueryPaged(query, perPage, page).toHit()

        return if (localHits.isNotEmpty()) localHits.sortedBy { it.id }.right()
            else {
            when (val networkResult = networkDataSource.get(query, page, perPage)) { // todo fold?
                    is Either.Left -> networkResult
                    is Either.Right -> {
                        // Insert hits into local database
                        insertAll(networkResult.value.hits, query)

                        // Convert network hits to model
                        networkResult.value.hits.map { it.toModel() }.right()
                    }
                }
            }
    }

    private suspend fun insertAll(hits: List<HitDTO>, query: String) {
        // Insert Hits
        val hitEntities = hits.map { it.toEntity() }
        localDataSource.insertAll(hitEntities)

        // Insert SearchQueryWithHitEntity
        // One to many relationship between SearchQuery and Hit
        val hitWithSearchQueryEntities = hits.map { hit ->
            SearchQueryWithHitEntity(hitId = hit.id, searchQuery = query)
        }
        localDataSource.insertQueryWithHit(hitWithSearchQueryEntities)

        // Insert Tags
        // Many to many relationship between Hit and Tag
        hits.forEach { hit ->
            hit.tags.forEach { tag ->
                localDataSource.insertTag(TagEntity(tag))
                localDataSource.insertHitAndTag(
                    HitAndTagCrossRef(
                        hitId = hit.id,
                        tagId = tag
                    )
                )
            }
        }
    }

    override suspend fun getHit(id: Int): Hit {
        return localDataSource.getHitWithTags(id).toHit()
    }
}