package com.gimpel.pixabay.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
fun DetailScreen(
    viewModel: DetailViewModel = hiltViewModel()
) {

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val uiState = viewModel.uiState.collectAsStateWithLifecycle()

            if (uiState.value.isLoading) {
                Text(text = "Loading...")
            } else {
                uiState.value.hit?.let { hit ->
                    DetailContent(hit = hit)
                }
            }
        }
    }
}

@Composable
fun DetailContent(hit: Hit) {
    Image(
        painter = rememberAsyncImagePainter(model = hit.largeImageURL),
        contentDescription = "Image thumbnail",
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(CutCornerShape(16.dp))
    )
    Spacer(modifier = Modifier.height(16.dp))
    Text(text = "User: ${hit.user}", fontWeight = FontWeight.Bold)
    Text(text = "Tags: ${hit.tags}")
    Text(text = "Likes: ${hit.likes}")
    Text(text = "Downloads: ${hit.downloads}")
    Text(text = "Comments: ${hit.comments}")
}

