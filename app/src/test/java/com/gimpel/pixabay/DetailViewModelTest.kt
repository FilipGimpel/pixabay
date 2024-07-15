package com.gimpel.pixabay

import androidx.lifecycle.SavedStateHandle
import com.gimpel.pixabay.PixabayDestinationsArgs.DETAIL_ID_ARG
import com.gimpel.pixabay.search.domain.repository.ImagesRepository
import com.gimpel.pixabay.search.presentation.viewmodel.DetailViewModel
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class DetailViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val savedState = mockk<SavedStateHandle>()
    private val imagesRepository = mockk<com.gimpel.pixabay.search.domain.repository.ImagesRepository>(relaxed = true)

    private val fakeId = 1

    @Test
    fun `should fetch details on initialization`() = runTest {
        // given
        every { savedState.get<Int>(DETAIL_ID_ARG) } returns fakeId

        // when
        com.gimpel.pixabay.search.presentation.viewmodel.DetailViewModel(imagesRepository, savedState)

        // then
        coVerify { imagesRepository.getHit(fakeId) }
    }
}
