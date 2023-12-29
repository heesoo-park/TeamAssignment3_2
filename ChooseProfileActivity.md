ChooseProfileActivity
=
# 레이아웃 구성

![image](https://github.com/heesoo-park/TeamAssignment3_2/assets/80674868/f310b895-80bd-46fb-b1a7-c14260fc7f1a)

ConstraintLayout를 기본으로 그 안에

상단 : 안내문구

중앙 : 프로필 이미지 샘플들

하단 : 선택한 프로필 이미지 미리보기 & 회원가입 버튼

## 프로필 이미지 샘플
각각의 프로필 이미지 샘플들은 ImageButton을 사용했다.

여기서 버튼의 클릭이 되었다는 걸 표시해주기 위해 selector를 사용했다.

버튼이 눌렸을 때 외곽선이 하얀색으로 3dp 두께로 나왔다가 사라진다.

이 효과가 시각적으로 보이기 위해 ImageButton에 padding을 주었다.

이걸 주지 않으면 외곽선은 뷰 객체의 크기 안쪽으로 늘어나면서 생기기 때문에 보이지 않았다. 
```xml
<selector xmlns:android="http://schemas.android.com/apk/res/android">
    <item android:state_pressed="true">
        <shape>
            <stroke
                android:width="3dp"
                android:color="@color/white"/>
        </shape>
    </item>
</selector>
```

## 선택된 이미지 미리보기
선택된 이미지 미리보기는 세로모드에서는 샘플들 아래, 가로모드에서는 샘플들 우측에 위치한다.

샘플들보다는 확실하게 큰 사이즈로 설정하여 미리보기임을 인식시킨다.

![image](https://github.com/heesoo-park/TeamAssignment3_2/assets/80674868/562b89b1-5387-4629-a486-c8d9d500e3ff)

## 회원가입 버튼
이 페이지 자체가 회원가입 페이지를 지나서 오는 페이지이기 때문에 회원가입이 완료되는 버튼이다.

물론 편집 페이지를 통해서 오게 된다면 수정하기 버튼으로 텍스트가 변경된다.

![image](https://github.com/heesoo-park/TeamAssignment3_2/assets/80674868/137c5ed7-f582-466e-adf4-8a2f11800fbf)

# 코드 설명
사용한 변수나 함수에 대한 설명이다.

## 프로필 이미지 샘플 뷰 아이디 리스트
```kotlin
private val profileSampleIds = listOf(
    R.id.ib_choose_profile_sample1,
    R.id.ib_choose_profile_sample2,
    R.id.ib_choose_profile_sample3,
    R.id.ib_choose_profile_sample4,
    R.id.ib_choose_profile_sample5,
    R.id.ib_choose_profile_sample6,
    R.id.ib_choose_profile_sample7,
    R.id.ib_choose_profile_sample8,
    R.id.ib_choose_profile_sample9,
    R.id.ib_choose_profile_sample10,
    R.id.ib_choose_profile_sample11,
    R.id.ib_choose_profile_sample12,
    )
```
처리하기 쉽게 리스트 형태로 ImageButton의 id를 저장했다.

## 선택된 이미지 미리보기
```kotlin
private val ivSelectedProfile: ImageView by lazy {
    findViewById(R.id.iv_choose_selected_profile)
}
```
미리보기 ImageView는 따로 by lazy로 선언했다.

## 선택된 이미지 정보
```kotlin
private var selectedImageIdx: Int = 0
```
선택된 이미지 정보는 Int 타입으로 받아오게 했다.

## 프로필 이미지 샘플 뷰 리스트
```kotlin
private val profileSampleButtonList: List<ImageButton> by lazy {
    profileSampleIds.map { findViewById(it) }
}
```
위에서 만들어 놓은 profileSampleIds를 가지고 map 함수를 이용해 간결하게 초기화했다.

## 프로필 이미지 샘플 리소스 리스트
```kotlin
private val profileImageList = listOf(
    R.drawable.img_dog1,
    R.drawable.img_dog2,
    R.drawable.img_dog3,
    R.drawable.img_dog4,
    R.drawable.img_dog5,
    R.drawable.img_dog6,
    R.drawable.img_cat1,
    R.drawable.img_cat2,
    R.drawable.img_cat3,
    R.drawable.img_cat4,
    R.drawable.img_cat5,
    R.drawable.img_cat6,
    )
```
selectedImageIdx를 가지고 선택된 이미지 리소스를 사용하기 위해 리스트 형태로 만들었다.

## 이외의 변수
```kotlin
private lateinit var name: String
private lateinit var id: String 
private lateinit var password: String
private val statusMessage: String = ""
private val userPosts: ArrayList<Post> = arrayListOf()

private lateinit var editId: String
private lateinit var userData: User
```
회원가입에 필요한 데이터들과 편집페이지로 사용될 때 필요한 데이터들을 변수로 선언했다.

## 인텐트에서 넘어온 값 받기
```kotlin
name = intent.getStringExtra("name") ?: ""
id = intent.getStringExtra("id") ?: ""
password = intent.getStringExtra("password") ?: ""
```
가장 먼저 onCreate에서 인텐트로 넘어온 값들을 받는다.

## setEditCheck
```kotlin
private fun setEditCheck() {
    if (intent.getStringExtra("editId") != null) {
        editId = intent.getStringExtra("editId")!!
        userData = UserDatabase.getUser(editId)!!
        btnSignUp.setText(R.string.edit_do)
        ivSelectedProfile.setImageResource(userData.profileImage) 
    }
}
```
ChooseProfileActivity가 편집 페이지로부터 왔는지 확인하는 함수다.
(편집 페이지는 회원가입 페이지를 재활용하여 만들었기 때문에 따로 구별할 필요가 있다.)

편집 페이지로부터 왔다면 수정할 사용자의 아이디를 받고 사용자 정보를 가져온다.

또한 회원가입이라 써있는 버튼의 텍스트를 수정하기로 변경하고 미리보기도 사용자의 프로필 이미지를 띄워준다.
(기본적으로는 제일 첫번째 리소스가 들어가있다.)

## setOnClickListener
```kotlin
private fun setOnClickListener() {
    profileSampleButtonList.forEachIndexed { idx, ib ->
        ib.setOnClickListener {
            selectedImageIdx = idx
            ivSelectedProfile.setImageResource(profileImageList[idx])
        }
    }
    
    btnSignUp.setOnClickListener {
        if (intent.getStringExtra("editId") != null) {
            UserDatabase.totalUserData.find { it.id == editId }
                .let { it!!.profileImage = profileImageList[selectedImageIdx] }
        }else {
            UserDatabase.addUser(
                User(
                    name,
                    id,
                    password,
                    profileImageList[selectedImageIdx],
                    statusMessage,
                    userPosts
                )
            )
        }
        setResult(RESULT_OK, intent)
        finish()
        overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left)
    }
}
```
클릭 리스너를 모아둔 함수다.

먼저는 profileSampleButtonList를 반복문으로 돌면서 setOnClickListener를 설정한다.

프로필 이미지 샘플이 클릭되면 해당 인덱스를 selectedImageIdx에 저장하고 미리보기도 클릭한 프로필 이미지 샘플로 변경한다.

그 다음으로는 btnSignUp에 setOnClickListener를 설정한다.
```kotlin
if (intent.getStringExtra("editId") != null) {
    UserDatabase.totalUserData.find { it.id == editId }
        .let { it!!.profileImage = profileImageList[selectedImageIdx] }
}
```
편집 페이지로부터 왔다면 현재 수정할 사용자 정보를 가져와 프로필 이미지만 변경한다.

<br/>

```kotlin
else {
    UserDatabase.addUser(
        User(
            name,
            id,
            password,
            profileImageList[selectedImageIdx],
            statusMessage,
            userPosts
        )
    )
}
```
회원가입 페이지로부터 왔다면 회원가입 페이지로부터 받은 회원가입 정보들과 합쳐서 UserDatabase에 추가한다.

<br/>

```kotlin
setResult(RESULT_OK, intent)
finish()
```
잘 완료되었음을 알려주기 위해 setResult 안에 RESULT_OK를 넣어서 이전 페이지로 보내주고 액티비티를 종료한다.

