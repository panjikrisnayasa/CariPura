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
import com.panjikrisnayasa.caripura.adapter.MyTempleListWaitingAdapter
import com.panjikrisnayasa.caripura.util.SharedPrefManager
import com.panjikrisnayasa.caripura.viewmodel.MyTempleListViewModel
import kotlinx.android.synthetic.main.fragment_my_temple_list_waiting.*

/**
 * A simple [Fragment] subclass.
 */
class MyTempleListWaitingFragment : Fragment() {

    private lateinit var mViewModel: MyTempleListViewModel
    private lateinit var mAdapter: MyTempleListWaitingAdapter
    private lateinit var mSharedPref: SharedPrefManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(
            R.layout.fragment_my_temple_list_waiting,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mSharedPref = SharedPrefManager.getInstance(context)

        showRecyclerView()

        mViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(MyTempleListViewModel::class.java)

        mViewModel.getTempleListWaiting(mSharedPref.getId()).observe(this, Observer { templeList ->
            progress_my_temple_list_waiting.visibility = View.GONE
            if (templeList != null) {
                recycler_my_temple_list_waiting.visibility = View.VISIBLE
                text_my_temple_list_waiting_no_data.visibility = View.GONE
                mAdapter.setData(templeList)
            } else {
                text_my_temple_list_waiting_no_data.visibility = View.VISIBLE
                recycler_my_temple_list_waiting.visibility = View.GONE
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null)
            if (requestCode == MyTempleDetailWaitingActivity.REQUEST_CANCEL)
                if (resultCode == MyTempleDetailWaitingActivity.RESULT_CANCEL) {
                    val position = data.getIntExtra(MyTempleDetailWaitingActivity.EXTRA_POSITION, 0)
                    mAdapter.removeItem(position)
                }
    }

    private fun showRecyclerView() {
        mAdapter = MyTempleListWaitingAdapter(this)
        recycler_my_temple_list_waiting?.layoutManager = LinearLayoutManager(context)
        recycler_my_temple_list_waiting?.adapter = mAdapter
    }
}
