Post
=
# 구조
게시물 하나하나의 정보를 저장할 data class이다.

사용자들이 올린 게시물들을 구별하는 데 사용하는 key,

게시물을 작성한 사용자의 프로필 이미지를 저장하는 userProfileImage,

게시물의 이미지를 저장하는 postImage,

게시물에 댓글을 단 사용자를 저장하는 commentUser,

게시물의 좋아요 수를 저장하는 like,

게시물의 게시글을 저장하는 postContent,

게시물에 좋아요를 누른 사용자들을 저장하는 likeSelectedUser로 구성되어있다.

## 코드
```kotlin
data class Post(
    val key: String = UUID.randomUUID().toString(),
    val userProfileImage: Int,
    var postImage: ArrayList<Int>,
    val commentUser: ArrayList<CommentUser>?,
    var like: Int = 0,
    var postContent: String = "",
    var likeSelectedUser: ArrayList<String> = ArrayList()
)
```

## 추가설명
### key
key에서 사용한 UUID 클래스는 유일한 식별자를 생성하는데 사용된다.

그래서 각각 게시물들을 유일하게 식별할 수 있기에 한 사용자가 여러 게시물을 올렸을 때 구별이 가능하다.

### commentUser
commentUser는 게시물에 댓글을 단 사용자를 저장한다.

그렇기 때문에 여러 댓글이 추가될 수 있도록 ArrayList를 사용했고 그 안에 필요한 내용이 담길 수 있도록 
CommentUser라는 데이터 클래스를 만들어 사용했다.

게시물을 처음 올려진 상태에서는 댓글이 없을 것이기 때문에 nullable하게 타입을 설정했다.

### like
like의 기본값이 0인 이유는 좋아요가 0부터 시작하기 때문이다.