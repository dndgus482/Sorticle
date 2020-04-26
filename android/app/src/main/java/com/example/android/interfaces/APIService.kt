package com.example.android.interfaces

import com.example.android.model.ArticlePreview
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {
    @GET("getList")
    fun getList(): Call<ArticlePreview>
}