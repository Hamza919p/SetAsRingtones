package com.wmt.setasringtones.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.wmt.setasringtones.R
import com.wmt.setasringtones.adapter.LatestAndTopViewPagerAdapter
import com.wmt.setasringtones.databinding.ActivityCategoryDetailBinding
import com.wmt.setasringtones.custom.CustomTabLayout
import com.wmt.setasringtones.utils.Utils

class CategoryDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCategoryDetailBinding
    var ringtoneId: String ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_category_detail)
        ringtoneId = intent?.getStringExtra("ringtoneId")
        binding.pager.adapter = LatestAndTopViewPagerAdapter(this, true)
        binding.pager.currentItem = 0
        CustomTabLayout.setLatestAndBestTabLayout(binding.tabLayout, binding.pager)
        setSupportActionBar(binding.toolbar)
        Utils.initializeBannerAds(this, binding.bannersAdd)
        Utils.setActionBar(supportActionBar, intent?.getStringExtra("ringtoneCategory")!!)
    }
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}