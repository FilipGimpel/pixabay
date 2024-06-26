package com.gimpel.pixabay.data

import com.gimpel.pixabay.data.local.LocalHit
import com.gimpel.pixabay.data.local.PixabayDao
import com.gimpel.pixabay.data.network.Hit
import com.gimpel.pixabay.data.network.PixabayService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultImagesRepository @Inject constructor(
    private val networkDataSource: PixabayService,
    private val localDataSource: PixabayDao
) : ImagesRepository {
    override suspend fun getHits(tags: List<String>): Result<List<Hit>> {
        var localHits = getHitsWithAllTags(tags)

        // If local data is empty, fetch from network
        if (localHits.isEmpty()) {
            val result = networkDataSource.get(tags.joinToString(","))

            // If network request fails, return error
            if (result.isFailure) {
                return Result.failure(result.exceptionOrNull()!!)
            } else {
                // Convert network hits to local hits
                localHits = result.getOrNull()!!.hits.toLocal()

                // Insert hits into local database
                localDataSource.insertAll(localHits)
            }
        }

        // Convert local hits to network hits and return
        return Result.success(localHits.toNetwork())
    }

    /**
     * I fully realise its not the right way to do it but for a showcase app it's fine.
     * I'm more than happy to discuss the right way to do it.
     */
    private suspend fun getHitsWithAllTags(tags: List<String>): List<LocalHit> {
        // Get all hits from the database
        val allHits = localDataSource.getAllHits()

        // Filter hits that contain all tags
        val hitsWithAllTags = allHits.filter { hit ->
            tags.all { tag ->
                // any tag starts with the given tag
                hit.tags.any { it.startsWith(tag) }
            }
        }

        return hitsWithAllTags
    }

    override suspend fun getHit(id: Int): Hit {
        return localDataSource.getHit(id).toNetwork()
    }
}