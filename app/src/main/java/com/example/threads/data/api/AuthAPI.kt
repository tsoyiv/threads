package com.example.threads.data.api

import com.example.threads.data.models.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthAPI {
    @POST("v1/register/")
    fun registerUser(@Body user: CustomUser): Call<Unit>

    @POST("v1/login/")
    fun loginUser(@Body request: UserLogin): Call<UserLoginResponse>

    @POST("v1/forgot-password/")
    fun emailForgotPassword(@Body request: EmailForgotPassword): Call<Unit>

    @POST("v1/verify-otp/")
    fun otpVerification(@Body request: OtpVerification): Call<Unit>

    @POST("v1/reset-password/")
    fun createNewPassword(@Body request: NewPasswordRequest): Call<Unit>
}