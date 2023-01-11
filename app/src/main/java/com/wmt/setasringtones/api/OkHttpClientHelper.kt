package com.wmt.setasringtones.api

import okhttp3.Request
import okhttp3.RequestBody

object OkHttpClientHelper {

    fun postRequest(url: String, requestBody: RequestBody): Request {
        return Request.Builder()
            .url(url)
            .post(requestBody)
            .build()
    }

    fun getRequest(url: String): Request {
        return Request.Builder().url(url).get().build()
    }
}