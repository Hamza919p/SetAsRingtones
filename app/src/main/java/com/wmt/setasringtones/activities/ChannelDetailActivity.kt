package com.wmt.setasringtones.activities

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.wmt.setasringtones.R
import com.wmt.setasringtones.adapter.LatestAndTopViewPagerAdapter
import com.wmt.setasringtones.databinding.ActivityChannelDetailBinding
import com.wmt.setasringtones.custom.CustomTabLayout
import com.wmt.setasringtones.utils.Utils

class ChannelDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChannelDetailBinding
    var userId: Int ?= null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_channel_detail)
        userId = intent?.getIntExtra("channelUserId", 0)
        binding.userChannelCustomCard.tvUser.text = intent?.getStringExtra("ownerName")
        binding.userChannelCustomCard.tvTotalRingtones.text = "Total Ringtones: " + intent?.getIntExtra("ringtonesCount",0).toString()
        binding.pager.adapter = LatestAndTopViewPagerAdapter(this, false)
        binding.pager.currentItem = 0
        Utils.setCardGradientBackground(this,  binding.userChannelCustomCard.view)
        setSupportActionBar(binding.toolbar)
        Utils.setActionBar(supportActionBar, intent?.getStringExtra("ownerName")!!)
        Utils.initializeBannerAds(this, binding.bannersAdd)
        CustomTabLayout.setLatestAndBestTabLayout(binding.tabLayout, binding.pager)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

}