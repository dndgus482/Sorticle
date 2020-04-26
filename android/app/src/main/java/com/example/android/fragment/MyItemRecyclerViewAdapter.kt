package com.example.android.fragment

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.android.R


import com.example.android.fragment.ItemFragment.OnListFragmentInteractionListener
import com.example.android.model.ArticlePreview
import kotlinx.android.synthetic.main.activity_article.view.*
import kotlinx.android.synthetic.main.fragment_article.view.*

import kotlinx.android.synthetic.main.fragment_item.view.*

class MyItemRecyclerViewAdapter(
    private val mValues: List<ArticlePreview.Item>,
    private val mListener: OnListFragmentInteractionListener?
) : RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener


    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as ArticlePreview.Item
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onListFragmentInteraction(item)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.title.text = item.title
        holder.pubDate.text = item.pubDate.toString();

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val id: Int = 1;
        val title: TextView = mView.article_title
        val pubDate: TextView = mView.article_pubDate
    }
}
