package com.example.threads.data.models

import android.net.Uri

class UserOwnInfo(
    val id: Int,
    val email: String,
    val username: String,
    val name: String?,
    val profile_picture: String?,
    val bio: String?,
    val link: String?,
    val is_private: Boolean,
    val following: List<Follower>,
    val followers: List<Follower>
)

data class Follower(
    val id: Int,
    val username: String,
    val name: String?,
    val profile_picture: String?
)