package com.example.android.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class ArticlePreview (
    @PrimaryKey var id : Int = 0,
    var index : String = "",
    var company : String = "",
    var link : String = "",
    var title : String = "",
    var year : String ="",
    var path : String = "",
    @Ignore var category : HashMap<String, Boolean> = HashMap()
) : Serializable, Comparable<ArticlePreview> {
    override fun compareTo(other: ArticlePreview): Int {
        return if(year < other.year) 1
        else if(year == other.year) 0
        else -1
    }
}
