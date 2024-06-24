package com.gimpel.pixabay.list

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gimpel.pixabay.data.network.PixabayService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val pixabayService: PixabayService,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    init {
        Log.d("Filip", "SearchViewModel created")
        viewModelScope.launch {
            Log.d("Filip", pixabayService.get("kittens").toString())
        }
    }
}