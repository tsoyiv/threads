package com.example.threads.data.repositories

import com.example.threads.data.api.ThreadActionAPI
import com.example.threads.data.models.CommentRequest
import com.example.threads.data.models.CommentResponse
import com.example.threads.data.models.ThreadRequest
import com.example.threads.data.models.ThreadResponse
import com.example.threads.data.models.ThreadWithCommentsResponse
import retrofit2.Call

class ThreadRepository(private val threadActionAPI: ThreadActionAPI) {

    fun createThread(token: String, content: ThreadRequest): Call<ThreadResponse> {
        return threadActionAPI.createThread(token, content)
    }

    fun getThread(token: String): Call<List<ThreadResponse>> {
        return threadActionAPI.getThreadMainPage(token)
    }

    fun getThreadDetails(token: String, threadId: Int): Call<ThreadResponse> {
        return threadActionAPI.getThreadDetails(token, threadId)
    }

    fun writeComment(token: String, threadId: Int, content: String): Call<CommentResponse> {
        val commentRequest = CommentRequest(content)
        return threadActionAPI.writeComment(token, threadId, commentRequest)
    }

    fun getThreadsWithComments(token: String): Call<List<ThreadWithCommentsResponse>> {
        return threadActionAPI.getThreadsWithComments(token)
    }
}