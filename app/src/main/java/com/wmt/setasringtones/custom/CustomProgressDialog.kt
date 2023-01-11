package com.wmt.setasringtones.custom

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import android.widget.ProgressBar
import com.wmt.setasringtones.R

class CustomProgressDialog(context: Context) : Dialog(context) {

    var loadingIndicator: ProgressBar

    init {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.setContentView(R.layout.progress_dialog)
        this.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCancelable(false)
        loadingIndicator = findViewById(R.id.rotate_loading)
        setOnCancelListener {
            loadingIndicator.visibility = View.GONE
            cancel()
        }
    }

    override fun show() {
        try {
            loadingIndicator.visibility = View.VISIBLE
            super.show()
        } catch (e: Exception) {
        }
    }

    override fun hide() {
        try {
            loadingIndicator.visibility = View.GONE
            super.hide()
        } catch (e: Exception) {
        }
    }
}