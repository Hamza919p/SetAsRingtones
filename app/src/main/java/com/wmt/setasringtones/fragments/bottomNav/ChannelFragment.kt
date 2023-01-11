package com.wmt.setasringtones.fragments.bottomNav

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.wmt.setasringtones.R
import com.wmt.setasringtones.`interface`.ItemClickListener
import com.wmt.setasringtones.adapter.ChannelsAdapter
import com.wmt.setasringtones.adapter.LoadMoreAdapter
import com.wmt.setasringtones.databinding.FragmentChannelBinding
import com.wmt.setasringtones.fragments.BaseBottomTabFragment
import com.wmt.setasringtones.utils.Launcher
import com.wmt.setasringtones.viewModels.fragments.ChannelViewModel
import kotlinx.coroutines.flow.collectLatest

class ChannelFragment : BaseBottomTabFragment() {
    private lateinit var binding: FragmentChannelBinding
    private lateinit var viewModel: ChannelViewModel
    private lateinit var channelsAdapter: ChannelsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_channel, container, false)
        initializeViewModel()
        initializeAdapter()
        updateChannelsAdapter()
        initializeRefreshListener()
        return binding.root
    }

    private fun initializeViewModel() {
        viewModel = ChannelViewModel()
        binding.apply {
            channelsFragment = viewModel
            lifecycleOwner = this@ChannelFragment
        }
    }

    private fun initializeAdapter() {
        channelsAdapter = ChannelsAdapter(requireActivity())
        val manager = GridLayoutManager(requireContext(), 2)
        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (channelsAdapter.isBannersAdView(position)) manager.spanCount else 1
            }
        }

        binding.rvChannels.apply {
            adapter = channelsAdapter.withLoadStateFooter(LoadMoreAdapter())
            layoutManager = manager
        }

        channelsAdapter.addLoadStateListener { loadState ->
            if(loadState.source.refresh is LoadState.NotLoading){
                if(channelsAdapter.itemCount < 1){
                    binding.tvEmpty.visibility = View.VISIBLE
                    binding.rvChannels.visibility = View.GONE
                }else{
                    binding.tvEmpty.visibility = View.GONE
                    binding.rvChannels.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun updateChannelsAdapter() {
        binding.swipeRefresh.isRefreshing = true
        lifecycleScope.launchWhenCreated {
            viewModel.retrieveToneChannels(binding.swipeRefresh).collectLatest {
                channelsAdapter.submitData(it)
            }
        }
    }

    private fun initializeRefreshListener() {
        binding.swipeRefresh.setColorSchemeColors(
            ContextCompat.getColor(
                requireContext(),
                R.color.primary
            )
        )
        binding.swipeRefresh.setOnRefreshListener {
            updateChannelsAdapter()
            binding.swipeRefresh.isRefreshing = false
        }
    }
}