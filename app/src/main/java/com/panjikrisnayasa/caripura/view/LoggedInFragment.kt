package com.panjikrisnayasa.caripura.view

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.messaging.FirebaseMessaging
import com.panjikrisnayasa.caripura.R
import com.panjikrisnayasa.caripura.util.SharedPrefManager
import com.panjikrisnayasa.caripura.viewmodel.LoggedInViewModel
import kotlinx.android.synthetic.main.fragment_logged_in.*

/**
 * A simple [Fragment] subclass.
 */
class LoggedInFragment : Fragment() {

    private lateinit var mViewModel: LoggedInViewModel
    private lateinit var mSharedPref: SharedPrefManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_logged_in, container, false)

        val toolbar =
            view.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_logged_in)
        toolbar.inflateMenu(R.menu.menu_logged_in_options)
        toolbar.setOnMenuItemClickListener {
            onOptionsItemSelected(it)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mSharedPref = SharedPrefManager.getInstance(context)
        if (mSharedPref.getRole() == "contributor") {
            text_logged_in_toolbar_title.text = getString(R.string.logged_in_text_contributor)
            button_logged_in_temple_request_list.visibility = View.GONE

            val topic = "/topics/approval_" + mSharedPref.getId()
            FirebaseMessaging.getInstance().subscribeToTopic(topic)
                .addOnSuccessListener {
                    Log.d("hyperLoop", "success subscribe to $topic")
                }.addOnFailureListener {
                    Log.d("hyperLoop", "failed subscribe to $topic")
                }
        } else if (mSharedPref.getRole() == "admin") {
            FirebaseMessaging.getInstance().subscribeToTopic("/topics/request")
                .addOnSuccessListener {
                    Log.d("hyperLoop", "success subscribe to /topics/request")
                }.addOnFailureListener {
                    Log.d("hyperLoop", "failed subscribe to /topics/request")
                }
        }

        mViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            LoggedInViewModel::class.java
        )

        button_logged_in_my_temple_list.setOnClickListener {
            val myTempleListIntent = Intent(context, MyTempleListActivity::class.java)
            startActivity(myTempleListIntent)
        }

        button_logged_in_temple_request_list.setOnClickListener {
            val templeRequestListIntent = Intent(context, TempleRequestListActivity::class.java)
            startActivity(templeRequestListIntent)
        }

        text_input_edit_text_logged_in_full_name.setText(mSharedPref.getFullName())
        text_input_edit_text_logged_in_email.setText(mSharedPref.getEmail())
        text_input_edit_text_logged_in_phone_number.setText(mSharedPref.getPhoneNumber())
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_logged_in_options_logout -> {
                val tContext = context
                if (tContext != null) {
                    val alertBuilder = AlertDialog.Builder(activity)
                    alertBuilder.setTitle(getString(R.string.dialog_logout_title))
                    alertBuilder.setMessage(getString(R.string.dialog_logout_message))
                    alertBuilder.setPositiveButton(getString(R.string.dialog_logout_positive_button)) { _, _ ->
                    }
                    alertBuilder.setNegativeButton(getString(R.string.dialog_logout_negative_button)) { _, _ ->
                        signOut()
                    }
                    val alertDialog = alertBuilder.create()
                    alertDialog.show()
                    alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                        .setTextColor(
                            ContextCompat.getColor(
                                tContext,
                                R.color.colorNegativeButton
                            )
                        )
                    val message: TextView = alertDialog.findViewById(android.R.id.message)
                    message.typeface = Typeface.createFromAsset(activity?.assets, "gotham_book.ttf")
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun signOut() {
        if (mSharedPref.getRole() == "contributor") {
            val topic = "/topics/approval_" + mSharedPref.getId()
            FirebaseMessaging.getInstance().unsubscribeFromTopic(topic)
                .addOnSuccessListener {
                    Log.d("hyperLoop", "success unsubscribe to $topic")
                }.addOnFailureListener {
                    Log.d("hyperLoop", "failed unsubscribe to $topic")
                }
        } else if (mSharedPref.getRole() == "admin") {
            FirebaseMessaging.getInstance().unsubscribeFromTopic("/topics/request")
                .addOnSuccessListener {
                    Log.d("hyperLoop", "success unsubscribe to /topics/request")
                }.addOnFailureListener {
                    Log.d("hyperLoop", "failed unsubscribe to /topics/request")
                }
        }
        mViewModel.signOut()
        mSharedPref.logout()
        fragmentManager?.beginTransaction()?.replace(
            R.id.frame_layout_main,
            AccountLoginFragment(),
            AccountLoginFragment::class.java.simpleName
        )?.commit()
    }
}
