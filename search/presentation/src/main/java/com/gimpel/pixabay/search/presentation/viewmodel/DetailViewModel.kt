package com.gimpel.pixabay.search.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gimpel.pixabay.search.domain.model.Hit
import com.gimpel.pixabay.search.domain.usecase.GetHit
import com.gimpel.pixabay.search.presentation.ui.DetailScreenNavArgs.DETAIL_ID_ARG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getHit: GetHit,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val mutableUiState = MutableStateFlow(UiState())
    val uiState = mutableUiState.asStateFlow()

    init {
        viewModelScope.launch {
            val hitId: Int = savedStateHandle[DETAIL_ID_ARG]!!
            mutableUiState.value = mutableUiState.value.copy(hit = getHit(hitId))
        }
    }

    data class UiState(
        val isLoading: Boolean = false,
        val hit: Hit? = null
    )
}