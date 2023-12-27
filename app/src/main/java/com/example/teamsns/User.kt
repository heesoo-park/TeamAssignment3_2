package com.example.teamsns

data class User(
    var name: String,
    var id: String,
    val password: String,
    val profileImage: Int,
    var statusMessage: String?,
    var userPosts: ArrayList<Post> = arrayListOf()
)
