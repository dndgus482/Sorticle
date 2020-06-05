package com.example.android.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.GraphItem
import com.example.android.GraphView
import com.example.android.R
import com.example.android.activity.ArticleActivity
import com.example.android.interfaces.OnListFragmentInteractionListener
import com.example.android.model.ArticlePreview
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.news_item_list.*
import kotlinx.android.synthetic.main.news_item_list.view.*
import kotlinx.android.synthetic.main.fragment_news.*
import kotlinx.android.synthetic.main.fragment_news.view.*

class NewsFragment : Fragment(), OnListFragmentInteractionListener {
    private val upHeight: Int by lazy {
        graph_layout.height - BottomSheetBehavior.from<View>(bottom_sheet).peekHeight
    }

    private val databaseReference: FirebaseDatabase by lazy {
        FirebaseDatabase.getInstance()
    }

    var count = 1
    private lateinit var listener: OnListFragmentInteractionListener
    private lateinit var v: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_news, container, false)
        graphLayoutInit()
        bottomSheetInit()
        return v
    }


    private fun graphLayoutInit() {

        v.add_button.setOnClickListener {
            count++
            if (count > 8) count = 8
            update()
        }

        v.subtract_button.setOnClickListener {
            count--
            if (count <= 0) count = 1
            update()
        }

        v.graph_view.actionListener = object : GraphView.GraphViewActionListener {
            override fun onTopicClicked(item: GraphItem) {
                Log.i("MainActivity", "onTopicClicked : " + item.name)
            }

            override fun onFocusingTopicChanged(before: GraphItem?, after: GraphItem?) {
                Log.i("MainActivity",
                    "onFocusingTopicChanged : " + (before?.name ?: "root") + " -> " + (after?.name
                        ?: "root")
                )
            }
        }

        update()
    }

    private fun bottomSheetInit() {
        listener = this
        val behavior = BottomSheetBehavior.from(v.bottom_sheet)

        v.main_button.setOnClickListener {
            getList("코로나/백신/개발")
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (behavior.state) {
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        button_expand.setImageResource(R.drawable.ic_expand_less_24px)
                        graph_layout.layoutParams = CoordinatorLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            upHeight
                        )
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        button_expand.setImageResource(R.drawable.ic_expand_more_24px)
                    }

                    BottomSheetBehavior.STATE_HIDDEN -> {
                        graph_layout.layoutParams = CoordinatorLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT
                        )
                    }
                }
            }
        })

        v.button_expand.setOnClickListener {
            when (behavior.state) {
                BottomSheetBehavior.STATE_COLLAPSED -> {
                    behavior.state = BottomSheetBehavior.STATE_EXPANDED
                    button_expand.setImageResource(R.drawable.ic_expand_less_24px)
                }
                BottomSheetBehavior.STATE_EXPANDED -> {
                    behavior.state = BottomSheetBehavior.STATE_COLLAPSED
                    button_expand.setImageResource(R.drawable.ic_expand_more_24px)
                }

            }
        }
        behavior.state = BottomSheetBehavior.STATE_HIDDEN
        v.recyclerView.setDemoShimmerDuration(0)

    }

    private fun update() {
        val itemList = ArrayList<GraphItem>()
        for (i in 0 until count) {
            itemList.add(
                GraphItem(
                    "Item $i", arrayListOf(
                        GraphItem("Item $i-0", null),
                        GraphItem("Item $i-1", null),
                        GraphItem("Item $i-2", null),
                        GraphItem("Item $i-3", null)
                    )
                )
            )
        }
        v.graph_view.itemList = itemList
    }

    private fun getList(q: String) {
        v.recyclerView.showShimmerAdapter()
        val recycle = recyclerView as RecyclerView
        val ref = databaseReference.getReference(q)
        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(data: DataSnapshot) {
                val list = ArrayList<ArticlePreview>();
                data.children.forEach {
                    val article = it.getValue(ArticlePreview::class.java)
                    article?.let {
                        list.add(article)
                        Log.d("aaa", article.toString())
                    }
                }

                with(recycle) {
                    layoutManager = LinearLayoutManager(context)
                    adapter = MyItemRecyclerViewAdapter(list, listener, R.layout.news_item)
                }
            }

        })
    }

    override fun onListFragmentInteraction(id: ArticlePreview) {
        val intent = Intent(activity, ArticleActivity::class.java)
        intent.putExtra("id", id);
        startActivity(intent);
    }
}

