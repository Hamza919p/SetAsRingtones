package com.wmt.setasringtones.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdView
import com.wmt.setasringtones.R
import com.wmt.setasringtones.`interface`.ItemClickListener
import com.wmt.setasringtones.models.ChannelsItem
import com.wmt.setasringtones.utils.Constants
import com.wmt.setasringtones.utils.Launcher
import com.wmt.setasringtones.utils.Utils

class ChannelsAdapter(var activity: Activity) : PagingDataAdapter<ChannelsItem, RecyclerView.ViewHolder>(DiffUtilCallback()) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is ChannelsViewHolder) {
            holder.channelName.text = getItem(position)?.name
            holder.channelName.setOnClickListener {
                Launcher.startChannelDetailActivity(activity, getItem(position)?.id!!, getItem(position)?.name!!, getItem(position)?.ringtones_count!!)
            }
        }else if(holder is BannerAdsViewHolder){
            Utils.initializeBannerAds(activity, holder.bannerAd)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == Constants.ITEMS) {
            val v: View = inflater.inflate(R.layout.categories_list, parent, false)
            ChannelsViewHolder(v)
        } else {
            val v: View = inflater.inflate(R.layout.banner_ad, parent, false)
            return BannerAdsViewHolder(v)
        }
    }

    private inner class ChannelsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val channelName: TextView = itemView.findViewById(R.id.tv_title)
    }

    private inner class BannerAdsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bannerAd: AdView = itemView.findViewById(R.id.banners_add)
    }

    fun isBannersAdView(position: Int): Boolean {
        return position != 1 && position % 9 == 1
    }

    override fun getItemViewType(position: Int): Int {
        return if(position != 1 && position%9 == 1) {
            Constants.ADS
        } else {
            Constants.ITEMS
        }
    }

    private class DiffUtilCallback : DiffUtil.ItemCallback<ChannelsItem>() {
        override fun areItemsTheSame(oldItem: ChannelsItem, newItem: ChannelsItem): Boolean{
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ChannelsItem, newItem: ChannelsItem): Boolean {
            return oldItem == newItem
        }
    }
}