package com.gimpel.pixabay.list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gimpel.pixabay.data.ImagesRepository
import com.gimpel.pixabay.data.network.Hit
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val imagesRepository: ImagesRepository, private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val mutableUiState = MutableStateFlow(UiState())
    val uiState = mutableUiState.asStateFlow()

    init {
        viewModelScope.launch {
            imagesRepository.getHits(listOf("fruit")).let { hits ->
                mutableUiState.update { it.copy(items = hits) }
            }
        }
    }

    fun showDialog() {
        val current = mutableUiState.value
        mutableUiState.value = current.copy(showDialog = true)
    }

    fun hideDialog() {
        val current = mutableUiState.value
        mutableUiState.value = current.copy(showDialog = false)
    }

    data class UiState(
        val isLoading: Boolean = false,
        val showDialog: Boolean = false,
        val query: String = "",
        val items: List<Hit> = emptyList(),
    )
}