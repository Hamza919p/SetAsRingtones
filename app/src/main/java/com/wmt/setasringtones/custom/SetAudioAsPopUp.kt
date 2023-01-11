package com.wmt.setasringtones.custom

import android.content.Context
import android.widget.PopupMenu
import android.widget.TextView
import com.wmt.setasringtones.R
import com.wmt.setasringtones.`interface`.PlayerItemsClickListener


object SetAudioAsPopUp {

    fun open(context: Context, button: TextView, playerItemsClickListener: PlayerItemsClickListener) {
        val popup = PopupMenu(context, button)
        popup.menuInflater.inflate(R.menu.ringtone_types, popup.menu)
        popup.setOnMenuItemClickListener { item ->
            playerItemsClickListener.onSetAsRingtoneClick(item?.title.toString())
            true
        }
        popup.show()
    }

}