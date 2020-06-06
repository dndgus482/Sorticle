package com.example.android.model

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
abstract class HistoryDao() {
    @Query(
        "SELECT ArticlePreview.* FROM ArticlePreview INNER JOIN History ON unnamed = id ORDER BY createdAt DESC"
    )
    abstract fun getAll(): LiveData<List<ArticlePreview>>

    //    @Query("SELECT * FROM History WHERE id IN (:ids)")
//    abstract fun loadAllByIds(ids: IntArray): List<ArticlePreview>
//
//    @Query("SELECT * FROM articlepreview WHERE unnamed IN (:id)")
//    abstract fun findByName(id: Int): ArticlePreview
//
    @Query(
        """
            DELETE FROM History 
            WHERE id NOT IN (SELECT id from History ORDER BY createdAt DESC LIMIT 50)
            """
    )
    abstract fun deleteOld()



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAll(vararg histories: History)
    fun insertWithTimestamp(vararg histories: History) {
        histories.forEach {
            it.createdAt = System.currentTimeMillis()
        }
    }

    @Delete
    abstract fun delete(history: History)
}
