package com.gimpel.pixabay.search.data.local

import com.gimpel.pixabay.search.domain.model.Hit

interface LocalHitDataSource {
    suspend fun insertAll(hits: List<Hit>, query: String)
    suspend fun getHit(id: Int): Hit
    suspend fun getHitsForQuery(query: String, perPage: Int = 20, page: Int = 0): List<Hit>
}