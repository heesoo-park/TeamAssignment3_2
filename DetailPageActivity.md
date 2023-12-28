DetailPageActivity
=
[readme](https://github.com/heesoo-park/TeamAssignment3_2/blob/dev/README.md)
# 레이아웃 구성 

![image](https://github.com/heesoo-park/TeamAssignment3_2/assets/116724657/2c965d4c-2b81-40f1-803b-b77f61ca70a2)

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



## 게시물 레이아웃
post_item.xml
![image](https://github.com/heesoo-park/TeamAssignment3_2/assets/116724657/fb719b03-6041-4ab5-b933-fb3e4a18b6de)

리니어 레이아웃 안의 포스트를 지우고

그 안에 들어갈 레이아웃을 만들었다.

```
<TextView
            android:id="@+id/tv_detail_post_list_contents"
            android:layout_width="230dp"
            android:layout_height="wrap_content"
            android:layout_marginStart="10dp"
            android:layout_marginTop="5dp"
            android:layout_marginEnd="50dp"
            android:layout_marginBottom="5dp"
            android:maxLines="1"
            android:text=" Lorem Ipsum."
            android:textSize="18sp"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toTopOf="parent" />

        <TextView
            android:id="@+id/tv_detail_post_show_more"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginBottom="2dp"
            android:text="@string/show_more"
            app:layout_constraintBottom_toBottomOf="parent"
            app:layout_constraintStart_toEndOf="@id/tv_detail_post_list_contents" />

        <ImageView
            android:id="@+id/iv_detail_post_like_btn"
            android:layout_width="20dp"
            android:layout_height="20dp"
            android:layout_marginTop="5dp"
            android:layout_marginEnd="10dp"
            android:src="@drawable/selector_heart_button"
            app:layout_constraintEnd_toStartOf="@id/tv_detail_post_like_count"
            app:layout_constraintTop_toTopOf="parent" />
```
editText의 길이를 정해주고 그 옆에 더보기 버튼을 추가

사용자가 좋아요를 누를 수 있게 하트 아이콘을 만들고 리소스파일 selector_heart_button 을 만들어

눌렀을때 이미지가 반응하게 만들었다

```
<selector xmlns:android="http://schemas.android.com/apk/res/android">
    <item android:drawable="@drawable/img_heart" android:state_pressed="true"/>
    <item android:drawable="@drawable/img_empty_heart"/>
</selector>
```
### 가로모드
![image](https://github.com/heesoo-park/TeamAssignment3_2/assets/116724657/03008233-d5db-4cd9-ab6b-2ad226544acb)

land에선 editText의 길이를 늘려 더 길게 출력가능하게 만들었다

# Activity

```kotlin
    private val myId: String? by lazy {
        intent.getStringExtra("myId")
    }

    private val id: String? by lazy {
        intent.getStringExtra("id")
    }

```
프로필을 눌렀을때 ID와 로그인한 본인의 ID를 Extra로 받는다

```kotlin
    private fun init() {
        setProfile()

        setPersonalButton()

        setBackButton()
    }
```
init은 프로필 세팅, 개인 버튼 세팅, 뒤로가기 세팅으로 구성했다

## 프로필 세팅
![image](https://github.com/heesoo-park/TeamAssignment3_2/assets/116724657/ce1ad4ea-c914-451d-9026-5134f5fdf58d)

```kotlin
    private fun setProfile() {
        userData = UserDatabase.getUser(id!!)!!
        if (myId == id) tvDetailMyPageOrDetail.setText(DetailPageMessage.MYPAGE.message)
        else tvDetailMyPageOrDetail.setText(DetailPageMessage.DETAIL.message)
        name = userData.name
        statusMessage = userData.statusMessage.toString()
        ivDetailProfile.setImageResource(userData.profileImage)
        tvDetailId.setText(id)
        tvDetailName.setText(name)
        tvDetailStatusMessage.setText(statusMessage)

        detailPostLayout.removeAllViews()
        setPostList()
    }
```
메인페이지에서 보낸 StringExtra "id"를 기준으로 페이지에 프로필을 설정한다

입력받은 id가 로그인 아이디랑 같은지 확인하고 상단의 페이지 명(tvDetailMyPageOrDetail)을 변경한다

이름 상태메시지 프로필 사진들을 받고 화면에 설정하고 setPostList를 갱신한다

### 유저가 작성한 포스트 출력
![image](https://github.com/heesoo-park/TeamAssignment3_2/assets/116724657/a2a3c4a8-6d30-48af-b2f7-b35570426302)

```kotlin
    private fun setPostList() {
        for (post in userData.userPosts.reversed()) {
            val postView: View = inflater.inflate(R.layout.post_item, detailPostLayout, false)

            val ivDetailPostListImg: ImageView = postView.findViewById(R.id.iv_detail_post_list_img)
            val tvDetailPostListContents: TextView =
                postView.findViewById(R.id.tv_detail_post_list_contents)
            val ivDetailPostCommentIcon: ImageView =
                postView.findViewById(R.id.iv_detail_post_comment_icon)
            val tvDetailPostComment: TextView = postView.findViewById(R.id.tv_detail_post_comment)
            val ivDetailPostLikeBtn: ImageView = postView.findViewById(R.id.iv_detail_post_like_btn)
            val tvDetailPostLikeCount: TextView =
                postView.findViewById(R.id.tv_detail_post_like_count)
            val tvDetailPostShowMore: TextView =
                postView.findViewById(R.id.tv_detail_post_show_more)

            tvDetailPostListContents.text = post.postContent

            ivDetailPostListImg.setImageResource(post.postImage)

            ivDetailPostCommentIcon.setImageResource(post.commentIcon)

            tvDetailPostComment.text = post.comment

            detailPostLayout.addView(postView)

            if (post.likeSelectedUser.any { it == myId }) {
                ivDetailPostLikeBtn.setImageResource(img_heart)
            }

            tvDetailPostLikeCount.text = post.like.toString()

            setLikeButton(post, ivDetailPostLikeBtn, tvDetailPostLikeCount)
            setShowMoreVisible(post, tvDetailPostListContents, tvDetailPostShowMore)
        }
    }
```
LayoutInflater 인스턴스를 가져오고

inflate() 메서드를 호출해서 레이아웃 파일 post_item.xml을 View 객체로 가져온다

postLayout을 부모로 둬 그 안에서 생성되고

false로 attachToRoot를 설정해 호출자가 나중에 View를 추가할 수 있도록 생성된 View만 반환한다

반복문을 이용해 유저가 작성한 post를 찾고 그 정보를 바탕으로 게시물을 세팅하게 했다.

좋아요 버튼은 이미 자신이 좋아요를 눌렀다면 다음에 눌렀을땐 취소가 되면서 좋아요 수가 줄어들게 만들었는데

사용자가 자신이 좋아요을 누른 포스트인지 구별하기 좋게 빈하트가 아닌 채워진하트로 나오게 했다

#### 좋아요 버튼 구현
![image](https://github.com/heesoo-park/TeamAssignment3_2/assets/116724657/7c87fe7e-91da-47c8-8b40-f5079a253dab)

```kotlin
private fun setLikeButton(post: Post, likeButton: ImageView, likeCount: TextView) {
        likeButton.setOnClickListener {
            Log.e("user", post.likeSelectedUser.toString())
            if (post.likeSelectedUser.any { it == myId }) {
                post.like -= 1
                likeButton.setImageResource(img_empty_heart)
                post.likeSelectedUser.remove(myId)
            } else {
                post.like += 1
                likeButton.setImageResource(img_heart)
                post.likeSelectedUser.add(myId!!)
            }

            likeCount.text = post.like.toString()

            setPersonalButton()
        }
    }
```
게시물 에서 포스트의 정보를가진 post와 좋아요 버튼, 좋아요 수를 가지고 와서 수정한다

좋아요 버튼의 온클릭 리스터를 설정

자신이 좋아요를 눌렀었다면 좋아요 버튼을 눌렀을때 post의 좋아요가 1 내려가게되고 이미지가 empty_heart로 바뀌게 되고

피드에 좋아요를 한 사람중 자신의 아이디가 사라지게 된다

누르지 않았다면 좋아요가 1올라가게 되고 이미지가 heart로 바뀌며

피드에 좋아요를 한 사람의 리스트에 올라가게 된다

### 더보기 버튼 구현
![image](https://github.com/heesoo-park/TeamAssignment3_2/assets/116724657/166ced5c-9154-45b5-8fac-45d8fc3dffe8)

```kotlin
    private fun setShowMoreVisible(post: Post, detailContent: TextView, showMore: TextView) {
        detailContent.post {
            if (detailContent.lineCount > detailContent.maxLines) showMore.visibility = View.VISIBLE
            else showMore.visibility = View.INVISIBLE
        }

        setShowMoreButton(post, detailContent, showMore)
    }
```
lineCount를 이용해 maxLines과 비교하고 더크면 ..더보기 버튼을 보여주고

그렇지 않으면 숨기게 했다

visibility가 visible이 된다면 버튼을 활성화한다.

#### 게시물 내용 더보기
```kotlin
    private fun setShowMoreButton(post: Post, detailContent: TextView, showMore: TextView) {
        showMore.setOnClickListener {
            if (detailContent.maxLines == Integer.MAX_VALUE) {
                detailContent.maxLines = 1
                showMore.setText(DetailPageMessage.SHOWMORE.message)
            } else {
                detailContent.maxLines = Integer.MAX_VALUE
                showMore.setText(DetailPageMessage.SHOWCLOSE.message)
            }
        }
    }
```
더보기 버튼을 누르면 maxLine의 제한이 해제되며 접기로 변하게 했다

## 본인 프로필일시 보이는 버튼 구현
![image](https://github.com/heesoo-park/TeamAssignment3_2/assets/116724657/a1bed87b-3a95-4255-8f52-90bc2a3d9803)

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

### 로그아웃 버튼
![Honeycam 2023-12-28 20-18-26](https://github.com/heesoo-park/TeamAssignment3_2/assets/116724657/2fc21cbb-979f-4ced-a5e1-f6125cefe892)
```kotlin
    private fun setLogOutButton(){
        val intent = Intent(this, SignInActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }
```
로그아웃 버튼을 클릭시

flags를 이용해 모든 액티비티를 종료되고

fade_out애니메이션이 실행되며 SignInActivity를 실행한다

### 프로필 편집 버튼
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
            overridePendingTransition(R.anim.slide_in_from_right, R.anim.fade_out)
        }
    }
```
회원가입 페이지로 넘어가고 Extra파일을 보내 회원가입 페이지를 프로필 수정 페이지로 재사용 한다

registerForActivityResult를 이용해 이때 수정된값을 회원가입 창이 닫혔을때 바로 갱신받는다

## 메인페이지로 돌아가기
![Honeycam 2023-12-28 20-28-08](https://github.com/heesoo-park/TeamAssignment3_2/assets/116724657/13f7898c-1062-47ab-bc57-e1094ecf7e1b)

```kotlin
private fun setBackButton() {
        ivDetailBackBtn.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right)
        }
    }
```
액티비티를 종료하고

오른쪽에서 왼쪽으로 슬라이드되면서 사라지고 

그 에 맞춰서 메인페이지가 나오게 한다
