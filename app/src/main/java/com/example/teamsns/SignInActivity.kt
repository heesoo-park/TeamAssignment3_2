package com.example.teamsns

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.teamsns.UserDatabase.getUser

class SignInActivity : AppCompatActivity() {

    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    private val etSignInId: EditText by lazy {
        findViewById(R.id.et_signin_id)
    }
    private val etSignInPw: EditText by lazy {
        findViewById(R.id.et_signin_password)
    }
    private val btnSignInLogIn: Button by lazy {
        findViewById(R.id.btn_signin_login)
    }
    private val btnSignInSignUp: Button by lazy {
        findViewById(R.id.btn_signin_signup)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        init()
    }

    private fun init() {
        activityResultLauncher()
        btnLogin()
        btnSignup()
    }

    // registerForActivityResult 세팅하는 함수
    private fun activityResultLauncher() {
        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == RESULT_OK) {
                    val userId = it.data?.getStringExtra("id") ?: ""
                    val userPw = it.data?.getStringExtra("pw") ?: ""
                    val editRecycle = it.data?.getStringExtra("edit") ?: ""
                    etSignInId.setText(userId)
                    etSignInPw.setText(userPw)
                }
            }
    }

    // 로그인 버튼 클릭 리스너 함수
    private fun btnLogin() {
        btnSignInLogIn.setOnClickListener{
            val userData = getUser(etSignInId.text.toString())
            when {
                etSignInId.text.toString().trim().isEmpty() -> {
                    Toast.makeText(this, SignUpErrorMessage.EMPTY_ID.message, Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                etSignInPw.text.toString().trim().isEmpty() -> {
                    Toast.makeText(this, SignUpErrorMessage.EMPTY_PASSWORD.message, Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                (userData == null) -> {
                    Toast.makeText(this, SignUpErrorMessage.PASSWORD_MISMATCH.message, Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                (userData.password != etSignInPw.text.toString()) -> {
                    Toast.makeText(this, SignUpErrorMessage.PASSWORD_MISMATCH.message, Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }

            val intent = Intent(this, MainPageActivity::class.java)

            intent.putExtra("id", etSignInId.text.toString())
            intent.putExtra("pw", etSignInPw.text.toString())

            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_from_bottom, R.anim.slide_out_to_top)
        }
    }

    // 회원가입 버튼 클릭 리스너 함수
    private fun btnSignup() {
        btnSignInSignUp.setOnClickListener{
            val intent = Intent(this, SignUpActivity::class.java)
            activityResultLauncher.launch(intent)
            overridePendingTransition(R.anim.slide_in_from_bottom, R.anim.slide_out_to_top)
        }
    }
}