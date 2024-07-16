package com.gimpel.pixabay.search.presentation

import com.gimpel.pixabay.search.presentation.usecase.ObserveQueryResults
import com.gimpel.pixabay.search.presentation.viewmodel.SearchViewModel
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

    private val observeQueryResults = mockk<ObserveQueryResults>(relaxed = true)

    private val query = "query"
    private val differentQuery = "differentQuery"
    private val anotherQuery = "anotherQuery"

    @Test
    fun `should fetch results on query change`() = runTest {
        // when
        SearchViewModel(observeQueryResults).updateQuery(query)

        advanceUntilIdle()

        // then
        verify { observeQueryResults(query) }
    }

    @Test
    fun `should collect only distinct queries`() = runTest {
        val viewModel = SearchViewModel(observeQueryResults)

        // when
        viewModel.updateQuery(query)
        delay(600) // we need delay because of debounce operator when collecting
        viewModel.updateQuery(query)

        advanceUntilIdle()

        // then
        verify(exactly = 1) { observeQueryResults(query) }
    }

    @Test
    fun `should throttle collection of queries`() = runTest {
        val viewModel = SearchViewModel(observeQueryResults)

        // when
        viewModel.updateQuery(query)
        delay(400) // this delay will be too short for debounce operator
        viewModel.updateQuery(differentQuery)
        delay(600) // this delay will be enough for debounce operator
        viewModel.updateQuery(anotherQuery)

        advanceUntilIdle()

        // then
        verifySequence {
            observeQueryResults(differentQuery)
            observeQueryResults(anotherQuery)
        }
    }
}