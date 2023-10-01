package com.example.threads.data.api

import com.example.threads.data.models.CommentRequest
import com.example.threads.data.models.CommentResponse
import com.example.threads.data.models.ThreadRequest
import com.example.threads.data.models.ThreadResponse
import com.example.threads.data.models.ThreadWithCommentsResponse
import retrofit2.Call
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

    @GET("v3/threads/{author_email}/")
    fun getThreadUserThread(
        @Header("Authorization") token: String,
        @Path("author_email") authorEmail: String
    ): Call<List<ThreadResponse>>

    @POST("v3/threads/{thread_id}/like/")
    fun likeThread(
        @Header("Authorization") token: String,
        @Path("thread_id") threadId: Int
    ): Call<ThreadResponse>

    @POST("v3/threads/{thread_id}/comments/")
    fun writeComment(
        @Header("Authorization") token: String,
        @Path("thread_id") threadId: Int,
        @Body request: CommentRequest
    ): Call<CommentResponse>

    @GET("v3/threads_with_comments/{thread_id}/")
    fun getThreadsWithComments(@Header("Authorization") token: String, @Path("thread_id") threadId: Int): Call<List<ThreadWithCommentsResponse>>

    @DELETE("v3/threads/{id}/delete/")
    fun removeThreadById(@Header("Authorization") token: String, @Path("id") id: Int): Call<Unit>
}