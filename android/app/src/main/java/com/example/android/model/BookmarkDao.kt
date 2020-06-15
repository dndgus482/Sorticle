package com.example.android.model

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
abstract class BookmarkDao() {
    @Query(
        "SELECT ArticlePreview.* FROM ArticlePreview INNER JOIN Bookmark ON ArticlePreview.id = Bookmark.id ORDER BY createdAt DESC"
    )
    abstract fun getAll(): LiveData<List<ArticlePreview>>

    //    @Query("SELECT * FROM History WHERE id IN (:ids)")
//    abstract fun loadAllByIds(ids: IntArray): List<ArticlePreview>
//
    @Query("SELECT * FROM Bookmark WHERE id IN (:id)")
    abstract fun findById(id: Int): Bookmark
//


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAll(vararg bookmarks: Bookmark)
    fun insertWithTimestamp(vararg bookmarks: Bookmark) {
        bookmarks.forEach {
            it.createdAt = System.currentTimeMillis()
        }
        insertAll(*bookmarks)
    }

    @Delete
    abstract fun delete(bookmark: Bookmark)
}
