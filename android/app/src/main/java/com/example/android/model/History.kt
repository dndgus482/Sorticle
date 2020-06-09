package com.example.android.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys = arrayOf(ForeignKey(entity = ArticlePreview::class,
                                        parentColumns = arrayOf("Unnamed"),
                                        childColumns = arrayOf("id"))))
data class History(
    @PrimaryKey val id : Int = 0,
    var createdAt: Long = 0
)