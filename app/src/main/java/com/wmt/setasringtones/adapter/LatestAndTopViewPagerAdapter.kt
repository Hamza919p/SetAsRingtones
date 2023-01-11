package com.wmt.setasringtones.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.wmt.setasringtones.fragments.CategoryBestTonesFragment
import com.wmt.setasringtones.fragments.CategoryLatestTonesFragment
import com.wmt.setasringtones.fragments.channel.ChannelBestTonesFragment
import com.wmt.setasringtones.fragments.channel.ChannelLatestTonesFragment
import com.wmt.setasringtones.fragments.home.BestTonesFragment
import com.wmt.setasringtones.fragments.home.LatestTonesFragment

class LatestAndTopViewPagerAdapter(fragmentActivity: FragmentActivity, var isCategoryActivity: Boolean) :
    FragmentStateAdapter(fragmentActivity)  {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        if(isCategoryActivity) {
            return when (position) {
                0 -> {
                    CategoryLatestTonesFragment()
                }
                else -> {
                    CategoryBestTonesFragment()
                }
            }
        }
        return when (position) {
            0 -> {
                ChannelLatestTonesFragment()
            }
            else -> {
                ChannelBestTonesFragment()
            }
        }
    }
}