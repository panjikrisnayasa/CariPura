package com.panjikrisnayasa.caripura.view

import android.content.Intent
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
import kotlinx.android.synthetic.main.fragment_temple_request_list_add.*

/**
 * A simple [Fragment] subclass.
 */
class TempleRequestListAddFragment : Fragment() {

    private lateinit var mViewModel: TempleRequestListViewModel
    private lateinit var mAdapter: TempleRequestListAdapter
    private lateinit var mSharedPref: SharedPrefManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_temple_request_list_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mSharedPref = SharedPrefManager.getInstance(context)

        showRecyclerView()

        mViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(TempleRequestListViewModel::class.java)

        mViewModel.getAddTempleRequestList().observe(this, Observer { templeList ->
            progress_temple_request_list_add.visibility = View.GONE
            if (templeList != null) {
                recycler_temple_request_list_add.visibility = View.VISIBLE
                text_temple_request_list_add_no_requests.visibility = View.GONE
                mAdapter.setData(templeList)
            } else {
                recycler_temple_request_list_add.visibility = View.GONE
                text_temple_request_list_add_no_requests.visibility = View.VISIBLE
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null)
            if (requestCode == TempleRequestDetailActivity.REQUEST_APPROVAL)
                if (resultCode == TempleRequestDetailActivity.RESULT_APPROVAL) {
                    val position = data.getIntExtra(TempleRequestDetailActivity.EXTRA_POSITION, 0)
                    mAdapter.removeItem(position)
                }
    }

    private fun showRecyclerView() {
        mAdapter = TempleRequestListAdapter(this)
        recycler_temple_request_list_add?.layoutManager = LinearLayoutManager(context)
        recycler_temple_request_list_add?.adapter = mAdapter
    }
}
