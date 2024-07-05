package com.gimpel.pixabay.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface PixabayDao {

    @Query("SELECT * FROM HitEntity WHERE hitId = :id")
    suspend fun getHit(id: Int): HitEntity

    @Query("SELECT * FROM HitEntity")
    suspend fun getAllHits(): List<HitEntity>

    @Upsert
    suspend fun insertAll(products: List<HitEntity>)

    @Upsert
    suspend fun insertQueryWithHit(tags: List<SearchQueryWithHitEntity>)

    @Query(
        """
        SELECT * FROM HitEntity 
        JOIN SearchQueryWithHitEntity ON HitEntity.hitId = SearchQueryWithHitEntity.hitId
        WHERE SearchQueryWithHitEntity.searchQuery = :query
        GROUP BY HitEntity.hitId
    """
    )
    suspend fun getHitsForQuery(query: String): List<HitEntity>
}