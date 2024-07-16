package com.gimpel.pixabay.search.presentation

import androidx.lifecycle.SavedStateHandle
import com.gimpel.pixabay.search.domain.usecase.GetHit
import com.gimpel.pixabay.search.presentation.ui.DetailScreenNavArgs.DETAIL_ID_ARG
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
    private val getHit = mockk<GetHit>(relaxed = true)

    private val fakeId = 1

    @Test
    fun `should fetch details on initialization`() = runTest {
        // given
        every { savedState.get<Int>(DETAIL_ID_ARG) } returns fakeId

        // when
        DetailViewModel(getHit, savedState)

        // then
        coVerify { getHit(fakeId) }
    }
}
