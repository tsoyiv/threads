package com.example.threads.data.api

import com.example.threads.data.models.FollowersResponse
import com.example.threads.data.models.ProfileAvatarResponse
import com.example.threads.data.models.ProfileUpdateRequest
import com.example.threads.data.models.SubscribeRequest
import com.example.threads.data.models.SubscribeResponse
import com.example.threads.data.models.UserOwnInfo
import com.example.threads.models.SearchUserInfo
import com.example.threads.models.UserFollower
import com.example.threads.models.UserRepresentation
import com.google.gson.annotations.Until
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface UserDataAPI {

//    @Multipart
//    @PUT("v1/profile/update/")
//    fun updateUserInfo(
//        @Header("Authorization") token: String,
//        @Part("username") user_name: RequestBody,
//        @Part("name") name: RequestBody,
//        @Part profile_picture: MultipartBody.Part,
//        @Part("bio") bio: RequestBody,
//        @Part("link") dateOfBirth: RequestBody,
//        @Part("is_private") isPrivate: RequestBody,
//    ): Call<Any>

    @PUT("v1/profile/update/")
    fun updateProfile(
        @Header("Authorization") token: String,
        @Body request: ProfileUpdateRequest
    ): Call<Unit>

    @Multipart
    @POST("v1/profile/avatar/")
    fun uploadProfilePicture(
        @Part profile_picture: MultipartBody.Part
    ): Call<ProfileAvatarResponse>

    @GET("v1/profile/")
    suspend fun getUserInfo(@Header("Authorization") token: String): Response<UserOwnInfo>

    @GET("v1/user/search/")
    fun searchUsers(@Header("Authorization") token: String, @Query("search") query: String): Call<List<SearchUserInfo>>

    @GET("v1/followers/{username}/")
    fun getSubscribersOfUser(@Header("Authorization") token: String, @Path("username") username: String): Call<List<UserFollower>>

//    @GET("v1/followers/{username}/")
//    fun getFollowers(
//        @Header("Authorization") token: String,
//        @Path("username") username: String
//    ): Call<List<FollowersResponse>>

    @POST("v1/followers/")
    fun followUser(@Header("Authorization") token: String, @Body request: SubscribeRequest): Call<SubscribeResponse>
}