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

    private val tvMainHelloWord: TextView by lazy { findViewById(R.id.tv_main_hello_word) }

    private val ivMainMyProfile: ImageView by lazy { findViewById(R.id.iv_main_profile_btn) }
    private val ivMainUser1: ImageView by lazy { findViewById(R.id.iv_main_user1_btn) }
    private val ivMainUser2: ImageView by lazy { findViewById(R.id.iv_main_user2_btn) }
    private val ivMainUser3: ImageView by lazy { findViewById(R.id.iv_main_user3_btn) }
    private val ivMainUser4: ImageView by lazy { findViewById(R.id.iv_main_user4_btn) }

    private val profileList
        get() = listOf(
            ivMainMyProfile,
            ivMainUser1,
            ivMainUser2,
            ivMainUser3,
            ivMainUser4,
        )

    private lateinit var loginUserID: String

    private val inflater: LayoutInflater by lazy {
        LayoutInflater.from(this)
    }

    private lateinit var userData: User

    private val mainPostLayout: LinearLayout by lazy {
        findViewById(R.id.layout_main_postlist)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)

        loginUserID = intent.getStringExtra("id") ?: "test1"
        userData = UserDatabase.getUser(loginUserID)!!

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
                        intent.putExtra("myId", loginUserID)
                        intent.putExtra("id", loginUserID)
                    }

                    R.id.iv_main_user1_btn -> {
                        intent.putExtra("myId", loginUserID)
                        intent.putExtra("id", UserDatabase.totalUserData[0].id)
                    }

                    R.id.iv_main_user2_btn -> {
                        intent.putExtra("myId", loginUserID)
                        intent.putExtra("id", UserDatabase.totalUserData[1].id)
                    }

                    R.id.iv_main_user3_btn -> {
                        intent.putExtra("myId", loginUserID)
                        intent.putExtra("id", UserDatabase.totalUserData[2].id)
                    }

                    R.id.iv_main_user4_btn -> {
                        intent.putExtra("myId", loginUserID)
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
                    inflater.inflate(R.layout.main_post_item, mainPostLayout, false)

                val detailImage: ImageView = postView.findViewById(R.id.iv_main_post)
                val detailContent: TextView = postView.findViewById(R.id.tv_main_post_content)
                val detailUserProfileIcon: ImageView = postView.findViewById(R.id.iv_main_post_userprofile)
                val detailPostName: TextView = postView.findViewById(R.id.tv_main_post_username)
                val likeButton: ImageView = postView.findViewById(R.id.iv_main_post_like_btn)
                val likeCount: TextView = postView.findViewById(R.id.tv_main_post_like_count)
                val showMore: TextView = postView.findViewById(R.id.tv_main_post_show_more)

                detailContent.text = post.postContent

                detailImage.setImageResource(post.postImage)

                detailUserProfileIcon.setImageResource(post.userProfileImage)

                detailPostName.text = user.name

                mainPostLayout.addView(postView)

                if (post.likeSelectedUser.any { it == loginUserID }) {
                    likeButton.setImageResource(R.drawable.heart)
                }

                likeCount.text = post.like.toString()

                setLikeButton(post, likeButton, likeCount, detailImage)
                setShowMoreVisible(post, detailContent, showMore)
                setOnProfileIconClickListener(user, detailUserProfileIcon)
            }
        }
    }

    private fun setOnProfileIconClickListener(user: User, detailUserProfileIcon: ImageView) {
        detailUserProfileIcon.setOnClickListener {
            val intent = Intent(this@MainPageActivity, DetailPageActivity::class.java)
            intent.putExtra("myId", loginUserID)
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
        if (post.likeSelectedUser.any { it == loginUserID }) {
            post.like -= 1
            click.setImageResource(R.drawable.empty_heart)
            post.likeSelectedUser.remove(loginUserID)
        } else {
            post.like += 1
            click.setImageResource(R.drawable.heart)
            post.likeSelectedUser.add(loginUserID)
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