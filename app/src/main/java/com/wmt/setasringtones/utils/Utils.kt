package com.wmt.setasringtones.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.media.Image
import android.media.RingtoneManager
import android.os.Environment
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.cunoraz.tagview.Tag
import com.cunoraz.tagview.TagView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.wmt.setasringtones.R
import com.wmt.setasringtones.adapter.PlayerAdapter
import java.io.File
import java.util.concurrent.TimeUnit


class Utils {
    companion object {
        fun setActionBar(actionBar: ActionBar?, toolbarTitle: String) {
            actionBar?.apply {
                setHomeAsUpIndicator(R.drawable.ic_back_white)
                setDisplayHomeAsUpEnabled(true)
                setDisplayShowHomeEnabled(true)
                title = toolbarTitle
            }
        }

        fun setCardGradientBackground(context: Context, layout: View) {
            val gradients = arrayListOf(
                R.drawable.linear_gradient_3,
                R.drawable.linear_gradient_1,
                R.drawable.linear_gradient_4,
                R.drawable.linear_gradient_5,
                R.drawable.linear_gradient_6,
                R.drawable.linear_gradient_7,
                R.drawable.linear_gradient_8
            )
            val rand = Math.random() * gradients.size
            layout.background = AppCompatResources.getDrawable(context, gradients[rand.toInt()])
        }

        fun initializeTags(activity: Activity, tagView: TagView, tags: String) {
            if (tags != "" && tags != "null") {
                val tagsArr = ArrayList<Tag>()
                var tag: Tag
                if (tags.contains(",")) {
                    val tagsString = tags.split(",")
                    for (i in 0 until tagsString.size) {
                        tag = Tag(tagsString[i].trim())
                        tag.background = AppCompatResources.getDrawable(
                            activity,
                            R.drawable.bg_round_corners_12
                        )
                        tag.tagTextColor = ContextCompat.getColor(activity, R.color.white)
                        tag.tagTextSize = 13f
                        tagsArr.add(tag)
                        tagView.addTags(tagsArr)
                    }
                } else {
                    tag = Tag(tags.trim())
                    tag.background = AppCompatResources.getDrawable(
                        activity,
                        R.drawable.bg_round_corners_12
                    )
                    tag.tagTextColor = ContextCompat.getColor(activity, R.color.white)
                    tag.tagTextSize = 13f
                    tagView.addTag(tag)
                }
//                binding.tagsTagGroup.setOnTagClickListener { _, position ->
//                    Launcher.startSearchActivity(this, tagsArr[position].text)
//                }
            } else {
                tagView.visibility = View.GONE
            }

            tagView.setOnTagClickListener { tag, _ ->
                Launcher.startSearchActivity(activity, tag.text)
            }
        }

        @SuppressLint("DefaultLocale")
        fun formatter(time: Int): String? {
            return java.lang.String.format(
                "%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(time.toLong()),
                TimeUnit.MILLISECONDS.toSeconds(time.toLong()) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time.toLong()))
            )
        }

        fun setPlayPauseDrawableState(context: Context, playPauseBtn: ImageView) {
            if (playPauseBtn.tag == "play") {
                playPauseBtn.tag = "pause"
                playPauseBtn.setImageDrawable(
                    AppCompatResources.getDrawable(
                        context,
                        R.drawable.ic_white_pause
                    )
                )
            } else {
                playPauseBtn.tag = "play"
                playPauseBtn.setImageDrawable(
                    AppCompatResources.getDrawable(
                        context,
                        R.drawable.ic_white_play_btn
                    )
                )
            }
        }

        fun generateRingtonesDownloadPath(toneTitle: String): File {
            val file =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_RINGTONES)
            if (!file.exists())
                file.mkdirs()
            return File(file, "$toneTitle.mp3")
        }

        fun getMIMEType(url: String?): String? {
            val updatedUrl: String = if (url?.contains(" ")!!) {
                url.replace(" ", "_")
            } else {
                url
            }
            var mType: String? = null
            val mExtension = MimeTypeMap.getFileExtensionFromUrl(updatedUrl)
            if (mExtension != null) {
                mType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(mExtension)
            }
            return mType
        }

        fun setRingtoneSuccessMessage(managerType: Int, context: Context) {
            when (managerType) {
                RingtoneManager.TYPE_RINGTONE -> Toast.makeText(
                    context,
                    "Audio set as Ringtone",
                    Toast.LENGTH_SHORT
                ).show()
                RingtoneManager.TYPE_NOTIFICATION -> Toast.makeText(
                    context,
                    "Audio set as Notification",
                    Toast.LENGTH_SHORT
                ).show()
                RingtoneManager.TYPE_ALARM -> Toast.makeText(
                    context,
                    "Audio set as Alarm",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        fun showBannerAd(activity: Activity, position: Int) {
            if(position % 6 == 1 && position != 1) {
                val linearLayout = LinearLayout(activity)
                val adView= AdView(activity)
                adView.setAdSize(AdSize.BANNER)
                adView.adUnitId = activity.getString(R.string.banner_ad_unit_id)
                val adRequest = AdRequest.Builder().build()
                adView.loadAd(adRequest)

                linearLayout.setBackgroundColor(activity.getColor(R.color.primary))
                val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                linearLayout.addView(adView, params)
            }
        }

        fun initializeBannerAds(activity: Activity, adView: AdView) {
            MobileAds.initialize(activity) {}
            val adRequest = AdRequest.Builder().build()
            adView.loadAd(adRequest)
        }
    }
}