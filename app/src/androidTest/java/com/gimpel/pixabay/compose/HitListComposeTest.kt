package com.gimpel.pixabay.compose

import android.app.Application
import androidx.annotation.StringRes
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollToIndex
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.gimpel.pixabay.R
import com.gimpel.pixabay.search.presentation.ui.HitList
import com.gimpel.pixabay.search.presentation.ui.SearchScreenTestTags
import com.gimpel.pixabay.search.domain.model.Hit
import io.mockk.every
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class HitListComposeTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val mockHit = com.gimpel.pixabay.search.domain.model.Hit(
        id = -1,
        previewURL = "",
        tags = emptyList(),
        user = "",
        likes = 0,
        comments = 0,
        downloads = 0,
        largeImageURL = ""
    )

    @Test
    fun should_be_idle_on_start() {
        // given
        val items = mockk<LazyPagingItems<com.gimpel.pixabay.search.domain.model.Hit>> {
            every { itemCount } returns 0
            every { loadState.isIdle } returns true
            every { loadState.refresh } returns LoadState.NotLoading(false)
            every { loadState.append } returns LoadState.NotLoading(false)
            every { loadState.source.refresh } returns LoadState.NotLoading(false)
        }
        val searchQuery = ""

        // when
        startHitList(items, searchQuery)

        // then
        composeTestRule.onNodeWithContentDescription("Loading").assertDoesNotExist()
    }

    @Test
    fun should_show_loading_when_loading() {
        // given
        val items = mockk<LazyPagingItems<com.gimpel.pixabay.search.domain.model.Hit>> {
            every { itemCount } returns 0
            every { loadState.isIdle } returns false
            every { loadState.refresh } returns LoadState.Loading
            every { loadState.append } returns LoadState.NotLoading(false)
            every { loadState.source.refresh } returns LoadState.Loading
        }
        val searchQuery = "not_empty_query"

        // when
        startHitList(items, searchQuery)

        // then
        composeTestRule.onNodeWithTag(com.gimpel.pixabay.search.presentation.ui.SearchScreenTestTags.LoadingIndicator).assertExists()
    }

    @Test
    fun should_show_items_when_finished() {
        // given
        val itemsCount = 10

        val items = mockk<LazyPagingItems<com.gimpel.pixabay.search.domain.model.Hit>> {
            every { itemCount } returns itemsCount
            every { loadState.isIdle } returns true
            every { loadState.refresh } returns LoadState.NotLoading(false)
            every { loadState.append } returns LoadState.NotLoading(false)
            every { loadState.source.refresh } returns LoadState.NotLoading(false)

            every { get(any<Int>()) } returns mockHit
        }
        val searchQuery = "not_empty_query"

        // when
        startHitList(items, searchQuery)

        // then
        composeTestRule.onAllNodesWithTag(com.gimpel.pixabay.search.presentation.ui.SearchScreenTestTags.ListItem).assertCountEquals(itemsCount)
    }

    @Test
    fun should_show_items_and_loading_when_appending() {
        // given
        val itemsCount = 10

        val items = mockk<LazyPagingItems<com.gimpel.pixabay.search.domain.model.Hit>> {
            every { itemCount } returns itemsCount
            every { loadState.isIdle } returns true
            every { loadState.refresh } returns LoadState.NotLoading(false)
            every { loadState.append } returns LoadState.Loading
            every { loadState.source.refresh } returns LoadState.NotLoading(false)

            every { get(any<Int>()) } returns mockHit
        }
        val searchQuery = "not_empty_query"

        // when
        startHitList(items, searchQuery)
        composeTestRule.onNodeWithTag(com.gimpel.pixabay.search.presentation.ui.SearchScreenTestTags.List)
            .performScrollToIndex(itemsCount)

        // then
        composeTestRule.onAllNodesWithTag(com.gimpel.pixabay.search.presentation.ui.SearchScreenTestTags.ListItem).assertCountEquals(itemsCount)
        composeTestRule.onNodeWithTag(com.gimpel.pixabay.search.presentation.ui.SearchScreenTestTags.AppendingIndicator).assertExists()
    }

    @Test
    fun should_show_no_results_when_no_results_for_query() {
        // given
        val itemsCount = 0

        val items = mockk<LazyPagingItems<com.gimpel.pixabay.search.domain.model.Hit>> {
            every { itemCount } returns itemsCount
            every { loadState.isIdle } returns true
            every { loadState.refresh } returns LoadState.NotLoading(false)
            every { loadState.append } returns LoadState.NotLoading(true)
            every { loadState.source.refresh } returns LoadState.NotLoading(false)

            every { get(any<Int>()) } returns mockHit
        }
        val searchQuery = "not_empty_query"

        // when
        startHitList(items, searchQuery)
        composeTestRule.onNodeWithTag(com.gimpel.pixabay.search.presentation.ui.SearchScreenTestTags.List)
            .performScrollToIndex(itemsCount)

        // then
        composeTestRule.onAllNodesWithTag(com.gimpel.pixabay.search.presentation.ui.SearchScreenTestTags.ListItem).assertCountEquals(itemsCount)
        composeTestRule.onNodeWithText(getString(R.string.no_results)).assertExists()
    }

    @Test
    fun should_show_error_on_error() {
        // given
        val items = mockk<LazyPagingItems<com.gimpel.pixabay.search.domain.model.Hit>> {
            every { itemCount } returns 0
            every { loadState.isIdle } returns false
            every { loadState.refresh } returns LoadState.Error(Exception())
            every { loadState.append } returns LoadState.NotLoading(false)
            every { loadState.source.refresh } returns LoadState.Error(Exception())
        }
        val searchQuery = "not_empty_query"

        // when
        startHitList(items, searchQuery)

        // then
        composeTestRule.onNodeWithText(getString(R.string.error_occurred)).assertExists()
    }

    private fun startHitList(items: LazyPagingItems<com.gimpel.pixabay.search.domain.model.Hit>, searchQuery: String) {
        composeTestRule.setContent {
            com.gimpel.pixabay.search.presentation.ui.HitList(
                lazyPagingItems = items,
                query = searchQuery,
                onSetLastClickedItemId = {},
                onDialogShow = {}
            )
        }
    }
}

fun getString(@StringRes resId: Int): String =
    ApplicationProvider.getApplicationContext<Application>().getString(resId)