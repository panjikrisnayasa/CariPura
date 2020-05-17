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
import com.panjikrisnayasa.caripura.adapter.TempleListAdapter
import com.panjikrisnayasa.caripura.util.SharedPrefManager
import com.panjikrisnayasa.caripura.viewmodel.TempleListViewModel
import kotlinx.android.synthetic.main.fragment_temple_list.*

/**
 * A simple [Fragment] subclass.
 */
class TempleListFragment : Fragment() {

    private lateinit var mViewModel: TempleListViewModel
    private lateinit var mAdapter: TempleListAdapter
    private lateinit var mSharedPref: SharedPrefManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_temple_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mSharedPref = SharedPrefManager.getInstance(context)

        showRecyclerView()

        mViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(TempleListViewModel::class.java)

        mViewModel.getTemple()
            .observe(this, Observer { templeList ->
                if (templeList != null) {
                    progress_temple_list.visibility = View.GONE
                    mAdapter.setData(templeList)
                } else {
                    text_temple_list_no_data.visibility = View.VISIBLE
                }
            })
    }

    private fun showRecyclerView() {
        mAdapter = TempleListAdapter()
        recycler_temple_list?.layoutManager = LinearLayoutManager(context)
        recycler_temple_list?.adapter = mAdapter
    }
}
