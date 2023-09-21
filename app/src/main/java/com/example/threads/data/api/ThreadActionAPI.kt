package com.example.threads.data.api

import com.example.threads.data.models.ThreadRequest
import com.example.threads.data.models.ThreadResponse
import com.example.threads.data.models.UserOwnInfo
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ThreadActionAPI {

    @POST("v3/create/")
    fun createThread(
        @Header("Authorization") token: String,
        @Body request: ThreadRequest
    ): Call<ThreadResponse>

    @GET("v3/threads/")
    fun getThreadMainPage(@Header("Authorization") token: String): Call<List<ThreadResponse>>

    @GET("v3/threads/{id}/")
    fun getThreadDetails(
        @Header("Authorization") token: String,
        @Path("id") threadId: Int
    ): Call<ThreadResponse>

    @POST("v3/threads/{thread_id}/like/")
    fun likeThread(
        @Header("Authorization") token: String,
        @Path("thread_id") threadId: Int
    ): Call<ThreadResponse>
}