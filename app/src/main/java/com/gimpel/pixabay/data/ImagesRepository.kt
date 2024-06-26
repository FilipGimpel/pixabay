package com.gimpel.pixabay.data

import com.gimpel.pixabay.data.network.Hit

interface ImagesRepository {
    suspend fun getHits(tags: List<String>): Result<List<Hit>>
    suspend fun getHit(id: Int): Hit
}