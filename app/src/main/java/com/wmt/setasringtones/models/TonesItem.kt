package com.wmt.setasringtones.models

data class TonesItem(
    val id: Int,
    val user: String,
    val title: String,
    val featured_ringtone: String,
    val ringtone: String,
    val description: String,
    val tags: String,
    val downloads: Int,
    val created: String,
    val category_id: Int,
    val category_heading: String,
    val category_name: String,
)