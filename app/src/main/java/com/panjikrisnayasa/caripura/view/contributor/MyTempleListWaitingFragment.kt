package com.panjikrisnayasa.caripura.view.contributor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.panjikrisnayasa.caripura.R
import com.panjikrisnayasa.caripura.adapter.contributor.ContributorMyTempleListWaitingAdapter
import com.panjikrisnayasa.caripura.model.Temple
import kotlinx.android.synthetic.main.fragment_contributor_my_temple_list_waiting.*

/**
 * A simple [Fragment] subclass.
 */
class MyTempleListWaitingFragment : Fragment() {

    private var mTempleList: ArrayList<Temple> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(
            R.layout.fragment_contributor_my_temple_list_waiting,
            container,
            false
        )
    }

    private fun showRecyclerView() {
        recycler_contributor_my_temple_list_waiting?.visibility = View.VISIBLE
        text_contributor_my_temple_list_waiting_no_data?.visibility = View.GONE
        val templeAdapter =
            ContributorMyTempleListWaitingAdapter(
                mTempleList
            )
        recycler_contributor_my_temple_list_waiting?.layoutManager = LinearLayoutManager(context)
        recycler_contributor_my_temple_list_waiting?.adapter = templeAdapter
    }
}
