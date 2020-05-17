package com.panjikrisnayasa.caripura.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.panjikrisnayasa.caripura.R
import com.panjikrisnayasa.caripura.view.MyTempleListApprovedFragment
import com.panjikrisnayasa.caripura.view.MyTempleListWaitingFragment

class MyTempleListViewPagerAdapter(fm: FragmentManager, context: Context?) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private var mContext = context

    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> return MyTempleListApprovedFragment()
            1 -> return MyTempleListWaitingFragment()
        }
        return MyTempleListApprovedFragment()
    }

    override fun getCount(): Int {
        return 2

    }

    override fun getPageTitle(position: Int): CharSequence? {
        when (position) {
            0 -> return mContext?.getString(R.string.view_pager_approved)
            1 -> return mContext?.getString(R.string.view_pager_waiting)
        }
        return super.getPageTitle(position)
    }
}