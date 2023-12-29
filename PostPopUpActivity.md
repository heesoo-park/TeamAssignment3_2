PostPage 댓글쓰기
=
![Honeycam 2023-12-29 10-40-35](https://github.com/Guri999/codekata/assets/116724657/e93b533c-5bef-4dec-9348-7d578708827f)

# Layout
## 세로모드
![image](https://github.com/heesoo-park/TeamAssignment3_2/assets/116724657/15eb026e-cf2e-44c2-a674-8b6fdfeb8eb1)

세로 모드는 좌측 상단에 사용자 아이콘과 이름

우측에 닫기버튼을 넣었다

디테일 페이지와 유사한대

```
                 <LinearLayout
                    android:id="@+id/post_comment_layout"
                    android:layout_width="match_parent"
                    android:layout_height="match_parent"
                    android:orientation="vertical" />
                    <androidx.constraintlayout.widget.ConstraintLayout
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:background="@color/component_color">
                        <EditText
                            android:id="@+id/post_input_comment"
                            android:layout_width="280dp"
                            android:layout_height="wrap_content"
                            android:layout_marginStart="10dp"
                            android:hint="@string/post_empty_comment"
                            app:layout_constraintStart_toStartOf="parent"
                            app:layout_constraintTop_toTopOf="parent"
                            app:layout_constraintBottom_toBottomOf="parent"/>

                        <ImageView
                            android:id="@+id/post_comment_btn"
                            android:layout_width="40dp"
                            android:layout_height="40dp"
                            android:layout_marginEnd="20dp"
                            android:src="@drawable/selector_button_submit"
                            app:layout_constraintEnd_toEndOf="parent"
                            app:layout_constraintTop_toTopOf="parent"
                            app:layout_constraintBottom_toBottomOf="parent"/>

                    </androidx.constraintlayout.widget.ConstraintLayout>
                </LinearLayout>

```
comment의 list를 받을 LinearLayout post_comment_layout를 만들어줬고

그 밑에 댓글을 입력할 창을 만들어서

댓글이 여러개 달려도 맨밑에 댓글 추가 위젯이 위치하게 했다.

## 가로모드
![image](https://github.com/heesoo-park/TeamAssignment3_2/assets/116724657/c734c2fe-a427-4e38-a117-04aafc5efc7b)
가로모드는 레이아웃 구성을 다르게 했는대

```
 <androidx.constraintlayout.widget.ConstraintLayout
        android:id="@+id/constraint1"
        android:layout_width="0dp"
        android:layout_height="match_parent"
        app:layout_constraintWidth_percent="0.5"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintBottom_toBottomOf="parent">
```
ConstraintLayout에서 가능한 _constraintWidth_percent를 사용해 텍스트가 있는 레이아웃과

이미지가 있는 레이아웃을 반반씩 나눠 가지게 했다

# PostPopupActivity

## 프로필 기본 세팅
```kotlin
    private val myId: String? by lazy {
        intent.getStringExtra("myId")
    }
    private val editUser: String? by lazy {
        intent.getStringExtra("editUser")
    }
    private val postKey: String? by lazy {
        intent.getStringExtra("postKey")
    }
    private val editUserData: User? by lazy {
        UserDatabase.totalUserData.find { it.id == editUser }
    }
    private val postData: Post? by lazy {
        editUserData!!.userPosts.find { it.key == postKey }
    }
...
private fun setPost(){
        ivUserProfile.setImageResource(editUserData!!.profileImage)
        tvUserName.text = editUserData!!.name
        ivPostImage.setImageResource(postData!!.postImage[0])
        tvPostContents.setText(postData!!.postContent)
        if (postData?.likeSelectedUser?.any { it == myId } == true) {
            ivLike.setImageResource(R.drawable.img_heart)
        }
        tvLikeCount.text = postData!!.like.toString()
        setLikeButton()
        setShowPostArrow()
        setCommentList()
    }
```
디테일 페이지에서 받은 사용자 아이디는 댓글쓸때

editUser는 작성자의 정보를 받아 작성자 프로필과 이름등을 설정

postkey는 보고자하는 포스트를 구분해서 볼 수 있게 한다

## 좋아요 버튼 기능

```kotlin
// 좋아요 버튼 기능 세팅하는 함수
    private fun setLikeButton() {
        ivLike.setOnClickListener {
            if (postData!!.likeSelectedUser.any { it == myId }) {
                postData!!.like -= 1
                ivLike.setImageResource(R.drawable.img_empty_heart)
                postData!!.likeSelectedUser.remove(myId)
            } else {
                postData!!.like += 1
                ivLike.setImageResource(R.drawable.img_heart)
                postData!!.likeSelectedUser.add(myId!!)
            }

            tvLikeCount.text = postData!!.like.toString()
        }
    }
```
디테일페이지와 달리 포스트가 하나이기 때문에

지역변수를 선언할 필요가 없어 졌다

## 댓글 리스트 세팅

```kotlin
private fun setCommentList() {
        commentLayout.removeAllViews()
        val commentUser = postData?.commentUser ?: listOf()
        for (comment in commentUser) {
            val commentView = inflater.inflate(R.layout.post_comment_item, commentLayout, false)
            val ivCommentProfile:ImageView = commentView.findViewById(R.id.iv_post_comment_icon)
            val tvComment:TextView = commentView.findViewById(R.id.tv_post_comment)
            val tvShowMore:TextView = commentView.findViewById(R.id.tv_post_show_more)

            ivCommentProfile.setImageResource(comment.commentIcon)
            tvComment.text = comment.id + " : " + comment.comment

            commentLayout.addView(commentView)
            setShowMoreVisible(tvComment, tvShowMore)
        }
    }
```
post_comment_item.xml을 가져와 commentLayout안에 넣어줬다.

댓글마다 사용자의 프로필,이름과 코맨트를 가져와 세팅하고 더보기 기능을 넣어줬다.

## 댓글 추가
```kotlin
private fun setAddComment(){
        ivCommentButton.setOnClickListener {
            if (!etComment.text.isEmpty()) {
                val commentData = CommentUser(
                    myId!!,
                    etComment.text.toString(),
                    UserDatabase.totalUserData.find { it.id == myId }!!.profileImage
                )
                etComment.setText("")
                postData!!.commentUser!!.add(commentData)
            }else etComment.error.get(R.string.post_empty_comment)
            setCommentList()
        }
    }
```
온클릭 리스너를 사용해 사용자가 submit버튼을 누르고

etComment(editText)가 비어있지 않을때 포스트의 commentUser에 사용자의 정보를 넣어준다.
