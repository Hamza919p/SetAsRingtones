package com.wmt.setasringtones.viewModels

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.wmt.setasringtones.`interface`.InterstitialAdCallback
import com.wmt.setasringtones.`interface`.MediaPlayerCallback
import com.wmt.setasringtones.custom.CustomInterstitialAd
import com.wmt.setasringtones.custom.CustomMediaPlayer
import com.wmt.setasringtones.custom.DownloadFileFromUrl
import com.wmt.setasringtones.models.TonesItem
import com.wmt.setasringtones.repository.RelatedTonesRepository
import com.wmt.setasringtones.utils.Launcher
import com.wmt.setasringtones.utils.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.File

class PlayerActivityViewModel : ViewModel() {
    private lateinit var customInterstitialAdObj: CustomInterstitialAd
    private var mInterstitialAd: InterstitialAd ?= null
    private var customMediaPlayer = CustomMediaPlayer.newInstance()

    private var mutableLiveData = MutableLiveData<Response<ArrayList<TonesItem>>>()
    val categoriesData: LiveData<Response<ArrayList<TonesItem>>>
        get() = mutableLiveData

    private var updatedTimeLiveData = MutableLiveData<String>()
    val updatedTime: LiveData<String>
        get() = updatedTimeLiveData

    fun restartRingtone(
        context: Context,
        ringtone: String,
        seekBar: SeekBar,
        songDuration: TextView
    ) {
        customMediaPlayer.listener(object : MediaPlayerCallback {
            override fun onUpdateTime(updatedTime: String) {
                updatedTimeLiveData.value = updatedTime
            }
        })
        customMediaPlayer.restartSong(context, ringtone, seekBar, songDuration)
    }

    fun playPauseRingtone(playPauseBtn: ImageView) {
        customMediaPlayer.playPauseBtn(playPauseBtn)
    }

    fun downloadAndSetAsRingtone(activity: Activity, uri: String, toneTitle: String, setAudioAs: String?) {
        customInterstitialAdObj = CustomInterstitialAd(activity)
        customInterstitialAdObj.initialize()
        customInterstitialAdObj.listener(object : InterstitialAdCallback{
            override fun onSuccess(interstitialAd: InterstitialAd) {
                mInterstitialAd = interstitialAd
                if (mInterstitialAd != null) {
                    mInterstitialAd?.show(activity)
                    customInterstitialAdObj.mInterstitialAddCallbacks(mInterstitialAd!!)
                } else {
                    Log.d("InterstitialAdListener", "The interstitial ad wasn't ready yet.")
                }
            }

            override fun onDismissFullScreen() {
                val file: File = Utils.generateRingtonesDownloadPath(toneTitle)
                DownloadFileFromUrl.download(activity, uri, setAudioAs, "$file")
            }

            override fun onFailure(interstitialAd: InterstitialAd?) {
                val file: File = Utils.generateRingtonesDownloadPath(toneTitle)
                DownloadFileFromUrl.download(activity, uri, setAudioAs, "$file")
            }
        })
    }

    fun shareRingtone(activity: Activity, categoryName: String, toneId: String, toneTitle: String) {
        Launcher.shareRingtoneIntent(activity, categoryName, toneId, toneTitle)
    }

    suspend fun observeRelatedTonesResponse(id: String) {
        viewModelScope.launch(Dispatchers.Main) {
            val repo = RelatedTonesRepository()
            mutableLiveData.value = repo.getRelatedRingtones(id)
        }
    }

}