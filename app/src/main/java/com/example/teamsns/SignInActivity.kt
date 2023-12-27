package com.example.teamsns

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts

class SignInActivity : AppCompatActivity() {

    lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    val et_id: EditText by lazy {
        findViewById(R.id.et_id)
    }
    val et_pw: EditText by lazy {
        findViewById(R.id.et_password)
    }
    val btn_login: Button by lazy {
        findViewById(R.id.btn_login)
    }
    val btn_signup: Button by lazy {
        findViewById(R.id.btn_signup)
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
                    val user_id = it.data?.getStringExtra("id") ?: ""
                    val user_pw = it.data?.getStringExtra("pw") ?: ""
                    et_id.setText(user_id)
                    et_pw.setText(user_pw)
                }
            }
    }

    private fun btnLogin() {
        btn_login.setOnClickListener{
            when {
                et_id.text.toString().trim().isEmpty() -> {Toast.makeText(this, getString(R.string.empty_id_message), Toast.LENGTH_SHORT).show()
                    return@setOnClickListener}
                et_pw.text.toString().trim().isEmpty() -> {Toast.makeText(this, getString(R.string.empty_password_message), Toast.LENGTH_SHORT).show()
                    return@setOnClickListener}
            }

            val intent = Intent(this, MainPageActivity::class.java)
            intent.putExtra("id", et_id.text.toString())
            startActivity(intent)
        }
    }

    private fun btnSignup() {
        btn_signup.setOnClickListener{
            val intent = Intent(this, SignUpActivity::class.java)
            activityResultLauncher.launch(intent)
        }

    }

}