package com.example.android.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.GraphItem
import com.example.android.GraphView
import com.example.android.R
import com.example.android.activity.ArticleActivity
import com.example.android.interfaces.OnListFragmentInteractionListener
import com.example.android.interfaces.SmallItemRecyclerViewAdapter
import com.example.android.model.ArticlePreview
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.firestore.*
import com.google.firestore.v1.Document
import kotlinx.android.synthetic.main.fragment_news.*
import kotlinx.android.synthetic.main.fragment_news.view.*
import kotlinx.android.synthetic.main.news_item_list.*
import kotlinx.android.synthetic.main.news_item_list.view.*


class NewsFragment : Fragment(), OnListFragmentInteractionListener {
    private val upHeight: Int by lazy {
        graph_layout.height - BottomSheetBehavior.from<View>(bottom_sheet).peekHeight
    }

    private val dbF: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    private val behavior: BottomSheetBehavior<LinearLayout> by lazy {
        BottomSheetBehavior.from(v.bottom_sheet)
    }
    private lateinit var listener: OnListFragmentInteractionListener
    private lateinit var v: View
    private lateinit var ref: DocumentReference
    private val path = ArrayList<String>()
    private var isLoading = false
    private val list =  ArrayList<ArticlePreview>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_news, container, false)
        graphLayoutInit()
        bottomSheetInit()

        v.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager =
                    v.recyclerView.layoutManager as LinearLayoutManager?

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == list.size - 1) {
                        dataMore()
                        isLoading = true
                    }
                }

            }
        })


            return v
    }


    private fun graphLayoutInit() {
        v.graph_view.actionListener = object : GraphView.GraphViewActionListener {
            override fun onTopicClicked(item: GraphItem) {
                path.add(item.name)
                getNewCategory(item)
                getList()
            }

            override fun onFocusingTopicChanged(before: GraphItem?, after: GraphItem?) {
                if (path.isEmpty()) {
                    behavior.state = BottomSheetBehavior.STATE_HIDDEN
                    return
                }
                path.removeAt(path.size - 1)
                if (path.isEmpty()) {
                    behavior.state = BottomSheetBehavior.STATE_HIDDEN
                    return
                }

                getList()
            }
        }

        update()
    }

    private fun bottomSheetInit() {
        listener = this

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
        v.graph_view.visibility = View.GONE
        v.progress_bar.visibility = View.VISIBLE

        ref = dbF.collection("category").document("sub")

        ref.get().addOnSuccessListener {

            val list = it.get("category") as List<*>
            val itemList = ArrayList<GraphItem>()
            list.forEach { item ->
                itemList.add(GraphItem(item as String, null))
            }
            v.graph_view.itemList = itemList
            v.progress_bar.visibility = View.GONE
            v.graph_view.visibility = View.VISIBLE

        }
    }


    private fun getNewCategory(parent: GraphItem) {
        v.graph_view.visibility = View.GONE
        v.progress_bar.visibility = View.VISIBLE

        var newRef = ref
        path.forEach { cur ->
            newRef = newRef.collection(cur).document("sub")
        }

        newRef.get().addOnSuccessListener {

            val list = it.get("category") as List<*>?
            val itemList = ArrayList<GraphItem>()
            list?.forEach { item ->
                val i = GraphItem(item as String, null)
                i.parent = parent
                itemList.add(i)
            }
            parent.children = itemList
            v.graph_view.updateLayout()
            v.progress_bar.visibility = View.GONE
            v.graph_view.visibility = View.VISIBLE

        }
    }

    private fun getList() {
        v.recyclerView.showShimmerAdapter()
        behavior.state = BottomSheetBehavior.STATE_COLLAPSED

        val recycle = recyclerView as RecyclerView
        val ref = dbF.collection("news")

        var q: Query? = null
        var task: Task<QuerySnapshot>? = null

        if (path.isNotEmpty()) {
            q = ref.whereEqualTo("search", path[0]);
            for (i in 1 until path.size) {
                q = q?.whereEqualTo("category.${path[i]}", true)
            }
            q = q?.limit(20)
            task = q?.get()

        } else {
            task = ref.get()
        }

        list.clear()
        task?.addOnSuccessListener {
            for (document in it) {
                val i = document.toObject(ArticlePreview::class.java)
                if(path.isNotEmpty()) {
                    i.path = path[0]
                    i.id = i.index.toInt()
                }
                list.add(i)
            }
            with(recycle) {
                layoutManager = LinearLayoutManager(context)
                adapter =
                    SmallItemRecyclerViewAdapter(
                        list,
                        listener,
                        R.layout.news_item,
                        requireActivity()
                    )
            }
        }
    }

    private fun dataMore() {
//        v.recyclerView.showShimmerAdapter()

        val recycle = recyclerView as RecyclerView
        val ref = dbF.collection("news")

        var q: Query? = null
        var task: Task<QuerySnapshot>? = null

        if (path.isNotEmpty()) {
            q = ref.whereEqualTo("search", path[0]);
            for (i in 1 until path.size) {
                q = q?.whereEqualTo("category.${path[i]}", true)
            }
            q = q?.limit(list.size.toLong() + 20)
            task = q?.get()

        } else {
            task = ref.get()
        }

        task?.addOnSuccessListener {
            val size = list.size
            val min = if(it.size() > size + 20) size + 20 else it.size()
            for (k in size until min) {
                val i = it.elementAt(k).toObject(ArticlePreview::class.java)
                if(path.isNotEmpty()) {
                    i.path = path[0]
                    i.id = i.index.toInt()
                }
                list.add(i)
            }
            recycle.adapter?.notifyDataSetChanged();
            isLoading = false
        }


    }

    override fun onListFragmentInteraction(id: ArticlePreview) {
        val intent = Intent(activity, ArticleActivity::class.java)
        intent.putExtra("id", id)
        startActivity(intent);
    }

}