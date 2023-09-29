package com.example.threads.data.models

import java.io.Serializable

data class ThreadResponse(
    val id: Int,
    val content: String,
    val thread_media: String,
    val author: String,
    val username: String,
    val likes: String,
    val comments_count: String,
    val quoted_thread: Int
) : java.io.Serializable
