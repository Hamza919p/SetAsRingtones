package com.wmt.setasringtones.fragments.bottomNav

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wmt.setasringtones.adapter.HomeViewPagerAdapter
import com.wmt.setasringtones.databinding.FragmentHomeBinding
import com.wmt.setasringtones.fragments.BaseBottomTabFragment
import com.wmt.setasringtones.custom.CustomTabLayout

class HomeFragment : BaseBottomTabFragment() {
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.pager.adapter = HomeViewPagerAdapter(requireActivity())
        binding.pager.currentItem = 0
        CustomTabLayout.setHomeTabLayout(requireActivity(), binding.tabLayout, binding.pager)
    }

}