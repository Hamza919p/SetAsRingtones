package com.wmt.setasringtones.viewModels

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
import com.wmt.setasringtones.pagingSource.SearchPagingSource
import kotlinx.coroutines.flow.Flow

class SearchActivityViewModel : ViewModel() {
    private val retrofitService = RetrofitHelper.getInstance().create(ApiServices::class.java)

    fun getTones(
        swipeRefreshLayout: SwipeRefreshLayout,
        query: String
    ): Flow<PagingData<TonesItem>> {

        return Pager(
            config = PagingConfig(pageSize = 6),
            pagingSourceFactory = {
                SearchPagingSource(
                    retrofitService,
                    query,
                    swipeRefreshLayout
                )
            }
        ).flow.cachedIn(viewModelScope)

    }
}