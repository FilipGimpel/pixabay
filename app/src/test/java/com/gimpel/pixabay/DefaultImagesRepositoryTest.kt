package com.gimpel.pixabay

import com.gimpel.pixabay.search.data.DefaultImagesRepository
import com.gimpel.pixabay.search.data.local.HitEntity
import com.gimpel.pixabay.search.data.local.HitWithTags
import com.gimpel.pixabay.search.data.local.PixabayDao
import com.gimpel.pixabay.search.data.network.PixabayService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class DefaultImagesRepositoryTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val networkDataSource: com.gimpel.pixabay.search.data.network.PixabayService = mockk(relaxed = true)
    private val localDataSource: com.gimpel.pixabay.search.data.local.PixabayDao = mockk(relaxed = true)

    private val repository = com.gimpel.pixabay.search.data.DefaultImagesRepository(networkDataSource, localDataSource)

    private val mockHitEntity = com.gimpel.pixabay.search.data.local.HitEntity(
        -1,
        "",
        "",
        "",
        0,
        0,
        0
    )
    private val nonEmptyList = listOf(com.gimpel.pixabay.search.data.local.HitWithTags(mockHitEntity, emptyList()))


    @Test
    fun `should not fetch network results if database not empty`() = runTest {
        // given
        coEvery { localDataSource.getHitsWithTagsForQueryPaged("query", 1,1) } returns nonEmptyList

        // when
        repository.getHits("query", 1, 1)

        // then
        coVerify(exactly = 1) { localDataSource.getHitsWithTagsForQueryPaged("query", 1, 1) }
        coVerify(exactly = 0) { networkDataSource.get(any(), any(), any()) }
    }

    @Test
    fun `should fetch network results if database empty`() = runTest {
        // given
        coEvery { localDataSource.getHitsWithTagsForQueryPaged("query", 1,1) } returns emptyList()

        // when
        repository.getHits("query", 1, 1)

        // then
        coVerify(exactly = 1) { localDataSource.getHitsWithTagsForQueryPaged("query", 1, 1) }
        coVerify(exactly = 1) { networkDataSource.get("query", 1, 1) }
    }
}