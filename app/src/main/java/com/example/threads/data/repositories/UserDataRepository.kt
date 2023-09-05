package com.example.threads.data.repositories

import com.example.threads.data.api.UserDataAPI
import com.example.threads.data.models.ProfileUpdateRequest
import com.example.threads.data.models.UserOwnInfo
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class UserDataRepository(private val userDataAPI: UserDataAPI) {

    fun updateProfile(token: String, request: ProfileUpdateRequest): Call<Unit> {
        return userDataAPI.updateProfile(token, request)
    }
//    fun updateUserInfo(
//        token: String,
//        username: String,
//        profile_picture: MultipartBody.Part,
//        name: String,
//        bio: String,
//        link: String,
//        isPrivate: Boolean,
//        callback: Callback<Any>
//    ) {
//        val usernameRequestBody = username.toRequestBody(MultipartBody.FORM)
//        val nameRequestBody = name.toRequestBody(MultipartBody.FORM)
//        val bioRequestBody = bio.toRequestBody(MultipartBody.FORM)
//        val linkRequestBody = link.toRequestBody(MultipartBody.FORM)
//        val isPrivateRequestBody = isPrivate.toString().toRequestBody(MultipartBody.FORM)
//
//        userDataAPI.updateUserInfo(
//            token,
//            usernameRequestBody,
//            nameRequestBody,
//            profile_picture,
//            bioRequestBody,
//            linkRequestBody,
//            isPrivateRequestBody
//        ).enqueue(callback)
//    }

    suspend fun getUserInfo(token: String): Response<UserOwnInfo> {
        return userDataAPI.getUserInfo(token)
    }
}
