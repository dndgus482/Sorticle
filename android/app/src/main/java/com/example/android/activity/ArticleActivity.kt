package com.example.android.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.android.R
import com.example.android.fragment.ItemFragment

class ArticleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article)
        var articleListFragment = ItemFragment()
        supportFragmentManager.beginTransaction().add(R.id.articleListContainer, articleListFragment).commit()
    }
}
