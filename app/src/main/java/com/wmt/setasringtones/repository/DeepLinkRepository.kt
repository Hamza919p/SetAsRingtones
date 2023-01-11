package com.wmt.setasringtones.repository

import com.wmt.setasringtones.`interface`.ApiServices
import com.wmt.setasringtones.api.RetrofitHelper
import com.wmt.setasringtones.models.TonesItem
import retrofit2.Response

class DeepLinkRepository {

    suspend fun getToneThroughDeepLink(ringtoneId: String) : Response<TonesItem> {
        val retrofit = RetrofitHelper.getInstance().create(ApiServices::class.java)
        return retrofit.getDeepLinkResponse(ringtoneId)
    }
}