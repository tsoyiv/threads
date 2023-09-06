package com.example.threads.data.repositories

import com.example.threads.data.api.ThreadActionAPI
import com.example.threads.data.models.ThreadRequest
import com.example.threads.data.models.ThreadResponse
import retrofit2.Call

class ThreadRepository(private val threadActionAPI: ThreadActionAPI) {

    fun createThread(token: String, content: ThreadRequest): Call<ThreadResponse> {
        return threadActionAPI.createThread(token, content)
    }
}