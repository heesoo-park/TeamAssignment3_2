package com.example.teamsns

data class Post(
    val userProfileImage: Int,
    var postImage: Int,
    val comment: String?,
    var like: Int = 0,
    var postContent: String? = null,
    var commentIcon: Int = 0,
    var likeSelectedUser: ArrayList<String>? = null
)
