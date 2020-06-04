package com.example.android.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.android.R
import com.example.android.fragment.BookmarkFragment
import com.example.android.fragment.HistoryFragment
import com.example.android.fragment.NewsFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val newsFragment = NewsFragment()
        val historyFragment = HistoryFragment()
        val bookmarkFragment = BookmarkFragment()

        supportFragmentManager.beginTransaction().replace(R.id.main_frame, newsFragment)
            .commitAllowingStateLoss()

        bottom_navi.setOnNavigationItemSelectedListener {
            if (it.itemId == R.id.action_news) {
                supportFragmentManager.beginTransaction().replace(R.id.main_frame, newsFragment)
                    .commitAllowingStateLoss()
                return@setOnNavigationItemSelectedListener true
            } else if(it.itemId == R.id.action_bookmark) {
                supportFragmentManager.beginTransaction().replace(R.id.main_frame, bookmarkFragment)
                    .commitAllowingStateLoss()
                return@setOnNavigationItemSelectedListener true

            } else if(it.itemId == R.id.action_history) {
                supportFragmentManager.beginTransaction().replace(R.id.main_frame, historyFragment)
                    .commitAllowingStateLoss()
                return@setOnNavigationItemSelectedListener true
            }
            else {
                return@setOnNavigationItemSelectedListener false
            }
        }

    }


}