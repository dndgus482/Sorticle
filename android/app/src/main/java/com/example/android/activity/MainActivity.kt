package com.example.android.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.android.R
import com.example.android.fragment.ItemFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ItemFragment.OnListFragmentInteractionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var articleListFragment = ItemFragment()
        supportFragmentManager.beginTransaction().add(R.id.articleListContainer, articleListFragment).commit()

        main_button.setOnClickListener { articleListFragment.getList() }
    }

    override fun onListFragmentInteraction(id : Int) {
        val intent = Intent(this, ArticleActivity::class.java)
        intent.putExtra("id", id);
        startActivity(intent);
    }

}
