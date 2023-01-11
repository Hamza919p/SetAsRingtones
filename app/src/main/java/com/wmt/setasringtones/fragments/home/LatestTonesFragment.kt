package com.wmt.setasringtones.fragments.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.wmt.setasringtones.R
import com.wmt.setasringtones.adapter.LoadMoreAdapter
import com.wmt.setasringtones.adapter.TonesAdapter
import com.wmt.setasringtones.databinding.FragmentLatestTonesBinding
import com.wmt.setasringtones.viewModels.fragments.home.HomeFragmentsViewModel
import kotlinx.coroutines.flow.collectLatest

class LatestTonesFragment : Fragment() {
    private lateinit var binding: FragmentLatestTonesBinding
    private lateinit var viewModel: HomeFragmentsViewModel
    private lateinit var tonesAdapter: TonesAdapter
    private var isAdapterInitialized = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_latest_tones, container, false)
        return binding.root
    }

    override fun onResume() {
        if(!isAdapterInitialized) {
            initializeViewModel()
            initializeAdapter()
            updateTonesAdapter()
            swipeRefreshListener()
        }
        super.onResume()
    }

    private fun initializeViewModel() {
        viewModel = HomeFragmentsViewModel()
        binding.apply {
            latestTones = viewModel
            lifecycleOwner = this@LatestTonesFragment
        }
    }

    private fun initializeAdapter() {
        tonesAdapter = TonesAdapter(requireActivity())
        val manager = GridLayoutManager(requireContext(), 2)
        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (tonesAdapter.isBannersAdView(position)) manager.spanCount else 1
            }
        }

        binding.rvLatestTones.apply {
            adapter = tonesAdapter.withLoadStateFooter(LoadMoreAdapter())
            layoutManager = manager
        }
        tonesAdapter.addLoadStateListener { loadState ->
            if(loadState.source.refresh is LoadState.NotLoading){
                if(tonesAdapter.itemCount < 1){
                    binding.tvEmpty.visibility = View.VISIBLE
                    binding.rvLatestTones.visibility = View.GONE
                }else{
                    binding.tvEmpty.visibility = View.GONE
                    binding.rvLatestTones.visibility = View.VISIBLE
                }
            }
        }

        isAdapterInitialized = true
    }

    private fun updateTonesAdapter() {
        binding.swipeRefresh.isRefreshing = true
        lifecycleScope.launchWhenCreated {
           viewModel.getTones(binding.swipeRefresh, "Latest").collectLatest {
               tonesAdapter.submitData(it)
           }
        }
    }

    private fun swipeRefreshListener() {
        binding.swipeRefresh.setColorSchemeColors(
            ContextCompat.getColor(
                requireContext(),
                R.color.primary
            )
        )
        binding.swipeRefresh.setOnRefreshListener {
            updateTonesAdapter()
            binding.swipeRefresh.isRefreshing = false
        }
    }

}