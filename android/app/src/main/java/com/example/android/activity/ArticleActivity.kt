package com.example.android.activity

import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.example.android.BookmarkService
import com.example.android.R
import com.example.android.model.AppDatabase
import com.example.android.model.ArticlePreview
import com.example.android.model.Bookmark
import com.example.android.model.History
import com.example.android.share
import kotlinx.android.synthetic.main.activity_article.*


class ArticleActivity : AppCompatActivity() {

    private val db: AppDatabase by lazy {
        AppDatabase.getInstance(this)!!
    }

    private val article: ArticlePreview by lazy {
        intent.getSerializableExtra("id") as ArticlePreview
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article)
        setSupportActionBar(toolbar)
        title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true);

        progress_bar.visibility = View.VISIBLE
        article_webview.settings.setAppCacheEnabled(true);
        article_webview.settings.loadsImagesAutomatically = true;
        article_webview.settings.javaScriptEnabled = true;


        article_webview.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, progress: Int) {
                if (progress < 100 && progress_bar.visibility == ProgressBar.GONE) {
                    progress_bar.visibility = ProgressBar.VISIBLE
                }
                progress_bar.progress = progress
                if (progress == 100) {
                    progress_bar.visibility = ProgressBar.GONE
                }
            }

        }

        article_webview.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                return false
            }
        }
        article_webview.loadUrl(article.link)

        db.runInTransaction {
            db.articleDao().insertAll(article)
            db.historyDao().insertWithTimestamp(History(article.id))
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
                    BookmarkService.add(db, article)
                } else {
                    item.setIcon(R.drawable.ic_bookmark_black_24dp)
                    BookmarkService.delete(db, article)
                }
                true
            }

            R.id.article_action_share -> {
                share(this, article.link)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val bookmarked :Bookmark? = db.bookmarkDao().findById(article.id)
        if(bookmarked == null) {
            menu?.getItem(0)?.isChecked = false
        } else {
            menu?.getItem(0)?.isChecked = true
            menu?.getItem(0)?.setIcon(R.drawable.ic_bookmark_pink_24dp)
        }

        return super.onPrepareOptionsMenu(menu)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_DOWN) {
            when (keyCode) {
                KeyEvent.KEYCODE_BACK -> {
                    if (article_webview.canGoBack()) {
                        article_webview.goBack()
                    } else {
                        finish()
                    }
                    return true
                }
            }
        }
        return super.onKeyDown(keyCode, event)
    }

}