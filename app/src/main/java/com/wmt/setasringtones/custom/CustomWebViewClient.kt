package com.wmt.setasringtones.custom

import android.content.Context
import android.graphics.Bitmap
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.DefaultLifecycleObserver

class CustomWebViewClient(var context: Context) : WebViewClient(), DefaultLifecycleObserver {

    var customProgressDialog = CustomProgressDialog(context)

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        customProgressDialog.show()
        super.onPageStarted(view, url, favicon)
    }

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        view?.loadUrl(request?.url.toString())
        return true
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        customProgressDialog.dismiss()
        super.onPageFinished(view, url)
    }
}