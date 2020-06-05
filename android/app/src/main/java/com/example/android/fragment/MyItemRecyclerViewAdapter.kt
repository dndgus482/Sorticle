package com.example.android.fragment

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.android.R


import com.example.android.model.ArticlePreview

import kotlinx.android.synthetic.main.news_item.view.*

class MyItemRecyclerViewAdapter(
    private val mList: List<ArticlePreview>,
    private val mListener: OnListFragmentInteractionListener?
) : RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener {
            val item = it.tag as ArticlePreview
            mListener?.onListFragmentInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.news_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mList[position]
        holder.title.text = item.title
        holder.pubDate.text = item.years
        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mList.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val title: TextView = mView.article_list_title
        val pubDate: TextView = mView.article_list_pubDate
    }
}
