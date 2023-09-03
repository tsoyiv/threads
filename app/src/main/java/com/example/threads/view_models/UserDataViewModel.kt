package com.example.threads.view_models

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.threads.data.models.UserOwnInfo
import com.example.threads.data.repositories.UserDataRepository
import com.example.threads.utils.ImageConverter
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class UserDataViewModel(private val userDataRepository: UserDataRepository) : ViewModel() {

    private val _updateStatus = MutableLiveData<Boolean>()
    val updateStatus: LiveData<Boolean> get() = _updateStatus

    private val _userInfo = MutableLiveData<UserOwnInfo?>()
    val userInfo: LiveData<UserOwnInfo?> = _userInfo

    fun updateUserInfo(
        context: Context,
        token: String,
        username: String,
        name: String,
        profile_picture: Uri,
        bio: String,
        link: String,
        isPrivate: Boolean
    ) {
        val imageFile = ImageConverter.getFile(context, profile_picture)
        if (imageFile != null) {
            val imagePart = prepareImagePart(imageFile)
            val imageParts = imagePart

            Log.d("UpdateUserInfo", "Profile Picture: ${imageFile.absolutePath}")

            userDataRepository.updateUserInfo(
                token,
                username,
                imageParts,
                name,
                bio,
                link,
                isPrivate,
                object :
                    Callback<Any> {
                    override fun onResponse(call: Call<Any>, response: Response<Any>) {
                        _updateStatus.value = response.isSuccessful
                    }

                    override fun onFailure(call: Call<Any>, t: Throwable) {
                        _updateStatus.value = false
                    }
                })
//        }
        }
    }

    private fun prepareImagePart(imageFile: File): MultipartBody.Part {
        val requestFile = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("profile_picture", imageFile.name, requestFile)
    }

    fun fetchUserInfo(token: String) {
        viewModelScope.launch {
            val response = userDataRepository.getUserInfo(token)
            if (response.isSuccessful) {
                val userInfo = response.body()
                _userInfo.postValue(userInfo)
            } else {
            }
        }
    }
}