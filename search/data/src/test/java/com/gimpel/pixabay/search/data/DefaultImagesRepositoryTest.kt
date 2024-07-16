package com.gimpel.pixabay.search.data

import com.gimpel.pixabay.search.data.local.LocalHitDataSource
import com.gimpel.pixabay.search.data.network.RemoteHitDataSource
import com.gimpel.pixabay.search.domain.model.Hit
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class DefaultImagesRepositoryTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val remoteDataSource: RemoteHitDataSource = mockk(relaxed = true)
    private val localDataSource: LocalHitDataSource = mockk(relaxed = true)

    private val repository = DefaultImagesRepository(localDataSource, remoteDataSource)

    private val mockHit = Hit(
        id = -1,
        previewURL = "",
        tags = emptyList(),
        user = "",
        likes = 0,
        comments = 0,
        downloads = 0,
        largeImageURL = ""
    )

    private val nonEmptyList = listOf(mockHit)


    @Test
    fun `should not fetch network results if database not empty`() = runTest {
        // given
        coEvery { localDataSource.getHitsForQuery("query", 1, 1) } returns nonEmptyList

        // when
        repository.getHits("query", 1, 1)

        // then
        coVerify(exactly = 1) { localDataSource.getHitsForQuery("query", 1, 1) }
        coVerify(exactly = 0) { remoteDataSource.getHitsForQuery(any(), any(), any()) }
    }

    @Test
    fun `should fetch network results if database empty`() = runTest {
        // given
        coEvery { localDataSource.getHitsForQuery("query", 1, 1) } returns emptyList()

        // when
        repository.getHits("query", 1, 1)

        // then
        coVerify(exactly = 1) { localDataSource.getHitsForQuery("query", 1, 1) }
        coVerify(exactly = 1) { remoteDataSource.getHitsForQuery("query", 1, 1) }
    }
}