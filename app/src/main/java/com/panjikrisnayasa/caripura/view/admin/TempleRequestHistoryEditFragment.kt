package com.panjikrisnayasa.caripura.view.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.panjikrisnayasa.caripura.R
import com.panjikrisnayasa.caripura.adapter.admin.TempleRequestHistoryEditAdapter
import com.panjikrisnayasa.caripura.model.Temple
import kotlinx.android.synthetic.main.fragment_temple_request_history_edit.*

/**
 * A simple [Fragment] subclass.
 */
class TempleRequestHistoryEditFragment : Fragment() {

    private var mTempleList: ArrayList<Temple> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_temple_request_history_edit, container, false)
    }

    private fun showRecyclerView() {
        recycler_temple_request_history_edit?.visibility = View.VISIBLE
        text_temple_request_history_edit_no_histories?.visibility = View.GONE
        val templeAdapter =
            TempleRequestHistoryEditAdapter(
                mTempleList
            )
        recycler_temple_request_history_edit?.layoutManager = LinearLayoutManager(context)
        recycler_temple_request_history_edit?.adapter = templeAdapter
    }
}
