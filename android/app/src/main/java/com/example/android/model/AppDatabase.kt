package com.example.android.model

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ArticlePreview::class, History::class, Bookmark::class], version = 5)
abstract class AppDatabase : RoomDatabase() {
    abstract fun articleDao(): ArticleDao
    abstract fun historyDao(): HistoryDao
    abstract fun bookmarkDao() : BookmarkDao
}
