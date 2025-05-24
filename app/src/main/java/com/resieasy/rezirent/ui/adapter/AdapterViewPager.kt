package com.resieasy.rezirent.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.resieasy.rezirent.ui.fragment.ShowHostelFragment
import com.resieasy.rezirent.ui.fragment.ShowRentFragment
import com.resieasy.rezirent.ui.fragment.ShowSellFragment
import com.resieasy.rezirent.ui.fragment.ShowlikedFragment


class AdapterViewPager(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    //ArrayList<Fragment> arr;
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ShowRentFragment()
            1 -> ShowSellFragment()
            2 -> ShowHostelFragment()
            3 -> ShowlikedFragment()
            else -> ShowRentFragment()

        }
    }

    override fun getItemCount(): Int {
        return 4
    }
}
