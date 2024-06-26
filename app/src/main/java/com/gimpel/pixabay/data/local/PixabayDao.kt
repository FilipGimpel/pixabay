package com.gimpel.pixabay.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface PixabayDao {

    @Query("SELECT * FROM hit WHERE id = :id")
    suspend fun getHit(id: Int): LocalHit

    @Query("SELECT * FROM hit")
    suspend fun getAllHits(): List<LocalHit>

    @Upsert
    suspend fun insertAll(products: List<LocalHit>)
}