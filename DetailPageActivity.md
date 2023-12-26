DetailPageActivity
=
레이아웃 구성
![image](https://github.com/heesoo-park/TeamAssignment3_2/assets/116724657/f0ad9d78-554f-4a69-b686-904dde3b1baf)

리니어를 기본으로 그 안에

ConstraintLayout 상단

ScrollView{
    Linear{ 이미지
        contraint{ 이미지 , 텍스트뷰}
    }
}
로 구성했다

왼쪽 상단의 <- 이미지를 누르면 홈 액티비티로 넘어갈건데

```xml
    <item android:drawable="@drawable/back_arrow2" android:state_pressed="true"/>
    <item android:drawable="@drawable/back_arrow"/>
```
리소스 파일을 만들어서 눌렀을때 화살표 이미지의 색이 변하도록 만들었다.


프로필 이미지의 둥근 모양은 CardView를 사용했다

```xml
<androidx.cardview.widget.CardView
            android:id="@+id/imageView"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginStart="30dp"
            android:layout_marginTop="30dp"
            app:cardCornerRadius="100dp"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toTopOf="parent">

            <androidx.appcompat.widget.AppCompatImageView
                android:id="@+id/profile_img"
                android:layout_width="100dp"
                android:layout_height="100dp"
                tools:srcCompat="@tools:sample/avatars" />
        </androidx.cardview.widget.CardView>
```
Cardview를 둥글게 만들고 그 안에 이미지를 넣어 만들었다.
