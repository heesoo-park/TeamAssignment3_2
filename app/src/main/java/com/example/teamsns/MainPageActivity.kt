package com.example.teamsns

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView

class MainPageActivity : AppCompatActivity() {
    private val ivMainMyProfile: ImageView by lazy { findViewById(R.id.iv_main_profile_btn) }
    private val ivMainUser1: ImageView by lazy { findViewById(R.id.iv_main_user1) }
    private val ivMainUser2: ImageView by lazy { findViewById(R.id.iv_main_user2) }
    private val ivMainUser3: ImageView by lazy { findViewById(R.id.iv_main_user3) }
    private val ivMainUser4: ImageView by lazy { findViewById(R.id.iv_main_user4) }
    private val tvMainHelloWord: TextView by lazy { findViewById(R.id.tv_main_hello_word) }

    private val profileList
        get() = listOf(
            ivMainMyProfile,
            ivMainUser1,
            ivMainUser2,
            ivMainUser3,
            ivMainUser4
        )

    private lateinit var loginUserID: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)

        loginUserID = intent.getStringExtra("id") ?: ""
        val loginUser = UserDatabase.getUser(loginUserID)

        tvMainHelloWord.text = getString(R.string.hello_word, loginUser.name)

        fun intent(activity:Activity){
            val intent=Intent(this, activity::class.java)
            startActivity(intent)
        }

        val iv_post1=findViewById<ImageView>(R.id.iv_post1)
        val iv_post2=findViewById<ImageView>(R.id.iv_post2)
        val iv_post3=findViewById<ImageView>(R.id.iv_post3)
        val iv_post4=findViewById<ImageView>(R.id.iv_post4)
        val like_num1=findViewById<TextView>(R.id.like1)
        val like_num2=findViewById<TextView>(R.id.like2)
        val like_num3=findViewById<TextView>(R.id.like3)
        val like_num4=findViewById<TextView>(R.id.like4)

        iv_post1.setOnClickListener {
            val num=like_num1.text.toString().toInt()+1
            like_num1.text=num.toString()
        }
        iv_post2.setOnClickListener {
            val num=like_num2.text.toString().toInt()+1
            like_num2.text=num.toString()
        }
        iv_post3.setOnClickListener {
            val num=like_num3.text.toString().toInt()+1
            like_num3.text=num.toString()
        }
        iv_post4.setOnClickListener {
            val num=like_num4.text.toString().toInt()+1
            like_num4.text=num.toString()
        }

    }

    fun setOnProflieClickListener() {
        profileList.forEach { iv ->
            iv.setOnClickListener {
                val intent = Intent(this@MainPageActivity, DetailPageActivity::class.java)
                when (iv.id) {
                    R.id.iv_main_profile_btn -> {
                        intent.putExtra("myId", loginUserID)
                        intent.putExtra("Id", loginUserID)
                    }
                    R.id.iv_main_user1 -> {
                        intent.putExtra("myId", loginUserID)
                        intent.putExtra("Id", UserDatabase.user1.id)
                    }
                    R.id.iv_main_user2 -> {
                        intent.putExtra("myId", loginUserID)
                        intent.putExtra("Id", UserDatabase.user2.id)
                    }
                    R.id.iv_main_user3 -> {
                        intent.putExtra("myId", loginUserID)
                        intent.putExtra("Id", UserDatabase.user3.id)
                    }
                    R.id.iv_main_user4 -> {
                        intent.putExtra("myId", loginUserID)
                        intent.putExtra("Id", UserDatabase.user4.id)
                    }
                }
                startActivity(intent)
            }
        }
    }
}