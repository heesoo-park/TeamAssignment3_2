package com.example.teamsns

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

class MainPageActivity : AppCompatActivity() {

    private val tvMainHelloWord: TextView by lazy {
        findViewById(R.id.tv_main_hello_word)
    }

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

    private lateinit var loginUserId: String

    private val inflater: LayoutInflater by lazy {
        LayoutInflater.from(this)
    }

    private lateinit var userData: User

    private val mainPostList: LinearLayout by lazy {
        findViewById(R.id.layout_main_postlist)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)

        loginUserId = intent.getStringExtra("id")!!
        userData = UserDatabase.getUser(loginUserId)!!

        tvMainHelloWord.text = getString(R.string.hello_word, userData.name)
        ivMainMyProfile.setImageResource(userData.profileImage!!)

        setOnProflieClickListener()
        setPostList()
    }

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

    private fun setPostList() {
        for (user in UserDatabase.getTotalUser()) {
            for (post in user.userPosts.reversed()) {
                val postView: View =
                    inflater.inflate(R.layout.main_post_item, mainPostList, false)

                val ivMainPost: ImageView = postView.findViewById(R.id.iv_main_post)
                val tvMainPostContent: TextView = postView.findViewById(R.id.tv_main_post_content)
                val ivMainPostUserProfile: ImageView = postView.findViewById(R.id.iv_main_post_userprofile)
                val tvMainPostUserName: TextView = postView.findViewById(R.id.tv_main_post_username)
                val ivMainPostLikeBtn: ImageView = postView.findViewById(R.id.iv_main_post_like_btn)
                val tvMainPostLikeCount: TextView = postView.findViewById(R.id.tv_main_post_like_count)
                val tvMainPostShowMore: TextView = postView.findViewById(R.id.tv_main_post_show_more)

                tvMainPostContent.text = post.postContent

                ivMainPost.setImageResource(post.postImage)

                ivMainPostUserProfile.setImageResource(post.userProfileImage)

                tvMainPostUserName.text = user.name

                mainPostList.addView(postView)

                if (post.likeSelectedUser.any { it == loginUserId }) {
                    ivMainPostLikeBtn.setImageResource(R.drawable.heart)
                }

                tvMainPostLikeCount.text = post.like.toString()

                setLikeButton(post, ivMainPostLikeBtn, tvMainPostLikeCount, ivMainPost)
                setShowMoreVisible(post, tvMainPostContent, tvMainPostShowMore)
                setOnProfileIconClickListener(user, ivMainPostUserProfile)
            }
        }
    }

    private fun setOnProfileIconClickListener(user: User, detailUserProfileIcon: ImageView) {
        detailUserProfileIcon.setOnClickListener {
            val intent = Intent(this@MainPageActivity, DetailPageActivity::class.java)
            intent.putExtra("myId", loginUserId)
            intent.putExtra("id", user.id)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left)
        }
    }

    private fun setLikeButton(post: Post, likeButton: ImageView, likeCount: TextView, detailImage:ImageView) {
        likeButton.setOnClickListener {
            likeShow(post, likeButton, likeCount)
        }
        detailImage.setOnLongClickListener {
            likeShow(post,likeButton, likeCount)
            true
        }
    }
    private fun likeShow(post: Post, click: ImageView, likeCount: TextView){
        if (post.likeSelectedUser.any { it == loginUserId }) {
            post.like -= 1
            click.setImageResource(R.drawable.empty_heart)
            post.likeSelectedUser.remove(loginUserId)
        } else {
            post.like += 1
            click.setImageResource(R.drawable.heart)
            post.likeSelectedUser.add(loginUserId)
        }
        likeCount.text = post.like.toString()
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
}