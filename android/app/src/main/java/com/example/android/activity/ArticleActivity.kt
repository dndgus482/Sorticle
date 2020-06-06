package com.example.android.activity

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.room.Database
import androidx.room.Room
import com.example.android.R
import com.example.android.model.AppDatabase
import com.example.android.model.ArticlePreview
import com.example.android.model.Bookmark
import com.example.android.model.History
import kotlinx.android.synthetic.main.activity_article.*


class ArticleActivity : AppCompatActivity() {

    private val db: AppDatabase by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "database-name"
        ).allowMainThreadQueries()
            .build()
    }

    private val article: ArticlePreview by lazy {
        intent.getSerializableExtra("id") as ArticlePreview
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);

        if (Build.VERSION.SDK_INT >= 21) {
            article_webview.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW;
        }
        article_webview.settings.domStorageEnabled = true;
        article_webview.settings.useWideViewPort = true;
        article_webview.settings.setAppCacheEnabled(true);
        article_webview.settings.loadsImagesAutomatically = true;
        article_webview.settings.javaScriptEnabled = true;

        article_webview.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                return false
            }
        }
        article_webview.loadUrl(article.link)

        db.runInTransaction {
            db.articleDao().insertAll(article)
            db.historyDao().insertAll(History(article.Unnamed))
            db.historyDao().deleteOld()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.article_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }

            R.id.article_action_bookmark -> {
                item.isChecked = !item.isChecked

                if (item.isChecked) {
                    item.setIcon(R.drawable.ic_bookmark_pink_24dp)
                    addBookmark()
                } else {
                    item.setIcon(R.drawable.ic_bookmark_black_24dp)
                    deleteBookmark()
                }

                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {

        val bookmarked = db.bookmarkDao().findById(article.Unnamed)
        if(bookmarked == null) {
            menu?.getItem(0)?.isChecked = false
        } else {
            menu?.getItem(0)?.isChecked = true
            menu?.getItem(0)?.setIcon(R.drawable.ic_bookmark_pink_24dp)
        }

        return super.onPrepareOptionsMenu(menu)
    }

    private fun addBookmark() {
        db.runInTransaction {
            db.articleDao().insertAll(article)
            db.bookmarkDao().insertAll(Bookmark(article.Unnamed))
        }
    }

    private fun deleteBookmark() {
        db.runInTransaction {
            db.bookmarkDao().delete(Bookmark(article.Unnamed))
        }
    }

}