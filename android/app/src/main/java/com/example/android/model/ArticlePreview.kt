package com.example.android.model

import java.util.*

data class ArticlePreview(val totals : Int, val items : List<Item>) {
    data class Item(
        val id : Int,
        val title : String,
        val pubDate : Date
    )
}