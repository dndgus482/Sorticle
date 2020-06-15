package com.example.android.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = ArticlePreview::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("id")
    )]
)
data class Bookmark(
    @PrimaryKey val id: Int = 0,
    var createdAt: Long = 0
)