package com.example.android.fragment

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.android.R


import com.example.android.model.ArticlePreview

import kotlinx.android.synthetic.main.news_item.view.*
import java.text.SimpleDateFormat
import java.util.*

class MyItemRecyclerViewAdapter(
    private val mValues: List<ArticlePreview.Item>,
    private val mListener: OnListFragmentInteractionListener?
) : RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener
    var today = Date();
    val fmt = SimpleDateFormat("yyyy-MM-dd")
    val fmtToday = SimpleDateFormat("HH-mm")

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as ArticlePreview.Item
            mListener?.onListFragmentInteraction(item.id)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.news_item, parent, false)
        today = Date()
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.title.text = item.title
        if (fmt.format(item.pubDate) == fmt.format(today))
            holder.pubDate.text = fmtToday.format(item.pubDate)
        else
            holder.pubDate.text = fmt.format(item.pubDate)

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val title: TextView = mView.article_list_title
        val pubDate: TextView = mView.article_list_pubDate
    }
}
