package com.example.threads.view_models

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.airbnb.lottie.utils.Utils
import com.example.threads.data.models.CustomUser
import com.example.threads.data.models.ProfileAvatarResponse
import com.example.threads.data.models.ProfileUpdateRequest
import com.example.threads.data.models.SubscribeRequest
import com.example.threads.data.models.SubscribeResponse
import com.example.threads.data.models.UserData
import com.example.threads.data.models.UserOwnInfo
import com.example.threads.data.repositories.UserDataRepository
import com.example.threads.models.SearchUserInfo
import com.example.threads.models.UserFollower
import com.example.threads.models.UserRepresentation
import com.example.threads.utils.Holder
import com.example.threads.utils.ImageConverter
import com.example.threads.utils.RetrofitInstance
import com.example.threads.utils.RetrofitInstance.Companion.userDataApi
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

    private val _imageAddedSuccess = MutableLiveData<Boolean>()
    val imageAddedSuccess: LiveData<Boolean> = _imageAddedSuccess

    private val _searchResults = MutableLiveData<List<SearchUserInfo>>()
    val searchResults: LiveData<List<SearchUserInfo>> = _searchResults

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _userFollowers = MutableLiveData<UserData>()
    val userFollowers: LiveData<UserData> = _userFollowers

    private val _uploadResult = MutableLiveData<String?>()
    val uploadResult: LiveData<String?> = _uploadResult

    private val _followResult = MutableLiveData<SubscribeResponse>()
    val followResult: LiveData<SubscribeResponse> = _followResult

    fun followUser(token: String, request: SubscribeRequest) {
        userDataRepository.followUser(token, request).enqueue(object : Callback<SubscribeResponse> {
            override fun onResponse(call: Call<SubscribeResponse>, response: Response<SubscribeResponse>) {
                if (response.isSuccessful) {
                    _followResult.postValue(response.body())
                } else {
                }
            }

            override fun onFailure(call: Call<SubscribeResponse>, t: Throwable) {
                val errorMessage = "Network Error: ${t.message}"
                _error.postValue(errorMessage)
            }
        })
    }

    fun uploadProfilePicture(imageUri: Uri) {
        val file = File(imageUri.path)
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("profile_picture", file.name, requestFile)

        userDataApi.uploadProfilePicture(body).enqueue(object : Callback<ProfileAvatarResponse> {
            override fun onResponse(call: Call<ProfileAvatarResponse>, response: Response<ProfileAvatarResponse>) {
                if (response.isSuccessful) {
                    // Image upload successful, get the URL
                    val profilePictureUrl = response.body()?.profile_picture
                    _uploadResult.postValue(profilePictureUrl)
                } else {
                    // Image upload failed
                    _uploadResult.postValue(null)
                }
            }

            override fun onFailure(call: Call<ProfileAvatarResponse>, t: Throwable) {
                // Image upload failed
                _uploadResult.postValue(null)
            }
        })
    }


    fun fetchUserFollowers(token: String, username: String) {
        userDataRepository.getUserFollowers(token, username).enqueue(object : Callback<UserData> {
            override fun onResponse(call: Call<UserData>, response: Response<UserData>) {
                if (response.isSuccessful) {
                    _userFollowers.postValue(response.body())
                } else {
                    val errorMessage = "API Error: ${response.code()}"
                    _error.postValue(errorMessage)
                }
            }

            override fun onFailure(call: Call<UserData>, t: Throwable) {
                Log.d("errror", "fetch users ${t.message}")
//                val errorMessage = "Network Error: ${t.message}"
//                _error.postValue(errorMessage)
            }
        })
    }
//    fun fetchUserFollowers(token: String, username: String) {
//        userDataRepository.getUserFollowers(token, username)
//            .enqueue(object : Callback<List<UserFollower>> {
//                override fun onResponse(
//                    call: Call<List<UserFollower>>,
//                    response: Response<List<UserFollower>>
//                ) {
//                    if (response.isSuccessful) {
//                        val results = response.body()
//                        _userFollowers.postValue(results)
//                    } else {
//                        val errorMessage = "API Error: ${response.code()}"
//                        _error.postValue(errorMessage)
//                    }
//                }
//
//                override fun onFailure(call: Call<List<UserFollower>>, t: Throwable) {
//                    val errorMessage = "Network Error: ${t.message}"
//                    _error.postValue(errorMessage)
//                }
//            })
//    }

    fun searchUsers(token: String, query: String) {
        userDataRepository.searchUsers(token, query)
            .enqueue(object : Callback<List<SearchUserInfo>> {
                override fun onResponse(
                    call: Call<List<SearchUserInfo>>,
                    response: Response<List<SearchUserInfo>>
                ) {
                    if (response.isSuccessful) {
                        val results = response.body() ?: emptyList()
                        _searchResults.postValue(results)
                    } else {
                        val errorMessage = "API Error: ${response.code()}"
                        _error.postValue(errorMessage)
                    }
                }

                override fun onFailure(call: Call<List<SearchUserInfo>>, t: Throwable) {
                    val errorMessage = "Network Error: ${t.message}"
                    _error.postValue(errorMessage)
                }
            })
    }


//    fun uploadProfilePicture(imageFile: File) {
//        userDataRepository.uploadProfilePicture(imageFile, object : Callback<Unit> {
//            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
//                if (response.isSuccessful) {
//                    _imageAddedSuccess.postValue(response.isSuccessful)
//                } else {
//                    _imageAddedSuccess.postValue(false)
//                }
//            }
//            override fun onFailure(call: Call<Unit>, t: Throwable) {
//                _imageAddedSuccess.postValue(false)
//            }
//        })
//    }

    fun editPhoto(
        context: Context,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        val photo = Holder.selectedImageUri
        val apiInterface = RetrofitInstance.userDataApi

        val file: File? = photo?.let { ImageConverter.getFile(context, it) }
        val requestBody = file?.asRequestBody("image/png".toMediaTypeOrNull())
        val imagePart = requestBody?.let {
            MultipartBody.Part.createFormData("photo", file.name, it)
        }

        imagePart?.let {
            apiInterface.uploadProfilePicture(
                it
            )
        }?.enqueue(object : Callback<ProfileAvatarResponse> {
            override fun onResponse(
                call: Call<ProfileAvatarResponse>,
                response: Response<ProfileAvatarResponse>
            ) {
                if (response.isSuccessful) {
                    onSuccess.invoke()
                } else {
                    onError.invoke()
                }
            }

            override fun onFailure(call: Call<ProfileAvatarResponse>, t: Throwable) {
                onError.invoke()
            }
        })
    }

//    fun uploadProfilePicture(imageData: ByteArray) {
//        userDataRepository.uploadProfilePicture(imageData, object : Callback<Unit> {
//            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
//                if (response.isSuccessful) {
//                    // Handle success, you can update a LiveData or perform any necessary actions
//                    _imageAddedSuccess.postValue(response.isSuccessful)
//                } else {
//                    // Handle failure, you can update a LiveData or perform error handling
//                    _imageAddedSuccess.postValue(false)
//                }
//            }
//
//            override fun onFailure(call: Call<Unit>, t: Throwable) {
//                // Handle failure, you can update a LiveData or perform error handling
//                _imageAddedSuccess.postValue(false)
//            }
//        })
//    }

    fun updateUser(token: String, user: ProfileUpdateRequest) {
        userDataRepository.updateProfile(token, user).enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                _updateStatus.postValue(response.isSuccessful)
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                _updateStatus.postValue(false)
            }
        })
    }

//    fun inputItem(
//        context: Context,
//        imageUri: Uri
//    ) {
//        val apiInterface = RetrofitInstance.userDataApi
//
//        val imageFile = ImageConverter.getFile(context, imageUri)
//        if (imageFile != null) {
//            val imagePart = prepareImagePart(imageFile)
//
//            apiInterface.uploadProfilePicture(
//                profilePicture = imagePart
//            ).enqueue(object : Callback<Void> {
//                override fun onResponse(
//                    call: Call<Void>,
//                    response: Response<Void>
//                ) {
//                    _imageAddedSuccess.value = response.isSuccessful
//                }
//
//                override fun onFailure(call: Call<Void>, t: Throwable) {
//                    _imageAddedSuccess.value = false
//                }
//            })
//        }
//    }

//    private fun prepareImagePart(imageFile: File): MultipartBody.Part {
//        val requestFile = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
//        return MultipartBody.Part.createFormData("photo", imageFile.name, requestFile)
//    }
//    fun updateUserInfo(
//        context: Context,
//        token: String,
//        username: String,
//        name: String,
//        profile_picture: Uri,
//        bio: String,
//        link: String,
//        isPrivate: Boolean
//    ) {
//        val imageFile = ImageConverter.getFile(context, profile_picture)
//        if (imageFile != null) {
//            val imagePart = prepareImagePart(imageFile)
//            val imageParts = imagePart
//
//            userDataRepository.updateUserInfo(
//                token,
//                username,
//                imageParts,
//                name,
//                bio,
//                link,
//                isPrivate,
//                object :
//                    Callback<Any> {
//                    override fun onResponse(call: Call<Any>, response: Response<Any>) {
//                        _updateStatus.value = response.isSuccessful
//                    }
//
//                    override fun onFailure(call: Call<Any>, t: Throwable) {
//                        _updateStatus.value = false
//                    }
//                })
////        }
//        }
//    }


//    private fun prepareImagePart(imageFile: File): MultipartBody.Part {
//        val requestFile = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
//        return MultipartBody.Part.createFormData("photo", imageFile.name, requestFile)
//    }

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