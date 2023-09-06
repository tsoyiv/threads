package com.example.threads.data.api

import com.example.threads.data.models.ThreadRequest
import com.example.threads.data.models.ThreadResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT

interface ThreadActionAPI {

    @POST("v3/create/")
    fun createThread(
        @Header("Authorization") token: String,
        @Body request: ThreadRequest
    ): Call<ThreadResponse>
}