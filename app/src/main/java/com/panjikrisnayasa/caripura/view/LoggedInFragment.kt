package com.panjikrisnayasa.caripura.view


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
import com.panjikrisnayasa.caripura.R
import kotlinx.android.synthetic.main.fragment_logged_in.*

/**
 * A simple [Fragment] subclass.
 */
class LoggedInFragment : Fragment() {

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
        button_logged_in_my_temple_list.setOnClickListener {
            val intent = Intent(view.context, MyTempleListActivity::class.java)
            startActivity(intent)
        }
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

                    }
                    val alertDialog = alertBuilder.create()
                    alertDialog.show()
                    alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                        .setTextColor(ContextCompat.getColor(tContext,
                            R.color.colorNegativeButton
                        ))
                    val message: TextView = alertDialog.findViewById(android.R.id.message)
                    message.typeface = Typeface.createFromAsset(activity?.assets, "gotham_book.ttf")
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
