package com.example.teamsns

data class User(
    val name: String,
    val id: String,
    val password: String,
    val profileImage: Int,
    val statusMessage: String?,
    var userPosts: ArrayList<Post> = arrayListOf()
)
