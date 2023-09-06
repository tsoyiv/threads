package com.example.threads.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.threads.data.models.ProfileUpdateRequest
import com.example.threads.data.models.ThreadRequest
import com.example.threads.data.models.ThreadResponse
import com.example.threads.data.models.UserOwnInfo
import com.example.threads.data.repositories.ThreadRepository
import com.example.threads.utils.Holder
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ThreadViewModel(private val threadRepository: ThreadRepository) : ViewModel() {

    private val _threadActionStatus = MutableLiveData<Boolean>()
    val threadActionStatus: LiveData<Boolean> = _threadActionStatus

    private val _threadInfo = MutableLiveData<List<ThreadResponse>>()
    val threadInfo: LiveData<List<ThreadResponse>> = _threadInfo

    private val _threads = MutableLiveData<List<ThreadResponse>>()
    val threads: LiveData<List<ThreadResponse>> get() = _threads


    fun createThread(token: String, content: ThreadRequest) {
        threadRepository.createThread(token, content).enqueue(object : Callback<ThreadResponse> {
            override fun onResponse(call: Call<ThreadResponse>, response: Response<ThreadResponse>) {
                if (response.isSuccessful) {
                    _threadActionStatus.postValue(response.isSuccessful)
                }
            }
            override fun onFailure(call: Call<ThreadResponse>, t: Throwable) {
                _threadActionStatus.postValue(false)
            }
        })
    }

    fun getThread() {
        val token = Holder.token
        val authHolder = "Bearer $token"
        threadRepository.getThread(authHolder).enqueue(object : Callback<List<ThreadResponse>> {
            override fun onResponse(
                call: Call<List<ThreadResponse>>,
                response: Response<List<ThreadResponse>>
            ) {
                if (response.isSuccessful) {
                    val threadList = response.body() ?: emptyList()
                    _threads.postValue(threadList)
                } else {
                }
            }

            override fun onFailure(call: Call<List<ThreadResponse>>, t: Throwable) {
            }
        })
    }

//    fun getThread(token: String) {
//        viewModelScope.launch {
//            val response = threadRepository.getThread(token)
//            if (response.isSuccessful) {
//                _threadActionStatus.postValue(true)
//            } else {
//            }
//        }
//    }
}