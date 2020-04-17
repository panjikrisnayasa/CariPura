package com.panjikrisnayasa.caripura.view.admin

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.panjikrisnayasa.caripura.R

class AdminMyTempleDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_temple_detail)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_my_temple_detail_options, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            R.id.menu_my_temple_detail_options_edit -> {
                val intent = Intent(applicationContext, EditTempleFirstActivity::class.java)
                startActivity(intent)
            }
            R.id.menu_my_temple_detail_options_delete -> {
                val alertBuilder = AlertDialog.Builder(this@AdminMyTempleDetailActivity)
                alertBuilder.setTitle(getString(R.string.dialog_delete_title))
                alertBuilder.setMessage(getString(R.string.dialog_delete_message))
                alertBuilder.setPositiveButton(getString(R.string.dialog_delete_positive_button)) { _, _ ->

                }
                alertBuilder.setNegativeButton(getString(R.string.dialog_delete_negative_button)) { _, _ ->

                }
                val alertDialog = alertBuilder.create()
                alertDialog.show()
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                    .setTextColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.colorNegativeButton
                        )
                    )
                val message: TextView = alertDialog.findViewById(android.R.id.message)
                message.typeface =
                    Typeface.createFromAsset(applicationContext.assets, "gotham_book.ttf")
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
