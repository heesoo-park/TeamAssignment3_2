CommentUser
=
# 구조
게시물에 달린 댓글 하나하나의 정보를 저장할 data class이다.

댓글을 단 사용자의 아이디를 저장하는 id,

댓글의 내용을 저장하는 comment,

댓글을 단 사용자의 프로필 이미지를 저장하는 commentIcon으로 구성되어있다.

## 코드
```kotlin
data class CommentUser(
    val id: String,
    val comment: String,
    var commentIcon: Int
)
```
