package com.example.threads.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.threads.data.models.ProfileUpdateRequest
import com.example.threads.data.models.ThreadRequest
import com.example.threads.data.models.ThreadResponse
import com.example.threads.data.repositories.ThreadRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ThreadViewModel(private val threadRepository: ThreadRepository) : ViewModel() {

    private val _threadActionStatus = MutableLiveData<Boolean>()
    val threadActionStatus: LiveData<Boolean> = _threadActionStatus

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
}