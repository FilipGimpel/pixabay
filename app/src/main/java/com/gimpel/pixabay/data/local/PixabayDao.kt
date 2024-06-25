package com.gimpel.pixabay.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface PixabayDao {

    @Query("SELECT * FROM hit WHERE tags IN (:tags)")
    suspend fun getHits(tags: List<String>): List<LocalHit>

    @Upsert
    suspend fun insertAll(products: List<LocalHit>)
}