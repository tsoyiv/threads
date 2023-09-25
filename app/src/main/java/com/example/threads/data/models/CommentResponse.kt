package com.example.threads.data.models

data class CommentResponse(
    val id: Int,
    val user: String,
    val content: String,
    val created: String,
    val likes: Int,
    val parent: Int?,
    val replies: List<CommentResponse>
)

data class ThreadWithCommentsResponse(
    val id: Int,
    val content: String,
    val thread_media: String?,
    val author: String,
    val likes: Int,
    val comments_count: Int,
    val comments: List<CommentResponse>
)

