package com.resieasy.rezirent.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.resieasy.rezirent.Fragment.ShowHostelFragment
import com.resieasy.rezirent.Fragment.ShowRentFragment
import com.resieasy.rezirent.Fragment.ShowSellFragment
import com.resieasy.rezirent.Fragment.ShowlikedFragment

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
