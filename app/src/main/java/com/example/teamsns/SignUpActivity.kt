package com.example.teamsns

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener

class SignUpActivity : AppCompatActivity() {

    private val et_name: EditText by lazy {
        findViewById(R.id.et_name)
    }
    private val error_message_name: TextView by lazy {
        findViewById(R.id.error_message_name)
    }
    private val et_id: EditText by lazy {
        findViewById(R.id.et_id)
    }
    private val error_message_id: TextView by lazy {
        findViewById(R.id.error_message_id)
    }
    private val et_password: EditText by lazy {
        findViewById(R.id.et_password)
    }
    private val error_message_password: TextView by lazy {
        findViewById(R.id.error_message_password)
    }
    private val et_password_confirmation: EditText by lazy {
        findViewById(R.id.et_password2)
    }
    private val error_message_password2: TextView by lazy {
        findViewById(R.id.error_message_password)
    }
    private val btn_signup: Button by lazy {
        findViewById(R.id.btn_sign_up_next)
    }
    private val tvId: TextView by lazy {
        findViewById(R.id.tv_id)
    }

    // arrays
    private val editTextArray: Array<EditText> by lazy {
        arrayOf(et_name, et_id, et_password, et_password_confirmation)
    }


    private val regexMap: Map<EditText, String> by lazy {
        mapOf(
            et_name to "^[a-zA-Z가-힣]*\$",
            et_id to "/^[a-z0-9_-]{3,16}\$/",
            et_password to "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[\\\$@\\\$!%*#?&.])[A-Za-z[0-9]\\\$@\\\$!%*#?&.]{8,16}\\\$",
            et_password_confirmation to "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[\\\$@\\\$!%*#?&.])[A-Za-z[0-9]\\\$@\\\$!%*#?&.]{8,16}\\\$"
        )
    }

    lateinit var id: String
    lateinit var userData: User
    private fun setEditCheck(){
        if (intent.getStringExtra("editId") != null){
            tvId.isVisible = false
            et_id.isVisible = false
            et_id.setText(id)
            id = intent.getStringExtra("editId")!!

            setUserData()
        }
    }

    private fun setUserData(){
        userData = UserDatabase.totalUserData.find { it.id == id }!!
        et_name.setText(userData.name)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        initView()
    }

    private fun initView() {
        setEditCheck()

        setTextChangedListener()
        setOnFocusChangedListener()
        btnSignup()
    }

    private fun btnSignup() {
        btn_signup.setOnClickListener {

            val intent = Intent(this, SignInActivity::class.java).apply {
                putExtra("id", et_id.text.toString())
                putExtra("pw", et_password.text.toString())
            }

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
            et_name -> error = getMessageValidName()
            et_id -> error = getMessageValidId()
            et_password -> error = getMessageValidPassword()
            et_password_confirmation -> error = getMessageValidPasswordConfirm()
// TODO 추가 사항 확인
            else -> Unit
        }
    }

    private fun getMessageValidName(): String? {
        val text = et_name.text.toString()
        val specialCharacterRegex = Regex(regexMap[et_name]!!)
        if (et_name.isVisible) {
            val errorCode = when {
                text.isBlank() -> SignUpErrorMessage.EMPTY_NAME
                !specialCharacterRegex.containsMatchIn(text)
                    .not() -> SignUpErrorMessage.INVIALID_NAME

                else -> null
            }
            return errorCode?.let { getString(it.message) }
        } else return null
    }

    private fun getMessageValidId(): String? {
        val text = et_id.text.toString()
        val specialCharacterRegex = Regex(regexMap[et_id]!!)
        if (et_id.isVisible) {
            val errorCode = when {
                text.isBlank() -> SignUpErrorMessage.EMPTY_ID
                !specialCharacterRegex.containsMatchIn(text)
                    .not() -> SignUpErrorMessage.INVALID_ID

                else -> null
            }
            return errorCode?.let { getString(it.message) }
        } else return null
    }


    private fun getMessageValidPassword(): String? {
        val text = et_password.text.toString()
        val specialCharacterRegex = Regex(regexMap[et_password]!!)
        if (et_password.isVisible) {
            val errorCode = when {
                text.isBlank() -> getString(SignUpErrorMessage.EMPTY_PASSWORD.message)
                !specialCharacterRegex.containsMatchIn(text)
                    .not() -> getString(SignUpErrorMessage.INVALID_PASSWORD.message)

                else -> null
            }
            return errorCode
        } else return null
    }

    private fun getMessageValidPasswordConfirm(): String? {
        val text = et_password_confirmation.text.toString()
        val specialCharacterRegex = Regex(regexMap[et_password]!!)
        if (et_id.isVisible) {
            val errorCode = when {
                text.isBlank() -> SignUpErrorMessage.EMPTY_PASSWORD
                !specialCharacterRegex.containsMatchIn(text)
                    .not() -> SignUpErrorMessage.INVALID_PASSWORD

                (text != et_password.text.toString()) -> SignUpErrorMessage.PASSWORD_MISMATCH


                else -> null
            }
            return errorCode?.let { getString(it.message) }
        } else return null

    }

    private fun setConfirmButtonEnable() {
        btn_signup.isEnabled = getMessageValidName() == null
                && getMessageValidId() == null
                && getMessageValidPassword() == null
                && getMessageValidPasswordConfirm() == null
    }


}


