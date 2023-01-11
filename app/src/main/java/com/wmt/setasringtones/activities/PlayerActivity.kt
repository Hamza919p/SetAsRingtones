package com.wmt.setasringtones.activities

import android.Manifest
import com.wmt.setasringtones.R
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import com.google.gson.Gson
import com.wmt.setasringtones.`interface`.PlayerItemsClickListener
import com.wmt.setasringtones.adapter.PlayerAdapter
import com.wmt.setasringtones.custom.CustomMediaPlayer
import com.wmt.setasringtones.custom.CustomProgressDialog
import com.wmt.setasringtones.databinding.ActivityPlayerBinding
import com.wmt.setasringtones.models.TonesItem
import com.wmt.setasringtones.utils.*
import com.wmt.setasringtones.viewModels.PlayerActivityViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class PlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayerBinding
    private lateinit var viewModel: PlayerActivityViewModel
    private lateinit var selectedRingtoneDetailJson: TonesItem
    private lateinit var progressDialog: CustomProgressDialog
    private var playerAdapter: PlayerAdapter? = null
    private var setAudioAs: String? = null
    var isAudioPermissionGranted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_player)
        val ringtoneDetail = intent.getStringExtra("toneDetail")
        selectedRingtoneDetailJson = Gson().fromJson(ringtoneDetail, TonesItem::class.java)
        progressDialog = CustomProgressDialog(this)
        setSupportActionBar(binding.toolbar)
        Utils.setActionBar(supportActionBar, selectedRingtoneDetailJson.title)
        checkAudioPermission()
        Utils.initializeBannerAds(this, binding.bannersAdd)
        lifecycle.addObserver(CustomMediaPlayer.newInstance())
    }

    private fun checkAudioPermission() {
        if (!Permissions.checkAudioPermission(this)) {
            audioPermissionResultLauncher.launch(
                arrayOf(
                    Manifest.permission.RECORD_AUDIO
                )
            )
        } else {
            isAudioPermissionGranted = true
            initializeViewModel()
            retrieveRelatedTones()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    private fun initializeViewModel() {
        viewModel = ViewModelProvider(this)[PlayerActivityViewModel::class.java]
        binding.apply {
            playerActivity = viewModel
            lifecycleOwner = this@PlayerActivity
        }
    }

    private fun retrieveRelatedTones() {
        progressDialog.show()
        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.observeRelatedTonesResponse(selectedRingtoneDetailJson.id.toString())
        }
        viewModel.categoriesData.observe(this) {
            if (it.isSuccessful) {
                if (it != null && it.body()?.size != 0) {
                    initializeAdapter(it.body()!!)
                    binding.tvEmpty.visibility = View.GONE
                } else {
                    binding.tvEmpty.visibility = View.VISIBLE
                }
            } else {
                binding.tvEmpty.visibility = View.VISIBLE

            }
        }
    }

    private fun observeMediaPlayerTime() {
        viewModel.updatedTime.observe(this) {
            playerAdapter?.textView?.text = it
        }
    }

    private fun initializeAdapter(relatedTones: ArrayList<TonesItem>) {
        playerAdapter = PlayerAdapter(
            this,
            relatedTones,
            selectedRingtoneDetailJson,
            viewModel,
            isAudioPermissionGranted,
            progressDialog
        )
        val manager = GridLayoutManager(this, 2)
        manager.spanSizeLookup = object : SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (playerAdapter?.isHeaderOrAds(position)!!) manager.spanCount else 1
            }
        }
        binding.rvRelatedTones.apply {
            adapter = playerAdapter
            layoutManager = manager
            recycledViewPool.setMaxRecycledViews(0, 0)
        }

        playerAdapter?.setOnClickListener(object : PlayerItemsClickListener {
            override fun onDownloadClick() {
                setAudioAs = null
                if (!Permissions.checkWritePermission(this@PlayerActivity)) {
                    writePermissionResultLauncher.launch(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                } else {
                    viewModel.downloadAndSetAsRingtone(
                        this@PlayerActivity,
                        selectedRingtoneDetailJson.ringtone,
                        selectedRingtoneDetailJson.title,
                        setAudioAs
                    )
                }
            }

            override fun onSetAsRingtoneClick(item: String) {
                setAudioAs = item
                if (!Permissions.checkWritePermission(this@PlayerActivity)
                ) {
                    writePermissionResultLauncher.launch(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                } else {

                    //when download option is clicked
                    if (item == getString(R.string.download)) {
                        setAudioAs = null

                        if (!Permissions.checkWritePermission(this@PlayerActivity)) {
                            writePermissionResultLauncher.launch(
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                            )
                        } else {
                            viewModel.downloadAndSetAsRingtone(
                                this@PlayerActivity,
                                selectedRingtoneDetailJson.ringtone,
                                selectedRingtoneDetailJson.title,
                                setAudioAs
                            )
                        }
                    }

                    //when download as well as set as ringtone option is clicked
                    else {
                        if (Settings.System.canWrite(this@PlayerActivity)) {
                            viewModel.downloadAndSetAsRingtone(
                                this@PlayerActivity,
                                selectedRingtoneDetailJson.ringtone,
                                selectedRingtoneDetailJson.title,
                                setAudioAs
                            )
                        } else {
                            Launcher.startActionManageWriteSettings(this@PlayerActivity)
                        }
                    }
                }
            }
        })
        observeMediaPlayerTime()

    }

    val writePermissionResultLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { result ->
        if (result) {
            if (setAudioAs != null) {
                if (Settings.System.canWrite(this))
                    viewModel.downloadAndSetAsRingtone(
                        this@PlayerActivity,
                        selectedRingtoneDetailJson.ringtone,
                        selectedRingtoneDetailJson.title,
                        setAudioAs
                    )
                else
                    Launcher.startActionManageWriteSettings(this@PlayerActivity)
            } else {
                viewModel.downloadAndSetAsRingtone(
                    this@PlayerActivity,
                    selectedRingtoneDetailJson.ringtone,
                    selectedRingtoneDetailJson.title,
                    setAudioAs
                )
            }
        }
    }

    private val audioPermissionResultLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.RECORD_AUDIO, false) -> {
                isAudioPermissionGranted = true
            }
        }
        initializeViewModel()
        retrieveRelatedTones()
    }

    override fun onResume() {
        if (playerAdapter != null) {
            Utils.setPlayPauseDrawableState(this, playerAdapter?.playPauseBtn!!)
        }
        super.onResume()
    }

    override fun onPause() {
        if (playerAdapter != null)
            Utils.setPlayPauseDrawableState(this, playerAdapter?.playPauseBtn!!)
        super.onPause()
    }

}