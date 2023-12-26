DetailPageActivity
=
## 레이아웃 구성

![image](https://github.com/heesoo-park/TeamAssignment3_2/assets/116724657/f0ad9d78-554f-4a69-b686-904dde3b1baf)

리니어를 기본으로 그 안에

ConstraintLayout 상단

ScrollView{
    Linear{ 이미지
        contraint{ 이미지 , 텍스트뷰}
    }
}
로 구성했다

왼쪽 상단의 <- 이미지를 누르면 홈 액티비티로 넘어갈건데

```xml
    <item android:drawable="@drawable/back_arrow2" android:state_pressed="true"/>
    <item android:drawable="@drawable/back_arrow"/>
```
리소스 파일을 만들어서 눌렀을때 화살표 이미지의 색이 변하도록 만들었다.


프로필 이미지의 둥근 모양은 CardView를 사용했다

```xml
<androidx.cardview.widget.CardView
            android:id="@+id/imageView"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginStart="30dp"
            android:layout_marginTop="30dp"
            app:cardCornerRadius="100dp"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toTopOf="parent">

            <androidx.appcompat.widget.AppCompatImageView
                android:id="@+id/profile_img"
                android:layout_width="100dp"
                android:layout_height="100dp"
                tools:srcCompat="@tools:sample/avatars" />
        </androidx.cardview.widget.CardView>
```
Cardview를 둥글게 만들고 그 안에 이미지를 넣어 만들었다.

# Activity

```kotlin
private val myId: String? by lazy {
        intent.getStringExtra("myId")
    }

    private val id: String? by lazy {
        intent.getStringExtra("id")
    }

private val userDate = UserDatabase.getUser(id!!)
```
프로필을 눌렀을때 ID와 로그인한 본인의 ID를 Extra로 받고

id를 기준으로 유저 데이터를 받는다

```kotlin
    private fun init() {
        setProfile()

        setPersonalButton()

        setBackButton()
    }
```
init은 프로필 세팅, 개인 버튼 세팅, 뒤로가기 세팅으로 구성했다

## setProfile

```kotlin
    private fun setProfile() {
        name = userDate.name
        statusMessage = userDate.statusMessage.toString()

        profileImageView.setImageResource(userDate.profileImage)
        idTextView.setText(id)
        nameTextView.setText(name)
        statusMessageTextView.setText(statusMessage)

        setPostList()
    }
```
유저의 데이터를 기반으로 프로필이미지 아이디 이름 상태 메시지를 출력하고

하단의 PostList를 만든다

### setPostList
```kotlin
    private fun setPostList() {
        for (post in userDate.userPosts.reversed()) {
            val postView: View = layoutInflater.inflate(R.layout.post_item, postLayout, false)

            detailImage = postView.findViewById(R.id.detail_activity_list_img)
            detailContent = postView.findViewById(R.id.detail_activity_list_contents)
            detailCommentIcon = postView.findViewById(R.id.detail_activity_comment_icon)
            detailComment = postView.findViewById(R.id.detail_activity_comment)
            likeButton = postView.findViewById(R.id.like_button)
            likeCount = postView.findViewById(R.id.like_count)

            detailContent.text = post.postContent

            detailImage.setImageResource(post.postImage)

            detailCommentIcon.setImageResource(post.commentIcon)

            detailComment.text = post.comment

            postLayout.addView(postView)

            if (post.likeSelectedUser?.any { it == myId } == true){
                likeButton.setImageResource(heart)
            }

            likeCount.text = post.like.toString()

            setLikeButton(post)
        }
    }
```
