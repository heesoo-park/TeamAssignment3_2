# SignInPageActivity.md

1. 로그인 페이지


## (1) Layout : [activity_sign_in.xml](https://github.com/heesoo-park/TeamAssignment3_2/blob/dev/app/src/main/res/layout/activity_sign_in.xml) & [activity_sign_in.xml-(land)](https://github.com/heesoo-park/TeamAssignment3_2/blob/dev/app/src/main/res/layout-land/activity_sign_in.xml)

### - Constraint Layout

요즘 Constraint Layout을 사용하는게 대세라고 튜터님께서 말씀해주셨어서 Constraint Layout을 사용하였습니다.

Landscape View에서는 위젯들이 다 안들어가기 때문에 ScrollView를 사용하여 스크롤하여 모든 위젯들에 접근 가능하도록 했습니다.

### - EditText

EditText는 inputType을 지정해줌으로써 EditText 활성화 시 적절한 키보드가 작동하도록 했습니다. 

비밀번호 inputType을 passwordText를 지정해줌으로써 EditText가 활성화 시 문자 키보드가 활성화 되고 입력되는 값들이 자동으로 가려집니다.

EditText에 Hint 텍스트 값을 부여해줌으로써 텍스트가 비어있고 비활성화 되어 있는 경우에 EditText에 텍스트가 표시될 수 있도록 만들었습니다.


## (2) Activity : [SignInPageActivity.kt](https://github.com/heesoo-park/TeamAssignment3_2/blob/dev/app/src/main/java/com/example/teamsns/SignInActivity.kt)

### 객체

````
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

````
회원가입 페이지에서 입력된 값을 로그인 페이지로 넘겨 줄 수 있도록 resultLauncher 또한 지연 초기화 해 주었습니다.

### 메소드

>initView()
````
    private fun init() {
        activityResultLauncher()
        btnLogin()
        btnSignup()
    }
````

코드의 가독성을 높이기 위해 onCreate에서 실행되는 초기화면 함수들을 따로 모아두었습니다.

> activityResultLauncher()

````
    private fun activityResultLauncher() {
        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == RESULT_OK) {
                    val userId = it.data?.getStringExtra("id") ?: ""
                    val userPw = it.data?.getStringExtra("pw") ?: ""
                    etSignInId.setText(userId)
                    etSignInPw.setText(userPw)
                }
            }
    }
````

이 전 페이지에서 넘겨주었던 값을 불러옵니다. 

즉 회원가입 페이지에서 작성해두었던 아이디와 비밀번호를 가져와 자동 입력시켜줍니다.

> btnLogin()
````
 private fun btnLogin() {
        btnSignInLogIn.setOnClickListener{
            val userData = getUser(etSignInId.text.toString())
            when {
                etSignInId.text.toString().trim().isEmpty() -> {
                    Toast.makeText(this, SignUpErrorMessage.EMPTY_ID.message, Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                etSignInPw.text.toString().trim().isEmpty() -> {
                    Toast.makeText(this, SignUpErrorMessage.EMPTY_PASSWORD.message, Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                (userData == null) -> {
                    Toast.makeText(this, SignUpErrorMessage.PASSWORD_MISMATCH.message, Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                (userData.password != etSignInPw.text.toString()) -> {
                    Toast.makeText(this, SignUpErrorMessage.PASSWORD_MISMATCH.message, Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }

            val intent = Intent(this, MainPageActivity::class.java)

            intent.putExtra("id", etSignInId.text.toString())
            intent.putExtra("pw", etSignInPw.text.toString())

            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_from_bottom, R.anim.slide_out_to_top)
        }
    }
````

로그인 버튼 클릭 리스너 함수: 로그인 버튼을 클릭했을 때 EditText 의 텍스트를 확인합니다.
만약 EditText의 텍스트가 부적절한 경우 토스트 메세지를 띄웁니다.

EditText가 비어있는 경우 토스트 메세지를 띄웁니다.

회원가입이 되어있지 않는 경우에도 토스트 메세지를 띄웁니다.

비밀번호가 틀린 경우에도 토스트 메세지를 띄웁니다.

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



> btnSignup()

````
    private fun btnSignup() {
        btnSignInSignUp.setOnClickListener{
            val intent = Intent(this, SignUpActivity::class.java)
            activityResultLauncher.launch(intent)
            overridePendingTransition(R.anim.slide_in_from_bottom, R.anim.slide_out_to_top)
        }
    }
````

회원가입 버튼을 누르면 회원가입 페이지로 이동합니다.
