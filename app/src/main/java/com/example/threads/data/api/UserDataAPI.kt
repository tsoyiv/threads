package com.example.threads.data.api

import android.net.Uri
import com.example.threads.data.models.UserInfoResponse
import com.example.threads.data.models.UserOwnInfo
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface UserDataAPI {

    @Multipart
    @PUT("v1/profile/update/")
    fun updateUserInfo(
        @Header("Authorization") token: String,
        @Part("username") user_name: RequestBody,
        @Part("name") name: RequestBody,
        @Part profile_picture: MultipartBody.Part,
        @Part("bio") bio: RequestBody,
        @Part("link") dateOfBirth: RequestBody,
        @Part("is_private") isPrivate: RequestBody,
    ): Call<Any>

    @GET("v1/user-info/")
    suspend fun getUserInfo(@Header("Authorization") token: String): Response<UserOwnInfo>
}