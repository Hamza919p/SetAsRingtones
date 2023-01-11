package com.wmt.setasringtones.custom

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.wmt.setasringtones.`interface`.AlertDialogListener

class CustomAlertDialog(var activity: Activity) {
    private lateinit var alertDialogListener: AlertDialogListener
    fun alertDialogCallback(mCallback: AlertDialogListener) {
        alertDialogListener = mCallback
    }

    fun initialize() {
        val alertDialogBuilder = AlertDialog.Builder(activity)
        alertDialogBuilder.setTitle("Caution!");
        alertDialogBuilder.setMessage("Please confirm. This will erase all your local app data.");

        alertDialogBuilder.setPositiveButton("Erase") { _, _ ->
            alertDialogListener.onErase()
        }
        alertDialogBuilder.setNegativeButton("No") { dialog,_ ->
            dialog.dismiss()
        }
        alertDialogBuilder.create().show()
    }

}