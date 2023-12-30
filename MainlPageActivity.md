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

각각의 사용자 이미지뷰는 원형모양으로 만들었고

초기에는 외부 라이브러리인 CircleImageView를 사용했으나 협업과정에서 라이브러리를 불러오지 못하는 경우가 발생해 CardView를 변경했다.


사용자목록 아래 게시물 목록은 남아있는 영역을 ScrollView로 잡아두고 사진과 게시글은 main_post_item.xml파일을 통해 동적으로 불러오게 하고 있다.

main_post_item의 레이아웃은 다음과 같다.

main_post_item
=
## 레이아웃 구성

리니어레이아웃을 기본으로

{ConstraintLayout 상단바(프로필, 사용자명)}

{ConstraintLayout 게시물사진, 좌우 화살표}

{ConstraintLayout 게시물 텍스트, 좋아요이미지, 좋아요수}

로 구성되어 있다.


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

## 초기화
```kotlin
private fun init(){
    mainUserProfileList.removeAllViews()
    setUserProfileList()
    setTopbar()
}
```
사용자 프로필 생성 중복방지를 위한 초기화 함수이다.

init함수가 실행되면 사용자 프로필 영역(HorizontalScrollView)의 모든 뷰는 삭제가 된다.

이후 사용자 프로필 세팅을 위한 setUserProfileList() 함수와 상단바 구성을 위한 setTopbar() 함수를 호출한다.

## 상단바 구성
```kotlin
private fun setTopbar() {
    ivMainMyProfile.setImageResource(userData.profileImage)

    setOnProflieClickListener(userData,ivMainMyProfile)
}
```
상단바 구성을 위한 함수이다.

"안녕하세요 ____님"과 본인의 프로필이미지를 나타내고

사용자 프로필을 보여주기 위한 setUserProfileList() 함수를 호출한다.


## 사용자 프로필
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
        mainPostList.removeAllViews()
    }
    setPostList()
}
```
사용자 프로필을 세팅하기 위한 함수이다.

현재의 레이아웃(activity_main_page.xml)에 다른 레이아웃(post_item.xml)을 올리기 위해서는 레이아웃을 객체화시켜줘야하기 때문에 inflater를 사용하여 스크롤뷰내에 프로필들을 표시하였다.

프로필은 원형모양의 테두리를 표시해기위해 사진을 올릴 이미지뷰(profileImg)와 테두리를 씌울 이미지뷰(profileStroke)를 불러왔고,

현재 사용자를 구분하기 위해 로그인한 아이디와 프로필아이디가 같으면 stroke(하늘색테두리)를, 다르면 strkoe2(분홍색테두리)를 불러와 표시해줬다.

프로필 이미지 클릭시 발생하는 이벤트는 setOnProfileClickListener() 함수 호출을 통해 실행이 되고

중복방지를 위한 scrollView내에 있는 모든 뷰를 삭제 후 게시물을 보여주기 위한 setPostList() 함수 호출한다. 

## 사용자 프로필 이미지 클릭시
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

## 게시물 세팅
```kotlin
private fun setPostList() {
    for (user in UserDatabase.getTotalUser()) {
        for (post in user.userPosts.reversed()) {
            val postView: View =
                inflater.inflate(R.layout.main_post_item, mainPostList, false)

            val ivMainPost: ImageView = postView.findViewById(R.id.iv_main_post) //main_post_item.xml파일 이미지뷰
            val tvMainPostContent: TextView = postView.findViewById(R.id.tv_main_post_content) //main_post_item.xml파일 게시글 텍스트뷰
            val ivMainPostUserProfile: ImageView = postView.findViewById(R.id.iv_main_post_user_profile) //main_post_item.xml파일 프로필사진 이미지뷰
            val tvMainPostUserName: TextView = postView.findViewById(R.id.tv_main_post_user_name) //main_post_item.xml파일 사용자명 텍스트뷰
            val ivMainPostLikeBtn: ImageView = postView.findViewById(R.id.iv_main_post_like_btn) //main_post_item.xml파일 좋아요사진 이미지뷰
            val tvMainPostLikeCount: TextView = postView.findViewById(R.id.tv_main_post_like_count) //main_post_item.xml파일 좋아요카운트 텍스트뷰
            val tvMainPostShowMore: TextView = postView.findViewById(R.id.tv_main_post_show_more) //main_post_item.xml파일 더보기 텍스트뷰
            val ivDetailPostLeftArrow: ImageView = postView.findViewById(R.id.iv_left_arrow_button) //게시물 좌 화살표
            val ivDetailPostRightArrow: ImageView = postView.findViewById(R.id.iv_right_arrow_button) //게시물 우 화살표
            val currentImageIndex = 0

            tvMainPostContent.text = post.postContent //post.kt 게시글
            ivMainPost.setImageResource(post.postImage[0]) //post.kt 게시물이미지
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

이후에 발생 할 수 있는 좋아요버튼클릭(setLikeButton()), 더보기클릭(setShowMoreViwible()), 프로필이미지클릭(setOnProfileImageClickListener()), 화살표클릭(setShowPostArrow())은 각각의 함수를 호출함으로서 처리히고 있다.

## 게시물 프로필 이미지 클릭
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

## 좋아요 기능
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

## 좋아요 수 && 하트 변화
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
setLikeButton() 함수에서 게시물 또는 좋아요 버튼을 클릭시 호출되는 함수로, 좋아요수를 표기하고 있다.

좋아요를 선택한 사용자 목록(리스트)내에 현재 사용자가 포함되있으면 좋아요수를 1만큼 줄여주고 좋아요버튼을 비어있는 하트로, post파일에 likeSelectedUser리스트에서 사용자를 삭제한다.

포함되어 있지 않으면 하트를 누르지 않았던 사용자임을 뜻하므로 좋아요수를 1만큼 늘리고, 좋아요버튼은 꽉찬 빨간하트로, 좋아요 사용자목록에 현재사용자를 추가한다.

## 더보기 버튼 활성화
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

더보기 기능은 setShowMoreButton() 함수를 호출을 통해 실행되고 있다.

## 더보기 기능
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

더보기 버튼을 눌렀을때 게시글 텍스트가 마지막줄까지 표시되었다면 "접기"가, 아니면 "..더보기"가 보여진다.

## 화살표 버튼 활성화
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
        else -> {
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

화살표 기능은 setSideArrowButton() 함수를 호출하여 실행된다.

## 화살표 기능
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
