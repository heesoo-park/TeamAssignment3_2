UserDatabase
=
# 구조
object class로 UserDatabase를 구현했다.
더미 데이터, 사용자 정보 저장소, 함수들로 구성되어있다.

## 더미 데이터
```kotlin
    private val user1 = User(
        "사용자1",
        "test1",
        "a",
        (R.drawable.img_cat1),
        "오늘 하루는 피곤하네요",
        arrayListOf(Post(userProfileImage = (R.drawable.img_cat1),postImage = (R.drawable.img_cat1), postContent = "안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요", comment = "아주 즐거워요", like =  0))
    )
```
더미 데이터는 User 데이터 클래스에 맞춰 직접 입력하여 추가했다.
각각 사용자에게 게시물을 하나씩 넣어놨다.
총 4명의 사용자를 미리 추가해놨다.

## 사용자 정보 저장소
```kotlin
    var totalUserData: ArrayList<User> = arrayListOf(user1, user2, user3, user4)
```
저장소는 User 타입을 받는 ArrayList 형태로 만들었다.

## 함수
### addUser
```kotlin
    fun addUser(user: User) {
        totalUserData.add(user)
    }
```
회원가입한 사용자를 사용자 정보 저장소에 저장하는 함수다.

### getTotalUser
```kotlin
    fun getTotalUser(): ArrayList<User> {
        return totalUserData
    }
```
모든 사용자 정보(사용자 정보 저장소)를 반환하는 함수다.

### getUser
```kotlin
    fun getUser(id: String): User? {
        return totalUserData.find { it.id == id }
    }
```
아이디를 가지고 해당 사용자의 정보를 반환하는 함수다.

### editUserData
```kotlin
    fun editUserData(user: User) {
        getUser(user.id)?.let {
            it.name = user.name
            it.statusMessage = user.statusMessage
            it.password = user.password
        }
    }
```
아이디를 가지고 해당 사용자의 정보를 수정하는 함수다.