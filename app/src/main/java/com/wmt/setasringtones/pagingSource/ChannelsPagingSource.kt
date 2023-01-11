package com.wmt.setasringtones.pagingSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.wmt.setasringtones.`interface`.ApiServices
import com.wmt.setasringtones.models.ChannelsItem

class ChannelsPagingSource(private val apiServices: ApiServices, private val swipeRefreshLayout: SwipeRefreshLayout) : PagingSource<Int, ChannelsItem>() {
    override fun getRefreshKey(state: PagingState<Int, ChannelsItem>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ChannelsItem> {
        return try {
            val currentPageNumber = params.key ?: 1
            val response = apiServices.getChannelsResponse(currentPageNumber.toString())
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