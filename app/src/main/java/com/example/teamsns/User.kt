package com.example.teamsns

data class User(
    var name: String,
    var id: String,
    val password: String,
    val profileImage: Int?, // 프로필 이미지 선택 화면으로 넘아가기 전에 프로필 이미지 수정
    var statusMessage: String?,
    var userPosts: ArrayList<Post> = arrayListOf()
)
