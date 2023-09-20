package com.example.threads.data.repositories

import com.example.threads.data.api.ThreadActionAPI
import com.example.threads.data.models.ThreadRequest
import com.example.threads.data.models.ThreadResponse
import com.example.threads.data.models.UserOwnInfo
import retrofit2.Call
import retrofit2.Response

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
}