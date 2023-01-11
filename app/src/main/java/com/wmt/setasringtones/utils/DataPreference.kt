package com.wmt.setasringtones.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.wmt.setasringtones.R

class DataPreference(context: Context) {

    private var prefs : SharedPreferences? = null
    private var sessionId: String = "sessionId"

    companion object {
        private var instance: DataPreference? = null

        @Synchronized
        fun getInstance(applicationContext: Context): DataPreference {
            if (instance == null) {
                instance = DataPreference(applicationContext)
            }
            return instance!!
        }
    }

    init {
        prefs = context.getSharedPreferences(
            context.getString(R.string.app_name),
            Context.MODE_PRIVATE
        )
    }

    fun getMediaPlayerSessionId(): Int? {
        return prefs!!.getInt(sessionId, 0)
    }

    fun setMediaPlayerSessionId(sessionId: Int) {
        prefs!!.edit().putInt(this.sessionId, sessionId).apply()
    }

}