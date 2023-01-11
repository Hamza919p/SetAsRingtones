package com.wmt.setasringtones.viewModels.fragments.categories

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
import com.wmt.setasringtones.pagingSource.categories.CategoryBestTonesPagingSource
import com.wmt.setasringtones.pagingSource.categories.CategoryLatestTonesPagingSource
import kotlinx.coroutines.flow.Flow

class CategoryDetailsViewModel : ViewModel() {
    private val retrofitService = RetrofitHelper.getInstance().create(ApiServices::class.java)

    fun getTones(swipeRefreshLayout: SwipeRefreshLayout, ringtoneId: Int, isLatestTones: Boolean): Flow<PagingData<TonesItem>> {
        return if(isLatestTones) {
            Pager(
                config = PagingConfig(pageSize = 6),
                pagingSourceFactory = { CategoryLatestTonesPagingSource(retrofitService, ringtoneId , swipeRefreshLayout) }
            ).flow.cachedIn(viewModelScope)
        }else {
            Pager(
                config = PagingConfig(pageSize = 6),
                pagingSourceFactory = { CategoryBestTonesPagingSource(retrofitService, ringtoneId , swipeRefreshLayout) }
            ).flow.cachedIn(viewModelScope)
        }
    }
}