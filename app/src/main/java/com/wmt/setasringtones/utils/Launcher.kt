package com.wmt.setasringtones.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.speech.RecognizerIntent
import com.wmt.setasringtones.activities.*


class Launcher {
    companion object {
        fun startMainActivity(activity: Activity) {
            val starter = Intent(activity, MainActivity::class.java)
            starter.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            activity.startActivity(starter)
            activity.finish()
            activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        fun startSearchActivity(activity: Activity, searchQuery: String) {
            activity.startActivity(
                Intent(
                    activity,
                    SearchActivity::class.java
                ).putExtra("searchQuery", searchQuery)
            )
        }

        fun startChannelDetailActivity(
            activity: Activity,
            userId: Int,
            ownerName: String,
            count: Int
        ) {
            activity.startActivity(
                Intent(activity, ChannelDetailActivity::class.java).putExtra(
                    "channelUserId",
                    userId
                ).putExtra("ownerName", ownerName).putExtra("ringtonesCount", count)
            )
        }

        fun startCategoryDetailActivity(activity: Activity, userId: String, ringtoneCategory: String) {
            activity.startActivity(
                Intent(activity, CategoryDetailActivity::class.java).putExtra(
                    "ringtoneId",
                    userId
                ).putExtra("ringtoneCategory", ringtoneCategory)
            )
        }

        fun startPlayerActivity(activity: Activity, toneDetail: String) {
            activity.startActivity(
                Intent(
                    activity,
                    PlayerActivity::class.java
                ).putExtra("toneDetail", toneDetail)
            )
        }

        fun startActionManageWriteSettings(activity: Activity){
            val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
            intent.data = Uri.parse("package:" + activity.packageName)
            activity.startActivity(intent)
        }

        fun shareRingtoneIntent(activity: Activity, categoryName: String, toneId: String, toneTitle: String) {
            val share = Intent(Intent.ACTION_SEND)
            share.type = "text/plain"

            val updatedCategoryName = if (categoryName.contains(" "))
                categoryName.replace(" ", "-")
            else
                categoryName

            val updatedToneTitle = if (toneTitle.contains(" "))
                toneTitle.replace(" ", "-")
            else
                toneTitle

            share.putExtra(Intent.EXTRA_SUBJECT, "SetAsRingtone")
            share.putExtra(
                Intent.EXTRA_TEXT,
                "https://www.setasringtones.com/category/$updatedCategoryName/$toneId/$updatedToneTitle"
            )

            activity.startActivity(Intent.createChooser(share, "Share link via!"))
        }

        fun startWebBrowserActivity(activity: Activity, url: String) {
            activity.startActivity(Intent(activity, WebBrowserActivity::class.java).putExtra("url", url))
        }

        fun startSplashActivity(activity: Activity) {
            activity.startActivity(Intent(activity, SplashActivity::class.java))
            activity.finishAffinity()
        }
    }
}