package com.wmt.setasringtones.viewModels.fragments.channelDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.wmt.setasringtones.`interface`.ApiServices
import com.wmt.setasringtones.api.RetrofitHelper
import com.wmt.setasringtones.models.TonesItem
import com.wmt.setasringtones.pagingSource.channelDetails.ChannelBestTonesPagingSource
import com.wmt.setasringtones.pagingSource.channelDetails.ChannelLatestTonesPagingSource
import kotlinx.coroutines.flow.Flow

class ChannelLatestAndBestTonesViewModel : ViewModel() {

    private val retrofitService = RetrofitHelper.getInstance().create(ApiServices::class.java)

    fun getTones(swipeRefreshLayout: SwipeRefreshLayout, userId: Int, isLatestTones: Boolean): Flow<PagingData<TonesItem>> {

        return if(isLatestTones) {
            Pager(
                config = PagingConfig(pageSize = 6),
                pagingSourceFactory = { ChannelLatestTonesPagingSource(retrofitService, userId , swipeRefreshLayout) }
            ).flow.cachedIn(viewModelScope)
        }else {
            Pager(
                config = PagingConfig(pageSize = 6),
                pagingSourceFactory = { ChannelBestTonesPagingSource(retrofitService, userId , swipeRefreshLayout) }
            ).flow.cachedIn(viewModelScope)
        }
    }
}