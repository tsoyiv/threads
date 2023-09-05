package com.example.threads.data.models

data class ProfileUpdateRequest(
    val username: String,
    val name: String,
    val bio: String,
    val link: String,
    val is_private: Boolean
)
