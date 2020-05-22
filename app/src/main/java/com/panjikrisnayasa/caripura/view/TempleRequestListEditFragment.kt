package com.panjikrisnayasa.caripura.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.panjikrisnayasa.caripura.R
import com.panjikrisnayasa.caripura.adapter.TempleRequestListAdapter
import com.panjikrisnayasa.caripura.util.SharedPrefManager
import com.panjikrisnayasa.caripura.viewmodel.TempleRequestListViewModel
import kotlinx.android.synthetic.main.fragment_temple_request_list_edit.*

/**
 * A simple [Fragment] subclass.
 */
class TempleRequestListEditFragment : Fragment() {

    private lateinit var mViewModel: TempleRequestListViewModel
    private lateinit var mAdapter: TempleRequestListAdapter
    private lateinit var mSharedPref: SharedPrefManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_temple_request_list_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mSharedPref = SharedPrefManager.getInstance(context)

        showRecyclerView()

        mViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(TempleRequestListViewModel::class.java)

        mViewModel.getEditTempleRequestList().observe(this, Observer { templeList ->
            progress_temple_request_list_edit.visibility = View.GONE
            if (templeList != null) {
                mAdapter.setData(templeList)
            } else {
                text_temple_request_list_edit_no_requests.visibility = View.VISIBLE
            }
        })
    }

    private fun showRecyclerView() {
        mAdapter = TempleRequestListAdapter()
        recycler_temple_request_list_edit?.layoutManager = LinearLayoutManager(context)
        recycler_temple_request_list_edit?.adapter = mAdapter
    }
}
