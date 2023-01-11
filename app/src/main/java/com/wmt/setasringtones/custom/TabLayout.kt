package com.wmt.setasringtones.custom

import android.annotation.SuppressLint
import android.app.Activity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.wmt.setasringtones.R

class CustomTabLayout {

    companion object {
        @SuppressLint("UseCompatLoadingForDrawables")
        fun setHomeTabLayout(activity: Activity, tabLayout: TabLayout, pager: ViewPager2) {
            TabLayoutMediator(tabLayout, pager) { tab, position ->

                when (position) {
                    0 -> {
                        tab.text = "Latest"
                        tab.icon = activity.getDrawable(R.drawable.latest_tab_selector)
                    }
                    1 -> {
                        tab.text = "Best"
                        tab.icon = activity.getDrawable(R.drawable.best_tab_selector)
                    }
                    2 -> {
                        tab.text = "Featured"
                        tab.icon = activity.getDrawable(R.drawable.featured_tab_selector)
                    }
                }
            }.attach()
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        fun setLatestAndBestTabLayout(tabLayout: TabLayout, pager: ViewPager2) {
            TabLayoutMediator(tabLayout, pager) { tab, position ->
                when (position) {
                    0 -> {
                        tab.text = "Latest"
                    }
                    1 -> {
                        tab.text = "Top"
                    }
                }
            }.attach()
        }


    }

}