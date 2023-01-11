package com.wmt.setasringtones.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdView
import com.google.gson.Gson
import com.wmt.setasringtones.R
import com.wmt.setasringtones.models.TonesItem
import com.wmt.setasringtones.utils.Constants
import com.wmt.setasringtones.utils.Launcher
import com.wmt.setasringtones.utils.Utils

class TonesAdapter(var activity: Activity) :
    PagingDataAdapter<TonesItem, RecyclerView.ViewHolder>(DiffUtilCallback()) {

    private var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(context)
        return if (viewType == Constants.ITEMS) {
            val v: View = inflater.inflate(R.layout.tones_list, parent, false)
            TonesViewHolder(v)
        } else{
            val v: View = inflater.inflate(R.layout.banner_ad, parent, false)
            BannerAdsViewHolder(v)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is TonesViewHolder) {
            holder.downloadsCount.text = getItem(position)?.downloads.toString()
            holder.musicDescription.text = getItem(position)?.title
            Utils.setCardGradientBackground(context!!, holder.constraintLayout)
            Utils.showBannerAd(activity, position)
            holder.playBtn.setOnClickListener {
                Launcher.startPlayerActivity(
                    activity,
                    Gson().toJson(getItem(position)!!)
                )
            }
        }
        else if (holder is BannerAdsViewHolder) {
            Utils.initializeBannerAds(activity, holder.bannerAd)
        }
    }

    fun isBannersAdView(position: Int): Boolean {
        return position != 1 && position % 9 == 1
    }

    private inner class BannerAdsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bannerAd: AdView  = itemView.findViewById(R.id.banners_add)
    }

    private inner class TonesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val downloadsCount: TextView = itemView.findViewById(R.id.tv_downloads_count)
        val playBtn: ImageView = itemView.findViewById(R.id.iv_play_btn)
        val musicDescription: TextView = itemView.findViewById(R.id.tv_music_description)
        val constraintLayout: ConstraintLayout = itemView.findViewById(R.id.constraint_layout)
    }

    override fun getItemViewType(position: Int): Int {
        return if(position != 1 && position%9 == 1) {
             Constants.ADS
        } else {
            Constants.ITEMS
        }
    }

    private class DiffUtilCallback :
        DiffUtil.ItemCallback<TonesItem>() {
        override fun areItemsTheSame(oldItem: TonesItem, newItem: TonesItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TonesItem, newItem: TonesItem): Boolean {
            return oldItem == newItem
        }
    }

}