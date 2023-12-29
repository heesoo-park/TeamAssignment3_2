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

class MainPageActivity : AppCompatActivity() {

    private val profileRefresh =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) init()
        }

    private val tvMainHelloWord: TextView by lazy {
        findViewById(R.id.tv_main_hello_word)
    }
    private val ivMainMyProfile: ImageView by lazy {
        findViewById(R.id.iv_main_profile_btn)
    }

    private lateinit var loginUserId: String

    private val inflater: LayoutInflater by lazy {
        LayoutInflater.from(this)
    }

    private lateinit var userData: User

    private val mainPostList: LinearLayout by lazy {
        findViewById(R.id.layout_main_post_list)
    }
    private val mainUserProfileList: LinearLayout by lazy {
        findViewById(R.id.layout_main_user_profile_list)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)

        loginUserId = intent.getStringExtra("id")!!
        userData = UserDatabase.getUser(loginUserId)!!


        init()
    }

    private fun init(){
        tvMainHelloWord.text = getString(R.string.hello_word, userData.name)
        ivMainMyProfile.setImageResource(userData.profileImage!!)
        mainUserProfileList.removeAllViews()
        setUserProfileList()
        setTopbar()
    }

    private fun setTopbar() {
        ivMainMyProfile.setImageResource(userData.profileImage)

        setOnProflieClickListener(userData,ivMainMyProfile)
    }

    // 사용자 프로필 세팅
    private fun setUserProfileList() {
        for (user in UserDatabase.totalUserData) {
            val profileView: View =
                inflater.inflate(R.layout.profile_item, mainUserProfileList, false)
            val profileImg: ImageView = profileView.findViewById(R.id.iv_main_user_profile)
            val profileStroke: ImageView = profileView.findViewById(R.id.iv_main_user_stroke)
            profileImg.setImageResource(user.profileImage)
            if (loginUserId != user.id) {
                profileStroke.setImageResource(R.drawable.shape_profile_image_stroke2)
            }else {
                profileStroke.setImageResource(R.drawable.shape_profile_image_stroke)
            }
            mainUserProfileList.addView(profileView)
            setOnProflieClickListener(user, profileImg)
            mainPostList.removeAllViews()
            setPostList()
        }
    }

    // 사용자 프로필 이미지 클릭 리스너 함수
    private fun setOnProflieClickListener(user: User, button: ImageView) {
        button.setOnClickListener {
            val intent = Intent(this@MainPageActivity, DetailPageActivity::class.java)
            intent.putExtra("myId", loginUserId)
            intent.putExtra("id", user.id)
            profileRefresh.launch(intent)
            overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left)
        }
    }

    // 게시물 목록을 세팅하는 함수
    private fun setPostList() {
        for (user in UserDatabase.getTotalUser()) {
            for (post in user.userPosts.reversed()) {
                val postView: View =
                    inflater.inflate(R.layout.main_post_item, mainPostList, false)

                val ivMainPost: ImageView = postView.findViewById(R.id.iv_main_post)
                val tvMainPostContent: TextView = postView.findViewById(R.id.tv_main_post_content)
                val ivMainPostUserProfile: ImageView = postView.findViewById(R.id.iv_main_post_user_profile)
                val tvMainPostUserName: TextView = postView.findViewById(R.id.tv_main_post_user_name)
                val ivMainPostLikeBtn: ImageView = postView.findViewById(R.id.iv_main_post_like_btn)
                val tvMainPostLikeCount: TextView = postView.findViewById(R.id.tv_main_post_like_count)
                val tvMainPostShowMore: TextView = postView.findViewById(R.id.tv_main_post_show_more)
                val ivDetailPostLeftArrow: ImageView = postView.findViewById(R.id.iv_left_arrow_button)
                val ivDetailPostRightArrow: ImageView = postView.findViewById(R.id.iv_right_arrow_button)
                val currentImageIndex = 0

                tvMainPostContent.text = post.postContent
                ivMainPost.setImageResource(post.postImage[0])
                ivMainPostUserProfile.setImageResource(post.userProfileImage)
                tvMainPostUserName.text = user.name
                tvMainPostLikeCount.text = post.like.toString()

                mainPostList.addView(postView)

                if (post.likeSelectedUser.any { it == loginUserId }) {
                    ivMainPostLikeBtn.setImageResource(R.drawable.img_heart)
                }

                tvMainPostLikeCount.text = post.like.toString()

                setLikeButton(post, ivMainPostLikeBtn, tvMainPostLikeCount, ivMainPost)
                setShowMoreVisible(post, tvMainPostContent, tvMainPostShowMore)
                setOnProfileImageClickListener(user, ivMainPostUserProfile)
                setShowPostArrow(post, ivDetailPostLeftArrow, ivDetailPostRightArrow,ivMainPost,currentImageIndex)
            }
        }
    }

    // 게시물 내 프로필 이미지 클릭 리스너 함수
    private fun setOnProfileImageClickListener(user: User, detailUserProfileIcon: ImageView) {
        detailUserProfileIcon.setOnClickListener {
            val intent = Intent(this@MainPageActivity, DetailPageActivity::class.java)
            intent.putExtra("myId", loginUserId)
            intent.putExtra("id", user.id)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left)
        }
    }

    // 게시물 내 좋아요 기능을 담당하는 클릭 리스너 함수
    private fun setLikeButton(post: Post, likeButton: ImageView, likeCount: TextView, detailImage:ImageView) {
        likeButton.setOnClickListener {
            likeShow(post, likeButton, likeCount)
        }
        detailImage.setOnLongClickListener {
            likeShow(post,likeButton, likeCount)
            true
        }
    }

    // 게시물 내 좋아요 숫자 세팅하는 함수
    private fun likeShow(post: Post, click: ImageView, likeCount: TextView){
        if (post.likeSelectedUser.any { it == loginUserId }) {
            post.like -= 1
            click.setImageResource(R.drawable.img_empty_heart)
            post.likeSelectedUser.remove(loginUserId)
        } else {
            post.like += 1
            click.setImageResource(R.drawable.img_heart)
            post.likeSelectedUser.add(loginUserId)
        }
        likeCount.text = post.like.toString()
    }

    // 게시물 내 더보기 버튼 활성화 함수
    private fun setShowMoreVisible(post: Post, detailContent: TextView, showMore: TextView) {
        detailContent.post {
            if (detailContent.lineCount > detailContent.maxLines) showMore.visibility = View.VISIBLE
            else showMore.visibility = View.INVISIBLE
        }

        setShowMoreButton(post, detailContent, showMore)
    }

    // 게시물 내 더보기 버튼 기능 세팅 함수
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
}