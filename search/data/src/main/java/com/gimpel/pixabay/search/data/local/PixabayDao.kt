package com.gimpel.pixabay.search.data.local

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

    @Transaction
    @Query("SELECT * FROM HitEntity WHERE hitId = :id")
    suspend fun getHitWithTags(id: Int): HitWithTags

    @Transaction
    @Query(
        """
        SELECT HitEntity.* FROM HitEntity 
        JOIN SearchQueryWithHitEntity ON HitEntity.hitId = SearchQueryWithHitEntity.hitId
        WHERE SearchQueryWithHitEntity.searchQuery = :query
        GROUP BY HitEntity.hitId
        ORDER BY HitEntity.hitId ASC
        LIMIT :perPage OFFSET :offset
    """
    )
    suspend fun getHitsWithTagsForQuery(query: String, perPage: Int = 20, offset: Int = 0): List<HitWithTags>
}