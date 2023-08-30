package com.example.threads.data.models

class UserOwnInfo(
    val id: Int,
    val email: String,
    val username: String,
    val name: String?,
    val profile_picture: String?,
    val bio: String?,
    val link: String?,
    val is_private: Boolean,
    val following: List<String>,
    val followers: List<String>
)