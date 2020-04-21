package com.panjikrisnayasa.caripura.adapter.contributor

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.panjikrisnayasa.caripura.R
import com.panjikrisnayasa.caripura.view.contributor.MyTempleRequestHistoryAddFragment
import com.panjikrisnayasa.caripura.view.contributor.MyTempleRequestHistoryDeleteFragment
import com.panjikrisnayasa.caripura.view.contributor.MyTempleRequestHistoryEditFragment

class MyTempleRequestHistoryViewPagerAdapter(fm: FragmentManager, context: Context?) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private var mContext = context

    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> return MyTempleRequestHistoryAddFragment()
            1 -> return MyTempleRequestHistoryEditFragment()
            2 -> return MyTempleRequestHistoryDeleteFragment()
        }
        return MyTempleRequestHistoryAddFragment()
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