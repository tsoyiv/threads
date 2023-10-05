package com.example.threads.data.models

data class FollowersResponse(
    val following: List<User>,
    val followers: List<User>
)

data class User(
    val id: Int,
    val username: String,
    val name: String,
    val profile_picture: String?
)
