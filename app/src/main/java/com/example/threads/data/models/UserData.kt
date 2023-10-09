package com.example.threads.data.models

data class UserData(
    val following: List<UserInfo>,
    val followers: List<UserInfo>
)

data class UserInfo(
    val id: Int,
    val username: String,
    val name: String?,
    val profile_picture: String?
)