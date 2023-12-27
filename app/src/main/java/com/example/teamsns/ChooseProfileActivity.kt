package com.example.teamsns

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView

class ChooseProfileActivity : AppCompatActivity() {

    private val ibProfile1: ImageButton by lazy { findViewById(R.id.ib_profile1) }
    private val ibProfile2: ImageButton by lazy { findViewById(R.id.ib_profile2) }
    private val ibProfile3: ImageButton by lazy { findViewById(R.id.ib_profile3) }
    private val ibProfile4: ImageButton by lazy { findViewById(R.id.ib_profile4) }
    private val ibProfile5: ImageButton by lazy { findViewById(R.id.ib_profile5) }
    private val ibProfile6: ImageButton by lazy { findViewById(R.id.ib_profile6) }
    private val ibProfile7: ImageButton by lazy { findViewById(R.id.ib_profile7) }
    private val ibProfile8: ImageButton by lazy { findViewById(R.id.ib_profile8) }
    private val ibProfile9: ImageButton by lazy { findViewById(R.id.ib_profile9) }
    private val ibProfile10: ImageButton by lazy { findViewById(R.id.ib_profile10) }
    private val ibProfile11: ImageButton by lazy { findViewById(R.id.ib_profile11) }
    private val ibProfile12: ImageButton by lazy { findViewById(R.id.ib_profile12) }

    private val ivSelectedImage: ImageView by lazy { findViewById(R.id.iv_selected_image) }

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_profile)

        setOnClickListener()
    }

    private fun setOnClickListener() {
        profileImageButtonList.forEachIndexed { idx, ib ->
            ib.setOnClickListener {
                ivSelectedImage.setImageResource(profileImageList[idx])
            }
        }
    }
}