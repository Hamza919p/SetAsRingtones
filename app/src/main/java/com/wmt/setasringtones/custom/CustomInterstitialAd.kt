package com.wmt.setasringtones.custom

import android.app.Activity
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.wmt.setasringtones.R
import com.wmt.setasringtones.`interface`.InterstitialAdCallback

class CustomInterstitialAd(var activity: Activity) {
    private lateinit var interstitialAdCallback: InterstitialAdCallback
    fun listener(mCallback: InterstitialAdCallback) {
        interstitialAdCallback = mCallback
    }

    fun initialize() {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(activity, activity.getString(R.string.interstitial_ad_unit_id), adRequest, object: InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(p0: LoadAdError) {
                Log.d("InterstitialAdListener", "Failed")
                interstitialAdCallback.onFailure(null)
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                Log.d("InterstitialAdListener", "Success")
                interstitialAdCallback.onSuccess(interstitialAd)

            }
        })
    }

    fun mInterstitialAddCallbacks(mInterstitialAd: InterstitialAd) {
        mInterstitialAd.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdClicked() {
                Log.d("InterstitialAdListener", "Add Clicked")
            }

            override fun onAdDismissedFullScreenContent() {
                Log.d("InterstitialAdListener", "Ad dismissed fullscreen content.")
                interstitialAdCallback.onDismissFullScreen()
                // Called when ad is dismissed.
            }

            override fun onAdFailedToShowFullScreenContent(addError: AdError) {
                Log.e("InterstitialAdListener", "Ad failed to show fullscreen content.")
            }

            override fun onAdImpression() {
                // Called when an impression is recorded for an ad.
                Log.d("InterstitialAdListener", "Ad recorded an impression.")
            }

            override fun onAdShowedFullScreenContent() {
                // Called when ad is shown.
                Log.d("InterstitialAdListener", "Ad showed fullscreen content.")
            }
        }
    }
}