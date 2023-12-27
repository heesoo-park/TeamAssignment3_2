package com.example.teamsns

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.example.teamsns.SignUpValidExtension.includeSpecialCharacters
import com.example.teamsns.SignUpValidExtension.includeUpperCase
import java.util.regex.Pattern

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
    private val btn_next: Button by lazy {
        findViewById(R.id.btn_sign_up_next)
    }

    // arrays
    private val editTextArray: Array<EditText> by lazy {
        arrayOf(et_name, et_id, et_password, et_password_confirmation)
    }
    private val errorMessageArray: Array<TextView> by lazy {
        arrayOf(
            error_message_name,
            error_message_id,
            error_message_password,
            error_message_password2
        )
    }


//    private val regexMap: Map<EditText, String> by lazy {
//        mapOf(
//            et_name to "^[a-zA-Z가-힣]*\$",
//            et_id to "/^[a-z0-9_-]{3,16}\$/",
//            et_password to "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[\\\$@\\\$!%*#?&.])[A-Za-z[0-9]\\\$@\\\$!%*#?&.]{8,16}\\\$",
//            et_password_confirmation to "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[\\\$@\\\$!%*#?&.])[A-Za-z[0-9]\\\$@\\\$!%*#?&.]{8,16}\\\$"
//        )
//    }


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

        btn_next.setOnClickListener {

            val intent = Intent(this, ChooseProfileActivity::class.java).apply {
                putExtra("id", et_id.text.toString())
                putExtra("password", et_password.text.toString())

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

            else -> Unit
        }
    }

    private fun getMessageValidName(): String? {
        val text = et_name.text.toString()
        if (et_name.isVisible) {
            val errorCode = when {
                text.isBlank() -> SignUpErrorMessage.EMPTY_NAME
                (text.isNotBlank() && Pattern.matches("^[가-힣]*$",text)) -> null

                else -> SignUpErrorMessage.INVIALID_NAME
            }
            return errorCode?.let { getString(it.message) }
        } else return null
    }

    private fun getMessageValidId(): String? {
        val text = et_id.text.toString()
        if (et_id.isVisible) {
            val errorCode = when {
                text.isBlank() -> SignUpErrorMessage.EMPTY_ID
                (text.isNotBlank() && Pattern.matches("^[a-z0-9]*\$",text)) -> null

                else -> SignUpErrorMessage.INVALID_PASSWORD
            }
            return errorCode?.let { getString(it.message) }
        } else return null
    }


    private fun getMessageValidPassword(): String? {
        val text = et_password.text.toString()
        if (et_password.isVisible) {
            val errorCode = when {
                text.isBlank() -> getString(R.string.empty_password_message)
                (text.isNotBlank() && Pattern.matches("^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&.])[A-Za-z[0-9]$@$!%*#?&.]{8,20}$", text)) -> null

                else -> getString(R.string.password_error_message)
            }
            return errorCode
        } else return null
    }

    private fun getMessageValidPasswordConfirm(): String? {
        val text = et_password_confirmation.text.toString()
        if (et_id.isVisible) {
            val errorCode = when {
                text.isBlank() -> SignUpErrorMessage.EMPTY_PASSWORD
                (text.isNotBlank() && Pattern.matches("^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&.])[A-Za-z[0-9]$@$!%*#?&.]{8,20}$", text)) -> null


                (text != et_password.text.toString()) -> SignUpErrorMessage.PASSWORD_MISMATCH


                else -> SignUpErrorMessage.INVALID_PASSWORD
            }
            return errorCode?.let { getString(it.message) }
        } else return null

    }

    private fun setConfirmButtonEnable() {
        btn_next.isEnabled = getMessageValidName() == null
                && getMessageValidId() == null
                && getMessageValidPassword() == null
                && getMessageValidPasswordConfirm() == null
    }


}


