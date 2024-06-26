package com.gimpel.pixabay.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.gimpel.pixabay.data.network.Hit


@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = hiltViewModel(),
    onItemClick: (Int) -> Unit
) {
    var clickedItemId by rememberSaveable { mutableStateOf<Int?>(null) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            TextField(
                value = uiState.query,
                onValueChange = { newQuery -> viewModel.updateQuery(newQuery) },
                label = { Text("Search") }
            )

            if (uiState.isError) {
                Text(text = "An error occurred")
            } else if (uiState.isLoading) {
                Text(text = "Loading...")
            } else {
                LazyColumn {
                    uiState.items.forEach { hit ->
                        item(key = hit.id) {
                            SearchResultItem(
                                hit = hit, onItemClick = {
                                    clickedItemId = hit.id
                                    viewModel.showDialog()
                                }
                            )
                        }
                    }
                }

                if (uiState.showDialog) {
                    AlertDialog(
                        onDismissRequest = { viewModel.hideDialog() },
                        title = { Text(text = "Dialog Title") },
                        text = { Text(text = "Dialog Text") },
                        confirmButton = {
                            TextButton(onClick = {
                                viewModel.hideDialog()
                                clickedItemId?.let { id -> onItemClick(id) }
                            }) {
                                Text(text = "Confirm")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { viewModel.hideDialog() }) {
                                Text(text = "Dismiss")
                            }
                        }
                    )
                }
            }
        }
    }
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
                .clip(CircleShape)
        )
        Column(
            modifier = Modifier
                .padding(start = 16.dp)
                .align(Alignment.CenterVertically)
        ) {
            Text(text = hit.user, fontWeight = FontWeight.Bold)
            Text(text = hit.tags)
        }
    }
}
