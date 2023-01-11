package com.wmt.setasringtones.fragments

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
import com.wmt.setasringtones.activities.CategoryDetailActivity
import com.wmt.setasringtones.adapter.LoadMoreAdapter
import com.wmt.setasringtones.adapter.TonesAdapter
import com.wmt.setasringtones.databinding.FragmentCategoryBestTonesBinding
import com.wmt.setasringtones.viewModels.fragments.categories.CategoryDetailsViewModel
import kotlinx.coroutines.flow.collectLatest

class CategoryBestTonesFragment : Fragment() {
    private lateinit var binding: FragmentCategoryBestTonesBinding
    private lateinit var viewModel: CategoryDetailsViewModel
    private lateinit var tonesAdapter: TonesAdapter
    private var parentActivity: CategoryDetailActivity?= null
    private var isAdapterInitialized = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_category_best_tones, container, false)
        parentActivity = activity as CategoryDetailActivity
        return binding.root
    }

    override fun onResume() {
        if(!isAdapterInitialized) {
            initializeViewModel()
            initializeAdapter()
            updateTonesAdapter()
            initializeRefreshListener()
        }
        super.onResume()
    }

    private fun initializeViewModel() {
        viewModel = CategoryDetailsViewModel()
        binding.apply {
            categoryBestTonesFragment = viewModel
            lifecycleOwner = this@CategoryBestTonesFragment
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

        binding.rvBestTones.apply {
            adapter = tonesAdapter.withLoadStateFooter(LoadMoreAdapter())
            layoutManager = manager
        }
        tonesAdapter.addLoadStateListener { loadState ->
            if(loadState.source.refresh is LoadState.NotLoading){
                if(tonesAdapter.itemCount < 1){
                    binding.tvEmpty.visibility = View.VISIBLE
                    binding.rvBestTones.visibility = View.GONE
                }else{
                    binding.tvEmpty.visibility = View.GONE
                    binding.rvBestTones.visibility = View.VISIBLE
                }
            }
        }
        isAdapterInitialized = true
    }

    private fun updateTonesAdapter() {
        binding.swipeRefresh.isRefreshing = true
        lifecycleScope.launchWhenCreated {
            viewModel.getTones(binding.swipeRefresh, parentActivity?.ringtoneId?.toInt()!!, false).collectLatest {
                tonesAdapter.submitData(it)
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
            updateTonesAdapter()
            binding.swipeRefresh.isRefreshing = false
        }
    }


}