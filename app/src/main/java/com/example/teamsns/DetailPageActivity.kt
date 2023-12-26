package com.example.teamsns

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
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

    private val userDate = UserDatabase.getUser(id!!)

    lateinit var name: String

    lateinit var statusMessage: String

    private val back: ImageView by lazy {
        findViewById(R.id.back)
    }

    private val edit: TextView by lazy {
        findViewById(R.id.edit)
    }

    private val idTextView: TextView by lazy {
        findViewById(R.id.detail_activity_id)
    }

    private val nameTextView: TextView by lazy {
        findViewById(R.id.detail_activity_name)
    }

    private val statusMessageTextView: TextView by lazy {
        findViewById(R.id.detail_activity_status_message)
    }

    private val logOut: TextView by lazy {
        findViewById(R.id.logout)
    }

    private val profileImageView: AppCompatImageView by lazy {
        findViewById(R.id.profile_img)
    }

    private val myPageDetail: TextView by lazy {
        findViewById(R.id.my_page_or_detail)
    }

    lateinit var detailImage: ImageView

    lateinit var detailContent: TextView

    lateinit var detailCommentIcon: ImageView

    lateinit var detailComment: TextView

    lateinit var likeButton: ImageView

    lateinit var likeCount: TextView

    private val layoutInflater: LayoutInflater by lazy {
        LayoutInflater.from(this)
    }

    private val postLayout: LinearLayout by lazy {
        findViewById(R.id.post_layout)
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
        if (myId == id) myPageDetail.setText(getString(R.string.detail_activity_my_page))
        name = userDate.name
        statusMessage = userDate.statusMessage.toString()
        profileImageView.setImageResource(userDate.profileImage)
        idTextView.setText(id)
        nameTextView.setText(name)
        statusMessageTextView.setText(statusMessage)

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
            overridePendingTransition(R.anim.none, R.anim.horizon_enter)
        }
    }

    private fun setPostList() {
        for (post in userDate.userPosts.reversed()) {
            val postView: View = layoutInflater.inflate(R.layout.post_item, postLayout, false)

            detailImage = postView.findViewById(R.id.detail_activity_list_img)
            detailContent = postView.findViewById(R.id.detail_activity_list_contents)
            detailCommentIcon = postView.findViewById(R.id.detail_activity_comment_icon)
            detailComment = postView.findViewById(R.id.detail_activity_comment)
            likeButton = postView.findViewById(R.id.like_button)
            likeCount = postView.findViewById(R.id.like_count)

            detailContent.text = post.postContent

            detailImage.setImageResource(post.postImage)

            detailCommentIcon.setImageResource(post.commentIcon)

            detailComment.text = post.comment

            postLayout.addView(postView)

            if (post.likeSelectedUser?.any { it == myId } == true){
                likeButton.setImageResource(heart)
            }

            likeCount.text = post.like.toString()

            setLikeButton(post)
        }
    }

    private fun  setLikeButton(post: Post){
        likeButton.setOnClickListener {
            if (post.likeSelectedUser?.any { it == myId } == true){
                post.like -= 1
                likeButton.setImageResource(empty_heart)
                post.likeSelectedUser!!.remove(myId)
            }else{
                post.like += 1
                likeButton.setImageResource(heart)
                post.likeSelectedUser?.add(myId!!)
            }
            likeCount.text = post.like.toString()
        }
    }
    private fun setLogOutButton(){
        val intent = Intent(this, SignInActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        overridePendingTransition(R.anim.none, R.anim.fade_out)
    }

    private fun setEditButton(){
        edit.setOnClickListener {
            val intent = Intent(this,SignUpActivity::class.java)
            intent.putExtra("editId",myId)
            profileRefresh.launch(intent)
            overridePendingTransition(R.anim.none, R.anim.fade_in)
        }
    }
}