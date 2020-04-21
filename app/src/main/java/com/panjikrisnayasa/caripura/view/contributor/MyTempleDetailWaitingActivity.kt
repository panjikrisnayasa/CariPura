package com.panjikrisnayasa.caripura.view.contributor

import android.app.AlertDialog
import android.graphics.Typeface
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.panjikrisnayasa.caripura.R

class MyTempleDetailWaitingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_temple_detail_waiting)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_cancel, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            R.id.menu_cancel -> {
                val alertBuilder = AlertDialog.Builder(this)
                alertBuilder.setTitle(getString(R.string.dialog_cancel_title_add))
                alertBuilder.setMessage(getString(R.string.dialog_cancel_message_add))
                alertBuilder.setPositiveButton(getString(R.string.dialog_cancel_positive_button)) { _, _ ->

                }
                alertBuilder.setNegativeButton(getString(R.string.dialog_cancel_negative_button)) { _, _ ->

                }
                val alertDialog = alertBuilder.create()
                alertDialog.show()
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                    .setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.colorNegativeButton
                        )
                    )
                val message: TextView = alertDialog.findViewById(android.R.id.message)
                message.typeface = Typeface.createFromAsset(this.assets, "gotham_book.ttf")
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
