별개냥
=

## 프로젝트 개요

### 프로젝트 팀원
박희수, 심규상, 김은이, 최성현

### 앱 이름
별개냥

### 앱 이름의 의미
강아지🐶와 고양이😺의 의미가 들어가 있으면서 인⭐그램의 '별'의 느낌도 가져가 익숙한 sns 느낌인데 강아지와 고양이가 주가 되는 sns임을 나타내는 동시에
'별거냐~'라는 의미도 가진다.

### 앱의 목적
자신의 펫이 주체가 되어 앱 내에서 재밌고 자유롭게 이야기를 공유하는 공간이 되는 것

### 프로젝트 일정
23/12/26 ~ 23/12/29

### 프로젝트 기획
먼저 팀노션에서 자유롭게 아이디어 브레인스토밍을 하고 
앱의 컨셉, 레퍼런스가 잡히고 나서 Figma를 가지고 레이아웃에 대한 의견을 나누고 결정했다.

<Figma 링크>

https://www.figma.com/file/W77t6eKPMSJsTW6WhqeqCJ/%EB%B3%84%EA%B0%9C%EB%83%A5?type=design&mode=design&t=WLSCOLn5fvtgA33U-1

### 역할 분담
박희수 : 사용자 정보 저장소 구현 & 메인 페이지 구현 & 프로필 이미지 선택 페이지 구현 & 발표 & 가로모드 구현

심규상 : 디테일 페이지 & 마이 페이지 구현 & 다크 모드 구현 & 가로 모드 구현 & 코드 최적화

김은이 : 로그인 & 회원가입 페이지 구현 & 애니메이션 효과 구현 & Figma UI 관리

최성현 : 메인 페이지 구현 & 원형 이미지뷰 구현

공통 : 문자열 관리(user_id1), 뷰 객체 이름(tv_signup_id), 깃허브 브랜치 & 풀 리퀘스트 관리

### 코드 컨벤션
베이스는 Kotlin 스타일 가이드에 맞춰 진행했다.

가장 기본적인 부분에 대한 코드 컨벤션을 다음과 같이 설정했다.

|제목|내용|
|------|---|
|클래스 이름|PascalCase|
|함수 이름|camelCase|
|변수 이름|camelCase|
|문자열 리소스 이름|snake_case|
|문자열 이름|(페이지_)용도|
|레이아웃 이름|속성_페이지_위치|
|뷰 이름|속성_페이지_용도(_사용방법)|
|리스트 내 변수|엔터로 정리|
|enum class 내 변수 이름|모두 다 대문자|
|drawable 내 이미지 이름|img(_위치)_용도|
|drawable 내 셀렉터 이름|selector(_위치)_용도|
|drawable 내 아이콘 이름|ic(_위치)_용도|
|by lazy|엔터로 구분|
|조건이 3개 이상일 때 |when 사용|
|코드 한 줄을 여러 줄로 작성하는 경우|안드로이드 스튜디오의 흰 줄을 넘어가는 경우|
|기본적인 코드 정리|CTRL + ALT + L|

### 협업 관련 사항 조정
**안드로이드 스튜디오** 사용

에뮬레이터는 **Nexus 5 API 31** 사용

**풀 리퀘스트** 전에는 말해주기 - 
  풀 리퀘스트에는 최대한 이름과 내용을 자세히 기록하고 팀원을 리뷰어로 등록해 최소 1명에게 확인받은 후 merge 진행

**Readme.md**를 이용해서 개발 기록 작성


[UserDatabase](https://github.com/heesoo-park/TeamAssignment3_2/blob/dev/UserDatabase.md)

[User](https://github.com/heesoo-park/TeamAssignment3_2/blob/dev/User.md)

[Post](https://github.com/heesoo-park/TeamAssignment3_2/blob/dev/Post.md)


## [로그인 페이지](https://github.com/heesoo-park/TeamAssignment3_2/blob/dev/SignUpPageActivity.md)
![image](https://github.com/heesoo-park/TeamAssignment3_2/assets/116724657/b6906e7f-3cba-42c7-989f-489c7e59ca54) ![image](https://github.com/heesoo-park/TeamAssignment3_2/assets/116724657/fc333dbe-21de-4f68-a509-18d6a8dbed9c)

사용자의 정보를 DB에서 찾아 가입된 사용자인지 확인 하는 액티비티

가입 되지 않은 사용자는 회원가입 링크로 이동가능하다


![Honeycam 2023-12-28 21-02-03](https://github.com/heesoo-park/TeamAssignment3_2/assets/116724657/f91dcc1d-bb90-4da1-8d7b-ce66d1bce24c)

가로 모드에선 양옆의 여백을 좀 더 줬고

상하 여백을 줄였습니다

한 화면에 전부 띄우지 못해 스크롤 뷰를 추가해서 사용자가 스크롤 할 수 있게끔 구현했다

## [회원가입 페이지](https://github.com/heesoo-park/TeamAssignment3_2/blob/dev/SignUpPageActivity.md)
![image](https://github.com/heesoo-park/TeamAssignment3_2/assets/116724657/9d7a6710-e9dd-42d2-964a-92cf67e14217) ![image](https://github.com/heesoo-park/TeamAssignment3_2/assets/116724657/cce9bf58-d3cd-4074-9d40-0d34b78ae313)

데이터베이스에 유저의 정보를 입력하는 페이지

유효성검사로 중복된 아이디를 체크하고

비밀번호등 조건에 맞으면 회원의 정보를 저장한다

![image](https://github.com/heesoo-park/TeamAssignment3_2/assets/116724657/84c0cd18-6ce1-48ad-b6e5-0338dbce5fe4)

가로 모드에서 로그인페이지와 같이 양옆 여백을 좀 더 줬고

스크롤 뷰로 스크롤 가능하게 구현했다

### [프로필 사진 선택 페이지](https://github.com/heesoo-park/TeamAssignment3_2/blob/dev/ChooseProfileActivity.md)
![Honeycam 2023-12-28 21-13-09](https://github.com/heesoo-park/TeamAssignment3_2/assets/116724657/050885db-2a1c-4a39-b432-95d19e451a74)


계정 생성시 프로필 사진을 생성하는 페이지

목록에서 사진을 선택하면 크게 보여주고 수정하기를 선택시 생성되는 계정의 프로필사진으로 저장

![image](https://github.com/heesoo-park/TeamAssignment3_2/assets/116724657/4ccbc03f-ef97-4bc4-a6a4-63540701fe50)

가로모드에선 사진을 오른쪽에서 보여주게 구현

## 메인 페이지
![image](https://github.com/heesoo-park/TeamAssignment3_2/assets/116724657/9d6b470c-fe88-4b2e-a8c8-3ca419e290b2) ![image](https://github.com/heesoo-park/TeamAssignment3_2/assets/116724657/d9dae805-c79c-4e45-a66c-55bd81f8b832)

화면 상단에는 왼쪽에 로고 오른쪽에 사용자 프로필이 있다

그 밑에는 다른 유저들의 프로필이 있는데 본인의 프로필의 테두리는 파란색으로 구별이 되게 했다
![Honeycam 2023-12-28 21-38-04](https://github.com/heesoo-park/TeamAssignment3_2/assets/116724657/7fddc7b5-97ac-4b38-be90-90c39ba18776)

스크롤 기능도 넣었는데

프로필은 가로로 게시물들은 세로로 스크롤해서 볼수 있다

![Honeycam 2023-12-28 21-40-54](https://github.com/heesoo-park/TeamAssignment3_2/assets/116724657/b7fc1748-c0b9-430e-b7e0-276f1e1a72c1)

좋아요 버튼과 더보기 버튼도 구현했는데

좋아요를 누르지 않은 게시물에 좋아요를 누르면 숫자가 추가되고 이미지가 변한다

게시물의 글이 길다면 더보기 버튼이 활성화되면서

클릭시 게시물을 늘려서 볼 수 있게 된다

![image](https://github.com/heesoo-park/TeamAssignment3_2/assets/116724657/d35823e0-8e2f-4890-9012-383f558a0b74)

가로모드는 이런식으로 구현했는데

게시물의 내용을 좀더 길게 볼수 있게 했다.

## [디테일 페이지](https://github.com/heesoo-park/TeamAssignment3_2/blob/dev/DetailPageActivity.md)
![image](https://github.com/heesoo-park/TeamAssignment3_2/assets/116724657/ee367f08-6fa9-4a3f-8061-d33b0a4bfbef) ![image](https://github.com/heesoo-park/TeamAssignment3_2/assets/116724657/2978b9c5-ebff-4464-8fed-58e29e7f1692)


메인 페이지에서 사용자들의 프로필을 누르면 이동하게 된다

입력받은 사용자 정보를 바탕으로 구성되어 있으며

사용자의 아이디 이름 상태메세지가 화면 상단에 출력되고

아래엔 사용자가 게시한 게시물들이 출력된다

![image](https://github.com/heesoo-park/TeamAssignment3_2/assets/116724657/05131199-6562-4902-9a8c-e27a1cb62a99)

로그인한 사용자가 본인이라면 편집 버튼과 로그아웃 버튼이 나타나게 된다

편집버튼으로 이동하는 편집 페이지는 회원가입할때 사용한 페이지를 재사용해서 만들었다

![image](https://github.com/heesoo-park/TeamAssignment3_2/assets/116724657/23fc9189-94c0-4ecc-8bd4-f62e76332dfd)

가로모드의 구성은 메인페이지와 유사하다

### 편집 페이지
![image](https://github.com/heesoo-park/TeamAssignment3_2/assets/116724657/4201dbb1-4034-4286-ad74-d80a8926a8f7)

회원가입 페이지를 재사용 했는데

ID입력칸을 상태메세지 입력창으로 바꿧다

사용자의 이름 상태 비밀번호 그리고 프로필 이미지를 수정할 수 있다.
