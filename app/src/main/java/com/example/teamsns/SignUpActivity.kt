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

class SignUpActivity : AppCompatActivity() {

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    private val etSignUpName: EditText by lazy {
        findViewById(R.id.et_signup_name)
    }
    private val etSignUpId: EditText by lazy {
        findViewById(R.id.et_signup_id)
    }
    private val etSignUpPassword: EditText by lazy {
        findViewById(R.id.et_signup_password)
    }
    private val etSignUpPassword2: EditText by lazy {
        findViewById(R.id.et_signup_password2)
    }
    private val btnSignUpNext: Button by lazy {
        findViewById(R.id.btn_signup_next)
    }
    private val tvSignUpId: TextView by lazy {
        findViewById(R.id.tv_signup_id)
    }

    // arrays
    private val editTextArray: Array<EditText> by lazy {
        arrayOf(etSignUpName, etSignUpId, etSignUpPassword, etSignUpPassword2)
    }

    lateinit var userData: User
    lateinit var id: String
    lateinit var name: String
    lateinit var status: String
    lateinit var user: User
    var myBoolean: Boolean? = false

    private fun setEditCheck() {
        if (intent.getStringExtra("editId") != null) {
            tvSignUpId.setText(R.string.edit_status)
            etSignUpId.setHint(R.string.edit_status_message)
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
        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    intent.putExtra("id", etSignUpId.text.toString())
                    intent.putExtra("password", etSignUpPassword.text.toString())
                    setResult(RESULT_OK, intent)
                    finish()
                }
            }

        btnSignUpNext.setOnClickListener {
            if (myBoolean == true) {
                user = User(
                    name = etSignUpName.text.toString(),
                    id = id,
                    statusMessage = etSignUpId.text.toString(),
                    password = etSignUpPassword.text.toString()
                )

                Log.e("USER DATA BEFORE", "Name: ${user.name}, ID: ${user.id}, Status: ${user.statusMessage}, Password: ${user.password}")
                UserDatabase.editUserData(user)
                Log.e("USER DATA AFTER", "Name: ${user.name}, ID: ${user.id}, Status: ${user.statusMessage}, Password: ${user.password}")

            }
            val intent = Intent(this, ChooseProfileActivity::class.java).apply {
                putExtra("name", etSignUpName.text.toString())
                putExtra("id", etSignUpId.text.toString())
                putExtra("password", etSignUpPassword.text.toString())
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
            etSignUpName -> error = getMessageValidName()
            etSignUpId -> error = getMessageValidId()
            etSignUpPassword -> error = getMessageValidPassword()
            etSignUpPassword2 -> error = getMessageValidPasswordConfirm()

            else -> Unit
        }
    }

    private fun getMessageValidName(): String? {
        val text = etSignUpName.text.toString()
        if (etSignUpName.isVisible) {
            val errorCode = when {
                text.isBlank() -> SignUpErrorMessage.EMPTY_NAME
                text.includeKorean() -> null

                else -> SignUpErrorMessage.INVIALID_NAME
            }
            return errorCode?.let { getString(it.message) }
        } else return null
    }

    private fun getMessageValidId(): String? {
        val text = etId.text.toString()
        val userData = UserDatabase.getUser(etId.text.toString())
        if (etId.isVisible) {
            val errorCode = when {
                text.isBlank() -> SignUpErrorMessage.EMPTY_ID
                (userData == null && text.isNotBlank() && Pattern.matches("^[a-z0-9]*\$",text)) -> null
                (userData != null) -> SignUpErrorMessage.OVERLAPPING_ID
                else -> SignUpErrorMessage.INVALID_PASSWORD
            }
            return errorCode?.let { getString(it.message) }
        } else return null
    }


    private fun getMessageValidPassword(): String? {
        val text = etSignUpPassword.text.toString()
        if (etSignUpPassword.isVisible) {
            val errorCode = when {
                text.isBlank() -> getString(R.string.empty_password_message)
                text.includeSpecialCharacters() -> null

                else -> getString(R.string.password_error_message)
            }
            return errorCode
        } else return null
    }

    private fun getMessageValidPasswordConfirm(): String? {
        val text = etSignUpPassword2.text.toString()
            val errorCode = when {
                text.isBlank() -> SignUpErrorMessage.EMPTY_PASSWORD
                (text.isNotBlank() && Pattern.matches(
                    "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&.])[A-Za-z[0-9]$@$!%*#?&.]{8,20}$",
                    text
                )) -> null

                (text != etSignUpPassword.text.toString()) -> SignUpErrorMessage.PASSWORD_MISMATCH

                else -> SignUpErrorMessage.INVALID_PASSWORD
            }
            return errorCode?.let { getString(it.message) }

    }

    private fun setConfirmButtonEnable() {
        btnSignUpNext.isEnabled = getMessageValidName() == null
                && getMessageValidId() == null
                && getMessageValidPassword() == null
                && getMessageValidPasswordConfirm() == null
    }
}


