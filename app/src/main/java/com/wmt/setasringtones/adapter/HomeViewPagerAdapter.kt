package com.wmt.setasringtones.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.wmt.setasringtones.fragments.home.BestTonesFragment
import com.wmt.setasringtones.fragments.home.FeaturedTonesFragment
import com.wmt.setasringtones.fragments.home.LatestTonesFragment

class HomeViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                LatestTonesFragment()
            }
            1 -> {
                BestTonesFragment()
            }
            else -> {
                FeaturedTonesFragment()
            }
        }
    }

    override fun getItemCount(): Int {
        return 3
    }
}