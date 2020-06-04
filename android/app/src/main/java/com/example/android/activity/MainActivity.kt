package com.example.android.activity

import android.R.attr.maxHeight
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.GraphItem
import com.example.android.GraphView
import com.example.android.R
import com.example.android.fragment.MyItemRecyclerViewAdapter
import com.example.android.helper.NetworkHelper
import com.example.android.interfaces.LockBottomSheetBehavior
import com.example.android.model.ArticlePreview
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_item.*
import kotlinx.android.synthetic.main.fragment_item_list.*
import retrofit2.Call
import retrofit2.Callback


class MainActivity : AppCompatActivity(), OnListFragmentInteractionListener {

    private val upHeight : Int by lazy {
        graph_layout.height - BottomSheetBehavior.from<View>(bottom_sheet).peekHeight
    }
    var count = 1
    private var listener: OnListFragmentInteractionListener? = null

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

        listener = this
        val behavior = BottomSheetBehavior.from(bottom_sheet)

        main_button.setOnClickListener {
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
                        graph_layout.layoutParams = CoordinatorLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            upHeight)
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        button_expand.setImageResource(R.drawable.ic_expand_more_24px)
                    }

                    BottomSheetBehavior.STATE_HIDDEN -> {
                        graph_layout.layoutParams = CoordinatorLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT)
                    }
                }
            }
        })

        button_expand.setOnClickListener {
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
        recyclerView.setDemoShimmerDuration(0)

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

    override fun onListFragmentInteraction(id : Int) {
        val intent = Intent(this, ArticleActivity::class.java)
        intent.putExtra("id", id);
        startActivity(intent);
    }

    fun getList(q : String) {
        val recycle = recyclerView as RecyclerView
        val errorMessage = {msg : String ->
            val myToast: Toast = Toast.makeText(
                this,
                msg,
                Toast.LENGTH_LONG
            )
            myToast.setGravity(Gravity.CENTER, 0, 0)
            myToast.show()
        }
        var response = NetworkHelper.apiService.getList(q).enqueue(object :
            Callback<ArticlePreview> {
            override fun onFailure(call: Call<ArticlePreview>, t: Throwable) {
                errorMessage("network Failure")
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
                    if(result == null || result.items.isEmpty()) errorMessage("받은 뉴스기사가 없습니다.")
                }
                else {
                    errorMessage("${response.code()} error")
                }
            }
        })
    }

}
interface OnListFragmentInteractionListener {
    // TODO: Update argument type and name
    fun onListFragmentInteraction(id : Int)
}
