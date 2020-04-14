package com.panjikrisnayasa.caripura.view.guest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.panjikrisnayasa.caripura.R
import com.panjikrisnayasa.caripura.view.admin.AdminLoggedInFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

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
                    replaceFragment(AdminLoggedInFragment())
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

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout_main, fragment, fragment::class.java.simpleName)
            .commit()
    }
}
