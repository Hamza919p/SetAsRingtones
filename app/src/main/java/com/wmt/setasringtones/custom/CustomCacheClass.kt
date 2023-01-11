package com.wmt.setasringtones.custom

import android.app.Activity
import android.content.Context
import android.os.Environment
import com.wmt.setasringtones.`interface`.AlertDialogListener
import com.wmt.setasringtones.utils.Launcher
import java.io.File

class CustomCacheClass(var activity: Activity) {
    fun initialize() {
        val customAlertDialog = CustomAlertDialog(activity)
        customAlertDialog.initialize()
        customAlertDialog.alertDialogCallback(object : AlertDialogListener {
            override fun onErase() {
                deleteCache()
                Launcher.startSplashActivity(activity)
            }
        })
    }

    private fun deleteCache() {
        try {
            val dir: File =  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_RINGTONES)
            deleteDir(dir)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    private fun deleteDir(dir: File?): Boolean {
        return if (dir != null && dir.isDirectory) {
            val children = dir.list()
            for (i in children.indices) {
                val success = deleteDir(File(dir, children[i]))
                if (!success) {
                    return false
                }
            }
            dir.delete()
        } else if (dir != null && dir.isFile) {
            dir.delete()
        } else {
            false
        }
    }
}