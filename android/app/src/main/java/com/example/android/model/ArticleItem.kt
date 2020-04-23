package com.example.android.model

data class ArticleItem(val body : List<Item>) {
    data class Item(
        var title : String,
        var content : String
    )
}