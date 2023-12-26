package com.example.teamsns

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.isVisible

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

    lateinit var detailImage: ImageView

    lateinit var detailContent: TextView

    lateinit var detailCommentIcon: ImageView

    lateinit var detailComment: TextView

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
        }
    }

    private fun setPostList() {
        for (post in userDate.userPosts.reversed()) {
            val postView: View = layoutInflater.inflate(R.layout.post_item, postLayout, false)

            detailImage = postView.findViewById(R.id.detail_activity_list_img)
            detailContent = postView.findViewById(R.id.detail_activity_list_contents)
            detailCommentIcon = postView.findViewById(R.id.detail_activity_comment_icon)
            detailComment = postView.findViewById(R.id.detail_activity_comment)

            detailContent.text = post.postContent

            detailImage.setImageResource(post.postImage)

            detailCommentIcon.setImageResource(post.commentIcon)

            detailComment.text = post.comment

            postLayout.addView(postView)
        }
    }

    private fun setLogOutButton(){
        val intent = Intent(this, SignInActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun setEditButton(){
        edit.setOnClickListener {
            val intent = Intent(this,ProfileEditActivity::class.java)
            intent.putExtra("editId",id)
            profileRefresh.launch(intent)
        }
    }
}