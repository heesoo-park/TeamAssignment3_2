package com.example.teamsns

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.isVisible
import com.example.teamsns.R.drawable.img_empty_heart
import com.example.teamsns.R.drawable.img_heart

class DetailPageActivity : AppCompatActivity() {

    private val profileRefresh =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) init()
        }

    private val myId: String? by lazy {
        intent.getStringExtra("myId")
    }

    private val id: String? by lazy {
        intent.getStringExtra("id")
    }

    private lateinit var userData: User

    private lateinit var name: String

    private lateinit var statusMessage: String

    private val ivDetailBackBtn: ImageView by lazy {
        findViewById(R.id.iv_detail_back_btn)
    }
    private val tvDetailEditBtn: TextView by lazy {
        findViewById(R.id.tv_detail_edit_btn)
    }
    private val tvDetailId: TextView by lazy {
        findViewById(R.id.tv_detail_id)
    }
    private val tvDetailName: TextView by lazy {
        findViewById(R.id.tv_detail_name)
    }
    private val tvDetailStatusMessage: TextView by lazy {
        findViewById(R.id.tv_detail_status_message)
    }
    private val tvDetailLogoutBtn: TextView by lazy {
        findViewById(R.id.tv_detail_logout_btn)
    }
    private val ivDetailProfile: AppCompatImageView by lazy {
        findViewById(R.id.iv_detail_profile)
    }
    private val tvDetailMyPageOrDetail: TextView by lazy {
        findViewById(R.id.tv_detail_my_page_or_detail)
    }

    private val inflater: LayoutInflater by lazy {
        LayoutInflater.from(this)
    }
    private val detailPostLayout: LinearLayout by lazy {
        findViewById(R.id.layout_detail_post_layout)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_page)

        init()
    }

    private fun init() {
        setProfile()
        setPersonalButton()
        setBackButton()
    }

    // 사용자 프로필 세팅하는 함수
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

    // 편집, 로그아웃 버튼 활성화 함수
    private fun setPersonalButton() {
        val visibleBoolean = (myId == id)
        tvDetailEditBtn.isVisible = visibleBoolean
        tvDetailLogoutBtn.isVisible = visibleBoolean

        setLogOutButton()
        setEditButton()
    }

    // 뒤로가기 버튼 동작 함수
    private fun setBackButton() {
        ivDetailBackBtn.setOnClickListener {
            setResult(RESULT_OK, intent)
            finish()
            overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right)
        }
    }

    // 게시물 리스트 세팅하는 함수
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
            val ivDetailPostLeftArrow: ImageView = postView.findViewById(R.id.iv_left_arrow_button)
            val ivDetailPostRightArrow: ImageView = postView.findViewById(R.id.iv_right_arrow_button)
            var currentImageIndex = 0

            tvDetailPostListContents.text = post.postContent
            ivDetailPostListImg.setImageResource(post.postImage[0])
            tvDetailPostComment.text = post.comment
            tvDetailPostLikeCount.text = post.like.toString()
            if (post.commentIcon != null) ivDetailPostCommentIcon.setImageResource(post.commentIcon!!)

            detailPostLayout.addView(postView)

            if (post.likeSelectedUser.any { it == myId }) {
                ivDetailPostLikeBtn.setImageResource(img_heart)
            }

            setLikeButton(post, ivDetailPostLikeBtn, tvDetailPostLikeCount)
            setShowMoreVisible(post, tvDetailPostListContents, tvDetailPostShowMore)
            setShowPostArrow(post, ivDetailPostLeftArrow, ivDetailPostRightArrow,ivDetailPostListImg,currentImageIndex)
        }
    }

    // 좋아요 버튼 기능 세팅하는 함수
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

    // 더보기 버튼 활성화 함수
    private fun setShowMoreVisible(post: Post, detailContent: TextView, showMore: TextView) {
        detailContent.post {
            if (detailContent.lineCount > detailContent.maxLines) showMore.visibility = View.VISIBLE
            else showMore.visibility = View.INVISIBLE
        }

        setShowMoreButton(post, detailContent, showMore)
    }

    // 더보기 버튼 기능 세팅하는 함수
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

    // 포스트 이미지가 여러개일시 화살표 표시
    private fun setShowPostArrow(post: Post, leftArrow: ImageView, rightArrow: ImageView, imageView: ImageView,currentImageIndex: Int) {
        val postSize = post.postImage.size
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

    // side화살표 버튼 클릭시 이미지 변화
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

    // 로그아웃 버튼 기능 세팅하는 함수
    private fun setLogOutButton() {
        tvDetailLogoutBtn.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }
    }

    // 편집 버튼 기능 세팅하는 함수
    private fun setEditButton() {
        tvDetailEditBtn.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            intent.putExtra("editId", myId)
            profileRefresh.launch(intent)
            overridePendingTransition(R.anim.slide_in_from_right, R.anim.fade_out)
        }
    }

}