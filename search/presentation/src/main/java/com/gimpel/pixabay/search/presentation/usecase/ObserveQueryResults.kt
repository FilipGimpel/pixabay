package com.gimpel.pixabay.search.presentation.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.gimpel.pixabay.search.domain.model.Hit
import com.gimpel.pixabay.search.domain.repository.ImagesRepository
import com.gimpel.pixabay.search.domain.usecase.GetHit
import com.gimpel.pixabay.search.domain.usecase.GetHits
import com.gimpel.pixabay.search.presentation.HitPagingSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveQueryResults @Inject constructor(
    private val getHits: GetHits
) {
    operator fun invoke(query: String) : Flow<PagingData<Hit>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = NETWORK_PAGE_SIZE),
            pagingSourceFactory = { HitPagingSource(getHits, query) }
        ).flow
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 20
    }
}