package com.example.teamsns

import java.util.UUID

data class Post(
    val id: String = UUID.randomUUID().toString(),
    val userProfileImage: Int,
    var postImage: Int,
    val comment: String?,
    var like: Int = 0,
    var postContent: String? = null,
    var commentIcon: Int = 0,
    var likeSelectedUser: ArrayList<String> = ArrayList()
)