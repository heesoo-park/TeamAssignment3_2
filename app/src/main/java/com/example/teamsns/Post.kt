package com.example.teamsns

import java.util.UUID

data class Post(
    val key: String = UUID.randomUUID().toString(),
    var userProfileImage: Int,
    var postImage: ArrayList<Int>,
    val commentUser: ArrayList<CommentUser>?,
    var like: Int = 0,
    var postContent: String = "",
    var likeSelectedUser: ArrayList<String> = ArrayList()
)
