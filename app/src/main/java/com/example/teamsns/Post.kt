package com.example.teamsns

data class Post(
    val userProfileImage: Int,
    var postImage: Int,
    val comment: String?,
    var like: Int = 0,
)
