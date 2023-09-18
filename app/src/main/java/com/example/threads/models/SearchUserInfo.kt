package com.example.threads.models

data class SearchUserInfo(
    val id: Int,
    val username: String,
    val profile_picture: String?,
    val bio: String,
    val numbOfFollowers: Int
)
