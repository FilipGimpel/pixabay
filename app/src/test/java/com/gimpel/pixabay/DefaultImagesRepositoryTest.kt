package com.gimpel.pixabay

import com.gimpel.pixabay.data.DefaultImagesRepository
import com.gimpel.pixabay.data.local.HitEntity
import com.gimpel.pixabay.data.local.HitWithTags
import com.gimpel.pixabay.data.local.PixabayDao
import com.gimpel.pixabay.data.network.PixabayService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class DefaultImagesRepositoryTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val networkDataSource: PixabayService = mockk(relaxed = true)
    private val localDataSource: PixabayDao = mockk(relaxed = true)

    private val repository = DefaultImagesRepository(networkDataSource, localDataSource)

    private val mockHitEntity = HitEntity(
        1,
        "previewURL",
        "user",
        "largeImageURL",
        1,
        1,
        1
    )
    private val nonEmptyList = listOf(HitWithTags(mockHitEntity, emptyList()))


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