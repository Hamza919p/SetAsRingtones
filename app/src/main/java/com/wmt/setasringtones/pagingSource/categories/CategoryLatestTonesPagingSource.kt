package com.wmt.setasringtones.pagingSource.categories

import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.wmt.setasringtones.`interface`.ApiServices
import com.wmt.setasringtones.models.TonesItem

class CategoryLatestTonesPagingSource(
    private val apiServices: ApiServices,
    private val ringtoneId: Int,
    private val swipeRefreshLayout: SwipeRefreshLayout
) : PagingSource<Int, TonesItem>() {
    override fun getRefreshKey(state: PagingState<Int, TonesItem>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TonesItem> {
        return try {
            val currentPageNumber = params.key ?: 1

            val response = apiServices.getCategoryLatestTonesResponse(
                ringtoneId.toString(),
                currentPageNumber.toString()
            )
            swipeRefreshLayout.isRefreshing = false

            LoadResult.Page(
                data = response.body()!!,
                prevKey = if (currentPageNumber == 1) null else currentPageNumber - 1,
                nextKey = if (currentPageNumber == 1000) null else currentPageNumber + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}