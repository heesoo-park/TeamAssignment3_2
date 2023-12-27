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

    private val etId: EditText by lazy {
        findViewById(R.id.et_signin_id)
    }
    private val etPw: EditText by lazy {
        findViewById(R.id.et_signin_password)
    }
    private val btnLogin: Button by lazy {
        findViewById(R.id.btn_signin_login)
    }
    private val btnSignup: Button by lazy {
        findViewById(R.id.btn_signin_signup)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        initView()
    }

    private fun initView() {
        activityResultLauncher()
        btnLogin()
        btnSignup()
    }

    private fun activityResultLauncher() {
        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == RESULT_OK) {
                    val userId = it.data?.getStringExtra("id") ?: ""
                    val userPw = it.data?.getStringExtra("pw") ?: ""
                    etId.setText(userId)
                    etPw.setText(userPw)
                }
            }
    }

    private fun btnLogin() {
        btnLogin.setOnClickListener{
            val userData = getUser(etId.text.toString())
            when {
                etId.text.toString().trim().isEmpty() -> {Toast.makeText(this, SignUpErrorMessage.EMPTY_ID.toString(), Toast.LENGTH_SHORT).show()
                    return@setOnClickListener}
                etPw.text.toString().trim().isEmpty() -> {Toast.makeText(this, SignUpErrorMessage.EMPTY_PASSWORD.toString(), Toast.LENGTH_SHORT).show()
                    return@setOnClickListener}
                (userData == null) -> {Toast.makeText(this, SignUpErrorMessage.PASSWORD_MISMATCH.toString(), Toast.LENGTH_SHORT).show()
                    return@setOnClickListener}
                (userData.password != etPw.text.toString()) -> {Toast.makeText(this, SignUpErrorMessage.PASSWORD_MISMATCH.toString(), Toast.LENGTH_SHORT).show()
                    return@setOnClickListener}
            }

            val intent = Intent(this, MainPageActivity::class.java)

            intent.putExtra("id", etId.text.toString())

            intent.putExtra("pw", etPw.text.toString())

            startActivity(intent)
        }
    }

    private fun btnSignup() {
        btnSignup.setOnClickListener{
            val intent = Intent(this, SignUpActivity::class.java)
            activityResultLauncher.launch(intent)
        }
    }

}