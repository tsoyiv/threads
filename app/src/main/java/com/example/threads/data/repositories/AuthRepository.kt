package com.example.threads.data.repositories

import com.example.threads.data.api.AuthAPI
import com.example.threads.data.models.*
import retrofit2.Call
import retrofit2.Response

class AuthRepository(private val authAPI: AuthAPI) {

    suspend fun registerUser(user: CustomUser): Call<Unit> {
        return authAPI.registerUser(user)
    }

    suspend fun loginUser(request: UserLogin): Call<UserLoginResponse> {
        return authAPI.loginUser(request)
    }

    suspend fun sendForgotPasswordEmail(email: String): Call<Unit> {
        val request = EmailForgotPassword(email)
        return authAPI.emailForgotPassword(request)
    }

    suspend fun otpVerify(code: String): Call<Unit> {
        val request = OtpVerification(code)
        return authAPI.otpVerification(request)
    }

    suspend fun createNewPassword(request: NewPasswordRequest): Call<Unit> {
        return authAPI.createNewPassword(request)
    }

    fun loginFromGoogle(request: GoogleSignInRequest): Call<UserLoginResponse> {
        return authAPI.signInWithGoogle(request)
    }
}