package com.example.threads.data.models

import android.net.Uri

data class ThreadResponse(
    val id: Int,
    val content: String,
    val thread_media: Uri?,
    val author: String,
    val likes: String,
    val comments_count: String,
    val quoted_thread: Int
)
