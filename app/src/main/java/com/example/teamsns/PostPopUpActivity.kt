package com.example.teamsns

import android.app.assist.AssistContent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback

class PostPopUpActivity : AppCompatActivity() {

    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    private val ivUserProfile: ImageView by lazy {
        findViewById(R.id.iv_post_userprofile)
    }
    private val tvUserName: TextView by lazy {
        findViewById(R.id.tv_post_username)
    }
    private val ivBackArrow: ImageView by lazy {
        findViewById(R.id.iv_back)
    }
    private val ivPostImage: ImageView by lazy {
        findViewById(R.id.iv_post_list_img)
    }
    private val ivLeftArrow: ImageView by lazy {
        findViewById(R.id.iv_left_arrow_button)
    }
    private val ivRightArrow: ImageView by lazy {
        findViewById(R.id.iv_right_arrow_button)
    }
    private val tvPostContents: TextView by lazy {
        findViewById(R.id.tv_post_list_contents)
    }
    private val ivLike: ImageView by lazy {
        findViewById(R.id.iv_post_like_btn)
    }
    private val tvLikeCount: TextView by lazy {
        findViewById(R.id.tv_post_like_count)
    }
    private val etComment: EditText by lazy {
        findViewById(R.id.post_input_comment)
    }
    private val ivCommentButton: ImageView by lazy {
        findViewById(R.id.post_comment_btn)
    }
    private val inflater: LayoutInflater by lazy {
        LayoutInflater.from(this)
    }
    private val commentLayout: LinearLayout by lazy {
        findViewById(R.id.post_comment_layout)
    }
    private val myName: String? by lazy {
        intent.getStringExtra("myName")
    }
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
    private var currentImageIndex: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_pop_up)

        editUserData
        this.onBackPressedDispatcher.addCallback(this, callback)
        init()
    }
    private fun init(){
        setPost()

        setBackButton()

        setAddComment()
    }
    // 프로필 기본 세팅
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

    // 게시물 내 좋아요 기능을 담당하는 클릭 리스너 함수
    private fun setLikeButton() {
        ivLike.setOnClickListener {
            likeShow(postData!!, ivLike, tvLikeCount)
        }
        ivPostImage.setOnLongClickListener {
            likeShow(postData!!, ivLike, tvLikeCount)
            true
        }
    }

    // 게시물 내 좋아요 숫자 세팅하는 함수
    private fun likeShow(post: Post, click: ImageView, likeCount: TextView){
        if (post.likeSelectedUser.any { it == myId }) {
            post.like -= 1
            click.setImageResource(R.drawable.img_empty_heart)
            post.likeSelectedUser.remove(myId)
        } else {
            post.like += 1
            click.setImageResource(R.drawable.img_heart)
            post.likeSelectedUser.add(myId!!)
        }
        likeCount.text = post.like.toString()
    }

    // 포스트 이미지가 여러개일시 화살표 표시
    private fun setShowPostArrow() {
        val postSize = postData!!.postImage.size
        when {
            postSize == 1 -> {
                ivLeftArrow.visibility = View.INVISIBLE
                ivRightArrow.visibility = View.INVISIBLE
            }
            currentImageIndex == postSize - 1 -> ivRightArrow.visibility = View.INVISIBLE
            currentImageIndex == 0 -> ivLeftArrow.visibility = View.INVISIBLE
            else -> {
                ivLeftArrow.visibility = View.VISIBLE
                ivRightArrow.visibility = View.VISIBLE
            }
        }
        setSideArrowButton()
    }

    // side화살표 버튼 클릭시 이미지 변화
    private fun setSideArrowButton(){
        ivLeftArrow.setOnClickListener {
            if(currentImageIndex > 0) {
                currentImageIndex -= 1
                ivPostImage.setImageResource(postData!!.postImage[currentImageIndex])
                setShowPostArrow()
            }
        }
        ivRightArrow.setOnClickListener {
            if(currentImageIndex < postData!!.postImage.size - 1) {
                currentImageIndex += 1
                ivPostImage.setImageResource(postData!!.postImage[currentImageIndex])
                setShowPostArrow()
            }
        }
    }

    // 댓글 리스트 세팅 하는 함수
    private fun setCommentList() {
        commentLayout.removeAllViews()
        val commentUser = postData?.commentUser ?: listOf()
        for (comment in commentUser) {
            val commentView = inflater.inflate(R.layout.post_comment_item, commentLayout, false)
            val ivCommentProfile:ImageView = commentView.findViewById(R.id.iv_post_comment_icon)
            val tvComment:TextView = commentView.findViewById(R.id.tv_post_comment)
            val tvShowMore:TextView = commentView.findViewById(R.id.tv_post_show_more)

            ivCommentProfile.setImageResource(comment.commentIcon)
            tvComment.text = comment.name + " : " + comment.comment

            commentLayout.addView(commentView)
            setShowMoreVisible(tvComment, tvShowMore)
        }
    }

    // 더보기 버튼 활성화 함수
    private fun setShowMoreVisible(comment: TextView, showMore: TextView) {
        comment.post {
            if (comment.lineCount > comment.maxLines) showMore.visibility = View.VISIBLE
            else showMore.visibility = View.INVISIBLE
        }

        setShowMoreButton(comment, showMore)
    }
    // 더보기 버튼 기능 세팅하는 함수
    private fun setShowMoreButton(comment: TextView, showMore: TextView) {
        showMore.setOnClickListener {
            if (comment.maxLines == Integer.MAX_VALUE) {
                comment.maxLines = 2
                showMore.setText(DetailPageMessage.SHOWMORE.message)
            } else {
                comment.maxLines = Integer.MAX_VALUE
                showMore.setText(DetailPageMessage.SHOWCLOSE.message)
            }
        }
    }

    //댓글 추가
    private fun setAddComment(){
        ivCommentButton.setOnClickListener {
            if (!etComment.text.isEmpty()) {
                val commentData = CommentUser(
                    myName!!,
                    etComment.text.toString(),
                    UserDatabase.totalUserData.find { it.id == myId }!!.profileImage
                )
                etComment.setText("")
                postData!!.commentUser!!.add(commentData)
            } else {
                etComment.error.get(R.string.post_empty_comment)
            }
            setCommentList()
        }
    }

    //뒤로가기
    private fun setBackButton(){
        ivBackArrow.setOnClickListener {
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}