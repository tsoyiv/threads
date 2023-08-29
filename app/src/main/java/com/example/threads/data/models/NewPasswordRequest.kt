package com.example.threads.data.models

data class NewPasswordRequest(
    val otp: String,
    val password: String,
    val confirm_password: String
)