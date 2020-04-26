package com.example.android.interfaces

import com.example.android.model.Article
import com.example.android.model.ArticlePreview
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {
    @GET("getList")
    fun getList(@Query("q") q : String): Call<ArticlePreview>

    @GET("getNews")
    fun getNews(@Query("id") id : Int) : Call<Article>
}