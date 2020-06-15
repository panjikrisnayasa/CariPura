package com.panjikrisnayasa.caripura.view


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class TempleListFragment : Fragment(), TextWatcher {

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

        mViewModel.getTempleList()
            .observe(this, Observer { templeList ->
                progress_temple_list.visibility = View.GONE
                if (templeList != null) {
                    text_temple_list_no_data.visibility = View.GONE
                    mAdapter.setData(templeList)
                    edit_temple_list_find_temple.isEnabled = true
                    edit_temple_list_find_temple.addTextChangedListener(this)
                } else {
                    text_temple_list_no_data.visibility = View.VISIBLE
                }
            })
    }

    private fun showRecyclerView() {
        val tContext = context
        if (tContext != null) {
            mAdapter = TempleListAdapter(tContext)
            recycler_temple_list?.layoutManager = LinearLayoutManager(context)
            recycler_temple_list?.adapter = mAdapter
        }
    }

    override fun afterTextChanged(s: Editable?) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        val query = edit_temple_list_find_temple.text.trim().toString()
            .toLowerCase(Locale.getDefault())
        mAdapter.filter(query)
        if (mAdapter.itemCount == 0)
            text_temple_list_no_data.visibility = View.VISIBLE
        else
            text_temple_list_no_data.visibility = View.GONE
    }
}
