package com.example.threads.data.models

data class CombinedResponse(
    val id: Int,
    val content: String,
    val thread_media: String?,
    val author: String,
    val likes: Int,
    val comments_count: Int,
    val comments: List<CombinedResponse>, // Recursive structure
    val user: String?,
    val created: String?,
    val parent: Int?,
    val replies: List<CombinedResponse>?
)

