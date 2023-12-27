package com.example.teamsns

object UserDatabase {
    private val user1 = User(
        "사용자1",
        "test1",
        "abcdABCD1!",
        (R.drawable.img_cat1),
        "오늘 하루는 피곤하네요",
        arrayListOf(Post(userProfileImage = (R.drawable.img_cat1),postImage = (R.drawable.img_cat1),comment = "아주 즐거워요", like =  0))
    )


    private val user2 = User(
        "사용자2",
        "test2",
        "abcdABCD1!",
        (R.drawable.img_cat2),
        "오늘 하루 힘내세요",
        arrayListOf(Post(userProfileImage = (R.drawable.img_cat2),postImage = (R.drawable.img_cat6),comment = "아주 즐거워요", like =  0))
    )

    private val user3 = User(
        "사용자3",
        "test3",
        "abcdABCD1!",
        (R.drawable.img_cat3),
        "오늘 하루는 행복하네요",
        arrayListOf(Post(userProfileImage = (R.drawable.img_cat3),postImage = (R.drawable.img_dog3),comment = "아주 즐거워요", like =  0))
    )

    private val user4 = User(
        "사용자4",
        "test4",
        "abcdABCD1!",
        (R.drawable.img_cat4),
        "오늘 하루는 행복하네요",
        arrayListOf(Post(userProfileImage = (R.drawable.img_cat4),postImage = (R.drawable.img_dog4),comment = "아주 즐거워요", like =  0))
    )

    var totalUserData: ArrayList<User> = arrayListOf(user1, user2, user3, user4)


    // 로그인한 사용자 저장 용도
    fun addUser(user: User) {
        totalUserData.add(user)
    }

    // 사용자 리스트와 게시물 출력 용도
    fun getTotalUser(): ArrayList<User> {
        return totalUserData
    }

    // 로그인한 사용자 정보 가져오는 용도
    fun getUser(id: String): User? {
        return totalUserData.find { it.id == id }
    }

    // 사용자 정보 수정 용도
    fun editUserData(user: User) {
        totalUserData.forEach {
            it.name = user.name
            it.id = user.id
            it.statusMessage = user.statusMessage
        }
    }

    // 회원가입 아이디 중복체크 용도
    fun checkUserId(id: String): Boolean {
        totalUserData.forEach {
            if (it.id == id) return false
        }
        return true
    }

    // 로그인 아이디, 비밀번호 체크 용도
    fun matchUser(id: String, password: String): Boolean {
        totalUserData.forEach {
            if (it.id == id && it.password == password) return true
        }
        return false
    }

    fun addPost(userId: String, post: Post) {
        val user = getUser(userId)
        if (user != null) {
            user.userPosts?.add(post)
        }
    }

}