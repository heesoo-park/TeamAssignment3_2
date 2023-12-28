package com.example.teamsns

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView

class ChooseProfileActivity : AppCompatActivity() {

    private val profileSampleIds = listOf(
        R.id.ib_choose_profile_sample1,
        R.id.ib_choose_profile_sample2,
        R.id.ib_choose_profile_sample3,
        R.id.ib_choose_profile_sample4,
        R.id.ib_choose_profile_sample5,
        R.id.ib_choose_profile_sample6,
        R.id.ib_choose_profile_sample7,
        R.id.ib_choose_profile_sample8,
        R.id.ib_choose_profile_sample9,
        R.id.ib_choose_profile_sample10,
        R.id.ib_choose_profile_sample11,
        R.id.ib_choose_profile_sample12,
    )

    private val ivSelectedProfile: ImageView by lazy {
        findViewById(R.id.iv_choose_selected_profile)
    }

    private var selectedImageIdx: Int = 0

    private val btnSignUp: Button by lazy {
        findViewById(R.id.btn_choose_signup)
    }

    private val profileSampleButtonList: List<ImageButton> by lazy {
        profileSampleIds.map { findViewById(it) }
    }

    private val profileImageList = listOf(
        R.drawable.img_dog1,
        R.drawable.img_dog2,
        R.drawable.img_dog3,
        R.drawable.img_dog4,
        R.drawable.img_dog5,
        R.drawable.img_dog6,
        R.drawable.img_cat1,
        R.drawable.img_cat2,
        R.drawable.img_cat3,
        R.drawable.img_cat4,
        R.drawable.img_cat5,
        R.drawable.img_cat6,
    )

    private lateinit var name: String
    private lateinit var id: String
    private lateinit var password: String
    private val statusMessage: String = ""
    private val userPosts: ArrayList<Post> = arrayListOf()

    private lateinit var editId: String
    private lateinit var userData: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_profile)

        name = intent.getStringExtra("name") ?: ""
        id = intent.getStringExtra("id") ?: ""
        password = intent.getStringExtra("password") ?: ""


        setEditCheck()
        setOnClickListener()
    }

    // 편집페이지로 사용하는지 체크하는 함수
    private fun setEditCheck() {
        if (intent.getStringExtra("editId") != null) {
            editId = intent.getStringExtra("editId")!!
            userData = UserDatabase.getUser(editId)!!
            btnSignUp.setText(R.string.edit_do)
            ivSelectedProfile.setImageResource(userData.profileImage)
        }
    }

    // 클릭 리스너를 모아둔 함수
    private fun setOnClickListener() {
        profileSampleButtonList.forEachIndexed { idx, ib ->
            ib.setOnClickListener {
                selectedImageIdx = idx
                ivSelectedProfile.setImageResource(profileImageList[idx])
            }
        }

        btnSignUp.setOnClickListener {
            if (intent.getStringExtra("editId") != null) {
                UserDatabase.totalUserData.find { it.id == editId }
                    .let { it!!.profileImage = profileImageList[selectedImageIdx] }
            }else {
                UserDatabase.addUser(
                    User(
                        name,
                        id,
                        password,
                        profileImageList[selectedImageIdx],
                        statusMessage,
                        userPosts
                    )
                )
            }
            setResult(RESULT_OK, intent)
            finish()
            overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left)
        }
    }
}