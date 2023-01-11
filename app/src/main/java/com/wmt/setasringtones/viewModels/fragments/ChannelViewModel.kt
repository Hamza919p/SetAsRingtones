package com.wmt.setasringtones.viewModels.fragments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.wmt.setasringtones.`interface`.ApiServices
import com.wmt.setasringtones.api.RetrofitHelper
import com.wmt.setasringtones.models.ChannelsItem
import com.wmt.setasringtones.pagingSource.ChannelsPagingSource
import kotlinx.coroutines.flow.Flow

class ChannelViewModel : ViewModel() {

    private val retrofitService = RetrofitHelper.getInstance().create(ApiServices::class.java)

    fun retrieveToneChannels(swipeRefreshLayout: SwipeRefreshLayout) : Flow<PagingData<ChannelsItem>> {
        return Pager(
            config = PagingConfig(pageSize = 6),
            pagingSourceFactory = { ChannelsPagingSource(retrofitService,swipeRefreshLayout) }
        ).flow.cachedIn(viewModelScope)
    }
}