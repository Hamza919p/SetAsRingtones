package com.wmt.setasringtones.repository

import android.util.Log
import com.wmt.setasringtones.api.ApiUrls
import com.wmt.setasringtones.api.OkHttpClientHelper
import okhttp3.*
import java.io.IOException

class FCMRepository {
    fun postToken(token:String, iso:String) {
        val multipart = MultipartBody.Builder()
        multipart.setType(MultipartBody.FORM)
        multipart.addFormDataPart("fcm_id", token)
        multipart.addFormDataPart("short_country_name", iso)

        val request = OkHttpClientHelper.postRequest(ApiUrls.registerDeviceURL+ ApiUrls.token,multipart.build())

        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("token", "failure")
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")
                    Log.d("token Success", "-->" + response.body!!.string())
                }
            }
        })
    }
}