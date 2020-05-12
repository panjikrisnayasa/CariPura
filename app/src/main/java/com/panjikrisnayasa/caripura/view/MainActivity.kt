package com.panjikrisnayasa.caripura.view

import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.panjikrisnayasa.caripura.R
import com.panjikrisnayasa.caripura.view.guest.AccountLoginFragment
import com.panjikrisnayasa.caripura.view.guest.FindTempleFragment
import com.panjikrisnayasa.caripura.view.guest.TempleListFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var mBackPressedOnce = false

    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_main_bottom_navigation_find_temple -> {
                    replaceFragment(FindTempleFragment())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.menu_main_bottom_navigation_temple_list -> {
                    replaceFragment(TempleListFragment())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.menu_main_bottom_navigation_account -> {
                    replaceFragment(AccountLoginFragment())
                    return@OnNavigationItemSelectedListener true
                }
            }
            return@OnNavigationItemSelectedListener false
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        replaceFragment(FindTempleFragment())

        bottom_navigation_view_main.setOnNavigationItemSelectedListener(
            mOnNavigationItemSelectedListener
        )
    }

    override fun onBackPressed() {
        if (mBackPressedOnce) {
            super.onBackPressed()
            return
        }
        mBackPressedOnce = true
        Toast.makeText(this, getString(R.string.main_on_back_pressed), Toast.LENGTH_SHORT).show()
        Handler().postDelayed({
            mBackPressedOnce = false
        }, 2000)
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout_main, fragment, fragment::class.java.simpleName)
            .commit()
    }
}
