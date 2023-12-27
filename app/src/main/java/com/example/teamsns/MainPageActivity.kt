package com.example.teamsns

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout

class MainPageActivity : AppCompatActivity() {

    private val tvMainHelloWord: TextView by lazy { findViewById(R.id.tv_main_hello_word) }

    private val ivMainMyProfile: ImageView by lazy { findViewById(R.id.iv_main_profile_btn) }
    private val ivMainUser1: ImageView by lazy { findViewById(R.id.iv_main_user1) }
    private val ivMainUser2: ImageView by lazy { findViewById(R.id.iv_main_user2) }
    private val ivMainUser3: ImageView by lazy { findViewById(R.id.iv_main_user3) }
    private val ivMainUser4: ImageView by lazy { findViewById(R.id.iv_main_user4) }

    private val layoutMainPost1Upper: ConstraintLayout by lazy { findViewById(R.id.layout_main_post1_upper) }
    private val layoutMainPost2Upper: ConstraintLayout by lazy { findViewById(R.id.layout_main_post2_upper) }
    private val layoutMainPost3Upper: ConstraintLayout by lazy { findViewById(R.id.layout_main_post3_upper) }
    private val layoutMainPost4Upper: ConstraintLayout by lazy { findViewById(R.id.layout_main_post4_upper) }

    private val tvMainPost1Name: TextView by lazy { findViewById(R.id.tv_main_post1_name) }
    private val tvMainPost2Name: TextView by lazy { findViewById(R.id.tv_main_post2_name) }
    private val tvMainPost3Name: TextView by lazy { findViewById(R.id.tv_main_post3_name) }
    private val tvMainPost4Name: TextView by lazy { findViewById(R.id.tv_main_post4_name) }

    private val ivPost1: ImageView by lazy { findViewById(R.id.iv_post1) }
    private val ivPost2: ImageView by lazy { findViewById(R.id.iv_post2) }
    private val ivPost3: ImageView by lazy { findViewById(R.id.iv_post3) }
    private val ivPost4: ImageView by lazy { findViewById(R.id.iv_post4) }

    private val likeNum1: TextView by lazy { findViewById(R.id.like1) }
    private val likeNum2: TextView by lazy { findViewById(R.id.like2) }
    private val likeNum3: TextView by lazy { findViewById(R.id.like3) }
    private val likeNum4: TextView by lazy { findViewById(R.id.like4) }

    private val profileList
        get() = listOf(
            ivMainMyProfile,
            ivMainUser1,
            ivMainUser2,
            ivMainUser3,
            ivMainUser4,
            layoutMainPost1Upper,
            layoutMainPost2Upper,
            layoutMainPost3Upper,
            layoutMainPost4Upper
        )

    private val postImageList
        get() = listOf(
            ivPost1,
            ivPost2,
            ivPost3,
            ivPost4
        )

    private lateinit var loginUserID: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)

        // loginUserID = intent.getStringExtra("id") ?: ""
        UserDatabase.addUser(User("dk", "dk", "dkdkdk", R.drawable.img_cat1, "", arrayListOf()))
        loginUserID = "dk"
        val loginUser = UserDatabase.getUser(loginUserID)

        tvMainHelloWord.text = getString(R.string.hello_word, loginUser.name)

        setOnProflieClickListener()

        // likeNum1.text = UserDatabase.getUser(tvMainPost1Name.text.toString()).userPosts[0].like.toString()

//        ivPost1.setOnClickListener {
//            val num=like_num1.text.toString().toInt()+1
//            like_num1.text=num.toString()
//        }
//        ivPost2.setOnClickListener {
//            val num=like_num2.text.toString().toInt()+1
//            like_num2.text=num.toString()
//        }
//        ivPost3.setOnClickListener {
//            val num=like_num3.text.toString().toInt()+1
//            like_num3.text=num.toString()
//        }
//        ivPost4.setOnClickListener {
//            val num=like_num4.text.toString().toInt()+1
//            like_num4.text=num.toString()
//        }

    }

    private fun setOnProflieClickListener() {
        profileList.forEach { iv ->
            iv.setOnClickListener {
                when (iv.id) {
                    R.id.iv_main_profile_btn -> {
                        val intent = Intent(this@MainPageActivity, DetailPageActivity::class.java)
                        Log.d("dkj", loginUserID)
                        intent.putExtra("myId", loginUserID)
                        intent.putExtra("id", loginUserID)
                        startActivity(intent)
                    }
                    R.id.iv_main_user1, R.id.layout_main_post1_upper -> {
                        val intent = Intent(this@MainPageActivity, DetailPageActivity::class.java)
                        intent.putExtra("myId", loginUserID)
                        intent.putExtra("id", UserDatabase.totalUserData[0].id)
                        startActivity(intent)
                    }
                    R.id.iv_main_user2, R.id.layout_main_post2_upper -> {
                        intent.putExtra("myId", loginUserID)
                        intent.putExtra("id", UserDatabase.totalUserData[1].id)
                        startActivity(intent)
                    }
                    R.id.iv_main_user3, R.id.layout_main_post3_upper -> {
                        intent.putExtra("myId", loginUserID)
                        intent.putExtra("id", UserDatabase.totalUserData[2].id)
                        startActivity(intent)
                    }
                    R.id.iv_main_user4, R.id.layout_main_post4_upper -> {
                        intent.putExtra("myId", loginUserID)
                        intent.putExtra("id", UserDatabase.totalUserData[3].id)
                        startActivity(intent)
                    }
                }
            }
        }
    }
}