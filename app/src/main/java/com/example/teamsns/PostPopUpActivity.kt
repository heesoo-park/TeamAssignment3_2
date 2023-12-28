package com.example.teamsns

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

class PostPopUpActivity : AppCompatActivity() {
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
        findViewById(R.id.tv_detail_post_list_contents)
    }
    private val tvShowMore: TextView by lazy {
        findViewById(R.id.tv_detail_post_show_more)
    }
    private val ivLike: ImageView by lazy {
        findViewById(R.id.iv_post_like_btn)
    }
    private val tvLikeCount: TextView by lazy {
        findViewById(R.id.tv_post_like_count)
    }
    private val inflater: LayoutInflater by lazy {
        LayoutInflater.from(this)
    }
    private val commentLayout: LinearLayout by lazy {
        findViewById(R.id.post_comment_layout)
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

        init()
    }

    private fun init(){
        setPost()

        setBackButton()
    }
    // 프로필 기본 세팅
    private fun setPost(){
        ivUserProfile.setImageResource(editUserData!!.profileImage)
        tvUserName.text = editUserData!!.name
        ivPostImage.setImageResource(postData!!.postImage[0])
        tvPostContents.text = postData!!.postContent
        if (postData!!.likeSelectedUser.any { it == myId }) {
            ivLike.setImageResource(R.drawable.img_heart)
        }
        setLikeButton()
        setShowPostArrow()
        setCommentList()
    }

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
        val commentUser = postData?.commentUser ?: listOf()
        for (comment in commentUser) {
            val commentView = inflater.inflate(R.layout.post_comment_item, commentLayout, false)
            val ivCommentProfile:ImageView = commentView.findViewById(R.id.iv_post_comment_icon)
            val tvComment:TextView = commentView.findViewById(R.id.tv_post_comment)
            val tvShowMore:TextView = commentView.findViewById(R.id.tv_post_show_more)

            ivCommentProfile.setImageResource(comment.commentIcon)
            tvComment.text = comment.id + " : " + comment.comment

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

    //뒤로가기
    private fun setBackButton(){
        ivBackArrow.setOnClickListener {
            finish()
        }
    }
}