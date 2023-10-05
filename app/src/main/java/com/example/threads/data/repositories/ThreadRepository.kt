package com.example.threads.data.repositories

import com.example.threads.data.api.ThreadActionAPI
import com.example.threads.data.models.CommentRequest
import com.example.threads.data.models.CommentResponse
import com.example.threads.data.models.ThreadRequest
import com.example.threads.data.models.ThreadResponse
import com.example.threads.data.models.ThreadUserLikedResponse
import com.example.threads.data.models.ThreadWithCommentsResponse
import retrofit2.Call

class ThreadRepository(private val threadActionAPI: ThreadActionAPI) {

    fun createThread(token: String, content: ThreadRequest): Call<ThreadResponse> {
        return threadActionAPI.createThread(token, content)
    }

    fun getThread(token: String): Call<List<ThreadResponse>> {
        return threadActionAPI.getThreadMainPage(token)
    }

    fun getThreadLikedUser(token: String, threadId: Int): Call<List<ThreadUserLikedResponse>> {
        return threadActionAPI.getThreadLikedUser(token, threadId)
    }

    fun getThreadDetails(token: String, threadId: Int): Call<ThreadResponse> {
        return threadActionAPI.getThreadDetails(token, threadId)
    }

    fun getThreadUserThread(token: String, authorEmail: String): Call<List<ThreadResponse>> {
        return threadActionAPI.getThreadUserThread(token, authorEmail)
    }

    fun writeComment(token: String, threadId: Int, content: String): Call<CommentResponse> {
        val commentRequest = CommentRequest(content)
        return threadActionAPI.writeComment(token, threadId, commentRequest)
    }

    fun getThreadsWithCommentsActivity(
        token: String,
        authorEmail: String
    ): Call<List<CommentResponse>> {
        return threadActionAPI.getThreadsWithCommentsActivity(token, authorEmail)
    }

    fun getThreadsWithComments(
        token: String,
        threadId: Int
    ): Call<List<ThreadWithCommentsResponse>> {
        return threadActionAPI.getThreadsWithComments(token, threadId)
    }

    fun removeThreadById(token: String, id: Int): Call<Unit> {
        return threadActionAPI.removeThreadById(token, id)
    }

    fun likeThread(token: String, threadId: Int): Call<ThreadResponse> {
        return threadActionAPI.likeThread(token, threadId)
    }

    fun likeComment(token: String, commentId: Int) : Call<CommentResponse> {
        return threadActionAPI.likeComment(token, commentId)
    }
}