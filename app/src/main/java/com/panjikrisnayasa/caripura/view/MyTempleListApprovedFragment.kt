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
import com.panjikrisnayasa.caripura.adapter.MyTempleListAdapter
import com.panjikrisnayasa.caripura.util.SharedPrefManager
import com.panjikrisnayasa.caripura.viewmodel.MyTempleListViewModel
import kotlinx.android.synthetic.main.fragment_my_temple_list_approved.*

/**
 * A simple [Fragment] subclass.
 */
class MyTempleListApprovedFragment : Fragment() {

    private lateinit var mViewModel: MyTempleListViewModel
    private lateinit var mAdapter: MyTempleListAdapter
    private lateinit var mSharedPref: SharedPrefManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(
            R.layout.fragment_my_temple_list_approved,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mSharedPref = SharedPrefManager.getInstance(context)

        floating_action_button_my_temple_list_approved_add_temple.setOnClickListener {
            val addIntent = Intent(context, AddTempleFirstActivity::class.java)
            startActivity(addIntent)
        }

        showRecyclerView()

        mViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(MyTempleListViewModel::class.java)

        mViewModel.getTempleApprovedList(mSharedPref.getId()).observe(this, Observer { templeList ->
            if (templeList != null) {
                progress_my_temple_list_approved.visibility = View.GONE
                mAdapter.setData(templeList)
            } else {
                text_my_temple_list_approved_no_data.visibility = View.VISIBLE
            }
        })
    }

    private fun showRecyclerView() {
        mAdapter = MyTempleListAdapter()
        recycler_my_temple_list_approved?.layoutManager = LinearLayoutManager(context)
        recycler_my_temple_list_approved?.adapter = mAdapter
    }
}
