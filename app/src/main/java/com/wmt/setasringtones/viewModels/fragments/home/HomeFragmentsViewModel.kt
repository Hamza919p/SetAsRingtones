package com.wmt.setasringtones.viewModels.fragments.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
import com.wmt.setasringtones.pagingSource.BestTonesPagingSource
import com.wmt.setasringtones.pagingSource.FeaturedTonesPagingSource
import com.wmt.setasringtones.pagingSource.LatestTonesPagingSource
import com.wmt.setasringtones.repository.DeepLinkRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import retrofit2.Response

class HomeFragmentsViewModel : ViewModel() {

    private var retrofitService = RetrofitHelper.getInstance().create(ApiServices::class.java)
    fun getTones(swipeRefreshLayout: SwipeRefreshLayout, fragmentName: String) : Flow<PagingData<TonesItem>>{
        when (fragmentName) {
            "Latest" -> {
                return Pager(
                    config = PagingConfig(pageSize = 6),
                    pagingSourceFactory = { LatestTonesPagingSource(retrofitService, swipeRefreshLayout) }
                ).flow.cachedIn(viewModelScope)

            }
            "Best" -> {
                return Pager(
                    config = PagingConfig(pageSize = 6),
                    pagingSourceFactory = { BestTonesPagingSource(retrofitService, swipeRefreshLayout) }
                ).flow.cachedIn(viewModelScope)
            }
            else -> {
                return Pager(
                    config = PagingConfig(pageSize = 6),
                    pagingSourceFactory = { FeaturedTonesPagingSource(retrofitService, swipeRefreshLayout) }
                ).flow.cachedIn(viewModelScope)
            }
        }
    }

}