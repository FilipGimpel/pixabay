package com.gimpel.pixabay.list

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil.compose.rememberAsyncImagePainter
import com.gimpel.pixabay.R
import com.gimpel.pixabay.model.Hit


@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = hiltViewModel(),
    onItemClick: (Int) -> Unit
) {
    var clickedItemId by rememberSaveable { mutableStateOf<Int?>(null) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val lazyPagingItems: LazyPagingItems<Hit> =
        uiState.itemsPaginatedFlow.collectAsLazyPagingItems()

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            TextField(
                modifier = modifier.fillMaxWidth(),
                singleLine = true,
                value = uiState.query,
                onValueChange = { newQuery -> viewModel.updateQuery(newQuery) },
                label = { Text(text = stringResource(id = R.string.placeholder_search)) }
            )

            LazyColumn {
                if (
                    lazyPagingItems.loadState.isIdle &&
                    lazyPagingItems.itemCount == 0 &&
                    uiState.query.isNotEmpty()) {
                    item {
                        TextLabel(text = stringResource(id = R.string.no_results))
                    }
                } else if (lazyPagingItems.loadState.refresh is LoadState.Error) {
                    item {
                        TextLabel(text = stringResource(id = R.string.error_occurred))
                    }
                } else if (lazyPagingItems.loadState.refresh is LoadState.Loading && uiState.query.isNotEmpty()) {
                    item {
                        LoadingView(Modifier.padding(vertical = 16.dp))
                    }
                } else if (lazyPagingItems.loadState.refresh is LoadState.NotLoading) {
                    items(
                        count = lazyPagingItems.itemCount,
                    ) { index ->
                        val hit = lazyPagingItems[index] ?: return@items
                        SearchResultItem(
                            hit = hit, onItemClick = {
                                clickedItemId = hit.id
                                viewModel.showDialog()
                            }
                        )
                    }
                }

                if (lazyPagingItems.loadState.append == LoadState.Loading) {
                    item {
                        LoadingView()
                    }
                }
            }

            if (uiState.showDialog) {
                AlertDialog(
                    onDismissRequest = { viewModel.hideDialog() },
                    title = { Text(text = stringResource(id = R.string.dialog_title)) },
                    text = { Text(text = stringResource(id = R.string.dialog_text)) },
                    confirmButton = {
                        TextButton(onClick = {
                            viewModel.hideDialog()
                            clickedItemId?.let { id -> onItemClick(id) }
                        }) {
                            Text(text = stringResource(id = R.string.yes))
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { viewModel.hideDialog() }) {
                            Text(text = stringResource(id = R.string.no))
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun LoadingView(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
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
