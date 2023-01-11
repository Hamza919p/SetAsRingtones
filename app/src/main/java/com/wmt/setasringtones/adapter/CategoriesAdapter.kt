package com.wmt.setasringtones.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdView
import com.wmt.setasringtones.R
import com.wmt.setasringtones.models.CategoriesItem
import com.wmt.setasringtones.utils.Constants
import com.wmt.setasringtones.utils.Launcher
import com.wmt.setasringtones.utils.Utils

class CategoriesAdapter(var activity: Activity, var categories: ArrayList<CategoriesItem>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == Constants.ITEMS) {
            val v: View = inflater.inflate(R.layout.categories_list, parent, false)
            CategoriesViewHolder(v)
        } else {
            val v: View = inflater.inflate(R.layout.banner_ad, parent, false)
            return BannerAdsViewHolder(v)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is CategoriesViewHolder) {
            holder.categoryTitle.text = categories[position].title
            holder.categoryTitle.setOnClickListener {
                Launcher.startCategoryDetailActivity(activity, categories[position].id.toString(), categories[position].title)
            }
        }else if(holder is BannerAdsViewHolder) {
            Utils.initializeBannerAds(activity, holder.bannerAd)
        }
    }

    fun isBannersAdView(position: Int): Boolean {
        return position != 1 && position % 9 == 1
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    override fun getItemViewType(position: Int): Int {
        return if(position != 1 && position%9 == 1) {
            Constants.ADS
        } else {
            Constants.ITEMS
        }
    }

    private inner class BannerAdsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bannerAd: AdView = itemView.findViewById(R.id.banners_add)
    }

    private inner class CategoriesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var categoryTitle: TextView = itemView.findViewById(R.id.tv_title)
    }
}