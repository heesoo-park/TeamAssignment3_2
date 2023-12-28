package com.example.teamsns

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView

class ChooseProfileActivity : AppCompatActivity() {

    private val ibProfileSample1: ImageButton by lazy {
        findViewById(R.id.ib_choose_profile_sample1)
    }
    private val ibProfileSample2: ImageButton by lazy {
        findViewById(R.id.ib_choose_profile_sample2)
    }
    private val ibProfileSample3: ImageButton by lazy {
        findViewById(R.id.ib_choose_profile_sample3)
    }
    private val ibProfileSample4: ImageButton by lazy {
        findViewById(R.id.ib_choose_profile_sample4)
    }
    private val ibProfileSample5: ImageButton by lazy {
        findViewById(R.id.ib_choose_profile_sample5)
    }
    private val ibProfileSample6: ImageButton by lazy {
        findViewById(R.id.ib_choose_profile_sample6)
    }
    private val ibProfileSample7: ImageButton by lazy {
        findViewById(R.id.ib_choose_profile_sample7)
    }
    private val ibProfileSample8: ImageButton by lazy {
        findViewById(R.id.ib_choose_profile_sample8)
    }
    private val ibProfileSample9: ImageButton by lazy {
        findViewById(R.id.ib_choose_profile_sample9)
    }
    private val ibProfileSample10: ImageButton by lazy {
        findViewById(R.id.ib_choose_profile_sample10)
    }
    private val ibProfileSample11: ImageButton by lazy {
        findViewById(R.id.ib_choose_profile_sample11)
    }
    private val ibProfileSample12: ImageButton by lazy {
        findViewById(R.id.ib_choose_profile_sample12)
    }

    private val ivSelectedProfile: ImageView by lazy {
        findViewById(R.id.iv_choose_selected_profile)
    }

    private var selectedImageIdx: Int = 0

    private val btnSignUp: Button by lazy {
        findViewById(R.id.btn_choose_signup)
    }

    private val profileSampleButtonList: List<ImageButton> by lazy {
        listOf(
            ibProfileSample1,
            ibProfileSample2,
            ibProfileSample3,
            ibProfileSample4,
            ibProfileSample5,
            ibProfileSample6,
            ibProfileSample7,
            ibProfileSample8,
            ibProfileSample9,
            ibProfileSample10,
            ibProfileSample11,
            ibProfileSample12
        )
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