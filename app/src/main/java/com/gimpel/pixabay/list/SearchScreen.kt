package com.gimpel.pixabay.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.rememberAsyncImagePainter
import com.gimpel.pixabay.R
import com.gimpel.pixabay.model.Hit


@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = hiltViewModel(),
    onHitClick: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val lazyPagingItems: LazyPagingItems<Hit> = uiState.itemsPaginatedFlow.collectAsLazyPagingItems()

    SearchScreen(
        modifier = modifier,
        uiState = uiState,
        lazyPagingItems = lazyPagingItems,
        onHitClick = onHitClick,
        onUpdateQuery = viewModel::updateQuery,
        onDialogShow = viewModel::showDialog,
        onDialogDismiss = viewModel::hideDialog,
        onSetLastClickedItemId = viewModel::setLastClickedItemId,
        onGetLastClickedItemId = viewModel::getLastClickedItemId
    )
}

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    uiState: SearchViewModel.UiState,
    lazyPagingItems: LazyPagingItems<Hit>,
    onHitClick: (Int) -> Unit = {},
    onUpdateQuery: (String) -> Unit = {},
    onDialogShow: () -> Unit = {},
    onDialogDismiss: () -> Unit = {},
    onSetLastClickedItemId: (Int) -> Unit = {},
    onGetLastClickedItemId: () -> Int? = { null },
) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            SearchBar(
                query = uiState.query,
                onUpdateQuery = onUpdateQuery
            )

            HitList(
                query = uiState.query,
                lazyPagingItems = lazyPagingItems,
                onSetLastClickedItemId = onSetLastClickedItemId,
                onDialogShow = onDialogShow
            )

            if (uiState.showDialog) {
                ShowDetailsDialog(
                    onDialogDismiss = onDialogDismiss,
                    onGetLastClickedItemId = onGetLastClickedItemId,
                    onHitClick = onHitClick
                )
            }
        }
    }
}

@Composable
fun ShowDetailsDialog(
    onDialogDismiss: () -> Unit,
    onGetLastClickedItemId: () -> Int?,
    onHitClick: (Int) -> Unit,
) {
    AlertDialog(
        onDismissRequest = { onDialogDismiss() },
        title = { Text(text = stringResource(id = R.string.dialog_title)) },
        text = { Text(text = stringResource(id = R.string.dialog_text)) },
        confirmButton = {
            TextButton(onClick = {
                onDialogDismiss()
                onGetLastClickedItemId()?.let { id -> onHitClick(id) }
            }) {
                Text(text = stringResource(id = R.string.yes))
            }
        },
        dismissButton = {
            TextButton(onClick = { onDialogDismiss() }) {
                Text(text = stringResource(id = R.string.no))
            }
        }
    )
}

@Composable
fun HitList(
    query: String,
    lazyPagingItems: LazyPagingItems<Hit>,
    onSetLastClickedItemId: (Int) -> Unit,
    onDialogShow: () -> Unit,
) {
    LazyColumn(modifier = Modifier.testTag(SearchScreenTestTags.List)) {
        when {
            noResultsAvailableForQuery(lazyPagingItems, query) -> item {
                TextLabel(text = stringResource(id = R.string.no_results))
            }
            isRefreshError(lazyPagingItems) -> item {
                TextLabel(text = stringResource(id = R.string.error_occurred))
            }
            isLoadingResultsForQuery(lazyPagingItems, query) -> item {
                LoadingView(testTag = SearchScreenTestTags.LoadingIndicator)
            }
            resultsAvailableForQuery(lazyPagingItems, query) -> items(
                count = lazyPagingItems.itemCount,
            ) { index ->
                val hit = lazyPagingItems[index] ?: return@items
                SearchResultItem(
                    hit = hit, onItemClick = {
                        onSetLastClickedItemId(hit.id)
                        onDialogShow()
                    }
                )
            }
        }

        if (isLoadingResultsAtTheEnd(lazyPagingItems)) {
            item {
                LoadingView(testTag = SearchScreenTestTags.AppendingIndicator)
            }
        }

    }
}

fun noResultsAvailableForQuery(lazyPagingItems: LazyPagingItems<*>, query: String): Boolean =
    lazyPagingItems.loadState.isIdle &&
    lazyPagingItems.itemCount == 0 &&
    query.isNotEmpty()

fun isRefreshError(lazyPagingItems: LazyPagingItems<*>): Boolean =
    lazyPagingItems.loadState.refresh is LoadState.Error

fun isLoadingResultsForQuery(lazyPagingItems: LazyPagingItems<*>, query: String): Boolean =
    lazyPagingItems.loadState.refresh is LoadState.Loading && query.isNotEmpty()

fun resultsAvailableForQuery(lazyPagingItems: LazyPagingItems<*>, query: String): Boolean =
    lazyPagingItems.loadState.refresh is LoadState.NotLoading && query.isNotEmpty()

fun isLoadingResultsAtTheEnd(lazyPagingItems: LazyPagingItems<*>): Boolean =
    lazyPagingItems.loadState.append is LoadState.Loading

@Composable
fun SearchBar(
    query: String,
    onUpdateQuery: (String) -> Unit,
) {
    TextField(
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        value = query,
        onValueChange = onUpdateQuery,
        label = { Text(text = stringResource(id = R.string.placeholder_search)) }
    )
}

@Composable
fun LoadingView(
    modifier: Modifier = Modifier,
    testTag: String = ""
) {
    Box(modifier = modifier.fillMaxSize().padding(vertical = 16.dp), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(
            modifier = Modifier.testTag(testTag)
        )
    }
}

@Composable
fun TextLabel(text: String) {
    Text(
        modifier = Modifier
            .padding(top = 16.dp)
            .fillMaxWidth(),
        text = text,
        textAlign = TextAlign.Center
    )
}

@Composable
fun SearchResultItem(hit: Hit, onItemClick: () -> Unit) {
    Row(
        modifier = Modifier
            .clickable(enabled = true, onClick = onItemClick)
            .fillMaxWidth()
            .padding(16.dp)
            .testTag(SearchScreenTestTags.ListItem)
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = hit.previewURL),
            contentDescription = "Image thumbnail",
            modifier = Modifier
                .size(50.dp)
        )
        Column(
            modifier = Modifier
                .padding(start = 16.dp)
                .align(Alignment.CenterVertically)
        ) {
            Text(text = hit.user, fontWeight = FontWeight.Bold)
            Text(text = hit.tags.joinToString(separator = ", "))
        }
    }
}

object SearchScreenTestTags {
    const val LoadingIndicator = "LoadingIndicator"
    const val AppendingIndicator = "AppendingIndicator"
    const val List = "List"
    const val ListItem = "ListItem"
}
