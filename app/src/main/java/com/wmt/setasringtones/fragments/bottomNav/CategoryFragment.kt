package com.wmt.setasringtones.fragments.bottomNav

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.wmt.setasringtones.R
import com.wmt.setasringtones.adapter.CategoriesAdapter
import com.wmt.setasringtones.databinding.FragmentCategoryBinding
import com.wmt.setasringtones.fragments.BaseBottomTabFragment
import com.wmt.setasringtones.models.CategoriesItem
import com.wmt.setasringtones.viewModels.fragments.categories.CategoriesViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CategoryFragment : BaseBottomTabFragment() {
    private lateinit var binding: FragmentCategoryBinding
    private lateinit var categoriesViewModel: CategoriesViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_category, container, false)
        initializeViewModel()
        retrieveCategories()
        initializeRefreshListener()
        return binding.root
    }

    private fun initializeViewModel() {
        categoriesViewModel = CategoriesViewModel()
        binding.apply {
            categoriesFragment = categoriesViewModel
            lifecycleOwner = this@CategoryFragment
        }
    }

    private fun retrieveCategories() {
        binding.swipeRefresh.isRefreshing = true
        lifecycleScope.launch(Dispatchers.IO) {
            categoriesViewModel.observeCategoriesResponse()
        }
        categoriesViewModel.categoriesData.observe(viewLifecycleOwner) {
            if(it.isSuccessful) {
                if(it != null && it.body()?.size != 0){
                    initializeAdapter(it.body()!!)
                    binding.tvEmpty.visibility = View.GONE
                }
                else{
                    binding.tvEmpty.visibility = View.VISIBLE
                }
            }else{
                binding.tvEmpty.visibility = View.VISIBLE
                Toast.makeText(requireContext(), it.message(), Toast.LENGTH_SHORT).show()
            }
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun initializeAdapter(categories: ArrayList<CategoriesItem>) {
        val categoriesAdapter = CategoriesAdapter(requireActivity(), categories)
        val manager = GridLayoutManager(requireContext(), 2)
        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (categoriesAdapter.isBannersAdView(position)) manager.spanCount else 1
            }
        }
        binding.rvCategories.apply {
            adapter = categoriesAdapter
            layoutManager = manager
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
            retrieveCategories()
            binding.swipeRefresh.isRefreshing = false
        }
    }
}