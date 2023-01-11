package com.wmt.setasringtones.activities

import android.app.SearchManager
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.view.Menu
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.google.gson.Gson
import com.wmt.setasringtones.R
import com.wmt.setasringtones.`interface`.WorkProgressListener
import com.wmt.setasringtones.custom.CustomProgressDialog
import com.wmt.setasringtones.databinding.ActivityMainBinding
import com.wmt.setasringtones.utils.Launcher
import com.wmt.setasringtones.utils.Utils
import com.wmt.setasringtones.viewModels.MainActivityViewModel


class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        Utils.initializeBannerAds(this, binding.bannersAdd)
        //intent: means data is coming from deep link
        if(intent != null && intent.data != null && (intent?.data!!.scheme.equals("https", ignoreCase = true))) {
            initializeViewModel()
        }
        bottomBarInitialization()
        setSupportActionBar(binding.toolbar)
    }

    private fun initializeViewModel() {
        val customProgressDialog = CustomProgressDialog(this)
        customProgressDialog.show()
        val viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
        viewModel.observerDeepLink(intent,customProgressDialog)
        viewModel.deepLinkTone.observe(this) {
            customProgressDialog.dismiss()
            Launcher.startPlayerActivity(this, Gson().toJson(it.body()))
        }
    }

    private fun bottomBarInitialization() {
        navController = findNavController(this, R.id.nav_host_fragment)
        setupWithNavController(binding.bottomNav, navController)
//        binding.bottomNav.setOnNavigationItemReselectedListener {
//            "Reselect blocked."
//        }

        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> navController.navigate(R.id.home_fragment)
                R.id.channel -> navController.navigate(R.id.channel_fragment)
                R.id.category -> navController.navigate(R.id.category_fragment)
                R.id.settings -> navController.navigate(R.id.settings_fragment)
            }
            true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.app_bar, menu)
        val menuItem = menu?.findItem(R.id.search)
        val searchView = menuItem?.actionView as SearchView

        val searchAutoComplete =
            searchView.findViewById(androidx.appcompat.R.id.search_src_text) as SearchView.SearchAutoComplete
        searchAutoComplete.setTextColor(getColor(R.color.white))

        val searchManager = this.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(searchManager.getSearchableInfo(this.componentName))
        searchView.queryHint = "Search"
        val spanString = SpannableString(menuItem.title.toString())
        spanString.setSpan(
            ForegroundColorSpan(Color.WHITE),
            0,
            spanString.length,
            0
        )

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Launcher.startSearchActivity(this@MainActivity, query!!)
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        when (navController.currentDestination?.id) {
            R.id.home_fragment -> binding.bottomNav.menu.getItem(0).isChecked = true
            R.id.channel_fragment -> binding.bottomNav.menu.getItem(1).isChecked = true
            R.id.category_fragment -> binding.bottomNav.menu.getItem(2).isChecked = true
            R.id.settings_fragment -> binding.bottomNav.menu.getItem(3).isChecked = true
            else -> finish()
        }
    }


}