package com.example.android.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Toast
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

            override fun onResponse(
                call: Call<Article?>,
                response: retrofit2.Response<Article?>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    val list = ArrayList<ArticlePreview.Item>();
                    result?.let{
                        article_source.text = it.source
                        article_title.text = it.title
                        article_pubDate.text = it.pubDate.toString()
                        article_originallink.text = it.originallink
                    }
                }
                else {
                    errorMessage("${response.code()} error")
                }
            }
        })
    }
}