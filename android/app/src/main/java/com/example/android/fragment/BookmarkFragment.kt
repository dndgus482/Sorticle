package com.example.android.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.android.R
import com.example.android.activity.ArticleActivity
import com.example.android.interfaces.OnListFragmentInteractionListener
import com.example.android.model.AppDatabase
import com.example.android.model.ArticlePreview
import kotlinx.android.synthetic.main.activity_article.*
import kotlinx.android.synthetic.main.history_item_list.view.*

class BookmarkFragment : Fragment(), OnListFragmentInteractionListener {

    private lateinit var listener: OnListFragmentInteractionListener
    private val db: AppDatabase by lazy {
        AppDatabase.getInstance(requireContext())!!
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).title = "Bookmark"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.bookmark_item_list, container, false)

        v.recyclerview.showShimmerAdapter()

        listener = this
        val recycle = v.recyclerview as RecyclerView

//        val list = db.bookmarkDao().getAll()
        db.bookmarkDao().getAll().observe(this, Observer {list ->
            with(recycle) {
                layoutManager = LinearLayoutManager(context)
                adapter = MyItemRecyclerViewAdapter(list, listener, R.layout.bookmark_item)
            }
        })

        return v
    }

    override fun onListFragmentInteraction(id: ArticlePreview) {
        val intent = Intent(activity, ArticleActivity::class.java)
        intent.putExtra("id", id);
        startActivity(intent)
    }



}