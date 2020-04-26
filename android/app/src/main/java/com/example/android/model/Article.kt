package com.example.android.model

import java.util.*

data class Article(
    val id : Int,
    val title : String,
    val pubDate : Date,
    val source : String,
    val originallink : String,
    val article_body : String
) {
}