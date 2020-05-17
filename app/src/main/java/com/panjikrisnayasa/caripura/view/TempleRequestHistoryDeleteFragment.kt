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
import com.panjikrisnayasa.caripura.adapter.TempleRequestHistoryAdapter
import com.panjikrisnayasa.caripura.util.SharedPrefManager
import com.panjikrisnayasa.caripura.viewmodel.TempleRequestHistoryViewModel
import kotlinx.android.synthetic.main.fragment_temple_request_history_delete.*

/**
 * A simple [Fragment] subclass.
 */
class TempleRequestHistoryDeleteFragment : Fragment() {

    private lateinit var mViewModel: TempleRequestHistoryViewModel
    private lateinit var mAdapter: TempleRequestHistoryAdapter
    private lateinit var mSharedPref: SharedPrefManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_temple_request_history_delete, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mSharedPref = SharedPrefManager.getInstance(context)

        showRecyclerView()

        mViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(TempleRequestHistoryViewModel::class.java)

        if (mSharedPref.getRole() == "admin") {
            mViewModel.getDeleteTempleApprovalHistory().observe(this, Observer { templeList ->
                if (templeList != null) {
                    progress_temple_request_history_delete.visibility = View.GONE
                    mAdapter.setData(templeList)
                } else {
                    text_temple_request_history_delete_no_histories.visibility = View.VISIBLE
                }
            })
        } else {
            mViewModel.getDeleteTempleRequestHistory(mSharedPref.getId())
                .observe(this, Observer { templeList ->
                    if (templeList != null) {
                        progress_temple_request_history_delete.visibility = View.GONE
                        mAdapter.setData(templeList)
                    } else {
                        text_temple_request_history_delete_no_histories.visibility = View.VISIBLE
                    }
                })
        }
    }

    private fun showRecyclerView() {
        mAdapter = TempleRequestHistoryAdapter()
        recycler_temple_request_history_delete?.layoutManager = LinearLayoutManager(context)
        recycler_temple_request_history_delete?.adapter = mAdapter
    }
}
