package com.gimpel.pixabay.data

import com.gimpel.pixabay.model.Hit

interface ImagesRepository {
    suspend fun getHits(query: String): Result<List<Hit>>
    suspend fun getHit(id: Int): Hit
}