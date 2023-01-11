package com.wmt.setasringtones.fragments.bottomNav

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.wmt.setasringtones.R
import com.wmt.setasringtones.custom.CustomCacheClass
import com.wmt.setasringtones.databinding.FragmentSettingsBinding
import com.wmt.setasringtones.fragments.BaseBottomTabFragment
import com.wmt.setasringtones.utils.Constants
import com.wmt.setasringtones.utils.Launcher

class SettingFragment : BaseBottomTabFragment(), View.OnClickListener{
    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_settings, container, false)
        initializeClickListeners()
        return binding.root
    }

    private fun initializeClickListeners() {
        binding.tvTermsOfService.setOnClickListener(this)
        binding.tvContactUs.setOnClickListener(this)
        binding.tvPrivacyPolicy.setOnClickListener(this)
        binding.tvAppVersion.setOnClickListener(this)
        binding.tvClearCache.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.tv_terms_of_service -> {
                Launcher.startWebBrowserActivity(requireActivity(), Constants.TERMS_OF_SERVICE_URL)
            }
            R.id.tv_contact_us -> {
                Launcher.startWebBrowserActivity(requireActivity(), Constants.CONTACT_US_URL)

            }
            R.id.tv_privacy_policy -> {
                Launcher.startWebBrowserActivity(requireActivity(), Constants.PRIVACY_POLICY_URL)
            }
            R.id.tv_clear_cache -> {
                val customCacheClassObj = CustomCacheClass(requireActivity())
                customCacheClassObj.initialize()
            }

        }
    }
}