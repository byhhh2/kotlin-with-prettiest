## Build a basic layout

<br>

### Layout Editor

- `res` > `layout` : 화면 레이아웃
- `activity_main.xml` : 화면 레이아웃에 관한 설명 포함

<br>

### palette

- 좌측 상단
- 드롭다운으로 요소 추가 가능

- `TextView` 추가
  - 배치
    - 제약조건을 추가하여 뷰의 위치를 제한
    - 요소를 클릭하고 우측에 `Layout` 부분에서 `+` 버튼을 통해 제약조건을 추가 가능

<br>

### app에 변경사항 적용

- `ctrl + Alt + F10` : 앱을 다시 시작
- `ctrl + F10` : 코드 변경사항만 적용

<br>

### 텍스트에 스타일 추가

- `Atrributes` > `Common Attributes` > `textAppearance`

<br>

### 정리

- Layout Editor를 사용하면 Android 앱용 UI를 만들 수 있습니다.
- 앱 화면에 표시되는 대부분의 내용은 View입니다.
- TextView는 앱에서 텍스트를 표시하는 UI 요소입니다.
- ConstraintLayout은 다른 UI 요소의 컨테이너입니다.
- Views는 ConstraintLayout 내에서 가로와 세로로 제한되어야 합니다.
- View를 배치하는 한 가지 방법은 여백을 사용하는 것입니다.
- 여백을 통해 View가 컨테이너의 가장자리에서 떨어진 정도를 설정할 수 있습니다.
- TextView에 글꼴, 텍스트 크기, 색상과 같은 속성을 설정할 수 있습니다.

### 이미지 삽입

- `Resource Manager` > `+` > `import drawbles`
- 경로는 `res` > `drawble`

### 정리

- Android 스튜디오의 Resource Manager를 사용하면 이미지와 기타 리소스를 추가하고 구성할 수 있습니다.
- `ImageView`는 앱에서 이미지를 표시하는 UI 요소입니다.
- `ImageViews`에는 앱의 접근성을 개선할 수 있는 콘텐츠 설명이 있어야 합니다.
- 생일 축하 메시지와 같이 사용자에게 표시되는 텍스트는 앱을 다른 언어로 쉽게 번역할 수 있도록 문자열 리소스로 추출해야 합니다.
