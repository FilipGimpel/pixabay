package com.gimpel.pixabay.search.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gimpel.pixabay.search.domain.repository.ImagesRepository
import com.gimpel.pixabay.search.presentation.ui.DetailScreenNavArgs.DETAIL_ID_ARG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val imagesRepository: ImagesRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val mutableUiState = MutableStateFlow(UiState())
    val uiState = mutableUiState.asStateFlow()

    init {
        viewModelScope.launch {
            val hitId: Int = savedStateHandle[DETAIL_ID_ARG]!!
            mutableUiState.value = mutableUiState.value.copy(hit = imagesRepository.getHit(hitId))
        }
    }

    // todo use sealed class and create states for loading, error, success
    data class UiState(
        val isLoading: Boolean = false,
        val hit: com.gimpel.pixabay.search.domain.model.Hit? = null
    )
}