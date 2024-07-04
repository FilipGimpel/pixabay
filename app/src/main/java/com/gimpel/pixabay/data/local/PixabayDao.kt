package com.gimpel.pixabay.data.local

import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Query
import androidx.room.Relation
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
    suspend fun insertQueryWithHit(tags: List<QueryWithHitEntity>)

    @Query(
        """
        SELECT * FROM HitEntity 
        JOIN QueryWithHitEntity ON HitEntity.hitId = QueryWithHitEntity.hitId
        WHERE QueryWithHitEntity.query = :query
        GROUP BY HitEntity.hitId
    """
    )
    suspend fun getHitsForQuery(query: String): List<HitEntity>
}