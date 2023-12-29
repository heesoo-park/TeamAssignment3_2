package com.example.teamsns

data class User(
    var name: String,
    val id: String,
    var password: String,
    var profileImage: Int = R.drawable.img_cat1,
    var statusMessage: String = "",
    var userPosts: ArrayList<Post> = arrayListOf()
)
