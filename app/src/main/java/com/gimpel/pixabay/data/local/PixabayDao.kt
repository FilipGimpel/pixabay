package com.gimpel.pixabay.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert

@Dao
interface PixabayDao {
    @Upsert
    suspend fun insertAll(products: List<HitEntity>)

    @Upsert
    suspend fun insertQueryWithHit(tags: List<SearchQueryWithHitEntity>)

    @Upsert
    suspend fun insertTag(tag: TagEntity)

    @Upsert
    suspend fun insertHitAndTag(hitAndTag: HitAndTagCrossRef)

    @Query("SELECT * FROM HitEntity WHERE hitId = :id")
    suspend fun getHitWithTags(id: Int): HitWithTags

    @Transaction
    @Query(
        """
        SELECT HitEntity.* FROM HitEntity 
        JOIN SearchQueryWithHitEntity ON HitEntity.hitId = SearchQueryWithHitEntity.hitId
        WHERE SearchQueryWithHitEntity.searchQuery = :query
        GROUP BY HitEntity.hitId
        LIMIT :perPage OFFSET :offset
    """
    )
    suspend fun getHitsWithTagsForQuery(query: String, perPage: Int = 20, offset: Int = 0): List<HitWithTags>

    suspend fun getHitsWithTagsForQueryPaged(query: String, perPage: Int = 20, page: Int = 0) =
        getHitsWithTagsForQuery(query, perPage, (page-1) * perPage)
}