package com.example.teamsns

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView

class ChooseProfileActivity : AppCompatActivity() {

    private val ibProfile1: ImageButton by lazy { findViewById(R.id.ib_choose_profile_sample1) }
    private val ibProfile2: ImageButton by lazy { findViewById(R.id.ib_choose_profile_sample2) }
    private val ibProfile3: ImageButton by lazy { findViewById(R.id.ib_choose_profile_sample3) }
    private val ibProfile4: ImageButton by lazy { findViewById(R.id.ib_choose_profile_sample4) }
    private val ibProfile5: ImageButton by lazy { findViewById(R.id.ib_choose_profile_sample5) }
    private val ibProfile6: ImageButton by lazy { findViewById(R.id.ib_choose_profile_sample6) }
    private val ibProfile7: ImageButton by lazy { findViewById(R.id.ib_choose_profile_sample7) }
    private val ibProfile8: ImageButton by lazy { findViewById(R.id.ib_choose_profile_sample8) }
    private val ibProfile9: ImageButton by lazy { findViewById(R.id.ib_choose_profile_sample9) }
    private val ibProfile10: ImageButton by lazy { findViewById(R.id.ib_choose_profile_sample10) }
    private val ibProfile11: ImageButton by lazy { findViewById(R.id.ib_choose_profile_sample11) }
    private val ibProfile12: ImageButton by lazy { findViewById(R.id.ib_choose_profile_sample12) }

    private val ivSelectedImage: ImageView by lazy { findViewById(R.id.iv_choose_selected_profile) }
    private var selectedImage: Int = 0

    private val btnNext: Button by lazy { findViewById(R.id.btn_choose_signup) }

    private val profileImageButtonList: List<ImageButton> by lazy { listOf(
        ibProfile1,
        ibProfile2,
        ibProfile3,
        ibProfile4,
        ibProfile5,
        ibProfile6,
        ibProfile7,
        ibProfile8,
        ibProfile9,
        ibProfile10,
        ibProfile11,
        ibProfile12
    ) }

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_profile)

        name = intent.getStringExtra("name") ?: ""
        id = intent.getStringExtra("id") ?: ""
        password = intent.getStringExtra("password") ?: ""

        setOnClickListener()
    }

    private fun setOnClickListener() {
        profileImageButtonList.forEachIndexed { idx, ib ->
            ib.setOnClickListener {
                selectedImage = idx
                ivSelectedImage.setImageResource(profileImageList[idx])
            }
        }

        btnNext.setOnClickListener {
            UserDatabase.addUser(User(name, id, password, profileImageList[selectedImage], statusMessage, userPosts))
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}