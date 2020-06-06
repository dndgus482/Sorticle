package com.example.android.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
@Entity(
    foreignKeys = [ForeignKey(
        entity = ArticlePreview::class,
        parentColumns = arrayOf("Unnamed"),
        childColumns = arrayOf("id")
    )]
)
data class Bookmark(
    @PrimaryKey val id: Int = 0,
    var createdAt: Long = 0
)