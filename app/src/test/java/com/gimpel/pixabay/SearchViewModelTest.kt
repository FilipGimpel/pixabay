package com.gimpel.pixabay

import com.gimpel.pixabay.data.HitRepository
import com.gimpel.pixabay.list.SearchViewModel
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifySequence
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = mockk<HitRepository>(relaxed = true)

    private val query = "query"
    private val differentQuery = "differentQuery"
    private val anotherQuery = "anotherQuery"

    @Test
    fun `should fetch results on query change`() = runTest {
        // when
        SearchViewModel(repository).updateQuery(query)

        advanceUntilIdle()

        // then
        verify { repository.getSearchResultStream(query) }
    }

    @Test
    fun `should collect only distinct queries`() = runTest {
        val viewModel = SearchViewModel(repository)

        // when
        viewModel.updateQuery(query)
        delay(600) // we need delay because of debounce operator when collecting
        viewModel.updateQuery(query)

        advanceUntilIdle()

        // then
        verify(exactly = 1) { repository.getSearchResultStream(query) }
    }

    @Test
    fun `should throttle collection of queries`() = runTest {
        val viewModel = SearchViewModel(repository)

        // when
        viewModel.updateQuery(query)
        delay(400) // this delay will be too short for debounce operator
        viewModel.updateQuery(differentQuery)
        delay(600) // this delay will be enough for debounce operator
        viewModel.updateQuery(anotherQuery)

        advanceUntilIdle()

        // then
        verifySequence {
            repository.getSearchResultStream(differentQuery)
            repository.getSearchResultStream(anotherQuery)
        }
    }
}