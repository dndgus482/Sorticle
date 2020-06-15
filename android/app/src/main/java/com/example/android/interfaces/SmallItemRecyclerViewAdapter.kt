package com.example.android.interfaces

import android.app.Activity
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.android.model.AppDatabase


import com.example.android.model.ArticlePreview
import kotlinx.android.synthetic.main.history_item.view.*

import kotlinx.android.synthetic.main.news_item.view.article_title

class SmallItemRecyclerViewAdapter(
    private val mList: List<ArticlePreview>,
    private val mListener: OnListFragmentInteractionListener,
    private val mLayout : Int,
    private val activity : Activity
) : RecyclerView.Adapter<SmallItemRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener
    private val db = AppDatabase.getInstance(activity)!!
    init {
        mOnClickListener = View.OnClickListener {
            val item = it.tag as ArticlePreview
            mListener.onListFragmentInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(mLayout, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mList[position]
        holder.title.text = item.title
        holder.year.text = item.year
        holder.company.text = item.company
        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mList.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val title: TextView = mView.article_title
        val year: TextView = mView.article_year
        val company : TextView = mView.article_company
    }
}
