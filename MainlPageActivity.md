MainPageActivity
=
## 레이아웃 구성

[//]: # (![image]&#40;https://github.com/heesoo-park/TeamAssignment3_2/assets/116724657/f0ad9d78-554f-4a69-b686-904dde3b1baf&#41;)


ConstraintLayout을 기본으로

{ConstraintLayout 상단바}
{HorizontalScrollView 사용자 목록}
{ScrollView 게시물 목록}
으로 구성했다.

상단바 왼쪽에는 로고가, 우측 사진은 사용자 본인의 프로필사진이 위치해있고
프로필사진 클릭시 마이페이지로 넘어간다.

사용자목록은 좌우로 스크롤이 가능하게 되어었고, 사용자 프로필 이미지를 클릭시 각자의 디테일액티비티로 넘어간다.
각자용자의 이미지뷰는 원형모양으로 만들었고
초기에는 외부 라이브러리인 CircleImageView를 사용했으나 협업과정에서 라이브러리를 불러오지 못한느 경우가 발생해 CardView를 사용했다.


사용자목록 아래 게시물 목록은 남아있는 영역을 ScrollView로 잡아두고 사진과 게시글은 main_post_item.xml파일을 통해 동적으로 불러오게 하고 있다.
main_post_item의 레이아웃은 하단 링크에 설명되어 있다.




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
private fun setUserProfileList() {
        for (user in UserDatabase.totalUserData) {
            val profileView: View =
                inflater.inflate(R.layout.profile_item, mainUserProfileList, false)
            val profileImg: ImageView = profileView.findViewById(R.id.iv_main_user_profile)//profile_item.xml파일 이미지뷰
            val profileStroke: ImageView = profileView.findViewById(R.id.iv_main_user_stroke)//profile_item.xml파일 원형이미지뷰
            profileImg.setImageResource(user.profileImage)//User.kt파일 프로필이미지
            if (loginUserId != user.id) {
                profileStroke.setImageResource(R.drawable.shape_profile_image_stroke2)//테두리 색상 분홍
            }else {
                profileStroke.setImageResource(R.drawable.shape_profile_image_stroke) //하늘색
            }
            mainUserProfileList.addView(profileView)
            setOnProflieClickListener(user, profileImg) //이미지 클릭 리스너 함수
        }
    }
```
현재의 레이아웃(activity_main_page.xml)에 다른 레이아웃(post_item.xml)을 올리기 위해서는 레이아웃을 객체화시켜줘야하기 때문에 inflater를 사용하여 스크롤뷰내에 프로필들을 표시하였다.

프로필은 원형모양의 테두리를 표시해기위해 사진을 올릴 이미지뷰(profileImg)와 테두리를 씌울 이미지뷰(profileStroke)를 불러왔고,

현재 사용자를 구분하기 위해 로그인한 아이디와 프로필아이디가 같으면 stroke(하늘색테두리)가 다으면 strkoe2(분홍색테두리)를 불러와 표시해줬다.


```kotlin
private fun setOnProflieClickListener(user: User, button: ImageView) {
        button.setOnClickListener {
            val intent = Intent(this@MainPageActivity, DetailPageActivity::class.java)
            intent.putExtra("myId", loginUserId)
            intent.putExtra("id", user.id)
            profileRefresh.launch(intent) //registerForActivityResult
            overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left) }
}
```
사용자 프로필 이미지를 클릭시 발생하는 이벤트를 처리하기 위한 함수이다.

각 프로필을 클릭시 디테일액티비티 인텐트를 생성하고

인텐트 실행시 사용자정보확인을 위해 해당프로필의 ID와 현재사용자의 계정ID를 같이 넘겨주었다.


```kotlin
private fun setPostList() {
    for (user in UserDatabase.getTotalUser()) {
        for (post in user.userPosts.reversed()) {
            val postView: View =
                inflater.inflate(R.layout.main_post_item, mainPostList, false)

            val ivMainPost: ImageView = postView.findViewById(R.id.iv_main_post) //main_post_item.xml파일 이미지뷰
            val tvMainPostContent: TextView = postView.findViewById(R.id.tv_main_post_content) //main_post_item.xml파일 게시글 텍스트뷰
            val ivMainPostUserProfile: ImageView =
                postView.findViewById(R.id.iv_main_post_user_profile) //main_post_item.xml파일 프로필사진 이미지뷰
            val tvMainPostUserName: TextView = postView.findViewById(R.id.tv_main_post_user_name) //main_post_item.xml파일 사용자명 텍스트뷰
            val ivMainPostLikeBtn: ImageView = postView.findViewById(R.id.iv_main_post_like_btn) //main_post_item.xml파일 좋아요사진 이미지뷰
            val tvMainPostLikeCount: TextView =
                postView.findViewById(R.id.tv_main_post_like_count) //main_post_item.xml파일 좋아요카운트 텍스트뷰
            val tvMainPostShowMore: TextView =
                postView.findViewById(R.id.tv_main_post_show_more) //main_post_item.xml파일 더보기 텍스트뷰

            tvMainPostContent.text = post.postContent //post.kt 게시글
            ivMainPost.setImageResource(post.postImage) //post.kt 게시물이미지
            ivMainPostUserProfile.setImageResource(post.userProfileImage) //post.kt 프로필이미지
            tvMainPostUserName.text = user.name //post.kt 사용자명
            tvMainPostLikeCount.text = post.like.toString() //post.kt 좋아요수

            mainPostList.addView(postView)

            if (post.likeSelectedUser.any { it == loginUserId }) {
                ivMainPostLikeBtn.setImageResource(R.drawable.img_heart) //빨간하트
            }

            tvMainPostLikeCount.text = post.like.toString() //좋아요텍스트뷰에 좋아요수 표시

            setLikeButton(post, ivMainPostLikeBtn, tvMainPostLikeCount, ivMainPost) //게시물 내 좋아요버튼 클릭 리스너 함수 실행
            setShowMoreVisible(post, tvMainPostContent, tvMainPostShowMore) //게시물 내 더보기버튼 클릭 리스너 함수 실행
            setOnProfileImageClickListener(user, ivMainPostUserProfile) //게시물 내 프로필 이미지 클릭 리스너 함수 실행
            setShowPostArrow(post, ivDetailPostLeftArrow, ivDetailPostRightArrow,ivMainPost,currentImageIndex)
        }
    }
}
```
게시물을 보여주기 위한 함수이다.

사용자 목록과 마찬가지로 메인페이지 레이아웃 위에 post_item레이아웃을 보여주기 위해서 inflater를 사용해 post_item을 객체화시켜주었고

좋아요를 누른 사용자들중 아무나의 아이디와 로그인한 아이디가 같으면(현재 사용자가 이미 좋아요를 눌렀으면) 좋아요이미지를 꽉찬하트로 보이게 한다.


```kotlin
private fun setOnProfileImageClickListener(user: User, detailUserProfileIcon: ImageView) {
    detailUserProfileIcon.setOnClickListener {
        val intent = Intent(this@MainPageActivity, DetailPageActivity::class.java)
        intent.putExtra("myId", loginUserId)
        intent.putExtra("id", user.id)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left) //애니메이션
    }
}
```
게시물 내 프로필이미지를 클릭시 발생하는 이벤트를 처리하는 함수이다.

프로필 이미지 클릭시에는 디테일페이지로 액티비티가 전환되고 사용자정보확인을 위해 본인의 아이디와 클릭한 프로필의 아이디를 같이 넘겨주고 있다.


게시물 내 좋아요 버튼 클릭 리스너 함수
```kotlin
private fun setLikeButton(post: Post, likeButton: ImageView, likeCount: TextView, detailImage:ImageView) {
    likeButton.setOnClickListener { //좋아요버튼(이미지) 클릭시
        likeShow(post, likeButton, likeCount) //좋아요 세팅함수 실행
    }
    detailImage.setOnLongClickListener { //게시물 이미지 길게 클릭시
        likeShow(post,likeButton, likeCount) //좋아요 세팅함수 실행
        true
    }
}
```
게시물 내 좋아요 버튼 클릭시 발생하는 이벤트를 처리하기 위한 함수이다.

좋아요버튼(하트 이미지)과 게시글을 클릭하면 좋아요표시가 되고

좋아요버튼은 클릭으로, 게시글은 길게 클릭시 반영된다.


```kotlin
private fun likeShow(post: Post, click: ImageView, likeCount: TextView){
    if (post.likeSelectedUser.any { it == loginUserId }) { //좋아요선택한 사용자중 아무나의 아이디와 로그인 아이디가 같으면(==로그인한 현재 사용자가 이미 좋아요를 눌렀다면)
        post.like -= 1
        click.setImageResource(R.drawable.img_empty_heart)
        post.likeSelectedUser.remove(loginUserId) //post파일 좋아요선택한 사용자 목록(리스트)에서 삭제
    } else {
        post.like += 1
        click.setImageResource(R.drawable.img_heart)
        post.likeSelectedUser.add(loginUserId)
    }
    likeCount.text = post.like.toString()
}
```
게시물 또는 좋아요버튼을 클릭시 실행되는 함수이고

좋아요를 선택한 사용자 목록(리스트)내에 현재 사용자가 포함되있으면 좋아요수 카운트를 1만큼 줄여주고 좋아요버튼을 이어있는 하트로, post파일에 likeSelectedUser리스트에서 사용자를 삭제한다.

포함되어 있지 않으면 하트를 누르지 않았던 사용자임을 뜻하므로 좋아요수 카운트를 1만큼 늘리고, 좋아요버튼은 꽉찬 빨간하트로, 좋아요 사용자목록에 현재사용자를 추가한다.


```kotlin
private fun setShowMoreVisible(post: Post, detailContent: TextView, showMore: TextView) {
    detailContent.post {
        if (detailContent.lineCount > detailContent.maxLines) showMore.visibility = View.VISIBLE
        else showMore.visibility = View.INVISIBLE
    }

    setShowMoreButton(post, detailContent, showMore)
}
```
게시물 내 더보기 버튼을 활성화하기 위한 함수이다.

한번에 보여줄 수 있는 줄보다 게시글이 더 길면 더보기 버튼이 활성화되고, 아니면 비활성화된다.


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
게시물 내 더보기 버튼 기능세팅하기 위한 함수이다.

더보기 버튼을 눌렀을때 게시글의 마지막줄까지 표시되었다면 "접기"가, 아니면 "..더보기"가 보여진다.


포스트 이미지가 여러개일시 화살표 표시
```kotlin
private fun setShowPostArrow(post: Post, leftArrow: ImageView, rightArrow: ImageView, imageView: ImageView,currentImageIndex: Int) {
    val postSize = post.postImage.size //게시글 이미지 리스트 사이즈
    when {
        postSize == 1 -> {
            leftArrow.visibility = View.INVISIBLE
            rightArrow.visibility = View.INVISIBLE
        }
        currentImageIndex == postSize - 1 -> rightArrow.visibility = View.INVISIBLE
        currentImageIndex == 0 -> leftArrow.visibility = View.INVISIBLE
        else -> { //그 외에는 좌우 화살표 모두 활성화
            leftArrow.visibility = View.VISIBLE
            rightArrow.visibility = View.VISIBLE
        }
    }
    setSideArrowButton(post, leftArrow, rightArrow, imageView, currentImageIndex)
}
```
게시글에 올라갈 이미지가 여러개일경우 화살표표시를 위한 함수이다.

post.kt파일에 postImage리스트에 사진이 저장되어 있고 리스트의 크기가 1이면(사진이 1장이면) 모든 화살표를 비활성화 시키고

사진이 마지막사진이면 오른쪽 화살표를 비활성화, 첫번째 사진이면 왼쪽 화살표를 비활성화 시킨다.


```kotlin
private fun setSideArrowButton(post: Post,leftArrow: ImageView,rightArrow: ImageView,imageView: ImageView,currentImageIndex: Int){
        var index = currentImageIndex
        leftArrow.setOnClickListener {
            if(index > 0) {
                index -= 1
                imageView.setImageResource(post.postImage[index])
                setShowPostArrow(post,leftArrow,rightArrow,imageView,index)
            }
        }
        rightArrow.setOnClickListener {
            if(index < post.postImage.size - 1) {
                index += 1
                imageView.setImageResource(post.postImage[index])
                setShowPostArrow(post,leftArrow,rightArrow,imageView,index)
            }
        }
        imageView.setImageResource(post.postImage[index])
    }
```
화살표 버튼 클릭시 이미지 변화를 위한 함수이다.

왼쪽 화살표를 눌렀을때 post.kt파일 postImage리스트의 인덱스가 0보다 크면(첫번째 사진이 아니면) 리스트상 앞의 사진이 보이게,

오른쪽 화살표를 눌렀을때 postImage리스트의 인덱스가 마지막인덱스보다 작으면(마지막 사진이 아니면) 뒤의 사진이 보이게 한다.




=






```kotlin
loginUserId = intent.getStringExtra("id")!!
userData = UserDatabase.getUser(loginUserId)!!

tvMainHelloWord.text = getString(R.string.hello_word, userData.name)
ivMainMyProfile.setImageResource(userData.profileImage!!)
```
로그인페이지로부터 사용자의 아이디와 이름을 받아 상단바에 사용자정보를 표시하고 있다.(EX. 안녕하세요 토토로님)
아이디는 각종 이벤트가 발생할때 사용자구분을 위해 같이 받아온다.

## 프로필클릭시 이벤트

상단바 본인의 프로필, 사용자목록의 프로필을 클릭시 해당 프로필의 상세 액티비티로 넘어가는데 
클릭한 프로필이 본인의 프로필이면 마이페이지로, 다르면 각 사용자의 디테일액티비티로 전환되기 위해 나의 ID와 해당 프로필 사용자ID를 같이 넘겨주고 있다.
```kotlin
    private fun setOnProflieClickListener() {
    profileList.forEach { iv ->
        iv.setOnClickListener {
            val intent = Intent(this@MainPageActivity, DetailPageActivity::class.java)
            when (iv.id) {
                R.id.iv_main_profile_btn -> {
                    intent.putExtra("myId", loginUserId)
                    intent.putExtra("id", loginUserId)
                }

                R.id.iv_main_user1_btn -> {
                    intent.putExtra("myId", loginUserId)
                    intent.putExtra("id", UserDatabase.totalUserData[0].id)
                }

                R.id.iv_main_user2_btn -> {
                    intent.putExtra("myId", loginUserId)
                    intent.putExtra("id", UserDatabase.totalUserData[1].id)
                }

                R.id.iv_main_user3_btn -> {
                    intent.putExtra("myId", loginUserId)
                    intent.putExtra("id", UserDatabase.totalUserData[2].id)
                }

                R.id.iv_main_user4_btn -> {
                    intent.putExtra("myId", loginUserId)
                    intent.putExtra("id", UserDatabase.totalUserData[3].id)
                }
            }
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left)
        }
    }
}
```

```kotlin
private val ivMainMyProfile: ImageView by lazy {
    findViewById(R.id.iv_main_profile_btn)
}
private val ivMainUser1: ImageView by lazy {
    findViewById(R.id.iv_main_user1_btn)
}
private val ivMainUser2: ImageView by lazy {
    findViewById(R.id.iv_main_user2_btn)
}
private val ivMainUser3: ImageView by lazy {
    findViewById(R.id.iv_main_user3_btn)
}
private val ivMainUser4: ImageView by lazy {
    findViewById(R.id.iv_main_user4_btn)
}

private val profileList
    get() = listOf(
        ivMainMyProfile,
        ivMainUser1,
        ivMainUser2,
        ivMainUser3,
        ivMainUser4,
    )
```
profileList는 findViewById를 통해 각 프로필의 정보를 얻어와 추가해주었다.

```kotlin
private fun setUserProfileList() {
        for (user in UserDatabase.totalUserData) {
            val profileView: View =
                inflater.inflate(R.layout.profile_item, mainUserProfileList, false)
            val profileImg: ImageView = profileView.findViewById(R.id.iv_main_user_profile)
            val profileStroke: ImageView = profileView.findViewById(R.id.iv_main_user_stroke)
            profileImg.setImageResource(user.profileImage)
            if (loginUserId != user.id) {
                profileStroke.setImageResource(R.drawable.shape_profile_image_stroke2)
            }else {
                profileStroke.setImageResource(R.drawable.shape_profile_image_stroke)
            }
            mainUserProfileList.addView(profileView)
            setOnProflieClickListener(user, profileImg)
        }
    }
```
게시물은 메인액티비티 xml파일에서 세팅하지 않고 UserDatabase에 저장된  MainPageActivity파일로 불러와 main_post_item.xml파일에 생성 후 메인액티비티 스크롤뷰에 담아주었다.



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
