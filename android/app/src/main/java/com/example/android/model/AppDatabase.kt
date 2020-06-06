package com.example.android.model

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ArticlePreview::class, History::class], version = 4)
abstract class AppDatabase : RoomDatabase() {
    abstract fun articleDao(): ArticleDao
    abstract fun historyDao(): HistoryDao
}
