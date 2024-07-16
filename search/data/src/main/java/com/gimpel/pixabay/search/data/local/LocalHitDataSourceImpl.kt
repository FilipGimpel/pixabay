package com.gimpel.pixabay.search.data.local

import com.gimpel.pixabay.search.data.toEntity
import com.gimpel.pixabay.search.data.toHit
import com.gimpel.pixabay.search.domain.model.Hit
import javax.inject.Inject

class LocalHitDataSourceImpl @Inject constructor(
    private val pixabayDao: PixabayDao
) : LocalHitDataSource {
    override suspend fun insertAll(hits: List<Hit>, query: String) {
        // Insert Hits
        val hitEntities = hits.toEntity()
        pixabayDao.insertAll(hitEntities)

        // Insert SearchQueryWithHitEntity
        // One to many relationship between SearchQuery and Hit
        val hitWithSearchQueryEntities = hits.map { hit ->
            SearchQueryWithHitEntity(hitId = hit.id, searchQuery = query)
        }
        pixabayDao.insertQueryWithHit(hitWithSearchQueryEntities)

        // Insert Tags
        // Many to many relationship between Hit and Tag
        hits.forEach { hit ->
            hit.tags.forEach { tag ->
                pixabayDao.insertTag(TagEntity(tag))
                pixabayDao.insertHitAndTag(
                    HitAndTagCrossRef(
                        hitId = hit.id,
                        tagId = tag
                    )
                )
            }
        }
    }

    override suspend fun getHit(id: Int): Hit =
        pixabayDao.getHitWithTags(id).toHit()


    override suspend fun getHitsForQuery(query: String, perPage: Int, page: Int): List<Hit> =
        pixabayDao.getHitsWithTagsForQuery(query, perPage, (page-1) * perPage).toHit()

}