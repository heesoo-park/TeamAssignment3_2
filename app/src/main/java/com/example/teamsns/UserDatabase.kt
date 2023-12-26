package com.example.teamsns

object UserDatabase {
    var totalUserData: ArrayList<User> = arrayListOf()

    fun addUser(user: User) {
        totalUserData.add(user)
    }
}