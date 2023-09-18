package com.example.threads.data.repositories

import com.example.threads.data.api.UserDataAPI
import com.example.threads.data.models.ProfileUpdateRequest
import com.example.threads.data.models.UserOwnInfo
import com.example.threads.models.SearchUserInfo
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
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

    fun searchUsers(token: String, query: String): Call<List<SearchUserInfo>> {
        return userDataAPI.searchUsers(token, query)
    }

//    fun uploadProfilePicture(imageFile: File, callback: Callback<Unit>) {
//        val requestFile = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
//        val profilePicturePart = MultipartBody.Part.createFormData("profile_picture", imageFile.name, requestFile)
//
//        userDataAPI.uploadProfilePicture(profilePicturePart).enqueue(callback)
//    }

//    fun uploadProfilePicture(imageData: ByteArray, callback: Callback<Unit>) {
//        val requestBody = RequestBody.create("image/*".toMediaTypeOrNull(), imageData)
//        val profilePicturePart = MultipartBody.Part.createFormData("profile_picture", "image.jpg", requestBody)
//
//        userDataAPI.uploadProfilePicture(profilePicturePart).enqueue(callback)
//    }

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
