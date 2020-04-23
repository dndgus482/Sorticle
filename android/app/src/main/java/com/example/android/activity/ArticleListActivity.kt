package com.example.android.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.example.android.R
import com.example.android.adapter.ArticleListAdapter
import com.example.android.model.ArticleItem

class ArticleListActivity : AppCompatActivity() {

    val list = ArrayList<ArticleItem.Item>();
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_list)

        list.add(ArticleItem.Item("aaa", "bbb"))
        list.add(ArticleItem.Item("ccc", "ddd"))
        list.add(ArticleItem.Item("eee", "fff"))
        list.add(ArticleItem.Item("ggg", "hhh"))

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.adapter = ArticleListAdapter(list)

    }
}
