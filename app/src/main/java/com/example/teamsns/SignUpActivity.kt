package com.example.teamsns

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import java.util.regex.Pattern

class SignUpActivity : AppCompatActivity() {

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    private val etName: EditText by lazy {
        findViewById(R.id.et_name)
    }
    private val etId: EditText by lazy {
        findViewById(R.id.et_sign_up_id)
    }
    private val etPassword: EditText by lazy {
        findViewById(R.id.et_sign_up_password)
    }
    private val etPasswordConfirmation: EditText by lazy {
        findViewById(R.id.et_password2)
    }
    private val btnNext: Button by lazy {
        findViewById(R.id.btn_sign_up_next)
    }
    // arrays
    private val editTextArray: Array<EditText> by lazy {
        arrayOf(etName, etId, etPassword, etPasswordConfirmation)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        initView()
    }
    private fun initView() {
        setTextChangedListener()
        setOnFocusChangedListener()
        btnSignup()
    }
    private fun btnSignup() {
        // registerForActivityResult 세팅
        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            // ChooseProfileActivity에서 RESULT_OK가 반환되면 현재 액티비티 종료
            // ChooseProfileActivity에서 바로 SignInActivity로 이동하는 형태가 됨
            if (result.resultCode == RESULT_OK) {
                // 먼저 SignInActivity로 아이디와 패스워드를 보냄
                intent.putExtra("id", etId.text.toString())
                intent.putExtra("password", etPassword.text.toString())
                setResult(RESULT_OK, intent)
                finish()
            }
        }

        btnNext.setOnClickListener {
            // 유저등록에 필요한 데이터와 함께 ChooseProfileActivity로 이동
            var intent = Intent(this@SignUpActivity, ChooseProfileActivity::class.java)
            intent.putExtra("name", etName.text.toString())
            intent.putExtra("id", etId.text.toString())
            intent.putExtra("password", etPassword.text.toString())
            resultLauncher.launch(intent)
        }
    }

    private fun setTextChangedListener() {
        editTextArray.forEach { editText ->
            editText.addTextChangedListener {
                editText.setErrorMessage()
                setConfirmButtonEnable()
            }
        }
    }

    private fun setOnFocusChangedListener() {
        editTextArray.forEach { editText ->
            editText.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus.not()) {
                    editText.setErrorMessage()
                    setConfirmButtonEnable()
                }
            }
        }
    }

    private fun EditText.setErrorMessage() {
        when (this) {
            etName -> error = getMessageValidName()
            etId -> error = getMessageValidId()
            etPassword -> error = getMessageValidPassword()
            etPasswordConfirmation -> error = getMessageValidPasswordConfirm()

            else -> Unit
        }
    }

    private fun getMessageValidName(): String? {
        val text = etName.text.toString()
        if (etName.isVisible) {
            val errorCode = when {
                text.isBlank() -> SignUpErrorMessage.EMPTY_NAME
                (text.isNotBlank() && Pattern.matches("^[가-힣]*$",text)) -> null

                else -> SignUpErrorMessage.INVIALID_NAME
            }
            return errorCode?.let { getString(it.message) }
        } else return null
    }

    private fun getMessageValidId(): String? {
        val text = etId.text.toString()
        if (etId.isVisible) {
            val errorCode = when {
                text.isBlank() -> SignUpErrorMessage.EMPTY_ID
                (text.isNotBlank() && Pattern.matches("^[a-z0-9]*\$",text)) -> null

                else -> SignUpErrorMessage.INVALID_PASSWORD
            }
            return errorCode?.let { getString(it.message) }
        } else return null
    }


    private fun getMessageValidPassword(): String? {
        val text = etPassword.text.toString()
        if (etPassword.isVisible) {
            val errorCode = when {
                text.isBlank() -> getString(R.string.empty_password_message)
                (text.isNotBlank() && Pattern.matches("^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&.])[A-Za-z[0-9]$@$!%*#?&.]{8,20}$", text)) -> null

                else -> getString(R.string.password_error_message)
            }
            return errorCode
        } else return null
    }

    private fun getMessageValidPasswordConfirm(): String? {
        val text = etPasswordConfirmation.text.toString()
        if (etId.isVisible) {
            val errorCode = when {
                text.isBlank() -> SignUpErrorMessage.EMPTY_PASSWORD
                (text.isNotBlank() && Pattern.matches("^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&.])[A-Za-z[0-9]$@$!%*#?&.]{8,20}$", text)) -> null

                (text != etPassword.text.toString()) -> SignUpErrorMessage.PASSWORD_MISMATCH

                else -> SignUpErrorMessage.INVALID_PASSWORD
            }
            return errorCode?.let { getString(it.message) }
        } else return null

    }

    private fun setConfirmButtonEnable() {
        btnNext.isEnabled = getMessageValidName() == null
                && getMessageValidId() == null
                && getMessageValidPassword() == null
                && getMessageValidPasswordConfirm() == null
    }
}


