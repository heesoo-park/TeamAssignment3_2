package com.example.teamsns

data class Post(
    val userId: String,
    val userProfileImage: Int,
    val comment: String?,
    val like: Int = 0,
)
