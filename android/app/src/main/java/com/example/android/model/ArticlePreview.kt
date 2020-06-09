package com.example.android.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class ArticlePreview(
    @PrimaryKey val Unnamed : Int = 0,
    val company : String = "",
    val link : String = "",
    val title : String = "",
    val years : String ="",
    var path : String = ""
) : Serializable
