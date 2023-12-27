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
LayoutInflater 인스턴스를 가져오고

inflate() 메서드를 호출해서 레이아웃 파일 post_item.xml을 View 객체로 가져온다

postLayout을 부모로 둬 그 안에서 생성되고

false로 attachToRoot를 설정해 호출자가 나중에 View를 추가할 수 있도록 생성된 View만 반환한다

생성된 post에 들어갈 자료들을 초기화하고 세팅

만약 자신이 좋아요을 누른 포스트라면 빈하트가 아닌 채워진하트로 나오게 했다

#### setLikeButton

```kotlin
private fun  setLikeButton(post: Post){
        likeButton.setOnClickListener {
            if (post.likeSelectedUser?.any { it == myId } == true){
                post.like -= 1
                likeButton.setImageResource(empty_heart)
                post.likeSelectedUser!!.remove(myId)
            }else{
                post.like += 1
                likeButton.setImageResource(heart)
                post.likeSelectedUser?.add(myId!!)
            }
            likeCount.text = post.like.toString()
        }
    }
```
좋아요 버튼의 온클릭 리스터를 설정

자신이 좋아요를 눌렀었다면 좋아요 버튼을 눌렀을때 post의 좋아요가 1 내려가게되고 이미지가 empty_heart로 바뀌게 되고

피드에 좋아요를 한 사람중 자신의 아이디가 사라지게 된다

누르지 않았다면 좋아요가 1올라가게 되고 이미지가 heart로 바뀌며

피드에 좋아요를 한 사람의 리스트에 올라가게 된다

### setShowMoreVisible
```kotlin
private fun setShowMoreVisible(post: Post) {
        detailContent.text = post.postContent

        if (detailContent.lineCount > detailContent.maxLines) showMore.visibility = View.VISIBLE
        else showMore.visibility = View.INVISIBLE

        setShowMoreButton(post)
    }
```
lineCount를 이용해 maxLines과 비교하고 더크면 ..더보기 버튼을 보여주고

그렇지 않으면 숨기게 했다

### setSHowMoreButton
```kotlin
    private fun setShowMoreButton(post: Post) {
        detailContent.text = post.postContent
        showMore.setOnClickListener {
            if (detailContent.maxLines == Integer.MAX_VALUE) {
                detailContent.maxLines = 2
                showMore.setText(R.string.show_more)
            } else {
                detailContent.maxLines = Integer.MAX_VALUE
                showMore.setText(R.string.show_close)
            }
        }
    }
```
더보기 버튼을 누르면 maxLine의 제한이 해제되며 접기로 변하게 했다

## setPersonalButton

```kotlin
   private fun setPersonalButton() {
        val visibleBoolean = myId == id
        edit.isVisible = visibleBoolean
        logOut.isVisible = visibleBoolean

        setLogOutButton()
        setEditButton()
    }
```
visivleBoolean이 myId와 id가 같다면 true 아니면 false가 나오게 하고

personal버튼들의 visible을 정해준다

### setLogOutButton
```kotlin
    private fun setLogOutButton(){
        val intent = Intent(this, SignInActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
```
flags를 이용해 모든 액티비티를 종료하고

SignInActivity가 새 작업의 기초가 된다

### setEditButton
```kotlin
    private val profileRefresh =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) init()
        }

    ....
    private fun setEditButton(){
        edit.setOnClickListener {
            val intent = Intent(this,SignUpActivity::class.java)
            intent.putExtra("editId",myId)
            profileRefresh.launch(intent)
        }
    }
```
회원가입 페이지로 넘어가고 Extra파일을 보내 회원가입 페이지를 프로필 수정 페이지로 재사용 한다

registerForActivityResult를 이용해 이때 수정된값을 회원가입 창이 닫혔을때 바로 갱신받는다

# resorce

![image](https://github.com/heesoo-park/TeamAssignment3_2/assets/116724657/e3ad3b13-3fa5-4d88-be09-ca0e44b961cd)
string파일을 언어에 맞춰 추가

![image](https://github.com/heesoo-park/TeamAssignment3_2/assets/116724657/f00e6be9-0c38-4d36-9060-5f9a2e6c5daf)
다크모드도 색을 변환시켜 추가했다

![image](https://github.com/heesoo-park/TeamAssignment3_2/assets/116724657/69c2c37c-1425-4b25-9815-e0f1f167fe72)
가로모드 추가
