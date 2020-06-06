package com.example.android.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.android.R
import com.example.android.activity.ArticleActivity
import com.example.android.interfaces.OnListFragmentInteractionListener
import com.example.android.model.AppDatabase
import com.example.android.model.ArticlePreview
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.history_item_list.view.*
import kotlinx.android.synthetic.main.news_item_list.*
import kotlinx.android.synthetic.main.news_item_list.view.*
import kotlinx.android.synthetic.main.news_item_list.view.recyclerView

class HistoryFragment : Fragment(), OnListFragmentInteractionListener {

    private lateinit var listener: OnListFragmentInteractionListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.history_item_list, container, false)

        v.history_recyclerview.showShimmerAdapter()
        val db = Room.databaseBuilder(
            requireActivity().applicationContext,
            AppDatabase::class.java, "database-name"
        ).allowMainThreadQueries()
            .build()

        listener = this
        val list = db.historyDao().getAll()

        val recycle = v.history_recyclerview as RecyclerView
        with(recycle) {
            layoutManager = LinearLayoutManager(context)
            adapter = MyItemRecyclerViewAdapter(list, listener, R.layout.history_item)
        }

        return v
    }

    override fun onListFragmentInteraction(id: ArticlePreview) {
        val intent = Intent(activity, ArticleActivity::class.java)
        intent.putExtra("id", id);
        startActivity(intent)
    }


}