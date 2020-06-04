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
import com.example.android.errorMessage
import com.example.android.helper.NetworkHelper
import com.example.android.model.ArticlePreview
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.news_item_list.*
import kotlinx.android.synthetic.main.news_item_list.view.*
import kotlinx.android.synthetic.main.fragment_news.*
import kotlinx.android.synthetic.main.fragment_news.view.*
import retrofit2.Call
import retrofit2.Callback

class NewsFragment : Fragment(), OnListFragmentInteractionListener {
    private val upHeight : Int by lazy {
        graph_layout.height - BottomSheetBehavior.from<View>(bottom_sheet).peekHeight
    }
    var count = 1
    private var listener: OnListFragmentInteractionListener? = null
    private lateinit var v : View
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
            if(count > 8) count = 8
            update()
        }

        v.subtract_button.setOnClickListener {
            count--
            if(count <= 0) count = 1
            update()
        }

        v.graph_view.actionListener = object : GraphView.GraphViewActionListener {
            override fun onTopicClicked(item: GraphItem) {
                Log.i("MainActivity", "onTopicClicked : " + item.name)
            }

            override fun onFocusingTopicChanged(before: GraphItem?, after: GraphItem?) {
                Log.i("MainActivity", "onFocusingTopicChanged : " + (before?.name ?: "root") + " -> " + (after?.name ?: "root"))
            }
        }

        update()
    }

    private fun bottomSheetInit() {
        listener = this
        val behavior = BottomSheetBehavior.from(v.bottom_sheet)

        v.main_button.setOnClickListener {
            getList("코로나")
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when(behavior.state) {
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        button_expand.setImageResource(R.drawable.ic_expand_less_24px)
                        graph_layout.layoutParams = CoordinatorLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            upHeight)
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        button_expand.setImageResource(R.drawable.ic_expand_more_24px)
                    }

                    BottomSheetBehavior.STATE_HIDDEN -> {
                        graph_layout.layoutParams = CoordinatorLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT)
                    }
                }
            }
        })

        v.button_expand.setOnClickListener {
            when(behavior.state) {
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
        for(i in 0 until count) {
            itemList.add(GraphItem("Item $i", arrayListOf(
                GraphItem("Item $i-0", null),
                GraphItem("Item $i-1", null),
                GraphItem("Item $i-2", null),
                GraphItem("Item $i-3", null)
            )))
        }
        v.graph_view.itemList = itemList
    }

    private fun getList(q : String) {
        v.recyclerView.showShimmerAdapter()
        val recycle = recyclerView as RecyclerView

        var response = NetworkHelper.apiService.getList(q).enqueue(object :
            Callback<ArticlePreview> {
            override fun onFailure(call: Call<ArticlePreview>, t: Throwable) {
                errorMessage(requireActivity(), "network Failure")
                t.printStackTrace()
            }

            override fun onResponse(
                call: Call<ArticlePreview?>,
                response: retrofit2.Response<ArticlePreview?>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    val list = ArrayList<ArticlePreview.Item>();
                    result?.let {
                        for (x in result.items) {
                            list.add(x)
                        }
                        with(recycle) {
                            layoutManager = LinearLayoutManager(context)
                            adapter = MyItemRecyclerViewAdapter(list, listener)
                        }
                    }
                    if(result == null || result.items.isEmpty()) errorMessage(requireActivity(),"받은 뉴스기사가 없습니다.")
                }
                else {
                    errorMessage(requireActivity(),"${response.code()} error")
                }
            }
        })
    }

    override fun onListFragmentInteraction(id : Int) {
        val intent = Intent(activity, ArticleActivity::class.java)
        intent.putExtra("id", id);
        startActivity(intent);
    }
}

interface OnListFragmentInteractionListener {
    fun onListFragmentInteraction(id : Int)
}
