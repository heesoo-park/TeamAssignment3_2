package com.example.teamsns

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import java.util.regex.Pattern
import com.example.teamsns.SignUpValidExtension.includeAlphabetAndNumber
import com.example.teamsns.SignUpValidExtension.includeKorean
import com.example.teamsns.SignUpValidExtension.includeSpecialCharacters
import com.example.teamsns.SignUpValidExtension.includeUpperCase

class SignUpActivity : AppCompatActivity() {

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    private val etName: EditText by lazy {
        findViewById(R.id.et_signup_name)
    }
    private val etId: EditText by lazy {
        findViewById(R.id.et_signup_id)
    }
    private val etPassword: EditText by lazy {
        findViewById(R.id.et_signup_password)
    }
    private val etPasswordConfirmation: EditText by lazy {
        findViewById(R.id.et_signup_password2)
    }
    private val btnNext: Button by lazy {
        findViewById(R.id.btn_signup_next)
    }
    private val tvId: TextView by lazy {
        findViewById(R.id.tv_sigup_id)
    }

    // arrays
    private val editTextArray: Array<EditText> by lazy {
        arrayOf(etName, etId, etPassword, etPasswordConfirmation)
    }

    lateinit var userData: User
    lateinit var id: String
    lateinit var name: String
    lateinit var status: String
    lateinit var user: User
    var myBoolean: Boolean? = false

    private fun setEditCheck() {
        if (intent.getStringExtra("editId") != null) {
            tvId.setText(R.string.edit_status)
            etId.setHint(R.string.edit_status_message)
            id = intent.getStringExtra("editId")!!

            myBoolean = true
            setEditUserData()
        }
    }


    private fun setEditUserData() {
        userData = UserDatabase.getUser(id)!!
        name = userData.name
        status = userData.statusMessage.toString()
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
        // registerForActivityResult 세팅
        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
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
            if (myBoolean == true) {
                user = User(
                    name = etName.text.toString(),
                    id = id,
                    statusMessage = etId.text.toString(),
                    password = etPassword.text.toString()
                )

                Log.e(
                    "USER DATA BEFORE",
                    "Name: ${user.name}, ID: ${user.id}, Status: ${user.statusMessage}, Password: ${user.password}"
                )
                UserDatabase.editUserData(user)
                Log.e(
                    "USER DATA AFTER",
                    "Name: ${user.name}, ID: ${user.id}, Status: ${user.statusMessage}, Password: ${user.password}"
                )

            } else {
                user = User(
                    name = etName.text.toString(),
                    id = etId.text.toString(),
                    password = etPassword.text.toString(),
                    statusMessage = ""
                )
                UserDatabase.addUser(user)
            }
            val intent = Intent(this, ChooseProfileActivity::class.java).apply {
                putExtra("id", user.id)
                putExtra("password", user.password)
                if (myBoolean == true) putExtra("editId", id)
            }
            setResult(RESULT_OK, intent)
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
                text.includeKorean() -> null

                else -> SignUpErrorMessage.INVIALID_NAME
            }
            return errorCode?.let { getString(it.message) }
        } else return null
    }

    private fun getMessageValidId(): String? {
        if (intent.getStringExtra("editId") == null) {
            val text = etId.text.toString()
            val errorCode = when {
                text.isBlank() -> SignUpErrorMessage.EMPTY_ID
                text.includeAlphabetAndNumber() -> null

                else -> SignUpErrorMessage.INVALID_PASSWORD
            }
            return errorCode?.let { getString(it.message) }
        } else return null
    }


    private fun getMessageValidPassword(): String? {
        val text = etPassword.text.toString()
        val errorCode = when {
            text.isBlank() -> getString(R.string.empty_password_message)
            text.includeSpecialCharacters() -> null

            else -> getString(R.string.password_error_message)
        }
        return errorCode
    }

    private fun getMessageValidPasswordConfirm(): String? {
        val text = etPasswordConfirmation.text.toString()
        val errorCode = when {
            text.isBlank() -> SignUpErrorMessage.EMPTY_PASSWORD
            (text.isNotBlank() && Pattern.matches(
                "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&.])[A-Za-z[0-9]$@$!%*#?&.]{8,20}$",
                text
            )) -> null

            (text != etPassword.text.toString()) -> SignUpErrorMessage.PASSWORD_MISMATCH

            else -> SignUpErrorMessage.INVALID_PASSWORD
        }
        return errorCode?.let { getString(it.message) }

    }

    private fun setConfirmButtonEnable() {
        btnNext.isEnabled = getMessageValidName() == null
                && getMessageValidId() == null
                && getMessageValidPassword() == null
                && getMessageValidPasswordConfirm() == null
    }
}


