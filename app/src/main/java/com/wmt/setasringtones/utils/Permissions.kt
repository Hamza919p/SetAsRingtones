package com.wmt.setasringtones.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.icu.text.SymbolTable
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

object Permissions : AppCompatActivity() {
    fun checkWritePermission(context: Context): Boolean {
        val result = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        return result == PackageManager.PERMISSION_GRANTED
    }

    fun checkAudioPermission(context: Context): Boolean {
        val result = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.RECORD_AUDIO
        )
        return result == PackageManager.PERMISSION_GRANTED
    }

}