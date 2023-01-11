package com.wmt.setasringtones.repository

import com.wmt.setasringtones.`interface`.ApiServices
import com.wmt.setasringtones.api.RetrofitHelper
import com.wmt.setasringtones.models.CategoriesItem
import com.wmt.setasringtones.models.TonesItem
import retrofit2.Response

class CategoriesRepository {

    suspend fun getCategories() : Response<ArrayList<CategoriesItem>> {
        val retrofit = RetrofitHelper.getInstance().create(ApiServices::class.java)
        return retrofit.getCategoriesResponse()
    }
}