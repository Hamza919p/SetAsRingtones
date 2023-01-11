package com.wmt.setasringtones.activities

import android.os.Bundle
import com.wmt.setasringtones.databinding.ActivitySplashBinding
import com.wmt.setasringtones.utils.Launcher
import java.util.*

class SplashActivity : BaseActivity()  {
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startLandingActivity()

    }
    private fun startLandingActivity() {
        Timer().schedule(object : TimerTask() {
            override fun run() {
                Launcher.startMainActivity(this@SplashActivity)
            }
        }, 3000)
    }
}