package com.example.android.activity

import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.android.R
import com.example.android.model.AppDatabase
import com.example.android.model.ArticlePreview
import com.example.android.model.History
import kotlinx.android.synthetic.main.activity_article.*


class ArticleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);


        val article = intent.getSerializableExtra("id") as ArticlePreview


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

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "database-name"
        ).allowMainThreadQueries()
            .build()


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
            else -> super.onOptionsItemSelected(item)
        }
    }

}