package com.gimpel.pixabay.search.presentation

import androidx.paging.PagingSource
import androidx.paging.PagingState
import arrow.core.Either
import com.gimpel.pixabay.search.domain.model.Hit
import com.gimpel.pixabay.search.domain.repository.ImagesRepository
import com.gimpel.pixabay.search.domain.usecase.GetHit
import com.gimpel.pixabay.search.domain.usecase.GetHits

private const val STARTING_PAGE_INDEX = 1

class HitPagingSource(
    private val getHits: GetHits,
    private val query: String
) : PagingSource<Int, Hit>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Hit> {
        val page = params.key ?: STARTING_PAGE_INDEX

        return when (val result = getHits(query, page, params.loadSize)) {
            is Either.Left ->
                LoadResult.Error(result.value)
            is Either.Right ->
                LoadResult.Page(
                    data = result.value,
                    prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1,
                    nextKey = if (result.value.isEmpty()) null else page + 1
                )
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Hit>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            // This loads starting from previous page, but since PagingConfig.initialLoadSize spans
            // multiple pages, the initial load will still load items centered around
            // anchorPosition. This also prevents needing to immediately launch prepend due to
            // prefetchDistance.
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }
}