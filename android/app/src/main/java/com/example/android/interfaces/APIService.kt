package com.example.android.interfaces

import com.example.android.model.ArticleItem
import retrofit2.Call
import retrofit2.http.GET

interface APIService {
    @GET("getList")
    fun getList(): Call<ArticleItem?>
}