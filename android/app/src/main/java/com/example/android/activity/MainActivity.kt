package com.example.android.activity

import android.os.Bundle
import android.view.Menu
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

        var newsFragment = NewsFragment()
        var historyFragment: HistoryFragment? = null
        var bookmarkFragment: BookmarkFragment? = null

        supportFragmentManager.beginTransaction().add(R.id.main_frame, newsFragment).commit()


        bottom_navi.setOnNavigationItemSelectedListener {
            if (it.itemId == R.id.action_news) {
                supportFragmentManager.beginTransaction().show(newsFragment).commit()
                if (historyFragment != null) supportFragmentManager.beginTransaction()
                    .hide(historyFragment!!).commit()
                if (bookmarkFragment != null) supportFragmentManager.beginTransaction()
                    .hide(bookmarkFragment!!).commit()
                return@setOnNavigationItemSelectedListener true
            } else if (it.itemId == R.id.action_bookmark) {
                if (bookmarkFragment == null) {
                    bookmarkFragment = BookmarkFragment()
                    supportFragmentManager.beginTransaction()
                        .add(R.id.main_frame, bookmarkFragment!!).commit()
                }
                supportFragmentManager.beginTransaction().hide(newsFragment).commit()
                if (historyFragment != null) supportFragmentManager.beginTransaction()
                    .hide(historyFragment!!).commit()
                if (bookmarkFragment != null) supportFragmentManager.beginTransaction()
                    .show(bookmarkFragment!!).commit()
                return@setOnNavigationItemSelectedListener true

            } else if (it.itemId == R.id.action_history) {
                if (historyFragment == null) {
                    historyFragment = HistoryFragment()
                    supportFragmentManager.beginTransaction()
                        .add(R.id.main_frame, historyFragment!!).commit()
                }

                supportFragmentManager.beginTransaction().hide(newsFragment).commit()
                if (historyFragment != null) supportFragmentManager.beginTransaction()
                    .show(historyFragment!!).commit()
                if (bookmarkFragment != null) supportFragmentManager.beginTransaction()
                    .hide(bookmarkFragment!!).commit()
                return@setOnNavigationItemSelectedListener true
            } else {
                return@setOnNavigationItemSelectedListener false
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.empty_menu, menu)
        return true
    }

}