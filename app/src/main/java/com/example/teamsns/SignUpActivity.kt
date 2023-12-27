package com.example.teamsns

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import java.util.regex.Pattern

class SignUpActivity : AppCompatActivity() {

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

        btnNext.setOnClickListener {

            val intent = Intent(this, ChooseProfileActivity::class.java).apply {
                putExtra("id", etId.text.toString())
                putExtra("password", etPassword.text.toString())
            }
            val user = User(
                name = etName.toString(),
                id = etId.toString(),
                password = etPassword.toString(),
                profileImage = null,
                statusMessage = null,
                userPosts = null
            )
            UserDatabase.addUser(user)

            setResult(RESULT_OK, intent)

            if (!isFinishing) finish()

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


