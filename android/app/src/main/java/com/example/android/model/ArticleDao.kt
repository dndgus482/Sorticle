package com.example.android.model

import androidx.room.*

@Dao
abstract class ArticleDao() {
    @Query("SELECT * FROM articlepreview ORDER BY createdAt DESC")
    abstract fun getAll(): List<ArticlePreview>

    @Query("SELECT * FROM articlepreview WHERE unnamed IN (:ids)")
    abstract fun loadAllByIds(ids: IntArray): List<ArticlePreview>

    @Query("SELECT * FROM articlepreview WHERE unnamed IN (:id)")
    abstract fun findByName(id: Int): ArticlePreview

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAll(vararg article: ArticlePreview)
    fun insertWithTimestamp(vararg article : ArticlePreview) {
        article.forEach {
            it.createdAt = System.currentTimeMillis()
        }
    }

    @Delete
    abstract fun delete(article: ArticlePreview)
}
