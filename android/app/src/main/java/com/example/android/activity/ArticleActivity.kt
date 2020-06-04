package com.example.android.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import androidx.core.text.HtmlCompat.fromHtml
import androidx.recyclerview.widget.RecyclerView
import com.example.android.R
import com.example.android.helper.NetworkHelper
import com.example.android.model.Article
import com.example.android.model.ArticlePreview
import kotlinx.android.synthetic.main.activity_article.*
import retrofit2.Call
import retrofit2.Callback

class ArticleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article)

        val id = intent.getIntExtra("id", 0)
        var errorMessage = {msg : String ->
            val myToast: Toast = Toast.makeText(
                applicationContext,
                msg,
                Toast.LENGTH_LONG
            )
            myToast.setGravity(Gravity.CENTER, 0, 0)
            myToast.show()
        }
        val response = NetworkHelper.apiService.getNews(id).enqueue(object :
            Callback<Article> {
            override fun onFailure(call: Call<Article>, t: Throwable) {
                errorMessage("network Failure")
                t.printStackTrace()
            }

            @SuppressLint("WrongConstant")
            override fun onResponse(
                call: Call<Article?>,
                response: retrofit2.Response<Article?>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    val list = ArrayList<ArticlePreview.Item>()
                    val webView = article_article_body
                    webView.settings.javaScriptEnabled = true
                    webView.settings.domStorageEnabled = true
                    result?.let{
                        article_source.text = it.source
                        article_title.text = it.title
                        article_pubDate.text = it.pubDate.toString()
                        article_originallink.text = it.originallink
                        webView.loadData(fromHtml(it.article_body, Html.FROM_HTML_MODE_LEGACY).toString(), "text/html", "UTF-8")

                    }
                }
                else {
                    errorMessage("${response.code()} error")
                }
            }
        })
    }
}