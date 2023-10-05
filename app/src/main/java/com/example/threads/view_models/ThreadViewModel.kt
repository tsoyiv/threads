package com.example.threads.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.threads.data.models.CommentRequest
import com.example.threads.data.models.CommentResponse
import com.example.threads.data.models.ProfileUpdateRequest
import com.example.threads.data.models.ThreadRequest
import com.example.threads.data.models.ThreadResponse
import com.example.threads.data.models.ThreadWithCommentsResponse
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

    private val _threads1 = MutableLiveData<List<CommentResponse>>()
    val threads1: MutableLiveData<List<CommentResponse>> get() = _threads1

    private val _threadDetails: MutableLiveData<ThreadResponse> = MutableLiveData()
    val threadDetails: LiveData<ThreadResponse> = _threadDetails

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _commentResult: MutableLiveData<Boolean> = MutableLiveData()
    val commentResult: LiveData<Boolean> = _commentResult

    private val _threadsWithComments = MutableLiveData<List<ThreadWithCommentsResponse>>()
    val threadsWithComments: LiveData<List<ThreadWithCommentsResponse>> = _threadsWithComments

    private val _removeThreadResult = MutableLiveData<Boolean>()
    val removeThreadResult: LiveData<Boolean>
        get() = _removeThreadResult

    private val _likeThreadResponse = MutableLiveData<ThreadResponse>()
    val likeThreadResponse: LiveData<ThreadResponse> = _likeThreadResponse

    fun filterThreadsByCommentCount(threads: List<ThreadResponse>): List<ThreadResponse> {
        return threads.filter { it.comments_count.toInt() > 0 }
    }

    fun likeThread(token: String, threadId: Int) {
        threadRepository.likeThread(token, threadId).enqueue(object : Callback<ThreadResponse> {
            override fun onResponse(call: Call<ThreadResponse>, response: Response<ThreadResponse>) {
                if (response.isSuccessful) {
                    _likeThreadResponse.value = response.body()
                } else {
                    _errorMessage.value = "Empty response"
                }
            }
            override fun onFailure(call: Call<ThreadResponse>, t: Throwable) {
                _errorMessage.value = "Empty response"
            }
        })
    }

    fun getUserThread1(token: String, authorEmail: String) {
        threadRepository.getThreadsWithCommentsActivity(token, authorEmail).enqueue(object : Callback<List<CommentResponse>> {
            override fun onResponse(
                call: Call<List<CommentResponse>>,
                response: Response<List<CommentResponse>>
            ) {
                if (response.isSuccessful) {
                    val threadList = response.body() ?: emptyList()
                    _threads1.postValue(threadList)
                } else {
                }
            }
            override fun onFailure(call: Call<List<CommentResponse>>, t: Throwable) {
            }
        })
    }
    fun getUserThread(token: String, authorEmail: String) {
        threadRepository.getThreadUserThread(token, authorEmail).enqueue(object : Callback<List<ThreadResponse>> {
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

//    fun getThreadUserThread(token: String, authorEmail: String) {
//        threadRepository.getThreadUserThread(token, authorEmail)
//            .enqueue(object : Callback<List<ThreadResponse>> {
//                override fun onResponse(call: Call<List<ThreadResponse>>, response: Response<List<ThreadResponse>>) {
//                    if (response.isSuccessful) {
//                        _threadResponse.postValue(response.body())
//                    } else {
//                        _error.postValue("API Error: ${response.code()}")
//                    }
//                }
//
//                override fun onFailure(call: Call<List<ThreadResponse>>, t: Throwable) {
//                    _error.postValue("Network Error: ${t.message}")
//                }
//            })
//    }

    fun removeThread(token: String, id: Int) {
        threadRepository.removeThreadById(token, id).enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
               if (response.isSuccessful) {
                   _removeThreadResult.value = true
               } else {
                   _removeThreadResult.value = false
               }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                _removeThreadResult.value = false
            }

        })
    }

    fun fetchThreadsWithComments(token: String, threadId: Int) {
        threadRepository.getThreadsWithComments(token, threadId).enqueue(object : Callback<List<ThreadWithCommentsResponse>> {
            override fun onResponse(
                call: Call<List<ThreadWithCommentsResponse>>,
                response: Response<List<ThreadWithCommentsResponse>>
            ) {
                if (response.isSuccessful) {
                    _threadsWithComments.value = response.body()
                } else {
                    _errorMessage.value = "Empty response"
                }
            }

            override fun onFailure(call: Call<List<ThreadWithCommentsResponse>>, t: Throwable) {
                _errorMessage.value = "Network error: ${t.message}"
            }
        })
    }

    fun writeComment(token: String, threadId: Int, content: String) {
        threadRepository.writeComment(token, threadId, content).enqueue(object : Callback<CommentResponse> {
            override fun onResponse(
                call: Call<CommentResponse>,
                response: Response<CommentResponse>
            ) {
                if (response.isSuccessful) {
                    val comment = response.body()
                    _commentResult.postValue(true)
                } else {
                    _commentResult.postValue(false)
                }
            }

            override fun onFailure(call: Call<CommentResponse>, t: Throwable) {
                _errorMessage.value = "Network error: ${t.message}"
            }
        })
    }

    fun getThreadDetails(token: String, threadId: Int) {
        _isLoading.value = true
        val call = threadRepository.getThreadDetails(token, threadId)
        call.enqueue(object : Callback<ThreadResponse> {
            override fun onResponse(call: Call<ThreadResponse>, response: Response<ThreadResponse>) {
                _isLoading.value = false

                if (response.isSuccessful) {
                    val threadResponse = response.body()
                    if (threadResponse != null) {
                        _threadDetails.value = threadResponse
                    } else {
                        _errorMessage.value = "Empty response"
                    }
                } else {
                    _errorMessage.value = "Failed to fetch thread details"
                }
            }

            override fun onFailure(call: Call<ThreadResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = "Network error: ${t.message}"
            }
        })
    }

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
}