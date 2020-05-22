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
import com.panjikrisnayasa.caripura.adapter.TempleRequestHistoryAdminAdapter
import com.panjikrisnayasa.caripura.adapter.TempleRequestHistoryContributorAdapter
import com.panjikrisnayasa.caripura.util.SharedPrefManager
import com.panjikrisnayasa.caripura.viewmodel.TempleRequestHistoryViewModel
import kotlinx.android.synthetic.main.fragment_temple_request_history_delete.*

/**
 * A simple [Fragment] subclass.
 */
class TempleRequestHistoryDeleteFragment : Fragment() {

    private lateinit var mViewModel: TempleRequestHistoryViewModel
    private lateinit var mSharedPref: SharedPrefManager
    private lateinit var mAdminAdapter: TempleRequestHistoryAdminAdapter
    private lateinit var mContributorAdapter: TempleRequestHistoryContributorAdapter

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

        mViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(TempleRequestHistoryViewModel::class.java)

        if (mSharedPref.getRole() == "admin") {
            showRecyclerViewAdmin()
            mViewModel.getDeleteTempleApprovalHistory().observe(this, Observer { templeList ->
                progress_temple_request_history_delete.visibility = View.GONE
                if (templeList != null) {
                    mAdminAdapter.setData(templeList)
                } else {
                    text_temple_request_history_delete_no_histories.visibility = View.VISIBLE
                }
            })
        } else {
            showRecyclerViewContributor()
            mViewModel.getDeleteTempleRequestHistory(mSharedPref.getId())
                .observe(this, Observer { templeList ->
                    progress_temple_request_history_delete.visibility = View.GONE
                    if (templeList != null) {
                        mContributorAdapter.setData(templeList)
                    } else {
                        text_temple_request_history_delete_no_histories.visibility = View.VISIBLE
                    }
                })
        }
    }

    private fun showRecyclerViewAdmin() {
        mAdminAdapter = TempleRequestHistoryAdminAdapter()
        recycler_temple_request_history_delete?.layoutManager = LinearLayoutManager(context)
        recycler_temple_request_history_delete?.adapter = mAdminAdapter
    }

    private fun showRecyclerViewContributor() {
        mContributorAdapter = TempleRequestHistoryContributorAdapter()
        recycler_temple_request_history_delete?.layoutManager = LinearLayoutManager(context)
        recycler_temple_request_history_delete?.adapter = mContributorAdapter
    }
}
