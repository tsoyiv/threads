package com.example.threads.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.threads.data.models.CustomUser
import com.example.threads.data.models.NewPasswordRequest
import com.example.threads.data.models.UserLogin
import com.example.threads.data.models.UserLoginResponse
import com.example.threads.data.repositories.AuthRepository
import com.example.threads.utils.Holder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

public class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _registrationStatus = MutableLiveData<Boolean>()
    val registrationStatus: LiveData<Boolean> = _registrationStatus

    private val _loginStatus = MutableLiveData<Boolean>()
    val loginStatus: LiveData<Boolean> = _loginStatus

    private val _forgotPasswordStatus = MutableLiveData<Boolean>()
    val forgotPasswordStatus: LiveData<Boolean> = _forgotPasswordStatus

    private val _otpStatus = MutableLiveData<Boolean>()
    val otpStatus: LiveData<Boolean> = _otpStatus

    private val _newPasswordStatus = MutableLiveData<Boolean>()
    val newPasswordStatus: LiveData<Boolean> = _newPasswordStatus

    suspend fun registerUser(user: CustomUser) {
        authRepository.registerUser(user).enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                _registrationStatus.postValue(response.isSuccessful)
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                _registrationStatus.postValue(false)
            }
        })
    }

    suspend fun loginUser(request: UserLogin) {
        authRepository.loginUser(request).enqueue(object : Callback<UserLoginResponse> {
            override fun onResponse(
                call: Call<UserLoginResponse>,
                response: Response<UserLoginResponse>
            ) {
//                val token = response.body()?.access
//                preferenceManager.authToken = token
                val loginResponse = response.body()
                val accessToken = loginResponse?.access
                val refreshToken = loginResponse?.refresh
                if (refreshToken != null && accessToken != null) {
                    Holder.token = accessToken
                }
                _loginStatus.postValue(response.isSuccessful)
            }

            override fun onFailure(call: Call<UserLoginResponse>, t: Throwable) {
                _loginStatus.postValue(false)
            }
        })
    }

    suspend fun sendForgotPasswordEmail(email: String) {
        authRepository.sendForgotPasswordEmail(email).enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                _forgotPasswordStatus.postValue(response.isSuccessful)
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                _forgotPasswordStatus.postValue(false)
            }
        })
    }

    suspend fun otpCodeRequest(code: String) {
        authRepository.otpVerify(code).enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                _otpStatus.postValue(response.isSuccessful)
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                _otpStatus.postValue(false)
            }
        })
    }

    suspend fun createNewPassword(code: String, password: String, confirmPassword: String) {
        val otp = Holder.otpCode
        val request = NewPasswordRequest(otp, password, confirmPassword)

        authRepository.createNewPassword(request).enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                _newPasswordStatus.postValue(response.isSuccessful)
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                _newPasswordStatus.postValue(false)
            }
        })
    }
}