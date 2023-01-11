package com.wmt.setasringtones.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.chibde.visualizer.CircleBarVisualizer
import com.cunoraz.tagview.TagView
import com.google.android.gms.ads.AdView
import com.google.gson.Gson
import com.wmt.setasringtones.R
import com.wmt.setasringtones.`interface`.PlayerItemsClickListener
import com.wmt.setasringtones.custom.CustomProgressDialog
import com.wmt.setasringtones.custom.CustomVisualizerManager
import com.wmt.setasringtones.custom.SetAudioAsPopUp
import com.wmt.setasringtones.models.TonesItem
import com.wmt.setasringtones.utils.Constants
import com.wmt.setasringtones.utils.Launcher
import com.wmt.setasringtones.utils.Utils
import com.wmt.setasringtones.viewModels.PlayerActivityViewModel

class PlayerAdapter(
    private var activity: Activity,
    private var relatedRingtonesList: List<TonesItem>,
    private var selectedRingtoneDetail: TonesItem,
    private var playerViewModel: PlayerActivityViewModel,
    private var isAudioPermissionGranted: Boolean,
    private var customProgressDialog: CustomProgressDialog
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var textView: TextView ?= null
    var playPauseBtn: ImageView ?= null
    private var isHeaderInitialized = false
    private lateinit var playerItemsClickListener: PlayerItemsClickListener
    fun setOnClickListener(mCallback: PlayerItemsClickListener) {
        playerItemsClickListener = mCallback
    }

    private var context: Context? = null
    var type: String? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(context)
        return if (viewType == Constants.HEADER) {
            val v: View = inflater.inflate(R.layout.player, parent, false)
            HeaderViewHolder(v)
        } else if(viewType == Constants.ADS) {
            val v: View = inflater.inflate(R.layout.banner_ad, parent, false)
            BannerAdsViewHolder(v)
        }
        else {
            val v: View = inflater.inflate(R.layout.tones_list, parent, false)
            return RelatedTonesViewHolder(v)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HeaderViewHolder) {
            if (!isHeaderInitialized) {
                isHeaderInitialized = true
                this.textView = holder.ringtoneDuration
                this.playPauseBtn = holder.playPauseBtn
                holder.downloads.text = selectedRingtoneDetail.downloads.toString()
                holder.ringtoneUser.text = "By " + selectedRingtoneDetail.user
                holder.ringtoneTitle.text = selectedRingtoneDetail.title
                Utils.initializeTags(activity, holder.tagView, selectedRingtoneDetail.tags)
                playerViewModel.restartRingtone(
                    context!!,
                    selectedRingtoneDetail.ringtone,
                    holder.seekBar,
                    holder.ringtoneDuration
                )
                Utils.setPlayPauseDrawableState(context!!, holder.playPauseBtn)

                holder.playPauseBtn.setOnClickListener {
                    playerViewModel.playPauseRingtone(holder.playPauseBtn)
                    Utils.setPlayPauseDrawableState(context!!, holder.playPauseBtn)
                }
                holder.downloads.setOnClickListener {
                    playerItemsClickListener.onDownloadClick()
                }
                holder.share.setOnClickListener {
                    playerViewModel.shareRingtone(
                        activity,
                        selectedRingtoneDetail.category_name,
                        selectedRingtoneDetail.id.toString(),
                        selectedRingtoneDetail.title
                    )
                }
                holder.setAudioAs.setOnClickListener {
                    SetAudioAsPopUp.open(context!!, holder.setAudioAs, playerItemsClickListener)
                }

                if (isAudioPermissionGranted) {
                    Utils.setCardGradientBackground(context!!, holder.layout)
                    CustomVisualizerManager.initialize(context!!, holder.visualizer)
                }
                Utils.initializeBannerAds(activity, holder.bannerAd)
            }
        } else if (holder is RelatedTonesViewHolder) {
            holder.downloadsCount.text = relatedRingtonesList[position - 1].downloads.toString()
            holder.musicDescription.text = relatedRingtonesList[position - 1].title
            Utils.setCardGradientBackground(context!!, holder.constraintLayout)
            holder.relatedTonesPlayBtn.setOnClickListener {
                Launcher.startPlayerActivity(
                    activity,
                    Gson().toJson(relatedRingtonesList[position - 1])
                )
                activity.finish()
            }
        } else if(holder is BannerAdsViewHolder) {
            Utils.initializeBannerAds(activity, holder.bannerAd)
        }
        customProgressDialog.dismiss()
    }

    fun isHeaderOrAds(position: Int): Boolean {
        if(position == 0)
            return position == 0
        //for ads
        return position != 1 && position%8 == 1
    }

    override fun getItemCount(): Int {
        return relatedRingtonesList.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            Constants.HEADER
        } else if(position != 1 && position%8 == 1) {
            Constants.ADS
        }
        else
            Constants.ITEMS
    }

    inner class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val downloads: TextView = itemView.findViewById(R.id.tv_downloads)
        val ringtoneTitle: TextView = itemView.findViewById(R.id.tv_tone_title)
        val ringtoneUser: TextView = itemView.findViewById(R.id.tv_ringtone_user)
        val tagView: TagView = itemView.findViewById(R.id.tags)
        val playPauseBtn: ImageView = itemView.findViewById(R.id.iv_play_pause_btn)
        val seekBar: SeekBar = itemView.findViewById(R.id.seek_bar)
        val ringtoneDuration: TextView = itemView.findViewById(R.id.tv_ringtone_duration)
        val share: TextView = itemView.findViewById(R.id.tv_share)
        val setAudioAs: TextView = itemView.findViewById(R.id.tv_set_audio_as)
        val visualizer: CircleBarVisualizer = itemView.findViewById(R.id.visualizer)
        val layout: LinearLayout = itemView.findViewById(R.id.linealayout)
        val bannerAd: AdView  = itemView.findViewById(R.id.banners_add)
    }

    private inner class RelatedTonesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val downloadsCount: TextView = itemView.findViewById(R.id.tv_downloads_count)
        val relatedTonesPlayBtn: ImageView = itemView.findViewById(R.id.iv_play_btn)
        val musicDescription: TextView = itemView.findViewById(R.id.tv_music_description)
        val constraintLayout: ConstraintLayout = itemView.findViewById(R.id.constraint_layout)
    }

    private inner class BannerAdsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bannerAd: AdView = itemView.findViewById(R.id.banners_add)
    }

}