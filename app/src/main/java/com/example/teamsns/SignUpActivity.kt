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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        initView()
    }

    private fun initView() {
        setEditCheck()
        setTextChangedListener()
        setOnFocusChangedListener()
        btnNext()
    }

    // 편집 페이지로 사용해야하는지 판별하는 함수
    private fun setEditCheck() {
        if (intent.getStringExtra("editId") != null) {
            tvSignUpId.setText(R.string.edit_status)
            etSignUpId.setHint(R.string.edit_status_message)
            id = intent.getStringExtra("editId")!!

            myBoolean = true
            setEditUserData()
        }
    }

    // 편집할 사용자 정보를 저장하는 함수
    private fun setEditUserData() {
        userData = UserDatabase.getUser(id)!!
        name = userData.name
        status = userData.statusMessage.toString()
    }

    // 다음 버튼을 눌렀을 때 실행되는 함수
    private fun btnNext() {
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
                UserDatabase.editUserData(user)
            }

            val intent = Intent(this, ChooseProfileActivity::class.java).apply {
                putExtra("name", etSignUpName.text.toString())
                putExtra("id", etSignUpId.text.toString())
                putExtra("password", etSignUpPassword.text.toString())

                if (myBoolean == true) putExtra("editId", id)
            }

            setResult(RESULT_OK, intent)
            resultLauncher.launch(intent)
            overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left)
        }
    }

    // EditText의 값 변경 리스너 함수
    private fun setTextChangedListener() {
        editTextArray.forEach { editText ->
            editText.addTextChangedListener {
                editText.setErrorMessage()
                setConfirmButtonEnable()
            }
        }
    }

    // EditText의 포커스 변경 리스너 함수
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

    // EditText에서 에러 메세지를 출력하기 위해 만든 확장함수
    private fun EditText.setErrorMessage() {
        when (this) {
            etSignUpName -> error = getMessageValidName()
            etSignUpId -> error = getMessageValidId()
            etSignUpPassword -> error = getMessageValidPassword()
            etSignUpPassword2 -> error = getMessageValidPasswordConfirm()

            else -> Unit
        }
    }

    // 이름에 대한 에러 메세지 반환하는 함수
    private fun getMessageValidName(): String? {
        val text = etSignUpName.text.toString()
        return if (etSignUpName.isVisible) {
            val errorCode = when {
                text.isBlank() -> SignUpErrorMessage.EMPTY_NAME
                text.includeKorean() -> null

                else -> SignUpErrorMessage.INVIALID_NAME
            }
            errorCode?.let { getString(it.message) }
        } else null
    }

    // 아이디에 대한 에러 메세지 반환하는 함수
    private fun getMessageValidId(): String? {
        if (myBoolean == false) {
            val text = etSignUpId.text.toString()
            val userData = UserDatabase.getUser(etSignUpId.text.toString())
            val errorCode = when {
                text.isBlank() -> SignUpErrorMessage.EMPTY_ID
                text.includeAlphabetAndNumber() -> null
                (userData != null) -> SignUpErrorMessage.OVERLAPPING_ID
                else -> SignUpErrorMessage.INVALID_ID
            }
            return errorCode?.let { getString(it.message) }
        }else return null
    }

    // 비밀번호에 대한 에러 메세지 반환하는 함수
    private fun getMessageValidPassword(): String? {
        val text = etSignUpPassword.text.toString()
        val errorCode = when {
            text.isBlank() -> getString(R.string.empty_password_message)
            text.includeSpecialCharacters() -> null
            else -> getString(R.string.password_error_message)
        }

        return errorCode
    }

    // 비밀번호 확인에 대한 에러 메세지 반환하는 함수
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

    // 다음 버튼 활성화 함수
    private fun setConfirmButtonEnable() {
        btnSignUpNext.isEnabled = getMessageValidName() == null
                && getMessageValidId() == null
                && getMessageValidPassword() == null
                && getMessageValidPasswordConfirm() == null
    }
}


