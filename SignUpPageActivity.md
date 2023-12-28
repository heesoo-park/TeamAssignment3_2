# SignUpPageActivity.md

![image](https://github.com/heesoo-park/TeamAssignment3_2/assets/148201041/9746921e-dfd4-429e-a4bd-d8bd24e3f03b)


## (1) Layout : [activity_sign_up.xml](https://github.com/heesoo-park/TeamAssignment3_2/blob/dev/app/src/main/res/layout/activity_sign_up.xml) & [activity_sign_up.xml-(land)](https://github.com/heesoo-park/TeamAssignment3_2/blob/dev/app/src/main/res/layout-land/activity_sign_up.xml)

### - Constraint Layout

요즘 Constraint Layout을 사용하는게 대세라고 튜터님께서 말씀해주셨어서 Constraint Layout을 사용하였습니다.

Landscape View에서는 위젯들이 다 안들어가기 때문에 ScrollView를 사용하여 스크롤하여 모든 위젯들에 접근 가능하도록 했습니다.

### - EditText

EditText는 inputType을 지정해줌으로써 EditText 활성화 시 적절한 키보드가 작동하도록 했습니다. 

비밀번호 inputType을 passwordText를 지정해줌으로써 EditText가 활성화 시 문자 키보드가 활성화 되고 입력되는 값들이 자동으로 가려집니다.

EditText에 Hint 텍스트 값을 부여해줌으로써 텍스트가 비어있고 비활성화 되어 있는 경우에 EditText에 텍스트가 표시될 수 있도록 만들었습니다.

