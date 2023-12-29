[readme](https://github.com/heesoo-park/TeamAssignment3_2/blob/dev/README.md)

# SignUpPageActivity.md

1. 회원가입 페이지
2. 편집 페이지 ->  (로그인 후 사용자 아이디 및 상태메세지 수정 시에 재활용 됩니다.)



## (1) Layout : [activity_sign_up.xml](https://github.com/heesoo-park/TeamAssignment3_2/blob/dev/app/src/main/res/layout/activity_sign_up.xml) & [activity_sign_up.xml-(land)](https://github.com/heesoo-park/TeamAssignment3_2/blob/dev/app/src/main/res/layout-land/activity_sign_up.xml)

### - Constraint Layout

요즘 Constraint Layout을 사용하는게 대세라고 튜터님께서 말씀해주셨어서 Constraint Layout을 사용하였습니다.

위젯이랑 이미지가 한 페이지에 다 안들어가기 때문에 ScrollView를 사용하여 스크롤하여 모든 사진 및 위젯들에 접근 가능하도록 했습니다.

### - EditText

EditText는 inputType을 지정해줌으로써 EditText 활성화 시 적절한 키보드가 작동하도록 했습니다. 

비밀번호 inputType을 passwordText를 지정해줌으로써 EditText가 활성화 시 문자 키보드가 활성화 되고 입력되는 값들이 자동으로 가려집니다.

EditText에 Hint 텍스트 값을 부여해줌으로써 텍스트가 비어있고 비활성화 되어 있는 경우에 EditText에 텍스트가 표시될 수 있도록 만들었습니다.

## (2) Activity : [SignUpPageActivity.kt](https://github.com/heesoo-park/TeamAssignment3_2/blob/dev/app/src/main/java/com/example/teamsns/SignUpActivity.kt)

### 객체

[activity_sign_up.xml](https://github.com/heesoo-park/TeamAssignment3_2/blob/dev/app/src/main/res/layout/activity_sign_up.xml) & [activity_sign_up.xml-(land)](https://github.com/heesoo-park/TeamAssignment3_2/blob/dev/app/src/main/res/layout-land/activity_sign_up.xml) 의 EditText와 Button을 모두 지연 초기화를 시켜주었습니다.
````
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

````
회원가입 페이지에서 입력된 값을 로그인 페이지로 넘겨 줄 수 있도록 resultLauncher 또한 지연 초기화 해 주었습니다.
````
    lateinit var userData: User
    lateinit var id: String
    lateinit var name: String
    lateinit var status: String
    lateinit var user: User
    var myBoolean: Boolean? = false
````

편집 페이지로 이 회원가입 페이지를 재활용하는 경우에 사용자의 정보를 각각 저장하고 그 값을 user 데이터 클래스에 넘길 수 있도록 지연초기화 해주었습니다. 

myBoolean 값이 이 페이지가 편집 페이지인지 회원가입페이지인지 분별해주는 기준이 됩니다.

### 메소드

> initView()
````
    private fun initView() {
        setEditCheck()
        setTextChangedListener()
        setOnFocusChangedListener()
        btnNext()
    }
````
코드의 가독성을 높이기 위해 onCreate에서 실행되는 초기화면 함수들을 따로 모아두었습니다.



> setEditCheck()
````
private fun setEditCheck() {
        if (intent.getStringExtra("editId") != null) {
            tvSignUpId.setText(R.string.edit_status)
            etSignUpId.setHint(R.string.edit_status_message)
            id = intent.getStringExtra("editId")!!

            myBoolean = true
            setEditUserData()
        }
    }
````

편집 페이지로 사용해야하는지 판별하는 함수입니다.

회원가입 페이지로 사용되는 경우에는 이전 페이지에서 아무 값도 넘겨 받지 않습니다. 반면에 편집 페이지로 사용된다면 이미 로그인이 되어 있는 상태이기 때문에 userId에 대한 정보를 이미 알고 있습니다. 따라서 이 사용자 정보를 SignUpPageActivity에 넘겨줌으로써 편집 페이지로 이용하는 경우인지 확인할 수 있습니다.

이전 페이지에서 editId를 넘겨 받았다면 editPage로 사용되고 넘겨 받지 못했다면 회원가입 페이지로 사용됩니다.



> setEditUserData()
````
    private fun setEditUserData() {
        userData = UserDatabase.getUser(id)!!
        name = userData.name
        status = userData.statusMessage.toString()
    }
````
편집페이지로 판별이 되었을 때에만 실행되는 함수입니다. 

편집할 사용자 정보를 저장하는 함수입니다. 앞서 지연초기화해준 사용자 정보 객체들이 이 함수에서 초기화 됩니다.



> btnNext() 
````
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
````
다음 버튼을 눌렀을 때 실행되는 함수입니다. 

registerForActivityResult를 사용해줌으로써 putExtra한 값들을 넘겨줍니다.

그리고 ChooseProfileActivity()로 화면이 전환됩니다.



> setTextChangedListener()
````
    private fun setTextChangedListener() {
        editTextArray.forEach { editText ->
            editText.addTextChangedListener {
                editText.setErrorMessage()
                setConfirmButtonEnable()
            }
        }
    }
````
EditText의 값 변경 리스너 함수: EditText의 값이 변경될때마다 실행되는 함수입니다.



> setOnFocusChangedListener()
````
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
````
EditText의 포커스 변경 리스너 함수 : EditText의 focus가 변경될때마다 실행되는 함수입니다.

EditText가 수정이 될 떄마다 EditText에 입력된 값이 유효한지 확인해 줍니다.

이 확인 작업은 editText.setErrorMessage() 에서 실행됩니다.

유효한 경우에는 다음 버튼이 활성화되어야 하기 때문에 setConfirmButtonEnable() 메소드 또한 실행됩니다.





> EditText.setErrorMessage()

 ````
private fun EditText.setErrorMessage() {
        when (this) {
            etSignUpName -> error = getMessageValidName()
            etSignUpId -> error = getMessageValidId()
            etSignUpPassword -> error = getMessageValidPassword()
            etSignUpPassword2 -> error = getMessageValidPasswordConfirm()

            else -> Unit
        }
    }
````
EditText에서 에러 메세지를 출력하기 위해 만든 확장함수입니다. 

error = 문자열 메세지 이름 을 사용해주면 TextView 오른쪽에 빨간 동그라미와 함께 에러 메세지가 표시됩니다.

각각의 EditText에 해당되는 유효성체크를 getMessageValidName() & getMessageValidId() & getMessageValidPassword() & getMessageValidPasswordConfirm() 에서 해주고 
유효하지 않은 경우에는 error에 문자열을 넘기고 유효한 값이라면 error에 null을 전달하여줍니다.




#### getMessageValidName() & getMessageValidId() & getMessageValidPassword() & getMessageValidPasswordConfirm()

이름 / 아이디 / 비밀번호 / 비밀번호 확인 의 입력값이 옳지 않거나 비어있을 경우 해당되는 에러 메세지를 각각 반환하는 함수입니다.

이름 : 한글만 입력 가능

아이디 : 영문 소문자 또는 숫자 (기존 등록된 아이디와 중복될 수 없음)

비밀번호 : 8~16자, 영대소문자 및 특수문자 최소 1개 이상 포함



> getMessageValidId()
````
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
````

에러메세지를 모두 enum class 에 저장하여 에러 메세지를 모두 한 파일에서 관리할 수 있도록 했습니다.



> enum class

````
enum class SignUpErrorMessage(
    @StringRes val message: Int,
) {
    EMPTY_NAME(R.string.empty_name_message),
    EMPTY_ID(R.string.empty_id_message),
    EMPTY_PASSWORD(R.string.empty_password_message),

    INVIALID_NAME(R.string.name_error_message),
    INVALID_ID(R.string.id_error_message),
    INVALID_PASSWORD(R.string.password_error_message),

    PASSWORD_MISMATCH(R.string.password_check_error_message),
    OVERLAPPING_ID(R.string.overlapping_id),
}
````




> setConfirmButtonEnable()

    private fun setConfirmButtonEnable() {
        btnSignUpNext.isEnabled = getMessageValidName() == null
                && getMessageValidId() == null
                && getMessageValidPassword() == null
                && getMessageValidPasswordConfirm() == null
    }
위 모든 에러 메세지가 표시되지 않는경우에는 다음 버튼이 활성화됩니다.
