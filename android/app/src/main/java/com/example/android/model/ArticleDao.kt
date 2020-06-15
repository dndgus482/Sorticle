package com.example.android.model

import androidx.room.*

@Dao
abstract class ArticleDao() {
    @Query("SELECT * FROM ArticlePreview")
    abstract fun getAll(): List<ArticlePreview>

    @Query("SELECT * FROM ArticlePreview WHERE id IN (:ids)")
    abstract fun loadAllByIds(ids: IntArray): List<ArticlePreview>

    @Query("SELECT * FROM ArticlePreview WHERE id IN (:id)")
    abstract fun findByName(id: Int): ArticlePreview

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAll(vararg article: ArticlePreview)

    @Delete
    abstract fun delete(article: ArticlePreview)
}
