# SignUpPageActivity.md

1. 회원가입 페이지
2. 편집 페이지 ->  (로그인 후 사용자 아이디 및 상태메세지 수정 시에 재활용 됩니다.)

![image](https://github.com/heesoo-park/TeamAssignment3_2/assets/148201041/9746921e-dfd4-429e-a4bd-d8bd24e3f03b)


## (1) Layout : [activity_sign_up.xml](https://github.com/heesoo-park/TeamAssignment3_2/blob/dev/app/src/main/res/layout/activity_sign_up.xml) & [activity_sign_up.xml-(land)](https://github.com/heesoo-park/TeamAssignment3_2/blob/dev/app/src/main/res/layout-land/activity_sign_up.xml)

### - Constraint Layout

요즘 Constraint Layout을 사용하는게 대세라고 튜터님께서 말씀해주셨어서 Constraint Layout을 사용하였습니다.

Landscape View에서는 위젯들이 다 안들어가기 때문에 ScrollView를 사용하여 스크롤하여 모든 위젯들에 접근 가능하도록 했습니다.

### - EditText

EditText는 inputType을 지정해줌으로써 EditText 활성화 시 적절한 키보드가 작동하도록 했습니다. 

비밀번호 inputType을 passwordText를 지정해줌으로써 EditText가 활성화 시 문자 키보드가 활성화 되고 입력되는 값들이 자동으로 가려집니다.

EditText에 Hint 텍스트 값을 부여해줌으로써 텍스트가 비어있고 비활성화 되어 있는 경우에 EditText에 텍스트가 표시될 수 있도록 만들었습니다.

## (2) Activity : [SignUpPageActivity.kt](https://github.com/heesoo-park/TeamAssignment3_2/blob/dev/app/src/main/java/com/example/teamsns/SignUpActivity.kt)

### 객체

[activity_sign_up.xml](https://github.com/heesoo-park/TeamAssignment3_2/blob/dev/app/src/main/res/layout/activity_sign_up.xml) & [activity_sign_up.xml-(land)](https://github.com/heesoo-park/TeamAssignment3_2/blob/dev/app/src/main/res/layout-land/activity_sign_up.xml) 의 EditText와 Button을 모두 지연 초기화를 시켜주었습니다.

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>


회원가입 페이지에서 입력된 값을 로그인 페이지로 넘겨 줄 수 있도록 resultLauncher 또한 지연 초기화 해 주었습니다.

    lateinit var userData: User
    lateinit var id: String
    lateinit var name: String
    lateinit var status: String
    lateinit var user: User
    var myBoolean: Boolean? = false

사용자의 정보를 각각 저장하고 그 값을 user 데이터 클래스에 넘길 수 있도록 지연초기화 해주었습니다.

### 메소드

#### initView()

    private fun initView() {
        setEditCheck()
        setTextChangedListener()
        setOnFocusChangedListener()
        btnNext()
    }

코드의 가독성을 높이기 위해 onCreate에서 실행되는 초기화면 함수들을 따로 모아두었습니다.

#### setEditCheck()

private fun setEditCheck() {
        if (intent.getStringExtra("editId") != null) {
            tvSignUpId.setText(R.string.edit_status)
            etSignUpId.setHint(R.string.edit_status_message)
            id = intent.getStringExtra("editId")!!

            myBoolean = true
            setEditUserData()
        }
    }

#### setEditUserData()

    private fun setEditUserData() {
        userData = UserDatabase.getUser(id)!!
        name = userData.name
        status = userData.statusMessage.toString()
    }

#### btnNext() 

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
            overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left)
        }
    }

#### setTextChangedListener()

    private fun setTextChangedListener() {
        editTextArray.forEach { editText ->
            editText.addTextChangedListener {
                editText.setErrorMessage()
                setConfirmButtonEnable()
            }
        }
    }

#### setOnFocusChangedListener()

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

#### EditText.setErrorMessage()

    private fun EditText.setErrorMessage() {
        when (this) {
            etSignUpName -> error = getMessageValidName()
            etSignUpId -> error = getMessageValidId()
            etSignUpPassword -> error = getMessageValidPassword()
            etSignUpPassword2 -> error = getMessageValidPasswordConfirm()

            else -> Unit
        }
    }

#### getMessageValidName()

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

#### getMessageValidId()

    private fun getMessageValidId(): String? {
        if (myBoolean == false) {
            val text = etSignUpId.text.toString()
            val userData = UserDatabase.getUser(etSignUpId.text.toString())
            val errorCode = when {
                text.isBlank() -> SignUpErrorMessage.EMPTY_ID
                text.includeAlphabetAndNumber() -> null
                (userData != null) -> SignUpErrorMessage.OVERLAPPING_ID
                else -> SignUpErrorMessage.INVALID_PASSWORD
            }
            return errorCode?.let { getString(it.message) }
        }else return null
    }

#### getMessageValidPassword()

    private fun getMessageValidPassword(): String? {
        val text = etSignUpPassword.text.toString()
        val errorCode = when {
            text.isBlank() -> getString(R.string.empty_password_message)
            text.includeSpecialCharacters() -> null

            else -> getString(R.string.password_error_message)
        }
        return errorCode
    }

#### getMessageValidPasswordConfirm()

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

#### setConfirmButtonEnable()

    private fun setConfirmButtonEnable() {
        btnSignUpNext.isEnabled = getMessageValidName() == null
                && getMessageValidId() == null
                && getMessageValidPassword() == null
                && getMessageValidPasswordConfirm() == null
    }
