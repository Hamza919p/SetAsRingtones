package com.wmt.setasringtones.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.wmt.setasringtones.R
import com.wmt.setasringtones.adapter.LoadMoreAdapter
import com.wmt.setasringtones.adapter.TonesAdapter
import com.wmt.setasringtones.databinding.ActivitySearchBinding
import com.wmt.setasringtones.utils.Launcher
import com.wmt.setasringtones.utils.Utils
import com.wmt.setasringtones.viewModels.SearchActivityViewModel
import kotlinx.coroutines.flow.collectLatest

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private lateinit var viewModel: SearchActivityViewModel
    private lateinit var tonesAdapter: TonesAdapter
    private var searchQuery: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search)
        searchQuery = intent?.getStringExtra("searchQuery")?.trim()
        initializeViewModel()
        initializeAdapter()
        updateTonesAdapter()
        initializeRefreshListener()
        setSupportActionBar(binding.toolbar)
        Utils.initializeBannerAds(this, binding.bannersAdd)
        Utils.setActionBar(supportActionBar, "Search Result")
    }

    private fun initializeViewModel() {
        viewModel = SearchActivityViewModel()
        binding.apply {
            searchActivity = viewModel
            lifecycleOwner = this@SearchActivity
        }
    }

    private fun initializeAdapter() {
        tonesAdapter = TonesAdapter(this)
        val manager = GridLayoutManager(this, 2)
        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (tonesAdapter.isBannersAdView(position)) manager.spanCount else 1
            }
        }

        binding.rvSearchResult.apply {
            adapter = tonesAdapter.withLoadStateFooter(LoadMoreAdapter())
            layoutManager = manager
        }
        tonesAdapter.addLoadStateListener { loadState ->
            if (loadState.source.refresh is LoadState.NotLoading) {
                if (tonesAdapter.itemCount < 1) {
                    binding.tvEmpty.visibility = View.VISIBLE
                    binding.rvSearchResult.visibility = View.GONE
                } else {
                    binding.tvEmpty.visibility = View.GONE
                    binding.rvSearchResult.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun updateTonesAdapter() {
        binding.swipeRefresh.isRefreshing = true
        lifecycleScope.launchWhenCreated {
            viewModel.getTones(binding.swipeRefresh, searchQuery!!).collectLatest {
                tonesAdapter.submitData(it)
            }
        }
    }

    private fun initializeRefreshListener() {
        binding.swipeRefresh.setColorSchemeColors(
            ContextCompat.getColor(
                this,
                R.color.primary
            )
        )
        binding.swipeRefresh.setOnRefreshListener {
            updateTonesAdapter()
            binding.swipeRefresh.isRefreshing = false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.app_bar, menu)
        val menuItem = menu?.findItem(R.id.search)
        val searchView = menuItem?.actionView as SearchView
        val searchAutoComplete = searchView.findViewById(androidx.appcompat.R.id.search_src_text) as SearchView.SearchAutoComplete
        searchAutoComplete.setTextColor(getColor(R.color.white))

        //expand search view by default
        searchView.onActionViewExpanded()
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        searchView.setIconifiedByDefault(true)

        searchView.setQuery(searchQuery, false)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Launcher.startSearchActivity(this@SearchActivity, query!!)
                searchView.clearFocus()

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
        searchView.clearFocus()
        return super.onCreateOptionsMenu(menu)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

}