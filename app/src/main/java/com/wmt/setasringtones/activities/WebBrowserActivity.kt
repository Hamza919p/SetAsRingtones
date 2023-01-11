package com.wmt.setasringtones.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebViewClient
import com.wmt.setasringtones.R
import com.wmt.setasringtones.custom.CustomWebViewClient
import com.wmt.setasringtones.databinding.ActivityWebBrowserBinding

class WebBrowserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWebBrowserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebBrowserBinding.inflate(layoutInflater)
        init(intent?.getStringExtra("url")!!)
        setContentView(binding.root)
    }

    private fun init(url: String) {
        try {
            binding.webView.webViewClient = CustomWebViewClient(this@WebBrowserActivity)
            binding.webView.settings.javaScriptEnabled = true
            binding.webView.loadUrl(url)
        }catch (e: Exception) {
            e.printStackTrace()
        }
    }
}