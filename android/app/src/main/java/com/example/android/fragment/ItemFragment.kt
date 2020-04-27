package com.example.android.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.android.R
import com.example.android.helper.NetworkHelper

import com.example.android.model.ArticlePreview
import kotlinx.android.synthetic.main.fragment_item_list.view.recyclerView
import retrofit2.Call
import retrofit2.Callback
import kotlin.collections.ArrayList

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [ItemFragment.OnListFragmentInteractionListener] interface.
 */
class ItemFragment : Fragment() {

    var thisView : View? = null
    private var listener: OnListFragmentInteractionListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        thisView = inflater.inflate(R.layout.fragment_item_list, container, false)
        return thisView
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    fun getList(q : String) {
        val recycle = thisView?.recyclerView as RecyclerView

        var errorMessage = {msg : String ->
            val myToast: Toast = Toast.makeText(
                thisView?.context,
                msg,
                Toast.LENGTH_LONG
            )
            myToast.setGravity(Gravity.CENTER, 0, 0)
            myToast.show()
        }
        var response = NetworkHelper.apiService.getList(q).enqueue(object : Callback<ArticlePreview> {
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
                    result?.let{
                        for(x in result.items) {
                            list.add(x)
                        }
                        if (recycle is RecyclerView) {
                            with(recycle) {
                                layoutManager = LinearLayoutManager(context)
                                adapter = MyItemRecyclerViewAdapter(list, listener)
                            }
                        }
                    }
                    if(result == null || result.items.size == 0 ) errorMessage("받은 뉴스기사가 없습니다.")
                }
                else {
                    errorMessage("${response.code()} error")
                }
            }
        })
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson
     * [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onListFragmentInteraction(id : Int)
    }
}