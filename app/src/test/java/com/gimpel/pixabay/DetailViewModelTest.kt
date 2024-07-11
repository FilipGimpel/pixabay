package com.gimpel.pixabay

import androidx.lifecycle.SavedStateHandle
import com.gimpel.pixabay.PixabayDestinationsArgs.DETAIL_ID_ARG
import com.gimpel.pixabay.data.ImagesRepository
import com.gimpel.pixabay.detail.DetailViewModel
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

class DetailViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val savedState = mockk<SavedStateHandle>()
    private val imagesRepository = mockk<ImagesRepository>(relaxed = true)

    private val fakeId = 1

    @Test
    fun `should fetch details on initialization`() = kotlinx.coroutines.test.runTest {
        // given
        every { savedState.get<Int>(DETAIL_ID_ARG) } returns fakeId

        // when
        DetailViewModel(imagesRepository, savedState)

        // then
        coVerify { imagesRepository.getHit(fakeId) }
    }
}
