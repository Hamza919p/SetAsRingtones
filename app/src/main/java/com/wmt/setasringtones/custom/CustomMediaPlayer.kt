package com.wmt.setasringtones.custom

import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import androidx.lifecycle.*
import com.wmt.setasringtones.`interface`.MediaPlayerCallback
import com.wmt.setasringtones.utils.DataPreference
import com.wmt.setasringtones.utils.Utils


class CustomMediaPlayer : MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener,
    MediaPlayer.OnBufferingUpdateListener, DefaultLifecycleObserver {

    private var mMediaPlayer: MediaPlayer? = null
    private lateinit var mediaPlayerCallback: MediaPlayerCallback
    private lateinit var seekBar: SeekBar
    private lateinit var tvRingtoneDuration: TextView
    var handler: Handler = Handler(Looper.getMainLooper())
    private var mContext: Context? = null
    var userSelectedPosition = 0

    fun listener(mCallback: MediaPlayerCallback) {
        mediaPlayerCallback = mCallback
    }

    companion object {
        private var instance: CustomMediaPlayer?= null
        @Synchronized
        fun newInstance() : CustomMediaPlayer {
            if(instance == null)
                instance = CustomMediaPlayer()
            return instance!!
        }
    }

    private fun initializeMediaPlayer(ringtone: String) {
        if (mMediaPlayer != null) {
            mMediaPlayer?.reset()
        } else {
            mMediaPlayer = MediaPlayer()
            mMediaPlayer?.setOnPreparedListener(this)
            val sessionId = mMediaPlayer?.audioSessionId as Int
            DataPreference.getInstance(mContext!!).setMediaPlayerSessionId(sessionId)
            mMediaPlayer?.setOnCompletionListener(this)
            mMediaPlayer?.setOnBufferingUpdateListener(this)
        }

        try {
            mMediaPlayer?.setDataSource(ringtone)
            mMediaPlayer?.prepare()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    private val updateSongTime: Runnable = object : Runnable {
        override fun run() {
            if (mMediaPlayer != null) {
                seekBar.progress = mMediaPlayer!!.currentPosition
            }
            handler.postDelayed(this, 100)
        }
    }

    fun restartSong(context: Context, ringtone: String, seekBar: SeekBar, songDuration: TextView) {
        mContext = context
        initializeMediaPlayer(ringtone)
        initializeSeekBar(seekBar)
    }

    @SuppressLint("SetTextI18n")
    fun playPauseBtn(playPauseBtn: ImageView) {
        if (playPauseBtn.tag == "play") {
            mMediaPlayer?.start()
        } else {
            mMediaPlayer?.pause()
            mediaPlayerCallback.onUpdateTime(Utils.formatter(userSelectedPosition) + "/" + Utils.formatter(mMediaPlayer?.duration!!))
        }
    }

    private fun initializeSeekBar(seekBar: SeekBar) {
        this.seekBar = seekBar
        this.seekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            @SuppressLint("SetTextI18n")
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                userSelectedPosition = progress
                mediaPlayerCallback.onUpdateTime(Utils.formatter(progress) + "/" + Utils.formatter(mMediaPlayer?.duration!!))
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                mMediaPlayer!!.seekTo(userSelectedPosition)
            }
        })
    }

    override fun onCompletion(mp: MediaPlayer?) {
    }

    @SuppressLint("SetTextI18n")
    override fun onPrepared(mp: MediaPlayer?) {
        handler.postDelayed(updateSongTime, 100)
        mMediaPlayer?.start()
        mediaPlayerCallback.onUpdateTime(Utils.formatter(mMediaPlayer?.duration!!) + "/" + Utils.formatter(mMediaPlayer?.duration!!))
        seekBar.max = mMediaPlayer?.duration!!

    }

    override fun onBufferingUpdate(mp: MediaPlayer?, percent: Int) {
        if (percent < seekBar.max)
            seekBar.secondaryProgress = percent * seekBar.max
    }

    override fun onDestroy(owner: LifecycleOwner) {
        if (mMediaPlayer != null) {
            mMediaPlayer?.stop()
            mMediaPlayer?.release()
            mMediaPlayer = null
        }
    }

    override fun onResume(owner: LifecycleOwner) {
        if(mMediaPlayer != null)
            mMediaPlayer?.start()
    }

    override fun onPause(owner: LifecycleOwner) {
        if(mMediaPlayer != null)
            mMediaPlayer?.pause()
    }
}