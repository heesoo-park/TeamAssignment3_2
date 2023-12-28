Post
=
# 구조
게시물 하나하나의 정보를 저장할 data class이다.

게시물을 작성한 사용자의 프로필 이미지를 저장하는 userProfileImage,

게시물의 이미지를 저장하는 postImage,

게시물의 댓글을 저장하는 comment,

게시물의 좋아요 수를 저장하는 like,

게시물의 게시글을 저장하는 postContent,

게시물에 댓글을 단 사용자의 프로필 이미지를 저장하는 commentIcon,

게시물에 좋아요를 누른 사용자들을 저장하는 likeSelectedUser로 구성되어있다.

## 코드
```kotlin
    data class Post(
        val userProfileImage: Int,
        var postImage: Int,
        val comment: String?,
        var like: Int = 0,
        var postContent: String? = null,
        var commentIcon: Int? = null,
        var likeSelectedUser: ArrayList<String> = ArrayList()
    )
```

## 추가설명
comment가 String? 타입인 이유는 null일 때는 아무것도 나오지 않아야하기 때문이다.

commentIcon이 Int? 타입인 이유도 위와 동일하다.

like의 기본값이 0인 이유는 좋아요가 0부터 시작하기 때문이다.