User
=
# 구조
한 사용자의 정보를 저장할 data class이다.

사용자의 이름을 저장하는 name,

사용자의 아이디를 저장하는 id,

사용자의 비밀번호를 저장하는 password,

사용자의 프로필 이미지를 저장하는 profileImage,

사용자의 상태 메세지를 저장하는 statusMessage,

사용자의 게시물들을 저장하는 userPosts로 구성되어있다.

## 코드
```kotlin
    data class User(
        var name: String,
        val id: String,
        var password: String,
        var profileImage: Int = R.drawable.img_cat1,
        var statusMessage: String = "",
        var userPosts: ArrayList<Post> = arrayListOf()
    )
```

## 추가 설명
statusMessage가 기본값을 가지고 있는 이유는 회원가입 때 적지 않으니까 ""로 세팅한다.

profileImage가 기본값을 가지고 있는 이유는 편집 페이지에서 프로필 이미지 선택 페이지를 가기전에 한번 사용자 정보를 수정하기 때문이다.
(프로필 이미지 선택 페이지를 가서는 편집 페이지에서 왔는지 확인하고 그에 맞는 코드를 수행한다.)
