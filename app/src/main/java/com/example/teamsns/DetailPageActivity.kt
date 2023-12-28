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

    lateinit var userDate:User

    lateinit var name: String

    lateinit var statusMessage: String

    private val back: ImageView by lazy {
        findViewById(R.id.iv_detail_back_btn)
    }

    private val edit: TextView by lazy {
        findViewById(R.id.tv_detail_edit_btn)
    }

    private val idTextView: TextView by lazy {
        findViewById(R.id.tv_detail_id)
    }

    private val nameTextView: TextView by lazy {
        findViewById(R.id.tv_detail_name)
    }

    private val statusMessageTextView: TextView by lazy {
        findViewById(R.id.tv_detail_status_message)
    }

    private val logOut: TextView by lazy {
        findViewById(R.id.tv_detail_logout_btn)
    }

    private val profileImageView: AppCompatImageView by lazy {
        findViewById(R.id.iv_detail_profile)
    }

    private val myPageDetail: TextView by lazy {
        findViewById(R.id.tv_detail_my_page_or_detail)
    }

    private lateinit var detailImage: ImageView
    private lateinit var detailContent: TextView
    private lateinit var detailCommentIcon: ImageView
    private lateinit var detailComment: TextView
    private lateinit var likeButton: ImageView
    private lateinit var likeCount: TextView
    private lateinit var showMore: TextView

    private val inflater: LayoutInflater by lazy {
        LayoutInflater.from(this)
    }

    private val postLayout: LinearLayout by lazy {
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
        userDate = UserDatabase.getUser(id!!)!!
        if (myId == id) myPageDetail.setText(DetailPageMessage.MYPAGE.message)
        else myPageDetail.setText(DetailPageMessage.DETAIL.message)
        name = userDate.name
        statusMessage = userDate.statusMessage.toString()
        profileImageView.setImageResource(userDate.profileImage)
        idTextView.setText(id)
        nameTextView.setText(name)
        statusMessageTextView.setText(statusMessage)

        postLayout.removeAllViews()
        setPostList()
    }

    private fun setPersonalButton() {
        val visibleBoolean = myId == id
        edit.isVisible = visibleBoolean
        logOut.isVisible = visibleBoolean

        setLogOutButton()
        setEditButton()
    }

    private fun setBackButton() {
        back.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right)
        }
    }

    private fun setPostList() {
        for (post in userDate.userPosts.reversed()) {
            val postView: View = inflater.inflate(R.layout.post_item, postLayout, false)

            val detailImage: ImageView = postView.findViewById(R.id.iv_detail_post_list_img)
            val detailContent: TextView = postView.findViewById(R.id.tv_detail_post_list_contents)
            val detailCommentIcon: ImageView = postView.findViewById(R.id.iv_detail_post_comment_icon)
            val detailComment: TextView = postView.findViewById(R.id.tv_detail_post_comment)
            val likeButton: ImageView = postView.findViewById(R.id.iv_detail_post_like_btn)
            val likeCount: TextView = postView.findViewById(R.id.tv_detail_post_like_count)
            val showMore: TextView = postView.findViewById(R.id.tv_detail_post_show_more)

            detailContent.text = post.postContent

            detailImage.setImageResource(post.postImage)

            detailCommentIcon.setImageResource(post.commentIcon)

            detailComment.text = post.comment

            postLayout.addView(postView)

            if (post.likeSelectedUser.any { it == myId }) {
                likeButton.setImageResource(heart)
            }

            likeCount.text = post.like.toString()

            setLikeButton(post, likeButton, likeCount)
            setShowMoreVisible(post, detailContent, showMore)
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
        logOut.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            overridePendingTransition(R.anim.none, R.anim.fade_out)
        }
    }

    private fun setEditButton() {
        edit.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            intent.putExtra("editId", myId)
            profileRefresh.launch(intent)
            overridePendingTransition(R.anim.slide_in_from_right, R.anim.fade_out)
        }
    }

}