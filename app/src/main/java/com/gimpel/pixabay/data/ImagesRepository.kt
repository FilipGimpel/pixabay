package com.gimpel.pixabay.data

import com.gimpel.pixabay.data.network.HitDTO

interface ImagesRepository {
    suspend fun getHits(query: String): Result<List<HitDTO>>
    suspend fun getHit(id: Int): HitDTO
}