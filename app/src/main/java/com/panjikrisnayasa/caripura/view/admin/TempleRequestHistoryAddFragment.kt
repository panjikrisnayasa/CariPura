package com.panjikrisnayasa.caripura.view.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.panjikrisnayasa.caripura.R
import com.panjikrisnayasa.caripura.adapter.admin.TempleRequestHistoryAddAdapter
import com.panjikrisnayasa.caripura.model.Temple
import kotlinx.android.synthetic.main.fragment_temple_request_history_add.*

/**
 * A simple [Fragment] subclass.
 */
class TempleRequestHistoryAddFragment : Fragment() {

    private var mTempleList: ArrayList<Temple> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_temple_request_history_add, container, false)
    }

    private fun showRecyclerView() {
        recycler_temple_request_history_add?.visibility = View.VISIBLE
        text_temple_request_history_add_no_histories?.visibility = View.GONE
        val templeAdapter =
            TempleRequestHistoryAddAdapter(
                mTempleList
            )
        recycler_temple_request_history_add?.layoutManager = LinearLayoutManager(context)
        recycler_temple_request_history_add?.adapter = templeAdapter
    }
}