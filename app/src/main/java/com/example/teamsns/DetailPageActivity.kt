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
import com.example.teamsns.R.drawable.empty_heart
import com.example.teamsns.R.drawable.heart

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

    private lateinit var userData:User

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

    private fun setPersonalButton() {
        val visibleBoolean = (myId == id)
        tvDetailEditBtn.isVisible = visibleBoolean
        tvDetailLogoutBtn.isVisible = visibleBoolean

        setLogOutButton()
        setEditButton()
    }

    private fun setBackButton() {
        ivDetailBackBtn.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.none, R.anim.horizon_out)
        }
    }

    private fun setPostList() {
        for (post in userData.userPosts.reversed()) {
            val postView: View = inflater.inflate(R.layout.post_item, detailPostLayout, false)

            val ivDetailPostListImg: ImageView = postView.findViewById(R.id.iv_detail_post_list_img)
            val tvDetailPostListContents: TextView = postView.findViewById(R.id.tv_detail_post_list_contents)
            val ivDetailPostCommentIcon: ImageView = postView.findViewById(R.id.iv_detail_post_comment_icon)
            val tvDetailPostComment: TextView = postView.findViewById(R.id.tv_detail_post_comment)
            val ivDetailPostLikeBtn: ImageView = postView.findViewById(R.id.iv_detail_post_like_btn)
            val tvDetailPostLikeCount: TextView = postView.findViewById(R.id.tv_detail_post_like_count)
            val tvDetailPostShowMore: TextView = postView.findViewById(R.id.tv_detail_post_show_more)

            tvDetailPostListContents.text = post.postContent

            ivDetailPostListImg.setImageResource(post.postImage)

            ivDetailPostCommentIcon.setImageResource(post.commentIcon)

            tvDetailPostComment.text = post.comment

            detailPostLayout.addView(postView)

            if (post.likeSelectedUser.any { it == myId }) {
                ivDetailPostLikeBtn.setImageResource(heart)
            }

            tvDetailPostLikeCount.text = post.like.toString()

            setLikeButton(post, ivDetailPostLikeBtn, tvDetailPostLikeCount)
            setShowMoreVisible(post, tvDetailPostListContents, tvDetailPostShowMore)
        }
    }

    private fun setLikeButton(post: Post, likeButton: ImageView, likeCount: TextView) {
        likeButton.setOnClickListener {
            Log.e("user", post.likeSelectedUser.toString())
            if (post.likeSelectedUser.any { it == myId }) {
                post.like -= 1
                likeButton.setImageResource(empty_heart)
                post.likeSelectedUser.remove(myId)
            } else {
                post.like += 1
                likeButton.setImageResource(heart)
                post.likeSelectedUser.add(myId!!)
            }

            likeCount.text = post.like.toString()

            setPersonalButton()
        }
    }

    private fun setShowMoreVisible(post: Post, detailContent: TextView, showMore: TextView) {
        detailContent.post {
            if (detailContent.lineCount > detailContent.maxLines) showMore.visibility = View.VISIBLE
            else showMore.visibility = View.INVISIBLE
        }

        setShowMoreButton(post, detailContent, showMore)
    }

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

    private fun setLogOutButton() {
        tvDetailLogoutBtn.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            overridePendingTransition(R.anim.none, R.anim.fade_out)
        }
    }

    private fun setEditButton() {
        tvDetailEditBtn.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            intent.putExtra("editId", myId)
            profileRefresh.launch(intent)
            overridePendingTransition(R.anim.none, R.anim.fade_in)
        }
    }

}