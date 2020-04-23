package com.example.android.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android.R
import com.example.android.model.ArticleItem

class ArticleListAdapter(val articleList : ArrayList<ArticleItem.Item>) :
    RecyclerView.Adapter<ArticleListAdapter.Holder>() {

    inner class Holder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        var title = itemView.findViewById<TextView>(R.id.article_title)
        var content = itemView.findViewById<TextView>(R.id.article_content)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_article_list_item, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return articleList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.title?.text = articleList[position].title;
        holder.content?.text = articleList[position].content;
    }

}