package com.example.android.model

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ArticlePreview::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun articleDao(): ArticleDao
}
