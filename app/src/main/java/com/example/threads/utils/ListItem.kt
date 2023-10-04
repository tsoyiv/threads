package com.example.threads.utils

import com.example.threads.data.models.CommentResponse
import com.example.threads.data.models.ThreadWithCommentsResponse

sealed class ListItem {
    data class CommentItem(val comment: CommentResponse) : ListItem()
    data class ThreadItem(val thread: ThreadWithCommentsResponse) : ListItem()
}
