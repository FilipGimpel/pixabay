package com.gimpel.pixabay.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.gimpel.pixabay.model.Hit

private const val STARTING_PAGE_INDEX = 1

class HitPagingSource(
    private val repository: ImagesRepository,
    private val query: String
) : PagingSource<Int, Hit>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Hit> {
        val page = params.key ?: STARTING_PAGE_INDEX
        return try {
            val response = repository.getHits(query, page, params.loadSize)
            val hits = response.getOrNull()!!
            LoadResult.Page(
                data = hits,
                prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1,
                nextKey = if (hits.isEmpty()) null else page + 1
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
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