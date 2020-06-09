package com.example.android

import com.example.android.model.AppDatabase
import com.example.android.model.ArticlePreview
import com.example.android.model.Bookmark

class BookmarkService {

    companion object {
        fun init(db : AppDatabase, article : ArticlePreview, bookmark : BookmarkButton) {
            val bookmarked : Bookmark? = db.bookmarkDao().findById(article.Unnamed)
            bookmark.checked = bookmarked != null
        }

        fun add(db : AppDatabase, article : ArticlePreview) {
            db.runInTransaction {
                db.articleDao().insertAll(article)
                db.bookmarkDao().insertAll(Bookmark(article.Unnamed))
            }
        }

        fun delete(db :AppDatabase, article: ArticlePreview) {
            db.runInTransaction {
                db.bookmarkDao().delete(Bookmark(article.Unnamed))
            }
        }
    }
}