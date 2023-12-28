package com.example.teamsns

data class Post(
    val userProfileImage: Int,
    var postImage: ArrayList<Int>,
    val commentUser: ArrayList<CommentUser>?,
    var like: Int = 0,
    var postContent: String? = null,
    var likeSelectedUser: ArrayList<String> = ArrayList()
)
