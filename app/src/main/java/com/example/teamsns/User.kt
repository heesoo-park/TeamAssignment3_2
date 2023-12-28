package com.example.teamsns

data class User(
    var name: String,
    val id: String,
    var password: String,
    var profileImage: Int = (R.drawable.img_cat1), // 프로필 이미지 선택 화면으로 넘아가기 전에 프로필 이미지 수정
    var statusMessage: String?,
    var userPosts: ArrayList<Post> = arrayListOf()
)
