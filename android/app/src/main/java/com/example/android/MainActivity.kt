package com.example.android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    var count = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        add_button.setOnClickListener {
            count++
            if(count > 8) count = 8
            update()
        }

        subtract_button.setOnClickListener {
            count--
            if(count <= 0) count = 1
            update()
        }

        graph_view.actionListener = object : GraphView.GraphViewActionListener {
            override fun onTopicClicked(item: GraphItem) {
                Log.i("MainActivity", "onTopicClicked : " + item.name)
            }

            override fun onFocusingTopicChanged(before: GraphItem?, after: GraphItem?) {
                Log.i("MainActivity", "onFocusingTopicChanged : " + (before?.name ?: "root") + " -> " + (after?.name ?: "root"))
            }
        }

        update()
    }

    private fun update() {
        val itemList = ArrayList<GraphItem>()
        for(i in 0 until count) {
            itemList.add(GraphItem("Item $i", arrayListOf(
                GraphItem("Item $i-0", null),
                GraphItem("Item $i-1", null),
                GraphItem("Item $i-2", null),
                GraphItem("Item $i-3", null)
            )))
        }
        graph_view.itemList = itemList
    }
}
