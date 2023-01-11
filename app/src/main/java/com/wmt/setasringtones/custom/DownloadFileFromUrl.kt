package com.wmt.setasringtones.custom

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.media.RingtoneManager
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.widget.Toast
import com.wmt.setasringtones.utils.Utils
import java.io.*
import java.net.URL
import java.util.concurrent.Executors


object DownloadFileFromUrl {
    private val executor = Executors.newSingleThreadExecutor()
    private val handler = Handler(Looper.getMainLooper())

    fun download(
        context: Activity,
        uriString: String,
        setAudioAs: String?,
        outputFile: String,
    ) {
        val progressDialog = CustomProgressDialog(context)
        progressDialog.show()
        Toast.makeText(context, "Your file is downloading", Toast.LENGTH_SHORT).show()
        executor.execute {
            try {
                URL(uriString).openStream().use { input ->
                    FileOutputStream(outputFile).use { output ->
                        input.copyTo(output)
                    }
                }
                handler.post {
                    Toast.makeText(context, "Downloaded", Toast.LENGTH_SHORT).show()
                    if (setAudioAs != null)
                        setNotification(
                            outputFile,
                            context,
                            setAudioAs,
                            progressDialog
                        )
                    else progressDialog.dismiss()
                }
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
                handler.post {
                    progressDialog.dismiss()
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                }
            } catch (e: IOException) {
                e.printStackTrace()
                handler.post {
                    progressDialog.dismiss()
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setNotification(
        pathString: String, context: Context, type: String, progressDialog: CustomProgressDialog
    ) {
        try {
            val f = File(pathString)
            // Insert the ring tone to the content provider
            val value = ContentValues()
            value.put(MediaStore.MediaColumns.DATA, f.absolutePath)
            value.put(MediaStore.MediaColumns.TITLE, f.name)
            value.put(MediaStore.MediaColumns.SIZE, f.length())
            value.put(MediaStore.MediaColumns.MIME_TYPE, Utils.getMIMEType(pathString))
            value.put(
                MediaStore.Audio.Media.ARTIST,
                context.getString(com.wmt.setasringtones.R.string.app_name)
            )
            value.put(MediaStore.Audio.Media.IS_MUSIC, true)
            var managerType = 0
            if (type.equals("Ringtone", ignoreCase = true)) {
                value.put(MediaStore.Audio.Media.IS_RINGTONE, true);
                value.put(MediaStore.Audio.Media.IS_NOTIFICATION, false);
                value.put(MediaStore.Audio.Media.IS_ALARM, false);
                managerType = RingtoneManager.TYPE_RINGTONE;
            } else if (type.equals("Message", ignoreCase = true)) {
                value.put(MediaStore.Audio.Media.IS_RINGTONE, false);
                value.put(MediaStore.Audio.Media.IS_NOTIFICATION, true);
                value.put(MediaStore.Audio.Media.IS_ALARM, false);
                managerType = RingtoneManager.TYPE_NOTIFICATION;
            } else if (type.equals("Alarm", ignoreCase = true)) {
                value.put(MediaStore.Audio.Media.IS_RINGTONE, false);
                value.put(MediaStore.Audio.Media.IS_NOTIFICATION, false);
                value.put(MediaStore.Audio.Media.IS_ALARM, true);
                managerType = RingtoneManager.TYPE_ALARM;
            }

            val uri = MediaStore.Audio.Media.getContentUriForPath(f.absolutePath)
            context.contentResolver
                .delete(uri!!, MediaStore.MediaColumns.DATA + "=\"" + f.absolutePath + "\"", null)

            //Ok now insert it
            val newUri: Uri = context.contentResolver.insert(uri, value)!!

            //Ok now set the ringtone_app from the content manager's uri, NOT the file's uri
            RingtoneManager.setActualDefaultRingtoneUri(
                context,
                managerType,
                newUri
            )
            Utils.setRingtoneSuccessMessage(managerType, context)
            progressDialog.dismiss()

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            progressDialog.dismiss()
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()

        }
    }
}