package com.wmt.setasringtones.api

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitHelper {

    private var gson = GsonBuilder()
        .setLenient()
        .create()

    fun getInstance(): Retrofit {
        return Retrofit.Builder().baseUrl(ApiUrls.BaseURL)
            .addConverterFactory(GsonConverterFactory.create(gson)).build()
    }

}