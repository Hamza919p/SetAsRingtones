package com.wmt.setasringtones.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wmt.setasringtones.R

class LoadMoreAdapter : LoadStateAdapter<RecyclerView.ViewHolder>() {
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, loadState: LoadState) {
        holder as LoadMoreViewHolder
        holder.loadMore.isVisible = loadState is LoadState.Loading
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.load_more, parent, false)
        return LoadMoreViewHolder(view)
    }

    private inner class LoadMoreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val loadMore: ProgressBar = itemView.findViewById(R.id.load_more)
    }
}