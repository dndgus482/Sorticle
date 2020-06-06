package com.example.android.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
@Entity
data class ArticlePreview(
    @PrimaryKey val Unnamed : Int = 0,
    val company : String = "",
    val link : String = "",
    val title : String = "",
    val years : String =""
) : Serializable
