package com.panjikrisnayasa.caripura


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_temple_list.*

/**
 * A simple [Fragment] subclass.
 */
class TempleListFragment : Fragment() {

    private lateinit var mEditFindTemple: EditText
    private lateinit var mDatabaseReference: DatabaseReference

    private var mTempleList: ArrayList<Temple> = arrayListOf()
    private val mChildEventListener = object : ChildEventListener {
        override fun onCancelled(p0: DatabaseError) {}

        override fun onChildMoved(p0: DataSnapshot, p1: String?) {}

        override fun onChildChanged(p0: DataSnapshot, p1: String?) {}

        override fun onChildAdded(p0: DataSnapshot, p1: String?) {
            val temple = p0.getValue(Temple::class.java)
            if (temple != null) {
                mTempleList.add(temple)
                showRecyclerView()
            }
        }

        override fun onChildRemoved(p0: DataSnapshot) {}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_temple_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tActivity = this.activity

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("temple")
        mDatabaseReference.addChildEventListener(mChildEventListener)

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
    }

    private fun showRecyclerView() {
        recycler_temple_list.visibility = View.VISIBLE
        text_temple_list_no_data.visibility = View.GONE
        val templeAdapter = TempleAdapter(mTempleList)
        recycler_temple_list.layoutManager = LinearLayoutManager(context)
        recycler_temple_list.adapter = templeAdapter
    }
}
