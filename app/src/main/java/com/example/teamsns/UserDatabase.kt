package com.example.teamsns

object UserDatabase {
    // 더미 데이터
    private val user1 = User(
        "사용자1",
        "test1",
        "a",
        (R.drawable.img_cat1),
        "오늘 하루는 피곤하네요",
        arrayListOf(Post(userProfileImage = (R.drawable.img_cat1),postImage = arrayListOf(R.drawable.img_cat1,R.drawable.img_cat2,R.drawable.img_cat3,R.drawable.img_cat4), postContent = "안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요", commentUser = arrayListOf(CommentUser("사용자3","아주 즐거워요",(R.drawable.img_cat2))), like =  0)
        ,
            Post(userProfileImage = (R.drawable.img_cat1),postImage = arrayListOf(R.drawable.img_toon,R.drawable.img_toon2,R.drawable.img_toon3,R.drawable.img_toon4,R.drawable.img_toon5), postContent = "하.. 옛날 생각나네", commentUser = arrayListOf(CommentUser("사용자3","너무 슬프다",(R.drawable.img_cat2))), like =  0)))
    private val user2 = User(
        "사용자2",
        "test2",
        "abcdABCD1!",
        (R.drawable.img_cat2),
        "오늘 하루 힘내세요",
        arrayListOf(Post(userProfileImage = (R.drawable.img_cat2), postContent = "안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요",postImage = arrayListOf(R.drawable.img_cat6), commentUser = arrayListOf(CommentUser("사용자3","아주 즐거워요",(R.drawable.img_cat2))), like =  0))
    )
    private val user3 = User(
        "사용자3",
        "test3",
        "abcdABCD1!",
        (R.drawable.img_cat3),
        "오늘 하루는 행복하네요",
        arrayListOf(Post(userProfileImage = (R.drawable.img_cat3), postContent = "안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요",postImage = arrayListOf(R.drawable.img_dog3),commentUser = arrayListOf(CommentUser("사용자3","아주 즐거워요",(R.drawable.img_cat2))), like =  0))
    )
    private val user4 = User(
        "사용자4",
        "test4",
        "abcdABCD1!",
        (R.drawable.img_cat4),
        "오늘 하루는 행복하네요",
        arrayListOf(Post(userProfileImage = (R.drawable.img_cat4), postContent = "안녕하세요",postImage = arrayListOf(R.drawable.img_dog4),commentUser = arrayListOf(CommentUser("사용자3","아주 즐거워요",(R.drawable.img_cat2))), like =  0))
    )

    var totalUserData: ArrayList<User> = arrayListOf(user1, user2, user3, user4)

    // 회원가입한 사용자를 저장하는 함수
    fun addUser(user: User) {
        totalUserData.add(user)
    }

    // 모든 사용자 정보를 가져오는 함수
    fun getTotalUser(): ArrayList<User> {
        return totalUserData
    }

    // 아이디를 가지고 해당 사용자 정보 가져오는 함수
    fun getUser(id: String): User? {
        return totalUserData.find { it.id == id }
    }

    // 사용자 정보를 수정하는 함수
    fun editUserData(user: User) {
        getUser(user.id)?.let {
            it.name = user.name
            it.statusMessage = user.statusMessage
            it.password = user.password
        }
    }
}