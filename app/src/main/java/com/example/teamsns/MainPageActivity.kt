package com.example.teamsns

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

class MainPageActivity : AppCompatActivity() {

    private val tvMainHelloWord: TextView by lazy { findViewById(R.id.tv_main_hello_word) }

    private val ivMainMyProfile: ImageView by lazy { findViewById(R.id.iv_main_profile_btn) }
    private val ivMainUser1: ImageView by lazy { findViewById(R.id.iv_main_user1) }
    private val ivMainUser2: ImageView by lazy { findViewById(R.id.iv_main_user2) }
    private val ivMainUser3: ImageView by lazy { findViewById(R.id.iv_main_user3) }
    private val ivMainUser4: ImageView by lazy { findViewById(R.id.iv_main_user4) }

    lateinit var detailImage: ImageView
    lateinit var detailContent: TextView
    lateinit var detailUserProfileIcon: ImageView
    lateinit var detailPostName: TextView
    lateinit var likeButton: ImageView
    lateinit var likeCount: TextView
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
        findViewById(R.id.main_post_layout)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)

        loginUserID = intent.getStringExtra("id") ?: "test3"
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
                    R.id.iv_main_user1 -> {
                        intent.putExtra("myId", loginUserID)
                        intent.putExtra("id", UserDatabase.totalUserData[0].id)
                    }
                    R.id.iv_main_user2 -> {
                        intent.putExtra("myId", loginUserID)
                        intent.putExtra("id", UserDatabase.totalUserData[1].id)
                    }
                    R.id.iv_main_user3 -> {
                        intent.putExtra("myId", loginUserID)
                        intent.putExtra("id", UserDatabase.totalUserData[2].id)
                    }
                    R.id.iv_main_user4 -> {
                        intent.putExtra("myId", loginUserID)
                        intent.putExtra("id", UserDatabase.totalUserData[3].id)
                    }
                }
                startActivity(intent)
            }
        }
    }

    private fun setPostList() {
        for (user in UserDatabase.getTotalUser()) {
            for (post in user.userPosts?.reversed()!!) {
                val postView: View = inflater.inflate(R.layout.main_post_item, mainPostLayout, false)

                detailImage = postView.findViewById(R.id.main_activity_list_img)
                detailContent = postView.findViewById(R.id.main_activity_list_contents)
                detailUserProfileIcon = postView.findViewById(R.id.main_activity_user_profile)
                detailPostName = postView.findViewById(R.id.main_activity_user_name)
                likeButton = postView.findViewById(R.id.main_like_button)
                likeCount = postView.findViewById(R.id.main_like_count)
                showMore = postView.findViewById(R.id.main_show_more)

                detailContent.text = post.postContent

                detailImage.setImageResource(post.postImage)

                detailUserProfileIcon.setImageResource(post.userProfileImage)

                detailPostName.text = user.name

                mainPostLayout.addView(postView)

                if (post.likeSelectedUser.any { it == loginUserID }) {
                    likeButton.setImageResource(R.drawable.heart)
                }

                likeCount.text = post.like.toString()

                setLikeButton(post)
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


    private fun setLikeButton(post: Post) {
        likeButton.setOnClickListener {
            Log.e("user", post.likeSelectedUser.toString())
            if (post.likeSelectedUser.any { it == loginUserID }) {
                post.like -= 1
                likeButton.setImageResource(R.drawable.empty_heart)
                post.likeSelectedUser.remove(loginUserID)
            } else {
                post.like += 1
                likeButton.setImageResource(R.drawable.heart)
                post.likeSelectedUser.add(loginUserID!!)
            }
            likeCount.text = post.like.toString()
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