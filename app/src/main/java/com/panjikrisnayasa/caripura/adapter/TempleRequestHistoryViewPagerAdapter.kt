package com.panjikrisnayasa.caripura.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.panjikrisnayasa.caripura.R
import com.panjikrisnayasa.caripura.view.TempleRequestHistoryAddFragment
import com.panjikrisnayasa.caripura.view.TempleRequestHistoryDeleteFragment
import com.panjikrisnayasa.caripura.view.TempleRequestHistoryEditFragment

class TempleRequestHistoryViewPagerAdapter(fm: FragmentManager, context: Context?) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private var mContext = context

    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> return TempleRequestHistoryAddFragment()
            1 -> return TempleRequestHistoryEditFragment()
            2 -> return TempleRequestHistoryDeleteFragment()
        }
        return TempleRequestHistoryAddFragment()
    }

    override fun getCount(): Int {
        return 3
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when (position) {
            0 -> return mContext?.getString(R.string.view_pager_add_temple)
            1 -> return mContext?.getString(R.string.view_pager_edit_temple)
            2 -> return mContext?.getString(R.string.view_pager_delete_temple)
        }
        return super.getPageTitle(position)
    }
}