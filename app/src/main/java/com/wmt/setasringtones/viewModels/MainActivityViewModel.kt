package com.wmt.setasringtones.viewModels

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wmt.setasringtones.`interface`.WorkProgressListener
import com.wmt.setasringtones.custom.CustomProgressDialog
import com.wmt.setasringtones.models.TonesItem
import com.wmt.setasringtones.repository.DeepLinkRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class MainActivityViewModel : ViewModel() {

    private var deepLinkLiveData = MutableLiveData<Response<TonesItem>>()
    val deepLinkTone: LiveData<Response<TonesItem>>
        get() = deepLinkLiveData

    fun observerDeepLink(intent: Intent, customProgressDialog: CustomProgressDialog) {
        val deepLinkRepository = DeepLinkRepository()
        val check = intent.data.toString()
        Log.d("deepLinkData -> ", check)
        if (check.contains("https://www.setasringtones.com/", ignoreCase = true)) {
            try {
                val subString = check.substring(31)
//                val split = subString.split("/")
//                val ringtoneId = split[2]
                val ringtoneId = subString.reversed().split("-")[0].reversed()
                viewModelScope.launch(Dispatchers.Main) {
                    deepLinkLiveData.value = deepLinkRepository.getToneThroughDeepLink(ringtoneId)
                }
            } catch (e: Exception) {
               customProgressDialog.dismiss()
                Log.d("DeepLink Exception", e.message.toString())
            }
        }
    }

}