package com.example.android.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class ArticlePreview(
    @PrimaryKey var id : Int = 0,
    val index : String = "",
    val company : String = "",
    val link : String = "",
    val title : String = "",
    val year : String ="",
    var path : String = ""
) : Serializable
