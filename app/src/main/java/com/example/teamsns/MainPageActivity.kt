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

    lateinit var detailImage: ImageView
    lateinit var detailContent: TextView
    lateinit var detailUserProfileIcon: ImageView
    lateinit var detailPostName: TextView
    lateinit var showMore: TextView

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
                overridePendingTransition(R.anim.none, R.anim.horizon_enter)
            }
        }
    }

    private fun setPostList() {
        for (user in UserDatabase.getTotalUser()) {
            for (post in user.userPosts.reversed()) {
                val postView: View =
                    inflater.inflate(R.layout.main_post_item, mainPostLayout, false)

                detailImage = postView.findViewById(R.id.iv_main_post)
                detailContent = postView.findViewById(R.id.tv_main_post_content)
                detailUserProfileIcon = postView.findViewById(R.id.iv_main_post_userprofile)
                detailPostName = postView.findViewById(R.id.tv_main_post_username)
                val tempLikeButton: ImageView = postView.findViewById(R.id.btn_main_post_like)
                val tempLikeCount: TextView = postView.findViewById(R.id.tv_main_post_like_count)
                showMore = postView.findViewById(R.id.tv_main_post_show_more)

                detailContent.text = post.postContent

                detailImage.setImageResource(post.postImage)

                detailUserProfileIcon.setImageResource(post.userProfileImage)

                detailPostName.text = user.name

                mainPostLayout.addView(postView)

                if (post.likeSelectedUser.any { it == loginUserID }) {
                    tempLikeButton.setImageResource(R.drawable.heart)
                }

                tempLikeCount.text = post.like.toString()

                setLikeButton(post, tempLikeButton, tempLikeCount)
                setShowMoreVisible(post)
                setOnNameLayoutClickListener(user)
            }
        }
    }

    private fun setOnNameLayoutClickListener(user: User) {
        detailUserProfileIcon.setOnClickListener {
            val intent = Intent(this@MainPageActivity, DetailPageActivity::class.java)
            intent.putExtra("myId", loginUserID)
            intent.putExtra("id", user.id)
            startActivity(intent)
        }
    }


    private fun setLikeButton(post: Post, templikeButton: ImageView, templikeCount: TextView) {
        templikeButton.setOnClickListener {
            if (post.likeSelectedUser.any { it == loginUserID }) {
                post.like -= 1
                templikeButton.setImageResource(R.drawable.empty_heart)
                post.likeSelectedUser.remove(loginUserID)
            } else {
                post.like += 1
                templikeButton.setImageResource(R.drawable.heart)
                post.likeSelectedUser.add(loginUserID)
            }
            templikeCount.text = post.like.toString()
        }
    }

    private fun setShowMoreVisible(post: Post) {
        detailContent.text = post.postContent

        if (detailContent.lineCount > detailContent.maxLines) showMore.visibility = View.VISIBLE
        else showMore.visibility = View.INVISIBLE

        setShowMoreButton(post)
    }

    private fun setShowMoreButton(post: Post) {
        detailContent.text = post.postContent
        showMore.setOnClickListener {
            if (detailContent.maxLines == Integer.MAX_VALUE) {
                detailContent.maxLines = 2
                showMore.setText(DetailPageMessage.SHOWMORE.message)
            } else {
                detailContent.maxLines = Integer.MAX_VALUE
                showMore.setText(DetailPageMessage.SHOWCLOSE.message)
            }
        }
    }
}