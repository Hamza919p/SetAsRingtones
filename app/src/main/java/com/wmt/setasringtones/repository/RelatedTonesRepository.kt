package com.wmt.setasringtones.repository

import com.wmt.setasringtones.`interface`.ApiServices
import com.wmt.setasringtones.api.RetrofitHelper
import com.wmt.setasringtones.models.TonesItem
import retrofit2.Response

class RelatedTonesRepository {
    suspend fun getRelatedRingtones(id: String) : Response<ArrayList<TonesItem>> {
        val retrofit = RetrofitHelper.getInstance().create(ApiServices::class.java)
        return retrofit.getRelatedTonesResponse(id)
    }
}