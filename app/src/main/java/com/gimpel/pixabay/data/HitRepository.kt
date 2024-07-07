package com.gimpel.pixabay.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.gimpel.pixabay.model.Hit
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HitRepository @Inject constructor(private val service: ImagesRepository) {

    fun getSearchResultStream(query: String): Flow<PagingData<Hit>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = NETWORK_PAGE_SIZE),
            pagingSourceFactory = { HitPagingSource(service, query) }
        ).flow
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 20
    }
}