package com.gimpel.pixabay.data

import com.gimpel.pixabay.data.network.Hit

interface ImagesRepository {
    suspend fun getHits(tags: List<String>): List<Hit>
    suspend fun getHit(id: Int): Hit
}