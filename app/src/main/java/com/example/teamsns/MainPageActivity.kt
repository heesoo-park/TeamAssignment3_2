package com.example.teamsns

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.example.teamsns.R.id.iv_profile1

class MainPageActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)

        fun intent(activity:Activity){
            val intent=Intent(this, activity::class.java)
            startActivity(intent)
        }

        val iv_informaiton=findViewById<ImageView>(R.id.imageView2)
        val iv_profile1=findViewById<ImageView>(R.id.iv_profile1)
        val iv_profile2=findViewById<ImageView>(R.id.iv_profile2)
        val iv_profile3=findViewById<ImageView>(R.id.iv_profile3)
        val iv_profile4=findViewById<ImageView>(R.id.iv_profile4)
        val iv_post1=findViewById<ImageView>(R.id.iv_post1)
        val iv_post2=findViewById<ImageView>(R.id.iv_post2)
        val iv_post3=findViewById<ImageView>(R.id.iv_post3)
        val iv_post4=findViewById<ImageView>(R.id.iv_post4)
        val like_num1=findViewById<TextView>(R.id.like1)
        val like_num2=findViewById<TextView>(R.id.like2)
        val like_num3=findViewById<TextView>(R.id.like3)
        val like_num4=findViewById<TextView>(R.id.like4)

        iv_informaiton.setOnClickListener{
            intent(DetailPageActivity())
        }
        iv_profile1.setOnClickListener{
            intent(DetailPageActivity())
        }
        iv_profile2.setOnClickListener{
            intent(DetailPageActivity())
        }
        iv_profile3.setOnClickListener{
            intent(DetailPageActivity())
        }
        iv_profile4.setOnClickListener{
            intent(DetailPageActivity())
        }

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
}