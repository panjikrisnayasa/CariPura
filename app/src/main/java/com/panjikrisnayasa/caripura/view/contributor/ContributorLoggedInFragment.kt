package com.panjikrisnayasa.caripura.view.contributor

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.panjikrisnayasa.caripura.R
import com.panjikrisnayasa.caripura.util.SharedPrefManager
import com.panjikrisnayasa.caripura.view.admin.AdminMyTempleListActivity
import com.panjikrisnayasa.caripura.view.guest.AccountLoginFragment
import kotlinx.android.synthetic.main.fragment_contributor_logged_in.*

/**
 * A simple [Fragment] subclass.
 */
class ContributorLoggedInFragment : Fragment() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mSharedPref: SharedPrefManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mAuth = FirebaseAuth.getInstance()
        val view = inflater.inflate(R.layout.fragment_contributor_logged_in, container, false)
        val toolbar =
            view.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_contributor_logged_in)
        toolbar.inflateMenu(R.menu.menu_logged_in_options)
        toolbar.setOnMenuItemClickListener {
            onOptionsItemSelected(it)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button_contributor_logged_in_my_temple_list.setOnClickListener {
            val intent = Intent(view.context, AdminMyTempleListActivity::class.java)
            startActivity(intent)
        }

        mSharedPref = SharedPrefManager.getInstance(context)
        text_input_edit_text_contributor_logged_in_full_name.setText(mSharedPref.getFullName())
        text_input_edit_text_contributor_logged_in_email.setText(mSharedPref.getEmail())
        text_input_edit_text_contributor_logged_in_phone_number.setText(mSharedPref.getPhoneNumber())
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
                        mAuth.signOut()
                        mSharedPref.logout()
                        fragmentManager?.beginTransaction()?.replace(
                            R.id.frame_layout_main,
                            AccountLoginFragment(),
                            AccountLoginFragment::class.java.simpleName
                        )?.commit()
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
}
