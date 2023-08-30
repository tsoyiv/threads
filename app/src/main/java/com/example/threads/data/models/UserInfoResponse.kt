package com.example.threads.data.models

data class UserInfoResponse(
    val id: Int,
    val email: String,
    val username: String,
    val name: String,
    val profile_picture: String,
    val bio: String,
    val link: String,
    val is_private: Boolean
)
