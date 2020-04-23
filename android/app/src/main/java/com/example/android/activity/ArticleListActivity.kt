package com.example.android.activity

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.android.R
import com.example.android.adapter.ArticleListAdapter
import com.example.android.helper.NetworkHelper
import com.example.android.model.ArticleItem
import retrofit2.Call
import retrofit2.Callback


class ArticleListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_list)

        var errorMessage = {msg : String ->
            val myToast: Toast = Toast.makeText(
                this@ArticleListActivity,
                msg,
                Toast.LENGTH_LONG
            )
            myToast.setGravity(Gravity.CENTER, 0, 0)
            myToast.show()
        }
        var response = NetworkHelper.apiService.getList().enqueue(object : Callback<ArticleItem?> {
            override fun onFailure(call: Call<ArticleItem?>, t: Throwable) {
                errorMessage("network Failure")
            }

            override fun onResponse(
                call: Call<ArticleItem?>,
                response: retrofit2.Response<ArticleItem?>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    val list = ArrayList<ArticleItem.Item>();
                    result?.let{
                        for(x in result.body) {
                            list.add(x)
                        }
                        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
                        recyclerView.adapter = ArticleListAdapter(list)
                    }
                    if(result == null || result.body == null || result.body.size == 0 ) errorMessage("받은 뉴스기사가 없습니다.")
                }
                else {
                    errorMessage("${response.code()} error")
                }
            }
        })
    }
}
