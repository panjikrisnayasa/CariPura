package com.panjikrisnayasa.caripura.view.guest


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.panjikrisnayasa.caripura.R
import com.panjikrisnayasa.caripura.adapter.guest.TempleListAdapter
import com.panjikrisnayasa.caripura.util.SharedPrefManager
import com.panjikrisnayasa.caripura.viewmodel.guest.TempleListViewModel
import kotlinx.android.synthetic.main.fragment_temple_list.*

/**
 * A simple [Fragment] subclass.
 */
class TempleListFragment : Fragment() {

    private lateinit var mEditFindTemple: EditText
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

        val tActivity = this.activity
        if (tActivity != null)
            mEditFindTemple = tActivity.findViewById(R.id.edit_temple_list_find_temple)
        mEditFindTemple.requestFocus()
        val bundle = arguments
        if (bundle != null) {
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(
                InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY
            )
        }

        showRecyclerView()

        mViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(TempleListViewModel::class.java)

        mViewModel.getTemple()
            .observe(this.viewLifecycleOwner, Observer { templeList ->
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
