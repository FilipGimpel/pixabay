package com.gimpel.pixabay.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.gimpel.pixabay.data.HitRepository
import com.gimpel.pixabay.model.Hit
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: HitRepository
) : ViewModel() {

    private val mutableUiState = MutableStateFlow(UiState())
    val uiState = mutableUiState.asStateFlow()

    private var clickedItemId: Int? = null

    init {
        viewModelScope.launch {
            uiState.map { it.query }
            .debounce(500)
            .distinctUntilChanged()
            .collect { query ->
                if (query.trim().isNotEmpty()) {
                    var currentUiState = mutableUiState.value
                    mutableUiState.value = currentUiState.copy(
                        itemsPaginatedFlow = repository.getSearchResultStream(query).cachedIn(viewModelScope)
                    )
                }
            }
        }
    }

    fun setLastClickedItemId(itemId: Int) {
        clickedItemId = itemId
    }

    fun getLastClickedItemId(): Int? {
        return clickedItemId
    }

    fun showDialog() {
        val current = mutableUiState.value
        mutableUiState.value = current.copy(showDialog = true)
    }

    fun hideDialog() {
        val current = mutableUiState.value
        mutableUiState.value = current.copy(showDialog = false)
    }

    fun updateQuery(newQuery: String) {
        val current = mutableUiState.value
        mutableUiState.value = current.copy(query = newQuery)
    }

    data class UiState(
        val showDialog: Boolean = false,
        val query: String = "",
        val itemsPaginatedFlow: Flow<PagingData<Hit>> = emptyFlow()
    )
}