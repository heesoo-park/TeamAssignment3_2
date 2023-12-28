package com.example.teamsns

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
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
    private val postLaout: LinearLayout by lazy {
        findViewById(R.id.post_comment_layout)
    }
    private val id: String? by lazy {
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_pop_up)

        init()
    }

    private fun init(){
        setPost()
    }
    // 프로필 기본 세팅
    private fun setPost(){
        ivUserProfile.setImageResource(editUserData!!.profileImage)
        tvUserName.text = editUserData!!.name
        ivPostImage.setImageResource(postData!!.postImage[0])
        tvPostContents.text = postData!!.postContent
        if (postData!!.likeSelectedUser.any { it == id }) {
            ivLike.setImageResource(R.drawable.img_heart)
        }
        setLikeButton()
    }


}