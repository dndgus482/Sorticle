package com.example.android.model

import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
data class ArticlePreview(
    val Unnamed : Int = 0 ,
    val company : String = "",
    val link : String = "",
    val title : String = "",
    val years : String =""
) : Serializable
