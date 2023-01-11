package com.wmt.setasringtones.custom

import android.content.Context
import androidx.core.content.ContextCompat
import com.chibde.visualizer.CircleBarVisualizer
import com.wmt.setasringtones.R
import com.wmt.setasringtones.utils.DataPreference


object CustomVisualizerManager {

    fun initialize(context: Context, view: CircleBarVisualizer) {
        customizeVisualizer(context, view)
    }

    private fun customizeVisualizer(context: Context, circleBarVisualizer: CircleBarVisualizer) {
        circleBarVisualizer.setColor(ContextCompat.getColor(context, R.color.primary))
        circleBarVisualizer.setPlayer(DataPreference.getInstance(context).getMediaPlayerSessionId()!!)
    }
}