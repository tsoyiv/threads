package com.example.threads.models

import java.io.Serializable

data class SearchUserInfo(
    val id: Int,
    val email: String,
    val username: String,
    val name: String,
    val profile_picture: String?,
    val bio: String,
    val link: String,
    val numbOfFollowers: Int,
    val is_private: Boolean
): Serializable
